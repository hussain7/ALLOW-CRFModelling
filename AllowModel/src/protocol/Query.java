package protocol;

import java.util.*;

public class Query {
	String from;
	Files file;
	List<String> nodeIdsVisited = new ArrayList<String>();
	int hopCount;
	
	public Query(String nodeId, Files fileName){
		from = nodeId;
		file = fileName;
		hopCount =0;
	}
	
}