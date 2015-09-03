package abd.datastructure.graph;


import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.io.Serializable;

import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;


public class TreeNode implements Serializable {

	private int parentId;
	private int selfId;
	protected String nodeName;
	protected Object obj;
	protected TreeNode parentNode;
	protected List<TreeNode> childList;
	protected Set<PropositionalFormula> hyp;
	protected Conjunction conj;

	public TreeNode() {
		initChildList();
		initHypo();
		conj= new Conjunction();
	}

	public TreeNode(TreeNode parentNode) {
		this.getParentNode();
		initChildList();
		initHypo();
		conj= new Conjunction();
	}

	public boolean isLeaf() {
		if (childList == null) {
			return true;
		} else {
			if (childList.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/* 插入一个child节点到当前节点中 */
	public void addChildNode(TreeNode treeNode) {
		initChildList();
		childList.add(treeNode);
	}

	public void initChildList() {
		if (childList == null)
			childList = new ArrayList<TreeNode>();
	}
	
	public void initHypo(){
		if(hyp == null){
			hyp = new HashSet<PropositionalFormula>();
		}
	}

	public boolean isValidTree() {
		return true;
	}

	/* 返回当前节点的父辈节点集合 */
	public List<TreeNode> getElders() {
		List<TreeNode> elderList = new ArrayList<TreeNode>();
		TreeNode parentNode = this.getParentNode();
		if (parentNode == null) {
			return elderList;
		} else {
			elderList.add(parentNode);
			elderList.addAll(parentNode.getElders());
			return elderList;
		}
	}

	/* 返回当前节点的晚辈集合 */
	public List<TreeNode> getJuniors() {
		List<TreeNode> juniorList = new ArrayList<TreeNode>();
		List<TreeNode> childList = this.getChildList();
		if (childList == null) {
			return juniorList;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				TreeNode junior = childList.get(i);
				juniorList.add(junior);
				juniorList.addAll(junior.getJuniors());
			}
			return juniorList;
		}
	}
	
	public List<TreeNode> getLeaves(){
		List<TreeNode> leaveNodes = new ArrayList<TreeNode>();
		List<TreeNode> childList = this.getChildList();
		if (childList == null) {
			return leaveNodes;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				TreeNode junior = childList.get(i);
				if(junior.isLeaf()){
					leaveNodes.add(junior);
				}
				else{
					leaveNodes.addAll(junior.getLeaves());
				}
			}
			return leaveNodes;
		}
	}

	/* 返回当前节点的孩子集合 */
	public List<TreeNode> getChildList() {
		return childList;
	}

	/* 删除节点和它下面的晚辈 */
	public void deleteNode() {
		TreeNode parentNode = this.getParentNode();
		int id = this.getSelfId();

		if (parentNode != null) {
			parentNode.deleteChildNode(id);
		}
	}

	/* 删除当前节点的某个子节点 */
	public void deleteChildNode(int childId) {
		List<TreeNode> childList = this.getChildList();
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			TreeNode child = childList.get(i);
			if (child.getSelfId() == childId) {
				childList.remove(i);
				return;
			}
		}
	}

	/* 动态的插入一个新的节点到当前树中 */
	public boolean insertJuniorNode(TreeNode treeNode) {
		int juniorParentId = treeNode.getParentId();
		if (this.parentId == juniorParentId) {
			addChildNode(treeNode);
			return true;
		} else {
			List<TreeNode> childList = this.getChildList();
			int childNumber = childList.size();
			boolean insertFlag;

			for (int i = 0; i < childNumber; i++) {
				TreeNode childNode = childList.get(i);
				insertFlag = childNode.insertJuniorNode(treeNode);
				if (insertFlag == true)
					return true;
			}
			return false;
		}
	}

	/* 找到一颗树中某个节点 */
	public TreeNode findTreeNodeById(int id) {
		if (this.selfId == id)
			return this;
		if (childList.isEmpty() || childList == null) {
			return null;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				TreeNode child = childList.get(i);
				TreeNode resultNode = child.findTreeNodeById(id);
				if (resultNode != null) {
					return resultNode;
				}
			}
			return null;
		}
	}

	/* 遍历一棵树，层次遍历 */
	public void traverse() {
		if (selfId < 0)
			return;
		print(this.selfId);
		if (childList == null || childList.isEmpty())
			return;
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			TreeNode child = childList.get(i);
			child.traverse();
		}
	}

	public void print(String content) {
		System.out.println(content);
	}

	public void print(int content) {
		System.out.println(String.valueOf(content));
	}

	public void setChildList(List<TreeNode> childList) {
		this.childList = childList;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getSelfId() {
		return selfId;
	}

	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}

	public TreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public TreeNode clone(){
		TreeNode n = new TreeNode();
		n.setObj(this.getObj());
		n.addAllHyp(this.getHyp());
		n.setConjunction(this.getConjunction());
		return n;
	}

	public void setConjunction(Conjunction conjunction) {
		// TODO Auto-generated method stub
		conj=conjunction.clone();
		
	}

	public Conjunction getConjunction() {
		// TODO Auto-generated method stub
		return conj;
	}

	public void initOrgEntity() {
		// TODO Auto-generated method stub
		obj = new OrganizationEntity();
	}

	public void addHyp(PropositionalFormula l) {
		// TODO Auto-generated method stub
		hyp.add(l);
		conj.add(l);
		
	}
	public void addAllHyp(Set<PropositionalFormula> ls){
		hyp.addAll(ls);
		conj.addAll(ls);
	}
	
	public Set<PropositionalFormula> getHyp(){
		return hyp;
	}

	public void reduceConjunction() {
		// TODO Auto-generated method stub
		Conjunction copie = conj.clone();
		conj.clear();
	    conj.addAll(copie.getLiterals());
	}

}
