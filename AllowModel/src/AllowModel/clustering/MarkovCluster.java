package AllowModel.clustering;


import java.util.Vector;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.mcl.MarkovClustering;
import net.sf.javaml.clustering.mcl.SparseMatrix;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.AbstractSimilarity;
import net.sf.javaml.distance.DistanceMeasure;


public class MarkovCluster {

	  private DistanceMeasure dm;

	    // Maximum difference between row elements and row square sum (measure of
	    // idempotence)
	    private double maxResidual = 0.1;

	    // inflation exponent for Gamma operator
	    private double pGamma = 3.0;

	    // loopGain values for cycles
	    private double loopGain = 0.;

	    // maximum value considered zero for pruning operations
	    private double maxZero = 0.1;
	
	 public MarkovCluster(DistanceMeasure dm) {
	        this(dm, 0.001, 2.0, 0, 0.1);

	    }
	 
	 public MarkovCluster(DistanceMeasure dm, double maxResidual, double pGamma, double loopGain, double maxZero) {
	        if (!(dm instanceof AbstractSimilarity))
	            throw new RuntimeException("MCL requires the distance measure to be a Similarity measure");

	        this.dm = dm;
	        this.maxResidual = maxResidual;
	        this.pGamma = pGamma;
	        this.loopGain = loopGain;
	        this.maxZero = maxZero;
	    }
	  
	 public int[][] cluster(Dataset data) {
	        SparseMatrix dataSparseMatrix = new SparseMatrix();
	        for (int i = 0; i < data.size(); i++) {
	            for (int j = 0; j <= i; j++) {
	                Instance x = data.instance(i);
	                Instance y = data.instance(j);
	                double dist = dm.measure(x, y);
	                if (dist > maxZero)
	                    dataSparseMatrix.add(i, j, dist);
	            }
	        }

	        MarkovClustering mcl = new MarkovClustering();
	        SparseMatrix matrix = mcl.run(dataSparseMatrix, maxResidual, pGamma, loopGain, maxZero);
	        
			return null;
	   }

}
