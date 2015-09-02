package AllowModel.protocol;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import net.sf.javaml.clustering.mcl.MCL;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.NodeInstance;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import AllowModel.clustering.KMedoids;
import AllowModel.clustering.prim;
import AllowModel.crf.CrfMap;
import AllowModel.metrics.AllowDistance;
import AllowModel.metrics.ConfidenceList;
import AllowModel.metrics.Distance;
import AllowModel.metrics.MergeDistribution;
import AllowModel.metrics.NodeIdConfidence;
import AllowModel.metrics.ScopeInformation;
import AllowModel.model.LocalKnowledgeModel;
import AllowModel.model.RoutingKnowledgeModel;
import AllowModel.model.RoutingTable;
import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.*;
import peersim.vector.SingleValueHolder;


public class AllowNode //extends GeneralNode
{
	boolean knowledgeUpdate ; // flag to check if there is any change/build in local knowldege model.
	int sizeCrfGraph = 8;
	String nodeId;
	int[][] adjacencyMatrix  ; //
	CrfMap CRFmap;
	List<AllowNode> neighbors;
	RoutingTable routingTable  ;
	LocalKnowledgeModel model;
	Query currentQuery;
	RoutingKnowledgeModel routingModel ;/// model formed empty
	 public static final int INFINITE = 999;
	public AllowNode(String idNum,File inputFile) throws IOException
	{
		sizeCrfGraph = 8;
		knowledgeUpdate = true;
		//super(idNum);
		nodeId = idNum;
		neighbors = new ArrayList<AllowNode>();
		CRFmap = new CrfMap(inputFile,nodeId);
		routingTable = new RoutingTable(); 
		adjacencyMatrix  	= new int[sizeCrfGraph][sizeCrfGraph];
		buildAjdMatrix(adjacencyMatrix);
	}
	
	private void buildAjdMatrix(int [][]mat)
	{
		mat[0][1] = 1; mat[1][0] =1;
		mat[1][2] = 1; mat[2][1] =1;
		mat[2][3] = 1; mat[3][2] =1;
		mat[2][4] = 1; mat[4][2] =1;
		mat[4][5] = 1; mat[5][4] =1;
		mat[6][4] = 1; mat[4][6] =1;
		mat[4][7] = 1; mat[7][4] =1;
		mat[7][1] = 1; mat[1][7] =1;
		  		   
	}
	
	public void addNeighbor(AllowNode node)
	{  
		neighbors.add(node);
	}
	
