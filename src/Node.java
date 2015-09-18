public class Node {
	private int jobId, time;
	private Node next;
	
	public Node(){
		 jobId = 0;
	     time = 0;
	     next = null;
	} 
	
	public Node(int j){
		jobId = j;
		next = null;
	}
	
	public Node(int j, int t){
        jobId = j;
        time = t;
        next = null;
    }
	
	public void setJobId(int j){
		jobId = j;
	}
	
	public int getJobId(){
        return jobId;
    }
	
	public void setTime(int t){
        time = t;
    }
	
	public int getTime(){
		return time;
	}
	
	public void setNext(Node n){
		next = n;
	}
	
	public Node getNext(){
		return next;
	}
}