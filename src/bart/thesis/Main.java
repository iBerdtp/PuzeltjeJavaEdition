package bart.thesis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main
{
	public static final Processing PROCESSING = new Processing();
	
	public static final GameType GAME_TYPE = GameType.SQUARE;
	public static final int CHOSEN_DIM = 5;
	public static final Vector[] GOALS = {new Vector(1,1)};
	public static final int CAP = 6;//CHOSEN_DIM*CHOSEN_DIM-GOALS.length;
	public static final Class ANALYSER_CLASS = BackwardsExhaustive.class;
	
	public static void main(String[] args)
	{
		
//		PApplet.runSketch(new String[]{"MySketch"}, PROCESSING);
		fullAnalysis();
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void fullAnalysis()
	{
		try
		{
			StringBuilder txtSB = new StringBuilder();
			StringBuilder csvSB = new StringBuilder();
			StringBuilder initialsSB = new StringBuilder();
			StringBuilder solvedsSB = new StringBuilder();
			
			ArrayList<Integer> gods = new ArrayList<Integer>();
			for(int i = 0; i<=CAP; i++)
				gods.add(god(GAME_TYPE, CHOSEN_DIM, GOALS, i, txtSB, csvSB, initialsSB, solvedsSB));
			String godsString = getGodsString(gods);
			printlnWrite(godsString, txtSB);
			
			writeFile("report", txtSB);
			writeFile("solveds", solvedsSB);
			writeFile("initials", initialsSB);
			
			FileWriter fwCSV = new FileWriter("/Users/bart/Documents/1920/scripsie/logs/"+getFileName("csv","csv"));
			fwCSV.append("-");
			int godGod = Util.max(gods);
			for(int i=0; i<=godGod; i++)
				fwCSV.append(","+i);
			fwCSV.append("\n"+csvSB.toString());
			fwCSV.close();
		} catch(IOException e)
		{
			System.out.println("ies nie glukt met de fail");
		}
	}
	
	public static int god(GameType gameType, int chosenDim, Vector[] goals, int nrOfPawns, StringBuilder reportSB, StringBuilder csvSB, StringBuilder initialsSB, StringBuilder solvedsSB) throws IOException
	{
		Analyser analyser = null;
		try {
			analyser = (Analyser) ANALYSER_CLASS.getConstructors()[0].newInstance(gameType, chosenDim, goals, nrOfPawns);
		} catch (Exception e) {
			System.out.println("Analyser probleempje");
		}
		int t0 = PROCESSING.millis();
		analyser.calculate();
		int dur = PROCESSING.millis()-t0;
		
		// initials & solveds
		ArrayList<int[][]> hardests = analyser.getHardests();
		ArrayList<int[][]> hardestsSolved = analyser.getHardestsSolved();
		for(int i=0; i<hardests.size(); i++)
		{
			for(int y=0; y<CHOSEN_DIM; y++)
			{
				for(int x=0; x<CHOSEN_DIM; x++)
				{
					printWrite(hardests.get(i)[x][y]+" ", initialsSB);
					printWrite(hardestsSolved.get(i)[x][y]+" ", solvedsSB);
				}
				printWrite("\n", initialsSB);
				printWrite("\n", solvedsSB);
			}
			printWrite("----------------------------\n", initialsSB);
			printWrite("----------------------------\n", solvedsSB);
		}
		
		// csv
		ArrayList<Integer> distribution = analyser.getDistribution();
		int unsolvableCount = analyser.getUnsolvableCount();
		csvSB.append(""+unsolvableCount);
		for(int entry:distribution)
			csvSB.append(","+entry);
		csvSB.append("\n");

		// report
		int god = analyser.getGod();
		String report = getReport(gameType, chosenDim, nrOfPawns, goals, god, dur, Util.removeRandom(hardests), distribution, unsolvableCount);
		printlnWrite(report, reportSB);
		return god;
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
	
	public static String getReport(GameType gameType, int chosenDim, int nrOfPawns, Vector[] goals, int god, int millis, int[][] example, ArrayList<Integer> distribution, int unsolvableCount)
	{
		StringBuilder sb = new StringBuilder(gameType+", dim: "+chosenDim+", pawns: "+nrOfPawns+"\n");
		sb.append("Goals: ");
		for(Vector goal : goals)
			sb.append(goal+", ");
		sb.append("\n\n");
		sb.append("God: "+god+"\n");
		int nrOfExamplars = distribution.get(distribution.size()-1);
		sb.append("Examplars: "+nrOfExamplars+"\n");
		sb.append("time: "+millis/1000f+"s\n\n");
		int[][] intses = example;
		for(int y = 0; y<intses.length; y++)
		{
			for(int x = 0; x<intses[y].length; x++)
				sb.append(intses[x][y]+" ");
			sb.append("\n");
		}
		sb.append("\nDistribution:\n-");
		for(int i=0; i<distribution.size(); i++)
			sb.append("\t"+i);
		sb.append("\n"+unsolvableCount);
		for(int i=0; i<distribution.size(); i++)
			sb.append("\t"+distribution.get(i));
		sb.append("\n________________________________________________");
		return sb.toString();
	}
	
	public static String getGodsString(ArrayList<Integer> gods)
	{
		StringBuilder sb = new StringBuilder();
		for(int god : gods)
			sb.append(god+" ");
		return sb.toString();
	}
	
	public static String getFileName(String addition, String ext)
	{
		StringBuilder sb = new StringBuilder(GAME_TYPE+" dim"+CHOSEN_DIM+" g");
		for(Vector goal : GOALS)
			sb.append(goal);
		sb.append(" "+abreev(ANALYSER_CLASS.getSimpleName())+" "+addition+"."+ext);
		return sb.toString();
	}
	
	public static void printlnWrite(String s, StringBuilder sb) throws IOException
	{
		printWrite(s+"\n", sb);
	}
	
	public static void printWrite(String s, StringBuilder sb) throws IOException
	{
		System.out.print(s);
		sb.append(s);
	}
	
	public static void writeFile(String addition, StringBuilder sb) throws IOException
	{
		FileWriter fw = new FileWriter("/Users/bart/Documents/1920/scripsie/logs/"+getFileName(addition, "txt"));
		fw.write(sb.toString());
		fw.close();
	}
	
	public static String abreev(String original)
	{
		String abreeved = "";
		for(char c : original.toCharArray())
			if(Character.isUpperCase(c))
				abreeved += c;
		return abreeved;
	}
}