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
	   
	    //	Instance instance = new DenseInstance(values);
	    	//Instance instance = InstanceTools.randomInstance(2);
	  
	
	       /* 
	        n=14;
		 	   testmean = 0.90900;	
		   dev =  Math.sqrt((1-testmean)*(testmean)/n);
		  
		    	 values = new double[] { testmean, dev};
		    	instance = new NodeInstance(values, 6);
		        dataset.add(instance);


		        n=14;
			 	   testmean = 0.98900;
			   dev =  Math.sqrt((1-testmean)*(testmean)/n);
			  
			    	 values = new double[] { testmean, dev};
			    	instance = new NodeInstance(values, 7);
			        dataset.add(instance);
			        
			        
			  	  n=14011;
			       testmean = 0.00025832;	
				   dev =  Math.sqrt((1-testmean)*(testmean)/n);
				  
				    	 values = new double[] { testmean, dev};
				    	instance = new NodeInstance(values, 8);
				        dataset.add(instance);
				        
				        n=14;
					    testmean = 0.90900;	
					   dev =  Math.sqrt((1-testmean)*(testmean)/n);
					  
					    	 values = new double[] { testmean, dev};
					    	instance = new NodeInstance(values, 9);
					        dataset.add(instance);
					        
					        
					        n=12;
						 	   testmean = 0.98900;
							   dev =  Math.sqrt((1-testmean)*(testmean)/n);
							  
							    	 values = new double[] { testmean, dev};
							    	instance = new NodeInstance(values, 10);
							        dataset.add(instance);*/
	        
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
	        
	   n=11;
	   testmean = 0.969060;
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
			 	        
			 	       n=14;     
				 	   testmean = 0.90900;
				 	   dev =  Math.sqrt((1-testmean)*(testmean)/n);     	 	    
				 	   values = new double[] { testmean, dev};
				 	        instance = new NodeInstance(values, 7);
				 	        dataset.add(instance);
		    //	Instance instance = new DenseInstance(values);
		    	//Instance instance = InstanceTools.randomInstance(2);
	  /*   for(int i = 0; i < 4; i++) {
		    	double[] values = new double[] { 0.336697,Math.sqrt(0.000586) };
		    	Instance instance = new DenseInstance(values);
		        dataset.add(i,instance);
	     }
	     
	     for(int i = 4; i < 6; i++) {
		    	double[] values = new double[] {0.649580,Math.sqrt(0.001507) };
		    	Instance instance = new DenseInstance(values);
		       // dataset.add(instance);
		       dataset.add(i, instance);
	     }
	   
	     for(int i = 6; i < 10; i++) {
		    //	double[] values = new double[] {0.076208,Math.sqrt(0.000078) };
		    	double[] values = new double[] { 0.332121,Math.sqrt(0.001507) };
		    	Instance instance = new DenseInstance(values);
		        dataset.add(i,instance);
	     }
	    
	*/
	     /* Load a dataset */
     //   Dataset data = FileHandler.loadDataset(new File("E:\\iris.data.txt"), 4, ",");
	   AllowDistance allowDistance = new AllowDistance();
	 //  JaccardIndexSimilarity distance = new JaccardIndexSimilarity();
	   MCL mcl = new MCL(allowDistance);
	   
        Clusterer km = new KMedoids(3,100,allowDistance); 
       // Clusterer km = new net.sf.javaml.clustering.KMedoids();
        Clusterer cw = new Cobweb();
        /*
         * Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster
         */
        Dataset[] clusters = km.cluster(dataset);
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
