package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;

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
		if(frontier.get(0).isWin())
			return frontier.get(0);
		while(true)
		{
			if(frontier.isEmpty())
				return null;
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
								if(copy.isWin())
									return copy;
								frontier.add(copy);
								visited.add(-boardPos-1, copy.getBoard());
							}
						}
		}
	}
}