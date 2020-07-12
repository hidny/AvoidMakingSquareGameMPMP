package player;

import env.Board;

public interface PlayerI {
	public int getBestMove(Board node, int depth);
}
