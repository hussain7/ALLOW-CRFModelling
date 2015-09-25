package AllowModel.protocol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import AllowModel.metrics.NodeIdConfidence;
import AllowModel.model.ExplorationQueryElement;

public class RunProtocol {
	static int selectivity = 20;
    @SuppressWarnings("unused")
	public static void queryLearning() throws IOException
    {
    	
    	int progapationHopCount = 12;
    	int numberExploreQueries = 100;
    	int allowNodesNumber = 200;
    	int queryConf = 6;
    	
    	double qualityRetrivalOverall =0;
		double qualityBest =0;
    	
		GenerateGraph TestGraph = new GenerateGraph(allowNodesNumber);
		//AllowNode node = TestGraph.graph.get(1);
		//node.triggerBuildingNetwork(5);
		 int networkSize = TestGraph.graph.size();
		for(int k=0; k< networkSize ;k++){	
			
	    	 AllowNode allownode = TestGraph.graph.get(k); 
	    	 allownode.buildLocalKnowlegdeModel();  
	    	 allownode.dumpKnowledgeModel();
	    	}
		
		Random rand = new Random();
		FileWriter filewrtier = new FileWriter("D:\\QueryLearning.txt");
		PrintWriter output = new PrintWriter(filewrtier);
		for(int sel =1 ;sel <= selectivity ;sel++) {
		for(numberExploreQueries=200 ;numberExploreQueries <=200;numberExploreQueries= numberExploreQueries+100){
			
	   
    	for(int c=1; c <= numberExploreQueries ; c++)
		{	
    		// for generating different types of queries
    	List<Integer> queryScopeCRFNodes =  new ArrayList<Integer>();
    	generateRandomQueryScopes( queryScopeCRFNodes,  rand.nextInt(queryConf));
    	
       for(int k=0; k< networkSize ;k++)
    	{
		AllowNode allownode = TestGraph.graph.get(k);
		List<AllowNode> neighborList = allownode.getNeighbors();
		
		for(AllowNode tempNode: neighborList){
			ExplorationQuery queryExploration =TestGraph.graph.get(k).
					GenerateExplorationQuery(sel,progapationHopCount,queryScopeCRFNodes);
			
			tempNode.RandomWalkExplorationQuery(queryExploration,allownode.getAllowNodeId());
			double maxConfidence = queryExploration.maxConfidence;
			ExplorationQueryElement queryElement = new ExplorationQueryElement(queryExploration,maxConfidence);
			
			if(allownode.getQueryLearnMap().containsKey(tempNode.getAllowNodeId()))
			{
				List<ExplorationQueryElement> queryList = allownode.getQueryLearnMap().get(tempNode.getAllowNodeId());
				if(queryList == null)
				{
					queryList = new ArrayList<ExplorationQueryElement>();
				}
					queryList.add(queryElement);
			
			}
			else
			{   List<ExplorationQueryElement> queryList  = new ArrayList<ExplorationQueryElement>();
			    queryList.add(queryElement);
				allownode.getQueryLearnMap().put(tempNode.getAllowNodeId(),queryList);
				
			}
			
		  }
		
	    }
	  }
    	
    	for(int k=0; k< networkSize ;k++){
    		
    	 AllowNode allownode = TestGraph.graph.get(k);
    	// allownode.dumpQueryLearningMap(allownode.getAllowNodeId());
    	 allownode.buildRegressionMap();
    	 
    	}
    	
    	//testQuery(TestGraph,output,numberExploreQueries);
     }	
		testQuery(TestGraph,output,numberExploreQueries,sel);
		}
		output.close();
   }
    
