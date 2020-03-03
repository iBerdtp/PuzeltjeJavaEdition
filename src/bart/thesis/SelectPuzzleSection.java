package bart.thesis;

import java.io.File;
import processing.core.PConstants;
import processing.core.PVector;

public class SelectPuzzleSection extends Section
{
	private String puzzlesPath;
	private String[] list;
	private int currentIndex;
	private Board currentOption;
	private int borderSize;
	private GameType gameType;
	
	public SelectPuzzleSection(Section parentSection, File puzzlesDir, GameType gameType)
	{
		this.parentSection = parentSection;
		this.puzzlesPath = puzzlesDir.getPath();
		this.list = puzzlesDir.list();
		this.currentIndex = 0;
		this.currentOption = loadPuzzle(list[currentIndex]);
		this.borderSize = 50;
		this.gameType = gameType;
		resize();
	}
	
	public void resize()
	{
		if(gameType==GameType.SQUARE || gameType==GameType.DIAGONAL)
			Util.resizeSquare(5);
		else if(gameType==GameType.HEX)
			Util.resizeHex(5, 3);
	}
	
	public void handleInput()
	{
		if(Main.PROCESSING.KEYS[PConstants.RIGHT])
			currentOption = loadPuzzle(list[currentIndex = (currentIndex+1)%list.length]);
		if(Main.PROCESSING.KEYS[PConstants.LEFT])
			currentOption = loadPuzzle(list[currentIndex = (currentIndex>0 ? currentIndex-1 : list.length-1)]);
		if(Main.PROCESSING.KEYS[PConstants.ENTER])
			startPuzzle();
		if(Main.PROCESSING.KEYS[PConstants.BACKSPACE])
			backToParent();
	}
	
	public void iterate()
	{
		Main.PROCESSING.background(Processing.TEXT_BACKGROUND);
		Util.showBoard(gameType, currentOption, borderSize);
	}
	
	private Board loadPuzzle(String puz)
	{
		String[] strings = Main.PROCESSING.loadStrings(puzzlesPath+"\\"+puz);
		int dim = Integer.parseInt(strings[0]);
		int[][] intses = new int[dim][dim];
		for(int y = 0; y<dim; y++)
		{
			int[] row = Util.toIntArray(strings[1+y].split(" "));
			for(int x = 0; x<dim; x++)
				intses[x][y] = row[x];
		}
		int nrOfGoals = Integer.parseInt(strings[dim+1]);
		PVector[] goals = new PVector[nrOfGoals];
		for(int i = 0; i<nrOfGoals; i++)
		{
			int[] coordinates = Util.toIntArray(strings[dim+2+i].split(" "));
			goals[i] = new PVector(coordinates[0], coordinates[1]);
		}
		return new Board(gameType, dim, goals, intses);
	}
	
	private void startPuzzle()
	{
		if(gameType==GameType.SQUARE)
			Main.PROCESSING.setSection(new SquareGame(this, GameType.SQUARE, currentOption));
		else if(gameType==GameType.HEX)
			Main.PROCESSING.setSection(new HexGame(this, currentOption));
		else if(gameType==GameType.DIAGONAL)
			Main.PROCESSING.setSection(new SquareGame(this, GameType.DIAGONAL, currentOption));
	}
	
}
