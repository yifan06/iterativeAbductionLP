package abd.tableau.aliseda;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import abd.datastructure.graph.TreeNode;
import abd.knowledge.util.MinHittingSet;
import abd.knowledge.util.SortedIntList;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class Aliseda {
	protected static PlParser parser = new PlParser();
	public static void main(String[] args) throws ParserException, IOException{
			
//			PropositionalFormula obs = (PropositionalFormula) parser.parseFormula("p2 && p4");
//			int nb_var = 7;
//			int nb_formulas = 8;
//			
//			String output = String.format("./result/output-%1$02d-%2$02d.txt", nb_var, nb_formulas);
//			Writer writer = new FileWriter(output);
//			float sum = 0;
//			for(int i =2 ; i<12;i++){
//				System.out.println(i);
//		    	String file = String.format("./generator/%1$02d/Data-%1$02d-%2$02d-%3$03d.txt", nb_var, nb_formulas,i);
//				float rt = run(file, obs);
//				writer.write(rt + "\n");
//				sum = sum +rt;
//		    }		
//			float mean = sum /10;
//			writer.write("mean time " + mean + "\n");
//		    writer.close();
		    
		    
			PropositionalFormula obs = (PropositionalFormula) parser.parseFormula("w && d");
			String file="/home/yifan/plkb.txt";
			float rt = run(file, obs);
		    
	}


	public static float run(String name, PropositionalFormula observation) throws FileNotFoundException, ParserException, IOException{
		
		// Create a pl knowledge base
		//PlParser parser = new PlParser();
//		String file="/home/yifan/plkb.txt";
		String file= name;
//		String file="/home/yifan/workspace_eclipse/iterativeTab/kbset/generator_5_5/generator9.txt";
		PlBeliefSet kb = parser.parseBeliefBaseFromFile(file);
		
		// Parse observation formula 
//		String observation="/home/yifan/obs.txt";
//		PropositionalFormula obs = (PropositionalFormula) parser.parseFormulaFromFile(observation);
		PropositionalFormula obs = observation;
		
		// Set SAT solver
		SatSolver.setDefaultSolver(new Sat4jSolver());
		
		// consistency
		//boolean consistency =kb.isConsistent();
		//System.out.println("knowledge base is "+consistency+" consistent");
//		if(!consistency){
//			System.out.println("not a consistent kb. Stop");
//			System.exit(0);
//		}
		
		kb = new PlBeliefSet(kb.toCnf());
		
		// Get current time
		long start = System.currentTimeMillis();
		
		
		// Initialization aliseda tableau
		AlisedaTableau tab = new AlisedaTableau();
		tab.addKb(kb);
		tab.addRoot(obs);
		
		// Expansion of the tableau
		tab.expansion();
		
		// print tableau
		 //tab.printGraph();
       System.out.println("nodes size: "+ tab.getnbNodes());
		
		Vector<AlisedaNode> leaves = tab.getLeaves();
		System.out.println("open branch size: "+leaves.size());
		for(int i=0; i<leaves.size(); i++){
			//System.out.println(i);
//			leaves.elementAt(i).printleaves();
			leaves.elementAt(i).removeRedundance();
//			leaves.elementAt(i).printleaves();
		}
		
		// compute explanation
//        Date startComputationTime = new Date();
//        System.out.println("start");
		
		for(int i=0;i<leaves.size();i++){
			AlisedaNode n = leaves.elementAt(i);
			Vector<PropositionalFormula> lits = n.getLiterals();
			System.out.print(i+" leaves: ");
			for(PropositionalFormula f : lits){
				System.out.print(f);
			}
			System.out.println();
		}
		
//		Vector<PropositionalFormula> hyp = tab.generationHypotheses(leaves);
//        System.out.println("end");
//        Date endComputationTime = new Date();
//		for(int i=0; i<hyp.size(); i++){
//			System.out.println("hyp " +i + "  "+ hyp.elementAt(i));
//		}
		
		
		// compute explanation using hitting set algo from diagengine.
		ArrayList confsets = new ArrayList();
		for(int i=0; i<leaves.size(); i++){
			SortedIntList cs = new SortedIntList();
			AlisedaNode n = leaves.elementAt(i);
			Vector<PropositionalFormula> lits = n.getLiterals();
			for(PropositionalFormula f:lits){
				cs.addSorted(f);
			}
			confsets.add(cs);
		}
        MinHittingSet hittingSets = new MinHittingSet(false, confsets);
        
//        Date startComputationTime = new Date();
//        System.out.println("start");
        
        hittingSets.compute(100, 100);
//        System.out.println("end");
//        Date endComputationTime = new Date();
//        long passedTime = endComputationTime.getTime() - startComputationTime.getTime();
        
        Vector<PropositionalFormula> hyp = new Vector<PropositionalFormula>();

        Iterator itHS = hittingSets.getMinHSAsIntLists().iterator();
        while(itHS.hasNext()) {
            System.out.println();
            Conjunction h= new Conjunction();
            SortedIntList hs = (SortedIntList)itHS.next();
            
            Iterator itInt = hs.iterator();
            while(itInt.hasNext()) {
            	PropositionalFormula n = (PropositionalFormula)itInt.next();
            	h.add((PropositionalFormula) n.complement());
                System.out.print(n.toString()+ " ");
            }
            System.out.println();
            hyp.add(h);
        }
        
        
        
        
        
        System.out.println();        

//        System.out.println("passed milliseconds: " + passedTime);            

//        boolean minimal = hittingSets.checkMinimalityHS();
//        if (minimal) System.out.println("MINIMAL!");
//        else System.out.println("NOT MINIMAL!");
//		return passedTime;

        
		// get Explanation
		Vector<PropositionalFormula> minimal = tab.getMiniamlExplantion(hyp);
		for(int i=0; i<minimal.size(); i++){
			System.out.println("minimal " +i + "  "+ minimal.elementAt(i));
		}
		
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		
		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;

//		// Get elapsed time in minutes
//		float elapsedTimeMin = elapsedTimeMillis/(60*1000F);
//
//		// Get elapsed time in hours
//		float elapsedTimeHour = elapsedTimeMillis/(60*60*1000F);
//
//		// Get elapsed time in days
//		float elapsedTimeDay = elapsedTimeMillis/(24*60*60*1000F);
		
		System.out.println("elapsedTimeSec " +elapsedTimeSec);
		System.out.println("Finish");
		
		return elapsedTimeSec;
//		
//		// singature 
//		Signature sg = kb.getSignature();
//		System.out.println(sg.getClass());
//		System.out.println(sg.toString());
	//	
//		System.out.println(file);
		
		// print every formula in the terminal
//		System.out.println(kb.toString()); 
//		Iterator<PropositionalFormula> it = kb.iterator();
//		while(it.hasNext()){
//			PropositionalFormula pf = it.next();
//			
//			System.out.println(pf.toString());
//			System.out.println("NNF " +pf.toNnf());
//			System.out.println("NNF C " +pf.toNnf().collapseAssociativeFormulas());
//			System.out.println("DNF " +pf.toDnf());
//			System.out.println("DNF C " +pf.toDnf().collapseAssociativeFormulas());
//			
//			PropositionalFormula nnf = pf.toNnf();
//		    // DNF( P || Q) = DNF(P) || DNF(Q)
//		    if(nnf instanceof Disjunction) {
//		      Disjunction d = (Disjunction) nnf;
//		      Disjunction dnf = new Disjunction();
//		      for(PropositionalFormula f : d) {
//			      System.out.println("dnf " + f.toNnf());
//		      }
//			}
//
//			
//			System.out.println("CNF " +pf.toCnf());
//			System.out.println("CNF C" +pf.toCnf().collapseAssociativeFormulas());
//			System.out.println("atoms "+ pf.toString());
 

   }

}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		
//		
//		// aliseda tableau launcher
//		// read from a file the knowledge base and observations
//		
//		
//		
//		// Initialization aliseda tableau
//		
//		// lauch the reasoner to construct the tableau
//		
//		
//		// take the minimal hitting set from the tableau
//		
//		// eliminating the inconsistent and taking the minimal explanation.
//		
//		// output in a file.
//
//	}

