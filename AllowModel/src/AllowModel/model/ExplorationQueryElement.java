package AllowModel.model;

import AllowModel.protocol.ExplorationQuery;


public class ExplorationQueryElement{
	
	public ExplorationQueryElement(ExplorationQuery query,double confidence)
	{
		exQuery = query;
		queryConfidence = confidence;
	}
	public ExplorationQuery exQuery;
	public double queryConfidence;

}