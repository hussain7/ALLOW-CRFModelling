package AllowModel.model;

import net.sf.javaml.clustering.mcl.MarkovClustering;
import net.sf.javaml.clustering.mcl.SparseMatrix;

public class markovclustering {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SparseMatrix dataSparseMatrix = new SparseMatrix();
		
		dataSparseMatrix.add(1, 0, 1.29);
		dataSparseMatrix.add(0, 1, 1.29);
		dataSparseMatrix.add(4, 5, 2.2);
		dataSparseMatrix.add(5, 4, 2.2);
		
		// dataSparseMatrix.add(i, j, dis)
		
		MarkovClustering mcl = new MarkovClustering();
        SparseMatrix matrix = mcl.run(dataSparseMatrix, 0.1, 2.0, 0, 1);
        System.out.println("");
	}

}
