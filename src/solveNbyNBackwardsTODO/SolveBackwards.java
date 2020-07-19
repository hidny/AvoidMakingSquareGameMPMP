package solveNbyNBackwardsTODO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import env.Constants;
import solveMPMP.SolitaryBoard;
import solveUtil.SolveUtilFunctions;

//TODO: need to debug!!!
public class SolveBackwards {

	
	//There's a bug with N=8... :(
	public static int N = 3;
	public static int NUM_CELLS = N * N;
	public static boolean FIND_PLAYER1_LOSSES = true;

	//num player 1 losing/tying solutions for a 4x4 board: 29340
	//Num of unique solutions: 27342

	//num player 2 losing/tying solutions for a 4x4 board: 16932
	//Num of unique solutions: 16122
	
	// N= 5
	// solveLosingPositions(23, true);
	//No solution!
	
	public static void main(String[] args) {
		
		System.out.println("Start");
		
		int emptySpaces;
		
		boolean searchPosWhereOddNumSpacesLeft = false;
		
		if((FIND_PLAYER1_LOSSES == true && N%2 == 1) ||
				(FIND_PLAYER1_LOSSES == false && N%2 == 0)) {
			searchPosWhereOddNumSpacesLeft = true;
		}
		
		//Find ties: (TODO: refind ties)
		//solveForTiedFinalPositions();
		//System.out.println("After solve for tie: " + codes.size());
		
		//TODO: translate ties into losing positions 1-2 moves back.
		
		
		//TODO: uncomment:
		//Find loses
		for(int i=1; 2*i <=NUM_CELLS; i++) {
			
			if(searchPosWhereOddNumSpacesLeft) {
				emptySpaces = 2*i - 1;
			} else {
				emptySpaces = 2*i;
			}
			
			
			System.out.println("Solve Losing positions (or tying) with " + emptySpaces + " empty spaces left:");
			solvePositionsWhereNextMoveLoses(NUM_CELLS - emptySpaces, true);
			
			System.out.println("Num solutions so far: " + codes.size());
			
			//TODO: translate lossing positions into losing positions 2 moves back, unless were at the very start.
		}
		
		if(FIND_PLAYER1_LOSSES == true) {
			System.out.println("num player 1 losing/tying solutions for a " + N + "x" + N + " board (with duplicates): " + numSolutions);
		} else {
			System.out.println("num player 2 losing/tying solutions for a " + N + "x" + N + " board (with duplicates):" + numSolutions);
		}
		System.out.println("Num of unique solutions: " + codes.size());
		
		if(searchPosWhereOddNumSpacesLeft == true) {
			System.out.println("Searched where there were an ODD number of empty cells left in the position");
		} else {
			System.out.println("Searched where there were an EVEN number of empty cells left in the position");
		}
		
		debugPrintSolutionCodes();
	}
	

