import java.io.*; 
import java.util.*; 
 
/*
0:resp_pkts
1:service
2:orig_ip_bytes
3:local_resp
4:missed_bytes
5:proto
6:duration
7:conn_state
8:dest_ip_zeek
9:orig_pkts
10:community_id
11:resp_ip_bytes
12:dest_port_zeek
13:orig_bytes
14:local_orig
15:datetime
16:history
17:resp_bytes
18:uid
19:src_port_zeek
20:ts
21:src_ip_zeek
22:label_tactic
*/

public class ParseConnData_FollowHopsV3 {

   // Parameters when running:
   // "trunc"   Indicates input should be the file listed in TRUNC_DATASET
   // "IP="     Process only those records where IP = source/destination IP
   // "not"     Invert filtering on the tactic provided.  Tactic must be provided.
   // "windows" Indicates program will run in windows environment, change path
   // tactic    If not any of the words above, then parameter is assumed to be the
   //           tactic you wish to filter for.  Use "not" to filter for all tactics
   //           that are not the tactic provided.
   //
   // If a tactic is not provided then no filtering is done and all records processed

   // Update the following when running in a different environment.
   public static final String LINUX_PATH    = "/mnt/c/Class/";
   public static final String WINDOWS_PATH  = "..";
   public static final String DATA_FOLDER   = "Data";
   public static final String OUTPUT_FOLDER = "Output";
   public static final String FULL_DATASET  = "full_dataset.csv";
   public static final String TRUNC_DATASET = "truncated_dataset.csv";

   // Store/accumulate various attributes from the input data file.
   private static LinkedHashMap<String,String>  srcMap          = new LinkedHashMap<>();
   private static LinkedHashMap<String,String>  nodeMap         = new LinkedHashMap<>();
   private static LinkedHashMap<String,Integer> srcDstCountMap  = new LinkedHashMap<>();
   private static LinkedHashMap<String,Double>  srcDstDurMap    = new LinkedHashMap<>();
   private static LinkedHashMap<String,Double>  srcDstBytesMap  = new LinkedHashMap<>();
   private static LinkedHashMap<String,String>  srcDstTacticMap = new LinkedHashMap<>();
   private static LinkedHashMap<String,ArrayList<String>> srcDstPortMap
                                                                = new LinkedHashMap<>();
   private static LinkedHashMap<String,Vertice> finalGraph      = new LinkedHashMap<>();

   // Helper function to trim node IDs.
   public static String right(String value, int length) {
      return value.substring(value.length() - length);
   }

