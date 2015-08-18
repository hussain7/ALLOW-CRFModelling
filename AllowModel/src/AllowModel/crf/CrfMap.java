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
	int instancesIdofLearning ;
	
	public CrfMap(File inputfile,String allowNodeId) throws IOException
	{
		adjacencyMatrix  	= new int[6][6];  // six nodes of crf model
		buildAjdMatrix(adjacencyMatrix);
		confidenceMap		= new HashMap<Integer,List<ConfidenceList>>();
		constantConfidenceMap = new HashMap<Integer,ConfidenceList>();
		populateConfidenceMap(inputfile, allowNodeId);
		instancesIdofLearning =0;
	
	}
	
	public CrfMap() {
		// TODO Auto-generated constructor stub
		adjacencyMatrix  	= new int[6][6];
		buildAjdMatrix(adjacencyMatrix);
		confidenceMap		= new HashMap<Integer,List<ConfidenceList>>();
		constantConfidenceMap = new HashMap<Integer,ConfidenceList>();
		instancesIdofLearning = 0;
	}
	
	private void buildAjdMatrix(int [][]mat)
	{
		mat[0][1] = 1; mat[1][0] =1;
		mat[1][2] = 1; mat[2][1] =1;
		mat[2][3] = 1; mat[3][2] =1;
		mat[2][4] = 1; mat[4][2] =1;
		mat[4][5] = 1; mat[5][4] =1;		
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
	    
	    //populateconstantConfidenceMap(allowNodeId);
	    populatingConstantMap(allowNodeId);
	}
	

	public int[][] getAdjacencyMatrix()
	{
		return adjacencyMatrix;
	}
	public Map<Integer,List<ConfidenceList>> getConfidenceMap()
	{
		return confidenceMap;
	}
	 
	public void populateconstantConfidenceMap(String allowNodeId)
	{
		// taken at index from the list of 
		//condeinces for a particuar CRF node
		
		int allowNode =Integer.parseInt(allowNodeId) ;
		instancesIdofLearning  = allowNode%10;
	  //	Set<Integer> keys = confidenceMap.keySet();
		int temp = instancesIdofLearning;
		for (Integer key : confidenceMap.keySet()) {
			
			ConfidenceList conListCrfNode = confidenceMap.get(key).get(temp); 
			// on different crf nodes have different instance of learning
			constantConfidenceMap.put(key, conListCrfNode);
			temp++;
		}

	}
	 //testing !!!!
	private void populatingConstantMap(String allowNodeId){
		
		int allowNode =Integer.parseInt(allowNodeId) ;
		int x =11;
		double testmean = Math.exp(-x/300);
		double var =  (1-testmean)*testmean/x;
		
		switch(allowNode){
		
		case 1:
			for (Integer key : confidenceMap.keySet()) {
			
			if(key ==1 || key ==2){
				x=11;
				
				testmean = 0.969060 ;
				 var =  Math.sqrt((1-testmean)*(testmean)/x);
				ConfidenceList list = new ConfidenceList(x,testmean,var );
				constantConfidenceMap.put(key, list);
			}
			
			if(key ==3 || key ==4){
				x=381;
				testmean = 0.336697;
				 var =  Math.sqrt((1-testmean)*(testmean)/x);
				ConfidenceList list = new ConfidenceList(x,testmean,var );
				constantConfidenceMap.put(key, list);
			}
			
		   }
			break;
		case 2:
			for (Integer key : confidenceMap.keySet()) {
				
				if(key ==1 || key ==2 || key==3){
					x=381;
					
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==4 || key==5 ){
					x=1021;
					
					testmean =0.054088;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==6 || key==7 || key==8){
					x=1451;
					testmean = 0.015832;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==9 || key==10 ){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
			   }
			break;
		case 3:
               for (Integer key : confidenceMap.keySet()) {
				
				if(key ==1 || key ==2){
					x=11;
					testmean = 0.969060 ;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==3 || key ==4){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				if(key ==4 || key==5 ||key==6 ){
					x=151;
					
					testmean = 0.649580;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==7 || key==8 || key==9 || key==10  ){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
			   }
			break;
		case 4:
              for (Integer key : confidenceMap.keySet()) {
				
				if(key ==1 || key ==2){
					x=1451;
					testmean = 0.015832;
					
					var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==3 || key ==4){
					x=11;
					
					testmean = 0.969060 ;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				if(key ==5 || key ==9 || key ==5 || key ==6 || key ==7 || key ==8||key ==10 ){
					x=621;
					testmean = 0.169605;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				
			   }
			break;
		case 5:
               for (Integer key : confidenceMap.keySet()) {
				
				if(key ==1 || key ==2){
					x=151;
					
					testmean = 0.649580;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==3 || key ==4 || key ==5 || key ==6 || key ==7 || key ==8){
					x=1451;
					testmean = 0.015832;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==9 || key ==10){
					x=11;
					
					testmean = 0.969060 ;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
			   }
			break;
		case 6:
             for (Integer key : confidenceMap.keySet()) {
				
				if(key ==1 || key ==2){
					x=11;
					
					testmean = 0.969060 ;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==3 || key ==4){  
					x=1451;
					testmean = 0.015832;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				if(key ==5 || key==6 ){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==6 || key==7 || key==8 || key==9 || key==10  ){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
			   }
			break;
		case 7:
          for (Integer key : confidenceMap.keySet()) {
				
				if(key ==1 || key ==2){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==3 || key ==4){
					x=901;
					
					testmean = 0.076208 ;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				if(key ==5 || key==6 ){
					x=381;
					testmean = 0.336697;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
				if(key ==6 || key==7 || key==8 || key==9 || key==10  ){
					x=151;
				
					testmean = 0.649580;
					 var =  Math.sqrt((1-testmean)*(testmean)/x);
					ConfidenceList list = new ConfidenceList(x,testmean,var );
					constantConfidenceMap.put(key, list);
				}
			   }
			break;
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
	
	
	
	public void incrementStateofEntity()
	{
		
		instancesIdofLearning++ ;
	//	Set<Integer> keys = confidenceMap.keySet();
		int temp = instancesIdofLearning;
		for (Integer key : confidenceMap.keySet()) {
			
			ConfidenceList conListCrfNode = confidenceMap.get(key).get(temp); 
			// on different crf nodes have different instance of learning
			constantConfidenceMap.put(key, conListCrfNode);
			temp++;
		}
		
	}
	public void dumpCRFMap(String allowNodeId)
	{
		StringBuffer s = new StringBuffer();
		
		s.append("\n ALLOW ID:"+allowNodeId+"\n" );
		Set<Integer> keys = getCRFNodesIds() ;
		
		for(Integer key:keys)
		{
			ConfidenceList list = constantConfidenceMap.get(key);
			double mean = list.getMean();
			double margin = list.getMargin();
			double instance  = list.getInstance();
			double confidence = Math.sqrt(1-mean)*(1-margin);
			
			s.append("CrfNode: "+Integer.toString(key)+ "Instance: "+list.getInstance()+"Mean: "+list.getMean()
					+"Margin: "+list.getMargin()+ "Confidence Score" + confidence +"\n");
		}
		
	System.out.print(s);

	}
	
}
