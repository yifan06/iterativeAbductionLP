package abd.tableau.iterative;

import java.util.*;

public class IteGraph {
 
	protected OrNode root = null;
    protected Vector<AndNode> nodes = new Vector<AndNode>();
    protected Vector<Edge> edges = new Vector<Edge>();
    protected boolean directed = false;
    protected boolean sortedNeighbors = false;
     
    public double[][] getAdjacencyMatrix() {
        double[][] adjMatrix = new double[nodes.size()][nodes.size()];
         
        for(int i = 0; i < nodes.size(); i++)
            for(int j = 0; j < nodes.size(); j++)
                if(i == j)
                    adjMatrix[i][j] = 0;
                else
                    adjMatrix[i][j] = Double.POSITIVE_INFINITY;
                 
        for(int i = 0; i < nodes.size(); i++) {
        	AndNode node = nodes.elementAt(i);
            //System.out.println("Current node: " + node);
             
            for(int j = 0; j < edges.size(); j++) {
                Edge edge = edges.elementAt(j);
                 
                if(edge.a == node) {
                    int indexOfNeighbor = nodes.indexOf(edge.b);
                     
                    adjMatrix[i][indexOfNeighbor] = edge.weight;
                }
            }
        }
         
        return adjMatrix;
    }
     
    public void setDirected() {
        directed = true;
    }
     
    public void setUndirected() {
        directed = false;
    }
     
    public boolean isDirected() {
        return directed;
    }
     
    public boolean isSortedNeighbors() {
        return sortedNeighbors;
    }
     
    public void setSortedNeighbors(boolean flag) {
        sortedNeighbors = flag;
    }
     
    public int indexOf(AndNode a) {
        for(int i = 0; i < nodes.size(); i++)
//            if(nodes.elementAt(i).data.equals(a.data))
                return i;
                 
        return -1;
    }
     
    public Vector<AndNode> getNodes() {
        return nodes;
    }
     
    public Vector<Edge> getEdges() {
        return edges;
    }
     
    public AndNode getNodeAt(int i) {
        return nodes.elementAt(i);
    }
     
    public void unvisitAllNodes() {
        for(int i = 0; i < nodes.size(); i++)
            nodes.elementAt(i).unvisit();
    }
     
    public Vector<AndNode> getNeighbors(AndNode a) {
        Vector<AndNode> neighbors = new Vector<AndNode>();
         
        for(int i = 0; i < edges.size(); i++) {
            Edge edge = edges.elementAt(i);
             
            if(edge.a == a)
                neighbors.add(edge.b);
                 
            if(!directed && edge.b == a)
                neighbors.add(edge.a);
        }
         
        if(sortedNeighbors)
            Collections.sort(neighbors);
         
        return neighbors;
    }
     
    public int addNode(AndNode a) {
        nodes.add(a);
         
        return nodes.size() - 1;
    }
     
    public void addEdge(Edge a) {
        edges.add(a);
         
        if(!directed)
            edges.add(new Edge(a.b, a.a, a.weight));
    }
     
    public void printNodes() {
        System.out.println(nodes);
    }
     
    public void printEdges() {
        System.out.println(edges);
    }

	public void setRoot(OrNode root) {
		// TODO Auto-generated method stub
		this.root = root;
	}
 
}