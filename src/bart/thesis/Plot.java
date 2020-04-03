package bart.thesis;

import processing.core.PApplet;

public class Plot extends PApplet
{
	private final static int BORDER = 20;
	private float[] ints;
	
	public Plot(float[] ints)
	{
		this.ints = ints;
	}
	
	@Override
	public void settings()
	{
		size(500, 500);
	}
	
	@Override
	public void draw()
	{
		background(0);
		fill(255);
		float interX = (width-2*BORDER)/(ints.length-1);
		float interY = (height-2*BORDER)/Util.max(ints);
		for(int i=0; i<ints.length; i++)
			ellipse(BORDER+i*interX, height-(BORDER+ints[i]*interY), 5, 5);
	}
}
