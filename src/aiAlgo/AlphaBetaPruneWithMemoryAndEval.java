package aiAlgo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import aiAlgoevaluator.BasicEval;
import aiAlgoevaluator.EvaluatorI;
import env.Board;
import env.Constants;

//5x5 board after an hour and 30 minutes:
//Num calls to AlphaBetaMem while 3 Moves in: 2
public class AlphaBetaPruneWithMemoryAndEval implements player.PlayerI {
	
	public AlphaBetaPruneWithMemoryAndEval(EvaluatorI evalFunc) {
		this(evalFunc, Constants.NUM_CELLS);
	}
	
	public AlphaBetaPruneWithMemoryAndEval(EvaluatorI evalFunc, int saveMinDepth) {
		this.evalFunc = evalFunc;
		this.saveMinDepth = saveMinDepth;
	}
	
	//DEBUG
	public long numCallsToAlphaBeta = 0;

	public int DEBUG_K = 7;
	public long numCallsToAlphaBetaKMovesIn[] = new long[DEBUG_K + 1];
	//END DEBUG
	
	//TODO: You think I can have a copy with Long and only use this long passes?
	// Answer: it's a lot of work and probably not worthwhile

	public HashMap<BigInteger, Double>  records1 = new  HashMap<BigInteger, Double>();
	public int numElementsRecorded1 = 0;
	
	public HashMap<BigInteger, Double>  records2 = new  HashMap<BigInteger, Double>();
	public int numElementsRecorded2 = 0;
	
	//public static HashSet<Long>  fasterRecordSearch = new  HashSet<Long>();
	
	
	//TODO: what's the max hash table size that I can get away with?
	public int ONE_MILION = 1000000;
	public int maxNumElements = 10 * ONE_MILION;
	
	
	//TODO: use to save important early records.
	//public static HashSet<Long>  recordsToSave = new  HashSet<Long>();
	
	//TODO
	//public static int MIN_DEPTH_TO_SAVE = 10;
	
	
	private EvaluatorI evalFunc;
	private int saveMinDepth;
	
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
		
		if(Constants.SIZE >= Constants.DEBUG_SIZE && depth >= Constants.NUM_CELLS - DEBUG_K) {
			int tmpDebugMovesIn =  (Constants.NUM_CELLS - depth);
			numCallsToAlphaBetaKMovesIn[tmpDebugMovesIn]++;
			
			if(depth == node.getNumPieces() + Constants.NUM_CELLS) {
				System.out.println("Full search:");
			}
			System.out.println("Num calls to AlphaBetaMem while " + tmpDebugMovesIn + " Moves in: " + numCallsToAlphaBetaKMovesIn[tmpDebugMovesIn]);
			System.out.println("Alpha: " + alpha);
			System.out.println("Beta: " + beta);
			System.out.println("saved 1: " + numElementsRecorded1);
			System.out.println("saved 2: " + numElementsRecorded2);
			System.out.println("END debug message");
			
		}
		
		if(numCallsToAlphaBeta % 10000000 == 0) {
			System.out.println("Depth: " + depth);
			System.out.println(numCallsToAlphaBeta);
		}
		//END DEBUG

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
			
		}
		
		BigInteger uniqueCode = node.getUniqueCode();

			
		if(isMaximizingPlayer) {
			double eval = - Double.MAX_VALUE;
			
			if(records1.containsKey(uniqueCode)) {
				
				eval = records1.get(uniqueCode);
				alpha = Math.max(alpha, eval);
				
				if(alpha >= beta) {
					return eval;
				}
			}
			
			Board leafNodeList[] = evalFunc.getPlayableMovesSortedBasedOnEval(node);
			
			for(int i=0; i<leafNodeList.length; i++) {
				eval = Math.max(eval, alphaBetaPrune(leafNodeList[i], depth - 1, alpha, beta));
				
				alpha = Math.max(alpha, eval);
				
				if(alpha >= beta) {
					break; //Beta cut-off
				}
			}
			//TODO: 0.0 is such an arbitrary number...
			if(eval >= 0.0 && depth > Constants.NUM_CELLS - this.saveMinDepth) {
				saveElement1(node.getUniqueCode(), eval, isMaximizingPlayer);
			}
			
			if(depth >= Constants.NUM_CELLS - 6) {
				System.out.println("TEST");
				System.out.println("DEPTH: " + depth);
				System.out.println("DEBUG: I bet it's Max value!");
				System.out.println(eval);
				System.out.println("END TEST");
			}
			
			return eval;
			
		} else {
			double eval = + Double.MAX_VALUE;
			
			

			if(records2.containsKey(uniqueCode)) {
				
				eval = records2.get(uniqueCode);
				beta = Math.min(beta, eval);
				
				if(beta <= alpha) {
					return eval;
				}
			}
			
			Board leafNodeList[] = evalFunc.getPlayableMovesSortedBasedOnEval(node);
			
			for(int i=0; i<leafNodeList.length; i++) {
				eval = Math.min(eval, alphaBetaPrune(leafNodeList[i], depth - 1, alpha, beta));
				
				beta = Math.min(beta, eval);
				
				if(beta <= alpha) {
					break; //Alpha cut-off
				}
			}

			//TODO: 0.0 is such an arbitrary number...
			if(eval <= 0.0 && depth > Constants.NUM_CELLS - this.saveMinDepth) {
				saveElement2(node.getUniqueCode(), eval, isMaximizingPlayer);
			}
			return eval;
		}
		
	}
	
	//public static HashMap<BigInteger, Double>  records = new  HashMap<BigInteger, Double>();
	//public static int MIN_DEPTH_TO_SAVE = 10;
	
	
	public void saveElement1(BigInteger code, double eval, boolean isMaximizing) {
		
		if(records1.containsKey(code)) {
			double value = records1.get(code);
			
			if((isMaximizing && eval > value)
					|| (isMaximizing == false && eval < value)) {
				records1.remove(code);
				records1.put(code, eval);
			}
		} else {
			records1.put(code, eval);
			numElementsRecorded1++;
			
		}
		
		if(numElementsRecorded1 > maxNumElements) {
			System.out.println("DEBUG: MEMORY REFRESH 1");
			records1.clear();
			numElementsRecorded1 = 0;
		}
	}
	
	//TODO: fix copy/paste code
	public void saveElement2(BigInteger code, double eval, boolean isMaximizing) {
		
		if(records2.containsKey(code)) {
			double value = records2.get(code);
			
			if((isMaximizing && eval > value)
					|| (isMaximizing == false && eval < value)) {
				records2.remove(code);
				records2.put(code, eval);
			}
		} else {
			records2.put(code, eval);
			numElementsRecorded2++;
			
		}
		
		if(numElementsRecorded2 > maxNumElements) {
			System.out.println("DEBUG: MEMORY REFRESH 2");
			records2.clear();
			numElementsRecorded2 = 0;
		}
	}
	
	public void clearMemoryRecords() {
		records1.clear();
		numElementsRecorded1 = 0;
		
		records2.clear();
		numElementsRecorded2 = 0;
	}
	
	
	public static void main(String args[]) {
		
		AlphaBetaPruneWithMemoryAndEval test = new AlphaBetaPruneWithMemoryAndEval(new BasicEval(), -1);
		
		BigInteger TWO = new BigInteger("2");
		
		BigInteger TWO2 = new BigInteger("2");
		
		test.saveElement1(TWO, 1.0, true);
		test.saveElement1(TWO2, 2.0, true);
		test.saveElement1(TWO, 1.5, true);
		
		System.out.println(test.numElementsRecorded1);
		
		System.out.println(test.records1.get(TWO));
	}
	
}
