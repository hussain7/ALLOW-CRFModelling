package protocol;

import java.util.*;

public class Query {
String from; // source of ALLOW node ID
public	double queryMean;
public	double queryMargin;
List<String> allowNodeIdsVisited = new ArrayList<String>();
int hopCount;
public List<Integer> queryScopeCRFNodes;	

public Query(String allowNodeId,double queryMean,double margin,List<Integer> list){
		
		from = allowNodeId;
		this.queryMean = queryMean;
		this.queryMargin =  queryMargin;	
		hopCount =0;
		queryScopeCRFNodes =  list;
		
	}
	
public List<Integer> getQueryScopeCRFNodes()
  {
   return 	queryScopeCRFNodes;
  }

}