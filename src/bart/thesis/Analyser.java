package bart.thesis;

import java.util.ArrayList;

public interface Analyser
{
	public void calculate();
	public int getGod();
	public ArrayList<int[][]> getHardests();
	public ArrayList<int[][]> getHardestsSolved();
	public int getUnsolvableCount();
	public ArrayList<Integer> getDistribution();
}