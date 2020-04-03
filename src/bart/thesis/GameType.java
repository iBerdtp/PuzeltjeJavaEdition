package bart.thesis;

import processing.core.PConstants;

public enum GameType
{
	SQUARE("Square", new Move[]{Move.UP, Move.LEFT, Move.RIGHT, Move.DOWN},
			new int[]{PConstants.UP, PConstants.LEFT, PConstants.RIGHT, PConstants.DOWN}),
	HEX("Hex",
			new Move[]{Move.UP, Move.LEFT, Move.DOWN_LEFT, Move.UP_RIGHT, Move.RIGHT, Move.DOWN},
			new int[]{36, 37, 35, 33, 39, 34}),
	DIAGONAL("Diagonal",
			new Move[]{Move.UP_LEFT, Move.UP, Move.UP_RIGHT, Move.LEFT, Move.RIGHT, Move.DOWN_LEFT, Move.DOWN, Move.DOWN_RIGHT},
			new int[]{36, 38, 33, 37, 39, 35, 40, 34});
	
	private String string;
	private Move[] allowed;
	private int[] moveControls;
	
	private GameType(String string, Move[] allowed, int[] moveControls)
	{
		this.string = string;
		this.allowed = allowed;
		this.moveControls = moveControls;
	}
	
	public String toString()
	{
		return string;
	}
	
	public Move[] getAllowed()
	{
		return allowed;
	}
	
	public int[] getMoveControls()
	{
		return moveControls;
	}
	
	public static GameType toType(String s)
	{
		for(GameType gt : values())
			if(s.equals(gt.toString()))
				return gt;
		return null;
	}
	
	public int[][] getEmpty(int arrayDim)
	{
		int[][] empty = new int[arrayDim][arrayDim];
		if(this==HEX)
			Util.setHexBoundaries(empty);
		return empty;
	}
}