    public static void generateRandomQueryScopes(List<Integer> queryScopeCRFNodes, int conf){
    	
    	if(conf ==1) {
    	queryScopeCRFNodes.add(1);
    	queryScopeCRFNodes.add(2);
    	}
    	else if(conf == 2){
    		queryScopeCRFNodes.add(1);
        	queryScopeCRFNodes.add(2);
        	queryScopeCRFNodes.add(3);
        	
    	}
        else if(conf == 3){
        	queryScopeCRFNodes.add(4);
        	queryScopeCRFNodes.add(5);
        	queryScopeCRFNodes.add(6);
        	queryScopeCRFNodes.add(7);
        	
        	// queryScopeCRFNodes.add(13);
         	//queryScopeCRFNodes.add(14);
          //queryScopeCRFNodes.add(15);
        //	queryScopeCRFNodes.add(1);
    	}
        else if(conf == 4){
	
        	queryScopeCRFNodes.add(2);
        	queryScopeCRFNodes.add(3);
       	// queryScopeCRFNodes.add(11);
        	//queryScopeCRFNodes.add(12);
         //queryScopeCRFNodes.add(13);
       	//queryScopeCRFNodes.add(14);
        
         }
         else if(conf == 5){
        	 queryScopeCRFNodes.add(1);
         	queryScopeCRFNodes.add(2);
         	queryScopeCRFNodes.add(3);
         	queryScopeCRFNodes.add(4);
        	 queryScopeCRFNodes.add(5);
          	queryScopeCRFNodes.add(7);
          	// queryScopeCRFNodes.add(15);
           	//queryScopeCRFNodes.add(16);
            //queryScopeCRFNodes.add(18);
          //	queryScopeCRFNodes.add(17);
    	}
         else if(conf == 6){
        	 queryScopeCRFNodes.add(6);
         	 queryScopeCRFNodes.add(2);
         	 queryScopeCRFNodes.add(3);
         	 queryScopeCRFNodes.add(4);
        	 queryScopeCRFNodes.add(5);
          //	 queryScopeCRFNodes.add(16);
          	//queryScopeCRFNodes.add(17);
           	//queryScopeCRFNodes.add(18);
            //queryScopeCRFNodes.add(19);
          	//queryScopeCRFNodes.add(20);
    	}
         else{
        	 queryScopeCRFNodes.add(2);
         	 queryScopeCRFNodes.add(3);
         	 //queryScopeCRFNodes.add(1);
          	//queryScopeCRFNodes.add(42);
          	//queryScopeCRFNodes.add(33);
          	//queryScopeCRFNodes.add(43);
         	 //queryScopeCRFNodes.add(45);
           	//queryScopeCRFNodes.add(47);
           	// queryScopeCRFNodes.add(15);
            	//queryScopeCRFNodes.add(16);
             //queryScopeCRFNodes.add(18);
           	//queryScopeCRFNodes.add(17);
         }
    }
   
    
    public static double getGlobalBestConfidence(GenerateGraph TestGraph,List<Integer> queryCrfNodes)
    {
   
        int size = TestGraph.graph.size();
        double maxConfidence = 0.0001;
    	for(int k=0; k< size ;k++){
    		
       	 AllowNode allownode = TestGraph.graph.get(k);
       	
       	 double scopeConfidence =allownode.getConfidenceforQueryScope(queryCrfNodes);
       	 if(maxConfidence < scopeConfidence){
       		maxConfidence = scopeConfidence;
       	  }
       	 
       	}
		return maxConfidence;
    }
   
