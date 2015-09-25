package AllowModel.clustering;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import AllowModel.crf.CrfMap;
import AllowModel.metrics.AllowDistance;
import AllowModel.metrics.ConfidenceList;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import AllowModel.metrics.MergeDistribution;
 
public class prim
{
    private boolean unsettled[];
    private boolean settled[];
    private int numberofvertices;
    private double adjacencyMatrix[][];
    private double key[];

    public static final int INFINITE = 999;
    private int parent[];
    private static CrfMap crfmap;
 
    public prim(int numberofvertices)
    {
        this.numberofvertices = numberofvertices;
        unsettled = new boolean[numberofvertices + 1];
        settled = new boolean[numberofvertices + 1];
        adjacencyMatrix = new double[numberofvertices + 1][numberofvertices + 1];
       // crfmap = new CrfMap();
        
        key = new double[numberofvertices + 1];
        parent = new int[numberofvertices + 1];
    }
 
    public int getUnsettledCount(boolean unsettled[])
    {
        int count = 0;
        for (int index = 0; index < unsettled.length; index++)
        {
            if (unsettled[index])
            {
                count++;
            }
        }
        return count;
    }
 
    public void primsAlgorithm(double adjacencyMatrix[][],CrfMap CRFmap)
    {
    	 crfmap = CRFmap;
    	 Map<Integer, ConfidenceList> constMap=  crfmap.getConstantConfidenceMap();
    	 
        int evaluationVertex;
        for (int source = 1; source <= numberofvertices; source++)
        {
            for (int destination = 1; destination <= numberofvertices; destination++)
            {
                this.adjacencyMatrix[source][destination] = adjacencyMatrix[source][destination];
            }
        }
 
        for (int index = 1; index <= numberofvertices; index++)
        {
            key[index] = INFINITE;
        }
        key[1] = 0;
        unsettled[1] = true;
        parent[1] = 1;
 
        while (getUnsettledCount(unsettled) != 0)
        {
            evaluationVertex = getMimumKeyVertexFromUnsettled(unsettled);
            unsettled[evaluationVertex] = false;
            settled[evaluationVertex] = true;
            evaluateNeighbours(evaluationVertex);
        }
    } 
 
    private int getMimumKeyVertexFromUnsettled(boolean[] unsettled2)
    {
        double min = Integer.MAX_VALUE;
        int node = 0;
        for (int vertex = 1; vertex <= numberofvertices; vertex++)
        {
            if (unsettled[vertex] == true && key[vertex] < min)
            {
                node = vertex;
                min = key[vertex];
            }
        }
        return node;
    }
 
    public void evaluateNeighbours(int evaluationVertex)
    {
 
        for (int destinationvertex = 1; destinationvertex <= numberofvertices; destinationvertex++)
        {
            if (settled[destinationvertex] == false)
            {
                if (adjacencyMatrix[evaluationVertex][destinationvertex] != INFINITE)
                {
                    if (adjacencyMatrix[evaluationVertex][destinationvertex] < key[destinationvertex])
                    {
                        key[destinationvertex] = adjacencyMatrix[evaluationVertex][destinationvertex];
                        parent[destinationvertex] = evaluationVertex;
                    }
                    unsettled[destinationvertex] = true;
                }
            }
        }
    }
 
