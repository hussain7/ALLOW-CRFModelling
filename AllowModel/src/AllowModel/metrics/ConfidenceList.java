package AllowModel.metrics;

public class ConfidenceList {

	int instances;
	double mean;
	double margin ;
	
	public ConfidenceList(int instances,double mean,double margin)
	{
		this.instances = instances;
		this.mean = mean;
		this.margin = margin;
	}
	
	void setInstance(int instance)
	{
		this.instances = instances;
	}
	void setMean( double mean )
	{
		this.mean = mean ;
	}
	
	void setMargin(double margin)
	{
		this.margin = margin;
	}
	
	public int getInstance()
	{
		return instances ;
	}
	
	public double getMean()
	{
		return mean;
	}
	public double getMargin()
	{
		return margin;
	}
	
}
