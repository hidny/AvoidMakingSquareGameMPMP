package aiAlgo;

import env.Board;

public class MinMaxPlayer implements player.PlayerI {

	public static long numCallsToMinMax = 0;
	
	//TODO: maybe print the evals?
	public int getBestMove(Board node, int depth) {
		boolean isMaximizingPlayer = node.isP1turn();
		
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

		if(depth == 0 || node.currentPlayerCantMove() ) {
			return node.naiveShallowEval();
			
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
