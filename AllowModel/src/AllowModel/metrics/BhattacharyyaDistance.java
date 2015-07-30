package AllowModel.metrics;

import java.lang.Math;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.distribution.NormalDistribution;

public class BhattacharyyaDistance {

	double distance = new Double(0); 

	
	 public double calculateDistance(double meanOne,double meanTwo ,double marginOne,double marginTwo)
	 {
		 double t1 =0,t2=0,t3=0,t4=0,t5=0,t6;
		 
		 t1= Math.pow((marginOne/marginTwo), 2);
		 t2 = Math.pow((marginTwo/marginOne), 2) + 2.0 ;
		 t6 = (  Math.pow((marginOne/marginTwo), 2) +
				   Math.pow((marginTwo/marginOne), 2) + 2.0 );
		 t4 =(t1+t2)/4;
		t5=  Math.log(t4);
		 t3= 1/4*Math.log(
			         ( Math.pow((marginOne/marginTwo), 2) +
					   Math.pow((marginTwo/marginOne), 2) + 2.0 ) /4 );
		 
	
		 t4 = Math.pow( (meanOne - meanTwo ),2 )/(  Math.pow(marginOne,2)  + Math.pow(marginTwo,2) )/4;

		 distance = t3 +t4;
		//distance =   1/4*Math.log(
			//        1/4* (  Math.pow((marginOne/marginTwo), 2) +
				//	   Math.pow((marginTwo/marginOne), 2) + 2.0 )  )
                 //              +
     //(  1/4* Math.pow( (meanOne - meanTwo ),2 )/(  Math.pow(marginOne,2)  + Math.pow(marginTwo,2) )  );
		 
		 
		 
		/* NormalDistribution dist_one = new NormalDistribution( meanOne, marginOne);
		 NormalDistribution dist_two = new NormalDistribution(meanTwo, marginTwo);
		 
		 Gaussian gauss =  new Gaussian(meanOne,marginOne);
		 Gaussian gauss1 =  new Gaussian(meanTwo,marginTwo);
		 
		
		 SimpsonIntegrator simpson = new SimpsonIntegrator();
		 TrapezoidIntegrator trapezoid = new TrapezoidIntegrator();
		 
		 double j = trapezoid.integrate(10000, (UnivariateFunction) gauss, 0, 1); // area 
		 double i = trapezoid.integrate(10000, (UnivariateFunction) gauss1, 0, 1); // are
				 */
		// System.out.println("Trapezoid integral : " + j);
		// distance = j;
		 return distance;
		 
	 }

	double getBhattacharyyaDistance()
	{
		return distance;
		
	}
}
