package bart.thesis;

import processing.core.PVector;

public class Board
{
	private GameType gameType;
	private int arrayDim;
	private int[][] board;
	private PVector lastSelected;
	private Move lastMove;
	private int depth;
	public PVector[] goals;
	private int difficulty;
	private Board parent;
	
	public Board(GameType gameType, int arrayDim)
	{
		this.gameType = gameType;
		this.arrayDim = arrayDim;
		this.board = new int[arrayDim][arrayDim];
	}
	
	public Board(GameType gameType, int arrayDim, PVector[] goals, PVector[] pawns, PVector[] blues)
	{
		this(gameType, arrayDim);
		this.goals = goals;
		for(PVector v : pawns)
			set(v, 2);
		for(PVector v : blues)
			set(v, 1);
	}
	
	public Board(GameType gameType, int arrayDim, PVector goals[], int[][] board)
	{
		this(gameType, arrayDim);
		this.goals = goals;
		this.board = board;
	}
	
	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}
	
	public PVector move(PVector selected, Move move)
	{
		int type = get(selected);
		if(type>0 && move!=null)
		{
			lastSelected = selected.copy();
			lastMove = move;
			PVector direction = move.getVector();
			PVector v = PVector.add(selected, direction);
			
			if(boundariesRespected(v) && get(v)==0)
			{
				v.add(direction);
				while(boundariesRespected(v))
				{
					if(get(v)>0)
					{
						v.sub(direction);
						set(v, type);
						if(!lastSelected.equals(v))
							set(lastSelected, 0);
						setDepth(getDepth()+1);
						return v;
					}
					v.add(direction);
				}
			}
		}
		return selected;
	}
	
	private boolean boundariesRespected(PVector v)
	{
		return v.x>=0 && v.x<arrayDim && v.y>=0 && v.y<arrayDim && get(v)!=-1;
	}
	
	public int get(int x, int y)
	{
		return board[x][y];
	}
	
	public int get(PVector v)
	{
		return get((int)v.x, (int)v.y);
	}
	
	public PVector getNext(PVector v)
	{
		int i = (int)v.x+1;
		for(int j = (int)v.y; j<arrayDim; j++)
		{
			for(; i<arrayDim; i++)
				if(get(i, j)>0)
					return new PVector(i, j);
			i = 0;
		}
		return getNext(new PVector(-1, 0));
		// kan oneindig bij leeg bord
	}
	
	public void set(int x, int y, int n)
	{
		board[x][y] = n;
	}
	
	public void set(PVector v, int n)
	{
		set((int)v.x, (int)v.y, n);
	}
	
	public boolean isWin()
	{
		for(PVector goal : goals)
			if(get(goal)!=1)
				return false;
		return true;
	}
	
	public Board copy()
	{
		Board b = new Board(this.gameType, this.arrayDim);
		for(int i = 0; i<b.arrayDim; i++)
			for(int j = 0; j<b.arrayDim; j++)
				b.set(i, j, get(i, j));
		b.goals = goals;
		b.setDepth(depth);
		b.parent = parent;
		return b;
	}
	
	public String[][] toStringArray()
	{
		String[][] strArray = new String[arrayDim][arrayDim];
		for(int y = 0; y<arrayDim; y++)
			for(int x = 0; x<arrayDim; x++)
			{
				strArray[x][y] = Integer.toString(get(x, y));
				for(PVector goal : goals)
					if(goal.x==x && goal.y==y)
						strArray[x][y] += "g";
			}
		return strArray;
	}
	
	public int getArrayDim()
	{
		return arrayDim;
	}
	
	public PVector[] getGoals()
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
	
	public int getDepth()
	{
		return depth;
	}
	
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}
	
	public int[][] getBoard()
	{
		return board;
	}
}
