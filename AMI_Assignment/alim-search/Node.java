import java.util.*;
import java.io.*;

public class Node
{
	private String nodeName; // name of the node
	private double heuristic; // self heuristic
	public List<Node> children; // children of this parent
	public List<Weight> weights; // various weights that this node connects to its children
	public List<Node> parents; // parents of the node
	public double score; // f_score of the node
	public double currDistance; // current distance of the node to the starting node
	
	public Node(String inNodeName, double inHeuristic)
	{
		nodeName = inNodeName;
		heuristic = inHeuristic;
		children = new ArrayList<Node>();
		weights = new ArrayList<Weight>();
		parents = new ArrayList<Node>();
		score = 0;
		currDistance = 0;
	}
	
	// Get node name
	public String getNodeName() 
	{
		return nodeName;
	}
	
	// Set the node name
	public void setNodeName(String inName) 
	{
		nodeName = inName;
	}
	
	// Get the heuristic value for the node
	public double getHeuristic() 
	{
		return heuristic;
	}
	
	// Set the heuristic value for the node
	public void setHeuristic(double inHeuristic) 
	{
		heuristic = inHeuristic;
	}
	
	// Add children to the list of children the node has
	public void addChildren(Node inChildren) 
	{
		children.add(inChildren);
	}
	
	// Adds a list of children to the current list of children
	public void addListChildren(List<Node> inChildren) 
	{
		children.addAll(inChildren);
	}
	
	// Get the list of children the node maps to
	public List<Node> getChildren() 
	{
		return children;
	}
	
	// Method returning true or false depending if inNode exists in the list of children
	public boolean containsChildren(Node inNode) 
	{
		Iterator<Node> cIterator = children.iterator();
		Boolean exists = false;
		Node evaluate = null;
		while(cIterator.hasNext())
		{
			evaluate = cIterator.next();
			if(evaluate.getNodeName().equals(inNode.getNodeName()))
			{
				exists = true;
			}
		}
		
		return exists;
	}
	
	// Method to add weight to the list of weights
	public void addWeight(Weight inWeight) 
	{
		weights.add(inWeight);
	}
	
	// Adds a list of weights to the current list of weights
	public void setWeights(List<Weight> inWeight) 
	{
		weights.addAll(inWeight);
	}
	
	// Get the list of weights
	public List<Weight> getListWeights()
	{
		return weights;
	}

	// Add a parent to the list of parents
	public void addParent(Node inParent)
	{
		parents.add(inParent);
	}
	
	// Removes a certain parent from the list of parents
	public void removeParent(Node inParent) 
	{
		parents.remove(inParent);
	}
	
	// Returns a list of parents
	public List<Node> getParents() 
	{
		return parents;
	}
	
	// NULLs the parents, makes node have no parent
	public void nullParents() 
	{
		parents = new ArrayList<Node>();
	}
	
	public Node getParent()
	{
		if(!parents.isEmpty())
		{
			return parents.get(0);
		}
		else
		{
			return null;
		}
		
	}

	// Get the name of the first parent in the list of parents
	public String getParentName() 
	{
		if(parents.isEmpty())
		{
			return "EMPTY";
		}
		else
		{
			return parents.get(0).getNodeName(); // Only ever has one parent because only one node can expand into this particular node.
		}
		
	}
	
	// Determines if a node is a parent of this node
	public boolean isParent(Node node) 
	{
		boolean isAParent = false;
		Iterator<Node> pIterator = parents.iterator();
		Node evaluate = null;
		
		while(pIterator.hasNext())
		{
			evaluate = pIterator.next();
			if(evaluate.getNodeName().equals(node.getNodeName()))
			{
				isAParent = true;
			}
		}

		return isAParent;
	}
	
	// Sets the f_score of the node
	public void setScore(double inScore) 
	{
		score = inScore;
	}
	
	// Gets the f_score of the node
	public double getScore() 
	{
		return score;
	}
	
	// Gets the distance from this node to a particular node
	public double getDistance(Node node) 
	{
		Iterator<Weight> wIterator = weights.iterator();
		double distance = 0.0;
		Weight evaluate = null;
		String from = "", to = "";
		while(wIterator.hasNext())
		{
			evaluate = wIterator.next();
			from = evaluate.getFrom();
			to = evaluate.getTo();
			if(node.getNodeName().equals(to))
			{
				distance = evaluate.getWeight();
			}
		}
		
		return distance;
	}
	
	// Sets the G_Score or the distance the node is from the starting node
	public void setCurrDistance(double inDistance) 
	{
		currDistance = inDistance;
	}
	
	// Get the g_score or distance of the node from the starting node
	public double getCurrDistance() 
	{
		return currDistance;
	}
	
	// Print out the contents of the node
	public String contents() 
	{
		if(!parents.isEmpty())
		{
			return "Node Name: " + nodeName + ", Heuristic: " + heuristic  + ", PARENT: " + parents.get(0).getNodeName() + ", SCORE:" + score;
		}
		else
		{
			return "Node Name: " + nodeName + ", Heuristic: " + heuristic  + ", PARENT: EMPTY, SCORE:" + score;
		}
	}
	
	// Print the information of the node and its children and its grandchildren etc.
	public void print(String indent) 
	{
		Iterator<Node> cIterator = children.iterator();
		System.out.println(indent + "NODE: " + nodeName + ", HEURISTIC: " + heuristic);
		
		while(cIterator.hasNext())
		{
			cIterator.next().print(indent + "    ");
		}
	}
	
	// Returns the path as a list of nodes
	public List<Node> returnPath(String startNode, List<Node> path)
	{
		if(!parents.isEmpty())
		{
			path.add(parents.get(0));
			path = parents.get(0).returnPath(startNode, path);
		}
		return path;
		
	}
	
	// returns false if the inNode is not in the path currently.
	// returns true if it is in the path
	public boolean inPath(String startNode, String inNode)
	{
		boolean isInPath = false;
		if(!parents.isEmpty())
		{
			// if the node exists in the path, set isInPath = true to specify that it is in path
			// if the trickling reaches to the starting node, return false
			// otherwise, recurse up the path.
			
			// check if the start of the path is this node, if it is, return true.
			if(nodeName.equals(startNode))
			{
				isInPath = false;
			}
			else if(parents.get(0).getNodeName().equals(inNode)) // check if this 
			{
				isInPath = true;
			}
			else
			{
				isInPath = parents.get(0).inPath(startNode, inNode);
			}
		}
		
		return isInPath;
	}

	// returns false if the inNode is not in the path currently.
	// returns true if it is in the path
	public boolean inPath(Node startNode, Node inNode)
	{
		boolean isInPath = false;
		if(!parents.isEmpty())
		{
			// if the node exists in the path, set isInPath = true to specify that it is in path
			// if the trickling reaches to the starting node, return false
			// otherwise, recurse up the path.
			
			// check if the start of the path is this node, if it is, return true.
			if(nodeName.equals(startNode.getNodeName()))
			{
				isInPath = false;
			}
			else if(parents.get(0).equals(inNode)) // check if this 
			{
				isInPath = true;
			}
			else
			{
				isInPath = parents.get(0).inPath(startNode, inNode);
			}
		}
		
		return isInPath;
	}
	
	public String printPath(String startNode) // Trickles up the "tree" to find the starting node and prints out the path.
	{
		String path = "";
		if(!nodeName.equals(startNode))
		{
			path = parents.get(0).printPath(startNode) + "-->" + nodeName;
		}
		else
		{
			path = nodeName;
		}
		return path;
	}
}