import java.util.*;
import java.io.*;

public class Weight
{
	private String from;
	private String to;
	private int weight;
	
	public Weight(String inFrom, String inTo, int inWeight)
	{
		from = inFrom;
		to = inTo;
		weight = inWeight;		
	}
	
	// Set initial location
	public void setFrom(String inFrom)
	{
		from = inFrom;
	}
	
	// Get initial location
	public String getFrom()
	{
		return from;
	}
	
	// Set destination
	public void setTo(String inTo)
	{
		to = inTo;
	}
	
	// Get destination
	public String getTo()
	{
		return to;
	}
	
	// Set distance
	public void setWeight(int inWeight)
	{
		weight = inWeight;
	}
	
	// Get distance
	public int getWeight()
	{
		return weight;
	}
	
	// Print contents of locations and the distance between two nodes
	public String contents()
	{
		return "FROM: " + from + ", TO: " + to + ", Distance/Weight: " + weight;
	}
}