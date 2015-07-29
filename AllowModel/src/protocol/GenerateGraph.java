package protocol;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class GenerateGraph {

	int n=5;
	public List<AllowNode> graph = new ArrayList<AllowNode>();
	UndirectedGraph<String, DefaultEdge> graphVisual =
            new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

	public GenerateGraph(){

		File file = new File("E:\\model.txt") ;
		
		try {  // make two ajdacent nodes 
			graph.add(new AllowNode(Integer.toString(1),null,file));
			
			graph.add(new AllowNode(Integer.toString(2),null,file));
			graph.get(0).addNeighbor(graph.get(1));
			graph.get(1).addNeighbor(graph.get(0));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i=3; i<=n; i++){
		 
			try {
				graph.add(new AllowNode(Integer.toString(i), GenerateNeighbors(),file));
				
			//	System.out.println("AllowNode" +i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		printGraph();
		 
	}//end constructor


	public List<AllowNode> GenerateNeighbors(){
		List<AllowNode> nodes = new ArrayList<AllowNode>();
		Random rand = new Random();
		//max number of connections is ten minimum is one
		int  test = graph.size() ;
		if(graph.size() > 10){
			int numNeighbors = 1+ rand.nextInt(9);
			for(int i =0; i< numNeighbors; i++){
				int neighbor=  rand.nextInt(graph.size());
				nodes.add(graph.get(neighbor));
			}
		}
		else{
			int numNeighbors = 1 + rand.nextInt( graph.size());
			for(int i =0; i< numNeighbors; i++){
				int neighbor =  rand.nextInt(graph.size());
				
				if(nodes.contains(graph.get(neighbor)) == false)
				nodes.add(graph.get(neighbor));
				
			}
		}
		return nodes;
	}
	
	public void printGraph()
	{
		for(AllowNode a:graph){
		System.out.println("\nALLOW Node:\n" + a.getAllowNodeId() );
		List<AllowNode> list = a.getNeighbors();
		System.out.println("Node Neighbors:");
		for(AllowNode n:list)
		   {
			System.out.print( n.getAllowNodeId() + " " );
	       }
		}
		
	}

}