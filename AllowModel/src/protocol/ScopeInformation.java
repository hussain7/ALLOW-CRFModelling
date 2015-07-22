package protocol;

import java.util.List;
import java.util.Random;

public class ScopeInformation {

	int scopeId;
	double scopeMean;
	double scopeMargin;
	List<Integer> nodeIds; // nodeIs list in the scope
	int parentAllowNode; // node this scope is associated with 
	// zero in case of local node else from the 
	int noofInstances ;
	ScopeInformation(double mean, double margin, List<Integer> nodeIds, int parentNode,int noofInstances)
	{
		this.scopeId =  (int)Math.random();
		this.nodeIds =  nodeIds;
		this.scopeMean = mean;
		this.scopeMargin = margin;
		this.parentAllowNode = parentNode;
		this.noofInstances = noofInstances;
	}

	int getscopeId()
	{
		return scopeId;}
	
	double getscopeMargin()
	{
		return scopeMargin;
		
	}
	
	double getscopeMean()
	{
		
		return scopeMean;
	}
	
	int getscopeInstances()
	{
		
		return noofInstances;
	}
	
	List<Integer> getNodeIdswithScope()
	{
		
		return nodeIds;
		
	}
	
	
	
}
