package AllowModel.protocol;

import java.util.*;

public class ExplorationQuery {
public String from; // source of ALLOW node ID
public	double maxConfidence;
//public List<String> allowNodeIdsVisited = new ArrayList<String>();
public int progapationHopCount;
public int currentHopCount;
public int selectivity;
public List<Integer> queryScopeCRFNodes;	

public ExplorationQuery(String allowNodeId,int selectivity ,int progapationHopCount,List<Integer> queryScopeCRFNodes
){
		
		from = allowNodeId;
		this.maxConfidence = maxConfidence;
		this.progapationHopCount =  progapationHopCount;	
		currentHopCount =0;
		this.queryScopeCRFNodes = queryScopeCRFNodes ;	
     this.selectivity =selectivity;
}
	
public List<Integer> getQueryScopeCRFNodes()
  {
   return 	queryScopeCRFNodes;
  }

public int getprogapationHopCount()
{
 return 	progapationHopCount;
}

public String getQuerySourceAllowId()
{
 return 	from;
}

}