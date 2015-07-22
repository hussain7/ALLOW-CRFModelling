package protocol;

import java.util.*;

public class GenerateGraph {

	int n=5000;
	List<AllowNode> graph = new ArrayList<AllowNode>();

	public GenerateGraph(){


		//graph.add(new AllowNode(null));

		for(int i=1; i<=5000; i++){
		//	graph.add(new AllowNode(i, GenerateNeighbors()));
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