package AllowModel.metrics;
import java.awt.List;
import java.lang.Math;
import java.util.Map;

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
	

	public MergeDistribution( CrfMap map, int[] nodeData)  // nodeId data 
	{
		
		double sum_mean =0;
		double sum_instances =0;
		double margin_sum =0;
		double product =1;
		Map<Integer,ConfidenceList > confidenceMap =  map.getConstantConfidenceMap();
		for(int i=0; i < nodeData.length; i++){
			if(nodeData[i] == 0) continue; // nodeid can not be zero so skip it .
	    	ConfidenceList nodeIdList =  confidenceMap.get(nodeData[i]);
	    	int instances = nodeIdList.getInstance();
	    	double margin =	nodeIdList.getMargin();
	    	product =  nodeIdList.getMean()*nodeIdList.getInstance();
	    	sum_mean =sum_mean + product;
	    	sum_instances = sum_instances + nodeIdList.getInstance();
	    	margin_sum = Math.pow(instances, 2)*margin + margin_sum;   
	    }
		
		mean = sum_mean/sum_instances;
		margin = margin_sum/Math.pow(sum_instances, 2);
		noOfInstances = (int) (sum_instances/nodeData.length);
	}
	
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
