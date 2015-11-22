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
	
	// configuartion parameters as data members 
	static int selectivity = 20;
	static int progapationHopCount = 12;
	static int numberExploreQueries = 100;
	static int maximumLimitExploreQueries = 200;
	static int allowNodesNumber = 200;
	static int queryConfiguartion = 6;
  	static double qualityRetrivalOverall =0;
	static double qualityBest =0;
    
	
	// query learning apporoach
    @SuppressWarnings("unused")
	public static void queryLearning(String filename) throws IOException
    {
    	
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
		 // add path of output file
		FileWriter filewrtier = new FileWriter(filename);
		PrintWriter output = new PrintWriter(filewrtier);
		for(int sel =1 ;sel <= selectivity ;sel++) {
		for( ;numberExploreQueries <= maximumLimitExploreQueries; 
			  numberExploreQueries = numberExploreQueries + 100){
			 
    	for(int c=1; c <= numberExploreQueries ; c++)
		{	
    		// for generating different types of queries
    	List<Integer> queryScopeCRFNodes =  new ArrayList<Integer>();
    	generateRandomQueryScopes( queryScopeCRFNodes,  rand.nextInt(queryConfiguartion));
    	
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
    	
     }	
		testQuery(TestGraph,output,numberExploreQueries,sel);
		}
		output.close();
   }
    
    // generate diffrent random queries used in query learning approach
    // and query scopes ( crf nodes to be queried ) are added 
    public static void generateRandomQueryScopes(List<Integer> queryScopeCRFNodes, int configurationParam){
    	
    	if(configurationParam ==1) {
    	queryScopeCRFNodes.add(1);
    	queryScopeCRFNodes.add(2);
    	}
    	else if(configurationParam == 2){
    		queryScopeCRFNodes.add(1);
        	queryScopeCRFNodes.add(2);
        	queryScopeCRFNodes.add(3);
        	
    	}
        else if(configurationParam == 3){
        	queryScopeCRFNodes.add(4);
        	queryScopeCRFNodes.add(5);
        	queryScopeCRFNodes.add(6);
        	queryScopeCRFNodes.add(7);
  
    	}
        else if(configurationParam == 4){
	
        	queryScopeCRFNodes.add(2);
        	queryScopeCRFNodes.add(3);
       	// queryScopeCRFNodes.add(11);
        	//queryScopeCRFNodes.add(12);
         //queryScopeCRFNodes.add(13);
       	//queryScopeCRFNodes.add(14);
        
         }
         else if(configurationParam == 5){
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
         else if(configurationParam == 6){
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
		// lest generate the query on some random Allow node in a graph
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
    
    public static void knowledgeAggregation(String filename) throws IOException
    {
    	
      int count_max = 500, increment =1100, initial = 500;
		
		//////////////// test query input parameters ///////////////////////////////
		List<Integer> queryCrfNodes  = new ArrayList<Integer>();
		queryCrfNodes.add(1);
		queryCrfNodes.add(2);
		queryCrfNodes.add(3);
		
		int n = 1700;
		double queryTestMean = Math.exp((double)-n/300) ;;
		double queryTestDevation =  Math.sqrt((1-queryTestMean )*(queryTestMean )/n);
		
		///////////////////////////////////////////////////////////////////////
		
		
		FileWriter filewrtier = new FileWriter(filename);
		PrintWriter output = new PrintWriter(filewrtier);
		
	  int numberofNeighbors = 21 ;
	  for(int netsCount=1; netsCount<=numberofNeighbors ;netsCount++){
		   
		  //
		for(int c=initial; c <= count_max ; c=c+increment )
		{	
			 int testRandomWalk =0;
			 // way to propgate building neibhors table on basis of Knowledge Aggregation
			 int testNeighborTable =0 ; 
			 int testcount =1;
			//make new graph
			 //creating graph with c nodes . 
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
			
	/////////// check half of the nodes after building the knowledge network /////////////
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
		System.out.println(" Efficiency Knowledge Aggregation : "+ testNeighborTable +"\n");
		
		
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
		
	
	    }
	  }
		output.close();
		
    	
    }
	public static void main(String[] args) throws IOException {
		
		// please create a file first if not exist

		if(args.toString().equalsIgnoreCase("KA"))
			knowledgeAggregation("C:\\KnowledgeAggregation.txt");
		else
	  	queryLearning("C:\\QueryLearning.txt");
	 
	}
 
	
}