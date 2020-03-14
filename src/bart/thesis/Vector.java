package bart.thesis;

public class Vector
{
	public int x, y;
	
	public Vector(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector add(Vector v)
	{
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector add(Vector v, int n)
	{
		this.x += n*v.x;
		this.y += n*v.y;
		return this;
	}
	
	public Vector sub(Vector v)
	{
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	public Vector sub(Vector v, int n)
	{
		this.x -= n*v.x;
		this.y -= n*v.y;
		return this;
	}
	
	public Vector setTo(Vector v)
	{
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	public Vector copy()
	{
		return new Vector(x, y);
	}
	
	@Override
	public String toString()
	{
		return "["+x+","+y+"]";
	}
	
	@Override
	public boolean equals(Object o)
	{
		Vector v = (Vector)o;
		return v.x==x&&v.y==y;
	}
}