package abd.tableau.iterative;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class IterativeMethod {

public static void main(String[] args) throws ParserException, IOException{
		
		// Create a pl knowledge base
		PlParser parser = new PlParser();
//		String file="/home/yifan/plkb.txt";
		String file="/home/yifan/workspace_eclipse/iterativeTab/kbset/generator_5_5/generator9.txt";
		PlBeliefSet kb = parser.parseBeliefBaseFromFile(file);
		
		// Parse observation formula 
//		String observation="/home/yifan/obs.txt";
//		PropositionalFormula obs = (PropositionalFormula) parser.parseFormulaFromFile(observation);
		PropositionalFormula obs = (PropositionalFormula) parser.parseFormula("p6 && p3");
		
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
		
		HashMap<PropositionalFormula, Vector<PropositionalFormula>> dict = new HashMap<PropositionalFormula, Vector<PropositionalFormula>>();
		Iterator<PropositionalFormula> it = kb.iterator();
		while(it.hasNext()){
			PropositionalFormula f = it.next();
			Set<PropositionalFormula> s = f.getLiterals();
			for(PropositionalFormula a:s){
				Vector<PropositionalFormula> v = new Vector<PropositionalFormula>();
				v.add(f);
				if(dict.get(a)!= null){
					v.addAll(dict.get(a));
				}
				dict.put(a, v);
			}	
		}
		
		Set<PropositionalFormula> keys = dict.keySet();
		for(PropositionalFormula k:keys){
			PropositionalFormula comp_k = (PropositionalFormula)k.complement();
			Vector<PropositionalFormula> values= dict.get(comp_k);
			if(values!=null){
				for(int i=0;i<values.size();i++){
					System.out.println("literal "+ k +" in formula "+values.elementAt(i));
				}
			}
		}
		
		// Get current time
		long start = System.currentTimeMillis();
		
//		IteGraph andortab = new IteGraph();
//		OrNode root = new OrNode((PropositionalFormula)obs.complement());
//		andortab.setRoot(root);
		
	}

}
