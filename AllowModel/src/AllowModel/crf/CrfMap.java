package AllowModel.crf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AllowModel.metrics.ConfidenceList;

public class CrfMap {

	int[][] adjacencyMatrix  ; //
	Map<Integer,List<ConfidenceList>> confidenceMap ;
	int numberofCRFNodes ;
	Map<Integer,ConfidenceList> constantConfidenceMap ;
	
	public CrfMap(File inputfile,String allowNodeId) throws IOException
	{
		adjacencyMatrix  	= new int[5][5];
		confidenceMap		= new HashMap<Integer,List<ConfidenceList>>();
		constantConfidenceMap = new HashMap<Integer,ConfidenceList>();
		populateConfidenceMap(inputfile, allowNodeId);
		
	
	}
	
	public CrfMap() {
		// TODO Auto-generated constructor stub
		adjacencyMatrix  	= new int[5][5];
		confidenceMap		= new HashMap<Integer,List<ConfidenceList>>();
		constantConfidenceMap = new HashMap<Integer,ConfidenceList>();
	}

	public void populateConfidenceMap(File inputfile,String allowNodeId) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(inputfile));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine(); // line by line reading
             int count=0;
             line = br.readLine(); // remove the first line(contains tags) 
	        while (line != null) {
	        	
	        	String[] words = line.split(" "); // split words
	        	// order of strings in line  // NodeId || Instances || mean || margin
	        	int crfNodeId = 0, instances = 0;
	        	double mean = 0,margin = 0;
	        	for (String word : words){
	        		word.trim();
	        		if(word.contentEquals(""))
	        			continue;
	        		else count++;
	        		if(count%4 ==1)
	        	 crfNodeId = Integer.parseInt(word);
	        		
	        		if(count%4 ==2)
	        	  instances = Integer.parseInt(word) ;
	        		
	        		if(count%4 ==3)
	        	 mean = Double.parseDouble(word );
	        		
	        		if(count%4 ==0)
	        	 margin = Double.parseDouble(word );
	        	}
	        	// create confidence List 
	        	ConfidenceList confidenceList = new	ConfidenceList(instances,mean,margin);
	        	// create map entry with this crf node;
	        	
	        	List<ConfidenceList> list  =   confidenceMap.get(crfNodeId);
	        	if(list == null)
	        		list = new ArrayList<ConfidenceList>();
	        	
	        	list.add(confidenceList);
	        	confidenceMap.put(crfNodeId,list );
	        	
	           line = br.readLine();
	        }
	        
	        
	    } finally {
	        br.close();
	    }
	    
	    populateconstantConfidenceMap(allowNodeId);
	}
	
	public int[][] getAjdacency(){
		
		return adjacencyMatrix;
		
		}
	public Map<Integer,List<ConfidenceList>> getConfidenceMap()
	{
		return confidenceMap;
	}
	 
	public void populateconstantConfidenceMap(String allowNodeId)
	{
		int instancesIdofLearning = 0; // taken at index from the list of 
		//condeinces for a particuar CRF node
		
		int allowNode =Integer.parseInt(allowNodeId) ;
		instancesIdofLearning  = allowNode%10;
	//	Set<Integer> keys = confidenceMap.keySet();
		
		for (Integer key : confidenceMap.keySet()) {
			
			ConfidenceList conListCrfNode = confidenceMap.get(key).get(instancesIdofLearning); 
			// on different crf nodes have different instance of learning
			constantConfidenceMap.put(key, conListCrfNode);
			instancesIdofLearning++;
		}

	}
	// constant for testing at certain instances at each ALLOW nodes
	public Map<Integer,ConfidenceList> getConstantConfidenceMap()
	{
		return constantConfidenceMap;
	}
	
	
	public int getNumberofCRFNodes()
	{
		numberofCRFNodes = constantConfidenceMap.keySet().size();
		return numberofCRFNodes;
	}
	
	public Set<Integer> getCRFNodesIds()
	{
		Set<Integer> number = constantConfidenceMap.keySet();
		return number;
	}
}
