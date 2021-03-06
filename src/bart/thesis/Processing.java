package bart.thesis;

import java.io.File;
import java.io.PrintWriter;
import java.util.Comparator;
import processing.core.PApplet;

public class Processing extends PApplet
{
	private Section section;
	public boolean[] KEYS;
	final static String SAVES_PATH = "C:\\Users\\Bert\\Documents\\1920\\scripsie\\Puzeltje\\saves";
	final static File SAVES_DIR = new File(SAVES_PATH);
	final static float ELLIPSE_FACTOR = 0.8f;
	final static int REG_SQUARE_SIZE = 100;
	final static int TEXT_BACKGROUND = 75;
	final static int BORDER_SIZE = 50;
	final static Comparator<int[][]> COMPARATOR = new Comparator<int[][]>()
	{
		// assumes same dimensions
		@Override
		public int compare(int[][] a, int[][] b)
		{
			for(int y = 0; y<a.length; y++)
				for(int x = 0; x<a[y].length; x++)
					if(a[x][y]==b[x][y])
						continue;
					else
						return a[x][y]-b[x][y];
			return 0;
		}
	};
	final static Comparator<int[][]> GOAL_COMPARATOR = new Comparator<int[][]>()
	{
		// assumes same dimensions
		@Override
		public int compare(int[][] a, int[][] b)
		{
			for(int y = 0; y<a.length; y++)
				for(int x = 0; x<a[y].length; x++)
					if(a[x][y]==1&&b[x][y]!=1)
						return 1;
					else if(b[x][y]==1&&a[x][y]!=1)
						return -1;
			return 0;
		}
	};
	
	public void settings()
	{
		size(100, 100);
	}
	
	public void setup()
	{
		resetKeys();
		setSection(new Welcome());
	}
	
	public void draw()
	{
		section.superIterate();
	}
	
	public void keyPressed()
	{
		if(keyCode==ESC)
			System.exit(0);
		if(keyCode=='R')
			setup();
		else
			KEYS[keyCode] = true;
	}
	
	public void mousePressed()
	{
		KEYS[256] = true;
	}
	
	public void setSection(Section section)
	{
		this.section = section;
	}
	
	public void setSurfaceSize(int w, int h)
	{
		surface.setSize(w, h);
	}
	
	public void resetKeys()
	{
		this.KEYS = new boolean[257];
	}
	
	public void savePuzzle(Board initial, GameType gameType)
	{
		File dir = new File(SAVES_DIR,gameType+"\\"+initial.getDifficulty());
		String fileName = dir.getPath()+"\\";
		if(dir.isDirectory() && dir.list().length>0)
		{
			String[] list = dir.list();
			fileName += Integer.parseInt(list[list.length-1].substring(0, 1))+1;
		}
		else
			fileName += 0;
		fileName += ".puz";
		Vector[] goals = initial.getGoals();
		PrintWriter output = createWriter(fileName);
		
		output.println(initial.getArrayDim());
		output.println(initial);
		output.println(goals.length);
		for(Vector v : goals)
			output.println((int)v.x+" "+(int)v.y);
		output.close();
		
		println("saved to: "+fileName);
	}
}