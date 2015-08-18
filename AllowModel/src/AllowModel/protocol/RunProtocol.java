package AllowModel.protocol;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RunProtocol {


	 public void systemBuildingOne()
	 {
		 GenerateGraph TestGraph = new GenerateGraph();
			for(int j=0;j< TestGraph.graph.size();j++)
			 { 
				AllowNode node = TestGraph.graph.get(j);
				node.buildLocalKnowlegdeModel();
				node.buildRoutingModel();
				node.sentRoutingModeltoNeighbors();
				node.dumpCrfMap();
				node.dumpKnowledgeModel();
				node.dumpRoutingModel();
				//node.dumpTable();
			   
			 }
	 }
	public static void main(String[] args) throws IOException {
	 int test =0;
	 int testcount =10;
		while(testcount > 0){
			
		Random rand = new Random();
		boolean flag = true;
		long startTime=0;
		long stopTime =0;
		int tempReturn=0;
		int numFailures=0;
		double averageRunTime=0.0;
		double averageHops=0.0;
				
		//make new graph
		GenerateGraph TestGraph = new GenerateGraph();
		for(int j=0;j< TestGraph.graph.size();j++)
		 { 
			AllowNode node = TestGraph.graph.get(j);
			node.buildLocalKnowlegdeModel();	
			//node.dumpTable();
		   
		 }
		
		// build routing model from the leaves of tree
    	// hard coded for a time bieng
		
		for(int j=3;j<=6;j++)
		 { 
			AllowNode node = TestGraph.graph.get(j);
			node.buildRoutingModel();
			node.sentRoutingModeltoNeighbors();
			node.dumpCrfMap();
			node.dumpKnowledgeModel();
			node.dumpRoutingModel();
			//node.sentRoutingModeltoNeighbors();
			//node.sentRoutingModeltoNeighbors();
			
			node.dumpTable();
		   
		 }
		
		// building upper nodes Routing model
		for(int j=1;j<=2;j++)
		 { 
			AllowNode node = TestGraph.graph.get(j);
			node.buildRoutingModel();
			node.sentRoutingModeltoNeighbors();
			node.dumpCrfMap();
			node.dumpKnowledgeModel();
			node.dumpRoutingModel();
			//node.sentRoutingModeltoNeighbors();
			//node.sentRoutingModeltoNeighbors();
			
			node.dumpTable();
		   
		 }
		// finally create model of root node
		AllowNode node = TestGraph.graph.get(0);
		node.buildRoutingModel();
		node.dumpCrfMap();
		node.dumpKnowledgeModel();
		node.dumpRoutingModel();
		node.dumpTable();
		
		Scanner reader = new Scanner(System.in);
		int method;

	//	System.out.println("Enter a search method: ");
		//method = Integer.parseInt(reader.next());
   // test 
		int count =0;
		for(int i=0; i<10; i++){
		method =3; 
		//int start =  rand.nextInt(TestGraph.n);
		int start =0;
		// test query
		Query query =TestGraph.graph.get(start).GenerateQuery(0.79,0.04);
		System.out.println(" Query started Allow NodeId  : "+(start+1));
		String nodeid = TestGraph.graph.get(start).RandomWalkWithNeighborTable(query);
		if(nodeid.equalsIgnoreCase("4") )
			test++;
		System.out.println(" Query answered @ : "+nodeid+"\n");
		System.out.println(" Query HopeCount : "+ (query.hopCount -1)+"\n");
		System.out.println(" Query nodes visited @ : "+query.allowNodeIdsVisited.toString()+"\n");
		
		}
		//How many times to test the system
	/*	for(int i=0; i<10000; i++){
			//choose node to start search from
			int start =  rand.nextInt(TestGraph.n);

		
			
			/*switch(method) {
			case 1: tempReturn = TestGraph.graph.get(start).Flood(TestGraph.graph.get(start).GenerateQuery(0.003f,0.0004f));
			break;

			case 2: tempReturn = TestGraph.graph.get(start).RandomWalk(TestGraph.graph.get(start).GenerateQuery(0.03f));
			break;

			case 3: tempReturn = TestGraph.graph.get(start).RandomWalkWithNeighborTable(TestGraph.graph.get(start).GenerateQuery(file));
			break;
			}

			//take down start time
			startTime = System.currentTimeMillis();


			/*
			 * 
			 * execute a search
			 * Three different types Flood, RandomWalk, and RandomWalkWithNeighborTable
			 * 
			 * 




			//take down search end time
			stopTime = System.currentTimeMillis();

			if(tempReturn != -100){
				averageRunTime += (stopTime-startTime);
				averageHops += TestGraph.graph.get(start).currentQuery.hopCount;
			}
			else{
				numFailures++;
			}

		}

		System.out.println("Average Time for 10000 runs is: " + (averageRunTime/(10000.0-numFailures)));
		System.out.println("Average Hops for 10000 runs is: " + (averageHops/(10000.0-numFailures)));
		System.out.println("Number of Failures is: " + numFailures);

   

	}*/ 
		testcount--;
		}

		System.out.println(" Efficiency @ : "+test+"\n");
	}

}