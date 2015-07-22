package protocol;

import java.util.ArrayList;
import java.util.List;

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
	
	
	
}
