package generalSearch;
import java.util.*;

public class Node {
	int ml, mr, cl, cr;
	static ArrayList<Node> searchQueue = new ArrayList<Node>();
	boolean pBoat; 			//false is right, true is left;
	Node parentNode;
	int depth;
	ArrayList<Node> searchQueueBackup = new ArrayList<Node>();
	static ArrayList<Node> searched = new ArrayList<Node>();	
	static int count = 0;
	final int MAX_DEPTH = 12;

	//constructor
	public Node(int mlIn, int mrIn, int clIn, int crIn, boolean pBoatIn, Node parentNodeIn, int depthIn){
		ml = mlIn;
		mr = mrIn;
		cl = clIn;
		cr = crIn;
		pBoat = pBoatIn;
		parentNode = parentNodeIn;
		depth = depthIn;
	}
	//copy constructor
	public Node(Node copinIn, Node parentNodeIn){
		this(copinIn.getML(), copinIn.getMR(), copinIn.getCL(), copinIn.getCR(), copinIn.getPBoat(), parentNodeIn, parentNodeIn.getDepth() + 1);
		searchQueueBackup = copinIn.getSearchQueue();
	}

	public static void addBaseNode(Node baseIn){
		searchQueue.add(baseIn);
	}

	public int getML(){
		return  ml;
	}

	public int getMR(){
		return mr;
	}

	public int getCL(){
		return cl;
	}

	public int getCR(){
		return cr;
	}

	public boolean getPBoat(){
		return pBoat;
	}

	public Node getParentNode(){
		return parentNode;
	}

	public ArrayList<Node> getSearchQueue(){
		return searchQueueBackup;
	}

	public int getDepth(){
		return depth;
	}

	//check whether the current node is looping
	public boolean sameAsParent(Node nodeIn){
		if(parentNode == null)
			return false;
		return nodeIn.equals(parentNode) || parentNode.sameAsParent(nodeIn);
	}

	//Goal Test
	public boolean goalTest(){
		if(ml == 0 && mr == 3 && cl == 0 && cr == 3 && pBoat == false){
			System.out.println("success on "+ depth + "-----");
			success();
			return true;
		}
		return false;
	}

	//return true if fails
	//compare to parent node status and evaluate boat
	public boolean failB(){
		if(ml < 0 || mr < 0 || cl < 0 || cr < 0)
			return true;
		if(( Math.abs(ml - parentNode.getML()) + Math.abs(cl - parentNode.getCL()) ) > 2)
			return true;
		if(( Math.abs(ml - parentNode.getML()) + Math.abs(cl - parentNode.getCL()) ) == 0)
			return true;
		if((ml !=0 && ml < cl) || (mr < cr && mr != 0))
			return true;
		return false;
	}

	//main search process for each node, search the queue for the current node
	//as long as it is not empty
	public static void process(){
		while(searchQueue.size() != 0){
			Node temp = searchQueue.get(0);
			searchQueue.remove(temp);
			temp.nProcess();
		}
	}
	//nProcess stands for node process
	public void nProcess(){
		if(searchQueueBackup.size() == 0){
			count ++;
			System.out.println("Calculating node #" + count);
		}
		if(goalTest() || depth >= MAX_DEPTH)
			return;
		if(searchQueueBackup.size() != 0){
			updateParentNode(searchQueueBackup);
			searchQueue.addAll(searchQueueBackup);
		}
		else{
			generateSubnodes();
		}

	}

	//change the searched array's parent node to the current
	//node for result showing
	public void updateParentNode(ArrayList<Node> sqb){
		for(Node n : sqb)
			n = new Node(n, this);
	}

	//operator
	//determines two things:
	//1) if all parent nodes has this node
	//2) if this node had been processed
	public void generateSubnodes(){
		if(pBoat == true)
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++){
					Node temp = new Node(ml - i, mr + i, cl - j, cr + j, !pBoat, this, depth + 1);
					if(!sameAsParent(temp) && !temp.failB())   //&& temp.unique()
						searchAdd(temp);	
				}
		else
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++){
					Node temp = new Node(ml + i, mr - i, cl + j, cr - j, !pBoat, this, depth + 1);
					if(!sameAsParent(temp) && !temp.failB())  //&& temp.unique()
						searchAdd(temp);
				}
	}

	//add one node to the search queue, backup queue, and
	//the searched queue.
	public void searchAdd(Node temp){
		searchQueue.add(temp);
		searchQueueBackup.add(temp);
		searched.add(temp);
	}

	//determin whether it is unique; if not, return the previously searched node
	//for optimal purposes
	public boolean unique(){
		for(Node temp : searched)
			if(this.equals(temp) && this != temp)
				return false;
		return true;

	}

	//Document the childnode success for reuse purposes
	public void success(){
		if(parentNode != null)
			parentNode.success();
		//some lines of output
		System.out.println(this);
		return;
	}

	@Override
	public String toString(){
		String result = "";
		result += ml+ " " + cl;
		if(pBoat)
			result += " |boat|> ---------- ";
		else
			result += " ---------- <|boat| ";
		result += mr+ " " + cr;
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		else if (this.getClass()!= other.getClass()) {
			return false;
		}
		else {
			Node casted = (Node)other;
			return 
					this.getML() == casted.getML() 
					&& 
					this.getMR() == casted.getMR() 
					&& 
					this.getCL() == casted.getCL()
					&&
					this.getCR() == casted.getCR()
					&&
					this.getPBoat() == casted.getPBoat();
		}
	}
}
