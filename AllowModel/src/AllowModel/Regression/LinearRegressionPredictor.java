package AllowModel.Regression;



import Jama.Matrix;
import Jama.QRDecomposition;

public class LinearRegressionPredictor implements Runnable {
    private  int N;        // number of 
    private  int p;        // number of dependent variables
    private  Matrix beta;  // regression coefficients
    private double SSE;         // sum of squared
    private double SST;         // sum of squared

    private double[][] x;
    private  double[] y;
    public LinearRegressionPredictor(double[][] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("dimensions don't agree"); 
        this.x=x;
        this.y=y;
        
        N = y.length;
        p = x[0].length;
       // beta = new Matrix(0,0);
    }

    public double beta(int j) {
        return beta.get(j, 0);
    }

    public double R2() {
        return 1.0 - SSE/SST;
    }

 @Override
	public void run() {
		// TODO Auto-generated method stub
		   Matrix X = new Matrix(x);
  int r =X.rank();
        // create matrix from vector
        Matrix Y = new Matrix(y, N);
       int yr= Y.rank();
        // find least squares solution
        QRDecomposition qr = new QRDecomposition(X);
        
        beta = qr.solve(Y);
        
        // mean of y[] values
        double sum = 0.0;
        for (int i = 0; i < N; i++)
            sum += y[i];
        double mean = sum / N;

        // total variation to be accounted for
        for (int i = 0; i < N; i++) {
            double dev = y[i] - mean;
            SST += dev*dev;
        }

        // variation not accounted for
        Matrix residuals = X.times(beta).minus(Y);
        SSE = residuals.norm2() * residuals.norm2();
		
		
	}

    public static void main(String[] args) throws InterruptedException {
     
        double[][] x =   {{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0},
 {1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0}, 
{1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0}, 
{1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0}, 
{1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
 {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0},
 {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0}, 
{1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0},
 {1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, 
{1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0}} ;
   
 double[] y = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.9762413233154189, 0.9762413233154189, 0.0} ;
        LinearRegressionPredictor regression = new LinearRegressionPredictor(x, y);
        Thread t = new Thread(regression);
        t.start();
        t.join();
        System.out.println("beta1  beta2 \n"+
                      regression.beta(0)+" "+regression.beta(1)+" "+ regression.beta(2)+" "+ regression.R2());
    }

	
}
