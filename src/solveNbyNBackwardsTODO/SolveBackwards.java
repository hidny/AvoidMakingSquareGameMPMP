package solveNbyNBackwardsTODO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import env.Constants;
import solveMPMP.SolitaryBoard;

//TODO:
public class SolveBackwards {

	
	public static int N = 5;
	public static int NUM_CELLS = N * N;

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	
	
	public static int[] initOrderToSolve() {
		
		int currentOrder[] = new int[NUM_CELLS];
		for(int i=0; i<NUM_CELLS; i++) {
			currentOrder[i] = i;
		}
		
		int numSqaresWithCellInCorner[] = new int[NUM_CELLS];
		
		for(int i=0; i<NUM_CELLS; i++) {
			numSqaresWithCellInCorner[i] = getNumSqaresCellCouldParticipateIn(i);
			System.out.println(numSqaresWithCellInCorner[i]);
		}
		
		
		for(int i=0; i<NUM_CELLS; i++) {
			int bestIndex = i;
			for(int j=i+1; j<NUM_CELLS; j++) {
				
				if(numSqaresWithCellInCorner[j] > numSqaresWithCellInCorner[bestIndex]) {
					bestIndex = j;
				}
			}
			
			int tmp = numSqaresWithCellInCorner[i];
			numSqaresWithCellInCorner[i] = numSqaresWithCellInCorner[bestIndex];
			numSqaresWithCellInCorner[bestIndex] = tmp;
			
			int tmpOrder = currentOrder[i];
			currentOrder[i] = currentOrder[bestIndex];
			currentOrder[bestIndex] = tmpOrder;
		}
		
		return currentOrder;
	}
	
	public static int getNumSqaresCellCouldParticipateIn(int code) {
		
		//i cell
		//j cell
		int icell = code / N;
		int jcell = code % N;
		
		int ret = 0;
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {

					int dy = i-icell;
					int dx = j-jcell;
					
					
					if(icell - dx >= 0         && icell - dx < N
							&& jcell + dy >= 0 && jcell + dy < N
							&& i - dx >= 0     && i - dx < N
							&& j + dy >= 0     && j + dy < N) {
						
						ret++;
						
					}// END IF could make square
					
			}// END inner for loop
		
		}//END outer for loop
		
		return ret;
	}
	
	public static Scanner in = new Scanner(System.in);
	
	public static int numSolutions = 0;
	

	public static boolean P1TURN = true;
	public static boolean P2TURN = false;
	
	
	//TODO:
	//IDEA:
	//I want positions where P2 is to play and (lose/tie)...
	
	//My guess is that I should backtrack:
	//So, in the case of N=5
	//A solution has to be N*N - 2 = 23 cells on the table.
	//And there can't be any squares (and P2 can't win)
	
	//Try:
	//param  solveLoss(N, depth, isTieALoss, int maxNumMovesToLose = Infinity)
	// ex: solveLoss(5, 23, true, 1)
	
	//From the answers
	
	//Stratgy: start with just looking for a losing position:
		//EZ:
		//Maybe find all possible positions where P1 played 12 time and P2 played 11 times and P2 will not win.
		
		//1st: find all possible positions where P1 played 12 time and P2 played 11 times by reusing solve funcion
			// For every such position, check if P2 can win with the AlphaBetaPrune Player
		
		//One we get that, we can try to do:
		//solveLoss(N, depth, isTieALoss, int maxNumMovesToLose = Infinity)
		//solveLoss(5, 21, true, 3)
		
		//This can maybe rely on the answer to solveLoss(5, 23, true, 1), so the alpha beta pruner only has to do 2 plys.
		
		//Then:
		//solveLoss(5, 19, true, 3)
		//This can maybe rely on the answer to solveLoss(5, 21, true, 3), so the alpha beta pruner only has to do 2 plys.
		
		//...
		//solveLoss(5, 1, true, 23)
		//Done...
		//The hope is that Player 2 doesn't really lose
		
		//If this doesn't work, try letting Player 2 accept ties
		
		//Or:
		//Try finding positions Player 1 loses.
	
	
	
	//TODO: start with 4x4 case because it can be completely checked against AlphaBetaPrunPlayer
	
	//TODO: OR: start with 23 pegs case
	
	//Recursive function to get
	//Get all losing positions for player 2
	public static ArrayList<BigInteger> solveLosingPositions(int size, int numPegsOnBoard, boolean tieIsAsGoodAsLoss) {
		
		//If size * size - numPegsOnBoard > 2:
		// Will need to call: solveLosingPositions(size, numPegsOnBoard - 2, tieIsAsGoodAsLoss)
		return null;
	}
	
	
	
	public static ArrayList<BigInteger> solve(SolitaryBoard current, int orderToSolve[]) {
		
		//If solved: stop and show

		int table[][] = current.getTable();
		boolean emptySpots[][] = new boolean[table.length][table.length];

		boolean curSolved = true;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				if(table[i][j] == Constants.EMPTY) {
					emptySpots[i][j] = true;
					curSolved = false;
				}
			}
		}
		
		if(curSolved) {
			System.out.println("Found a solution!");
			numSolutions++;
			System.out.println("Solution #" + numSolutions);
			current.draw();
			System.out.println();
			
			return null;
			//in.nextLine();
		}
		
		//End if solved

		//Get next location to insert
		int nextI = -1;
		int nextJ = -1;
		
		for(int k=0; k<orderToSolve.length; k++) {
			int i = orderToSolve[k] / table.length;
			int j = orderToSolve[k] % table.length;
			
			if(emptySpots[i][j]) {
				nextI = i;
				nextJ = j;
			}
		}
		//End get next empty spot
		
		
		//Get num pieces left to add:
		int PIECE_NEEDED_P1 = -1;
		int PIECE_NEEDED_P2 = -1;
		
		if(orderToSolve.length % 2 == 0) {
			PIECE_NEEDED_P1 = orderToSolve.length / 2;
			PIECE_NEEDED_P2 = orderToSolve.length / 2;
		} else {
			PIECE_NEEDED_P1 = (orderToSolve.length + 1) / 2;
			PIECE_NEEDED_P2 = (orderToSolve.length - 1) / 2;
			
		}
		//End get num pieces left to add

		
		//Try playing a move for P1: (TODO: try no limit)
		if(current.getNumPiecesForPlayer1() < PIECE_NEEDED_P1) {
		
			//try insert P1
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, P1TURN);
			
			if(tmpBoard != null) {
				//And insert all implied pegs
				//SolitaryBoard nextPosToAnalyze = insertAllImpliedPegsForATie(tmpBoard);
				
				//if(nextPosToAnalyze != null) {
				//	return solve(nextPosToAnalyze, orderToSolve);
				//}
			}
			//solve for next pos
		}
		
		
		//Try playing a move for P2 instead:(TODO: try no limit)
		if( current.getNumPiecesForPlayer2() < PIECE_NEEDED_P2) {
			
			//try insert P2
			SolitaryBoard tmpBoard2 = current.moveNullOnLoss(nextI, nextJ, P2TURN);
			
			if(tmpBoard2 != null) {
				//And insert all implied pegs
				
				//SolitaryBoard nextPosToAnalyze = insertAllImpliedPegsForATie(tmpBoard2);
				
				//if(nextPosToAnalyze != null) {
				//	return solve(nextPosToAnalyze, orderToSolve);
				//}
				
			}
			//solve for next pos
		
		}
		//Done.
		
		return null;
		
	}
	

}
