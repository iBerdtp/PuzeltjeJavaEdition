package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;
import processing.core.PVector;

public class BFS
{
	private Move[] allowed;
	private ArrayList<Board> frontier;
	private ArrayList<int[][]> visited;
	
	public BFS(Board board, Move[] allowed)
	{
		this.allowed = allowed;
		this.frontier = new ArrayList<Board>();
		frontier.add(board);
		this.visited = new ArrayList<int[][]>();
		visited.add(board.getBoard());
	}
	
	public Board solution()
	{
		while(true)
		{
			if(frontier.isEmpty())
				return null;
			Board current = frontier.remove(0);
			for(int i = 0; i<current.getArrayDim(); i++)
				for(int j = 0; j<current.getArrayDim(); j++)
					if(current.get(i, j)>0)
						for(Move move : allowed)
						{
							Board copy = current.copy();
							copy.move(new PVector(i, j), move);
							int boardPos = Collections.binarySearch(visited, copy.getBoard(), Processing.COMPARATOR);
							if(boardPos<0)
							{
								if(copy.isWin())
									return copy;
								frontier.add(copy);
								visited.add(-boardPos-1, copy.getBoard());
							}
						}
		}
	}
}