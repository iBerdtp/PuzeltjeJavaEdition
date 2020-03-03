package bart.thesis;

import java.io.File;

public class TypeLoadSection extends TextSection
{
	public TypeLoadSection(Section parentSection)
	{
		super(parentSection, getLine());
	}
	
	public void performWhenDone()
	{
		String[] options = Processing.SAVES_DIR.exists() ? Processing.SAVES_DIR.list() : null;
		if(options==null)
		{
			Main.PROCESSING.setSection(new Welcome());
			return;
		}
		File newDir = new File(Processing.SAVES_DIR, options[answers[0]-1]);
		Main.PROCESSING.setSection(new DifficultyLoadSection(this, newDir, GameType.toType(options[answers[0]-1])));
	}
	
	private static String getLine()
	{
		String[] options;
		if(!Processing.SAVES_DIR.exists() || (options = Processing.SAVES_DIR.list()).length==0)
			return "No saves (1)";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<options.length; i++)
			sb.append((i==0 ? "" : " or ")+options[i]+"("+(i+1)+")");
		sb.append("?");
		return sb.toString();
	}
}
