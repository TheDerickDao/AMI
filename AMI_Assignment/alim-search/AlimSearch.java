import java.util.*;
import java.io.*;

public class AlimSearch
{
	public static void main(String[] args)
	{
		String nodeFile, heuristicFile, startNode, finishNode;
		List<Node> graph;
		ReadFile rd = new ReadFile();
		Iterator<Node> gIterator;
		Node evaluate = null, cGet = null;
		Search search = new Search();
		
		nodeFile = args[0]; // Name of the node file
		heuristicFile = args[1]; // Name of heuristics file
		startNode = args[2]; // Starting node to search from
		finishNode = args[3]; // Finish node to search for
		
		// Obtain the graph
		graph = rd.readNode(nodeFile, heuristicFile);
		
		// Finds the starting node in the graph corresponding to cmd input
		gIterator = graph.iterator();
		while(gIterator.hasNext())
		{
			evaluate = gIterator.next();
			if(evaluate.getNodeName().equals(startNode))
			{
				cGet = evaluate;
			}
		}
		
		// Start the beam search
		System.out.println("\n ========== START MEMORY LIMITED A* SEARCH ==========\n");
		search.starSearch(finishNode, cGet); // Use Beam Informed Search on the node 
		
		System.out.println("\nNO OTHER SOLUTIONS ARE FOUND! \n");
		System.out.println("\n ========== MEMORY LIMITED A* SEARCH COMPLETE ==========\n");
		System.exit(0);
	}
	
	
}