    public void printMST()
    {
       // System.out.println("SOURCE  : DESTINATION = WEIGHT");
        for (int vertex = 2; vertex <= numberofvertices; vertex++)
        {
          //  System.out.println(parent[vertex] + "\t:\t" + vertex +"\t=\t"+ adjacencyMatrix[parent[vertex]][vertex]);
        }
        
        clusterMST(6);  
        
    }
    
    
    public int[][] clusterMST(int pruneNumber)
    {
    	
    	 Map<Double,List<Integer>> map = new HashMap<Double,List<Integer>>();
    	 Map<Integer, ConfidenceList> constMap=  crfmap.getConstantConfidenceMap();
    	 
    	 
    	 List<Vector<Vector<Integer>> > finalClustersList = new ArrayList< Vector< Vector<Integer> > >();
    	
    	 
    	 // neighboring nodes map
    	 Map<Integer,List<Integer>> mapNeigh = new HashMap<Integer,List<Integer>>();
    	 
    	 List crfNodes = new ArrayList<Integer>();
    	 for(int i=1;i<= crfmap.getNumberofCRFNodes();i++)
    	 crfNodes.add(i);
    	 
    	// int crfNodes[] = new int[numberofvertices];
    	 
       //  crfNodes.addAll( constMap.keySet());
    	 double confidenceEffective =0;
    	 double confidenceEffectivePrevious =0;
         boolean flagMaxCheck =false;
         Vector<Vector<Integer>> finalClusters = new Vector<Vector<Integer>>();  
         Vector<Vector<Integer>> superClusters = new Vector<Vector<Integer>>(); 
         
         Vector<Vector<Integer>> previousClusters = null ;
         Vector<Vector<Integer>> finalCluster = null;
    	 
     //  System.out.println("SOURCE  : DESTINATION = WEIGHT");
        for (int vertex = 2; vertex <= numberofvertices; vertex++)
        {   
        	//crfNodes.add(parent[vertex]) ;
            
        //    System.out.println(parent[vertex] + "\t:\t" + vertex +"\t=\t"+ adjacencyMatrix[parent[vertex]][vertex]);
            List arr = new ArrayList<Integer>();
            arr.add(parent[vertex]);
            arr.add(vertex);
            Random rand = new Random();
            // it contains the key already 
           if( map.containsKey(adjacencyMatrix[parent[vertex]][vertex]) == true)
           {
        	   rand.nextInt(1000);
        	   map.put(adjacencyMatrix[parent[vertex]][vertex] + 0.00000005*rand.nextInt(1000), arr); 
           }
           else{
            map.put(adjacencyMatrix[parent[vertex]][vertex], arr);
           }
            // fill neighboring nodes map
            
            List<Integer> list = mapNeigh.get(parent[vertex]);
            if(list == null)
            	{ 
            	  list = new ArrayList<Integer>(); 
            	  list.add(vertex);
            	  mapNeigh.put(parent[vertex], list);
            	}
            else{
            	list.add(vertex);
            	mapNeigh.put(parent[vertex], list);
            }
            
            // do same for vertex as well
            List<Integer> list2 = mapNeigh.get(vertex);
            if(list2 == null)
            	{ 
            	  list2 = new ArrayList<Integer>(); 
            	  list2.add(parent[vertex]);
            	  mapNeigh.put(vertex, list2);
            	}
            else{
            	list2.add(parent[vertex]);
            	mapNeigh.put(vertex, list2);
            }
            
        }
        
        Set<Double> keys = map.keySet();
        // make set ereverse
        List<Double> listWeights = new ArrayList<Double>(keys);
        Collections.sort(listWeights);;
        Collections.reverse(listWeights);;
        
        
        Vector<Integer> superCluster = new Vector<Integer>();
        
        superCluster.addAll(crfNodes);
        finalClusters.add(superCluster);  // put the first vector
        superClusters.add(superCluster);
       
        int count  =0;
        for( Double item : listWeights ){
           
        	if( count >=  pruneNumber)
        		break;
           
        	
        	List<Integer> nodestoCut = map.get(item);
        	nodestoCut.get(0);
        	nodestoCut.get(1);
        	
        	double confidenceBeforePartition = 0;
        	double sumconfidenceClusterOld =0;
        	double sumconfidenceClusterNew =0;
        	Vector<Integer> clustertoPartition =  null;
        // get cluster contaning the nodes
        	Iterator<Vector<Integer>> iterator =  finalClusters.iterator()  ;
        	while(iterator.hasNext())
        	{
        		Vector<Integer> vector  =	iterator.next();
        		if( vector.contains(nodestoCut.get(0)) && vector.contains(nodestoCut.get(1)) ){
        			clustertoPartition  = vector;
        			Map<Integer,ConfidenceList > confidenceMap = new HashMap<Integer,ConfidenceList >();
                	// from that cluster vector , get avg confidence ,
                	int[] nodeData = new int[clustertoPartition.size()];
                	int i=0;
                	for(Integer crfNodeId:clustertoPartition ){
                		nodeData[i] =  crfNodeId;
                		i++;
                		confidenceMap.put(crfNodeId, constMap.get(crfNodeId)) ;
                	}
                	
                	MergeDistribution merge =  new MergeDistribution(confidenceMap,nodeData);
                	
                	// get avg information the cluster
                    confidenceBeforePartition = Math.sqrt(1 - merge.getMean())*merge.getMargin();
                	
        		}
        		else
        		{  
        			// other cluster which need not to break 
        			
        			Map<Integer,ConfidenceList > confidenceMap = new HashMap<Integer,ConfidenceList >();
                	// from that cluster vector , get avg confidence ,
                	int[] nodeData = new int[vector.size()];
                	int i=0;
                	for(Integer crfNodeId:vector ){
                		nodeData[i] =  crfNodeId;
                		i++;
                		confidenceMap.put(crfNodeId, constMap.get(crfNodeId)) ;
                	}
                	
                	MergeDistribution merge =  new MergeDistribution(confidenceMap,nodeData);
                	
                	// get avg information the cluster
                	sumconfidenceClusterOld = sumconfidenceClusterOld + Math.sqrt(1 - merge.getMean())*(1 -merge.getMargin());
                    
        		}
        		
        		
        	}
        	 // sum before partitioning 
        	
        	sumconfidenceClusterOld = sumconfidenceClusterOld +  confidenceBeforePartition;
           
        	// after the parttion of clsuter
        	// remove clsute to partiton from final clsuters list since it is breaked up intot two clusters.
        	finalClusters.remove(clustertoPartition)  ;
        	
        	
        	
        	// we will add two new clusters in the final clsuter list
        	
        	
        	// create new clsuters from the cluster
        	
        	Vector<Integer> clusterNewOne = new Vector<Integer>();
        	Vector<Integer> clusterNewTwo = new Vector<Integer>();
        	
        	 // remove the other node from neigbor list
        	clusterNewOne.add(nodestoCut.get(0));
        	List<Integer> listnei = mapNeigh.get(nodestoCut.get(0));
        	listnei.remove(nodestoCut.get(1));
        	
        	for(Integer neighbor:listnei){
        		// since neighbors of neighbors are also part of this cluster
        		addNeighbor(mapNeigh, clusterNewOne,neighbor,nodestoCut.get(0));
        		
        	}
        	
        	clusterNewTwo.add(nodestoCut.get(1));
        	List<Integer> listnei2 = mapNeigh.get(nodestoCut.get(1));
        	listnei2.remove(nodestoCut.get(0));
        	for(Integer neighbor:listnei2){
        		// since neighbors of neighbors are also part of this cluster
        		addNeighbor(mapNeigh, clusterNewTwo,neighbor,nodestoCut.get(1));
        		
        	}
        	
        	// ///////////////////////////get the information 
        	Map<Integer,ConfidenceList > confidenceMap1 = new HashMap<Integer,ConfidenceList >();
        	// from that cluster vector , get avg confidence ,
        	int[] nodeData1 = new int[clusterNewOne.size()];
        	int i=0;
        	for(Integer crfNodeId:clusterNewOne ){
        		nodeData1[i] =  crfNodeId;
        		i++;
        		confidenceMap1.put(crfNodeId, constMap.get(crfNodeId)) ;
        	}
        	
        	MergeDistribution merge1 =  new MergeDistribution(confidenceMap1,nodeData1);
        	
        	// get avg information of the cluster
        	double cluster1Confidence = Math.sqrt(1 - merge1.getMean())*(1- merge1.getMargin());
        	sumconfidenceClusterNew = sumconfidenceClusterNew +  cluster1Confidence;
        	
        	/////////////////////////////////////////////// for cluster 2
        	Map<Integer,ConfidenceList > confidenceMap2 = new HashMap<Integer,ConfidenceList >();
        	// from that cluster vector , get avg confidence ,
        	int[] nodeData2 = new int[clusterNewTwo.size()];
        	 i=0;
        	for(Integer crfNodeId:clusterNewTwo ){
        		nodeData2[i] =  crfNodeId;
        		i++;
        		confidenceMap2.put(crfNodeId, constMap.get(crfNodeId)) ;
        	}
        	
        	MergeDistribution merge2 =  new MergeDistribution(confidenceMap2,nodeData2);
        	
        	// get avg information of the cluster
        	double cluster2Confidence = Math.sqrt(1 - merge2.getMean())*(1- merge2.getMargin());
        	sumconfidenceClusterNew = sumconfidenceClusterNew +  cluster2Confidence ;
        	
     //////////////////////////////////////////////////////////////////////
        	
        	Iterator<Vector<Integer>> iterator1 =  finalClusters.iterator()  ;
        	while(iterator1.hasNext())
        	{
        		Vector<Integer> vector  =	iterator1.next();
        		Map<Integer,ConfidenceList > confidenceMap3 = new HashMap<Integer,ConfidenceList >();
            	// from that cluster vector , get avg confidence ,
            	int[] nodeData3 = new int[vector.size()];
            	 i=0;
            	for(Integer crfNodeId:vector ){
            		nodeData3[i] =  crfNodeId;
            		i++;
            		confidenceMap3.put(crfNodeId, constMap.get(crfNodeId)) ;
            	}
            	
            	MergeDistribution merge3 =  new MergeDistribution(confidenceMap3,nodeData3);
            	
            	// get avg information the cluster
            	double otherClusterConfidence =  Math.sqrt(1 - merge3.getMean())*( 1-merge3.getMargin());
            	sumconfidenceClusterNew = sumconfidenceClusterNew + otherClusterConfidence;
                
        		
        	}

      //////////////////////////////////////////////////////
        	
        	// add two new clusters into the final clusters list.
        	finalClusters.add(clusterNewOne);
        	finalClusters.add(clusterNewTwo);
        	
        	int nClusters = finalClusters.size();
        	confidenceEffective = sumconfidenceClusterNew - ( 1- (item/sum(listWeights)) )*((cluster1Confidence + cluster2Confidence));
        	if( confidenceEffective < confidenceEffectivePrevious)
        	{
        		finalCluster = previousClusters;
        		break;
        	}
        	else
        	{ 
        		previousClusters = finalClusters;
        		confidenceEffectivePrevious = confidenceEffective;
        	}
        	
        	//finalClustersList.add(finalClusters);
        	
        //	confidenceEffective = sumconfidenceClusterNew - ( 1- (item/sum(listWeights)) )*confidenceBeforePartition;
       
        
        	// get neighbors of nodes which got cut
        	 count++;
        }
        
        if(finalCluster ==  null) {  finalCluster = superClusters ;}
        
        // modifies cluster so that  clusters with same confidence are mergred into one. 
        // if there is a connection from any node of the cluster 1 to cluster 2 , there can be merged togethre.
        
       // mergingSameCluster();
       // System.out.println("Prune Edge "+(count+1) +" Confidence : "+ confidenceEffective);
    
        finalCluster.size();
        int output[][] =  new int [ finalCluster.size()][];
       
        	Iterator<Vector<Integer>> iterator =  finalCluster.iterator()  ;
        	int i=0;
        	    while(iterator.hasNext())
        	    {
        	       Vector<Integer> clusterSingle =  iterator.next();
        	       output[i] = new int[clusterSingle.size()];
        		   for (int j = 0; j < clusterSingle.size(); j++)
        		   {
        			   output[i][j]  =   clusterSingle.get(j);
        			   
        		   }
        		     i++;
        	    }
        	
		return output;
    }
 
