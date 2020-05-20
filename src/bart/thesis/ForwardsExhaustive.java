package bart.thesis;

import java.util.ArrayList;
import java.util.LinkedList;

public class ForwardsExhaustive implements Analyser
{
	private GameType gameType;
	private int arrayDim;
	private int nrOfPawns;
	private Vector[] goals;
	private int nrOfGoals;
	private Move[] allowed;
	private ArrayList<Integer> distribution;
	private int unsolvableCount;
	private int maxDif;
	private ArrayList<int[][]> hardests;
	private ArrayList<int[][]> hardestsSolved;
	private boolean calculated;
	
	public ForwardsExhaustive(GameType gameType, int chosenDim, Vector[] goals, int nrOfPawns)
	{
		this.calculated = false;
		this.gameType = gameType;
		this.arrayDim = gameType==GameType.HEX ? chosenDim*2-1 : chosenDim;
		this.nrOfPawns = nrOfPawns;
		this.goals = goals;
		this.nrOfGoals = goals.length;
		this.allowed = gameType.getAllowed();
		this.distribution = new ArrayList<Integer>();
		this.unsolvableCount = 0;
		this.maxDif = -1;
		this.hardests = new ArrayList<int[][]>();
		this.hardestsSolved = new ArrayList<int[][]>();
	}
	
	public void calculate()
	{
		if(calculated)
		{
			System.out.println("Already calculated");
			return;
		}
		calculated = true;
		LinkedList<Board> puzzles = generateAllPossiblePuzzles();
		for(Board puzzle : puzzles)
		{
			BFS bfs = new BFS(puzzle, allowed);
			Board solution = bfs.solution();
			if(solution!=null)
			{
				int depth = solution.getDepth();
				if(depth>=maxDif)
				{
					if(depth>maxDif)
					{
						hardests = new ArrayList<int[][]>();
						for(int i=0; i<depth-maxDif; i++)
							distribution.add(0);
						maxDif = depth;
					}
					hardests.add(puzzle.getBoard());
					hardestsSolved.add(solution.getBoard());
				}
				distribution.set(depth, distribution.get(depth)+1);
			}
			else
				unsolvableCount++;
		}
	}
	
	private LinkedList<Board> generateAllPossiblePuzzles()
	{
		LinkedList<Board> puzzles = new LinkedList<Board>();
		Vector location = new Vector(-1, 0);
		Board empty = new Board(gameType, arrayDim, goals);
		addPawns(empty, location, 0, puzzles);
		return puzzles;
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
		return unsolvableCount;
	}
	
	public ArrayList<Integer> getDistribution()
	{
		return distribution;
	}
}