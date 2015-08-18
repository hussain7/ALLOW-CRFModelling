package AllowModel.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AllowModel.metrics.ScopeInformation;

public class RoutingTable {
	
	Map<String,List<ScopeInformation>> neighborScopes ;  // key is neighbor ID
	                 						// List of all scopes of specific neighbor
	
	public RoutingTable()
	{
		
		neighborScopes =  new HashMap<String,List<ScopeInformation>>();
		
	}

	public Map<String, List<ScopeInformation>> getNeighborScopes()
	{
		return neighborScopes;
	}
	
	public void addEntrytoTable(String neighborID,List<ScopeInformation> scopeInfos)
	{
		neighborScopes.put(neighborID, scopeInfos);
	}
	
	public boolean empty() {
		// TODO Auto-generated method stub
		return neighborScopes.isEmpty() ;
	}
	
	public void dumpTable(String allowNodeId)
	{
		StringBuffer s = new StringBuffer();
		
		s.append("\n ALLOW ID:"+ allowNodeId+"\n" );
		Map<String, List<ScopeInformation>>  scopesTable = getNeighborScopes() ;
		Set<String> scopeSet = scopesTable.keySet();
		
		for(String key:scopeSet)
		{   
            s.append(" NeiborId:"+key+"\n");
			List<ScopeInformation> scopeList = scopesTable.get(key);
			for(ScopeInformation scope:scopeList)
			{
				double mean = scope.getscopeMean();
				double margin = scope.getscopeMargin();
				double instance  = scope.getscopeInstances();
				double confidence = Math.sqrt(1-mean)*(1-margin);
				
				s.append(" ScopeId: "+scope.getscopeId()+ " Instances: "+scope.getscopeInstances()+
						" CRF Nodes in Scope "+scope.getNodeIdswithScope().toString()+ " Mean: "+scope.getscopeMean()
						+" Margin: "+scope.getscopeMargin()+" Confidence "+confidence+"\n");
			}
			
		}
		
	System.out.print(s);

	}
	
	
}
