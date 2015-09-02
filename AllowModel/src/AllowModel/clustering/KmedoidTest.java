package AllowModel.clustering;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import AllowModel.crf.CrfMap;
import AllowModel.metrics.ConfidenceList;
import AllowModel.metrics.Distance;


public class KmedoidTest {

	static CrfMap map; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		     map  = new CrfMap();
		     confidenceList();
			 int numberOfClusters = 3 ;
			 int maxIterations =1000 ; 
			 Distance dm = new Distance() ;
			 
		    KMedoids clustering = new KMedoids(numberOfClusters, maxIterations,dm,map );
		    int noofCRFNodes = map.getNumberofCRFNodes();
		    
		    Set<Integer> CRFNodesIds  = map.getCRFNodesIds();
		  
		    int[][] output1  =  clustering.clusterCoWeb(toInt(CRFNodesIds));
		    int[][] output = clustering.cluster(toInt(CRFNodesIds));
	}

	public static int[] toInt(Set<Integer> set) {
		  int[] a = new int[set.size()];
		  int i = 0;
		  for (Integer val : set) a[i++] = val;
		  return a;
		}
	
	private static void confidenceList()
	{
		map  = new CrfMap();

		Map<Integer,ConfidenceList> mapCon = map.getConstantConfidenceMap();
	
		ConfidenceList list1 = new ConfidenceList(100,0.01,0.004);
		ConfidenceList list2 = new ConfidenceList(100,0.1,0.004);
		ConfidenceList list3 = new ConfidenceList(100,0.1,0.004);
		ConfidenceList list4 = new ConfidenceList(1000,0.91,0.9);
		mapCon.put(1, list1);
		mapCon.put(2, list2);
		mapCon.put(3, list3);
		mapCon.put(4, list4);
		
	}
	
	
}



