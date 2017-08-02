import java.util.*;
import java.io.*;

public class Node
{
	private String nodeName; // name of the node
	private double heuristic; // self heuristic
	public List<Node> children; // children of this parent
	public List<Weight> weights; // various weights that this node connects to its children
	public List<Node> parents;
	
	public Node(String inNodeName, double inHeuristic)
	{
		nodeName = inNodeName;
		heuristic = inHeuristic;
		children = new ArrayList<Node>();
		weights = new ArrayList<Weight>();
		parents = new ArrayList<Node>();
	}
	
	public String getNodeName() // Get node name
	{
		return nodeName;
	}
	
	public void setNodeName(String inName) // Set the node name
	{
		nodeName = inName;
	}
	
	public double getHeuristic() // Get the heuristic value for the node
	{
		return heuristic;
	}
	
	public void setHeuristic(double inHeuristic) // Set the heuristic value for the node
	{
		heuristic = inHeuristic;
	}

	public void addParent(Node inParent)
	{
		parents.add(inParent);
	}
	
	public void addChildren(Node inChildren) // Add children to the list of children the node has
	{
		children.add(inChildren);
	}
	
	public List<Node> getChildren() // Get the list of children the node maps to
	{
		return children;
	}

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
	
	public boolean containsChildren(Node inNode) // Method returning true or false depending if inNode exists in the list of children
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
	
	public void addWeight(Weight inWeight) // Method to add weight to the list of weights
	{
		weights.add(inWeight);
	}
	
	public String contents() // Print out the contents of the node
	{
		return "Node Name: " + nodeName + ", Heuristic: " + heuristic;
	}
	
	public String toString() // Used to print out the path of the node
	{
		return "-->" + nodeName;
	}
	
	public void print(String indent) // Print the information of the node and its children and its grandchildren etc.
	{
		Iterator<Node> cIterator = children.iterator();
		System.out.println(indent + "NODE: " + nodeName + ", HEURISTIC: " + heuristic);
		
		while(cIterator.hasNext())
		{;
			cIterator.next().print(indent + "    ");
		}
	}
}