package depthSearch;
import java.util.*;

public class Node {
	int ml, mr, cl, cr;
	// position of the boat.
	// false represents the right side of the river, true represents the left;
	boolean pBoat; 			
	boolean allFail = false, hasSucChild = false;
	Node parentNode;
	int depth;
	ArrayList<Node> searchQueue = new ArrayList<Node>();
	ArrayList<Node> searchQueueBackup = new ArrayList<Node>();
	static ArrayList<Node> searched = new ArrayList<Node>();	
	static int count = 0;
	final int MAX_DEPTH = 80;

	// constructor
	public Node(int mlIn, int mrIn, int clIn, int crIn, boolean pBoatIn, Node parentNodeIn, int depthIn){
		ml = mlIn;
		mr = mrIn;
		cl = clIn;
		cr = crIn;
		pBoat = pBoatIn;
		parentNode = parentNodeIn;
		depth = depthIn;
		searched.add(this);
	}

	// copy constructor
	public Node(Node copinIn, Node parentNodeIn){
		this(copinIn.getML(), copinIn.getMR(), copinIn.getCL(), copinIn.getCR(), copinIn.getPBoat(), parentNodeIn, parentNodeIn.getDepth() + 1);
		searchQueueBackup = copinIn.getSearchQueue();
		searched.add(this);
	}

	// bunch of getters
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

	// main search process. search the queue as long as it is not empty.
	// use the searched result if the node has already been processed.
	public void process(){
		if(searchQueueBackup.size() == 0 && allFail == false){
			count ++;
			System.out.println("Calculating node #" + count);
		}
		if(goalTest() || depth >= MAX_DEPTH || allFail)
			return;
		if(searchQueueBackup.size() != 0 && allFail == false)
			searchQueue = searchQueueBackup;
		else{
			searchQueue = generateSubnodes();
			searchQueueBackup = (ArrayList) searchQueue.clone();
		}
		while(searchQueue.size() != 0){
			Node temp = searchQueue.get(0);
			searchQueue.remove(temp);
			temp.process();
		}
		allFail = !hasSucChild;
		return;

	}

	// operator
	// determines two things:
	// 1) if all parent nodes has this node
	// 2) if this node had been processed
	public ArrayList<Node> generateSubnodes(){
		ArrayList<Node> result = new ArrayList<Node>();
		if(pBoat == true)
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++){
					Node temp = new Node(ml - i, mr + i, cl - j, cr + j, !pBoat, this, depth + 1);
					if(!sameAsParent(temp) && !temp.failB())
						result.add(temp.unique());
				}
		else
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++){
					Node temp = new Node(ml + i, mr - i, cl + j, cr - j, !pBoat, this, depth + 1);
					if(!sameAsParent(temp) && !temp.failB())
						result.add(temp.unique());
				}
		return result;
	}

	// determins whether it is unique; if not, return the previously searched node
	// for optimal purposes
	public Node unique(){
		Node result = this;
		for(Node temp : searched)
			if(result.equals(temp))
				if(result.getDepth() > temp.getDepth())
					result = temp;
		if(result != this){
			System.out.println("at depth: " + depth + " This node had been searched. substituted");
			return new Node(result, parentNode);
		}
		return this;

	}

	// checks whether the current node is looping and end this thread if
	// it is looping.
	public boolean sameAsParent(Node nodeIn){
		if(parentNode == null)
			return false;
		return nodeIn.equals(parentNode) || parentNode.sameAsParent(nodeIn);
	}

	// the Failure Test
	// returns true if fails
	// compares to parent node status for avalibility and evaluates boat position
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

	// output the childnode success for reuse purposes
	public void success(){
		hasSucChild = true;
		if(parentNode != null)
			parentNode.success();
		//some lines of output
		System.out.println(this);
		return;
	}

	// Goal Test
	public boolean goalTest(){
		if(ml == 0 && mr == 3 && cl == 0 && cr == 3 && pBoat == false){
			System.out.println("success on "+ depth + "-----");
			success();
			return true;
		}
		return false;
	}
	
	// the toString method. Just to get a good look at the nodes
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