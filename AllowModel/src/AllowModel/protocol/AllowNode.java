package AllowModel.protocol;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import AllowModel.clustering.KMedoids;
import AllowModel.crf.CrfMap;
import AllowModel.metrics.ConfidenceList;
import AllowModel.metrics.Distance;
import AllowModel.metrics.MergeDistribution;
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
	String nodeId;
	
	CrfMap CRFmap;
	List<AllowNode> neighbors;
	RoutingTable routingTable  ;
	LocalKnowledgeModel model;
	Query currentQuery;
	RoutingKnowledgeModel routingModel ;/// model formed empty
	
	public AllowNode(String idNum,File inputFile) throws IOException
	{
		//super(idNum);
		nodeId = idNum;
		neighbors = new ArrayList<AllowNode>();
		CRFmap = new CrfMap(inputFile,nodeId);
		routingTable = new RoutingTable(); 
		
	}
	public void addNeighbor(AllowNode node)
	{  
		neighbors.add(node);
	}
	
	//constructor
	public AllowNode(String idNum, List<AllowNode> nodeList,File inputFile) throws IOException{
		//super(idNum);
		nodeId= idNum;
		neighbors = nodeList;
		CRFmap = new CrfMap(inputFile,nodeId);
		routingTable = new RoutingTable(); 	
	}

	
	public List<AllowNode> getNeighbors(){
		
		return neighbors;
		
	}
	public Query GenerateQuery(double queryMean,double margin){
		
		List<Integer> queryScopesCRFNodes = new  ArrayList<Integer>();
		queryScopesCRFNodes.add(2, 3);
		Query query = new Query(nodeId, queryMean,margin,queryScopesCRFNodes);
		currentQuery =query;
		
		return query;
	} // end method GenerateQuery

	
	public int[][] clusteringCrfNodes( CrfMap map)
	{
		 
		 int numberOfClusters = 5 ;
		 int maxIterations =100 ; 
		 Distance dm = new Distance() ;
		 
	    KMedoids clustering = new KMedoids(numberOfClusters, maxIterations,dm,map );
	    int noofCRFNodes = map.getNumberofCRFNodes();
	    
	    Set<Integer> CRFNodesIds  = map.getCRFNodesIds();
	    int[][] output = clustering.cluster(toInt(CRFNodesIds));
	  
		return output;
		
	}
	
	public void buildLocalKnowlegdeModel()
	{
	    model = new LocalKnowledgeModel(nodeId);
		int[][] clusters = clusteringCrfNodes(CRFmap);
		int noOfClusters = clusters.length;
	
		for(int i=0;i<noOfClusters; i++)
		{
		  List<Integer> nodeIdsList  = new ArrayList<Integer>();
		  for(int j=0;j<clusters[0].length; j++)
		  {
			  nodeIdsList.add(clusters[i][j]);
		  }
		  
		   MergeDistribution avg = new MergeDistribution( CRFmap, clusters[i]) ; // get the avg mean and margin of cluster 
		 
		   ScopeInformation info = new ScopeInformation( avg.getMean(), avg.getMargin(), nodeIdsList, 0,avg.getNoofInstances()); 
		   model.addScopeInformation(info);  // add cluster information in the model
		  
		}
			
	}
	
	public void populateRoutingModel(CrfMap map)
	{ 
		// clustering of crf nodes are done
		int[][] clusters = clusteringCrfNodes(map);
		int noOfClusters = clusters.length;
		
		for(int i=0;i<noOfClusters; i++)
		{
		  List<Integer> nodeIdsList  = new ArrayList<Integer>();
		  for(int j=0;j<clusters[0].length; j++)
		  {
			  nodeIdsList.add(clusters[i][j]);
		  }
		  
		   MergeDistribution avg = new MergeDistribution( map, clusters[i]) ; // get the avg mean and margin of cluster 
		 
		   ScopeInformation info = new ScopeInformation( avg.getMean(), avg.getMargin(), nodeIdsList, 0,avg.getNoofInstances()); 
		   routingModel.addScopeInformation(info);  // add cluster information in the model
		  
		}
		routingModel.getScopesInformation(); // test 
		
	}
	
	
	public String getAllowNodeId()
	{
		return nodeId;
	}
	
	public void buildRoutingModel()
	{
		routingModel = new RoutingKnowledgeModel(nodeId);
	 	// build routing model of a ALLOW node initially
		//get local model first //
		List<ScopeInformation> localModelScopes = model.getScopesInformation();
		// get remote scopes information
		int  noofCRFNodes = CRFmap.getNumberofCRFNodes();
		Set<Integer> crfNodesIdsList = CRFmap.getCRFNodesIds();
		
		// temp map to store output
		CrfMap tempCRFMap = new CrfMap();
		
		for(Integer crfnodeId:crfNodesIdsList){
			double sum_mean =0; 
			int sum_instances =0;
			double sum_margin =0;
		
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
				  {
					 double margin  =scopes.getscopeMargin();
					 double mean =  scopes.getscopeMean();
					 double instances = scopes.getscopeInstances();
					 
					 sum_mean =sum_mean + mean*instances;
					 sum_instances = (int) (sum_instances + instances);
					 sum_margin = Math.pow(instances, 2)*margin + sum_margin;
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
				 
				 sum_mean =sum_mean + mean*instances;
				 sum_instances = (int) (sum_instances + instances);
				 sum_margin = Math.pow(instances, 2)*margin + sum_margin;
			  }
			 
		   }
		   /// final mean and margin of each CRF node  . let this be populated 
		 double finalMeanCRFNode = sum_mean/sum_instances;
		 double finalMarginCRFNode = sum_margin/Math.pow(sum_instances, 2);
		 
		 // populate this information in routing table for each CRF node
		 // tempopary store new infomration of CRF nodes .
		 
		 ConfidenceList list = new  ConfidenceList(sum_instances/noofCRFNodes,finalMeanCRFNode,finalMarginCRFNode);
		 tempCRFMap.getConstantConfidenceMap().put(crfnodeId, list);
		 
		}
		 
		// finally clustering with the new means and margins on the CRF nodes to get the final routing model 
	// and populate in routing model.
		
	  // clustering & populating in routing node	
		populateRoutingModel(tempCRFMap);			
		
		
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
	public String RandomWalk(Query in){
		//pick random neighbor to ask
		Random rand = new Random();
		String neighborToAsk = "0";
		int randomIndex=-1;
		
		in.allowNodeIdsVisited.add(nodeId);
		in.hopCount++;
		
		boolean queryAnswered=false;
		if(neighbors == null || (neighbors.size() <= 0)){
			return "NN";
		}
		else{  
			randomIndex = rand.nextInt(neighbors.size());
			neighborToAsk =  neighbors.get(rand.nextInt(neighbors.size())).getAllowNodeID();
		}
		
		if( false   )   // critiria for query answer with current ALLOW node
			queryAnswered=true;
		
		
		if(queryAnswered){
			return nodeId;
		}
		else if(in.hopCount >= 100){
			return "NA";
		}
		else{
			if(neighbors != null){
				return neighbors.get(randomIndex).RandomWalk(in);
			}
		}
		
		return "NN";
		
		
	}//end method Random Walk
	
	
	/*Method RandomWalk with neighbor tables look ahead: This method checks to see if its neighbors lists of files have
	 * the desired query within them. If so that neighbor is walked to if not it randomly choosed*/
	public String RandomWalkWithNeighborTable(Query in){
		//pick random neighbor to ask
		Random rand = new Random();
		String neighborToForward = null;
		boolean neighborHasFile=false;
		AllowNode neighborWithBestModel = null;
		
		in.allowNodeIdsVisited.add(nodeId);
		in.hopCount++;
		
		boolean queryAnswered = false;
		
		// check if query can be answered by the current ALLOW node
			
		if( false   )   // critiria for query answer with current ALLOW node
			queryAnswered=true;
		
		//Begin Lookup of find the neigbor which has best routing model wrt query
		
		neighborToForward  = "";
		for(int i=0;i<neighbors.size();i++ ){
		  AllowNode node = neighbors.get(i);
		  if(true)  // condition to check neighbor routing model
		     { 
			  neighborWithBestModel = node;
			  break;
		     }
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
			if(neighbors != null){
				return neighborWithBestModel.RandomWalkWithNeighborTable(in);
			}
		}
		
		return "NN";
		
		
	}//end method Random Walk with neighbor table
	
	
	public void getBestNeighbor(Query in){
		
		List<Integer> queryCrflist = in.getQueryScopeCRFNodes();
		// check for I node testing
		
		 routingTable.getNeighborScopes();
		
	}
	
	
	
	public void sentRoutingModeltoNeighbors()
	{
		
		if(neighbors == null || (neighbors.size() <= 0)){
			return ;
		}
		
		for(int i=0;i<neighbors.size();i++ ){
 
		  neighbors.get(i).receiveRoutingModelfromNeighbor( routingModel.getScopesInformation(), nodeId);
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
		
	}

	public void receiveRoutingModelfromNeighbor(List<ScopeInformation> scopeInfo , String neighborNodeId )
	{
		// update routing table accordingly
		updateRoutingTable(scopeInfo , neighborNodeId);

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