	//constructor
	public AllowNode(String idNum, List<AllowNode> nodeList,File inputFile) throws IOException{
		//super(idNum);
		sizeCrfGraph = 8;
		knowledgeUpdate = true;
		nodeId= idNum;
		neighbors = nodeList;
		CRFmap = new CrfMap(inputFile,nodeId);
		routingTable = new RoutingTable(); 	
		 adjacencyMatrix  	= new int[sizeCrfGraph][sizeCrfGraph];
		 buildAjdMatrix(adjacencyMatrix);
	}

	
	public List<AllowNode> getNeighbors(){
		
		return neighbors;
		
	}
	public Query GenerateQuery(double queryMean,double margin){
		
		List<Integer> queryScopesCRFNodes = new  ArrayList<Integer>();
		queryScopesCRFNodes.add(7);
		Query query = new Query(nodeId, queryMean,margin,queryScopesCRFNodes);
		currentQuery = query;
		
		return query;
	} // end method GenerateQuery

	
	public int[][] clusterSpanningTree(CrfMap map)
	{
		double adjacency_matrix[][];
        int number_of_vertices;
     //   Scanner scan = new Scanner(System.in);
       // CrfMap mapC =  new CrfMap();
       // map.populatingConstantMap(Integer.toString(3));
        number_of_vertices =  map.getNumberofCRFNodes();
        Map<Integer, ConfidenceList> mapFinal =     map.getConstantConfidenceMap();
        adjacency_matrix = new double[number_of_vertices + 1][number_of_vertices + 1];
          
      AllowDistance allowDistance = new AllowDistance(adjacencyMatrix);
           
      for (int i = 1; i <= number_of_vertices; i++)
            {  
    	     
    	      ConfidenceList conI = mapFinal.get(i);
                for (int j = 1; j <= number_of_vertices; j++)
                { 
                	 ConfidenceList conJ = mapFinal.get(j);
                     adjacency_matrix[i][j] = allowDistance.measure(i, j, conI.getMean(), conJ.getMean(), conI.getMargin(), conJ.getMargin());
                	 // adjacency_matrix[i][j] =   allowDistance.measure(x, y) ;
                    if (i == j)
                    {
                        adjacency_matrix[i][j] = 0;
                        continue;
                    }
                    
                    if (adjacency_matrix[i][j] == 9999)
                    {
                        adjacency_matrix[i][j] = INFINITE;
                    }
                    
                }
            }
 
            prim prims = new prim(number_of_vertices);
            prims.primsAlgorithm(adjacency_matrix,map);
          //  prims.printMST();
          
		return prims.clusterMST(6);
	}
	
	
	public int[][] clusteringCrfNodes( CrfMap map)
	{
		 /*
		int numberOfClusters = 5 ;
		int maxIterations =100 ; 
		Distance dm = new Distance() ;
		Data
	    KMedoids clustering = new KMedoids(numberOfClusters, maxIterations,dm,map );*/
	   
		Dataset dataset = new DefaultDataset();
		Map<Integer, ConfidenceList> mapC = map.getConstantConfidenceMap();
		Set<Entry<Integer,ConfidenceList>> set = mapC.entrySet();
		for(Entry<Integer, ConfidenceList> i:set){
	
			ConfidenceList clist = i.getValue();
			double[] values = new double[] { clist.getMean(), clist.getMargin()};
			Instance instance = new NodeInstance(values,i.getKey() );
			dataset.add(instance);
		}
		
		
		
	    int noofCRFNodes = map.getNumberofCRFNodes();
	    AllowDistance allowDistance = new AllowDistance(adjacencyMatrix);
		 //  JaccardIndexSimilarity distance = new JaccardIndexSimilarity();
	   // this.dumpCrfMap();
	    MCL mcl = new MCL(allowDistance);
	    Dataset[] dataarray = mcl.cluster(dataset);
	    int rows = dataarray.length;
	    String testNdode = nodeId;
	//    int[][] output  =  clustering.clusterCoWeb(toInt(CRFNodesIds));
	    int[][] output = new int[rows][];
	    ///
	    int k=0;
	    for(Dataset data:dataarray)
	    {    
	    	
	    	 int length  = data.size();
	    	 output[k] =  new int[length];
	    	 int i=0;
	    	 while( i < length)
	    	   { 
	    		  Instance ins = data.get(i);
	    		  int id = ins.getID();
	    		  output[k][i] = id;
	    		  i++;
	    	   }
	    	 k++;
	    }
		return output;
		
	}
	
	public void buildLocalKnowlegdeModel()
	{
	    model = new LocalKnowledgeModel(nodeId);
		int[][] clusters = clusterSpanningTree(CRFmap) ;//clusteringCrfNodes(CRFmap);
		int noOfClusters = clusters.length;
	
		for(int i=0;i<noOfClusters; i++)
		{
		  List<Integer> nodeIdsList  = new ArrayList<Integer>();
		  for(int j=0;j<clusters[i].length; j++)
		  {
			  nodeIdsList.add(clusters[i][j]);
		  }
		  
		   MergeDistribution avg = new MergeDistribution( CRFmap, clusters[i]) ; // get the avg mean and margin of cluster 
		 
		   ScopeInformation info = new ScopeInformation( avg.getMean(), avg.getMargin(), nodeIdsList, 0,avg.getNoofInstances()); 
		   model.addScopeInformation(info);  // add cluster information in the model
		   model.getScopesInformation();  // add cluster information in the model
		  
		}
			
	}
	
