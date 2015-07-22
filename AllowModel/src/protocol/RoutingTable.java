package protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingTable {
	
	Map<String,List<ScopeInformation>> neighborScopes ;  // key is neighbor ID
	                                						// List of all scopes of specific neighbor
	public RoutingTable()
	{
		neighborScopes =  new HashMap<String,List<ScopeInformation>>();
		
	}

	Map<String, List<ScopeInformation>> getNeighborScopes()
	{
		return neighborScopes;
	}
	
	public void addEntrytoTable(String neighborID,List<ScopeInformation> scopeInfos)
	{
		neighborScopes.put(neighborID, scopeInfos);
	}

	public void mergeScopes()
	{
		 // calculation of summarised information on nodes from various scopes of neighboring nodes are done
	}
	
	public void clustering()
	{
		
	}
	
	public void mergeConfidenceonCRFNode(int CRFNodeId)
	{
		
	}
	
}
