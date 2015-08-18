/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2012, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package AllowModel.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import AllowModel.crf.CrfMap;
import AllowModel.metrics.ConfidenceList;
import AllowModel.metrics.Distance;
import AllowModel.metrics.MergeDistribution;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.tools.DatasetTools;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.Cobweb;

/**
 * Implementation of the K-medoids algorithm. K-medoids is a clustering
 * algorithm that is very much like k-means. The main difference between the two
 * algorithms is the cluster center they use. K-means uses the average of all
 * instances in a cluster, while k-medoids uses the instance that is the closest
 * to the mean, i.e. the most 'central' point of the cluster.
 * 
 * Using an actual point of the data set to cluster makes the k-medoids
 * algorithm more robust to outliers than the k-means algorithm.
 * 
 * 
 * @author Thomas Abeel
 * 
 */
public class KMedoids {
	/* Distance measure to measure the distance between instances */
	private Distance dm;

	/* Number of clusters to generate */
	private int numberOfClusters;

	/* Random generator for selection of candidate medoids */
	private Random rg;

	/* The maximum number of iterations the algorithm is allowed to run. */
	private int maxIterations;
	private CrfMap map;

	/**
	 * default constructor
	 */
	public KMedoids() {
		this(4, 100, new Distance(),new CrfMap());
	}

	/**
	 * Creates a new instance of the k-medoids algorithm with the specified
	 * parameters.
	 * 
	 * @param numberOfClusters
	 *            the number of clusters to generate
	 * @param maxIterations
	 *            the maximum number of iteration the algorithm is allowed to
	 *            run
	 * @param DistanceMeasure
	 *            dm the distance metric to use for measuring the distance
	 *            between instances
	 * 
	 */
	public KMedoids(int numberOfClusters, int maxIterations, Distance dm,CrfMap map) {
		super();
		this.numberOfClusters = numberOfClusters;
		this.maxIterations = maxIterations;
		this.dm = dm;
		rg = new Random(System.currentTimeMillis());
		this.map =map;
		
	}

	 public int randomCRFNodeId(int min, int max)
	 {
		 
		return ( min + (int)(Math.random() * ((max - min) + 1)));
		
	 }
	 
	public int[][] clusterCoWeb(int[] data)
	{
		Map<Integer,Integer > keyMap = new HashMap<Integer,Integer >();
		
		Dataset dataset = new DefaultDataset();
		// double[] values = new double[] { 0.336697,Math.sqrt(0.000586) };
		 Map<Integer, ConfidenceList> cMap = map.getConstantConfidenceMap();
		 int index =0;
		 for(Integer key:cMap.keySet())
		 {  
			 keyMap.put(index, key) ;
			 ConfidenceList clist =  cMap.get(key);
		     double[] values = new double[] { clist.getMean(),clist.getMargin() };
	    	 Instance instance = new DenseInstance(values);
	         dataset.add(index++,instance);
	         
		 }
		  
		 Clusterer cw = new Cobweb();
	     Dataset[] clustersCw = cw.cluster(dataset);
	     int totalClusters = clustersCw.length;
	     int[][] output   = new int[][]{{1,2},{3,4}};
	    /* int i=0;
	     for(Dataset dataIns:clustersCw)
	     {
	    	 int j=0;
	    	 output[i] = new int[dataIns.size()];
	    	 while(j < dataIns.size()){
	    		 
	    	 Instance inst = dataIns.get(j);
	    	 int indexInMap = inst.getID();
	    	 int keyofCrfNodes = keyMap.get(indexInMap);
	    	 output[i][j] = keyofCrfNodes;
	    	 j++;
	    	 }
	    	 i++;
	     }
	     */
		return output;
		
	}
	 
	public int[][] cluster(int[] data) {
		
		int[] medoids = new int[numberOfClusters];
		Arrays.sort(data);
	    int min = data[0];
	   // System.out.println(min);
	    int max= data[data.length-1];
	  //  System.out.println(max);
	    
	    // create an array for each output cluster
		
	    int[][] output = new int[numberOfClusters][];
	    for (int i = 0; i < numberOfClusters ; i++) {
	    	output[i] = new int[data.length];
	    }
	    
		
		// default assigment of clusters
		for (int i = 0; i < numberOfClusters; i++) {
			
			// randomly assign medoids 
			int random = randomCRFNodeId(min,max);
			medoids[i] = data[random-1];
		}

		// iterate
		boolean changed = true;
		int count = 0;
		while (changed && count < maxIterations) {
			changed = false;
			count++;
			int[] assignment = assign(medoids, data);
			changed = recalculateMedoids(assignment, medoids, output, data);

		}
		
		// removing duplicates 
		for (int i = 0; i < numberOfClusters ; i++) {
			removeDuplicates(output[i] );
	    }
		
		int[][]  result = removeDuplicateScopes(output);
		
		    return result;
	}

