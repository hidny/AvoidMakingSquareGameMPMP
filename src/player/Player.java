package player;

import env.Board;

public interface Player {
	public int getBestMove(Board node, int depth);
}
