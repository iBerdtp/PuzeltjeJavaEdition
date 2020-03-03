package bart.thesis;

public class NewGameSection extends TextSection
{
	public NewGameSection(Section parentSection)
	{
		super(parentSection, "Square(1), Hex(2) or Diagonal(3)?", "Size?", "Number of goals?", "Number of pawns?", "Difficulty?");
	}
	
	public void performWhenDone()
	{
		openGame(answers[0], answers[1], answers[2], answers[3], answers[4]);
	}
	
	public void openGame(int type, int dim, int nrOfGoals, int nrOfPawns, int optimal)
	{
		if(type==1)
			Main.PROCESSING.setSection(new SquareGame(this, GameType.SQUARE, dim, nrOfGoals, nrOfPawns, optimal));
		else if(type==2)
			Main.PROCESSING.setSection(new HexGame(this, dim, nrOfGoals, nrOfPawns, optimal));
		else
			Main.PROCESSING.setSection(new SquareGame(this, GameType.DIAGONAL, dim, nrOfGoals, nrOfPawns, optimal));
	}
}
