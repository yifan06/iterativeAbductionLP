package abd.tableau.aliseda;

import java.util.*;

import abd.datastructure.graph.*;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import abd.datastructure.graph.*;

public class AlisedaTableau{
	    
	private AlisedaNode root;
	private Vector<AlisedaNode> leafnodes;
	
	private AlisedaGraph model;
	
	private PlBeliefSet kb;
	private PropositionalFormula obs;
	
	private Vector<PropositionalFormula> usedFormula;
	private Vector<PropositionalFormula> unusedFormula;
	
	
	public AlisedaTableau() {
		// TODO Auto-generated constructor stub
		model = new AlisedaGraph();
		root = new AlisedaNode();
		leafnodes = new Vector<AlisedaNode>();
		usedFormula = new Vector<PropositionalFormula>();
		unusedFormula = new Vector<PropositionalFormula>();
//		model.addRoot(root);
	}

	void initilization(AlisedaNode rootnode){
		root = rootnode;
		model.initilization(rootnode);
	}
	
	void addKb(PlBeliefSet k){
		kb = k;
		Iterator<PropositionalFormula> it = kb.iterator();
		while(it.hasNext())
			unusedFormula.add(it.next());
	}
	
    public int getnbNodes(){
    	return model.getNodesize();
    }
	
	public void expansion(){

		while(!unusedFormula.isEmpty()){			
			PropositionalFormula f = unusedFormula.firstElement();
			System.out.println("apply the axiom " + f);
			model.expansion(f);
			usedFormula.addElement(f);
			unusedFormula.remove(f);
		}
		leafnodes.addAll(model.getAllLeaves());

	}

	public void addRoot(PropositionalFormula obs) {
		// TODO Auto-generated method stub
//		root = new AlisedaNode();
		this.obs=obs;
		Set<PropositionalFormula> o = obs.getLiterals();
		root.init(o);
//		root.addUnsedFormula(f);
		model.initilization(root);
		
	}
	
	public void printGraph(){
		model.printAllNodes();
		System.out.println("=========================================");
		model.printTableau();
	}

	public Vector<PropositionalFormula>  getMiniamlExplantion(Vector<PropositionalFormula> hyp ) {
		// TODO Auto-generated method stub
		Vector<PropositionalFormula> minimal = new Vector<PropositionalFormula>();
		SatSolver mysolver = new Sat4jSolver();
		for(int i=hyp.size()-1;i>=0;i--){
			PropositionalFormula f = hyp.elementAt(i);
			Conjunction c = new Conjunction(f.getLiterals());
			Conjunction o = new Conjunction(obs.getLiterals());
			PropositionalFormula irr = c.combineWithAnd(o.complement());
			if(c.getLiterals().containsAll(o.getLiterals())|| o.getLiterals().containsAll(c.getLiterals())){
				System.out.println("irrelevant hyp: "+ hyp.get(i));
				hyp.remove(i);
				continue;
			}
			
			if(!mysolver.isConsistent((PropositionalFormula)irr)){
				System.out.println("irrelevant hyp: "+ hyp.get(i));
				hyp.remove(i);
				continue;
			}
			PlBeliefSet test = new PlBeliefSet(kb);
			test.add(hyp.get(i));
			if(!mysolver.isConsistent(test)){
				System.out.println("inconsistent hyp: "+ hyp.get(i));
				hyp.remove(i);
				continue;
			}
		}
		
		
		loop1:
		for(int i =0; i<hyp.size();i++){
			PropositionalFormula f = hyp.elementAt(i);
			Conjunction c = new Conjunction(f.getLiterals());
////			if(minimal.size()==0)
////				minimal.add(c);
//			Conjunction o = new Conjunction(obs.getLiterals());
////			System.out.println("hyp " +  c);
////			System.out.println("obs " + o);
//			PropositionalFormula irr = o.combineWithAnd(c.complement());
////			Conjunction irr = new Conjunction();
////			irr.add((PropositionalFormula) c.complement());
////			irr.add(o);
//			if(!mysolver.isConsistent((PropositionalFormula)irr)){
//				System.out.println("irrelevant hyp: "+ hyp.get(i));
//				continue;
//			}
//			if(f.getLiterals().contains("w")){
//				System.out.println("observation contains yes");
//				continue;
//			}
//			kb.add(c);
			if(mysolver.isConsistent(kb)){
//				kb.remove(c);
				for(int j=hyp.size()-1; j>i; j--){
					PropositionalFormula fc = hyp.elementAt(j);
					Conjunction fcc = new Conjunction(fc.getSignature());
					PropositionalFormula sub1= c.combineWithAnd(fcc.complement());
//					System.out.println(i+" "+ j+" sub1 "+sub1);
					PropositionalFormula sub2= fcc.combineWithAnd(c.complement());
//					System.out.println(i+" "+ j+ "sub2"+sub2);
					kb.add(sub1);
					if(!mysolver.isConsistent(kb)){
						kb.remove(sub1);
						kb.add(sub2);
						if(!mysolver.isConsistent(kb)){
							kb.remove(sub2);
						}else{
							kb.remove(sub2);
							break loop1;
						}
					}else{
						kb.remove(sub1);
						kb.add(sub2);
						if(!mysolver.isConsistent(kb)){
							kb.remove(sub2);
							hyp.removeElementAt(j);
						}else{
							kb.remove(sub2);
						}
					}
				}
				minimal.add(c);
			}else
				System.out.println(hyp.elementAt(i));
//			kb.remove(hyp.elementAt(i));
//			loop:
//				System.out.println("a break");
		}
		return minimal;
	}

