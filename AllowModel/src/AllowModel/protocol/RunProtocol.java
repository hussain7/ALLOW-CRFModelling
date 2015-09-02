package AllowModel.protocol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import AllowModel.metrics.NodeIdConfidence;

public class RunProtocol {


	 
	public static void main(String[] args) throws IOException {
		
	
	
		/*for(int j=0;j< TestGraph.graph.size();j++)
		 { 
		    node = TestGraph.graph.get(j);
		    //node.buildLocalKnowlegdeModel();
		    node.dumpKnowledgeModel();
		    node.dumpTable();
		 }*/
		
		
		FileWriter filewrtier = new FileWriter("D:\\Allowlog.txt");
		PrintWriter output = new PrintWriter(filewrtier);
		for(int c=10;c< 100 ;c=c+10 )
		{	
			 int testRandomWalk =0;
			 int testNeighborTable =0 ;
			 int testcount =1;
			//make new graph
			GenerateGraph TestGraph = new GenerateGraph(c);
			AllowNode node = TestGraph.graph.get(1);
			node.triggerBuildingNetwork();
			
			
		int nNodestoCheck = c-2 ;
		double qualityRetrival1 =0;
		double qualityRetrival2 =0;
		double qualityRetrivalOverall =0;
		double qualityBest =0;
		double qualityRetrivalRandom =0;

		while(testcount > 0){
			
			Random rand = new Random();
			boolean flag = true;
			long startTime=0;
			long stopTime =0;
			int tempReturn=0;
			int numFailures=0;
			double averageRunTime=0.0;
			double averageHops=0.0;
	//	System.out.println("Enter a search method: ");
		//method = Integer.parseInt(reader.next());
   // test 
		int count =0;
		
		for(int i=1 ; i<= nNodestoCheck; i++){
		//method =3; 
		//int start =  rand.nextInt(TestGraph.n);
		int start = i;
		// test query
		Query queryRandom =TestGraph.graph.get(start).GenerateQuery(0.79,0.04);
		Query queryTable =TestGraph.graph.get(start).GenerateQuery(0.79,0.04);
		
	
	//	System.out.println(" Query started Allow NodeId  : "+(start));
		
		//String nodeConfTable = TestGraph.graph.get(start).RandomWalkWithNeighborTable(queryTable);
		NodeIdConfidence nodeConfTable = TestGraph.graph.get(start).RandomWalkWithNeighborTableQRetreival(queryTable);
		
		// test with random walk 
		
		NodeIdConfidence nodeConfRandom = TestGraph.graph.get(start).RandomWalk(queryRandom);
		
		/////////////////////
		if(nodeConfRandom != null){
		if(nodeConfRandom.getallowNodeId().equalsIgnoreCase("94")|| nodeConfRandom.getallowNodeId().equalsIgnoreCase("12") ||
				nodeConfRandom.getallowNodeId().equalsIgnoreCase("872") || nodeConfRandom.getallowNodeId().equalsIgnoreCase("6")
				|| nodeConfRandom.getallowNodeId().equalsIgnoreCase("7"))
			 { testRandomWalk++; }
		else
		  {
			// qualityRetrival2 =   qualityRetrival2  + nodeConfTable.getConfidenceValue()/qualityBest ;
			//System.out.println("Missed Node: "+nodeConfTable.getallowNodeId());
		  }
			qualityRetrivalRandom = (qualityRetrivalRandom + nodeConfRandom.getConfidenceValue()/0.99998);
		}
		
		

		if(nodeConfTable != null) {
			if(nodeConfTable.getallowNodeId().equalsIgnoreCase("94")|| nodeConfTable.getallowNodeId().equalsIgnoreCase("12") ||
					nodeConfTable.getallowNodeId().equalsIgnoreCase("872") || nodeConfTable.getallowNodeId().equalsIgnoreCase("6")
					|| nodeConfTable.getallowNodeId().equalsIgnoreCase("7"))
			{
			 testNeighborTable++; 
			 qualityBest =   nodeConfTable.getConfidenceValue();
			 //qualityRetrival1 =  qualityRetrival1 + 1 ;
			} 
		 else
		  {
			// qualityRetrival2 =   qualityRetrival2  + nodeConfTable.getConfidenceValue()/qualityBest ;
			System.out.println("Missed Node: "+nodeConfTable.getallowNodeId());
		  }
			qualityRetrivalOverall = (qualityRetrivalOverall + nodeConfTable.getConfidenceValue()/0.99998);
		}
		//qualityRetrivalOverall = (qualityRetrival1 + qualityRetrival2)/ nNodestoCheck;
		
		
		
		//System.out.println(" Query answered @ : "+nodeConfRandom.getallowNodeId()+"\n");
		//System.out.println(" Query HopeCount : "+ (query.hopCount -1)+"\n");
		System.out.println(" Query nodes visited (Random) @ : "+queryRandom.allowNodeIdsVisited.toString()+"\n");
		System.out.println(" Query nodes visited (Table) @ : "+queryTable.allowNodeIdsVisited.toString()+"\n");
		
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

		System.out.println(" Efficiency Random Walk : "+ testRandomWalk +"\n");
		System.out.println(" Efficiency Neighbor Tables : "+ testNeighborTable +"\n");
		
		System.out.println(" Retreival Qaulity Table : "+  qualityRetrivalOverall/nNodestoCheck +"\n");
		System.out.println(" Retreival Qaulity Random : "+ qualityRetrivalRandom/nNodestoCheck +"\n");
		
		
		// write  file to visualize
		
		    output.printf("%s\r\n", "Random"+","+nNodestoCheck+","+(qualityRetrivalRandom/nNodestoCheck));
		    output.printf("%s\r\n", "Allow"+","+nNodestoCheck+","+(qualityRetrivalOverall/nNodestoCheck));
		
		
	 }
		output.close();
	}

}