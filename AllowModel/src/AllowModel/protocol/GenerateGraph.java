package AllowModel.protocol;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class GenerateGraph {
    int nodeCount = 0;
	int n=20;
	public List<AllowNode> graph = new ArrayList<AllowNode>();
	UndirectedGraph<String, DefaultEdge> graphVisual =
            new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

	public GenerateGraph(){

		File file = new File("E:\\model.txt") ;
		
		for(int i=0; i< n; i++){
			 
				try {
					graph.add(new AllowNode(Integer.toString(i),file));
				//	System.out.println("AllowNode" +i);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
		
		for(int i=1; i< n; i++){
		  int parent  = nodeCount/2;
			graph.get(i).addNeighbor(graph.get(parent));
			graph.get(parent).addNeighbor(graph.get(i));
			nodeCount++;
//	System.out.println("AllowNode" +i);
		}
/*
		        graph.get(0).addNeighbor(graph.get(2));
		        graph.get(2).addNeighbor(graph.get(0));
		       
				graph.get(2).addNeighbor(graph.get(5));
				graph.get(5).addNeighbor(graph.get(2));
				
				graph.get(2).addNeighbor(graph.get(6));
				graph.get(6).addNeighbor(graph.get(2));
				
				graph.get(1).addNeighbor(graph.get(3));
				graph.get(3).addNeighbor(graph.get(1));
				
				graph.get(1).addNeighbor(graph.get(4));
				graph.get(4).addNeighbor(graph.get(1));
			*/

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
		System.out.println(" ALLOW Node: " + a.getAllowNodeId() );
		List<AllowNode> list = a.getNeighbors();
		System.out.print(" Node Neighbors:");
		for(AllowNode n:list)
		   {
			System.out.print( n.getAllowNodeId() + " " );
	       }
		System.out.print(" \n");
		}
		
	}

}