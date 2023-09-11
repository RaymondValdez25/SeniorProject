import java.util.*;
 
public class Vertice {
   static int VerticeCount;
   
   private String     id;
   private List<Edge> in      = new ArrayList<Edge>();
   private List<Edge> out     = new ArrayList<Edge>();
   private Boolean    visited = false;
   
   public Vertice(String id) {
      this.id = id;
      VerticeCount++;
   }
   
   public void addEdgeIn()        { in.add(new Edge()); }
   public void addEdgeIn(Edge e)  { in.add(e); }
   
   public void addEdgeOut()       { out.add(new Edge()); }
   public void addEdgeOut(Edge e) { out.add(e); }
   
   public void markVisited()      { visited = true; }
   
   public String id()     { return id; }
   public int inDegree()  { return in.size(); }
   public int outDegree() { return out.size(); }
   
   public List<Edge> getEdgesOut() { return out; }
   public List<Edge> getEdgesIn()  { return in;  }

   public Boolean visited() { return visited; }
   
   @Override
   public String toString() {
      return id;
   }
}
