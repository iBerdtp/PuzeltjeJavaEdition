package bart.thesis;

import java.util.ArrayList;

public class SquareGame extends Game
{
	public SquareGame(Section parentSection, GameType gameType, int dim, int nrOfGoals, int nrOfPawns, int optimal)
	{
		super(parentSection, gameType, dim, nrOfGoals, nrOfPawns, optimal);
	}
	
	public SquareGame(Section parentSection, GameType gameType, Board board)
	{
		super(parentSection, gameType, board);
	}
	
	protected void showSelected()
	{
		for(int i = 0; i<arrayDim; i++)
			for(int j = 0; j<arrayDim; j++)
				if(selected.x==i && selected.y==j)
				{
					Main.PROCESSING.noFill();
					Main.PROCESSING.strokeWeight(3);
					Main.PROCESSING.stroke(255, 255, 0);
					Main.PROCESSING.rect(i*Processing.REG_SQUARE_SIZE, j*Processing.REG_SQUARE_SIZE, Processing.REG_SQUARE_SIZE, Processing.REG_SQUARE_SIZE);
				}
	}
	
	protected void fillAccordingly(Board b, int nrOfGoals, int nrOfPawns)
	{
		setGoals(b, nrOfGoals);
		setUnits(b, nrOfGoals, nrOfPawns);
	}
	
	private void setGoals(Board b, int nrOfGoals)
	{
		ArrayList<Vector> possibleGoals = getPossibleSpots(b, 1);
		Vector[] goals = new Vector[nrOfGoals];
		for(int i = 0; i<nrOfGoals; i++)
			goals[i] = Util.getRandom(possibleGoals);
		b.setGoals(goals);
	}
	
	private void setUnits(Board b, int nrOfGoals, int nrOfPawns)
	{
		ArrayList<Vector> spots = getPossibleSpots(b, 0);
		Vector[] goals = b.getGoals();
		for(int i = 0; i<nrOfPawns; i++)
			b.set(Util.getRandom(spots), 2);
		for(int i = 0; i<spots.size(); i++)
			for(int j = 0; j<goals.length; j++)
				if(spots.get(i).equals(goals[j]))
				{
					spots.remove(i--);
					break;
				}
		for(int i = 0; i<nrOfGoals; i++)
			b.set(Util.getRandom(spots), 1);
	}
	
	private ArrayList<Vector> getPossibleSpots(Board b, int rimSize)
	{
		ArrayList<Vector> spots = new ArrayList<Vector>();
		for(int y = rimSize; y<b.getArrayDim()-rimSize; y++)
			for(int x = rimSize; x<b.getArrayDim()-rimSize; x++)
				spots.add(new Vector(x, y));
		return spots;
	}
	
	protected void setAdditional()
	{
		resize();
	}
	
	public void resize()
	{
		Util.resizeSquare(arrayDim);
	}
}
