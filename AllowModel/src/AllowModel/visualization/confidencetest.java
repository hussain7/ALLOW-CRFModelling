package AllowModel.visualization;

public class confidencetest {

	public static void main(String[] args) {
		
		int n=900;
		 double testmean =  Math.exp((double)-n/300) ;
		double var =  Math.sqrt((1-testmean)*(testmean)/n);
		
		System.out.println("number"+n+"Confidence "+ confidence(testmean,var));
		
	}

	public static double confidence(double mean, double var)
	{
		double confidence = Math.sqrt(1-mean)*(1-var);
		return confidence;
	}
}
