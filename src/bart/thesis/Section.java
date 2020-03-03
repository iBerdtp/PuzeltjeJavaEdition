package bart.thesis;

public abstract class Section
{
	Section parentSection;
	
	public void backToParent()
	{
		Main.PROCESSING.setSection(parentSection);
		parentSection.resize();
	}
	
	public void superIterate()
	{
		handleInput();
		Main.PROCESSING.resetKeys();
		iterate();
	}
	
	public abstract void handleInput();
	public abstract void iterate();
	public abstract void resize();
}