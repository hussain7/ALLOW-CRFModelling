package AllowModel.protocol;

import java.util.*;

public class Query {
String from; // source of ALLOW node ID
public	double queryMean;
public	double queryMargin;
public List<String> allowNodeIdsVisited = new ArrayList<String>();
public int hopCount;
public List<Integer> queryScopeCRFNodes;	

private double confidence ;
public Query(String allowNodeId,double queryMean,double margin,List<Integer> list){
		
		from = allowNodeId;
		this.queryMean = queryMean;
		this.queryMargin =  margin;	
		hopCount =0;
		queryScopeCRFNodes =  list;
		
	}
	
public List<Integer> getQueryScopeCRFNodes()
  {
   return 	queryScopeCRFNodes;
  }

public double getConfidence()
 {
	 confidence = Math.sqrt(1-queryMean)*(1-queryMargin);
	return confidence;
 }

public String getQuerySourceAllowId()
{
 return 	from;
}
}