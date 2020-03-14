package bart.thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackwardsGenerator
{
	public static Board generate(GameType gameType, int arrayDim, Vector[] goals, int maxNrOfPawns, int minDifficulty)
	{
		while(true)
		{
			int maxDif = 0;
			int[][] mostDifBoard = null;
			BackwardsNode node = new BackwardsNode(gameType, arrayDim, goals, maxNrOfPawns);
			boolean moved = true;
			while(moved)
			{
				Board board = node.yieldBoard();
				Board solution = new BFS(board, gameType.getAllowed()).solution();
				if(solution == null && maxDif >= minDifficulty && mostDifBoard != null)
				{
					System.out.println("SICK solvable in " + maxDif);
					return new Board(gameType, arrayDim, goals, mostDifBoard);
				}
					
				if(solution.getDepth() > maxDif)
				{
					maxDif = solution.getDepth();
					mostDifBoard = board.getBoard();
				}
				int nrOfUnits = goals.length + node.getNrOfPawns();
				ArrayList<Integer> candidates = Util.sequence(nrOfUnits);
				moved = false;
				while(!candidates.isEmpty() && !moved)
				{
					Vector selected = node.getNext(new Vector(-1, 0));
					int nrOfNexts = Util.removeRandom(candidates);
					for(int i = 0; i<nrOfNexts; i++)
						selected = node.getNext(selected);
					ArrayList<Move> moveCandidates = new ArrayList<Move>(Arrays.asList(gameType.getAllowed()));
					while(!moveCandidates.isEmpty() && !moved)
						moved = node.backwardsMove(selected, Util.removeRandom(moveCandidates));
				}
			}
			if(maxDif >= minDifficulty && mostDifBoard != null)
			{
				System.out.println("SICK solvable in " + maxDif);
				return new Board(gameType, arrayDim, goals, mostDifBoard);
			}
			System.out.println("found one with " + maxDif);
		}
	}
}
