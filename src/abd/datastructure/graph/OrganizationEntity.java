package abd.datastructure.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalPredicate;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;



public class OrganizationEntity {
//    public int parentId;
//    public int orgId;
//    public String orgName;
	public PropositionalFormula p;
    public ArrayList<PropositionalFormula> pf;
    public boolean consistency;
    
//    public int getParentId() {
//        return parentId;
//    }
//    public void setParentId(int parentId) {
//        this.parentId = parentId;
//    }
//    public int getOrgId() {
//        return orgId;
//    }
//    public void setOrgId(int orgId) {
//        this.orgId = orgId;
//    }
    
//    public String getOrgName() {
//        return orgName;
//    }
    
//    public void setOrgName(String orgName) {
//        this.orgName = orgName;
//    }
     public OrganizationEntity(){
         p = null;
    	 initFormulaList();
    	 consistency= true;
     }
     
     public void initFormulaList(){
    	 pf = new ArrayList<PropositionalFormula>();
     }
     
     public void setLiteral(PropositionalFormula p){
    	 this.p = p;
    	 pf.add(p);
    	 
     }
     
     public void addLiterals(ArrayList<PropositionalFormula> l){
    	 Iterator<PropositionalFormula> it = l.iterator();
    	 while(it.hasNext()){
    		 pf.add(it.next());
    	 }
     }
     
     public PropositionalFormula getLiteral(){
    	 return p;
     }

	public boolean hasLiteral() {
		// TODO Auto-generated method stub
		if(p==null)
			return false;
		else
			return true;
	}
     
     
     
     
    
//    public void setPropositionalFormula(PropositionalFormula p){
//    	this.pf = p;
//    }
//    public PropositionalFormula getPropositionalFormula(){
//    	return pf;
//    }
    
}
