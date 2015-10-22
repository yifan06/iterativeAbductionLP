package abd.tableau.iterative;
//
//import com.bpodgursky.jbool_expressions.And;
//import com.bpodgursky.jbool_expressions.Expression;
//import com.bpodgursky.jbool_expressions.Not;
//import com.bpodgursky.jbool_expressions.Or;
//import com.bpodgursky.jbool_expressions.Variable;
//
//
//
//public class Test {
//  public static void main(String[] args){
//	  
//	  // testing basic functions of jbool_expression library
//    System.out.println("hello");
//    Expression<String> expr = And.of(Variable.of("A"),
//        Variable.of("B"),
//        Or.of(Variable.of("C"), Not.of(Variable.of("C"))));
// 
//    System.out.println(expr);
//    //  ((!C | C) & A & B)  
//  }
//}
//
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.sf.tweety.commons.BeliefBaseSampler;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.DHitInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.DMaxInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.DSumInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.DfInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.NaiveMusEnumerator;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.analysis.ContensionInconsistencyMeasure;
import net.sf.tweety.logics.pl.analysis.DalalDistance;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.LingelingSolver;
import net.sf.tweety.logics.pl.sat.MarcoMusEnumerator;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.semantics.PossibleWorldIterator;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Contradiction;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.pl.util.CnfSampler;
import net.sf.tweety.math.func.FracAggrFunction;

public class Test {
public static void main(String[] args) throws ParserException, IOException{
	
	//
//	PropositionalSignature sig = new PropositionalSignature(10);
//	
//	BeliefBaseSampler<PlBeliefSet> sampler = new CnfSampler(sig, 0.3);
//	
//	for(int i = 0; i < 10; i++)
//		System.out.println(sampler.randomSample(5, 10));

	
	
	
	// Create a pl knowledge base
	PlParser parser = new PlParser();
	String file="/home/yifan/generator.txt";
	PlBeliefSet kb = parser.parseBeliefBaseFromFile(file);
	
	// Set SAT solver
	SatSolver.setDefaultSolver(new Sat4jSolver());
	
	// consistency
	System.out.println("knowledge base is "+ kb.isConsistent()+" consistent");
	
	PropositionalFormula oa = (PropositionalFormula) parser.parseFormula("s");
	PropositionalFormula od = (PropositionalFormula) parser.parseFormula("!s");
	PropositionalFormula ob = (PropositionalFormula) parser.parseFormula("d || c");
	PropositionalFormula oc = (PropositionalFormula) parser.parseFormula("d || ! c");
	Conjunction c =new Conjunction();
	Conjunction d =new Conjunction();
//	d.addAll(oc.getLiterals());
	c.add(ob);
	c.add(oa);
	d.add(oc);	
	d.add(ob);
	
	System.out.println("hashcode " + oa + " : " + oa.hashCode());
	System.out.println("hashcode " + od + " : " + od.hashCode());
	System.out.println("hashcode " + ob + " : " + ob.hashCode());
	System.out.println("hashcode " + oc + " : " + oc.complement().hashCode());
	System.out.println("signature " + oc + " : " + oc.getSignature());

	
	
	//	PropositionalFormula od;
//	od.oc.getLiterals()
	HashSet<PropositionalFormula> ha = new HashSet<PropositionalFormula>();
	HashSet<PropositionalFormula> hc = new HashSet<PropositionalFormula>();
	ha.add(oa);
	ha.add(ob);
	hc.add(oc);
	System.out.println("xx" + ha.contains(oc));
	HashSet<String> has = new HashSet<String>();
	HashSet<String> hcs = new HashSet<String>();
	has.add(oa.toString());
	has.add(ob.toString());
	hcs.add(oc.toString());
	System.out.println(oa.toString());
	System.out.println(oc.toString());
	System.out.println(ob.toString().equals(oc.toString()));
	System.out.println(ha.containsAll(hc));

	System.out.println(has.contains(hcs));

//	System.out.println(c);
	
	/*
	if(oa.equals(ob))
		System.out.println("yes");
	
	PlBeliefSet x = new PlBeliefSet();
	x.add(oa);
	x.add(ob);
	
	PropositionalFormula oc = (PropositionalFormula) parser.parseFormula("s");
	PlBeliefSet y = new PlBeliefSet();
	y.add(oc);
	if(y.contains(x))
		System.out.println("yess");
		
	// singature 
	Signature sg = kb.getSignature();
	System.out.println(sg.getClass());
	System.out.println(sg.toString());
//	
//	System.out.println(file);
	
	// print every formula in the terminal
	System.out.println(kb.toString()); 
	Iterator<PropositionalFormula> it = kb.iterator();
	while(it.hasNext()){
		PropositionalFormula pf = it.next();
		
		System.out.println(pf.toString());
		System.out.println("NNF " +pf.toNnf());
		System.out.println("NNF C " +pf.toNnf().collapseAssociativeFormulas());
		System.out.println("DNF " +pf.toDnf());
		System.out.println("DNF C " +pf.toDnf().collapseAssociativeFormulas());
		
		PropositionalFormula nnf = pf.toDnf().collapseAssociativeFormulas();
		
	    // DNF( P || Q) = DNF(P) || DNF(Q)
	    if(nnf instanceof Disjunction) {
	      Disjunction d = (Disjunction) nnf;
	      d.remove(new Contradiction());
	      Disjunction dnf = new Disjunction();
	      for(PropositionalFormula f : d) {
		      System.out.println("dnf " + f.toNnf());
	      }
		}

		
		System.out.println("CNF " +pf.toCnf());
		System.out.println("CNF C" +pf.toCnf().collapseAssociativeFormulas());
		System.out.println("atoms "+ pf.getLiterals());
		
		Set<PropositionalFormula> lits = pf.getLiterals();
		Vector<PropositionalFormula> a = new Vector<PropositionalFormula>(lits);
		Vector<PropositionalFormula> b = new Vector<PropositionalFormula>(lits);
		
		outerloop:
		for(int i=0; i< a.size();i++){
			for(int j=i; j< a.size();j++){
				if(a.elementAt(i).equals(a.elementAt(j).complement())){
					System.out.println(a.elementAt(i));
					System.out.println(a.elementAt(j));
					System.out.println(false);
					break outerloop;
				}
			}
		}
		
//		Iterator<PropositionalFormula> it1 = lits.iterator();
//		Iterator<PropositionalFormula> it2 = lits.iterator();
//		while(it1.hasNext()){
//			PropositionalFormula r = it1.next();
//			ArrayList<PropositionalFormula> al = (ArrayList<PropositionalFormula>) lits.toArray();
//				PropositionalFormula c = it2.next();
//				System.out.println(" r "+r);
//				System.out.println(" c "+c);
//				if(r.equals(c.complement())){
//					System.out.println("contradiction");
//				}
//			
//		}	
	}
	*/
}
}