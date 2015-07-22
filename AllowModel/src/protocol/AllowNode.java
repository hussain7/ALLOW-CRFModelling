package protocol;

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

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.*;
import peersim.vector.SingleValueHolder;


public class AllowNode //extends GeneralNode
{
	String nodeId;
	Files CRFNodeFile = new Files();
	public  CrfMap CRFmap;
	List<AllowNode> neighbors= new ArrayList<AllowNode>();
	RoutingTable routingTable  ;
	LocalKnowledgeModel model;
	Query currentQuery;
	RoutingKnowledgeModel routingModel ;/// model formed empty
	
	public AllowNode(String idNum,File inputFile) throws IOException
	{
		//super(idNum);
		nodeId = idNum;
		neighbors = null;
		CRFmap = new CrfMap(inputFile);
		
	}
	
	//constructor
	public AllowNode(String idNum, List<AllowNode> nodeList,File inputFile) throws IOException{
		//super(idNum);
		nodeId= idNum;
		neighbors = nodeList;
		CRFmap = new CrfMap(inputFile);
		routingTable = new RoutingTable(); 
		
	}

	
	public Query GenerateQuery(Files search){
		
		
		Query query = new Query(nodeId, search);
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
		model.getScopesInformation(); // test 
		
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
		int  noofCRFNodes = CRFmap.numberofCRFNodes;
		Set<Integer> crfNodesIdsList = CRFmap.getCRFNodesIds();
		
		// temp map to store output
		CrfMap tempCRFMap = new CrfMap();
		
		
		for(Integer crfnodeId:crfNodesIdsList){
			double sum_mean =0; 
			int sum_instances =0;
			double sum_margin =0;
		Iterator<AllowNode> iterator = neighbors.iterator();
		while (iterator.hasNext()) {
			Map<String, List<ScopeInformation>> neighborScopes =
					routingTable.getNeighborScopes();
			List<ScopeInformation> neighborScopesList = neighborScopes.get(((AllowNode) iterator).getAllowNodeId());
			// merge distribution of local model and model of neighbor node
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
			 
			iterator.next();
		   }	
		// add with the local knowlegde part for each CRF node
		
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
		 
		 
		 ConfidenceList list = new  ConfidenceList(sum_instances,finalMeanCRFNode,finalMarginCRFNode);
		 tempCRFMap.getConfidenceMap().put(crfnodeId, list);
		 
		}
		 
		// finally clustering with the new means and margins on the CRF nodes to get the final routing model 
	// and populate in routing model.
		
	  // clustering & populating in routing node	
		populateRoutingModel(tempCRFMap);	
		
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

	public long getID() {
		// TODO Auto-generated method stub
		return 0;
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