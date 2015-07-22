package protocol;

public class ConfidenceList {

	int instances;
	double mean;
	double margin ;
	
	ConfidenceList(int instances,double mean,double margin)
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
	
	int getInstance()
	{
		return instances ;
	}
	
	double getMean()
	{
		return mean;
	}
	double getMargin()
	{
		return margin;
	}
	
}
