import java.io.*;
import java.util.*;

public class Search
{	
	// Alim Search with the graph, the finish node, and the parent being specified
	public void starSearch(String finish, Node parent)
	{
		List<Node> open = null, children = null, searchTree = null;
		Node current = null, evaluate = null, remake = null, startNode = parent;
		Iterator<Node> cIterator = null;
		ReadFile rd = new ReadFile();
		boolean promptValid = false;
		String continuePrompt = "";
		Console csl = System.console();
		
		// Make the search tree
		searchTree = new ArrayList<Node>();
		
		// Make a queue and add the start node to the queue with it having no parents.
		open = new ArrayList<Node>();
		startNode.nullParents();
		open.add(startNode);
		
		current = findLowestScore(open);
		while(!open.isEmpty())
		{
			open.remove(current);
			
			if(current.getParent() != null)
			{
				if(!searchTree.contains(current.getParent()))
				{
					if(searchTree.size() >= 15)
					{									
						searchTree = memoryCheck(searchTree, current.getParent(), startNode);
						
						// Check if all of the path is in the searchTree, if not, remove nodes until the whole path is in the search tree
						searchTree = inSearchTree(current.getParent(), searchTree, startNode);
						
					}
					else
					{
						// add parent to list
						searchTree.add(current.getParent());
						
						// Check if all of the path is in the searchTree, if not, remove nodes so that the whole path is in the search tree
						searchTree = inSearchTree(current.getParent(), searchTree, startNode);
					}
				}
			}
			
			if(searchTree.size() >= 15)
			{					
				searchTree = memoryCheck(searchTree, current, startNode);
				if(searchTree == null)
				{
					return;
				}
				// Check if all of the path is in the searchTree, if not, remove nodes so that the whole path is in the search tree

				searchTree = inSearchTree(current, searchTree, startNode);
			}
			else
			{
				// add current node to search tree
				searchTree.add(current);
				// Check if all of the path is in the searchTree, if not, remove nodes so that the whole path is in the search tree
				searchTree = inSearchTree(current, searchTree, startNode);
			}
			
			// if current node being evaluated is the goal node and the last node in the path is its parent
			if(current.getNodeName().equals(finish) /*&& current.isParent(path.get(path.size()-1)) && current.getParentName().equals(path.get(path.size()-2).getNodeName())*/)
			{
				// add the current node to the path
				// print the path
				// remove the current node from the path
				// prompt the user to continue searching
				// remove the current node's parent which is the previous node in the path list.
				// remove the last node in the list which is the parent of the goal node
				// find the next lowest f_score node in the open list

				System.out.println("--------------------------------------------------------");
				System.out.println(current.printPath(startNode.getNodeName()));
				System.out.println("--------------------------------------------------------");
				
				do
				{
					continuePrompt = csl.readLine("\nWould you like to continue the search? Yes - Y. No - N. ANSWER: ");
					if(continuePrompt.equals("y") || continuePrompt.equals("Y"))
					{
						promptValid = true;
					}
					else if (continuePrompt.equals("n") || continuePrompt.equals("N"))
					{
						promptValid = true;
						return;
					}
					else
					{
						promptValid = false;
					}
				}while(!promptValid);
				current = findLowestScore(open);
			}
			else
			{
				// find the children of the current node being evaluated
				// if the node is the current node, then skip
				// if it is not part of the open list change its f_score and current distance from the start
				// add it to the open list
				
				children = current.getChildren();
				if(!children.isEmpty())
				{
					cIterator = children.iterator();
					while(cIterator.hasNext())
					{
						// Get the next child
						evaluate = cIterator.next();
						
						// While the evaluated and current node both have the same name, skip (Deals with loops or nodes that connect to itself)
						while(evaluate.getNodeName().equals(current.getNodeName()))
						{
							evaluate = cIterator.next();
						}
						
						// Checks if the child is already used in the path. If it isn't, continue...
						if(!current.inPath(startNode.getNodeName(), evaluate.getNodeName())) 
						{
							// remake the evaluated node with the same values as the evaluated node (makes a new object with a different object id)
							remake = new Node(evaluate.getNodeName(), evaluate.getHeuristic());
							remake.addListChildren(evaluate.getChildren());
							remake.setWeights(evaluate.getListWeights());
							remake.addParent(current);
							remake.setScore(evaluate.getScore());
							remake.setCurrDistance(evaluate.getCurrDistance());
							
							// adjusts the current distance and f_score of the new remade node
							remake.setCurrDistance(current.getDistance(remake) + current.getCurrDistance());
							remake.setScore(remake.getHeuristic() + remake.getCurrDistance());
							
							// add to open list
							open.add(remake);
						}
					}
				}
				current = findLowestScore(open);
			}
		}
		return;
	}
	
