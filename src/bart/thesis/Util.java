package bart.thesis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import processing.core.PConstants;

public class Util
{
	private final static Random random = new Random();
	
	public static <T> T removeRandom(ArrayList<T> list)
	{
		return list.remove(random.nextInt(list.size()));
	}
	
	public static <T> T getRandom(T[] array)
	{
		return array[random.nextInt(array.length)];
	}
	
	public static <T> T sample(ArrayList<T> list, float[] p)
	{
		float r = random.nextFloat();
		for(int i=0; i<p.length; i++)
		{
			if(r<p[i])
				return list.get(i);
			r-=p[i];
		}
		return null;
	}
	
	public static int getRandomIntIncl(int lowIncl, int maxIncl)
	{
		return random.nextInt(maxIncl+1-lowIncl)+lowIncl;
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
			if(e==i)
				return true;
		return false;
	}
	
	public static <T> boolean contains(T[] a, T t)
	{
		for(T e : a)
			if(e.equals(a))
				return true;
		return false;
	}
	
	public static int max(ArrayList<Integer> ints)
	{
		int max = ints.get(0);
		for(int i=1; i<ints.size(); i++)
			if(ints.get(i)>max)
				max = ints.get(i);
		return max;
	}
	
	public static float max(float[] ints)
	{
		float max = ints[0];
		for(int i=1; i<ints.length; i++)
			if(ints[i]>max)
				max = ints[i];
		return max;
	}
	
	public static ArrayList<Integer> sequence(int upperExcl)
	{
		return sequence(upperExcl, 0);
	}
	
	public static ArrayList<Integer> sequence(int upperExcl, int n)
	{
		ArrayList<Integer> sequence = new ArrayList<Integer>(upperExcl);
		for(int i=0; i<upperExcl; i++)
			sequence.add(i+n);
		return sequence;
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
	
	public static int[][] copy(int[][] original)
	{
		int dim = original.length;
		int[][] copy = new int[dim][dim];
		for(int y=0; y<dim; y++)
			for(int x=0; x<dim; x++)
				copy[x][y] = original[x][y];
		return copy;
	}
	
	public static void setHexBoundaries(int[][] b)
	{
		int arrayDim = b.length;
		for(int y = 0; y<arrayDim/2; y++)
			for(int x = 0; x<arrayDim/2-y; x++)
			{
				b[x][y] = -1;
				b[arrayDim-1-x][arrayDim-1-y] = -1;
			}
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
				int sat = 0;
				for(Vector goal : current.getGoals())
					if(goal.x==i && goal.y==j)
						sat = 255;
				Main.PROCESSING.stroke(Processing.TEXT_BACKGROUND);
				showField(sat, borderSize, squareSize, i, j);
				if(current.get(i, j)==1)
					showPawn(0, 0, 255, borderSize, squareSize, i, j);
				else if(current.get(i, j)>1)
					showPawn(0, 255, 0, borderSize, squareSize, i, j);
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
	
	public static void showPawn(int r, int g, int b, int borderSize, int squareSize, int i, int j)
	{
		Main.PROCESSING.noStroke();
		Main.PROCESSING.fill(r, g, b);
		Main.PROCESSING.ellipse(borderSize+i*squareSize, borderSize+j*squareSize, squareSize, squareSize);
	}
	
	public static void showField(int sat, int borderSize, int squareSize, int i, int j)
	{
		Main.PROCESSING.fill(sat, 0, 0);
		Main.PROCESSING.rect(borderSize+i*squareSize, borderSize+j*squareSize, squareSize, squareSize);
	}
}