    public static void testQuery(GenerateGraph TestGraph,PrintWriter output, int numberExploreQueries,int sel)
    {
       int nQueries =10;
       double qualityRetrivalOverall =0;
       int allowNetworksize = TestGraph.graph.size();
    	for(int i=0;i<nQueries;i++){
		// test query input parameters 
		List<Integer> queryCrfNodes  = new ArrayList<Integer>();
		queryCrfNodes.add(1);
		queryCrfNodes.add(2);
		//queryCrfNodes.add(3);
		
		Random rand = new Random();
		 int num =rand.nextInt(10);
		int n = 1700;
		double queryTestMean = Math.exp((double)-n/300) ;;
		double queryTestDevation =  Math.sqrt((1-queryTestMean )*(queryTestMean )/n);
		Query queryTable =TestGraph.graph.get(num).GenerateQuery(queryTestMean,queryTestDevation,queryCrfNodes);
		AllowNode allNode = TestGraph.graph.get(num);
		NodeIdConfidence conf =TestGraph.graph.get(num).RandomWalkTestQuery(queryTable, allNode.getAllowNodeId());
		
		double globalConfi = getGlobalBestConfidence(TestGraph,queryCrfNodes);
		if(conf != null){
		System.out.println(" Query answered @ :"+conf.getallowNodeId()+" Confidence Got "+conf.getConfidenceValue());	
		qualityRetrivalOverall =  qualityRetrivalOverall + conf.getConfidenceValue()/globalConfi;		
		  }
    	}
    	
		System.out.println(" Retreival Quality Table : " +   qualityRetrivalOverall/nQueries + " Selectivity  "+sel +"\n"); 
		output.printf("%d,%d,%d,%f\n",allowNetworksize,numberExploreQueries,sel,(qualityRetrivalOverall/nQueries));
		
    }
    
    
	public static void main(String[] args) throws IOException {
		
		queryLearning();
		/*int count_max = 500, increment =1100, initial = 500;
		// test query input parameters 
		List<Integer> queryCrfNodes  = new ArrayList<Integer>();
		queryCrfNodes.add(1);
		queryCrfNodes.add(2);
		queryCrfNodes.add(3);
		
		int n = 1700;
		double queryTestMean = Math.exp((double)-n/300) ;;
		double queryTestDevation =  Math.sqrt((1-queryTestMean )*(queryTestMean )/n);
		
		Query queryTable =TestGraph.graph.get(start).GenerateQuery(queryTestMean,queryTestDevation,queryCrfNodes);
		 	
		FileWriter filewrtier = new FileWriter("D:\\Allowlog.txt");
		PrintWriter output = new PrintWriter(filewrtier);
		
	  int numberofNeighbors = 21 ;
	  for(int netsCount=1; netsCount<=numberofNeighbors ;netsCount++){
		   
		for(int c=initial; c <= count_max ; c=c+increment )
		{	
			 int testRandomWalk =0;
			 int testNeighborTable =0 ;
			 int testcount =1;
			//make new graph
			GenerateGraph TestGraph = new GenerateGraph(c);
			AllowNode node = TestGraph.graph.get(1);
			
			// update nodes 
			 for(int j=0;j< TestGraph.graph.size();j++)
			 { 
			    node = TestGraph.graph.get(j);
			    if(node != null){
			    node.updateHopNetCount(netsCount);
			    }
			 }
			 
		  int updatesHopCount = 2;
		  node = TestGraph.graph.get(1);
		  node.triggerBuildingNetwork(updatesHopCount);
			
			for(int j=0;j< TestGraph.graph.size();j++)
			 { 
			    node = TestGraph.graph.get(j);
			    if(node != null){
			 //   node.dumpKnowledgeModel();
			    node.dumpTable();
			    }
			 }
			
		int nNodestoCheck = c/2 ;
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
	     
		int count =0;
		for(int i=1 ; i<= nNodestoCheck; i++){
		//method =3; 
		//int start =  rand.nextInt(TestGraph.n);
			
		int start =  rand.nextInt(c);
		Query queryRandom =TestGraph.graph.get(start).GenerateQuery(queryTestMean,queryTestDevation,queryCrfNodes);
		Query queryTable =TestGraph.graph.get(start).GenerateQuery(queryTestMean,queryTestDevation,queryCrfNodes);
	
		NodeIdConfidence nodeConfTable = TestGraph.graph.get(start).RandomWalkWithNeighborTableQRetreival(queryTable);
		NodeIdConfidence nodeConfRandom = TestGraph.graph.get(start).RandomWalk(queryRandom);
		
		/////////////////////////////////////////////////////////////
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
			qualityRetrivalRandom =  qualityRetrivalRandom +nodeConfRandom.getConfidenceValue()/0.9999;}
		
		double bestQuality =  0.999;
		if(nodeConfTable != null) {
			if(nodeConfTable.getallowNodeId().equalsIgnoreCase("12"))
			{
			 testNeighborTable++; 
			// qualityBest =   nodeConfTable.getConfidenceValue();
			 bestQuality = nodeConfTable.getConfidenceValue();
			 //qualityRetrival1 =  qualityRetrival1 + 1 ;
			} 
		 else
		  {
			// qualityRetrival2 =   qualityRetrival2  + nodeConfTable.getConfidenceValue()/qualityBest ;
			System.out.println("Missed Node: "+nodeConfTable.getallowNodeId());
		  }
			double confi = nodeConfTable.getConfidenceValue();
			double globalConfi =	getGlobalBestConfidence(TestGraph,queryCrfNodes);
		//	qualityRetrivalOverall = qualityRetrivalOverall + confi/globalConfi;
			qualityRetrivalOverall =  qualityRetrivalOverall + confi/globalConfi;
		//qualityRetrivalOverall = (qualityRetrival1 + qualityRetrival2)/ nNodestoCheck;
		
		}
		
		System.out.println(" Efficiency Random Walk : "+ testRandomWalk +"\n");
		System.out.println(" Efficiency Neighbor Tables : "+ testNeighborTable +"\n");
		
		
		//System.out.println(" Query answered @ : "+nodeConfRandom.getallowNodeId()+"\n");
		//System.out.println(" Query HopeCount : "+ (query.hopCount -1)+"\n");
		System.out.println(" Query nodes visited (Random) @ : "+queryRandom.allowNodeIdsVisited.toString()+"\n");
		System.out.println(" Query nodes visited (Table) @ : "+queryTable.allowNodeIdsVisited.toString()+"\n");
		
		}
	
		testcount--;
		}


		System.out.println(" Neighbors Updates : "+ netsCount +"\n");
		System.out.println(" Retreival Qaulity Table : "+  qualityRetrivalOverall/nNodestoCheck +"\n");
		System.out.println(" Retreival Qaulity Random : "+ qualityRetrivalRandom/nNodestoCheck+"\n");
		
		
		output.printf("%d,%d,%f,%f\n",nNodestoCheck,netsCount,(qualityRetrivalOverall/nNodestoCheck),
		    		(qualityRetrivalRandom/nNodestoCheck));
		
		// write  file to visualize
		
		  //  output.printf("%s\r\n", "Random"+","+nNodestoCheck+","+(qualityRetrivalRandom/nNodestoCheck));
		    //output.printf("%s\r\n", "Allow"+","+nNodestoCheck+","+(qualityRetrivalOverall/nNodestoCheck));
	
	    }
	  }
		output.close();*/
		
	
	}
 
