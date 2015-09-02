package AllowModel.metrics;

public class NodeIdConfidence {
	
	String allowNodeId;
	double confidenceValue ;
	Integer key ; // crf nodeId
	
	public NodeIdConfidence(String nodeId, double value,Integer key)
	{
		allowNodeId = nodeId;
		confidenceValue = value;
	}

	public double getConfidenceValue()
	{
		return confidenceValue;
	}
	
	public String getallowNodeId()
	{
		return allowNodeId;
	}
	
	public Integer getcrfnodeId()
	{
		return key;
	}
}
