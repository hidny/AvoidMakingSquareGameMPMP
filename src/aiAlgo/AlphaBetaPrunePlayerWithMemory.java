package aiAlgo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import env.Board;
import env.Constants;

public class AlphaBetaPrunePlayerWithMemory implements player.Player {
	
	//DEBUG
	public static long numCallsToAlphaBeta = 0;

	public static int DEBUG_K = 8;
	public static long numCallsToAlphaBetaKMovesIn[] = new long[DEBUG_K + 1];
	//END DEBUG
	
	//TODO: You think I can have a copy with Long and only use this long passes?
	// Answer: it's a lot of work and probably not worthwhile
	public static HashMap<BigInteger, Double>  records = new  HashMap<BigInteger, Double>();
	public static int numElementsRecorded = 0;
	
	//public static HashSet<Long>  fasterRecordSearch = new  HashSet<Long>();
	
	
	//TODO: what's the max hash table size that I can get away with?
	public static int ONE_MILION = 1000000;
	public static int maxNumElements = 10 * ONE_MILION;
	
	
	//TODO: use to save important early records.
	//public static HashSet<Long>  recordsToSave = new  HashSet<Long>();
	
	//TODO
	public static int MIN_DEPTH_TO_SAVE = 10;
	
	
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
				System.out.println("DEBUG: alphaBetaPrunePlayerWithMemory1 Trying move code " + moveCode[i]);
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
				System.out.println("DEBUG: alphaBetaPrunePlayerWithMemory1 Trying move code " + moveCode[i]);
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

		//DEBUG
		numCallsToAlphaBeta++;
		
		if(depth >= Constants.SIZE * Constants.SIZE - DEBUG_K) {
			int tmpDebugMovesIn =  (Constants.SIZE * Constants.SIZE - depth);
			numCallsToAlphaBetaKMovesIn[tmpDebugMovesIn]++;
			System.out.println("Num calls to AlphaBetaMem while " + tmpDebugMovesIn + " Moves in: " + numCallsToAlphaBetaKMovesIn[tmpDebugMovesIn]);
		}
		
		if(numCallsToAlphaBeta % 10000000 == 0) {
			System.out.println("Depth: " + depth);
			System.out.println(numCallsToAlphaBeta);
		}
		//END DEBUG

		boolean isMaximizingPlayer = node.isP1turn();

		if(depth == 0 || node.currentPlayerCantMove() ) {
			return node.naiveShallowEval();
			
		}
		
		BigInteger uniqueCode = node.getUniqueCode();

			
		if(isMaximizingPlayer) {
			double eval = - Double.MAX_VALUE;
			
			if(records.containsKey(uniqueCode)) {
				
				eval = records.get(uniqueCode);
				alpha = Math.max(alpha, eval);
				
				if(alpha >= beta) {
					return eval;
				}
			}
			
			int moveCode[]  = node.getPlayableMovesNaive();
			
			for(int i=0; i<moveCode.length; i++) {
				eval = Math.max(eval, alphaBetaPrune(node.playMove(moveCode[i]), depth - 1, alpha, beta));
				
				alpha = Math.max(alpha, eval);
				
				if(alpha >= beta) {
					break; //Beta cut-off
				}
			}

			saveElement(node.getUniqueCode(), eval, isMaximizingPlayer);
			
			return eval;
			
		} else {
			double eval = + Double.MAX_VALUE;
			
			int moveCode[]  = node.getPlayableMovesNaive();
			

			if(records.containsKey(uniqueCode)) {
				
				eval = records.get(uniqueCode);
				beta = Math.min(beta, eval);
				
				if(beta <= alpha) {
					return eval;
				}
			}
			
			for(int i=0; i<moveCode.length; i++) {
				eval = Math.min(eval, alphaBetaPrune(node.playMove(moveCode[i]), depth - 1, alpha, beta));
				
				beta = Math.min(beta, eval);
				
				if(beta <= alpha) {
					break; //Alpha cut-off
				}
			}

			saveElement(node.getUniqueCode(), eval, isMaximizingPlayer);
			
			return eval;
		}
		
	}
	
	//public static HashMap<BigInteger, Double>  records = new  HashMap<BigInteger, Double>();
	//public static int MIN_DEPTH_TO_SAVE = 10;
	
	
	public static void saveElement(BigInteger code, double eval, boolean isMaximizing) {
		
		if(records.containsKey(code)) {
			double value = records.get(code);
			
			if((isMaximizing && eval > value)
					|| (isMaximizing == false && eval < value)) {
				records.remove(code);
				records.put(code, eval);
			}
		} else {
			records.put(code, eval);
			numElementsRecorded++;
			
		}
		
		if(numElementsRecorded > maxNumElements) {
			records.clear();
			numElementsRecorded = 0;
		}
	}
	
	
	public static void main(String args[]) {
		
		BigInteger TWO = new BigInteger("2");
		
		BigInteger TWO2 = new BigInteger("2");
		
		saveElement(TWO, 1.0, true);
		saveElement(TWO2, 2.0, true);
		saveElement(TWO, 1.5, true);
		
		System.out.println(numElementsRecorded);
		
		System.out.println(records.get(TWO));
	}
	
}
