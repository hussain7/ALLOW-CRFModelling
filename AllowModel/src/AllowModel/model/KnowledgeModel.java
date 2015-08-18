package AllowModel.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import AllowModel.metrics.ConfidenceList;
import AllowModel.metrics.ScopeInformation;

public class KnowledgeModel {

	String allowNodeId ;
	List<ScopeInformation> scopes;
	
	public KnowledgeModel(String allowNodeId)
	{
		this.allowNodeId = allowNodeId;	
		scopes = new ArrayList<ScopeInformation>();
	}
	
	public void addScopeInformation(ScopeInformation info)
	{
		scopes.add(info);
	}
	
	public List<ScopeInformation> getScopesInformation()
	{ 
		return scopes;
		
	}
	
	
	public void dumpModel(String allowNodeId)
	{
		StringBuffer s = new StringBuffer();
		
		s.append("\n ALLOW ID:"+allowNodeId+"\n" );
		List<ScopeInformation> scopes = getScopesInformation() ;
		
		for(ScopeInformation scope:scopes)
		{
			double mean = scope.getscopeMean();
			double margin = scope.getscopeMargin();
			double instance  = scope.getscopeInstances();
			double confidence = Math.sqrt(1-mean)*(1-margin);
			
			s.append("ScopeId: "+scope.getscopeId()+ " Instances: "+scope.getscopeInstances()+
					"CRF Nodes in Scope "+scope.getNodeIdswithScope().toString()+ " Mean: "+scope.getscopeMean()
					+" Margin: "+scope.getscopeMargin()+" Confidence:"+confidence+"\n");
		}
		
	System.out.print(s);

	}
	
}
