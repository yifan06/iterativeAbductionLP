package abd.tableau.aliseda;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class Aliseda {
	public static void main(String[] args) throws ParserException, IOException{
		
		// Create a pl knowledge base
		PlParser parser = new PlParser();
//		String file="/home/yifan/plkb.txt";
		String file="/home/yifan/plkb_sec.txt";
//		String file="/home/yifan/workspace_eclipse/iterativeTab/kbset/generator_5_5/generator9.txt";
		PlBeliefSet kb = parser.parseBeliefBaseFromFile(file);
		
		// Parse observation formula 
//		String observation="/home/yifan/obs.txt";
//		PropositionalFormula obs = (PropositionalFormula) parser.parseFormulaFromFile(observation);
		PropositionalFormula obs = (PropositionalFormula) parser.parseFormula("!e && f");
		
		// Set SAT solver
		SatSolver.setDefaultSolver(new Sat4jSolver());
		
		// consistency
		boolean consistency =kb.isConsistent();
		System.out.println("knowledge base is "+consistency+" consistent");
		if(!consistency){
			System.out.println("not a consistent kb. Stop");
			System.exit(0);
		}
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
		Vector<PropositionalFormula> hyp = tab.generationHypotheses(leaves);
		for(int i=0; i<hyp.size(); i++){
			System.out.println("hyp " +i + "  "+ hyp.elementAt(i));
		}

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

