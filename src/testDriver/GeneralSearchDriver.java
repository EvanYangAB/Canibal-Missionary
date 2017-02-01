package testDriver;
import generalSearch.Node;

public class GeneralSearchDriver {
		public static void main(String[] args) {
			Node baseNode = new Node(3, 0, 3, 0, true, null, 1);
			Node.addBaseNode(baseNode);
			Node.process();
			System.out.println("done");			
		}
}
