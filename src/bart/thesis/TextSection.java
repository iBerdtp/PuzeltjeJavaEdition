package bart.thesis;

import processing.core.PConstants;

public abstract class TextSection extends Section
{
	protected int textSize;
	protected String[] questions;
	protected int[] answers;
	protected String text;
	private int index;
	
	public TextSection(Section parentSection, String... questions)
	{
		this.parentSection = parentSection;
		this.textSize = 32;
		Main.PROCESSING.textSize(textSize);
		this.questions = questions;
		resize();
		this.answers = new int[questions.length];
		this.text = "";
		this.index = 0;
	}
	
	public void resize()
	{
		Main.PROCESSING.setSurfaceSize(600, Math.max(4, (questions.length+2))*textSize);
	}
	
	public void handleInput()
	{
		if(Main.PROCESSING.KEYS[PConstants.ENTER] && text.length()>0)
		{
			answers[index++] = Integer.parseInt(text);
			text = "";
		}
		else
		{
			for(int i = 48; i<58; i++)
				if(Main.PROCESSING.KEYS[i])
					text += i-48;
			if(Main.PROCESSING.KEYS[PConstants.BACKSPACE])
			{
				if(!text.equals(""))
					text = "";
				else if(index>0)
					index--;
				else if(parentSection!=null)
					backToParent();
			}
		}
	}
	
	public void iterate()
	{
		if(index==questions.length)
		{
			performWhenDone();
			index--;
		}
		else
		{
			Main.PROCESSING.fill(255);
			Main.PROCESSING.background(Processing.TEXT_BACKGROUND);
			if(Main.PROCESSING.focused)
			{
				printAnswered();
				printCurrent();
			}
			else
				printClick();
		}
	}
	
	public abstract void performWhenDone();
	
	private void printAnswered()
	{
		Main.PROCESSING.textAlign(PConstants.LEFT, PConstants.TOP);
		for(int i = 0; i<index; i++)
			Main.PROCESSING.text(questions[i]+" "+answers[i], 0, i*textSize);
	}
	
	private void printCurrent()
	{
		Main.PROCESSING.textAlign(PConstants.CENTER);
		Main.PROCESSING.text(questions[index]+" "+text, Main.PROCESSING.width/2, Main.PROCESSING.height-textSize/2);
	}
	
	private void printClick()
	{
		Main.PROCESSING.textAlign(PConstants.CENTER);
		Main.PROCESSING.text("CLICK IN SCREEN", Main.PROCESSING.width/2, Main.PROCESSING.height/2);
	}
}