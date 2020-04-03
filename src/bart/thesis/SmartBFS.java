package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class SmartBFS
{
	public static Board getHardestGoalsSolution(Board board, Move[] allowed)
	{
		ArrayList<Board> frontier = new ArrayList<Board>();
		frontier.add(board);
		ArrayList<int[][]> visited = new ArrayList<int[][]>();
		visited.add(board.getBoard());
		TreeMap<int[][], Integer> goalsOptions = new TreeMap<int[][], Integer>(Processing.GOAL_COMPARATOR);
		goalsOptions.put(board.getBoard(), 0);
		Board hardestGoals = board;
		
		while(!frontier.isEmpty())
		{
			Board current = frontier.remove(0);
			for(int y = 0; y<current.getArrayDim(); y++)
				for(int x = 0; x<current.getArrayDim(); x++)
					if(current.get(x, y)>0)
						for(Move move : allowed)
						{
							Board copy = current.copy();
							copy.move(new Vector(x, y), move);
							int boardPos = Collections.binarySearch(visited, copy.getBoard(), Processing.COMPARATOR);
							if(boardPos<0)
							{
								int[][] grid = copy.getBoard();
								if(goalsOptions.get(grid) == null)
								{
									hardestGoals = copy;
									goalsOptions.put(grid, copy.getDepth());
								}
								frontier.add(copy);
								visited.add(-boardPos-1, copy.getBoard());
							}
						}
		}
		hardestGoals.setGoalsToCurrent();
		return hardestGoals;
	}
}