    public void mergingSameCluster()
    {
    	
    	 for (int source = 1; source <= numberofvertices; source++)
         {
             for (int destination = 1; destination <= numberofvertices; destination++)
             {
                 this.adjacencyMatrix[source][destination] = adjacencyMatrix[source][destination];
             }
         }
    	
    }
    
    
    
    public double sum(List<Double> listWeights)
    {   double sum =0;
    	for(Double i:listWeights){
    	 sum =  sum + i;	
    	}
		return  sum;
    	
    }
    public void addNeighbor(Map<Integer,List<Integer>> mapNeigh,Vector<Integer> clusterNewOne,Integer neighbor,Integer neighborprev)
    {
    	clusterNewOne.add(neighbor);
		List<Integer> neighList = mapNeigh.get(neighbor);
		if(neighList.size() == 1)
		{
			 return;
		}
		for(Integer neighbor1:neighList)
		{  if(neighbor1 == neighborprev )
			continue;
		  addNeighbor(mapNeigh,clusterNewOne,neighbor1,neighbor);
		}
		
    }
    
    
    public  static void clusterPrim(CrfMap mapC)
    {
    	double adjacency_matrix[][];
        int number_of_vertices;
     //   Scanner scan = new Scanner(System.in);
       // CrfMap mapC =  new CrfMap();
        mapC.populatingConstantMap(Integer.toString(1));
        number_of_vertices =  mapC.getNumberofCRFNodes();
        Map<Integer, ConfidenceList> mapFinal =     mapC.getConstantConfidenceMap();
        adjacency_matrix = new double[number_of_vertices + 1][number_of_vertices + 1];
            int mat[][] =new int[18][18];

    		mat[0][1] = 1; mat[1][0] =1;
    		mat[1][2] = 1; mat[2][1] =1;
    		mat[2][3] = 1; mat[3][2] =1;
    		mat[2][4] = 1; mat[4][2] =1;
    		mat[4][5] = 1; mat[5][4] =1;
    		mat[6][4] = 1; mat[4][6] =1;
    		mat[4][7] = 1; mat[7][4] =1;
    		mat[7][1] = 1; mat[1][7] =1;
    		
       		mat[8][1] = 1; mat[1][8] =1;
          		mat[9][8] = 1; mat[8][9] =1;
          		mat[10][3] = 1; mat[3][10] =1;
          		mat[11][10] = 1; mat[10][11] =1;
          		mat[4][11] = 1; mat[11][4] =1;
          		mat[12][8] = 1; mat[8][12] =1;
          		mat[12][13] = 1; mat[13][12] =1;
          		mat[13][14] = 1; mat[14][13] =1;
          		
          	   mat[15][7] = 1; mat[7][15] =1;
      		   mat[6][16] = 1; mat[16][6] =1;
   		   
      AllowDistance allowDistance = new AllowDistance(mat);
           
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
            prims.primsAlgorithm(adjacency_matrix,mapC);
            prims.printMST();
 
   } 
       
