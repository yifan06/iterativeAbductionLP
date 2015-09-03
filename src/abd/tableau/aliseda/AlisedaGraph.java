package abd.tableau.aliseda;

import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import abd.datastructure.graph.*;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class AlisedaGraph extends Graph{
	
	private AlisedaNode root;
	private Vector<AlisedaNode> nodes;
	
	
	// bette to defined in the aliseda tableau
	//	private PlBeliefSet kb;

	public AlisedaGraph() {
		// TODO Auto-generated constructor stub
		root = new AlisedaNode();
		nodes = new Vector<AlisedaNode>();
	}
	
	void initilization(AlisedaNode rootnode){
		root = rootnode;
		nodes.addElement(root);
		nodes.addAll(root.getChildren());
	}
	
//	void addKb(PlBeliefSet k){
//		kb = k;
//	}

	Vector<AlisedaNode> getAllLeaves(){
		Vector<AlisedaNode> leaves = new Vector<AlisedaNode>();
		int num = nodes.size();
		//System.out.println("nodes size " +num);
		for(int i=0; i<num; i++){
			//System.out.println(nodes.get(i).hasChild());
			//System.out.println(nodes.get(i).getcFlag());
			if(! nodes.get(i).hasChild() && nodes.get(i).getcFlag())
				leaves.add(nodes.get(i));
		}
		return leaves;
	}
//
//	public void addRoot(AlisedaNode r) {
//		// TODO Auto-generated method stub
//		root = r;
//	}
	public int getNodesize(){
		return nodes.size();
	}
	public void expansion(PropositionalFormula pf){
		// get all leave nodes
		Vector<AlisedaNode> leaves = getAllLeaves();
		System.out.println("leaves size: "+ leaves.size());
		
		for(int i=0; i< leaves.size(); i++){
			
			 PropositionalFormula f = pf.toDnf().collapseAssociativeFormulas();
			 
			 if(f instanceof Disjunction) {
				 Disjunction d = (Disjunction) f;
				 for(PropositionalFormula c : d) {
					 //System.out.println("dnf " + c.toNnf());
					 AlisedaNode n = leaves.elementAt(i).duplicateNode();
					 //System.out.println("new node 1 has children " + n.hasChild());
					 n.updateliterals(c.getLiterals());
					 n.checkConsistency();
					 n.setParent(leaves.elementAt(i));
					 leaves.elementAt(i).setChildren(n);
					 //System.out.println("new node has children " + n.hasChild());
					 nodes.addElement(n);
				 }
			 }

//		    if(f instanceof Disjunction) {
//		    	  f.toDnf().collapseAssociativeFormulas();
//			      Disjunction d = (Disjunction) f;
//			      Disjunction dnf = new Disjunction();
//			      for(PropositionalFormula ff : d) {
//				      System.out.println("dnf " + ff.toNnf());
//			      }
//		    }
//		    else{
//		    	Set<PropositionalFormula> conj = f.toNnf().getLiterals();
//		    	Vector<AlisedaNode> lnodes = this.getAllLeaves();
//		    	for(AlisedaNode l: lnodes){
//		    		AlisedaNode ch = l.duplicateNode();
//		    		
//		    	}
//		    }

		}
		
		
	}

	public void printAllNodes() {
		// TODO Auto-generated method stub
		System.out.println("root");
		Vector<PropositionalFormula> lits = root.getLiterals();
		for(int i=0;i<lits.size();i++){
			System.out.print(lits.elementAt(i)+" ");
		}
		System.out.println();	
		root.printChildren();
	}
	
	public void printTableau(){
		for(int i=0; i<nodes.size(); i++){
			System.out.println("No:" + i);
			nodes.elementAt(i).printleaves();
			for(int j=0;j<nodes.elementAt(i).getChildren().size();j++){
				System.out.println("children: " + nodes.indexOf(nodes.elementAt(i).getChildren().elementAt(j)));
			}
			
		}
	}

}
