package abd.datastructure.graph;

import java.util.Collection;
import java.util.Vector;
/*
 * A node is an abstract class representing basic vertex
 */


public class Node {

	// label for Node
	String name;
	
	protected Node ancestor;
	protected Vector<Node> successor;
	
	private Graph container;
	
	/**
	 * length of shortest path from source
	 */
	public int distance; 
	
	// constructor of a node
	protected Node(String n){
		name = n;
	}

	public int compareTo(Node w) {
		// TODO Auto-generated method stub
		if(this.name.equals(w.name))
			return 1;
		else
			return -1;
	}
	
	void addParent(Node p){
		ancestor = p;
	}
	
	void addChild(Node c){
		successor.add(c);
	}
	
	void addChildren(Collection<? extends Node> ch){
		successor.addAll(ch);
	}
	
	private Vector<Node> getChildren(){
		return successor;
	}
	
	public boolean hasChild() {
		// TODO Auto-generated method stub
		if(successor.isEmpty())
			return false;
		else 
			return true;
	}
	public Vector<Node> leafnodes() {
		// TODO Auto-generated method stub
		return successor;
	}
	
    public Object clone() throws CloneNotSupportedException {
        //直接调用父类的clone()方法,返回克隆副本
        return super.clone();
    }
}
