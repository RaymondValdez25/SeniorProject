// A Java Program to detect cycle in a graph
import java.util.*;
 
public class GraphCycle {
     
   private int V;
   private List<List<Integer>> adj;
 
   public GraphCycle(int V) {
      this.V  = V;
      this.adj = new ArrayList<>(V);
         
      for (int i = 0; i < V; i++)
         adj.add(new LinkedList<>());
   }
   
   public List<List<Integer>> graphEdges() {
      return adj;
   }
   
   public boolean isCyclicUtil(int i, boolean[] visited, boolean[] recStack) {
         
      // Mark the current node as visited and
      // part of recursion stack
      if (recStack[i])
        return true;
 
      if (visited[i])
         return false;
             
      visited[i] = true;
 
      recStack[i] = true;
      List<Integer> children = adj.get(i);
         
      for (Integer c: children)
         if (isCyclicUtil(c, visited, recStack))
            return true;
                 
      recStack[i] = false;
 
      return false;
   }
 
   public void addEdge(int source, int dest) {
      this.adj.get(source).add(dest);
   }
 
   public void removeEdge(int source, int dest) {
      //System.out.println("Remove Edge invoked,  Source = " + source + "  Dest = " + dest);

      List<Integer> list = adj.get(source);
      
      for(int i=0;i<list.size();i++) {
         if(list.get(i)==dest) {
            this.adj.get(source).remove(i);
         }
      }
   }
   
   // Returns true if the graph contains a
   // cycle, else false.
   // This function is a variation of DFS()
   public boolean isCyclic() {
         
      // Mark all the vertices as not visited and
      // not part of recursion stack
      boolean[] visited = new boolean[V];
      boolean[] recStack = new boolean[V];
         
      // Call the recursive helper function to
      // detect cycle in different DFS trees
      for (int i = 0; i < V; i++)
         if (isCyclicUtil(i, visited, recStack))
            return true;
 
      return false;
   }
}
 