	public List<Node> memoryCheck(List<Node> searchTree, Node current, Node startNode)
	{
		Node highestScore = null;
				
		// find node with highest f_score that isn't part of the current node's path
		highestScore = findHighestScore(searchTree, current, startNode);
		
		if(highestScore == null)
		{
			System.out.println("--------------------------------------------------------");
			System.out.println("SOLUTION UNABLE TO BE FOUND DUE TO MEMORY LIMIT");
			System.out.println(current.getParent().printPath(startNode.getNodeName()));
			System.out.println("--------------------------------------------------------");
			return searchTree;
		}
		
		// remove it from the search tree
		searchTree.remove(highestScore);
		
		// add parent to the list
		searchTree.add(current);
		
		return searchTree;
	}
	
	// Method is used to check if the current node's path is part of the search tree
	public List<Node> inSearchTree(Node currentNode, List<Node> searchTree, Node startNode)
	{
		List<Node> path = new ArrayList<Node>();
		Iterator<Node> pIterator = null, stIterator = null;
		Node evaluate = null;
		boolean inTree = false;
		
		path = currentNode.returnPath(startNode.getNodeName(), path);
		path.add(currentNode);
		
		stIterator = searchTree.iterator();
		pIterator = path.iterator();
		
		// Checks each path node against all nodes in the search tree
		// If a node in the search tree has the same name as the node from the path list, mark as true to say there is that node in the search tree
		// If the there is no node from the path in the search tree, add the path node into the search tree
		while(pIterator.hasNext())
		{
			evaluate = pIterator.next();
			
			if(searchTree.contains(evaluate))
			{
				inTree = true;
			}
			
			// Nullifies the iterator to change the search tree
			stIterator = null;
			
			// If the search tree doesn't contain a node from the path, ...
			if(inTree == false)
			{
				if(searchTree.size() >= 15)
				{
					searchTree = memoryCheck(searchTree, evaluate, startNode);
				}
				else
				{
					searchTree.add(evaluate);
				}
			}
			
			// Restarts the iterator from the start
			stIterator = searchTree.iterator();
			inTree = false;
		}
		
		return searchTree;
	}
	
	// Find the highest score node to remove when the memory is full
	public Node findHighestScore(List<Node> searchTree, Node currentNode, Node startNode)
	{
		Iterator<Node> sIterator = null;
		Node evaluate = null, returnNode = null;
		
		// go through the search tree
		// get a node
		// if the node is against the return node is null or the node that was obtained has a higher score AND that evaluate is not in the path of the current node
			
		// Now the remainders list are only filled with nodes that aren't part of the path.
		sIterator = searchTree.iterator();
		
		
		while(sIterator.hasNext())
		{
			evaluate = sIterator.next();
			if((returnNode == null || evaluate.getScore() > returnNode.getScore()) && !currentNode.inPath(startNode, evaluate))
			{
				returnNode = evaluate;
			}
		}
		
		return returnNode;
	}
	
	public Node findLowestScore(List<Node> nodes)
	{
		Iterator<Node> nIterator = nodes.iterator();
		Node evaluate = null, returnNode = null;
		
		while(nIterator.hasNext())
		{
			evaluate = nIterator.next();
			if(returnNode == null || evaluate.getScore() < returnNode.getScore())
			{
				returnNode = evaluate;
			}
		}
		
		return returnNode;
		
	}
}