package aiAlgo;

import env.Board;

public class MinMaxPlayer implements player.PlayerI {

	public static long numCallsToMinMax = 0;
	
	//TODO: maybe print the evals?
	public int getBestMove(Board node, int depth) {
		boolean isMaximizingPlayer = node.isP1turn();

		//Depth shouldn't be more the the number of move the board can make:
		depth = Math.min(depth, env.Constants.NUM_CELLS - node.getNumPieces());
		
		if(depth == 0 || node.currentPlayerCantMove() ) {
			return -1;
			
		}
		
		if(isMaximizingPlayer) {

			int retMoveCode = -1;
			double eval = - Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				System.out.println("DEBUG: minmaxplayer Trying move code " + moveCode[i]);
				double newEval = minimax(node.playMove(moveCode[i]), depth - 1);
				if(newEval >= eval) {
					retMoveCode = moveCode[i];
					eval = newEval;
				}
				
			}
			
			return retMoveCode;
			
		} else {

			int retMoveCode = -1;
			double eval = + Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				double newEval = minimax(node.playMove(moveCode[i]), depth - 1);
				
				if(newEval <= eval) {
					retMoveCode = moveCode[i];
					eval = newEval;
				}
			}
			
			return retMoveCode;
		}
	}
	
	public double minimax(Board node, int depth) {
		
		numCallsToMinMax++;
		
		if(numCallsToMinMax % 10000000 == 0) {
			System.out.println("Depth: " + depth);
			System.out.println(numCallsToMinMax);
		}
		boolean isMaximizingPlayer = node.isP1turn();

		if(depth == 0) {
			for(int i=0; i<node.getTable().length; i++) {
				for(int j=0; j<node.getTable().length; j++) {
					if(node.getTable()[i][j] == env.Constants.EMPTY) {
						System.out.println("AHH!!");
						System.exit(1);
					}
				}
			}
			return 0.0;
		} else if(node.currentPlayerCantMove() && depth > 0) {
			
			if(isMaximizingPlayer) {
				return - Double.MAX_VALUE;
			} else {
				return Double.MAX_VALUE;
			}
			
		} else if(isMaximizingPlayer) {
			double eval = - Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				eval = Math.max(eval, minimax(node.playMove(moveCode[i]), depth - 1));
			}
			
			return eval;
			
		} else {
			double eval = + Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				eval = Math.min(eval, minimax(node.playMove(moveCode[i]), depth - 1));
			}
			
			return eval;
		}
		
	}
}
