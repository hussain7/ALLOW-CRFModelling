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

	
	Map<Integer,List<ConfidenceList>> confidenceMap ;
	int numberofCRFNodes ;
	Map<Integer,ConfidenceList> constantConfidenceMap ;
	int instancesIdofLearning ;
	
	public CrfMap(File inputfile,String allowNodeId) throws IOException
	{
	
		confidenceMap = new HashMap<Integer,List<ConfidenceList>>();
		int keys = 7;
		for(int i=1; i<=keys; i++)
		{
		  confidenceMap.put(i, null);
		}
		
		constantConfidenceMap = new HashMap<Integer,ConfidenceList>();
		//populateConfidenceMap(inputfile, allowNodeId);
		instancesIdofLearning =0;
		populatingConstantMap(allowNodeId);
	
	}
	
	public CrfMap() {
		// TODO Auto-generated constructor stub
	
		confidenceMap		= new HashMap<Integer,List<ConfidenceList>>();
		constantConfidenceMap = new HashMap<Integer,ConfidenceList>();
		int keys = 7;
		for(int i=1; i<=keys; i++)
		{
		  confidenceMap.put(i, null);
		}
		numberofCRFNodes = keys;

		instancesIdofLearning = 0;
		
	}
	


	/*public void populateConfidenceMap(File inputfile,String allowNodeId) throws IOException
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

	}*/
	 //testing !!!!
	
	public void populatingConstantMap(String allowNodeId){
		
		int allowNode =Integer.parseInt(allowNodeId) ;
		int n=11;
	 	double  testmean = 0.969060;
		double   var =  Math.sqrt((1-testmean)*(testmean)/n);
		
		switch(allowNode){
		
		case 0:
			for (Integer key : confidenceMap.keySet()) {
			
		
				n=11;
			    testmean =  0.969060 + key*0.0001 + 2*0.00001;
				var =  Math.sqrt((1-testmean)*(testmean)/n);
				ConfidenceList list = new ConfidenceList(n,testmean,var );
				constantConfidenceMap.put(key, list);
			
			
		   }
			break;
		case 3:
			for (Integer key : confidenceMap.keySet()) {
				
	if(key ==5|| key == 4|| key == 6|| key ==14 || key ==12|| key ==13){
					
					n=14511;
				    testmean = 0.00015832 + key*0.000001 + 4*0.0000001;;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060 +  key*0.00001 + 1*0.0000001;;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				
			   }
			break;
		case 5:
               for (Integer key : confidenceMap.keySet()) {
				
            	
    					n=11;
    				    testmean =  0.969060 +  key*0.00001 + 1*0.0000001;;
    					var =  Math.sqrt((1-testmean)*(testmean)/n);
    					ConfidenceList list = new ConfidenceList(n,testmean,var );
    					constantConfidenceMap.put(key, list);
    				 
            	  
				
			   }
			break;
		case 1:
              for (Integer key : confidenceMap.keySet()) {
				
				if(key ==2|| key ==3 ||key == 10 || key == 11   ){
					
					n=14511;
				    testmean = 0.00015832 + key*0.000001 + 4*0.0000001;;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060 +  key*0.00001 + 1*0.0000001;;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				 
			   }
			break;
		case 4:
               for (Integer key : confidenceMap.keySet()) {
				
            	   n=11;
				    testmean =  0.969060 + key*0.00000005 + 4*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				
			   }
			break;
		case 2:
             for (Integer key : confidenceMap.keySet()) {
				
				if(key ==6 || key ==16|| key ==8 || key ==12){
					n=14511;
				    testmean = 0.0000015832 + key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060+ key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				
			   }
			break;
		case 94:
            for (Integer key : confidenceMap.keySet()) {
				
				if(key ==15 || key==7 || key==6){
					n=14511;
				    testmean = 0.0000015832 + key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060+ key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				
			   }
			break;
		case 150:
            for (Integer key : confidenceMap.keySet()) {
				
				if(key ==6 || key==5 ){
					n=14511;
				    testmean = 0.0000015832 + key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060+ key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				
			   }
			break;
		case 872:
            for (Integer key : confidenceMap.keySet()) {
				
				if( key ==7 || key ==5 ){
					n=14511;
				    testmean = 0.00000000000015832 + key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060+ key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				
			   }
			break;
		case 59:
            for (Integer key : confidenceMap.keySet()) {
				
				if(key ==2 || key==3 || key ==2 || key==10 ){
					n=14511;
				    testmean = 0.0000015832 + key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060+ key*0.000001 + 2*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				 }
				
			   }
			break;
		case 6:
          for (Integer key : confidenceMap.keySet()) {
				
				if(key ==5 || key ==1 || key == 2||key == 3 || key == 7 || key == 6){
					n=14511;
				    testmean = 0.00015832 + key*0.0000001 + 6*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				else
				{
					n=11;
				    testmean =  0.969060 + key*0.0000001 + 6*0.0000001;
					var =  Math.sqrt((1-testmean)*(testmean)/n);
					ConfidenceList list = new ConfidenceList(n,testmean,var );
					constantConfidenceMap.put(key, list);
				}
				
			   }
			break;
			
		case 7:
	          for (Integer key : confidenceMap.keySet()) {
					
					if( key ==13 || key ==12 || key ==14 || key ==7 ){
						n=14511;
					    testmean = 0.00015832 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					else
					{
						n=11;
					    testmean =  0.969060 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					
				   }
				break;
		case 18:
	          for (Integer key : confidenceMap.keySet()) {
					
					if(key ==4 || key ==2 || key ==3 || key ==5){
						n=14511;
					    testmean = 0.00015832 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					else
					{
						n=11;
					    testmean =  0.969060 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					
				   }
				break;
		case 12:
	          for (Integer key : confidenceMap.keySet()) {
					
					if( key ==1|| key ==2 || key ==10 || key ==3 ){
						n=14511;
					    testmean = 0.0015832 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					else
					{
						n=11;
					    testmean =  0.969060 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					
				   }
				break;
		case 15:
	          for (Integer key : confidenceMap.keySet()) {
					
					if(key ==2 || key ==3 || key ==1 ){
						n=14511888;
					    testmean = 0.0000000000000015832 + key*0.000001 + 2*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					else
					{
						n=11;
					    testmean =  0.969060 + key*0.0000001 + 7*0.0000001;
						var =  Math.sqrt((1-testmean)*(testmean)/n);
						ConfidenceList list = new ConfidenceList(n,testmean,var );
						constantConfidenceMap.put(key, list);
					}
					
				   }
				break;
				default :
				     for (Integer key : confidenceMap.keySet()) {
							
							if(key ==6 ){
								n=14511;
							    testmean = 0.00015832 + key*0.0000001 + 1222*0.0000001;
								var =  Math.sqrt((1-testmean)*(testmean)/n);
								ConfidenceList list = new ConfidenceList(n,testmean,var );
								constantConfidenceMap.put(key, list);
							}
							else
							{
								n=11;
							    testmean =  0.969060 + key*0.0000001 + 1223*0.0000001;
								var =  Math.sqrt((1-testmean)*(testmean)/n);
								ConfidenceList list = new ConfidenceList(n,testmean,var );
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
