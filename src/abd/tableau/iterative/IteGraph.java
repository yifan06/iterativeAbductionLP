package abd.tableau.iterative;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import scala.Array;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class IteGraph {
 
	protected AONode root = null;
    protected Vector<AONode> nodes = new Vector<AONode>();
    protected Vector<Edge> edges = new Vector<Edge>();
    protected boolean directed = false;
    protected boolean sortedNeighbors = false;
    protected HashMap<PropositionalFormula, Vector<PropositionalFormula>> dict;
    protected ArrayList<PropositionalFormula> hypotheses;
    protected ArrayList<Conjunction> hypotheses_bis;
    
	protected HashSet<PropositionalFormula> fusion_model = new HashSet<PropositionalFormula>();
    
    public void constructTree(AONode node){
    	// recursive method
    	// In this way, we prioritize the depth of a branch
    	if(!node.termination){
    		if(node.status){
    			System.out.println("and");
    			// and node, disjunct literals are separated
    			// create a  new node  for every literals 
    			PropositionalFormula pf = node.getLiteral();
    			System.out.println("root "+pf);
    			
    			//ArrayList<AONode> sib = new ArrayList<AONode>();
    			
    			if(!pf.isLiteral()){
    				Set<PropositionalFormula> lits = pf.getLiterals();
    				for(PropositionalFormula l : lits){
    					
    					AONode newnode = new AONode(l);
    					newnode.setOrNode();

    					if(node.getPredecessor()!= null && node.getPredecessor().getLiteral().equals(l.complement())){
    						System.out.println("contradiction");
    						newnode.setTermination();
    					}
    					newnode.setPredecessor(node);
    					
    					
    					// new node copy the predecessor's rule list
    					newnode.setAppliedRules(node.getAppliedRules());
    					newnode.setLeftRules(node.getLeftRules());
    					newnode.setRule(node.getRule());
    					
    					// new node copy the model and hypotheses
    					newnode.copyModel(node.getModel());
    					if(!newnode.updateModel()){
    						newnode.setTermination();
    					}
    					// model is the complement of the literals that appear in  a node
    					// considering sibling node
    					//sib.
    					
    					node.setChildren(newnode);
    					nodes.add(newnode);
    					// add the edges between the parent and children
    					Edge e = new Edge(node, newnode);
    					edges.add(e);
    					// recursive 
    					constructTree(newnode);
    				}
    			}
    		    
    		}else{
    			System.out.println("or");

    			// or node, add new clause to the node 
    			PropositionalFormula pf = node.getLiteral();
    			if(pf.isLiteral()){
    				PropositionalFormula comp_pf = (PropositionalFormula)pf.complement();
    				Vector<PropositionalFormula> values= dict.get(comp_pf);
    				if(values!=null){
    					for(int i=0;i<values.size();i++){
    						System.out.println("literal "+ pf +" in formula "+values.elementAt(i));
    						PropositionalFormula rf = values.elementAt(i);
    						System.out.println("selected formula" + rf);
    						
    						HashSet<PropositionalFormula> applied = node.getAppliedRules();
    						for(PropositionalFormula ap : applied){
    							System.out.println("applied rules " + ap);
    						}
    						
    						if(node.getAppliedRules().contains(rf)){
    							
    							System.out.println("already applied");
    							break;
    							
    						}
    						// add every formula as a new alterantive node
    						// set as and node
    						AONode newnode = new AONode(values.elementAt(i));
    						newnode.setRule(values.elementAt(i));
        					newnode.setAndNode();

        					newnode.setPredecessor(node);
        					
        					
        					// new node copy the predecessor's rule list
        					newnode.setAppliedRules(node.getAppliedRules());
        					newnode.setLeftRules(node.getLeftRules());
        					newnode.addAppliedRule(rf);
        					newnode.deleteLeftRule(rf);
        					newnode.setRule(node.getRule());
        					
        					// new node copy the model and hypotheses
        					newnode.copyModel(node.getModel());
        					
        					node.setChildren(newnode);
        					// add the edges between the parent and children
        					Edge e = new Edge(node, newnode);
        					edges.add(e);
        					
        					
        					nodes.add(newnode);
        					// recursive 
        					constructTree(newnode);
    					}
    				}else{
    					node.setTermination();
    				}
    			}
    		}
    	}
    }
     
    public double[][] getAdjacencyMatrix() {
        double[][] adjMatrix = new double[nodes.size()][nodes.size()];
         
        for(int i = 0; i < nodes.size(); i++)
            for(int j = 0; j < nodes.size(); j++)
                if(i == j)
                    adjMatrix[i][j] = 0;
                else
                    adjMatrix[i][j] = Double.POSITIVE_INFINITY;
                 
        for(int i = 0; i < nodes.size(); i++) {
        	AONode node = nodes.elementAt(i);
            //System.out.println("Current node: " + node);
             
            for(int j = 0; j < edges.size(); j++) {
                Edge edge = edges.elementAt(j);
                 
                if(edge.a == node) {
                    int indexOfNeighbor = nodes.indexOf(edge.b);
                     
                    adjMatrix[i][indexOfNeighbor] = edge.weight;
                }
            }
        }
         
        return adjMatrix;
    }
     
    public void setDirected() {
        directed = true;
    }
     
    public void setUndirected() {
        directed = false;
    }
     
    public boolean isDirected() {
        return directed;
    }
     
    public boolean isSortedNeighbors() {
        return sortedNeighbors;
    }
     
    public void setSortedNeighbors(boolean flag) {
        sortedNeighbors = flag;
    }
     
    public int indexOf(AndNode a) {
        for(int i = 0; i < nodes.size(); i++)
//            if(nodes.elementAt(i).data.equals(a.data))
                return i;
                 
        return -1;
    }
     
    public Vector<AONode> getNodes() {
        return nodes;
    }
     
    public Vector<Edge> getEdges() {
        return edges;
    }
     
    public AONode getNodeAt(int i) {
        return nodes.elementAt(i);
    }
     
    public void unvisitAllNodes() {
        for(int i = 0; i < nodes.size(); i++)
            nodes.elementAt(i).unvisit();
    }
     
    public Vector<AONode> getNeighbors(AONode a) {
        Vector<AONode> neighbors = new Vector<AONode>();
         
        for(int i = 0; i < edges.size(); i++) {
            Edge edge = edges.elementAt(i);
             
            if(edge.a == a)
                neighbors.add(edge.b);
                 
            if(!directed && edge.b == a)
                neighbors.add(edge.a);
        }
         
        if(sortedNeighbors)
            Collections.sort(neighbors);
         
        return neighbors;
    }
     
    public int addNode(AONode a) {
        nodes.add(a);
         
        return nodes.size() - 1;
    }
     
    public void addEdge(Edge a) {
        edges.add(a);
         
        if(!directed)
            edges.add(new Edge(a.b, a.a, a.weight));
    }
     
    public void printNodes() {
        System.out.println(nodes);
    }
     
    public void printEdges() {
        System.out.println(edges);
    }

	public void setRoot(AONode root) {
		// TODO Auto-generated method stub
		this.root = root;
		nodes.addElement(root);
	}

	public void setLiteralMap(
			HashMap<PropositionalFormula, Vector<PropositionalFormula>> dict) {
		// TODO Auto-generated method stub
		this.dict = dict;
	}

	public void startExpansion() {
		// TODO Auto-generated method stub
		constructTree(root);
	}
	
	public void toDot(String filename){
		try {
			String content = "This is the content to write into file";

			File file = new File("./output/"+filename+".dot");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			//bw.write(content);
			//print nodes information and edges information
			for(int i =0;i<nodes.size();i++){
				bw.write(Integer.toString(nodes.indexOf(nodes.elementAt(i)))+ "  " + "[label=\"" + nodes.elementAt(i).getLiteral().toString()+"\"];\n");
			}
			for(int j=0;j<edges.size();j++){
				Edge e = edges.get(j);
				int na = nodes.indexOf(e.a);
				int nb = nodes.indexOf(e.b);
				String se =e.a.getLiteral().toString();
				String ee = e.b.getLiteral().toString();
				String final_se = se.replace("||", "_or_").replace("!","n");
				String final_ee = ee.replace("||", "_or_").replace("!","n");
				//bw.write(Integer.toString(na).concat("_"+final_se)+ " -> " + Integer.toString(nb).concat("_"+final_ee));
				bw.write(Integer.toString(na)+ " -> " + Integer.toString(nb));

				// add conditions to separate dotted line and concrete line
				if(!e.a.status)
					bw.write(" [style = dotted];");
				else
					bw.write(";");
				bw.write("\n");
			}
			
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateExplanation(){
		hypotheses = new ArrayList<PropositionalFormula>();
		// using breadth first search to generate sub tree		
		// generate minimal semantic explanations.
		topdownHyp(root);
		// generate minimal syntatic explanations
        oneStepGeneration(root);
		
		

		
	}

	private void topdownHyp(AONode node) {
		// TODO Auto-generated method stub
		// to do rewritten using java Queue
		// or node, list all possible hyps
		ArrayList<AONode> ch = node.getChildren();
		if(ch!=null && ch.size()>0){
			if(node.status==false){
				Iterator<AONode> it = ch.iterator();
				while(it.hasNext()){
					AONode anode = it.next();
					if(!anode.termination){
						anode.copyHyp();
						topdownHyp(anode);
					}
				}
			}else{
			// and node, combination of complement of each literals

				Iterator<AONode> it = ch.iterator();
				Conjunction h = new Conjunction();
				while(it.hasNext()){
					AONode anode = it.next();
					if(!anode.termination)
						h.add((PropositionalFormula) anode.getLiteral().complement());
					topdownHyp(anode);
				}
				node.setHyp(h);

			}
		}

		// todo update the hypotheses in the same level
		// fusion the model to check the consistency of the hypotheses.
		// 
		
	}
	
	//todo rewrite the generation part using minimal hitting set algo
	
	public void oneStepGeneration(AONode node){
		ArrayList<AONode> ch_root = node.getChildren();
		Iterator<AONode> it_ch = ch_root.iterator();
		int nb_obs = root.getLiteral().getLiterals().size();
		Vector<List<Conjunction>> conj = new Vector<List<Conjunction>>(nb_obs);
		int i=0;
		ArrayList<AONode> children_list = new ArrayList<AONode>();
		while(it_ch.hasNext()){
			AONode ch_node = it_ch.next();
			children_list.add(ch_node);
			if(ch_node.termination == true){
				ArrayList<Conjunction> ac = new ArrayList<Conjunction>();
				Conjunction c = new Conjunction();
				c.add((PropositionalFormula) ch_node.getLiteral().complement());
				ac.add(c);
				//conj.add(i, ac);
			}
			i++;
			fusion_model.addAll(ch_node.getModel());
		}
		
		for(AONode n : children_list){
			ArrayList<AONode> grandchildren = n.getChildren();
			if( grandchildren !=null){
				Iterator<AONode> it_gc = grandchildren.iterator();
				while(it_gc.hasNext()){
					AONode gc = it_gc.next();
					HashMap<PropositionalFormula, PropositionalFormula> h_gc = gc.getHyp();
					if(!h_gc.isEmpty() && h_gc != null){
					Set<PropositionalFormula> hyp_gc = new HashSet<PropositionalFormula>(h_gc.values());
					ArrayList<Conjunction> h = new ArrayList<Conjunction>();
					for(PropositionalFormula f : hyp_gc){
						Conjunction c = new Conjunction(f.getLiterals());
						h.add(c);
					}
					conj.add(h);
					}
				}
			}
		}
		ArrayList<Conjunction> result = new ArrayList<Conjunction>();
		Conjunction current = new Conjunction();
		GeneratePermutations(conj, result, 0, current);
		hypotheses_bis = result;
	}
	
	
	public void GeneratePermutations(Vector<List<Conjunction>> Lists, List<Conjunction> result, int depth, Conjunction current)
	{
	    if(depth == Lists.size())
	    {
	       result.add(current);
	       return;
	     }

	    for(int i = 0; i < Lists.get(depth).size(); ++i)
	    {
	        GeneratePermutations(Lists, result, depth + 1, current.combineWithAnd(Lists.get(depth).get(i)));
	    }
	}
	

	public ArrayList<Conjunction> getExplanations() {
		// TODO Auto-generated method stub
		return hypotheses_bis;
	}
 
}