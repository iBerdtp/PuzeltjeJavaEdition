package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BackwardsExhaustive
{
	private GameType gameType;
	private int arrayDim;
	private int maxNrOfPawns;
	private Vector[] goals;
	private Move[] allowed;
	private LinkedList<BackwardsNode> frontier;
	private ArrayList<int[][]> visited;
	private int maxDif;
	private ArrayList<int[][]> hardests;
	
	public BackwardsExhaustive(GameType gameType, int chosenDim, Vector[] goals, int maxNrOfPawns)
	{
		this.gameType = gameType;
		this.arrayDim = gameType==GameType.HEX ? chosenDim*2-1 : chosenDim;
		this.maxNrOfPawns = maxNrOfPawns;
		this.goals = goals;
		this.allowed = gameType.getAllowed();
		this.frontier = new LinkedList<BackwardsNode>();
		this.visited = new ArrayList<int[][]>();
		this.maxDif = 0;
		this.hardests = new ArrayList<int[][]>();
	}
	
	public ArrayList<int[][]> getHardests()
	{
		BackwardsNode root = new BackwardsNode(gameType, arrayDim, goals, maxNrOfPawns);
		registerAll(root.completeBoard());
		while(true)
		{
			if(frontier.isEmpty())
				return hardests;
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
				}
				hardests.add(node.getBoard());
			}
			frontier.add(node);
			visited.add(-boardPos-1, node.getBoard());
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
}