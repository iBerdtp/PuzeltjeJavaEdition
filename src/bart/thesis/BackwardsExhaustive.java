package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class BackwardsExhaustive
{
	private GameType gameType;
	private int chosenDim;
	private int arrayDim;
	private int nrOfGoals;
	private int nrOfPawns;
	private Move[] allowed;
	private LinkedList<Board> frontier;
	private ArrayList<int[][]> visited;
	
	public BackwardsExhaustive(GameType gameType, int chosenDim, int nrOfGoals, int nrOfPawns)
	{
		this.gameType = gameType;
		this.chosenDim = chosenDim;
		this.arrayDim = gameType==GameType.HEX ? chosenDim*2-1 : chosenDim;
		this.nrOfGoals = nrOfGoals;
		this.nrOfPawns = nrOfPawns;
		this.allowed = gameType.getAllowed();
		this.frontier = new LinkedList<Board>();
		this.visited = new ArrayList<int[][]>();
	}
	
	//TODO inbouwen dat ie willekeurig stukjes invult als ie iets heeft gevonden
	
	public ArrayList<Board> getPuzzles()
	{
		Board root = createRoot(new Vector(arrayDim/2, arrayDim/2));
		ArrayList<Board> leaves = new ArrayList<Board>();
		frontier.add(root);
		visited.add(root.getBoard());
		
		while(true)
		{
			if(frontier.isEmpty())
				return leaves;
			Board current = frontier.remove(0);
			boolean deadEnd = true;
			for(int y = 0; y<current.getArrayDim(); y++)
				for(int x = 0; x<current.getArrayDim(); x++)
					if(current.get(x, y)>0)
						for(Move move : allowed)
						{
							if(current.getDifficulty()==0 && move!=Move.UP || current.getDifficulty()==1 && move==Move.RIGHT)
								continue;
							Board copy = current.copy();
							Vector o = new Vector(x+move.v.x, y+move.v.y);
							if(copy.boundariesRespected(o))
							{
								if(copy.get(o)==0)
									if(copy.getNrOfPawns()==nrOfPawns || copy.path[o.x][o.y])
										continue;
									else
									{
										copy.putPawn(o);
										copy.path[o.x][o.y]=true;
										copy.incrementPawnCount();
									}
								int type = copy.get(x, y);
								copy.set(x, y, 0);
								Vector reverse = new Vector(x, y);
								while(copy.boundariesRespected(reverse.sub(move.v)) && copy.get(reverse)==0)
								{
									deadEnd = false;
									copy.set(reverse, type);
									int boardPos = Collections.binarySearch(visited, copy.getBoard(), Processing.COMPARATOR);
									if(boardPos<0)
									{
										copy.path[reverse.x][reverse.y] = true;
										Board copycopy = copy.copy();
										copycopy.incrementDifficulty();
										frontier.add(copycopy);
										visited.add(-boardPos-1, copycopy.getBoard());
									}
									copy.set(reverse, 0);
								}
							}
						}
			if(deadEnd)
				if(nrOfPawns==current.getNrOfPawns())
				{
					BFS bfs = new BFS(current, allowed);
					Board solution = bfs.solution();
					if(solution==null)
					{
						System.out.println("geen solusie voor deze:\n"+current+"\n-----");
						current.printPath();
					}
					if(solution!=null && solution.getDepth()==current.getDifficulty())
						leaves.add(current);
				}
		}
	}
	
	private Board createRoot(Vector... goals)
	{
		int[][] board = new int[arrayDim][arrayDim];
		Board root = new Board(gameType, arrayDim, goals, board);
		for(Vector goal : goals)
		{
			board[goal.x][goal.y] = 1;
			root.path[goal.x][goal.y]= true; 
		}
		return root;
	}
}