	public void populateRoutingModel(CrfMap map,String neighborNodetoSend)
	{ 
		nodeId.getClass();
		// clustering of crf nodes are done
		int[][] clusters = clusterSpanningTree(map);
		int noOfClusters = clusters.length;
		//routingModel = new RoutingKnowledgeModel(nodeId);
		for(int i=0;i<noOfClusters; i++)
		{
		  List<Integer> nodeIdsList  = new ArrayList<Integer>();
		  for(int j=0;j<clusters[i].length; j++)
		  {
			  nodeIdsList.add(clusters[i][j]);
		  }
		  
		   MergeDistribution avg = new MergeDistribution( map, clusters[i]) ; // get the avg mean and margin of cluster 
		 
		   ScopeInformation info = new ScopeInformation( avg.getMean(), avg.getMargin(), nodeIdsList, 0,avg.getNoofInstances()); 
		   routingModel.addScopeInformation(info);  // add cluster information in the model
		  
		}
		routingModel.getScopesInformation(); // test 
		
	}
	
	
	public void triggerBuildingNetwork()
	{
		updateNetwork(	null);
			
	}
	
	public String getAllowNodeId()
	{
		return nodeId;
	}
	
	/*public void updateNetwork()
	{
		buildRoutingModel(); /// new routing model for the node is formed.
		routingModel.getScopesInformation();
		sentRoutingModeltoNeighbors();
	}*/
	
