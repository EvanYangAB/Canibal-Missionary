package testDriver;
import depthSearch.Node;

public class DepthSearchDriver {
	public static void main(String[] args) {
		Node baseNode = new Node(3, 0, 3, 0, true, null, 1);
		baseNode.process();
		System.out.println("done");
	}
}
