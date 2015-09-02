package AllowModel.clustering;

import java.io.File;
import java.io.IOException;
import java.util.List;

import AllowModel.metrics.AllowDistance;
import net.sf.javaml.clustering.* ;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.NodeInstance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.distance.JaccardIndexSimilarity;
import net.sf.javaml.distance.MahalanobisDistance;
import net.sf.javaml.distance.NormDistance;
import net.sf.javaml.tools.InstanceTools;
import net.sf.javaml.tools.data.FileHandler;
import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.clustering.mcl.MCL;
public class Clustering {

	public static void main(String[] args) throws IOException
	{
		
		//double[] values = new double[] { 0.01, 0.02 };
		
	  Dataset dataset = new DefaultDataset();
	   
	   
	 int n=14511;
       double testmean = 0.00015832;	
	 double   dev =  Math.sqrt((1-testmean)*(testmean)/n);
	  
	    	double[] values = new double[] { testmean, dev};
	    	Instance instance = new NodeInstance(values, 0);
	        dataset.add(instance);
	        
	        n=14511;
	        testmean = 0.00015832;
	   dev =  Math.sqrt((1-testmean)*(testmean)/n);
	  	  
	        values = new double[] { testmean, dev};
	    	instance = new NodeInstance(values, 1);
	        dataset.add(instance);
	        
	        
	        n=11;
	 	   testmean = 0.969060;
	 	   dev =  Math.sqrt((1-testmean)*(testmean)/n);
	 	  	  
	 	        values = new double[] { testmean, dev};
	 	    	instance = new NodeInstance(values, 2);
	 	        dataset.add(instance);
	        
	   n=14;
	   testmean = 0.919060;
	   dev =  Math.sqrt((1-testmean)*(testmean)/n);     
	    
	   values = new double[] { testmean, dev};
	        instance = new NodeInstance(values, 3);
	        dataset.add(instance);
	        
	        n=14511;
	        testmean = 0.00015832;
	 	   dev =  Math.sqrt((1-testmean)*(testmean)/n);     
	 	    
	 	   values = new double[] { testmean, dev};
	 	        instance = new NodeInstance(values, 4);
	 	        dataset.add(instance);
	    
	 	       n=14;
		 	   testmean = 0.90900;
		 	   dev =  Math.sqrt((1-testmean)*(testmean)/n);     
		 	    
		 	   values = new double[] { testmean, dev};
		 	        instance = new NodeInstance(values, 5);
		 	        dataset.add(instance);
		 	       n=14;
		 	       
			 	   testmean = 0.90900;
			 	   dev =  Math.sqrt((1-testmean)*(testmean)/n);     
			 	    
			 	   values = new double[] { testmean, dev};
			 	        instance = new NodeInstance(values, 6);
			 	        dataset.add(instance);
			 	        
			 	       n=14591;
				        testmean = 0.00015832;
				 	   dev =  Math.sqrt((1-testmean)*(testmean)/n);     	 	    
				 	   values = new double[] { testmean, dev};
				 	        instance = new NodeInstance(values, 7);
				 	        dataset.add(instance);

	     /* Load a dataset */
     //   Dataset data = FileHandler.loadDataset(new File("E:\\iris.data.txt"), 4, ",");
				 	        int mat[][] =new int[8][8];
				 	        mat[0][1] = 1; mat[1][0] =1;
				 			mat[1][2] = 1; mat[2][1] =1;
				 			mat[2][3] = 1; mat[3][2] =1;
				 			mat[2][4] = 1; mat[4][2] =1;
				 			mat[4][5] = 1; mat[5][4] =1;
				 			mat[6][4] = 1; mat[4][6] =1;
				 			mat[4][7] = 1; mat[7][4] =1;
				 			
	   AllowDistance allowDistance = new AllowDistance(mat);
	 //  JaccardIndexSimilarity distance = new JaccardIndexSimilarity();
	   MCL mcl = new MCL(allowDistance);
	   
       // Clusterer km = new KMedoids(3,100,allowDistance); 
       // Clusterer km = new net.sf.javaml.clustering.KMedoids();
        //Clusterer cw = new Cobweb();
        /*
         * Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster
         */
      //  Dataset[] clusters = km.cluster(dataset);
        Dataset[] clustersMacl  = mcl.cluster(dataset);
    //    Dataset[] clustersCw = cw.cluster(dataset);
   
        ClusterEvaluation sse= new SumOfSquaredErrors();
        /* Measure the quality of the clustering */
     //   double score=sse.score(clustersCw);
      //  System.out.println("Cluster count: " + clustersCw.length);
        
        //  int clustersLength = clusters.length;
         // int datasetLength = clusters[0].size();
          
       /*   for(Dataset d:clusters){
        	  for(int i=0 ;i< datasetLength ; i++){
        		Instance ins =  d.get(i);
        		   System.out.println(" InstaceId: " + ((NodeInstance) ins).getInstanceId());
              }
        	  System.out.println("\n ");7
          }
          */
	}
	
}
