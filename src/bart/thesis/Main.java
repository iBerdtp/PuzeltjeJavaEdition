package bart.thesis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import processing.core.PApplet;

public class Main
{
	public static final Processing PROCESSING = new Processing();
	
	public static final GameType GAME_TYPE = GameType.SQUARE;
	public static final int CHOSEN_DIM = 5;
	public static final Vector[] GOALS = {new Vector(1, 2), new Vector(2, 1), new Vector(2, 3)};
	public static final int CAP = CHOSEN_DIM*CHOSEN_DIM-GOALS.length;
	
	public static void main(String[] args)
	{
		String[] processingArgs = {"MySketch"};
		PApplet.runSketch(processingArgs, PROCESSING);
		
//		doeHet();
	}
	
	public static void doeHet()
	{
		try
		{
			String fileName = getFileName();
			FileWriter fw = new FileWriter("C:\\Users\\Bert\\Documents\\1920\\scripsie\\BE 2.0\\auto\\"+fileName);
			ArrayList<Integer> gods = new ArrayList<Integer>();
			for(int i = 0; i<=CAP; i++)
				gods.add(god(GAME_TYPE, CHOSEN_DIM, GOALS, i, fw));
			String godsString = getGodsString(gods);
			printlnWrite(godsString, fw);
			fw.close();
		} catch(IOException e)
		{
		}
	}
	
	public static int god(GameType gameType, int chosenDim, Vector[] goals, int nrOfPawns, FileWriter fw) throws IOException
	{
		BackwardsExhaustive fe = new BackwardsExhaustive(gameType, chosenDim, goals, nrOfPawns);
		int t0 = PROCESSING.millis();
		ArrayList<int[][]> hardests = fe.getHardests();
		int dur = PROCESSING.millis()-t0;
		String report = getReport(gameType, chosenDim, nrOfPawns, goals, fe.getGod(), hardests.size(), dur, Util.removeRandom(hardests));
		printlnWrite(report, fw);
		return fe.getGod();
	}
	
	public static int check(GameType gameType, int chosenDim, Vector[] goals, int[][] grid)
	{
		Board b = new Board(gameType, chosenDim, goals, grid);
		BFS bfs = new BFS(b, gameType.getAllowed());
		Board solution = bfs.solution();
		if(solution!=null)
			return solution.getDepth();
		return -1;
	}
	
	public static String getReport(GameType gameType, int chosenDim, int nrOfPawns, Vector[] goals, int god, int nrOfExamplars, int millis, int[][] example)
	{
		StringBuilder sb = new StringBuilder(gameType+", dim: "+chosenDim+", pawns: "+nrOfPawns+"\n");
		sb.append("Goals: ");
		for(Vector goal : goals)
			sb.append(goal+", ");
		sb.append("\n\n");
		sb.append("God: "+god+"\n");
		sb.append("Examplars: "+nrOfExamplars+"\n");
		sb.append("time: "+millis/1000f+"s\n\n");
		int[][] intses = example;
		for(int y = 0; y<intses.length; y++)
		{
			for(int x = 0; x<intses[y].length; x++)
				sb.append(intses[x][y]+" ");
			sb.append("\n");
		}
		sb.append("________________________________________________");
		return sb.toString();
	}
	
	public static String getGodsString(ArrayList<Integer> gods)
	{
		StringBuilder sb = new StringBuilder();
		for(int god : gods)
			sb.append(god+" ");
		return sb.toString();
	}
	
	public static String getFileName()
	{
		StringBuilder sb = new StringBuilder(GAME_TYPE+" dim"+CHOSEN_DIM+" g");
		for(Vector goal : GOALS)
			sb.append(goal);
		sb.append(".txt");
		return sb.toString();
	}
	
	public static void printlnWrite(String s, FileWriter fw) throws IOException
	{
		System.out.print(s+"\n");
		fw.write(s+"\n");
	}
}