	public Vector<PropositionalFormula> generationHypotheses(Vector<AlisedaNode> leaves){
		// TODO Auto-generated method stub
		Vector<PropositionalFormula> hypotheses = new Vector<PropositionalFormula>();
//		int numOpenset = leaves.size();
//		for(int i=0; i<numOpenset; i++){
//			AlisedaNode n = leaves.elementAt(i);
//			
//		}
		
//		for(int i=0; i<leaves.size(); i++){
//			System.out.println(i);
//			leaves.elementAt(i).printleaves();
//			AlisedaNode n = leaves.elementAt(i);
//			Vector<PropositionalFormula> candidates = n.getLiterals();
//		
//		}
		
		// get the minimal hitting set from the leave nodes
			// create a hitting set tree
		TreeHelper hs = new TreeHelper();
		TreeNode root = new TreeNode();
		root.initOrgEntity();
		hs.setRoot(root);
		
		for(int i=0; i< leaves.size(); i++){
			AlisedaNode n = leaves.elementAt(i);
			Vector<PropositionalFormula> candidates = n.getLiterals();
			ArrayList<TreeNode> tempNodeList = new ArrayList<TreeNode>();
			for(int j=0; j<candidates.size();j++){
				PropositionalFormula l = candidates.elementAt(j);
				TreeNode node = new TreeNode();
				OrganizationEntity entity = new OrganizationEntity();
				entity.setLiteral(l);
				node.setObj(entity);
				tempNodeList.add(node);
				node.addHyp(l);
			}
			//System.out.println("temp node size: " + tempNodeList.size());
			hs.setTempNodeList(tempNodeList);
			hs.updateTree();
			ArrayList<TreeNode> ln = (ArrayList<TreeNode>) hs.getLeafNodes();
			//System.out.println("leaf size: " + ln.size());
			
//			root.addChildNode(treeNode);
		}
		
		ArrayList<TreeNode> ln = (ArrayList<TreeNode>) hs.getLeafNodes();
		Iterator<TreeNode> itln = ln.iterator();
		while(itln.hasNext()){
			TreeNode leaf=itln.next();
			Conjunction lh= leaf.getConjunction();
			Conjunction h= new Conjunction();
			Iterator<PropositionalFormula> itlh = lh.getLiterals().iterator();
			while(itlh.hasNext()){
				h.add((PropositionalFormula) itlh.next().complement());
			}
			hypotheses.add(h);
		}
//		System.out.println("leaf size: " + ln.size());
//		
//		
//		// each nodes contains a set of literals.
//		Iterator<TreeNode> itln = ln.iterator();
//		while(itln.hasNext()){
//			TreeNode leaf=itln.next();
//			Conjunction lh= leaf.getConjunction();
//			Conjunction h= new Conjunction();
//			Iterator<PropositionalFormula> itlh = lh.getLiterals().iterator();
//			while(itlh.hasNext()){
//				h.add((PropositionalFormula) itlh.next().complement());
//			}
//			if(hypotheses.size()>0){
//				for(int i=0;i<hypotheses.size();i++){
//					if(h.getLiterals().contains(hypotheses.get(i).getLiterals())){
//						System.out.println("1");
//						break;
//					}else if (hypotheses.get(i).getLiterals().contains(h.getLiterals())) {
//						hypotheses.remove(i);
//						hypotheses.add(h);
//						System.out.println("2");
//						break;
//					}else{
//						if(i==hypotheses.size()-1){
//							System.out.println("3");
//							hypotheses.add(h);
//						}
//					}
//				}
////				hypotheses.add(h);
//			}else
//				hypotheses.add(h);
//		
//		}
		
//		while(itln.hasNext()){
//			TreeNode leaf = itln.next();
//			ArrayList<TreeNode> elders = (ArrayList<TreeNode>) leaf.getElders();
//			System.out.println("elders size "+elders.size());
//			Iterator<TreeNode> itelders = elders.iterator();
//			Conjunction h = new Conjunction();
//			OrganizationEntity leafliteral = (OrganizationEntity) leaf.getObj();
//			if(leafliteral.hasLiteral()){
////				System.out.println(e.getLiteral());
//				h.add((PropositionalFormula) leafliteral.getLiteral().complement());
//			}
//			while(itelders.hasNext()){
//				OrganizationEntity e = (OrganizationEntity) itelders.next().getObj();
//				if(e.hasLiteral()){
//					System.out.println(e.getLiteral());
//					h.add((PropositionalFormula) e.getLiteral().complement());
//				}
//					
//			}
//			hypotheses.add(h);
//		}
		// all possible combination
		return hypotheses;
	}
	
	public Vector<AlisedaNode> getLeaves(){
		return leafnodes;
	}
}
