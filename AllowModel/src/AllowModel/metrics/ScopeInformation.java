package AllowModel.metrics;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class ScopeInformation {

	int scopeId;
	double scopeMean;
	double scopeMargin;
	List<Integer> nodeIds; // nodeIs list in the scope
	int parentAllowNode; // node this scope is associated with 
	// zero in case of local node else from the 
	int noofInstances ;
	public ScopeInformation(double mean, double margin, List<Integer> nodeIds, int parentNode,int noofInstances)
	{
		this.scopeId =  (int)Math.random();
		this.nodeIds =  nodeIds;
		this.scopeMean = mean;
		this.scopeMargin = margin;
		this.parentAllowNode = parentNode;
		this.noofInstances = noofInstances;
	}

	public int getscopeId()
	{
		return scopeId;}
	
	public double getscopeMargin()
	{
		return scopeMargin;
		
	}
	
	public double getscopeMean()
	{
		
		return scopeMean;
	}
	
	public int getscopeInstances()
	{
		
		return noofInstances;
	}
	
	public List<Integer> getNodeIdswithScope()
	{
		
		return nodeIds;
		
	}
	
	public void dumpScopeInformation()
	{
		StringBuffer s = new StringBuffer();
			
			s.append(" Instance: "+noofInstances +" Mean: "+ scopeMean
					+" Margin: "+scopeMargin +" Nodes "+nodeIds.toString()+ "\n");
		
	System.out.print(s);

	}
	
	
}
