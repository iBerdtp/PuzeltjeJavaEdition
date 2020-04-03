package bart.thesis;

import java.util.ArrayList;

public class SmartGenerator
{
	private Board board;
	
	public Board generate(int minDif)
	{
		return null;
	}
	
	public static Board generatePuzzle(GameType gameType, int arrayDim, int nrOfGoals, int nrOfPawns, float[] p)
	{
		Board begin = generateSmartBegin(gameType, arrayDim, nrOfGoals, nrOfPawns, p);
		Board solution = SmartBFS.getHardestGoalsSolution(begin, gameType.getAllowed());
		begin.setGoals(solution.getGoals());
		begin.setDifficulty(solution.getDepth());
		return begin;
	}
	
	private static Board generateSmartBegin(GameType gameType, int arrayDim, int nrOfGoals, int nrOfPawns, float[] p)
	{
		Board begin = new Board(gameType, arrayDim);
		ArrayList<ArrayList<Vector>> rings = new ArrayList<ArrayList<Vector>>();
		rings.add(getRing(begin.getArrayDim(), 0));
		rings.add(getRing(begin.getArrayDim(), 1));
		rings.add(getRing(begin.getArrayDim(), 2));
		addUnits(begin, rings, p, nrOfGoals, 1);
		addUnits(begin, rings, p, nrOfPawns, 2);
		begin.setGoals(new Vector[nrOfGoals]);
		return begin;
	}
	
	private static void addUnits(Board board, ArrayList<ArrayList<Vector>> rings, float[] p, int nrOfUnits, int type)
	{
		for(int i = 0; i<nrOfUnits; i++)
		{
			ArrayList<Vector> ring = Util.sample(rings, p);
			if(!ring.isEmpty())
				board.set(Util.removeRandom(ring), type);
			else
				i--;
		}
	}
	
	private static ArrayList<Vector> getRing(int arrayDim, int fromOuter)
	{
		ArrayList<Vector> ring = new ArrayList<Vector>();
		for(int y = fromOuter; y<arrayDim-fromOuter; y++)
		{
			ring.add(new Vector(fromOuter, y));
			if(fromOuter!=arrayDim-fromOuter-1)
				ring.add(new Vector(arrayDim-fromOuter-1, y));
		}
		for(int x = fromOuter+1; x<arrayDim-fromOuter-1; x++)
		{
			ring.add(new Vector(x, fromOuter));
			ring.add(new Vector(x, arrayDim-fromOuter-1));
		}
		return ring;
	}
}
