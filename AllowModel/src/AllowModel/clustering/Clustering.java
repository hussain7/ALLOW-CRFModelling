package AllowModel.clustering;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.clustering.* ;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.tools.InstanceTools;
import net.sf.javaml.tools.data.FileHandler;
import net.sf.javaml.clustering.KMedoids;
public class Clustering {

	public static void main(String[] args) throws IOException
	{
		
		//double[] values = new double[] { 0.01, 0.02 };
		
	  Dataset dataset = new DefaultDataset();
	    
	    for(int i = 0; i < 15; i++) {
	    //	Instance instance = new DenseInstance(values);
	    	Instance instance = InstanceTools.randomInstance(2);
	       
	        dataset.add(instance);
	    }
	    
	     /* Load a dataset */
     //   Dataset data = FileHandler.loadDataset(new File("E:\\iris.data.txt"), 4, ",");
	     
        
         
       // Clusterer km = new KMedoids(4,100,new Distance()); 
        Clusterer km = new net.sf.javaml.clustering.KMedoids();
        /*
         * Cluster the data, it will be returned as an array of data sets, with
         * each dataset representing a cluster
         */
        Dataset[] clusters = km.cluster(dataset);
        System.out.println("Cluster count: " + clusters.length);

	}
	
}
