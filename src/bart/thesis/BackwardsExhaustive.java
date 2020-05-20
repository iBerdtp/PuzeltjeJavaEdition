package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BackwardsExhaustive implements Analyser
{
	private GameType gameType;
	private int arrayDim;
	private int maxNrOfPawns;
	private Vector[] goals;
	private Move[] allowed;
	private LinkedList<BackwardsNode> frontier;
	private ArrayList<int[][]> visited;
	private int maxDif;
	private ArrayList<Integer> distribution;
	private ArrayList<int[][]> hardests;
	private ArrayList<int[][]> hardestsSolved;
	private boolean calculated;
	
	public BackwardsExhaustive(GameType gameType, int chosenDim, Vector[] goals, int maxNrOfPawns)
	{
		this.calculated = false;
		this.gameType = gameType;
		this.arrayDim = gameType==GameType.HEX ? chosenDim*2-1 : chosenDim;
		this.maxNrOfPawns = maxNrOfPawns;
		this.goals = goals;
		this.allowed = gameType.getAllowed();
		this.frontier = new LinkedList<BackwardsNode>();
		this.visited = new ArrayList<int[][]>();
		this.hardests = new ArrayList<int[][]>();
		this.hardestsSolved = new ArrayList<int[][]>();
		this.distribution = new ArrayList<Integer>();
		this.maxDif = -1;
	}
	
	public void calculate()
	{
		if(calculated)
		{
			System.out.println("Already calculated");
			return;
		}
		calculated = true;
		BackwardsNode root = new BackwardsNode(gameType, arrayDim, goals, maxNrOfPawns);
		registerAll(root.completeBoard());
		while(true)
		{
			if(frontier.isEmpty())
				return;
			BackwardsNode current = frontier.remove(0);
			for(int y = 0; y<current.getArrayDim(); y++)
				for(int x = 0; x<current.getArrayDim(); x++)
					if(current.get(x, y)>0)
						for(Move move : allowed)
						{
							ArrayList<BackwardsNode> parents = current.getPossibleParents(new Vector(x, y), move);
							for(BackwardsNode parent : parents)
								register(parent);
						}
		}
	}
	
	private void register(BackwardsNode node)
	{
		int boardPos = Collections.binarySearch(visited, node.getBoard(), Processing.COMPARATOR);
		if(boardPos<0)
		{
			if(node.getNrOfPawns()==node.getMaxNrOfPawns())
			{
				if(node.getDepth()>maxDif)
				{
					maxDif = node.getDepth();
					hardests = new ArrayList<int[][]>();
					hardestsSolved = new ArrayList<int[][]>();
					distribution.add(0);
				}
				hardests.add(node.getBoard());
				hardestsSolved.add(node.getSolved().getBoard());
			}
			frontier.add(node);
			visited.add(-boardPos-1, node.getBoard());
			distribution.set(maxDif, distribution.get(maxDif)+1);
		}
	}
	
	private void registerAll(ArrayList<BackwardsNode> nodes)
	{
		for(BackwardsNode node : nodes)
			register(node);
	}
	
	public int getGod()
	{
		return maxDif;
	}

	public ArrayList<int[][]> getHardests()
	{
		return hardests;
	}
	
	public ArrayList<int[][]> getHardestsSolved()
	{
		return hardestsSolved;
	}
	
	public int getUnsolvableCount()
	{
		return 0;
	}
	
	public ArrayList<Integer> getDistribution()
	{
		return distribution;
	}
}