	public void updateNetwork(String recieveUpdateNodeId)
	{
		// if knowlegde model is already build then dont build it again and sent new formed routing model to 
		// neighboring nodes except the one from which it is coming mentioned by recieveUpdateNodeId
		if(knowledgeUpdate == false)
		{
			//build routing model for everynode except from the one which information is coming
			//and sent to respective neighbor..
			for(AllowNode neighborNode:this.getNeighbors()) 
			{	
				if(neighborNode.getAllowNodeId().equals(recieveUpdateNodeId) )
					continue;
				updateRoutingModel(neighborNode.getAllowNodeId());
			    //sentRoutingModeltoNeighbors();
			}

		}
		else{
			
			buildLocalKnowlegdeModel();
			knowledgeUpdate = false;  // set local knowledge building off once it is build for one  time.
			
			//build routing model for everynode and sent to respective neighbor..
			for(AllowNode neighborNode:this.getNeighbors()) 
			{	updateRoutingModel(neighborNode.getAllowNodeId());
			    //sentRoutingModeltoNeighbors();
			}
			 
		} 
	}
	
	
	public void updateRoutingModel(String neighborNodetoSend)
	{
		routingModel = new RoutingKnowledgeModel(nodeId);
	 	// build routing model of a ALLOW node initially
		//get local model first //
		List<ScopeInformation> localModelScopes = model.getScopesInformation();
	
		int  noofCRFNodes = CRFmap.getNumberofCRFNodes();
		Set<Integer> crfNodesIdsList = CRFmap.getCRFNodesIds();
	   
		// temp map to store output
		CrfMap tempCRFMap = new CrfMap();
	 // to get total scopes = scopes(crfnode)*neighbors(crfnode)
		for(Integer crfnodeId:crfNodesIdsList){
			
		     double maxConfidence =0;
		     double bestMean =0;
		     double bestMargin =0;
		     int bestInstances = 0;
		     	
			  Iterator<AllowNode> iterator = neighbors.iterator();
			  
			  while (iterator.hasNext()) {
				  if(routingTable.empty())
						break;
				  // build routing model except one node each time.
				  String allowId = iterator.next().getAllowNodeId();
				 if( allowId.equals(neighborNodetoSend) )
					 continue;
				
				Map<String, List<ScopeInformation>> neighborScopes =
						routingTable.getNeighborScopes();
				List<ScopeInformation> neighborScopesList = neighborScopes.get( allowId);
				// merge distribution of local model and model of neighbor node
				// check if the list of the neighboring allow node is empty or not , if empty leave that node.
				if(neighborScopesList != null){
				for(ScopeInformation scopes:neighborScopesList){
					
					  List<Integer>  nodesPresentInScope = scopes.getNodeIdswithScope();
					 if( nodesPresentInScope.contains(crfnodeId) == true)
					  {//
						 double margin  =scopes.getscopeMargin();
						 double mean =  scopes.getscopeMean();
						 double instances = scopes.getscopeInstances();
						 double confidence = confidence(mean,margin);
						 
						 if( maxConfidence < confidence)
						 {
							 bestMean = mean;
							 bestMargin = margin;
							 bestInstances = (int) instances;
							 maxConfidence = confidence;
						 }
						
					  }
					 
				   }
				 
			     } 
			   }	
			// add with the local knowlegde part for each CRF node
		
		for(ScopeInformation scopes:localModelScopes){
			
			  List<Integer>  nodesPresentInScope = scopes.getNodeIdswithScope();
			 if( nodesPresentInScope.contains(crfnodeId) == true)
			  {
				 double margin  =scopes.getscopeMargin();
				 double mean =  scopes.getscopeMean();
				 double instances = scopes.getscopeInstances();
				 double confidence = confidence(mean,margin);
				 if( maxConfidence < confidence)
				 {
					 bestMean = mean;
					 bestMargin = margin;
					 bestInstances = (int) instances;
					 maxConfidence =  confidence;
				 }
			
			  }
			 
		   }
		 
			String testnode =	nodeId;
			int crfN = crfnodeId;
		 ConfidenceList list = new  ConfidenceList(bestInstances,bestMean,bestMargin);
		 tempCRFMap.getConstantConfidenceMap().put(crfnodeId, list);
		 
		}
		 
	  // clustering & populating in routing node	
		populateRoutingModel(tempCRFMap,neighborNodetoSend);	
		sentRoutingModeltoNeighbor(neighborNodetoSend);
	  
	}
	
	
	public void buildRoutingModel()
	{
		routingModel = new RoutingKnowledgeModel(nodeId);
	 	// build routing model of a ALLOW node initially
		//get local model first //
		List<ScopeInformation> localModelScopes = model.getScopesInformation();
	
		int  noofCRFNodes = CRFmap.getNumberofCRFNodes();
		Set<Integer> crfNodesIdsList = CRFmap.getCRFNodesIds();
		
		// temp map to store output
		CrfMap tempCRFMap = new CrfMap();
	 // to get total scopes = scopes(crfnode)*neighbors(crfnode)
		for(Integer crfnodeId:crfNodesIdsList){
			int totalScopeScanned = 0;
			double sum_mean =0; 
			int sum_instances =0;
			double sum_margin =0;
		     double maxConfidence =0;
		     double bestMean =0;
		     double bestMargin =0;
		     int bestInstances = 0;
		     
			if(!neighbors.isEmpty())
			{		
		  Iterator<AllowNode> iterator = neighbors.iterator();
		  
		  while (iterator.hasNext()) {
			if(routingTable.empty())
				break;
			Map<String, List<ScopeInformation>> neighborScopes =
					routingTable.getNeighborScopes();
			List<ScopeInformation> neighborScopesList = neighborScopes.get( iterator.next().getAllowNodeId());
			// merge distribution of local model and model of neighbor node
			// check if the list of the neighboring allow node is empty or not , if empty leave that node.
			if(neighborScopesList != null){
			for(ScopeInformation scopes:neighborScopesList){
				
				  List<Integer>  nodesPresentInScope = scopes.getNodeIdswithScope();
				 if( nodesPresentInScope.contains(crfnodeId) == true)
				  {//
					 double margin  =scopes.getscopeMargin();
					 double mean =  scopes.getscopeMean();
					 double instances = scopes.getscopeInstances();
					 double confidence = confidence(mean,margin);
					 
					 if( maxConfidence < confidence)
					 {
						 bestMean = mean;
						 bestMargin = margin;
						 bestInstances = (int) instances;
						 maxConfidence = confidence;
					 }
					 sum_mean = sum_mean + mean*instances;
					 sum_instances = (int) (sum_instances + instances);
					 sum_margin = ( Math.pow(margin, 2) + Math.pow(mean, 2))*instances  + sum_margin;
					 totalScopeScanned++;
				  }
				 
			   }
			 
		     } 
		   }	
		// add with the local knowlegde part for each CRF node
	 }
		for(ScopeInformation scopes:localModelScopes){
			
			  List<Integer>  nodesPresentInScope = scopes.getNodeIdswithScope();
			 if( nodesPresentInScope.contains(crfnodeId) == true)
			  {
				 double margin  =scopes.getscopeMargin();
				 double mean =  scopes.getscopeMean();
				 double instances = scopes.getscopeInstances();
				 double confidence = confidence(mean,margin);
				 if( maxConfidence < confidence)
				 {
					 bestMean = mean;
					 bestMargin = margin;
					 bestInstances = (int) instances;
					 maxConfidence =  confidence;
				 }
				 
				 sum_mean =sum_mean + mean*instances;
				 sum_instances = (int) (sum_instances + instances);
				 sum_margin = ( Math.pow(margin, 2) + Math.pow(mean, 2))*instances  + sum_margin;
				 totalScopeScanned++;
			  }
			 
		   }
		   /// final mean and margin of each CRF node  . let this be populated 
		 double finalMeanCRFNode = sum_mean/sum_instances;
		 double finalMarginCRFNode =0;
		 double marginSqure = sum_margin/sum_instances - Math.pow(finalMeanCRFNode,2) ;
			finalMarginCRFNode = Math.sqrt(marginSqure);
		
			int effSumInstance = 0;
			//if(totalScopeScanned != 0) 
				//// change 
			// effSumInstance = sum_instances/totalScopeScanned;
				effSumInstance = sum_instances ;
		 // populate this information in routing table for each CRF node
		 // tempopary store new infomration of CRF nodes .
				//testing
			String testnode =	nodeId;
			int crfN = crfnodeId;
		 ConfidenceList list = new  ConfidenceList(bestInstances,bestMean,bestMargin);
		 tempCRFMap.getConstantConfidenceMap().put(crfnodeId, list);
		 
		}
		 
		// finally clustering with the new means and margins on the CRF nodes to get the final routing model 
	// and populate in routing model.
		
	  // clustering & populating in routing node	
		populateRoutingModel(tempCRFMap,null);				
	}
	
	
	//Flood asks every neighbor if they have the file
  public String Flood(Query in){
			boolean queryAnswered = false;
			in.allowNodeIdsVisited.add(nodeId);
			in.hopCount++;
           
			
					if( false   )   // critiria for query answer with current ALLOW node
					queryAnswered=true;
				
			
			
			if(queryAnswered){
				return nodeId;
			}
			else if(in.hopCount >= 7){
				return "NA";
			}
			else{
				if(neighbors != null){
					for(int i= 0; i< neighbors.size(); i++){
						
						//zero node is special node and does not exist
						if( ! neighbors.get(i).nodeId.equals("0")){
							return neighbors.get(i).Flood(in);
						}
					}
				}
			}
	
			return "NN";  // query cant be answered
		}
	
  
//random walk uses a random walker who chooses a random node to walk to
	public NodeIdConfidence RandomWalk(Query in){
		
		//pick random neighbor to ask
		
		//Random rand = new Random();
		String neighborToForward = null;
		boolean neighborHasFile=false;
		String neighborWithBestModel = null;
		
		//System.out.println("\n Allow Node Id :"+nodeId);
		//this.dumpCrfMap();
	    //this.dumpKnowledgeModel();
		//this.dumpTable(); 
		
	
		Random rand = new Random();
		String neighborToAsk = "0";
		int randomIndex=-1;
		
		in.allowNodeIdsVisited.add(nodeId);
		in.hopCount++;
		
		boolean queryAnswered=false;
		double scopeConfidence =0;
		// check if query can be answered by the current ALLOW node
				List<Integer> queryCrfNodes=	in.queryScopeCRFNodes;
				List<ScopeInformation> scopesLocalModel = model.getScopesInformation();
				// test for I node basic testing
				for(ScopeInformation sc:scopesLocalModel)
				{
					List<Integer> crfNodeIds = sc.getNodeIdswithScope();
					if(crfNodeIds.contains(queryCrfNodes.get(0)))
					{  // check for mean just basic testing
						 scopeConfidence = confidence(sc.getscopeMean(),sc.getscopeMargin());
						double queryConfidence = confidence(in.queryMean,in.queryMargin);
						if(scopeConfidence >= queryConfidence)
							{ 
							  queryAnswered = true;
							  break;
							 }
					}	
					
				}
				
				
	 NodeIdConfidence nodeIdConfidence	 = new NodeIdConfidence(nodeId,scopeConfidence,queryCrfNodes.get(0));
		if(queryAnswered == true){
			return nodeIdConfidence;
		}
		else if(in.hopCount >= 100){
			return null;
		}
		else if ( (neighbors.size() == 1)  && 
				  ( this.getAllowNodeId().equals(in.getQuerySourceAllowId())== false  ) )  // leaf node
		{
			return nodeIdConfidence;
		}
		else{
			 
				randomIndex = rand.nextInt(neighbors.size());
				neighborToAsk =  neighbors.get(rand.nextInt(neighbors.size())).getAllowNodeID();
			
				return neighbors.get(randomIndex).RandomWalk(in);	
		}
		
		//return "NN";
		
		
	}//end method Random Walk
	
	
	/*Method RandomWalk with neighbor tables look ahead: This method checks to see if its neighbors lists of files have
	 * the desired query within them. If so that neighbor is walked to if not it randomly choosed*/
	public String RandomWalkWithNeighborTable(Query in){
		//pick random neighbor to ask
		Random rand = new Random();
		String neighborToForward = null;
		boolean neighborHasFile=false;
		String neighborWithBestModel = null;
		
		in.allowNodeIdsVisited.add(nodeId);
		in.hopCount++;
		//if(nodeId.equals("0") || nodeId.equals("1") ){
		//System.out.println("\n Allow Node Id :"+nodeId);
		 //this.dumpCrfMap();
	     //this.dumpKnowledgeModel();
		 //this.dumpTable(); 
		//}
		boolean queryAnswered = false;
		
		// check if query can be answered by the current ALLOW node
		List<Integer> queryCrfNodes=	in.queryScopeCRFNodes;
		List<ScopeInformation> scopesLocalModel = model.getScopesInformation();
		// test for I node basic testing
		for(ScopeInformation sc:scopesLocalModel)
		{
			List<Integer> crfNodeIds = sc.getNodeIdswithScope();
			if(crfNodeIds.contains(queryCrfNodes.get(0)))
			{  // check for mean just basic testing
				double scopeConfidence = confidence(sc.getscopeMean(),sc.getscopeMargin());
				double queryConfidence = confidence(in.queryMean,in.queryMargin);
				if(scopeConfidence >= queryConfidence)
					{ 
					  queryAnswered = true;
					  break;
					 }
			}	
			
		}
		
  // critiria for query answer with current ALLOW node
		
		//Begin Lookup of find the neigbor which has best routing model wrt query
		
		neighborToForward  = "";
		
		//double minMean =1;
		double maxConfidence = 0;
		
		Map<String,List<ScopeInformation>> map = routingTable.getNeighborScopes();
	    Set<String> keys =map.keySet();
		for(String neighbors:keys)
		{
			List<ScopeInformation> scopes = map.get(neighbors);
			if(scopes !=null){
			for(ScopeInformation sc:scopes)
			{
				List<Integer> crfNodeIds = sc.getNodeIdswithScope();
				if(crfNodeIds.contains(queryCrfNodes.get(0)))
				{  // check for mean just basic testing
					
					double scopeConfidence = confidence(sc.getscopeMean(),sc.getscopeMargin());
					double queryConfidence = confidence(in.queryMean,in.queryMargin);
					if(  (maxConfidence < scopeConfidence)  )
					{ 
					   neighborWithBestModel = neighbors;
					   maxConfidence = scopeConfidence;
					}
					
					/*if((sc.getscopeMean() < in.queryMean) && (minMean > sc.getscopeMean())  )
						{ 
						   neighborWithBestModel = neighbors;
						   minMean = sc.getscopeMean();
						}
					*/
				}	
				
			 } 
			}
		}
		
		AllowNode bestNeighborNode = null;
		for(int i=0;i<neighbors.size();i++ ){
		  AllowNode node = neighbors.get(i);
		  if(node.getAllowNodeId().equals(neighborWithBestModel))
			  bestNeighborNode = node;
		}
		  
		 
		
		if(neighbors == null || (neighbors.size() <= 0)){
			return "NA";
		}
		
		if(queryAnswered){
			return nodeId;
		}
		else if(in.hopCount >= 100){
			return "NA";
		}
		else{
			if(neighbors != null && bestNeighborNode != null){
				return bestNeighborNode.RandomWalkWithNeighborTable(in);
			}
		}
		
		return "NN";
		
		
	}//end method Random Walk with neighbor table
	
	
	public NodeIdConfidence RandomWalkWithNeighborTableQRetreival(Query in){
		//pick random neighbor to ask
		Random rand = new Random();
		String neighborToForward = null;
		boolean neighborHasFile=false;
		String neighborWithBestModel = null;
		
		in.allowNodeIdsVisited.add(nodeId);
		in.hopCount++;
		
	//	System.out.println("\n Allow Node Id :"+nodeId);
		//this.dumpCrfMap();
	    //this.dumpKnowledgeModel();
		//this.dumpTable(); 
		    
		boolean queryAnswered = false;
		double scopeConfidence =0;
		// check if query can be answered by the current ALLOW node
		List<Integer> queryCrfNodes=	in.queryScopeCRFNodes;
		List<ScopeInformation> scopesLocalModel = model.getScopesInformation();
		// test for I node basic testing
		for(ScopeInformation sc:scopesLocalModel)
		{
			List<Integer> crfNodeIds = sc.getNodeIdswithScope();
			if(crfNodeIds.contains(queryCrfNodes.get(0)))
			{  // check for mean just basic testing
				 scopeConfidence = confidence(sc.getscopeMean(),sc.getscopeMargin());
				double queryConfidence = confidence(in.queryMean,in.queryMargin);
				if(scopeConfidence >= queryConfidence)
					{ 
					  queryAnswered = true;
					  break;
					 }
			}	
			
		}
		
		 NodeIdConfidence nodeIdConfidence	 = new NodeIdConfidence(nodeId,scopeConfidence,queryCrfNodes.get(0));
  // critiria for query answer with current ALLOW node
		
		//Begin Lookup of find the neigbor which has best routing model wrt query
		
		neighborToForward  = "";
		
		//double minMean =1;
		double maxConfidence = 0;
		
		Map<String,List<ScopeInformation>> map = routingTable.getNeighborScopes();
	    Set<String> keys =map.keySet();
		for(String neighbors:keys)
		{
			List<ScopeInformation> scopes = map.get(neighbors);
			if(scopes !=null){
			for(ScopeInformation sc:scopes)
			{
				List<Integer> crfNodeIds = sc.getNodeIdswithScope();
				if(crfNodeIds.contains(queryCrfNodes.get(0)))
				{  // check for mean just basic testing
					
					double scopeConfidenceTemp = confidence(sc.getscopeMean(),sc.getscopeMargin());
					double queryConfidence = confidence(in.queryMean,in.queryMargin);
					if(  (maxConfidence < scopeConfidenceTemp)  )
					{ 
					   neighborWithBestModel = neighbors;
					   maxConfidence = scopeConfidenceTemp;
					}
					
					/*if((sc.getscopeMean() < in.queryMean) && (minMean > sc.getscopeMean())  )
						{ 
						   neighborWithBestModel = neighbors;
						   minMean = sc.getscopeMean();
						}
					*/
				}	
				
			 } 
			}
		}
		
		AllowNode bestNeighborNode = null;
		for(int i=0;i<neighbors.size();i++ ){
		  AllowNode node = neighbors.get(i);
		  if(node.getAllowNodeId().equals(neighborWithBestModel))
			  bestNeighborNode = node;
		}
		  
		 
		
		if(queryAnswered == true){
			//System.out.println(" Node Print "+ nodeId);
			return nodeIdConfidence;
		}
		else if(in.hopCount >= 20){
			return null;
		}
		else if(  neighbors == null || ( (neighbors.size() == 1) && 
				( this.getAllowNodeId().equals(in.getQuerySourceAllowId())== false  )   ))
				{
			
			return nodeIdConfidence;
		}
		else if(neighbors != null && bestNeighborNode != null){
				return bestNeighborNode.RandomWalkWithNeighborTableQRetreival(in);
			
		}
		
		return null;
		
		
	}//end method Random Walk with neighbor table
	

