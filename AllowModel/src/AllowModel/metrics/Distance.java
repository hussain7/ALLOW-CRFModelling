/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2012, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package AllowModel.metrics;


import java.util.Map;

import AllowModel.crf.CrfMap;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.AbstractDistance;


public class Distance extends AbstractDistance {

    /**
     * 
     */
	BhattacharyyaDistance distance  = new BhattacharyyaDistance() ;
    private static final long serialVersionUID = -5844297515283628612L;

    public double measure(int nodeId1, int nodeId2,CrfMap map) {  // crf nodes
        //XXX optimize
    	Map<Integer,ConfidenceList> confidenceMap =  map.getConstantConfidenceMap();
    	ConfidenceList nodeIdList1 =  confidenceMap.get(nodeId1);
    	ConfidenceList nodeIdList2 =  confidenceMap.get(nodeId2);
    	
    	
        double dist = 0;
        
        distance.calculateDistance(nodeIdList1.getMean(), nodeIdList2.getMean()
        		, nodeIdList1.getMargin(), nodeIdList2.getMargin());
        dist = distance.getBhattacharyyaDistance() ;
        
        return dist;
    }

	public double measure(Instance x, Instance y) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double measure(int nodeId,double mean, double margin,CrfMap map ) {
		// TODO Auto-generated method stub
		if(nodeId == 0) return Integer.MAX_VALUE;
		Map<Integer,ConfidenceList> confidenceMap =  map.getConstantConfidenceMap();
    	ConfidenceList nodeIdList =  confidenceMap.get(nodeId);
    	  double dist = 0;
    		// System.out.println("NodeId : \n " + nodeId);
    	  dist = distance.calculateDistance(nodeIdList.getMean(), mean
          		, nodeIdList.getMargin(), margin);
        
		return dist;
	}


}
