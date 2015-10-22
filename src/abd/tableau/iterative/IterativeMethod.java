package abd.tableau.iterative;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import scala.sys.Prop;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class IterativeMethod {

	protected static PlParser parser = new PlParser();
public static void main(String[] args) throws ParserException, IOException{
	PropositionalFormula obs = (PropositionalFormula) parser.parseFormula("w && d");
	int nb_var = 5;
	int nb_formulas = 8;
	
	String output = String.format("./result/output-%1$02d-%2$02d-yifan.txt", nb_var, nb_formulas);
	Writer writer = new FileWriter(output);
	float sum = 0;
//	for(int i =0 ; i<20;i++){
//    	String file = String.format("./generator/%1$02d/Data-%1$02d-%2$02d-%3$03d.txt", nb_var, nb_formulas,i);
//		float rt = run(file, obs);
//		writer.write(rt + "\n");
//		sum = sum +rt;
//    }		
	String file = "/home/yifan/plkb.txt";
	float rt = run(file, obs);
	float mean = sum /20;
	writer.write("mean time " + mean + "\n");
    writer.close();

		
	}

public static float run(String name, PropositionalFormula observation) throws FileNotFoundException, ParserException, IOException{
	// Create a pl knowledge base
	PlParser parser = new PlParser();
	//		String file="/home/yifan/plkb.txt";
//	String file="/home/yifan/plkb_sec.txt";
	//		String file="/home/yifan/workspace_eclipse/iterativeTab/kbset/generator_5_5/generator9.txt";
	String file = name;
	PlBeliefSet kb = parser.parseBeliefBaseFromFile(file);
	
	// Parse observation formula 
//	String observation="/home/yifan/obs.txt";
//	PropositionalFormula obs = (PropositionalFormula) parser.parseFormulaFromFile(observation);
	//PropositionalFormula obs = (PropositionalFormula) parser.parseFormula("w && d");
	PropositionalFormula obs = observation;
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
	
//	// Get current time
//	long start = System.currentTimeMillis();
	
	IteGraph andortab = new IteGraph();
	PropositionalFormula obs_comp = (PropositionalFormula)obs.complement();
	AONode root = new AONode(obs_comp.toNnf());
	// set kb to root node
	Set<PropositionalFormula> k = kb.toCnf().getFormulas();
//	for(PropositionalFormula f : k){
//		System.out.println("dnf: "+f);
//	}
	HashSet<PropositionalFormula> kbrules = new HashSet<PropositionalFormula>();
	kbrules.addAll(k);
	root.setLeftRules(kbrules);
	root.setAndNode();
	andortab.setRoot(root);
	
	andortab.setLiteralMap(dict);
	andortab.startExpansion();
	andortab.generateExplanation();
	
	ArrayList<Conjunction> explanation = new ArrayList<Conjunction>();
	explanation = andortab.getExplanations();
	if(explanation.isEmpty()){
		System.out.println("no explanations");
	}else{
		Iterator<Conjunction> it_exp = explanation.iterator();
		while(it_exp.hasNext()){
			System.out.println("explanation is: "+ it_exp.next());
		}
	}
	
	// Get elapsed time in milliseconds
	long elapsedTimeMillis = System.currentTimeMillis()-start;
	
	// Get elapsed time in seconds
	float elapsedTimeSec = elapsedTimeMillis/1000F;
	
	System.out.println("elapsedTimeSec " +elapsedTimeSec);
	System.out.println("Finish");
	
	
	andortab.toDot("test");
	
	
	
	System.out.println("finished");
	return elapsedTimeSec;
}

}