	public double confidence(double mean, double var)
	{
		double confidence = Math.sqrt(1-mean)*(1-var);
		return confidence;
	}
	
	public void getBestNeighbor(Query in){
		
		List<Integer> queryCrflist = in.getQueryScopeCRFNodes();
		// check for I node testing
		
		 routingTable.getNeighborScopes();
		
	}
	
	// to specific neighbor with nodeID
	public void sentRoutingModeltoNeighbor(String neighborNodeId)
	{
		
		if(neighbors == null || (neighbors.size() <= 0)){
			return ;
		}
		
		for(int i=0;i<neighbors.size();i++ ){
           if(neighbors.get(i).getAllowNodeId().equals(neighborNodeId))
           {   
		     neighbors.get(i).receiveRoutingModelfromNeighbor( routingModel.getScopesInformation(), nodeId);
           }
		}
		
	}
	
	public void dumpTable()
	{

		System.out.println("\n Dumping Rotuing Table:\n ");
		routingTable.dumpTable(nodeId);
	}
	
	public void dumpKnowledgeModel(){
		System.out.println("\n Dumping  Knowledge Model:\n ");
		model.dumpModel(nodeId);
	}
	
	public void dumpRoutingModel(){
		
		System.out.println("\n Dumping Routing Knowledge Model:\n ");
		routingModel.dumpModel(nodeId);
	}
	
