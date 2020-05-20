package bart.thesis;

import java.util.ArrayList;

public class ProbabilisticAnalyserSmart implements Analyser
{
	private GameType gameType;
	private int arrayDim;
	private int nrOfPawns;
	private int nrOfGoals;
	private ArrayList<Integer> distribution;
	private int maxDif;
	private ArrayList<int[][]> hardests;
	private ArrayList<Vector[]> hardestsGoals;
	private ArrayList<int[][]> hardestsSolved;
	private boolean calculated;
	private int nrOfPuzzles;
	
	public ProbabilisticAnalyserSmart(GameType gameType, int chosenDim, Vector[] goals, int maxNrOfPawns)
	{
		this(gameType, chosenDim, maxNrOfPawns, 10000);
	}
	
	public ProbabilisticAnalyserSmart(GameType gameType, int chosenDim, int maxNrOfPawns, int nrOfPuzzles)
	{
		this.calculated = false;
		this.gameType = gameType;
		this.arrayDim = gameType==GameType.HEX ? chosenDim*2-1 : chosenDim;
		this.hardests = new ArrayList<int[][]>();
		this.hardestsGoals = new ArrayList<Vector[]>();
		this.hardestsSolved = new ArrayList<int[][]>();
		this.distribution = new ArrayList<Integer>();
		this.maxDif = -1;
		this.nrOfPuzzles = nrOfPuzzles;
	}
	
	public void calculate()
	{
		if(calculated)
		{
			System.out.println("Already calculated");
			return;
		}
		calculated = true;
		for(int i=0; i<nrOfPuzzles; i++)
		{
			Board puzzle = SmartGenerator.generatePuzzle(gameType, arrayDim, nrOfGoals, nrOfPawns, new float[] {0.95f,0.48f,0.02f});
			int difficulty = puzzle.getDifficulty();
			if(difficulty>=maxDif)
			{
				if(difficulty>maxDif)
				{
					hardests = new ArrayList<int[][]>();
					hardestsGoals = new ArrayList<Vector[]>();
					hardestsSolved = new ArrayList<int[][]>();
					for(int j=0; j<difficulty-maxDif; j++)
						distribution.add(0);
					maxDif = difficulty;
				}
				hardests.add(puzzle.getBoard());
				hardestsGoals.add(puzzle.getGoals());
				hardestsSolved.add(puzzle.getSolution().getBoard());
			}
			distribution.set(difficulty, distribution.get(difficulty)+1);
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
		return 0;
	}
	
	public ArrayList<Integer> getDistribution()
	{
		return distribution;
	}
}