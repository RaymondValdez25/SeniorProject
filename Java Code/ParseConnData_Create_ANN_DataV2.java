import java.io.*; 
import java.util.*; 

public class ParseConnData_Create_ANN_DataV2 {

   // Parameters when running:
   // "windows" Indicates program will run in windows environment, change path

   // Update the following when running in a different environment.
   public static final String LINUX_PATH    = "/mnt/c/Class/";
   public static final String WINDOWS_PATH  = "..";
   public static final String DATA_FOLDER   = "Output";
   public static final String OUTPUT_FOLDER = "Output";
   public static final String INPUT_FILE    = "FinalGraph_V4.csv";

   public static final String ONE_HOT_1     = "1,0,0";
   public static final String ONE_HOT_2     = "0,1,0";
   public static final String ONE_HOT_3     = "0,0,1";

   private static LinkedHashMap<String,Integer> rootCountMap  = new LinkedHashMap<>();
   private static LinkedHashMap<String,Integer> portCountMinMap  = new LinkedHashMap<>();
   private static LinkedHashMap<String,Integer> portCountMaxMap  = new LinkedHashMap<>();
   
   private static Integer minRootCount = 1;
   private static Integer maxRootCount = 0;
   private static Integer minPortCount = 1;
   private static Integer maxPortCount = 0;

   public static void main(String[] args) {

      Boolean platformWindows = false;
      
      System.out.println("==========================================================");
      
      // Process parameters, determine filters, and environment.
      if (args.length > 0) {
         for (String param : args) {
            if (param.equalsIgnoreCase("windows")) {
               platformWindows = true;
               System.out.println("Platform = Windows");
            }
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
      file = new File(dataPath + INPUT_FILE);
      
      File outFile1;
      outFile1 = new File(outputPath + "Output_NN_In_V7.csv");
      
      File outFile2;
      outFile2 = new File(outputPath + "Output_OneHot_V7.csv");
      
      // rootIP    - 0
      // srcIP     - 1
      // dstIP     - 2
      // count     - 3
      // dur       - 4
      // bytes     - 5
      // hops      - 6
      // tactic    - 7
      // nCount    - 8
      // nDur      - 9 
      // nBytes    - 10
      // portCount - 11
      // end       - 12
      
      // Pre-read the file and create a table of counts based on the rootIP.
      // This data will be used to generate the outDegree value of the rootIP
      // for all the associated edges that belong to that graph.
      
      try {         
         System.out.println("Preprocessing...\n");
         
         bufReader  = new BufferedReader(new FileReader(file));
         String line = bufReader.readLine();

         while((line=bufReader.readLine())!=null) {
            String[] strArr = line.split(",");
            String key = strArr[0];
            Integer portCount = 0;
            if(!strArr[11].equals(""))
               portCount = Integer.valueOf(strArr[11]);
            if(!rootCountMap.containsKey(key)) {
               rootCountMap.put(key, 1);
               portCountMinMap.put(key, portCount);
               portCountMaxMap.put(key, portCount);
            } else {
               rootCountMap.put(key, rootCountMap.get(key) + 1);
               if(rootCountMap.get(key) + 1 > maxRootCount)
                  maxRootCount = rootCountMap.get(key) + 1;
               if(portCount < portCountMinMap.get(key))
                  portCountMinMap.put(key, portCount);
               if(portCount > portCountMaxMap.get(key))
                  portCountMaxMap.put(key, portCount);
            }
            if(portCount > maxPortCount) maxPortCount = portCount;
            if(portCount < minPortCount) minPortCount = portCount;
         }
         
         bufReader.close();
      } catch (Exception e) { 
         e.printStackTrace(); 
         System.out.println(e);
         return; 
      }
      
      System.out.println("==========================================================");
      System.out.println("rootCount min:" + minRootCount + " max:" + maxRootCount);
      System.out.println("portCount min:" + minPortCount + " max:" + maxPortCount);
      
      // Read the input file again and generate the output files.
      try { 
         bufReader = new BufferedReader(new FileReader(file));
         String line = bufReader.readLine();
         
         FileOutputStream outStream1 = new FileOutputStream(outFile1);
         BufferedWriter bufWriter1   = new BufferedWriter(new OutputStreamWriter(outStream1));
         
         FileOutputStream outStream2 = new FileOutputStream(outFile2);
         BufferedWriter bufWriter2   = new BufferedWriter(new OutputStreamWriter(outStream2));
         
         int recCount  = 0,
             usedCount = 0;
         
         Boolean useRecord;
         Double  nCount,
                 nDur,
                 nBytes,
                 nVerticeCount,
                 nPortCount;
         String  tactic = "",
                 oneHot = "";
         
         while((line=bufReader.readLine())!=null) {
            String[] strArr = line.split(",");
            
            useRecord     = false;
            nCount        = 0.0;
            nDur          = 0.0;
            nBytes        = 0.0;
            nVerticeCount = 0.0;
            nPortCount    = 0.0;
            
            try {
               if(!strArr[7].equals(""))
                  tactic = strArr[7];
               
               if(tactic.equalsIgnoreCase("none")) {
                  useRecord = true;
                  oneHot = ONE_HOT_1;
               } else if(tactic.equalsIgnoreCase("Reconnaissance")) {
                  useRecord = true;
                  oneHot = ONE_HOT_2;
               } else if(tactic.equalsIgnoreCase("Discovery")) {
                  useRecord = true;
                  oneHot = ONE_HOT_3;
               }
               
               if(useRecord) {
                  usedCount++;
                  String key = strArr[0];
                  Integer rootCount = 0;
                  Integer portCount = 0;
                  if(rootCountMap.containsKey(key)) {
                     rootCount = rootCountMap.get(key);
                  }
                  if(!strArr[8].equals(""))
                     nCount = Double.valueOf(strArr[8]);
                  if(!strArr[9].equals(""))
                     nDur = Double.valueOf(strArr[9]);
                  if(!strArr[10].equals(""))
                     nBytes = Double.valueOf(strArr[10]);
                  if(!strArr[11].equals(""))
                     portCount = Integer.valueOf(strArr[11]);
                  String lineOut = String.format("%.20f", getNormalizedRootCount(rootCount)) + "," +
                                   String.format("%.20f", nCount) + "," +
                                   String.format("%.20f", nDur)   + "," +
                                   String.format("%.20f", nBytes) + "," +
                                   String.format("%.20f", getNormalizedPortCount(portCount));

                  if(usedCount==1) {
                     bufWriter1.write("[[" + lineOut + "]");
                     bufWriter2.write("[[" + oneHot + "]");
                  } else {
                     bufWriter1.write(",\n[" + lineOut + "]");
                     bufWriter2.write(",\n[" + oneHot + "]");
                  }
                  
                  
               }
            } catch (Exception e) { 
               //System.out.println(strArr[6]);
            }
            
            recCount++;
         
         } // end while not EOF
         
         bufWriter1.write("]\n");
         bufWriter2.write("]\n");
         
         bufWriter1.close();
         bufWriter2.close();
         
         bufReader.close();
         
         System.out.println("Records processed: " + recCount);
         System.out.println("     Records kept: " + usedCount);
            
      } catch (Exception e) { 
         e.printStackTrace(); 
         System.out.println(e);
         return; 
      }

      System.out.println("==========================================================");
   }
   
   public static Double getNormalizedRootCount(int count) {
      return (double)(count - minRootCount) / (maxRootCount - minRootCount);
   }
   
   public static Double getNormalizedPortCount(int count) {
      return (double)(count - minPortCount) / (maxPortCount - minPortCount);
   }
}