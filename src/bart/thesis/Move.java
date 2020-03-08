package bart.thesis;

public enum Move
{
	UP(0, -1, "U"), UP_LEFT(-1, -1, "UL"), UP_RIGHT(1, -1, "UR"),
	LEFT(-1, 0, "L"), RIGHT(1, 0, "R"), DOWN_LEFT(-1, 1, "DL"),
	DOWN_RIGHT(1, 1, "DR"), DOWN(0, 1, "D");
	
	public Vector v;
	private String string;
	
	private Move(int x, int y, String string)
	{
		this.v = new Vector(x, y);
		this.string = string;
	}
	
	public String toString()
	{
		return this.string;
	}
}