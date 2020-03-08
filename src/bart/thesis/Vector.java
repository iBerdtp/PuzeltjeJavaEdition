package bart.thesis;

public class Vector
{
	public int x, y;
	
	public Vector(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static Vector copy(Vector v)
	{
		return new Vector(v.x, v.y);
	}
	
	public Vector add(Vector v)
	{
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector sub(Vector v)
	{
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	public Vector setTo(Vector v)
	{
		this.x = v.x;
		this.y = v.y;
		return this;
	}
}