   public void dumpCrfMap(){
		
		System.out.println("\n Dumping CrfMap:\n ");
		CRFmap.dumpCRFMap(nodeId);;
   }

	public void receiveRoutingModelfromNeighbor(List<ScopeInformation> scopeInfoNew , String neighborNodeId )
	{
		/*if( checkChangeRoutingEntry(scopeInfoNew,neighborNodeId) ==  false)
			return;
		*/
		// update routing table accordingly
		
	//	System.out.println("\n Recieve: From "+neighborNodeId+"to "+this.getAllowNodeId()+"\n ");
		
	/*	for(ScopeInformation scope:scopeInfoNew)
		{
			scope.dumpScopeInformation();
		}
		*/
		updateRoutingTable(scopeInfoNew , neighborNodeId);
		updateNetwork(neighborNodeId);
        
	}
	
	private boolean checkChangeRoutingEntry(List<ScopeInformation> scopeInfoNew,String neighborNodeId)
	{
		Map<String,List<ScopeInformation>>  map = routingTable.getNeighborScopes();;
		List<ScopeInformation> scopeInfoOld = map.get(neighborNodeId);
		// if no of scopes changes then certainly a change !!!
		if (scopeInfoOld ==  null) return true;
		if(scopeInfoOld.size() != scopeInfoNew.size())
			return true;
		
		/// if any scopes has diffrent number of nodes then change occur !!
		for(ScopeInformation scopeold:scopeInfoOld)
		{
			for(ScopeInformation scopeNew:scopeInfoNew)
			{
				if(scopeold.getscopeId() == scopeNew.getscopeId()){
					if( scopeold.getscopeMean() != scopeNew.getscopeMean() 
							|| scopeold.getscopeInstances() != scopeNew.getscopeInstances())
						return true;
				}
			}
		}
		return false;
	}
	
   public void updateRoutingTable(List<ScopeInformation> scopeInfo , String neighborNodeId)
   {
	   routingTable.addEntrytoTable(neighborNodeId, scopeInfo);
   }
	
	public int[] toInt(Set<Integer> set) {
		  int[] a = new int[set.size()];
		  int i = 0;
		  for (Integer val : set) a[i++] = val;
		  return a;
		}
	
	public int getFailState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isUp() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFailState(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public String getAllowNodeID() {
		// TODO Auto-generated method stub
		return nodeId;
	}

	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Protocol getProtocol(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int protocolSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setIndex(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}