   public static void main(String[] args) {

      // Various items to capture data for process timings.
      long fileProcTimeStart, fileProcTimeEnd;
      long graphProcTimeStart, graphProcTimeEnd;
      long totalTime;
      
      // Various items to manage chosen parameters.
      String  tacticToFind    = "";
      String  IPtoFilterFor   = "";
      
      Boolean invertTactic    = false;
      Boolean fullDataset     = true;
      Boolean filterByIP      = false;
      Boolean platformWindows = false;
      Boolean useRecord       = false;
      
      // Keep track of any edges dropped as a result of cycles.
      int     droppedEdges    = 0;

      System.out.println("==========================================================");
      
      // Process parameters, determine filters, and environment.
      if (args.length > 0) {
         for (String param : args) {
            if (param.equalsIgnoreCase("trunc"))
               fullDataset   = false;
            else if (param.equalsIgnoreCase("not"))
               invertTactic  = true;
            else if (param.startsWith("IP=")) {  // equal used because colon is used in IPV6
               filterByIP    = true;
               String[] temp = param.split("=");
               if (temp.length != 2) {
                  System.out.println("IP parameter must be followed by an IP address!");
                  System.exit(0);
               }
               IPtoFilterFor = temp[1];
            System.out.println("Filtering on IP = [" + IPtoFilterFor + "]");
            } else if (param.equalsIgnoreCase("windows")) {
               platformWindows = true;
               System.out.println("Platform = Windows");
            }
            else
               tacticToFind = param;
         }
      }
      
      if(tacticToFind.equals("")) {
         System.out.println("Tactic to find = all");
      } else {
         if(invertTactic) {
            System.out.println("Tactics excluding: " + tacticToFind);
         } else {
            System.out.println("Only tactic: " + tacticToFind);
         }
      }

      // Process input data file
      BufferedReader bufReader = null; 
   
      String dataPath   = "",
             outputPath = "";
      if(platformWindows)
      {
         dataPath   = WINDOWS_PATH + "\\" + DATA_FOLDER + "\\";
         outputPath = WINDOWS_PATH + "\\" + OUTPUT_FOLDER + "\\";
      } else {
         dataPath   = LINUX_PATH + "/" + DATA_FOLDER + "/";
         outputPath = LINUX_PATH + "/" + OUTPUT_FOLDER + "/";
      }
      
      File file;
      if(fullDataset) {
         file = new File(dataPath + FULL_DATASET);
         System.out.println("Using full data.");
      } else {
         file = new File(dataPath + TRUNC_DATASET);
         System.out.println("Using truncated data.");
      }
      
      System.out.println("==========================================================");      
      fileProcTimeStart = System.currentTimeMillis();
      
      try { 
         bufReader = new BufferedReader(new FileReader(file));
         
         String line = bufReader.readLine();
         
         int nodeID = -1;
         int recCount = 0;
         while((line=bufReader.readLine())!=null) {
            String[] strArr = line.split(",");
            Double dur = 0.0;
            Double connBytes = 0.0;
            
            try {
               if(!strArr[6].equals(""))
                  dur = Double.valueOf(strArr[6]);
               if(!strArr[13].equals(""))
                  connBytes = Double.valueOf(strArr[13]);
            } catch (Exception e) { 
               //System.out.println(strArr[6]);
            }
            
            String tactic    = strArr[22];
            String src       = strArr[21];    // src_ip
            String src_port  = strArr[19];    // src_port
            String dst       = strArr[8];     // dst_ip
            String dst_port  = strArr[12];    // dst_port
            String bytes     = strArr[13];    // bytes
            String date_time = strArr[15];
            
            if(tacticToFind.equals("") || (invertTactic && !tacticToFind.equalsIgnoreCase(tactic))
                                       || (tacticToFind.equalsIgnoreCase(tactic))                ) {
               useRecord = false;
               
               if(!filterByIP || (filterByIP && src.equalsIgnoreCase(IPtoFilterFor)))
                  useRecord = true;
               
               if(useRecord) {
                  String key = src + "," + dst;
                  
                  // Keep track of the unique nodes.
                  if(!nodeMap.containsKey(src)) {
                     nodeID++;
                     nodeMap.put(src, right("000"+String.valueOf(nodeID),4));
                  }
                  if(!nodeMap.containsKey(dst)) {
                     nodeID++;
                     nodeMap.put(dst, right("000"+String.valueOf(nodeID),4));
                  }
            
                  // Unique src -> dst connections.
                  if(!srcDstCountMap.containsKey(key)) {
                     // New key, initialize accumulation with current record values
                     System.out.println (key + " added.");
                     srcDstCountMap.put (key, 1);
                     srcDstDurMap.put   (key,dur);
                     srcDstBytesMap.put (key,connBytes);
                     srcDstTacticMap.put(key,tactic); // Only one tactic allowed per key
                     ArrayList<String> portList = new ArrayList<>();
                     portList.add(dst_port);
                     srcDstPortMap.put  (key,portList);
                     
                     String newValue = srcMap.get(src);
                     if(newValue == null)
                        newValue = dst;
                     else
                        newValue += "," + dst;
                     srcMap.put(src, newValue);
                     
                  } else {
                     // Key seen before, add record to existing accumulations
                     srcDstCountMap.put (key, srcDstCountMap.get(key)+1);
                     srcDstDurMap.put   (key, srcDstDurMap.get(key) + dur);
                     srcDstBytesMap.put (key, srcDstBytesMap.get(key) + connBytes);
                     ArrayList<String> portList = srcDstPortMap.get(key);
                     if(!portList.contains(dst_port))
                        portList.add(dst_port);
                     srcDstPortMap.put(key, portList);
                     //test change
                  }
               }
            }
            
            recCount++;
         
         } // end while not EOF
            
         bufReader.close();
         fileProcTimeEnd = System.currentTimeMillis();
         System.out.println("Records processed: " + recCount);
            
      } catch (Exception e) { 
         e.printStackTrace(); 
         System.out.println(e);
         return; 
      }

      System.out.println("==========================================================");
      System.out.println("Number of src lists: " + srcMap.size());
      System.out.println("Number of edges: "     + srcDstCountMap.size()); 
      
      // Save accumulated information to disk - for processing validation.
      try {
         File outFile;
         outFile = new File(outputPath + "SourceToDests_V3.csv");
         
         FileOutputStream outStream = new FileOutputStream(outFile);
      
         BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(outStream));

         // file header
         bufWriter.write("srcIP,dstIP,dstIP\n");
         Integer x = 0;
         
         for(String key : srcMap.keySet()) {
            String[] temp = srcMap.get(key).split(",");
            String line = "[" + nodeMap.get(key) + "]" + key + "->";
            
            for(int d=0; d<temp.length; d++) {
               if(d == 0)
                  line += "["  + nodeMap.get(temp[d]) + "]" + temp[d];
               else
                  line += ",[" + nodeMap.get(temp[d]) + "]" + temp[d];
            }               
            bufWriter.write(line+'\n');
            x++;
         }
         bufWriter.close();
      } catch (Exception e) { 
         e.printStackTrace(); 
         System.out.println(e);
         return; 
      }

