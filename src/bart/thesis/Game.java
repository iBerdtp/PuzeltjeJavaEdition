package bart.thesis;

import java.util.HashMap;
import processing.core.PConstants;
import processing.data.IntList;

public abstract class Game extends Section
{
	private GameType gameType;
	protected int arrayDim;
	private int nrOfGoals;
	private int nrOfPawns;
	private int chosenDif;
	private Board initial;
	private Board current;
	protected Vector selected;
	private boolean won;
	private Move[] allowed;
	private int[] moveControls;
	private HashMap<Integer, Move> map;
	private boolean newGame;
	private boolean alreadySaved;
	private IntList possibleDifs;
	
	private Game(Section parentSection, GameType gameType, int arrayDim)
	{
		this.parentSection = parentSection;
		this.gameType = gameType;
		this.arrayDim = arrayDim;
		this.allowed = gameType.getAllowed();
		this.moveControls = gameType.getMoveControls();
		this.alreadySaved = true;
		this.possibleDifs = new IntList();
	}
	
	public Game(Section parentSection, GameType gameType, int arrayDim, int nrOfGoals, int nrOfPawns, int chosenDif)
	{
		this(parentSection, gameType, arrayDim);
		this.nrOfGoals = nrOfGoals;
		this.nrOfPawns = nrOfPawns;
		this.chosenDif = chosenDif;
		this.newGame = true;
		setMap(moveControls, allowed);
		setAdditional();
		createNewPuzzle();
	}
	
	public Game(Section parentSection, GameType gameType, Board board)
	{
		this(parentSection, gameType, board.getArrayDim());
		this.newGame = false;
		setMap(moveControls, allowed);
		setAdditional();
		setBoard(board);
	}
	
	public void iterate()
	{
		Main.PROCESSING.background(Processing.TEXT_BACKGROUND);
		gameStuff();
	}
	
	public void gameStuff()
	{
		Util.showBoard(gameType, current, 0);
		showSelected();
		if(!won)
			checkForWin();
	}
	
	public void handleInput()
	{
		if(!won)
			if(Main.PROCESSING.KEYS[PConstants.SHIFT] || Main.PROCESSING.KEYS[PConstants.TAB])
				selected = current.getNext(selected);
			else
				for(int c : moveControls)
					if(Main.PROCESSING.KEYS[c])
					{
						current.move(selected, map.get(c));
						break;
					}
				
		if(Main.PROCESSING.KEYS[PConstants.ENTER])
			if(newGame)
				createNewPuzzle();
			else
				Main.PROCESSING.setSection(parentSection);
			
		if(Main.PROCESSING.KEYS[PConstants.BACKSPACE])
			backToParent();
		
		if(Main.PROCESSING.KEYS['Z'])
			reset();
		
		if(Main.PROCESSING.KEYS['S'] && !alreadySaved)
		{
			Main.PROCESSING.savePuzzle(initial, gameType);
			alreadySaved = true;
		}
	}
	
	private void createNewPuzzle()
	{
		Vector[] goals = {new Vector(arrayDim/2, arrayDim/2)};
		
//		initial = BackwardsGenerator.generate(gameType, arrayDim, goals, nrOfPawns, chosenDif);
		initial = generate(arrayDim, nrOfGoals, nrOfPawns, chosenDif, allowed);
		alreadySaved = false;
		reset();
	}
	
	private void setBoard(Board board)
	{
		initial = board;
		reset();
	}
	
	private void reset()
	{
		current = initial.copy();
		won = false;
		for(int y = 0; y<current.getArrayDim(); y++)
			for(int x = 0; x<current.getArrayDim(); x++)
				if(current.get(x, y)==1)
				{
					selected = new Vector(x, y);
					return;
				}
	}
	
	private void checkForWin()
	{
		if(current.isWin())
		{
			System.out.println("won in "+current.getDepth()+" moves");
			won = true;
		}
	}
	
	private void setMap(int[] controls, Move[] allowed)
	{
		if(controls.length!=allowed.length)
			throw new IllegalArgumentException("wrong controls move thing");
		map = new HashMap<Integer, Move>();
		for(int i = 0; i<allowed.length; i++)
			map.put(controls[i], allowed[i]);
	}
	
	private Board generate(int arrayDim, int nrOfGoals, int nrOfPawns, int optimal, Move[] allowed)
	{
		System.out.println();
		int tried = 0;
		int t0 = Main.PROCESSING.millis();
		int checkLimit = 10000;
		while(true)
		{
			tried++;
			if(tried==checkLimit)
				System.out.println("tried "+checkLimit+" in "+(Main.PROCESSING.millis()-t0)/1000f+"s");
			Board board = randomBoard(arrayDim, nrOfGoals, nrOfPawns);
			BFS bfs = new BFS(board, allowed);
			Board solution = bfs.solution();
			if(solution!=null && !possibleDifs.hasValue(solution.getDepth()))
			{
				possibleDifs.append(solution.getDepth());
				possibleDifs.sort();
				System.out.println("possible difs: "+possibleDifs);
			}
			if(solution!=null && solution.getDepth()>=optimal)
			{
				System.out.println("tried: "+tried);
				System.out.println("solvable in: "+solution.getDepth());
				board.setDepth(0);
				board.setDifficulty(solution.getDepth());
				return board;
			}
		}
	}
	
	private Board randomBoard(int dim, int nrOfGoals, int nrOfPawns)
	{
		Board b = new Board(gameType, dim);
		fillAccordingly(b, nrOfGoals, nrOfPawns);
		return b;
	}
	
	private void printLooking()
	{
		Main.PROCESSING.textAlign(PConstants.CENTER, PConstants.CENTER);
		Main.PROCESSING.text("Looking for match...\nFound puzzles in range:\n[", Main.PROCESSING.width/2, Main.PROCESSING.height/2);
	}
	
	protected abstract void setAdditional();
	protected abstract void fillAccordingly(Board b, int nrOfGoals, int nrOfPawns);
	protected abstract void showSelected();
}