	/*
    public static void main(String[] args) throws IOException {
		
		
		FileWriter filewrtier = new FileWriter("D:\\Allowlog.txt");
		PrintWriter output = new PrintWriter(filewrtier);
		
		
		// test on 100 nodes 
		
		GenerateGraph TestGraph = new GenerateGraph(100);
		AllowNode node = TestGraph.graph.get(1);
		node.triggerBuildingNetwork();
		
		
		for(int numInstances=11;numInstances< 4000;numInstances= numInstances+10 ){
			int testRandomWalk =0;
			 int testNeighborTable =0 ;
			 int testcount =1;
			
		for(int j=0;j< TestGraph.graph.size();j++)
		{	
			 
			 node = TestGraph.graph.get(j);
			 
			 // good learning for crf node =7 
			 if(node.getAllowNodeId().equals("872") || node.getAllowNodeId().equals("94")||
					 node.getAllowNodeId().equals("12") || node.getAllowNodeId().equals("6") ||
					 node.getAllowNodeId().equals("7")){
				 
				 node.updateLocalKnowlegdeModel(numInstances); 
			 }
	    }
			/*for(int j=0;j< TestGraph.graph.size();j++)
			 { 
			    node = TestGraph.graph.get(j);
			    //node.buildLocalKnowlegdeModel();
			    node.dumpKnowledgeModel();
			    node.dumpTable();
			 }
			
		int nNodestoCheck = 98 ;
		double qualityRetrival1 =0;
		double qualityRetrival2 =0;
		double qualityRetrivalOverall =0;
		double qualityBest =0;
		double qualityRetrivalRandom =0;

		
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
	//	Query queryRandom =TestGraph.graph.get(start).GenerateQuery(0.79,0.04);
		Query queryTable =TestGraph.graph.get(start).GenerateQuery(0.79,0.04);
		
	
	//	System.out.println(" Query started Allow NodeId  : "+(start));
		
		//String nodeConfTable = TestGraph.graph.get(start).RandomWalkWithNeighborTable(queryTable);
		NodeIdConfidence nodeConfTable = TestGraph.graph.get(start).RandomWalkWithNeighborTableQRetreival(queryTable);
	

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
		//System.out.println(" Query nodes visited (Random) @ : "+queryRandom.allowNodeIdsVisited.toString()+"\n");
		System.out.println(" Query nodes visited (Table) @ : "+queryTable.allowNodeIdsVisited.toString()+"\n");
		
		  }
	
		
	//	System.out.println(" Efficiency Random Walk : "+ testRandomWalk +"\n");
		//System.out.println(" Efficiency Neighbor Tables : "+ testNeighborTable +"\n");
		
		System.out.println(" Retreival Qaulity Table : "+  qualityRetrivalOverall/nNodestoCheck +"\n");
		//System.out.println(" Retreival Qaulity Random : "+ qualityRetrivalRandom/nNodestoCheck +"\n");
		
		
		// write  file to visualize
		
		   // output.printf("%s\r\n", "Random"+","+nNodestoCheck+","+(qualityRetrivalRandom/nNodestoCheck));
		    output.printf("%d,%f\n",numInstances,(qualityRetrivalOverall/nNodestoCheck));
		
		}
	
		output.close(); 
	  }
	}*/

    
}