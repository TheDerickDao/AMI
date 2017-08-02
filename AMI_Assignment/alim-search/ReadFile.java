import java.util.*;
import java.io.*;
import io.*;

public class ReadFile
{
	// Method used to read from a file the node info to create lists of nodes to perform the search.
	// Reads also the heuristic values into the nodes and dictate each node's heuristic values.
	public static List<Node> readNode(String nodeFile, String heuristicFile)
	{
		// Buffers
		FileInputStream fileStrm = null;
		InputStreamReader rdr = null;
		BufferedReader bufRdr = null;
		
		// Used for reading file
		String sLine = "", firstNode, secondNode;
		String[] tokens = null;
		int distance;
		
		// Used for mapping each node with each other
		List<Node> nodesToMap = new ArrayList<Node>();
		List<Weight> nodeWeights = new ArrayList<Weight>();
		Node first = null, second = null;
		Weight nodeWeight = null, nodeWeight2 = null;
	  
		try
		{
			// Create streams and buffers
			fileStrm = new FileInputStream(nodeFile);
			rdr = new InputStreamReader(fileStrm);
			bufRdr = new BufferedReader(rdr);

			sLine = bufRdr.readLine(); // Read the first line of the file
			while(sLine != null)
			{
				if(!sLine.equals(""))
				{
					tokens = sLine.split(" ", -1); // Split with the delimiter being a space
				
					firstNode = tokens[0]; // First Node
					secondNode = tokens[1]; // Second Node
					distance = Integer.parseInt(tokens[2]); // Distance/Weight between first and second node
					
					first = new Node(firstNode, 0); // Creates the parent node with a 0 heuristic to update later when reading the heuristics file.
					second = new Node(secondNode, 0); // Creates the child linked to the parent with a 0 heuristic to update later
					nodeWeight = new Weight(firstNode, secondNode, distance); // Create weight object to define the weight of the parent node to its children
					nodeWeight2 = new Weight(secondNode, firstNode, distance); // Create weight object to define the weight of the parent node to its children

					// Add weights and unique nodes to the list of weights and nodes for later evaluation
					nodeWeights.add(nodeWeight);
					nodeWeights.add(nodeWeight2);
					
					// Check that the nodes being added are unique before adding them to the list
					if(checkUnique(nodesToMap, first))
					{
						nodesToMap.add(first);
					}
					if(checkUnique(nodesToMap, second))
					{
						nodesToMap.add(second);
					}
					
				}
				sLine = bufRdr.readLine();
			}
			nodesToMap = map(nodeWeights, nodesToMap);
			nodesToMap = readHeuristics(heuristicFile, nodesToMap);
			
			fileStrm.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found!");
		}
		catch(IOException e)
		{
			System.out.println("Failed to read line!");
		}
		
		return nodesToMap;
	}
	
	// Method used to map nodes to its children and allocate various weights to each node connecting to its children
	public static List<Node> map(List<Weight> weights, List<Node> nodes)
	{
		List<Node> returnNodes = new ArrayList<Node>();
		String from = "", to = "";
		Iterator<Weight> wIterator = weights.iterator();
		Iterator<Node> nIterator = null, retIterator = null;
		Node fromNode, toNode = null, evaluate = null, retEvaluate = null;
		Weight wEvaluate = null;
		
		while(wIterator.hasNext())
		{
			// Get to and from values for allocation of nodes
			wEvaluate = wIterator.next();
			from = wEvaluate.getFrom();
			to = wEvaluate.getTo();
			fromNode = null;
			toNode = null;
			
			nIterator = nodes.iterator(); // Resets the iterator every loop
			retIterator = returnNodes.iterator();
			
			// Iterate through the node list and the list to return.
			// If the node in the returning list maps to the node list, grab the from and to nodes.
			// Add the weights to the fromNode and add its children.
			// Check that the nodes are unique in the returning list before adding them to it.
			while(nIterator.hasNext())
			{
				evaluate = nIterator.next();
				retIterator = returnNodes.iterator();
				while(retIterator.hasNext()) // Checks if FROM and TO nodes are already in the list to return
				{
					retEvaluate = retIterator.next();
					if(retEvaluate.getNodeName().equals(from))
					{
						fromNode = retEvaluate;
					}
					if(retEvaluate.getNodeName().equals(to))
					{
						toNode = retEvaluate;
					}
				}
				if(evaluate.getNodeName().equals(from) && fromNode == null) // Finds the node that is the parent
				{
					fromNode = evaluate;
				}
				
				if(evaluate.getNodeName().equals(to) && toNode == null) // Find the node that the parent connects to
				{
					toNode = evaluate;
				}
			}
			fromNode.addChildren(toNode);
			toNode.addParent(fromNode);

			fromNode.addWeight(wEvaluate);
			
			// Checks that the nodes being added are unique in the list
			if(checkUnique(returnNodes, fromNode))
			{
				returnNodes.add(fromNode);
			}
			if(checkUnique(returnNodes, toNode))
			{
				returnNodes.add(toNode);
			}
		}
		
		return returnNodes;
	}
	
