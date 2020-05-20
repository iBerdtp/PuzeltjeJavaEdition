package bart.thesis;

import processing.core.PConstants;

public class DrawSection extends Section
{
	private GameType gameType;
	private int chosenDim;
	private int tool;
	private Board board;
	private int squareSize;
	
	public DrawSection(Section parentSection, GameType gameType, int chosenDim)
	{
		this.parentSection = parentSection;
		this.gameType = gameType;
		this.chosenDim = chosenDim;
		this.tool = 1;
		this.board = new Board(gameType, chosenDim); // niet voor hex
		resize();
		this.squareSize = (Main.PROCESSING.width-2*Processing.BORDER_SIZE)/chosenDim;
	}
	
	public void handleInput()
	{
		if(Main.PROCESSING.KEYS[PConstants.SHIFT] || Main.PROCESSING.KEYS[PConstants.TAB])
			tool = (tool+1)%3;
		if(Main.PROCESSING.KEYS[PConstants.ENTER])
		{
			if(board.isValid())
			{
				Main.PROCESSING.setSection(new SquareGame(this, gameType, board));
				System.out.println("Game started");
			}
			else
				System.out.println("Board invalid");
		}
		if(Main.PROCESSING.KEYS[PConstants.BACKSPACE])
			backToParent();
		if(Main.PROCESSING.KEYS[256])
		{
			int x = bound((Main.PROCESSING.mouseX - Processing.BORDER_SIZE)/squareSize);
			int y = bound((Main.PROCESSING.mouseY - Processing.BORDER_SIZE)/squareSize);
			if(tool == 0)
				if(board.isGoal(x, y))
					board.removeGoal(x, y);
				else
					board.extendGoals(x, y);
			else
				if(board.get(x, y) != tool)
					board.set(x, y, tool);
				else
					board.set(x, y, 0);
		}
		if(Main.PROCESSING.KEYS['C'])
			board = new Board(gameType, chosenDim);
		if(Main.PROCESSING.KEYS['S'])
		{
			BFS bfs = new BFS(board, gameType.getAllowed());
			Board solution = bfs.solution();
			System.out.println(solution.depth);
		}
		if(Main.PROCESSING.KEYS['H'])
		{
			Board solution = SmartBFS.getHardestGoalsSolution(board, gameType.getAllowed(), false);
			board.setGoals(solution.getGoals());
			System.out.println("Hardest solution generated");
		}
	}

	public void iterate()
	{
		Main.PROCESSING.background(Processing.TEXT_BACKGROUND);
		Util.showBoard(gameType, board, Processing.BORDER_SIZE);
		int x = bound((Main.PROCESSING.mouseX - Processing.BORDER_SIZE)/squareSize);
		int y = bound((Main.PROCESSING.mouseY - Processing.BORDER_SIZE)/squareSize);
		if(tool == 1)
			Util.showPawn(0, 0, 100, Processing.BORDER_SIZE, squareSize, x, y);
		if(tool == 2)
			Util.showPawn(0, 100, 0, Processing.BORDER_SIZE, squareSize, x, y);
		if(tool == 0)
			Util.showField(100, Processing.BORDER_SIZE, squareSize, x, y);
	}

	public void resize()
	{
		Util.resizeSquare(chosenDim);
	}
	
	private int bound(int i)
	{
		if(i<0)
			return 0;
		if(i>chosenDim-1)
			return chosenDim-1;
		return i;
	}
}
