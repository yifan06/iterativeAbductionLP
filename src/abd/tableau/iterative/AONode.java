package abd.tableau.iterative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class AONode implements Comparable<AONode> {
    
    protected HashSet<PropositionalFormula> appliedrules;
    protected HashSet<PropositionalFormula> leftrules;
    
    protected PropositionalFormula rule;
    protected PropositionalFormula f;
    
    protected boolean status = true; // true and node, false or node
    protected boolean termination = false; // termination node
    
    // model literals list 
    protected HashSet<PropositionalFormula> model = new HashSet<PropositionalFormula>();
    // hypotheses list
    protected HashMap<PropositionalFormula, PropositionalFormula> hyps = new HashMap<PropositionalFormula,PropositionalFormula>();
    
    protected boolean visited;
    public Integer index = null;
    public Integer lowlink = null;
    public double distance = Double.POSITIVE_INFINITY;
    public AONode predecessor = null;
    public ArrayList<AONode> children = null;
    public ArrayList<AONode> siblings = null;
     
    public AONode(PropositionalFormula f) {
        this.f = f;
        appliedrules = new HashSet<PropositionalFormula>();
        leftrules= new HashSet<PropositionalFormula>();
    }
     
    public AONode() {
        appliedrules = new HashSet<PropositionalFormula>();
        leftrules= new HashSet<PropositionalFormula>();
    }
     
    public boolean isVisited() {
        return visited;
    }
     
    public void visit() {
        visited = true;
    }
     
    public void unvisit() {
        visited = false;
    }
     
    public int compareTo(AONode ob) {
    	PropositionalFormula fb = ob.getLiteral();
        if(fb.isLiteral()){
        	return fb.toString().compareTo(f.toString());
        }else
        	return 0;
    }
     
    public PropositionalFormula getLiteral() {
		// TODO Auto-generated method stub
		return f;
	}

	public String toString() {
        return f.toString();
    }

	public void setOrNode() {
		// TODO Auto-generated method stub
		status = false;
	}
	
	public void setAndNode(){
		status = true;
	}

	public void setRule(PropositionalFormula rule) {
		// TODO Auto-generated method stub
		this.rule = rule;
	}
	
	public void setPredecessor(AONode pred){
		this.predecessor = pred;
	}
	
	public AONode getPredecessor(){
		return predecessor;
	}
	
	public void setAppliedRules(HashSet<PropositionalFormula> applied){
		for(PropositionalFormula f : applied){
			appliedrules.add(f);
		}
	}
	
	public HashSet<PropositionalFormula> getAppliedRules(){
		return appliedrules;
	}
	
	public void setLeftRules(HashSet<PropositionalFormula> left){
		for(PropositionalFormula f : left){
			leftrules.add(f);
		}
	}
	
	public HashSet<PropositionalFormula> getLeftRules(){
		return leftrules;
	}

	public PropositionalFormula getRule() {
		// TODO Auto-generated method stub
		return rule;
	}

	public void setTermination() {
		// TODO Auto-generated method stub
		termination = true;
	}

	public void addAppliedRule(PropositionalFormula rf) {
		// TODO Auto-generated method stub
		appliedrules.add(rf);
	}
	
	public void deleteLeftRule(PropositionalFormula rf){
		leftrules.remove(rf);
	}

	public HashSet<PropositionalFormula> getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public void copyModel(HashSet<PropositionalFormula> m){
		model.addAll(m);
	}
	
	public boolean updateModel(){
		Iterator<PropositionalFormula> it = model.iterator();
		while(it.hasNext()){
			PropositionalFormula element = it.next();
			if(f.equals((PropositionalFormula)element.complement())){
				termination = true;
				return false;
			}
		}
		model.add((PropositionalFormula)f.complement());
		return true;
	}

	public void setChildren(AONode newnode) {
		// TODO Auto-generated method stub
		if(children == null)
			children = new ArrayList<AONode>();
		children.add(newnode);
	}
	
	public ArrayList<AONode> getChildren(){
		if(children == null || children.size()==0){
			return null;
		}else
			return children;
	}

	public void setHyp(PropositionalFormula h) {
		// TODO Auto-generated method stub
		hyps.put(h, f);
	}

	public void copyHyp() {
		// TODO Auto-generated method stub
		this.hyps = predecessor.hyps;
	}
     
}