   public static void main(String... arg)
   {
	    CrfMap map = new CrfMap();
	    crfmap = map;
	   clusterPrim(crfmap);
   }
   
   
  /*  public static void main(String... arg)
    {
        double adjacency_matrix[][];
        int number_of_vertices;
        Scanner scan = new Scanner(System.in);
        CrfMap mapC =  new CrfMap();
        mapC.populatingConstantMap(Integer.toString(3));
        try
        {
            System.out.println("Enter the number of vertices");
            number_of_vertices = scan.nextInt();
            adjacency_matrix = new double[number_of_vertices + 1][number_of_vertices + 1];
            int mat[][] =new int[6][6];
 	        mat[1][2] = 1; mat[2][1] =1;
 			mat[1][4] = 1; mat[4][1] =1;
 			mat[4][3] = 1; mat[3][4] =1;
 			mat[2][3] = 1; mat[3][2] =1;
 			mat[2][5] = 1; mat[5][2] =1;
 			
 			
AllowDistance allowDistance = new AllowDistance(mat);
            
            System.out.println("Enter the Weighted Matrix for the graph");
            for (int i = 1; i <= number_of_vertices; i++)
            {
                for (int j = 1; j <= number_of_vertices; j++)
                {
                     adjacency_matrix[i][j] = scan.nextDouble();
                	 // adjacency_matrix[i][j] =   allowDistance.measure(x, y) ;
                    if (i == j)
                    {
                        adjacency_matrix[i][j] = 0;
                        continue;
                    }
                    
                    if (adjacency_matrix[i][j] == 0)
                    {
                        adjacency_matrix[i][j] = INFINITE;
                    }
                }
            }
 
            prim prims = new prim(number_of_vertices);
            prims.primsAlgorithm(adjacency_matrix,mapC);
            prims.printMST();
 
        } catch (InputMismatchException inputMismatch)
        {
            System.out.println("Wrong Input Format");
        }
        scan.close();
    }
    */
}