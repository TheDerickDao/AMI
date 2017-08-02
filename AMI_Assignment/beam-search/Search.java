import java.io.*;
import java.util.*;
import java.io.Console;

public class Search
{
	public static int count = 0;
	// Beam search with a parameter kValue dictating the k value passed through from the command line
	public boolean search(List<Node> node, String finish, int kValue, List<Node> path, Node parent)
	{
		Node evaluate = null;
		List<Node> children = null;
		Iterator<Node> nIterator = node.iterator(), findChildren = null;
		boolean returnVal = false, promptValid = false;
		ReadFile rd = new ReadFile();
		String continuePrompt = "";
		Console csl = System.console();
		
		while(nIterator.hasNext())
		{
			evaluate = nIterator.next();
			if(rd.checkUnique(path, evaluate))
			{
;				if(parent.containsChildren(evaluate))
				{
					path.add(evaluate);
					
					// If the current node being evaluated is the same as the goal node, print the path and remove the node from the path list.
					if(evaluate.getNodeName().equals(finish))
					{
						if(count == 0)
						{
							System.out.println("------------------------------------");
							System.out.println(" ~~ COMPLETE PATH ~~ \n");
							printPath(path);
							System.out.println("------------------------------------");
							path.remove(evaluate); // removes the current node that was put in the path list to print to backtrack
							count++;
						}
						else
						{
							System.out.println("------------------------------------");
							System.out.println(" ~~ ALTERNATE PATH ~~ \n");
							printPath(path);
							System.out.println("------------------------------------");
							path.remove(evaluate); // removes the current node that was put in the path list to print to backtrack
						}
						
						
						do
						{
							continuePrompt = csl.readLine("Would you like to continue the search? Yes - Y. No - N. ANSWER: ");
							if(continuePrompt.equals("y") || continuePrompt.equals("Y"))
							{
								promptValid = true;
							}
							else if (continuePrompt.equals("n") || continuePrompt.equals("N"))
							{
								promptValid = true;
								return false;
							}
							else
							{
								promptValid = false;
							}
						}while(!promptValid);
					}
					else
					{
						// Find children and grandchildren for recursion
						children = findChildren(node, kValue, path);
						
						// Run recursion
						returnVal = search(children, finish, kValue, path, evaluate);
						
						if(!returnVal)
						{
							return false;
						}
						
						printPath(path);
						path.remove(evaluate);
					}
				}
			}
			
		}
	
		if(continuePrompt.equals("y") || continuePrompt.equals("Y"))
		{
			return true;
		}
		else if (continuePrompt.equals("n") || continuePrompt.equals("N"))
		{
			return false;
		}
		return true;
	}
	
	// Method used to find k children from its parents with the least heuristic value
	public List<Node> findChildren(List<Node> parents, int kValue, List<Node> path)
	{
		List<Node> children = new ArrayList<Node>(), retNodes = new ArrayList<Node>();
		Node parent = null, evaluate = null, least = null;
		Iterator<Node> pIterator = null, cIterator = null;
		ReadFile rd = new ReadFile();
		
		// Gets all the children from the parents
		pIterator = parents.iterator();
		while(pIterator.hasNext())
		{
			parent = pIterator.next();
			children.addAll(parent.getChildren());
		}
		
		// From all the children, find the k amount of nodes with the least heuristics
		for(int ii = 0; ii < kValue; ii++)
		{
			cIterator = children.iterator();
			least = null;
			evaluate = null;
			while(cIterator.hasNext())
			{
				evaluate = cIterator.next();
				if((least == null || evaluate.getHeuristic() < least.getHeuristic()) && rd.checkUnique(retNodes, evaluate)  && rd.checkUnique(path, evaluate))
				{
					least = evaluate; // The evaluated node is now the smallest heuristic node
					
				}
			}
			
			if(least != null)
			{
				retNodes.add(least);
			}
		}
		
		return retNodes;
	}
	
	// Method used to print the search path
	public void printPath(List<Node> path)
	{
		String output = "";
		Iterator<Node> pIterator = path.iterator();
		
		while(pIterator.hasNext())
		{
			output = output + pIterator.next().toString(); // Accumulates a string to print to the screen
			
		}
		System.out.println(output);
	}
}