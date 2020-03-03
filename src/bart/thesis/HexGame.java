package bart.thesis;

import java.util.ArrayList;
import processing.core.PVector;

public class HexGame extends Game
{
	private int chosenDim;
	
	public HexGame(Section parentSection, int dim, int nrOfGoals, int nrOfPawns, int optimal)
	{
		super(parentSection, GameType.HEX, dim*2-1, nrOfGoals, nrOfPawns, optimal);
	}
	
	public HexGame(Section parentSection, Board board)
	{
		super(parentSection, GameType.HEX, board);
	}
	
	protected void setAdditional()
	{
		this.chosenDim = (arrayDim+1)/2;
		resize();
	}
	
	public void resize()
	{
		Util.resizeHex(arrayDim, chosenDim);
	}
	
	public void showSelected()
	{
		for(int i = 0; i<arrayDim; i++)
			for(int j = 0; j<arrayDim; j++)
				if(selected.x==i && selected.y==j)
				{
					Main.PROCESSING.noFill();
					Main.PROCESSING.strokeWeight(3);
					Main.PROCESSING.stroke(255, 255, 0);
					Main.PROCESSING.ellipse((i+(j-chosenDim+2f)/2)*Processing.REG_SQUARE_SIZE, (float)(0.5+j*Math.sqrt(3)/2)*Processing.REG_SQUARE_SIZE, Processing.REG_SQUARE_SIZE, Processing.REG_SQUARE_SIZE);
				}
	}
	
	protected void fillAccordingly(Board b, int nrOfGoals, int nrOfPawns)
	{
		setBoundaries(b);
		setGoals(b, nrOfGoals);
		setUnits(b, nrOfGoals, nrOfPawns);
	}
	
	private void setBoundaries(Board b)
	{
		for(int y = 0; y<chosenDim-1; y++)
			for(int x = 0; x<chosenDim-1-y; x++)
			{
				b.getBoard()[y][x] = -1;
				b.getBoard()[b.getArrayDim()-1-y][b.getArrayDim()-1-x] = -1;
			}
	}
	
	private void setGoals(Board b, int nrOfGoals)
	{
		ArrayList<PVector> possibleGoals = getPossibleSpots(b, 1);
		b.goals = new PVector[nrOfGoals];
		for(int i = 0; i<nrOfGoals; i++)
			b.goals[i] = Util.getRandom(possibleGoals);
	}
	
	private void setUnits(Board b, int nrOfGoals, int nrOfPawns)
	{
		ArrayList<PVector> spots = getPossibleSpots(b, 0);
		for(int i = 0; i<nrOfPawns; i++)
			b.set(Util.getRandom(spots), 2);
		for(int i = 0; i<spots.size(); i++)
			for(int j = 0; j<b.goals.length; j++)
				if(spots.get(i).equals(b.goals[j]))
				{
					spots.remove(i--);
					break;
				}
		for(int i = 0; i<nrOfGoals; i++)
			b.set(Util.getRandom(spots), 1);
	}
	
	private ArrayList<PVector> getPossibleSpots(Board b, int rimSize)
	{
		ArrayList<PVector> spots = new ArrayList<PVector>();
		for(int y = rimSize; y<b.getArrayDim()-rimSize; y++)
			for(int x = Math.max(rimSize, chosenDim-1-y+rimSize); x<Math.min(b.getArrayDim()-rimSize, 3*chosenDim-2-y-rimSize); x++)
				spots.add(new PVector(x, y));
		return spots;
	}
}