      // Turn accumulated data into a graph
      System.out.println("==========================================================");
      System.out.println("Building graph...");

      graphProcTimeStart = System.currentTimeMillis();

      ArrayList<String> newSrcDstList = new ArrayList<>();
      try {
         
         GraphCycle graph = new GraphCycle(nodeMap.size());
      
         //build the graph
         for (String key : srcMap.keySet()) {
            String[] temp = srcMap.get(key).split(",");
            int srcID = Integer.valueOf(nodeMap.get(key));
            for (int d=0; d < temp.length; d++) {
               int dstID = Integer.valueOf(nodeMap.get(temp[d]));
               System.out.print("Adding " + srcID + " -> " + dstID);
               graph.addEdge(srcID, dstID);
               if (graph.isCyclic()) {
                  System.out.println("  Created cycle, removing.");
                  graph.removeEdge(srcID, dstID);
                  droppedEdges++;
               } else {
                  System.out.println("  No cycle found.");
                  newSrcDstList.add(key+","+temp[d]);
               }
            }
         }

         if(graph.isCyclic())
            System.out.println("Graph contains cycle");
         else {
            System.out.println("Graph doesn't contain cycle");
            
            // Write out graph's raw data
            File outFile2;

            String outputFileName = "";
            if(tacticToFind.equals("")) {
               outputFileName = "FinalEdges_All.csv";
            } else {
               if(invertTactic) {
                  outputFileName = "FinalEdges_not_" + tacticToFind + ".csv";
               } else {
                  outputFileName = "FinalEdges_" + tacticToFind + ".csv";
               }
            }
            
            outFile2 = new File(outputPath + outputFileName);
            
            FileOutputStream outStream2 = new FileOutputStream(outFile2);
            BufferedWriter bufWriter2 = new BufferedWriter(new OutputStreamWriter(outStream2));
            bufWriter2.write("srcIP,dstIP,count,dur,bytes,tactic\n");
            
            for(int i=0;i<newSrcDstList.size();i++) {
               String key             = newSrcDstList.get(i);
               String tactic          = "";
               int    connectionCount = 0;
               int    hopCount        = 1;
               double totDuration     = 0.0;
               double totBytes        = 0.0;
               
               if(srcDstCountMap.containsKey(key))
                  connectionCount = srcDstCountMap.get(key);
               if(srcDstDurMap.containsKey(key))
                  totDuration     = srcDstDurMap.get(key);
               if(srcDstBytesMap.containsKey(key))
                  totBytes        = srcDstBytesMap.get(key);
               if(srcDstTacticMap.containsKey(key))
                  tactic          = srcDstTacticMap.get(key);
               
               bufWriter2.write(key+","+connectionCount+","+totDuration+","+totBytes+","+tactic+"\n");
            }
            bufWriter2.close();
         }
         
      } catch (Exception e) { 
         e.printStackTrace(); 
         System.out.println(e);
         return; 
      }
   
      graphProcTimeEnd = System.currentTimeMillis();
   
      // Report on finished process and timings.
      System.out.println("Droped Edges: " + droppedEdges);
   
      System.out.println("==========================================================");
      System.out.println("Producing Final Graph Data: ");
      
      for(int i=0;i<newSrcDstList.size();i++) {
         String key              = newSrcDstList.get(i);
         int    connectionCount  = 0;
         double totDuration      = 0.0;
         double totBytes         = 0.0;
         String tactic           = "";
         ArrayList<String> ports = new ArrayList<>();
         
         String[] temp = key.split(",");
         String srcKey = temp[0];
         String dstKey = temp[1];
         Vertice vSrc, vDst;
         
         if(finalGraph.containsKey(srcKey)) {
            vSrc = finalGraph.get(srcKey);
         } else {
            // new src vertice
            //System.out.println("New src vertice created: " + temp[0]);
            vSrc = new Vertice(srcKey);
            finalGraph.put(srcKey, vSrc);
         }            
         
         if(finalGraph.containsKey(dstKey)) {
            vDst = finalGraph.get(dstKey);
         } else {
            // new dst vertice
            //System.out.println("New dst vertice created: " + temp[1]);
            vDst = new Vertice(dstKey);
            finalGraph.put(dstKey, vDst);
         }
      
         if(srcDstCountMap.containsKey(key))
            connectionCount = srcDstCountMap.get(key);
         if(srcDstDurMap.containsKey(key))
            totDuration = srcDstDurMap.get(key);
         if(srcDstBytesMap.containsKey(key))
            totBytes = srcDstBytesMap.get(key);
         if(srcDstTacticMap.containsKey(key))
            tactic = srcDstTacticMap.get(key);
         if(srcDstPortMap.containsKey(key)) {
            ports  = srcDstPortMap.get(key);
            System.out.println("Ports: "+ports.toString());
         }

         Edge e = new Edge(vSrc, vDst, connectionCount, totDuration, totBytes, tactic);
         e.addPorts(ports);
         //System.out.print("   New edge created: " + e.getSrc().toString() + " -> " + e.getDst().toString());
         vSrc.addEdgeOut(e);
         vDst.addEdgeIn(e);
         //System.out.print("  Added to src and dst!\n");
      }
      
