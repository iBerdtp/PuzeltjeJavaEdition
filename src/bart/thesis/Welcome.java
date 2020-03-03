package bart.thesis;

public class Welcome extends TextSection
{
	public Welcome()
	{
		super(null, "New Game (1) or Load (2)?");
	}
	
	public void performWhenDone()
	{
		if(answers[0]==1)
			Main.PROCESSING.setSection(new NewGameSection(this));
		else
			Main.PROCESSING.setSection(new TypeLoadSection(this));
	}
}
