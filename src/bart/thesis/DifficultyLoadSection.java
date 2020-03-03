package bart.thesis;

import java.io.File;
import java.util.Arrays;

public class DifficultyLoadSection extends TextSection
{
	private File typeDir;
	private GameType gameType;
	private int[] difficulties;
	
	public DifficultyLoadSection(Section parentSection, File typeDir, GameType gameType)
	{
		super(parentSection, getLine(typeDir));
		this.typeDir = typeDir;
		this.gameType = gameType;
		this.difficulties = Util.toIntArray(typeDir.list());
	}
	
	@Override
	public void performWhenDone()
	{
		int answer = answers[0];
		if(Util.contains(difficulties, answer))
			Main.PROCESSING.setSection(new SelectPuzzleSection(this, new File(typeDir, Integer.toString(answer)), gameType));
		else
			Main.PROCESSING.setSection(new DifficultyLoadSection(this, typeDir, gameType));
	}
	
	public void resize()
	{
		Main.PROCESSING.setSurfaceSize(1000, Math.max(4, (questions.length+2))*textSize);
	}
	
	private static String getLine(File typeDir)
	{
		int[] difs = Util.toIntArray(typeDir.list());
		Arrays.sort(difs);
		return "["+String.join(",", Util.toStringArray(difs))+"]?";
	}
}
