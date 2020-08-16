package aiAlgo;

import env.Board;

public class AlphaBetaPrunePlayer implements player.PlayerI {

	public static long numCallsToAlphaBeta = 0;
	
	//TODO: maybe print the evals?
	public int getBestMove(Board node, int depth) {
		boolean isMaximizingPlayer = node.isP1turn();
		
		//Depth shouldn't be more the the number of move the board can make:
		depth = Math.min(depth, env.Constants.NUM_CELLS - node.getNumPieces());
		
		if(depth == 0 || node.currentPlayerCantMove() ) {
			return -1;
			
		}

		System.out.println("Debug Depth: " + depth);
		if(isMaximizingPlayer) {

			int retMoveCode = -1;
			double eval = - Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				System.out.println("DEBUG: alphaBetaPrunePlayer1 Trying move code " + moveCode[i]);
				double newEval = alphaBetaPrune(node.playMove(moveCode[i]), depth - 1);
				
				System.out.println("eval: " + newEval);
				if(newEval > eval) {
					retMoveCode = moveCode[i];
					eval = newEval;
				}
				
			}

			System.out.println("Best eval found: " + eval);

			return retMoveCode;
			
		} else {
		
			int retMoveCode = -1;
			double eval = + Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				System.out.println("DEBUG: alphaBetaPrunePlayer2 Trying move code " + moveCode[i]);
				double newEval = alphaBetaPrune(node.playMove(moveCode[i]), depth - 1);
				
				System.out.println("eval: " + newEval);
				if(newEval < eval) {
					retMoveCode = moveCode[i];
					eval = newEval;
				}
			}
			
			System.out.println("Best eval found: " + eval);
			
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
