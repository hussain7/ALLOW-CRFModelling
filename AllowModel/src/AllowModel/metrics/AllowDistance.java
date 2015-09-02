package AllowModel.metrics;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.AbstractDistance;
import net.sf.javaml.distance.AbstractSimilarity;

public class AllowDistance extends AbstractSimilarity  {

	int[][] adjacencyMatrix  ; //
	BhattacharyyaDistance distance  ;
	
	public AllowDistance(int[][] adj){
		
		adjacencyMatrix  = adj ; //
		distance  = new BhattacharyyaDistance() ;
	}
	
	public double measure(Instance x, Instance y) {
		double meanX = x.value(0);
		double meanY = y.value(0);
		
		double marginX = x.value(1);
		double marginY = y.value(1);
		double dist = 9999;
		
		/*if((x.getID() == 0 && y.getID() == 1) || (x.getID() == 1 && y.getID() == 0)
			|| (x.getID() == 1 && y.getID() == 2) || (x.getID() == 2 && y.getID() == 1)
				|| (x.getID() == 1 && y.getID() == 3) || (x.getID() == 3 && y.getID() == 1)
				|| (x.getID() == 3 && y.getID() == 4) || (x.getID() == 4 && y.getID() == 3)
				|| (x.getID() == 0 && y.getID() == 5) || (x.getID() == 5 && y.getID() == 0)
				|| (x.getID() == 6 && y.getID() == 5) || (x.getID() == 5 && y.getID() == 6)
				|| (x.getID() == 6 && y.getID() == 7) || (x.getID() == 7 && y.getID() == 6)
				|| (x.getID() == 2 && y.getID() == 9) || (x.getID() == 9 && y.getID() == 2)
				|| (x.getID() == 0 && y.getID() == 8) || (x.getID() == 8 && y.getID() == 0)
				|| (x.getID() == 10 && y.getID() == 3) || (x.getID() == 3 && y.getID() == 10)
			)
		{
			  dist = distance.calculateDistance(meanX, meanY
			       		,marginX , marginY );
		}*/
	 		
		if(adjacencyMatrix[x.getID()][y.getID() ]== 1  ){
			
			 dist = distance.calculateDistance(meanX, meanY
			       		,marginX , marginY );
			// if(dist == 0) ;//dist = 0.1;
		}
		
		//	dist = 1/dist ;
		
		return dist;
	}

	public double measure(int i, int j,double meanX,double  meanY
       		,double marginX , double marginY) {
		
		double dist = 9999;
		if(adjacencyMatrix[i][j ]== 1  ){
			
			  
			 dist = distance.calculateDistance(meanX, meanY
			       		,marginX , marginY );
			
		}
		
		if(dist == 0) dist = 0.0000000001 ;
		
		return dist;
	}
}