	public static void debugPrintSolutionCodes() {
		System.out.println("Solution codes listed in order:");
		
		Object codesArray[] = codes.toArray();
		
		for(int i=0; i<codesArray.length; i++) {
			int bestIndex = i;
			for(int j=i+1; j<codesArray.length; j++) {
				
				if(((BigInteger)codesArray[bestIndex]).compareTo((BigInteger)codesArray[j]) < 0) {
					bestIndex = j;
				}
			}
			
			Object tmp = codesArray[i];
			codesArray[i] = codesArray[bestIndex];
			codesArray[bestIndex] = tmp;
		}
		
		for(int i=0; i<codesArray.length; i++) {
			System.out.println((BigInteger)codesArray[i]);
		}
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
	
	public static ArrayList<BigInteger> solveForTiedFinalPositions() {
		return solvePositionsWhereNextMoveLoses(NUM_CELLS, true);
	}
	
	
	//Recursive function to get
	//Get all losing positions for player 2
	public static ArrayList<BigInteger> solvePositionsWhereNextMoveLoses(int numPegsOnBoard, boolean tieIsAsGoodAsLoss) {
		
		SolitaryBoard board = new SolitaryBoard(N);
		
		int orderToSolve[] = SolveUtilFunctions.initOrderToSolve(NUM_CELLS); 
		
		System.out.println("Printing order to solve:");
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				
				for(int k=0; k<NUM_CELLS; k++) {
					if(orderToSolve[k] == N*i + j) {
						System.out.print(k + ("     ".substring((k + "").length()))   );
					}
				}
			}
			System.out.println();
		}
		
		
		return solveLosingPositions1MoveAway(board, orderToSolve, 0, numPegsOnBoard, tieIsAsGoodAsLoss);
	}
	
	public static HashSet<BigInteger> codes = new HashSet<BigInteger>();
	
	public static ArrayList<BigInteger> solveLosingPositions1MoveAway(SolitaryBoard current, int orderToSolve[], int numSpacesSkipped, int numPegsOnBoard, boolean tieIsAsGoodAsLoss) {
		
		
		//Check for valid solution


		int numP1PiecesNeeded = SolveUtilFunctions.numP1PiecesNeeded(numPegsOnBoard);
		int numP2PiecesNeeded = SolveUtilFunctions.numP2PiecesNeeded(numPegsOnBoard);
		
		int numSpaceNeeded = NUM_CELLS - numPegsOnBoard;
		
		if( (current.getNumPiecesForPlayer1() == numP1PiecesNeeded)
			&& current.getNumPiecesForPlayer2() == numP2PiecesNeeded) {
			
			double eval = current.naiveShallowEval(isPlayer1Turn(current) == true);
			
			if( (isPlayer1Turn(current) == true && eval < 0.0)
				|| (isPlayer1Turn(current) == false && eval > 0.0)) {
				System.out.println("Found solution.");

				System.out.println("code: " + current.getUniqueCode());
				
				if(isPlayer1Turn(current)) {
					System.out.println("Player 1's turn");
				} else {
					System.out.println("Player 2's turn");
				}
				
				current.draw();
				System.out.println("TODO: record and check if it's an insta loss...");
				

				//System.out.println(eval);
				//System.out.println("code: " + current.getUniqueCode());
				codes.add(current.getUniqueCode());
				numSolutions++;
			} else if(tieIsAsGoodAsLoss && numSpaceNeeded == 0) {
				//It's a tie
				System.out.println("Found a Solution that's a Tie!");
				if(isPlayer1Turn(current)) {
					System.out.println("Player 1's turn");
				} else {
					System.out.println("Player 2's turn");
				}
				
				current.draw();
				System.out.println("TODO: record and check if it's an insta loss...");
				
				
				System.out.println("code: " + current.getUniqueCode());
				codes.add(current.getUniqueCode());
				numSolutions++;
			}
			
			
		}
		
		//Dude, who wrote this part?
		int coord[] = SolveUtilFunctions.getNextCellToConsider(SolveUtilFunctions.getEmptyCells(current), orderToSolve, numSpacesSkipped);
		int nextI = coord[0];
		int nextJ = coord[1];
		//End Dude
		
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		
		//Try to insert P1 peg
		if(current.getNumPiecesForPlayer1() < numP1PiecesNeeded) {
			
			//try insert P1
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, P1TURN);
			
			if(tmpBoard != null) {
				
				//TODO: this function doesn't work, but you could at least try to notice when too many cells need to be skipped...
				//And insert all implied pegs
				//SolitaryBoard nextPosToAnalyze = SolveUtilFunctions.insertAllImpliedPegsForNotDoneGame(tmpBoard, numP1PiecesNeeded, numP2PiecesNeeded, numSpaceNeeded);
				
				ret.addAll(solveLosingPositions1MoveAway(tmpBoard, orderToSolve, numSpacesSkipped, numPegsOnBoard, tieIsAsGoodAsLoss));
				
			}
			//solve for next pos
			
		}
		//End try to insert P1 peg
		
		

		//Try to insert P2 peg
		if(current.getNumPiecesForPlayer2() < numP2PiecesNeeded) {
			
			//try insert P2
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, P2TURN);
			
			if(tmpBoard != null) {
				//And insert all implied pegs
				
				//TODO: see above and create function to filter out pos with too many empty spots (numSpacesSkipped + other forced empty spots we will eventually hit) 
				
				ret.addAll(solveLosingPositions1MoveAway(tmpBoard, orderToSolve, numSpacesSkipped, numPegsOnBoard, tieIsAsGoodAsLoss));
			
			}
			//solve for next pos
			
		}
		//END try to insert P2 peg
		
		//Try to insert space
		if(numSpacesSkipped + 1 <= numSpaceNeeded) {
			ret.addAll(solveLosingPositions1MoveAway(current, orderToSolve, numSpacesSkipped + 1, numPegsOnBoard, tieIsAsGoodAsLoss));
		}
		//Done
		
		return ret;
	}
	

	
	//If num pieces is even, lets say it's player1's turn
	public static boolean isPlayer1Turn(SolitaryBoard board) {
		if(board.getNumPieces() % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
/* Start
+Did not use isStillAbleToGetToPartiallyDoneGame
+Printing order to solve:
+22   16   10   13   21
+17   5    1    6    14
+9    2    0    3    11
+15   7    4    8    18
+23   19   12   20   24
+Solve Losing positions (or tying) with 2 empty spaces left:
+Printing order to solve:
+22   16   10   13   21
+17   5    1    6    14
+9    2    0    3    11
+15   7    4    8    18
+23   19   12   20   24
+Found 2821954 losing positions so far (only checking losing positions 1 move away from loss)
+Solve Losing positions (or tying) with 4 empty spaces left:
+Printing order to solve:
+22   16   10   13   21
+17   5    1    6    14
+9    2    0    3    11
+15   7    4    8    18
+23   19   12   20   24
+Found 19561751 losing positions so far (only checking losing positions 1 move away from loss)
+Solve Losing positions (or tying) with 6 empty spaces left:
+Printing order to solve:
+22   16   10   13   21
+17   5    1    6    14
+9    2    0    3    11
+15   7    4    8    18
+23   19   12   20   24
+Found 21956935 losing positions so far (only checking losing positions 1 move away from loss)
+Solve Losing positions (or tying) with 8 empty spaces left:
+Printing order to solve:
+22   16   10   13   21
+17   5    1    6    14
+9    2    0    3    11
+15   7    4    8    18
+23   19   12   20   24
+
*/
