package bart.thesis;

import java.util.ArrayList;
import java.util.Random;
import processing.core.PConstants;

public class Util
{
	private final static Random random = new Random();
	
	public static Vector getRandom(ArrayList<Vector> vectors)
	{
		return vectors.remove(random.nextInt(vectors.size()));
	}
	
	public static void resizeSquare(int arrayDim)
	{
		Main.PROCESSING.setSurfaceSize(arrayDim*Processing.REG_SQUARE_SIZE, arrayDim*Processing.REG_SQUARE_SIZE);
	}
	
	public static void resizeHex(int arrayDim, int chosenDim)
	{
		Main.PROCESSING.setSurfaceSize(arrayDim*Processing.REG_SQUARE_SIZE, (int)((Math.sqrt(3)*(chosenDim-1)+1)*Processing.REG_SQUARE_SIZE));
	}
	
	public static boolean contains(int[] a, int i)
	{
		for(int e : a)
			if(i==e)
				return true;
		return false;
	}
	
	public static int[] toIntArray(String[] strings)
	{
		int[] ints = new int[strings.length];
		for(int i = 0; i<strings.length; i++)
			ints[i] = Integer.parseInt(strings[i]);
		return ints;
	}
	
	public static String[] toStringArray(int[] ints)
	{
		String[] strings = new String[ints.length];
		for(int i = 0; i<ints.length; i++)
			strings[i] = Integer.toString(ints[i]);
		return strings;
	}
	
	public static void showBoard(GameType gameType, Board current, int borderSize)
	{
		Main.PROCESSING.strokeWeight(2);
		if(gameType==GameType.SQUARE || gameType==GameType.DIAGONAL)
			showSquareBoard(current, borderSize);
		else if(gameType==GameType.HEX)
			showHexBoard(current, borderSize);
	}
	
	public static void showSquareBoard(Board current, int borderSize)
	{
		Main.PROCESSING.ellipseMode(PConstants.CORNER);
		int arrayDim = current.getArrayDim();
		int squareSize = (Main.PROCESSING.width-2*borderSize)/arrayDim;
		for(int i = 0; i<arrayDim; i++)
			for(int j = 0; j<arrayDim; j++)
			{
				Main.PROCESSING.fill(0);
				for(Vector goal : current.getGoals())
					if(goal.x==i && goal.y==j)
						Main.PROCESSING.fill(255, 0, 0);
				Main.PROCESSING.stroke(Processing.TEXT_BACKGROUND);
				Main.PROCESSING.rect(borderSize+i*squareSize, borderSize+j*squareSize, squareSize, squareSize);
				if(current.get(i, j)==1)
					drawPionnetje(0, 0, 255, borderSize, squareSize, i, j);
				else if(current.get(i, j)>1)
					drawPionnetje(0, 255, 0, borderSize, squareSize, i, j);
			}
	}
	
	public static void showHexBoard(Board current, int borderSize)
	{
		Main.PROCESSING.ellipseMode(PConstants.CENTER);
		int arrayDim = current.getArrayDim();
		int chosenDim = (arrayDim+1)/2;
		int squareSize = (Main.PROCESSING.width-2*borderSize)/arrayDim;
		for(int i = 0; i<arrayDim; i++)
			for(int j = 0; j<arrayDim; j++)
			{
				if(current.get(i, j)!=-1)
				{
					Main.PROCESSING.fill(0);
					for(Vector goal : current.getGoals())
						if(goal.x==i && goal.y==j)
							Main.PROCESSING.fill(255, 0, 0);
					Main.PROCESSING.stroke(Processing.TEXT_BACKGROUND);
					Main.PROCESSING.ellipse(borderSize+(i+(j-chosenDim+2f)/2)*squareSize, borderSize+(0.5f+j*(float)Math.sqrt(3)/2)*squareSize, squareSize, squareSize);
					if(current.get(i, j)==1)
					{
						Main.PROCESSING.noStroke();
						Main.PROCESSING.fill(0, 0, 255);
						Main.PROCESSING.ellipse(borderSize+(i+(j-chosenDim+2f)/2)*squareSize, borderSize+(0.5f+j*(float)Math.sqrt(3)/2)*squareSize, Processing.ELLIPSE_FACTOR*squareSize, Processing.ELLIPSE_FACTOR*squareSize);
					}
					else if(current.get(i, j)>1)
					{
						Main.PROCESSING.noStroke();
						Main.PROCESSING.fill(0, 255, 0);
						Main.PROCESSING.ellipse(borderSize+(i+(j-chosenDim+2f)/2)*squareSize, borderSize+(0.5f+j*(float)Math.sqrt(3)/2)*squareSize, Processing.ELLIPSE_FACTOR*squareSize, Processing.ELLIPSE_FACTOR*squareSize);
					}
				}
			}
	}
	
	private static void drawPionnetje(int r, int g, int b, int borderSize, int squareSize, int i, int j)
	{
		Main.PROCESSING.noStroke();
		Main.PROCESSING.fill(r, g, b);
		Main.PROCESSING.ellipse(borderSize+i*squareSize, borderSize+j*squareSize, squareSize, squareSize);
	}
}
