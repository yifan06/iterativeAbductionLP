package abd.datastructure.graph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//import net.sf.tweety.graphs.*;

public class Graph{
	
	private HashMap<Node, TreeSet<Node>> myAdjList;
	private HashMap<String, Node> myVertices;
	private static final TreeSet<Node> EMPTY_SET = new TreeSet<Node>();
	private int myNumVertices;
	private int myNumEdges;

	/**
	 * Construct empty Graph
	 */
	public Graph() {
		myAdjList = new HashMap<Node, TreeSet<Node>>();
		myVertices = new HashMap<String, Node>();
		myNumVertices = myNumEdges = 0;

	}

	/**
	 * Add a new Node name with no neighbors (if Node does not yet exist)
	 * 
	 * @param name
	 *            Node to be added
	 */
	public Node addNode(String name) {
		Node v;
		v = myVertices.get(name);
		if (v == null) {
			v = new Node(name);
			myVertices.put(name, v);
			myAdjList.put(v, new TreeSet<Node>());
			myNumVertices += 1;
		}
		return v;
	}

	/**
	 * Returns the Node matching v
	 * @param name a String name of a Node that may be in
	 * this Graph
	 * @return the Node with a name that matches v or null
	 * if no such Node exists in this Graph
	 */
	public Node getNode(String name) {
		return myVertices.get(name);
	}

	/**
	 * Returns true iff v is in this Graph, false otherwise
	 * @param name a String name of a Node that may be in
	 * this Graph
	 * @return true iff v is in this Graph
	 */
	public boolean hasNode(String name) {
		return myVertices.containsKey(name);
	}

	/**
	 * Is from-to, an edge in this Graph. The graph is 
	 * undirected so the order of from and to does not
	 * matter.
	 * @param from the name of the first Node
	 * @param to the name of the second Node
	 * @return true iff from-to exists in this Graph
	 */
	public boolean hasEdge(String from, String to) {

		if (!hasNode(from) || !hasNode(to))
			return false;
		return myAdjList.get(myVertices.get(from)).contains(myVertices.get(to));
	}
	
	/**
	 * Add to to from's set of neighbors, and add from to to's
	 * set of neighbors. Does not add an edge if another edge
	 * already exists
	 * @param from the name of the first Node
	 * @param to the name of the second Node
	 */
	public void addEdge(String from, String to) {
		Node v, w;
		if (hasEdge(from, to))
			return;
		myNumEdges += 1;
		if ((v = getNode(from)) == null)
			v = addNode(from);
		if ((w = getNode(to)) == null)
			w = addNode(to);
		myAdjList.get(v).add(w);
		myAdjList.get(w).add(v);
	}

	/**
	 * Return an iterator over the neighbors of Node named v
	 * @param name the String name of a Node
	 * @return an Iterator over Vertices that are adjacent
	 * to the Node named v, empty set if v is not in graph
	 */
	public Iterable<Node> adjacentTo(String name) {
		if (!hasNode(name))
			return EMPTY_SET;
		return myAdjList.get(getNode(name));
	}

	/**
	 * Return an iterator over the neighbors of Node v
	 * @param v the Node
	 * @return an Iterator over Vertices that are adjacent
	 * to the Node v, empty set if v is not in graph
	 */
	public Iterable<Node> adjacentTo(Node v) {
		if (!myAdjList.containsKey(v))
			return EMPTY_SET;
		return myAdjList.get(v);
	}

	/**
	 * Returns an Iterator over all Vertices in this Graph
	 * @return an Iterator over all Vertices in this Graph
	 */
	public Iterable<Node> getVertices() {
		return myVertices.values();
	}

	public int numVertices()
	{
		return myNumVertices;
	}
	
	public int numEdges()
	{
		return myNumEdges;
	}
	
	/**
	 * Sets each Vertices' centrality field to
	 * the degree of each Node (i.e. the number of
	 * adjacent Vertices)
	 */
	public void degreeCentrality()
	{
		// TODO: complete degreeCentrality
	}
	
	/**
	 * Sets each Vertices' centrality field to
	 * the average distance to every Node
	 */
	public void closenessCentrality()
	{
		// TODO: complete closenessCentrality
	}
	/**
	 * Sets each Vertices' centrality field to
	 * the proportion of geodesics (shortest paths) that
	 * this Node is on
	 */
	public void betweennessCentrality()
	{
		// TODO: complete betweennessCentrality
	}
	/*
	 * Returns adjacency-list representation of graph
	 */
	public String toString() {
		String s = "";
		for (Node v : myVertices.values()) {
			s += v + ": ";
			for (Node w : myAdjList.get(v)) {
				s += w + " ";
			}
			s += "\n";
		}
		return s;
	}

	private String escapedVersion(String s) {
		return "\'"+s+"\'";

	}

	public void outputGDF(String fileName)
    {
    	HashMap<Node, String> idToName = new HashMap<Node, String>();
    	 try {
			FileWriter out = new FileWriter(fileName);
			int count = 0;
			out.write("nodedef> name,label,style,distance INTEGER\n");
			// write vertices
			for (Node v: myVertices.values())
			{
				String id = "v"+ count++;
				idToName.put(v, id);
				out.write(id + "," + escapedVersion(v.name));
				out.write(",6,"+v.distance+"\n");
			}
			out.write("edgedef> node1,node2,color\n");
			// write edges
		    for (Node v : myVertices.values())
		    	for (Node w : myAdjList.get(v))  
		    	if (v.compareTo(w) < 0)
		    	{
		    		out.write(idToName.get(v)+","+ 
		    				idToName.get(w) + ",");
		    		if (v.ancestor == w || 
		    				w.ancestor == v)
		    			out.write("blue");
		    		else	
		    			out.write("gray");
		    		out.write("\n");
		    	}
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 
    }

	public static void main(String[] args) {
		Graph G = new Graph();
		G.addEdge("A", "B");
		G.addEdge("A", "C");
		G.addEdge("C", "D");
		G.addEdge("D", "E");
		G.addEdge("D", "G");
		G.addEdge("E", "G");
		G.addNode("H");
		System.out.println(G.escapedVersion("Bacon, Kevin"));
		// print out graph
		System.out.println(G);

		// print out graph again by iterating over vertices and edges
		for (Node v : G.getVertices()) {
			System.out.print(v + ": ");
			for (Node w : G.adjacentTo(v.name)) {
				System.out.print(w + " ");
			}
			System.out.println();
		}
		G.outputGDF("graph.gdf");
	}

}
