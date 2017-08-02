import java.util.*;
import java.io.*;

public class BeamSearch
{
	public static void main(String[] args)
	{
		String nodeFile, heuristicFile, startNode, finishNode;
		int kValue;
		List<Node> graph, path, children, cGetList;
		ReadFile rd = new ReadFile();
		Iterator<Node> gIterator;
		Node evaluate = null, cGet = null;
		Search search = new Search();
		
		nodeFile = args[0]; // Name of the node file
		heuristicFile = args[1]; // Name of heuristics file
		startNode = args[2]; // Starting node to search from
		finishNode = args[3]; // Finish node to search for
		kValue = Integer.parseInt(args[4]); // Depth level of the Beam Search
		
		// Obtain the graph
		graph = rd.readNode(nodeFile, heuristicFile);
		
		// Finds the starting node in the graph corresponding to cmd input
		gIterator = graph.iterator();
		
		// Gets the starting node from the graph
		while(gIterator.hasNext())
		{
			evaluate = gIterator.next();
			if(evaluate.getNodeName().equals(startNode))
			{
				cGet = evaluate;
			}
		}
		
		// Add the starting node to the path list for printing
		path = new ArrayList<Node>();
		path.add(cGet);
		
		// Gets the starting node into a list to find children
		cGetList = new ArrayList<Node>();
		cGetList.add(cGet);
		children = search.findChildren(cGetList, kValue, path);
		
		// Start the beam search
		System.out.println("\n ========== START BEAM SEARCH ==========\n");
		search.search(children, finishNode, kValue, path, cGet); // Use Beam Informed Search on the node 
		
		System.out.println("\n ========== BEAM SEARCH COMPLETE ==========\n");
		System.exit(0);
	}
	
	
}