package bart.thesis;

public class Board
{
	protected GameType gameType;
	protected int arrayDim;
	private int[][] board;
	public Vector[] goals;
	private int difficulty;
	protected int depth;
	private Board solution;
	
	public Board(GameType gameType, int arrayDim)
	{
		this.gameType = gameType;
		this.arrayDim = arrayDim;
		this.board = gameType.getEmpty(arrayDim);
		this.goals = new Vector[0];
	}
	
	public Board(GameType gameType, int arrayDim, Vector[] goals, Vector[] pawns, Vector[] blues)
	{
		this(gameType, arrayDim);
		this.goals = goals;
		for(Vector v : pawns)
			set(v, 2);
		for(Vector v : blues)
			set(v, 1);
	}
	
	public Board(GameType gameType, int arrayDim, Vector[] goals, int[][] board)
	{
		this(gameType, arrayDim);
		this.goals = goals;
		this.board = board;
	}
	
	public Board(GameType gameType, int arrayDim, Vector[] goals)
	{
		this(gameType, arrayDim, goals, new int[arrayDim][arrayDim]);
	}
	
	protected Board(Board b)
	{
		this(b.gameType, b.arrayDim);
		for(int y = 0; y<arrayDim; y++)
			for(int x = 0; x<arrayDim; x++)
				set(x, y, b.get(x, y));
		setGoals(b.goals);
		setDepth(b.depth);
	}
	
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
	
	public boolean move(Vector selected, Move move)
	{
		if(move!=null && get(selected)>0)
		{
			int type = get(selected);
			Vector addee = move.v;
			Vector scout = selected.copy().add(addee);
			if(boundariesRespected(scout) && get(scout)==0)
				while(boundariesRespected(scout.add(addee)))
					if(get(scout)>0)
					{
						set(selected, 0);
						set(selected.setTo(scout.sub(addee)), type);
						depth++;
						return true;
					}
		}
		return false;
	}
	
	protected boolean boundariesRespected(Vector v)
	{
		return boundariesRespected(v.x, v.y);
	}
	
	protected boolean boundariesRespected(int x, int y)
	{
		return x>=0 && x<arrayDim && y>=0 && y<arrayDim && get(x, y)!=-1;
	}
	
	public int get(int x, int y)
	{
		return board[x][y];
	}
	
	public int get(Vector v)
	{
		return get(v.x, v.y);
	}
	
	public Vector getNext(Vector v)
	{
		int x = v.x+1;
		for(int y = v.y; y<arrayDim; y++)
		{
			for(; x<arrayDim; x++)
				if(get(x, y)>0)
					return new Vector(x, y);
			x = 0;
		}
		return getNext(new Vector(-1, 0));
		// kan oneindig bij leeg bord
	}
	
	public Vector getNextEmpty(Vector v)
	{
		int x = v.x+1;
		for(int y = v.y; y<arrayDim; y++)
		{
			for(; x<arrayDim; x++)
				if(get(x, y)==0)
					return new Vector(x, y);
			x = 0;
		}
		return null;
	}
	
	public void set(int x, int y, int n)
	{
		board[x][y] = n;
	}
	
	public void set(Vector v, int n)
	{
		set(v.x, v.y, n);
	}
	
	public boolean isWin()
	{
		for(Vector goal : goals)
			if(get(goal)!=1)
				return false;
		return true;
	}
	
	public boolean isValid()
	{
		for(Vector goal : goals)
			if(goal==null)
				return false;
		return true;
	}
	
	public boolean isGoal(int x, int y)
	{
		for(Vector goal : goals)
			if(x==goal.x && y==goal.y)
				return true;
		return false;
	}
	
	public void extendGoals(int x, int y)
	{
		Vector[] backup = goals.clone();
		goals = new Vector[goals.length+1];
		for(int i=0; i<backup.length; i++)
			goals[i] = backup[i];
		goals[goals.length-1] = new Vector(x, y);
	}
	
	public void removeGoal(int x, int y)
	{
		Vector[] backup = goals.clone();
		goals = new Vector[goals.length-1];
		for(int i=0, j=0; i<backup.length; i++)
			if(backup[i].x != x || backup[i].y != y)
				goals[j++] = backup[i];
	}
	
	public Board copy()
	{
		return new Board(this);
	}
	
	public String[][] toStringArray()
	{
		String[][] strArray = new String[arrayDim][arrayDim];
		for(int y = 0; y<arrayDim; y++)
			for(int x = 0; x<arrayDim; x++)
			{
				strArray[x][y] = Integer.toString(get(x, y));
				for(Vector goal : goals)
					if(goal.x==x && goal.y==y)
						strArray[x][y] += "g";
			}
		return strArray;
	}
	
	public int getArrayDim()
	{
		return arrayDim;
	}
	
	public Vector[] getGoals()
	{
		return goals;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(int y = 0; y<board.length; y++)
		{
			for(int x = 0; x<board[y].length; x++)
				sb.append(get(x, y)+(x<board[y].length-1 ? " " : ""));
			sb.append(y<board.length-1 ? "\n" : "");
		}
		return sb.toString();
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}
	
	public int[][] getBoard()
	{
		return board;
	}
	
	public GameType getGameType()
	{
		return gameType;
	}
	
	public void setGoals(Vector[] goals)
	{
		this.goals = goals;
	}
	
	public void setGoalsToCurrent(boolean correctAmount)
	{
		if(correctAmount)
			goals = new Vector[goals.length];
		else
			goals = new Vector[getNrOfGoals()];
		int i=0;
		for(int y=0; y<arrayDim; y++)
			for(int x=0; x<arrayDim; x++)
				if(get(x, y) == 1)
					goals[i++] = new Vector(x, y);
	}
	
	public int getDepth()
	{
		return depth;
	}
	
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	protected void setBoard(int[][] board)
	{
		this.board = board;
	}
	
	public void setSolution(Board solution)
	{
		this.solution = solution;
	}
	
	public Board getSolution()
	{
		return solution;
	}
	
	private int getNrOfGoals()
	{
		int nrOfGoals = 0;
		for(int y=0; y<arrayDim; y++)
			for(int x=0; x<arrayDim; x++)
				if(get(x, y) == 1)
					nrOfGoals++;
		return nrOfGoals;
	}
}
