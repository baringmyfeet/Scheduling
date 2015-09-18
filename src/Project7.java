import java.util.*;
import java.io.*;

public class Project7 {
	public static File dependPairFile,oneJobTimeFile,varJobTimeFile,numProcFile,
	dependPairCycleFile,jobTimeCycleFile;
	public static PrintWriter outFile;
	public static int numberNodes=0,procNeed=0,totalJobTime=0;
	public static int[][] scheduleTable;
	public static LinkedList open = new LinkedList();
	public static LinkedList dependList;
	public static Node[] hashTable;
	public static int[] processJob,processTime,parentCount,jobTime,jobDone,jobMarked;
	
	public static void printInputs(){
		try{
			Scanner in1 = new Scanner(dependPairFile);
			Scanner in2 = new Scanner(oneJobTimeFile);
			Scanner in3 = new Scanner(varJobTimeFile);
			Scanner in4 = new Scanner(numProcFile);
			Scanner in5 = new Scanner(dependPairCycleFile);
			Scanner in6 = new Scanner(jobTimeCycleFile);
			outFile.println("The input one representing the dependency graph");
			numberNodes = in1.nextInt();
			outFile.println(numberNodes);
			int node1=0,node2=0;
			while(in1.hasNextInt()){
				node1 = in1.nextInt();
		        node2 = in1.nextInt();
		        outFile.println(node1+" "+node2);
			}
			outFile.println();
			in1.close();
			
			outFile.println("The input two contains the one unit time requirements for jobs");
		    numberNodes = in2.nextInt();
		    outFile.println(numberNodes);
		    int node3=0, timeNeed=0;
		    while (in2.hasNextInt()){
		        node3 = in2.nextInt();
		        timeNeed = in2.nextInt();
		        outFile.println(node3+" "+timeNeed);
		    }
		    outFile.println();
		    in2.close();
		    outFile.println("The input three contains the various unit time requirements for jobs");
		    numberNodes = in3.nextInt();
		    outFile.println(numberNodes);
		    while (in3.hasNextInt()) {
		    	node3 = in3.nextInt();
		        timeNeed = in3.nextInt();
		        outFile.println(node3+" "+timeNeed);
		    }
		    outFile.println();
		    in3.close();
		    outFile.println("The input four contains the number of processors");
		    int numProc=0;
		    while (in4.hasNextInt()) {
		    	numProc = in4.nextInt();
		        outFile.println(numProc);
		    }
		    outFile.println();
		    in4.close();
		    outFile.println("The input five contains the dependency pairs with cycles");
		    numberNodes = in5.nextInt();
		    outFile.println(numberNodes);
		    while (in5.hasNextInt()) {
		    	node3 = in5.nextInt();
		        timeNeed = in5.nextInt();
		        outFile.println(node3+" "+timeNeed);
		    }
		    outFile.println();
		    in5.close();
		    outFile.println("The input six contains time requirement for dependency pairs with cycles");
		    numberNodes = in6.nextInt();
		    outFile.println(numberNodes);
		    while (in6.hasNextInt()) {
		    	node3 = in6.nextInt();
		        timeNeed = in6.nextInt();
		        outFile.println(node3+" "+timeNeed);
		    }
		    outFile.println();
		    in6.close();
		}
		catch(IOException e){
			System.out.println(e);
		}	
	}
	
	public static boolean allJobDone(){
	    int numJobDone=0;
	    for (int i=1; i<numberNodes+1; i++) {
	        if (jobDone[i] == 1) {
	            numJobDone++;
	        }
	    }
	    if (numJobDone == numberNodes) {
	        return true;
	    }
	    else
	        return false;
	}
	
	public static void nodeToOpen(int node, int time){
	    Node newNode = new Node(node,time);
	    Node walker = open.get_listHead();
	    while (walker.getNext() != null) {
	        walker = walker.getNext();
	    }
	    newNode.setNext(walker.getNext());
	    walker.setNext(newNode);
	}

	public static void printOpen(){
	    Node walker = open.get_listHead().getNext();
	    while (walker != null) {
	        System.out.println("("+walker.getJobId()+", "+walker.getTime()+")");
	        walker = walker.getNext();
	    }
	    System.out.println();
	}

	public static void removeFromOpen(){
	    Node firstNode = open.get_listHead().getNext();
	    open.get_listHead().setNext(firstNode.getNext());
	}
	
