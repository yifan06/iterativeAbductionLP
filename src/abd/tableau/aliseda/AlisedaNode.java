package abd.tableau.aliseda;

import java.io.IOException;
import java.util.*;

import abd.datastructure.graph.*;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.*;

public class AlisedaNode extends Node implements Cloneable{

	// set of atomic propositions
	private Vector<PropositionalFormula> literals;
	// set of formulas
//	private ArrayList<PropositionalFormula> future_formulas;
//	private ArrayList<PropositionalFormula> past_formulas;
	private AlisedaNode ancestor;
	private Vector<AlisedaNode> successors;

	
	// inconsistent flag (closed branch)
	boolean cflag;
	
	AlisedaNode(){
		super("aliseda");
		cflag = true;
		literals = new Vector<PropositionalFormula>();
//		ancestor = new AlisedaNode();
		successors = new Vector<AlisedaNode>();
	}
	
	AlisedaNode(AlisedaNode n){
		super("aliseda");
		literals=n.getLiterals();
//		this.future_formulas=n.getFutureF();
//		this.past_formulas=n.getPastF();
		ancestor = n.getParent();
		successors = n.getChildren();
		cflag = true;
	}

//	private ArrayList<PropositionalFormula> getPastF() {
//		// TODO Auto-generated method stub
//		return past_formulas;
//	}

//	private ArrayList<PropositionalFormula> getFutureF() {
//		// TODO Auto-generated method stub
//		return future_formulas;
//	}

	protected Vector<PropositionalFormula> getLiterals() {
		// TODO Auto-generated method stub
		return literals;
	}

	AlisedaNode(String n) {
		super(n);
		cflag=true;
		// TODO Auto-generated constructor stub
	}
	public boolean hasChild(){
		if(successors.isEmpty())
			return false;
		else
			return true;
	}
 
	public Vector<AlisedaNode> getChildren(){
		return successors;
	}
	
	public void setChildren(AlisedaNode n){
		successors.addElement(n);
	}
	
	public void setChildren(Vector<AlisedaNode> vn){
		for(int i=0; i<vn.size();i++)
			successors.addElement(vn.elementAt(i));
	}
	
	public AlisedaNode getParent(){
		return ancestor;
	}
	
	public void setParent(AlisedaNode p){
		ancestor = p;
	}


	public boolean getcFlag() {
		// TODO Auto-generated method stub
		return cflag;
	}

	
	public AlisedaNode duplicateNode(){
		AlisedaNode nnode = new AlisedaNode();
		for(int i=0; i<literals.size(); i++){
			nnode.addLiterals(literals.elementAt(i));
		}
		
//		AlisedaNode nnode = null;
//		try {
//			nnode = (AlisedaNode) this.clone();
//		} catch (CloneNotSupportedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return nnode;
	}
	
	public void addLiterals(PropositionalFormula f) {
		// TODO Auto-generated method stub
		literals.addElement(f);
	}

	/**
     * @return 创建并返回此对象的一个副本。
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        //直接调用父类的clone()方法,返回克隆副本
        return super.clone();
    }
    
    
	public void init(Set<PropositionalFormula> o) {
		// TODO Auto-generated method stub
		literals = new Vector<PropositionalFormula>();
		Iterator<PropositionalFormula> it = o.iterator();
		ancestor = null;
		successors = new Vector<AlisedaNode>();
		
		while(it.hasNext()){
			AlisedaNode n = new AlisedaNode();
			n.addLiterals((PropositionalFormula) it.next().complement());
//			literals.add((PropositionalFormula) it.next().complement());
			n.setParent(this);
			this.setChildren(n);

		}
		
//		future_formulas = new ArrayList<PropositionalFormula>();
//		past_formulas = new ArrayList<PropositionalFormula>();

		
		
	}
	
	public void checkConsistency(){
		outerloop:
		for(int i=0; i< literals.size();i++){
			for(int j=i; j< literals.size();j++){
				if(literals.elementAt(i).equals(literals.elementAt(j).complement())){
					cflag = false;
					break outerloop;
				}
			}
		}
	}
	
	public boolean isConsistent(){
		return cflag;
	}

	public void updateliterals(Set<PropositionalFormula> l) {
		// TODO Auto-generated method stub
		literals.addAll(l);
	}

	public void printleaves() {
		// TODO Auto-generated method stub
		for(int i=0; i<literals.size(); i++){
			System.out.print(literals.elementAt(i));
		}
		System.out.println();
	}
	
	public void removeRedundance(){
		for(int i=literals.size()-1; i>=0; i--){
			for(int j=0; j< i ;j++){
				if(literals.elementAt(j).equals(literals.elementAt(i))){
					literals.removeElementAt(i);
					break;
				}
			}
//			System.out.print(literals.elementAt(i));
		}
	}

	public void printChildren() {
		// TODO Auto-generated method stub
//		Vector<PropositionalFormula> lits = root.getLiterals();
		System.out.print("node :");
		for(int i=0;i<literals.size();i++){
			System.out.print(literals.elementAt(i)+" ");
		}
		System.out.println();	
		System.out.println(" children: ");
		for(int i=0; i<successors.size();i++){
			successors.elementAt(i).printChildren();
		}
	}
	
//	public Vector<AlisedaNode> expansion(){
//		if(!future_formulas.isEmpty()){
//			PropositionalFormula f = future_formulas.get(0);
//			PropositionalFormula nnf = f.toNnf();
//			PlParser parser = new PlParser();
//			parser.parseFormula(text)
//		}
//		else
//			return null;
//	}
	
	



	
	

	
	

}
