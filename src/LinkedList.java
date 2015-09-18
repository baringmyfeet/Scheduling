
public class LinkedList {
	private Node listHead;
	private Node dummyNode;
	
	public LinkedList(){
		dummyNode = new Node();
		listHead = dummyNode;
	}
	
	public Node get_listHead(){
		return listHead;
	}
	
	public void set_listHead(Node n){
		listHead = n;
	}
}