	public static void insertDependcy(Node lh, int n){
		Node newNode = new Node(n);
	    Node walker = lh;
	    while (walker.getNext() != null){
	        walker = walker.getNext();
	    }
	    newNode.setNext(walker.getNext());
	    walker.setNext(newNode);
	}
	
	public static void printHash(Node lh){
	    Node walker = lh.getNext();
	    while (walker != null) {
	        System.out.print(walker.getJobId()+ " ");
	        walker = walker.getNext();
	    }
	}
	
	public static void dependencyGraph(File dependPairFile, File jobTimeFile, int procNeed){
		try{
			Scanner in = new Scanner(dependPairFile);
			Scanner in1 = new Scanner(jobTimeFile);
			numberNodes = in.nextInt();
			numberNodes = in1.nextInt();
			hashTable = new Node[numberNodes+1];
			for (int i=1; i<numberNodes+1; i++){
				hashTable[i] = new Node();
			} // create array of reference in Java
		    processJob = new int[numberNodes+1];
		    processTime = new int[numberNodes+1];
		    parentCount = new int[numberNodes+1];
		    jobTime = new int[numberNodes+1];
		    jobDone = new int[numberNodes+1];
		    jobMarked = new int[numberNodes+1];
		    for (int i=1; i<numberNodes+1; i++) {
		        dependList = new LinkedList();
		        dependList.set_listHead(hashTable[i]);
		        processJob[i] = 0;
		        processTime[i] = 0;
		        parentCount[i] = 0;
		        jobTime[i] = 0;
		        jobDone[i] = 0;
		        jobMarked[i] = 0;
		    } // step 0
		    int node=0,jobTimeNeeded=0;
		    totalJobTime=0;
			while(in1.hasNextInt()){
				node = in1.nextInt();
				jobTimeNeeded = in1.nextInt();
				jobTime[node] = jobTimeNeeded;
		        totalJobTime = totalJobTime+jobTimeNeeded;
			}
			in1.close();
			if (procNeed > numberNodes)
		        procNeed = numberNodes;
		    scheduleTable = new int[numberNodes+1][totalJobTime+1];
		    System.out.print(numberNodes+" ");
		    for(int i=1; i<numberNodes+1;i++){
		        for(int j=1; j<totalJobTime; j++){
		            scheduleTable[i][j] = 0;
		        }
		    }
		    
		    int node1=0, node2=0;
		    while(in.hasNextInt()){
				node1 = in.nextInt();
				node2 = in.nextInt();
				parentCount[node2]++;
		        insertDependcy(hashTable[node1],node2);
			}
		    in.close();
		    for (int i=1; i<numberNodes+1; i++) {
		    	System.out.print(i+" ");
		    	printHash(hashTable[i]);
		    	System.out.println();
		    }// debug hashtable
		    int procUsed=0,time=0;
		    while (!allJobDone()) {
		        int orphanNode=0,newJob=0,availProc=0,needTime=0;
		        for (int i=1; i<numberNodes+1; i++) {
		            if (parentCount[i] == 0 && jobMarked[i] == 0) {
		                orphanNode = i;
		                jobMarked[orphanNode] = 1;
		                nodeToOpen(orphanNode,jobTime[orphanNode]);
		            }
		        } //step 1
		        //printOpen(); //debug open list
		        if (procUsed>procNeed) {
		            procUsed--;
		        }
		        while (!(open.get_listHead().getNext() == null || procUsed > procNeed)) {
		            availProc = -1;
		            for (int i=1; i<=procUsed; i++) {
		                if (processJob[i]<=0) {
		                    availProc = i;
		                    break;
		                }
		            }
		            if (availProc == -1) {
		                procUsed++;
		                availProc = procUsed;
		            }

		            if (procUsed <= procNeed) {
		                Node jobTimePair = open.get_listHead().getNext();
		                newJob = jobTimePair.getJobId();
		                needTime = jobTimePair.getTime();
		                removeFromOpen();
		                processJob[availProc] = newJob;
		                processTime[availProc] = needTime;
		                for (int i=1; i<=needTime; i++) {
		                    scheduleTable[availProc][time+i] = newJob;
		                }
		            }
		        }// step 3
		        int countProcDone=0,countJobDone=0;
		        if (open.get_listHead().getNext() == null) {
		            for (int i=1; i<numberNodes+1; i++) {
		                if (processJob[i]==0){
		                    countProcDone++;
		                }
		                if (jobDone[i]==1) {
		                    countJobDone++;
		                }
		            
		            }
		            if (countProcDone==numberNodes && countJobDone != numberNodes) {
		            	System.out.print("there is a cycle in the graph for data file designed to create cycle");
		                outFile.println("there is a cycle in the graph for data file designed to create cycle");
		                return; // exit the program
		            }
		        }// step 4
		    
		        System.out.println("step 5 debugging");
		        System.out.println("time: "+time);
		        System.out.println("procneed "+procNeed);
		        System.out.println("procused "+procUsed);
		        System.out.print("processJob array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(processJob[i]+" ");
		        }
		        System.out.println();
		        System.out.print("processTime array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(processTime[i]+" ");
		        }
		        System.out.println();
		        System.out.print("parentCount array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(parentCount[i]+" ");
		        }
		        System.out.println();
		        System.out.print("jobTime array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(jobTime[i]+" ");
		        }
		        System.out.println();
		        System.out.print("jobDone array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(jobDone[i]+" ");
		        }
		        System.out.println();
		        System.out.print("jobMarked array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(jobMarked[i]+" ");
		        }
		        System.out.println();
		        for(int i=1; i<numberNodes+1;i++){
		            for(int j=1; j<totalJobTime+1; j++){
		            	System.out.print(scheduleTable[i][j]+" ");
		            }
		            System.out.println();
		        }
		        System.out.println();
		        time++;
		        for (int i=1; i<numberNodes+1; i++) {
		            processTime[i]--;
		        }// step 7
		        for (int i=1; i<numberNodes+1; i++) {
		            int job=0;
		            if (processTime[i] == 0) {
		                job = processJob[i];
		                processJob[i] = 0;
		                jobDone[job] = 1;
		                Node hashListHead = hashTable[job];
		                Node hashWalker = hashListHead.getNext();
		                while (hashWalker != null) {
		                    parentCount[hashWalker.getJobId()]--;
		                    hashWalker = hashWalker.getNext();
		                }
		            }
		        } // step 9
		        System.out.println("step 10 debugging");
		        System.out.println("time: "+time);
		        System.out.println("procneed "+procNeed);
		        System.out.println("procused "+procUsed);
		        System.out.print("processJob array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(processJob[i]+" ");
		        }
		        System.out.println();
		        System.out.print("processTime array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(processTime[i]+" ");
		        }
		        System.out.println();
		        System.out.print("parentCount array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(parentCount[i]+" ");
		        }
		        System.out.println();
		        System.out.print("jobTime array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(jobTime[i]+" ");
		        }
		        System.out.println();
		        System.out.print("jobDone array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(jobDone[i]+" ");
		        }
		        System.out.println();
		        System.out.print("jobMarked array ");
		        for (int i=1; i<numberNodes+1; i++) {
		        	System.out.print(jobMarked[i]+" ");
		        }
		        System.out.println();
		        for(int i=1; i<numberNodes+1;i++){
		            for(int j=1; j<totalJobTime+1; j++){
		            	System.out.print(scheduleTable[i][j]+" ");
		            }
		            System.out.println();
		        }
		        System.out.println();
		    }
		    outFile.println("The file name is "+jobTimeFile+",the final scheduleTable with number of processors equal to "+procNeed);
		    for(int i=1; i<numberNodes+1;i++){
		        for(int j=1; j<totalJobTime+1; j++){
		            outFile.print(scheduleTable[i][j]+" ");
		        }
		        outFile.println();
		    }
		    outFile.println();

		}
		catch(IOException e){
			System.out.println(e);
		}	
	}
	
	public static void main(String[] args){
		try{
			dependPairFile = new File(args[0]);
			oneJobTimeFile = new File(args[1]);
			varJobTimeFile = new File(args[2]);
			numProcFile = new File(args[3]);
			outFile = new PrintWriter(args[4]);
			dependPairCycleFile = new File(args[5]);
			jobTimeCycleFile = new File(args[6]);
			printInputs();
			Scanner in = new Scanner(numProcFile);
			while(in.hasNextInt()){
				procNeed = in.nextInt();
				dependencyGraph(dependPairFile,oneJobTimeFile,procNeed);
				dependencyGraph(dependPairFile,varJobTimeFile,procNeed);
			}
			dependencyGraph(dependPairCycleFile, jobTimeCycleFile, procNeed);
			in.close();
			outFile.close();
		}
		catch(IOException e){
			System.out.println(e);
		}	
	}
}
