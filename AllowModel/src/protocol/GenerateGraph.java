package protocol;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GenerateGraph {

	int n=500000;
	List<AllowNode> graph = new ArrayList<AllowNode>();

	public GenerateGraph(){

		File file = new File("E:\\model.txt") ;
		
		try {
			graph.add(new AllowNode("0",null,file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for(int i=1; i<=n; i++){
		 
			try {
				graph.add(new AllowNode("i", GenerateNeighbors(),file));
				
				System.out.println("AllowNode" +i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}//end constructor


	public List<AllowNode> GenerateNeighbors(){
		List<AllowNode> nodes = new ArrayList<AllowNode>();
		Random rand = new Random();
		//max number of connections is ten minimum is one
		if(graph.size() > 10){
			int numNeighbors = 1+ rand.nextInt(9);
			for(int i =0; i< numNeighbors; i++){
				int neighbor=  rand.nextInt(graph.size());
				nodes.add(graph.get(neighbor));
			}
		}
		else{
			int numNeighbors = rand.nextInt(graph.size());
			for(int i =0; i< numNeighbors; i++){
				int neighbor=  rand.nextInt(graph.size());
				nodes.add(graph.get(neighbor));
			}
		}
		return nodes;
	}

}