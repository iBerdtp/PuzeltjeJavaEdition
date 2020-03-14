package bart.thesis;

import java.util.ArrayList;
import java.util.Collections;

public class BackwardsNode extends Board
{
	private int nrOfPawns;
	private ArrayList<int[][]> visited;
	private int maxNrOfPawns;
	
	public BackwardsNode(GameType gameType, int arrayDim, Vector[] goals, int maxNrOfPawns)
	{
		super(gameType, arrayDim, goals, new int[arrayDim][arrayDim]);
		this.maxNrOfPawns = maxNrOfPawns;
		this.visited = new ArrayList<int[][]>();
		for(Vector goal : goals)
			set(goal, 1);
	}
	
	private BackwardsNode(BackwardsNode node)
	{
		super(node);
		this.maxNrOfPawns = node.maxNrOfPawns;
		this.nrOfPawns = node.nrOfPawns;
	}
	
	public ArrayList<BackwardsNode> getPossibleParents(Vector v, Move move)
	{
		ArrayList<BackwardsNode> parents = new ArrayList<BackwardsNode>();
		Vector obstacleSpot = v.copy().add(move.v);
		Vector reverse = v.copy().sub(move.v);
		if(!boundariesRespected(obstacleSpot) || !boundariesRespected(reverse) || get(reverse)>0 || get(obstacleSpot)==0)
			return parents;
		
		BackwardsNode copy = copy();
		copy.depth++;
		return copy.getPossibleParents(v, move, get(v), new ArrayList<BackwardsNode>());
	}
	
	private ArrayList<BackwardsNode> getPossibleParents(Vector v, Move move, int type, ArrayList<BackwardsNode> parents)
	{
		set(v, 0);
		v.sub(move.v);
		if(!boundariesRespected(v) || get(v)>0)
			return parents;
		set(v, type);
		if(!isWin())
			parents.add(this);
		return copy().getPossibleParents(v, move, type, parents);
	}
	
	public boolean backwardsMove(Vector v, Move move)
	{
		Vector obstacleSpot = v.copy().add(move.v);
		Vector reverse = v.copy().sub(move.v);
		if(!boundariesRespected(obstacleSpot) || !boundariesRespected(reverse) || get(reverse)>0 || get(obstacleSpot)==0 && (nrOfPawns==maxNrOfPawns))
			return false;
		
		int[][] backup = Util.copy(getBoard());
		
		if(get(obstacleSpot)==0)
			set(obstacleSpot, 2);
		
		int maxNrOfSteps = 1;
		while(boundariesRespected(reverse.sub(move.v)) && get(reverse)==0)
			maxNrOfSteps++;
		
		ArrayList<Integer> stepSizes = Util.sequence(maxNrOfSteps, 1);
		boolean moved = false;
		while(!stepSizes.isEmpty() && !moved)
		{
			int stepSize = Util.removeRandom(stepSizes);
			reverse.setTo(v).sub(move.v, stepSize);
			int type = get(v);
			set(v, 0);
			set(reverse, type);
			int boardPos = Collections.binarySearch(visited, getBoard(), Processing.COMPARATOR);
			if(boardPos<0)
			{
				visited.add(-boardPos-1, Util.copy(getBoard()));
				while(!v.equals(reverse))
					v.sub(move.v);
				moved = true;
			}
		}
		if(moved)
			return true;
		setBoard(backup);
		return false;
	}
	
	@Override
	public BackwardsNode copy()
	{
		return new BackwardsNode(this);
	}
	
	public Board yieldBoard()
	{
		return super.copy();
	}
	
	public int getNrOfPawns()
	{
		return nrOfPawns;
	}
	
	public void setMaxNrOfPawns(int maxNrOfPawns)
	{
		this.maxNrOfPawns = maxNrOfPawns;
	}
	
	public ArrayList<BackwardsNode> completeBoard()
	{
		return completeBoard(new ArrayList<BackwardsNode>(), new Vector(-1, 0));
	}
	
	private ArrayList<BackwardsNode> completeBoard(ArrayList<BackwardsNode> nodes, Vector location)
	{
		if(maxNrOfPawns==nrOfPawns)
			nodes.add(this);
		while(maxNrOfPawns>nrOfPawns && (location = getNextEmpty(location))!=null)
		{
			BackwardsNode copy = copy();
			copy.set(location, 2);
			copy.completeBoard(nodes, location);
		}
		return nodes;
	}
	
	public int getMaxNrOfPawns()
	{
		return maxNrOfPawns;
	}
	
	@Override
	public Vector getNextEmpty(Vector v)
	{
		int x = v.x+1;
		for(int y = v.y; y<arrayDim; y++)
		{
			for(; x<arrayDim; x++)
				if(get(x, y)==0)
					return new Vector(x, y);
			x = 0;
		}
		return null;
	}
	
	@Override
	public void set(int x, int y, int n)
	{
		if(get(x, y)==2)
			nrOfPawns--;
		super.set(x, y, n);
		if(n==2)
			nrOfPawns++;
	}
}