      System.out.println("VerticeCount = " + Vertice.VerticeCount + " EdgeCount="+Edge.EdgeCount);
      System.out.println("Edge count    min/max = " + Edge.getMinCount()    + "/" + Edge.getMaxCount());
      System.out.println("Edge bytes    min/max = " + Edge.getMinBytes()    + "/" + Edge.getMaxBytes());
      System.out.println("Edge duration min/max = " + Edge.getMinDuration() + "/" + Edge.getMaxDuration());
      
      // Dump final graph
      try {
         File outFile;
         outFile = new File(outputPath + "FinalGraph_V4.csv");
         
         FileOutputStream outStream = new FileOutputStream(outFile);
      
         BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(outStream));

         // file header
         bufWriter.write("rootIP,srcIP,dstIP,count,dur,bytes,hops,tactic,nCount,nDur,nBytes,portCount,end");
      
         for(String key : finalGraph.keySet()) {   
            Vertice v = finalGraph.get(key);
            if(v.inDegree() == 0) {
               v.markVisited();
               System.out.println("Vertice " + v.toString() + " is root vertice.");
               Integer hop = 0;
               iteratePath(v.id(), v.getEdgesOut(), hop, bufWriter);
            }
         }
         bufWriter.write("\n");
         bufWriter.close();
         
      } catch (Exception e) { 
         e.printStackTrace(); 
         System.out.println(e);
         return; 
      }

      System.out.println("==========================================================");
      System.out.println("Timings:");
      System.out.println("==========================================================");
   
      totalTime = fileProcTimeEnd - fileProcTimeStart;
      System.out.println("File processing execution time in milliseconds  : " + totalTime);
   
      totalTime = graphProcTimeEnd - graphProcTimeStart;
      System.out.println("Graph processing execution time in milliseconds  : " + totalTime);
 
      System.out.println("==========================================================");
   }
   
   public static void iteratePath(String rootID, List<Edge> edges, Integer hop, BufferedWriter bufWriter) {
      // Iterate through the paths, adding 1 each time a "hop" to another
      // vertice occurs
      
      Boolean outputGenerated = false;
      
      if(edges.size() == 0) {
         System.out.println(" -> null. ");
         try {
            if(outputGenerated)
               bufWriter.write(",Y");
         } catch (Exception ex) { 
               ex.printStackTrace(); 
               System.out.println(ex);
               return; 
         }
         hop = 0;
      } else {
         hop++;
         for(Edge e : edges) {
            try {
               if(!e.traversed()) {
                  outputGenerated = true;
                  bufWriter.write("\n" + rootID +
                     "," + e.getSrc().toString() +
                     "," + e.getDst().toString() +
                     "," + e.getCount().toString() +
                     "," + e.getDuration().toString() +
                     "," + e.getBytes().toString() +
                     "," + hop +
                     "," + e.getTactic() +
                     "," + e.getNormalizedCount().toString() +
                     "," + e.getNormalizedDuration().toString() +
                     "," + e.getNormalizedBytes().toString() +
                     "," + e.getUniquePortCount().toString());
                  if(e.getDst().getEdgesOut().size()>0)
                     bufWriter.write(",N");
               }
               e.markTraversed();
            } catch (Exception ex) { 
               ex.printStackTrace(); 
               System.out.println(ex);
               return; 
            } 
            System.out.print(" [" + e.getCount() + 
                             "," + e.getDuration() +
                             "," + e.getBytes() +
                             "," + e.getTactic() +
                             "] -> " + e.getDst().toString() +
                             "(" + hop + ")");
            iteratePath(rootID, e.getDst().getEdgesOut(), hop, bufWriter);
         }
      }
   }
}