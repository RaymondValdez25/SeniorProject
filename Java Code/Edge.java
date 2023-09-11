import java.util.*;
 
public class Edge {
   
   static int EdgeCount;
   
   // Used for normalization of edge values
   static Integer maxCount        = 0;
   static Integer minCount        = Integer.MAX_VALUE;
   static Double  maxBytes        = 0.0;
   static Double  minBytes        = Double.MAX_VALUE;
   static Double  maxDuration     = 0.0;
   static Double  minDuration     = Double.MAX_VALUE;
   
   private String  id;
   private Vertice src;
   private Vertice dst;
   private Integer count;
   private Double  bytes;
   private Double  duration;
   private String  tactic;
   private Boolean traversed;
   private ArrayList<String> portList;
   
   public Edge() {
      EdgeCount++;
      count    = 0;
      bytes    = 0.0;
      duration = 0.0;
      tactic   = "";
      portList = new ArrayList<>();
   }
   
   public Edge(Vertice src, Vertice dst, int count, double duration, double bytes, String tactic) {
      
      EdgeCount++;
      
      this.id          = src.id() + "," + dst.id();
      this.src         = src;
      this.dst         = dst;
      this.count       = count;
      this.bytes       = bytes;
      this.duration    = duration;
      this.tactic      = tactic;
      this.traversed   = false;
      this.portList    = new ArrayList<>();
      
      if(count > maxCount)              { maxCount     = count; }
      if(count < minCount)              { minCount     = count; }
      if(bytes > maxBytes)              { maxBytes     = bytes; }
      if(bytes < minBytes)              { minBytes     = bytes; }
      if(duration > maxDuration)        { maxDuration  = duration; }
      if(duration < minDuration)        { minDuration  = duration; }
   }
   
   public void addPorts(ArrayList<String> portList) {
      this.portList = portList;
   }
   
   public void addConnection()              { count++;                   }
   public void addBytes(double bytes)       { this.bytes    += bytes;    }
   public void addDuration(double duration) { this.duration += duration; }
   public void markTraversed()              { this.traversed = true;     }

   public Vertice getSrc()             { return src;       }
   public Vertice getDst()             { return dst;       }
   public Integer getCount()           { return count;     }
   public Double  getBytes()           { return bytes;     }
   public Double  getDuration()        { return duration;  }
   public String  getTactic()          { return tactic;    }
   public ArrayList<String> getPorts() { return portList;  }
   public Boolean traversed()          { return traversed; }
   public Integer getUniquePortCount() { return portList.size(); }
   
   public Double getNormalizedCount() { 
      return (double)(getCount() - minCount) / (maxCount - minCount);
   }
   
   public Double getNormalizedBytes() {
      return (getBytes() - minBytes) / (maxBytes - minBytes);
   }
   
   public Double getNormalizedDuration() {
      return (getDuration() - minDuration) / (maxDuration - minDuration);
   }
   
   public static Integer getMaxCount()        { return maxCount; }
   public static Integer getMinCount()        { return minCount; }
   public static Double  getMaxBytes()        { return maxBytes; }
   public static Double  getMinBytes()        { return minBytes; }
   public static Double  getMaxDuration()     { return maxDuration; }
   public static Double  getMinDuration()     { return minDuration; }
   
   @Override
   public String toString() {
      return id;
   }
}
