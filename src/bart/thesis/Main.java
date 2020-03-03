package bart.thesis;

import processing.core.PApplet;

public class Main
{
	public static final Processing PROCESSING = new Processing();
	
	public static void main(String[] args)
	{
		String[] processingArgs = {"MySketch"};
		PApplet.runSketch(processingArgs, PROCESSING);
	}
}