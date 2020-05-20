package bart.thesis;

public class Welcome extends TextSection
{
	public Welcome()
	{
		super(null, "New Game (1), Load (2), or Draw(3)?");
	}
	
	public void performWhenDone()
	{
		if(answers[0]==1)
			Main.PROCESSING.setSection(new NewGameSection(this));
		else if(answers[0]==2)
			Main.PROCESSING.setSection(new TypeLoadSection(this));
		else
			Main.PROCESSING.setSection(new DrawSection(this, GameType.SQUARE, 5));
	}
}
