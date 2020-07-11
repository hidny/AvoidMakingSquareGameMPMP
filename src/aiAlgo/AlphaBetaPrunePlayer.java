package aiAlgo;

import env.Board;

public class AlphaBetaPrunePlayer implements player.Player {

	public static int numCallsToAlphaBeta = 0;
	
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
				System.out.println("DEBUG: alphaBetaPrunePlayer1 Trying move code " + moveCode[i]);
				double newEval = alphaBetaPrune(node.playMove(moveCode[i]), depth - 1);
				if(newEval > eval) {
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
				System.out.println("DEBUG: alphaBetaPrunePlayer2 Trying move code " + moveCode[i]);
				double newEval = alphaBetaPrune(node.playMove(moveCode[i]), depth - 1);
				
				if(newEval < eval) {
					retMoveCode = moveCode[i];
					eval = newEval;
				}
			}
			
			return retMoveCode;
		}
	}
	
	public double alphaBetaPrune(Board node, int depth) {
		return alphaBetaPrune(node, depth, - Double.MAX_VALUE, Double.MAX_VALUE);
	}

	public double alphaBetaPrune(Board node, int depth, double alpha, double beta) {
		
		numCallsToAlphaBeta++;
		
		if(numCallsToAlphaBeta % 10000000 == 0) {
			System.out.println("Depth: " + depth);
			System.out.println(numCallsToAlphaBeta);
		}
		boolean isMaximizingPlayer = node.isP1turn();

		if(depth == 0 || node.currentPlayerCantMove() ) {
			return node.naiveShallowEval();
			
		} else if(isMaximizingPlayer) {
			double eval = - Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				eval = Math.max(eval, alphaBetaPrune(node.playMove(moveCode[i]), depth - 1, alpha, beta));
				
				alpha = Math.max(alpha, eval);
				
				if(alpha >= beta) {
					break; //Beta cut-off
				}
			}
			
			return eval;
			
		} else {
			double eval = + Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				eval = Math.min(eval, alphaBetaPrune(node.playMove(moveCode[i]), depth - 1, alpha, beta));
				
				beta = Math.min(beta, eval);
				
				if(beta <= alpha) {
					break; //Alpha cut-off
				}
			}
			
			return eval;
		}
		
	}
}
