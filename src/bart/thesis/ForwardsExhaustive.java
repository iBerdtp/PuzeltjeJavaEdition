package bart.thesis;

import java.util.ArrayList;
import java.util.LinkedList;

public class ForwardsExhaustive
{
	private GameType gameType;
	private int arrayDim;
	private int nrOfPawns;
	private Vector[] goals;
	private int nrOfGoals;
	private Move[] allowed;
	private int maxDif;
	private ArrayList<int[][]> hardests;
	
	public ForwardsExhaustive(GameType gameType, int chosenDim, Vector[] goals, int nrOfPawns)
	{
		this.gameType = gameType;
		this.arrayDim = gameType==GameType.HEX ? chosenDim*2-1 : chosenDim;
		this.nrOfPawns = nrOfPawns;
		this.goals = goals;
		this.nrOfGoals = goals.length;
		this.allowed = gameType.getAllowed();
		this.maxDif = 0;
		this.hardests = new ArrayList<int[][]>();
	}
	
	public ArrayList<int[][]> getHardests()
	{
		LinkedList<Board> possibilities = generateAllPossibililties();
		for(Board possibility : possibilities)
		{
			BFS bfs = new BFS(possibility, allowed);
			Board solution = bfs.solution();
			if(solution!=null && solution.getDepth()>=maxDif)
			{
				if(solution.getDepth()>maxDif)
				{
					maxDif = solution.getDepth();
					hardests = new ArrayList<int[][]>();
				}
				hardests.add(possibility.getBoard());
			}
		}
		return hardests;
	}
	
	public LinkedList<Board> generateAllPossibililties()
	{
		LinkedList<Board> possibilities = new LinkedList<Board>();
		Vector location = new Vector(-1, 0);
		Board empty = new Board(gameType, arrayDim, goals);
		addPawns(empty, location, 0, possibilities);
		return possibilities;
	}
	
	private void addPawns(Board current, Vector location, int count, LinkedList<Board> possibilities)
	{
		if(count==nrOfPawns)
		{
			addGoalUnits(current, new Vector(-1, 0), 0, possibilities);
			return;
		}
		while((location = current.getNextEmpty(location))!=null)
		{
			Board copy = current.copy();
			copy.set(location, 2);
			addPawns(copy, location, count+1, possibilities);
		}
	}
	
	private void addGoalUnits(Board current, Vector location, int count, LinkedList<Board> possibilities)
	{
		if(count==nrOfGoals)
		{
			possibilities.add(current);
			return;
		}
		while((location = current.getNextEmpty(location))!=null)
		{
			Board copy = current.copy();
			copy.set(location, 1);
			addGoalUnits(copy, location, count+1, possibilities);
		}
	}
	
	public int getGod()
	{
		return maxDif;
	}
}