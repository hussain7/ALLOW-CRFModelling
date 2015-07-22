package protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CrfMap {

	int[][] adjacencyMatrix  ; //
	Map<Integer,ConfidenceList> confidenceMap ;
	int numberofCRFNodes ;
	
	CrfMap(File inputfile) throws IOException
	{
		adjacencyMatrix  	= new int[5][5];
		confidenceMap		= new HashMap<Integer,ConfidenceList>();
		populateConfidenceMap(inputfile);
	
	}
	
	public CrfMap() {
		// TODO Auto-generated constructor stub
		adjacencyMatrix  	= new int[5][5];
		confidenceMap		= new HashMap<Integer,ConfidenceList>();
		
	}

	public void populateConfidenceMap(File inputfile) throws IOException
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
	        	confidenceMap.put(crfNodeId, confidenceList);
	        	
	           line = br.readLine();
	        }
	        
	        
	    } finally {
	        br.close();
	    }
	}
	
	public int[][] getAjdacency(){
		
		return adjacencyMatrix;
		
		}
	public Map<Integer,ConfidenceList> getConfidenceMap()
	{
		return confidenceMap;
	}
	
	public int getNumberofCRFNodes()
	{
		numberofCRFNodes = confidenceMap.keySet().size();
		return numberofCRFNodes;
	}
	
	public Set<Integer> getCRFNodesIds()
	{
		Set<Integer> number = confidenceMap.keySet();
		return number;
	}
}
