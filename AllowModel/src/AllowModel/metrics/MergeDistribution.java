package AllowModel.metrics;
import java.awt.List;
import java.lang.Math;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import AllowModel.crf.CrfMap;

public class MergeDistribution {
	
	double mean;
	double margin;
	int noOfInstances;    // taking avg as a merged value 
	
	MergeDistribution(double meanOne,double meanTwo ,double marginOne,double marginTwo,
			int instancesOne ,int instancesTwo)
	{
		mean = (instancesOne*meanOne + instancesTwo*meanTwo)/(instancesOne + instancesTwo );
		margin = ( Math.pow(instancesOne, 2)*marginOne  + Math.pow(instancesTwo, 2)*marginTwo )/
				  Math.pow(instancesOne+ instancesTwo,2);
		noOfInstances = (instancesTwo + instancesOne)/2;
	}
/*
	mean = (instancesOne*meanOne + instancesTwo*meanTwo)/(instancesOne + instancesTwo );
	margin = ( Math.pow(instancesOne, 2)*marginOne  + Math.pow(instancesTwo, 2)*marginTwo )/
			  Math.pow(instancesOne+ instancesTwo,2);*/
	
	//variance or margin
	 // pA *sigmaA^2 + pB*sigmaB^2  +  [pA*meanA^2 + pB*meanB^2 − (pA*meanA + pB *meanB )^2]
	 // where pA = n1/(n1+n2)  and pB  = n2/(n1+n2)
	
	// likewise mean = pA*mean1 + pB*mean2 
	
	//σ^2=(σ21+μ21)n1+(σ22+μ22)n2(n1+n2)−μ^2 

	public MergeDistribution( CrfMap map, int[] nodeData)  // nodeId data 
	{
		//removeDuplicates(nodeData);
		double sum_mean =0;
		double sum_instances =0;
		double margin_sum =0;
		double product =1;
		Map<Integer,ConfidenceList > confidenceMap =  map.getConstantConfidenceMap();
		 int len = length(nodeData);
		 
		for(int i=0; i < len; i++){
			if(nodeData[i] == 0) continue; // nodeid can not be zero so skip it .
	    	ConfidenceList nodeIdList =  confidenceMap.get(nodeData[i]);
	    	int instances = nodeIdList.getInstance();
	    	double margin =	nodeIdList.getMargin();
	    	double mean  = nodeIdList.getMean();
	    	product =  mean*instances;
	    	sum_mean = sum_mean + product;
	    	sum_instances = sum_instances + instances;
	    	margin_sum = ( Math.pow(margin, 2) + Math.pow(mean, 2))*instances  + margin_sum;   
	   
		}
		
		mean = sum_mean/sum_instances;
		double marginSqure = margin_sum/sum_instances - Math.pow(mean,2) ;
		margin = Math.sqrt(marginSqure);
		noOfInstances = (int) (sum_instances/len);
	}
	
	
	private int length(int[] nodeData)
	{
		 int count =0;
		 int i=0;
		 
		while( i < nodeData.length && count < nodeData.length)
			{ if(nodeData[i] != 0 )
			   {  count++; }
			     i++;
			}
		 
		return count;
		
	}
	
/*	public void removeDuplicates(int []arr)
	{
		int end = arr.length;
		Set<Integer> set = new HashSet<Integer>();

		for(int i = 0; i < end; i++){
		  set.add(arr[i]);
		}
		int  i=0;
		Iterator it = set.iterator();
		while(it.hasNext()) {
		  arr[i] = (Integer) it.next();
		  i++;
		}
		
		
		
	}
	*/

	
	public double getMean()
	{
		return mean;
	
	}
	
	
	public double getMargin()
	{
		return margin;
	}
	
	public int getNoofInstances()
	{
		return noOfInstances;
	}
}