	// Reads the heuristics file and allocates all the heuristics to the specified nodes
	public static List<Node> readHeuristics(String filename, List<Node> nodes)
	{
		// Buffers
		FileInputStream fileStrm = null;
		InputStreamReader rdr = null;
		BufferedReader bufRdr = null;
		
		// Used for reading file
		String sLine = "", nodeNumber;
		String[] tokens = null;
		double heuristic;
		
		// Used for inserting heuristics to each node
		Iterator<Node> nIterator = null;
		Node evaluate = null;
	  
		try
		{
			// Create streams and buffers
			fileStrm = new FileInputStream(filename);
			rdr = new InputStreamReader(fileStrm);
			bufRdr = new BufferedReader(rdr);

			sLine = bufRdr.readLine(); // Skips the first line indicating headers
			sLine = bufRdr.readLine(); // Read the first line of the file
			while(sLine != null)
			{
				if(!sLine.equals(""))
				{
					tokens = sLine.split(" ", -1); // Split with the delimiter being a space
				
					nodeNumber = tokens[0]; // Node name for heuristic
					heuristic = Double.parseDouble(tokens[1]); // Heuristic value
					
					nIterator = nodes.iterator(); // Reset iterator
					
					// If the name of the node specified in heuristics file maps to one in the given list, set the heuristic.
					while(nIterator.hasNext())
					{
						evaluate = nIterator.next();
						if(evaluate.getNodeName().equals(nodeNumber))
						{
							evaluate.setHeuristic(heuristic);							
						}
					}
				}
				sLine = bufRdr.readLine();
			}
			
			fileStrm.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found!");
		}
		catch(IOException e)
		{
			System.out.println("Failed to read line!");
		}
		return nodes;
	}
	
	// Function to check for unique named nodes in the list.
	// Returns true if there is no node with the same node as the one being inserted in.
	// Returns false if the list contains a node with that name.
	public static boolean checkUnique(List<Node> nodeMap, Node insertNode)
	{
		Iterator<Node> mapIterator = nodeMap.iterator();
		Node evaluate;
		boolean unique = true;
		
		while(mapIterator.hasNext())
		{
			evaluate = mapIterator.next();
			if(evaluate.getNodeName().equals(insertNode.getNodeName()))
			{
				unique = false;
			}
		}
		
		return unique;
	}
	
	// Method used in testing to print the contents of a list of Nodes
	public static void printContentsN(List<Node> list)
	{
		Iterator<Node> lIterator = list.iterator();
		Node nodePrint = null;
		
		while(lIterator.hasNext())
		{
			nodePrint = lIterator.next();
			System.out.println(nodePrint.contents());
		}
	}
	
	// Method used in testing to print the contents of a list of Weights
	public static void printContentsW(List<Weight> list)
	{
		Iterator<Weight> lIterator = list.iterator();
		Weight nodePrint = null;
		
		while(lIterator.hasNext())
		{
			nodePrint = lIterator.next();
			System.out.println(nodePrint.contents());
		}
	}
}