    public int[][] removeNullScopes(int[][] testArr)
    {
         List<int[]> arr = new ArrayList<int[]>();
    	 
  	    for (int i = 0; i < numberOfClusters ; i++) {
  	    	if(testArr[i][0] == 0)
  	    	 continue;
  	    	arr.add(testArr[i]) ;
  	    }
    	
		return testArr;
    	
    }
	
	public  int[][] removeDuplicateScopes(int[][] testArr)
    {
        /**
         * This method will remove the duplicate ROW from 2-D array and replace with {0,0,0}
         * However i assume array rows  =5 and coloums =3 ;) You can have the solution for that I assume
         */
		int length  =  testArr[0].length;
        HashSet<String> hashSet = new HashSet<String>();
        int[][] result = new int[numberOfClusters][length];
        int i = 0;
        for(int[] a : testArr)
        {
            //System.out.println(Arrays.toString(a));
            //System.out.println(hashSet.contains(a));
            if(!hashSet.contains(Arrays.toString(a))){
                hashSet.add(Arrays.toString(a));
                result[i] = a;
            }
            i++;
        }

        System.out.println("old array : "+Arrays.deepToString(testArr));
        System.out.println("new array : "+Arrays.deepToString(result));

        return result;
    }
	
	
	public void removeDuplicates(int []y) {
	    Set<Integer> foundNumbers = new HashSet<Integer>();

	    for(int index = 0; index < y.length; index++) {
	        if(foundNumbers.contains(y[index])) {
	            y[index] = 0;
	        } else {
	            foundNumbers.add(y[index]);
	        }
	    }
	}
	
	/**
	 * Assign all instances from the data set to the medoids.
	 * 
	 * @param medoids candidate medoids
	 * @param data the data to assign to the medoids
	 * @return best cluster indices for each instance in the data set
	 */
	
	private int[] assign(int[] medoids, int[] data) {
		int[] out = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			double bestDistance = dm.measure(data[i], medoids[0],map);
			int bestMedoid = medoids[0];
			for (int j = 1; j < medoids.length; j++) {
				double tmpDistance = dm.measure(data[i], medoids[j],map);
				if (tmpDistance > bestDistance) {
					bestDistance = tmpDistance;
					bestMedoid = medoids[j];
				}
			}
			out[i] = bestMedoid;

		}
		return out;

	}

	
	
	/**
	 * Return a array with on each position the clusterIndex to which the
	 * Instance on that position in the dataset belongs.
	 * 
	 * @param medoids
	 *            the current set of cluster medoids, will be modified to fit
	 *            the new assignment
	 * @param assigment
	 *            the new assignment of all instances to the different medoids
	 * @param output
	 *            the cluster output, this will be modified at the end of the
	 *            method
	 * @return the
	 */
	private boolean recalculateMedoids(int[] assignment, int[] medoids,
			int[][] output, int[] data) {
		boolean changed = false;
		
		for (int i = 0; i < numberOfClusters; i++) {
			int t=0;
			for (int j = 0; j < assignment.length; j++) {
				if (assignment[j] == medoids[i]) {
					
					output[i][t] = data[j];
					t++;
				}
			}
			if (output[i][0] == 0) { // new random, empty medoid // check if it has initially zero 
				//which means all zeros thereafter 
				int min = data[0];
			    int max= data[data.length-1];
			    
				int random = randomCRFNodeId(min,max);
				medoids[i] = data[random-1];
				changed = true;
			} else {
				
				
				int oldMedoid = medoids[i];
				MergeDistribution avg = new MergeDistribution( map, output[i]) ;
				medoids[i] = checkNearestNode(avg.getMean(),avg.getMargin(),output[i]);
				
				//medoids[i] = data.kNearest(1,centroid, dm).iterator().next();
				if (medoids[i] != oldMedoid)
					changed = true;
			}
		}
		return changed;
	}

	public Dataset[] cluster(Dataset data) {
		// TODO Auto-generated method stub
		return null;
	}

	 public int checkNearestNode(double mean , double margin ,int[] data )
	{
		int centroid = 0;
		int nearestNodeId = data[0];;
		double bestDistance = dm.measure(data[0],mean,margin,map);
		for (int i = 1; i < data.length; i++) {
			if(data[i] == 0) continue;
			double currentDistance = dm.measure(data[i],mean,margin,map);
			if(currentDistance < bestDistance )
			{
				bestDistance =currentDistance;
				nearestNodeId = data[i];
			}
			
		}

		return nearestNodeId;
		
	}
	 
}
