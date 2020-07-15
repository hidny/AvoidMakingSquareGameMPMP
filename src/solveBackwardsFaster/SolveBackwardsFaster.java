package solveBackwardsFaster;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import env.Constants;
import solveMPMP.SolitaryBoard;
import solveUtil.SolveUtilFunctions;
import solveUtil.UtilityFunctions;

//TODO: need to debug!!!
public class SolveBackwardsFaster {

	//TODO: only deal with 1 colour
	
	//There's a bug with N=8... :(
	public static int N = 4;
	public static int NUM_CELLS = N * N;
	public static boolean FIND_PLAYER1_LOSSES = false;
	
	public static long pascalsTriangle[][] = UtilityFunctions.createPascalTriangle(NUM_CELLS + 1);

	//num player 2 losing/tying solutions for a 4x4 board: 16932
	
	// N= 5
	// solveLosingPositions(23, true);
	//No solution!
	
	public static void main(String[] args) {
		
		System.out.println("Start faster");
		
		int emptySpaces;
		
		boolean searchPosWhereOddNumSpacesLeft = false;
		
		if((FIND_PLAYER1_LOSSES == true && N%2 == 1) ||
				(FIND_PLAYER1_LOSSES == false && N%2 == 0)) {
			searchPosWhereOddNumSpacesLeft = true;
		}
		
		//Find ties:
		solveForTiedFinalPositions();
		
		//TODO: translate ties into losing positions 1-2 moves back.
		
		//Find loses
		for(int i=1; 2*i <=NUM_CELLS; i++) {
			
			if(searchPosWhereOddNumSpacesLeft) {
				emptySpaces = 2*i - 1;
			} else {
				emptySpaces = 2*i;
			}
			
			
			System.out.println("Solve Losing positions (or tying) with " + emptySpaces + " empty spaces left:");
			solvePositionsWhereNextMoveLoses(NUM_CELLS - emptySpaces, true);
			//TODO: translate lossing positions into losing positions 2 moves back, unless were at the very start.
		}
		
		if(FIND_PLAYER1_LOSSES == true) {
			System.out.println("num player 1 losing/tying solutions for a " + N + "x" + N + " board: " + numSolutions);
		} else {
			System.out.println("num player 2 losing/tying solutions for a " + N + "x" + N + " board: " + numSolutions);
		}
		
		if(searchPosWhereOddNumSpacesLeft == true) {
			System.out.println("Searched where there were an ODD number of empty cells left in the position");
		} else {
			System.out.println("Searched where there were an EVEN number of empty cells left in the position");
		}
		
		
	}
	
	public static Scanner in = new Scanner(System.in);
	
	public static int numSolutions = 0;
	

	public static boolean P1TURN = true;
	public static boolean P2TURN = false;
	
	
	//Make it faster by only searching with 1 colour of peg

	//TODO: start with 4x4 case because it can be completely checked against AlphaBetaPrunPlayer
	
	public static ArrayList<BigInteger> solveForTiedFinalPositions() {
		return solvePositionsWhereNextMoveLoses(NUM_CELLS, true);
	}
	
	
	//Recursive function to get
	//Get all losing positions for player 2
	public static ArrayList<BigInteger> solvePositionsWhereNextMoveLoses(int numPegsToPlaceOnBoard, boolean tieIsAsGoodAsLoss) {
		
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
		
		
		return solveLosingPositions1MoveAway(board, orderToSolve, 0, numPegsToPlaceOnBoard, tieIsAsGoodAsLoss);
	}
	
	public static ArrayList<BigInteger> solveLosingPositions1MoveAway(SolitaryBoard current, int orderToSolve[], int indexToAdd, int numPegToPlacesOnBoard, boolean tieIsAsGoodAsLoss) {
		
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();;
		//Check for valid solution

		if(indexToAdd >= orderToSolve.length) {
			return ret;
		}

		int numP1PiecesNeeded = SolveUtilFunctions.numP1PiecesNeeded(numPegToPlacesOnBoard);
		int numP2PiecesNeeded = SolveUtilFunctions.numP2PiecesNeeded(numPegToPlacesOnBoard);

		boolean placingP1pegs;
		
		if(numPegToPlacesOnBoard % 2 == 0) {
			//Trying to find when P1 loses
			placingP1pegs = true;
			
		} else {
			//Trying to find when P2 loses
			placingP1pegs = false;
		}
		
		if(placingP1pegs) {
			if( current.getNumPiecesForPlayer1() == numP1PiecesNeeded) {
				
				int numSpacesPlayerCouldMove = SolveUtilFunctions.getNumSpacesPlayerCouldMove(current, placingP1pegs);
				
				if(numSpacesPlayerCouldMove <= numP2PiecesNeeded) {
					
					int numFreeSpacesForPlayer2 = NUM_CELLS - numP1PiecesNeeded - numSpacesPlayerCouldMove;
					int numFreePegsForPlayer2 = numP2PiecesNeeded - numSpacesPlayerCouldMove;
					
					//TODO: not everything is a solution here:
					numSolutions += pascalsTriangle[numFreeSpacesForPlayer2][numFreePegsForPlayer2];
					
					current.draw();
				//TODO: figure out actual solutions:
					
					//TODO: get player 2 to fill the places it needs to use to 'checkmate' player 1.
					
					//Board minimalCheckmateBoard = board.fillWithP1PegsNeeded to checkmate P2
					
					//if(tmpBoard == null) {
					//No solution
					//return ret
					//} 
					// else {
					//Keep going
					//}
					
					//int freeSpaceCodes[] = TODO
					boolean combo[] = new boolean[numFreeSpacesForPlayer2];
					for(int i=0; i<combo.length; i++) {
						if(i<numFreePegsForPlayer2) {
							combo[i] = true;
						} else {
							combo[i] = false;
						}
					}

					//for every combination of P2 pegs:
					while(combo != null) {
						
						//try to build board with it
	
						//Bonus: Avoid rebuilding completely after every combo iteration by identifying when the pegs placed are the same as last time
						
						//If could build board and there's no squares
						//If you could build it, then it's a solution
						
						int codesUsed[] = new int[numSpacesPlayerCouldMove];
						SolitaryBoard step[] = new SolitaryBoard[numSpacesPlayerCouldMove];
						
						
						//TODO:
						//if(itworks) {
						//numsolutions++
						//}
					
						
						combo = UtilityFunctions.getNextCombination(combo);
					}
					//Compare # of solution to the # doing it the slow way
					
				} else {
					//do nothing
				}
			
			
				return ret;
				
			}
		} else {
			//TODO: Reflect on code above...
		}
	
	
		int codeToConsider = orderToSolve[indexToAdd];
		int nextI = codeToConsider / N;
		int nextJ = codeToConsider % N;
		
		
		//Try to insert P1 peg
		if(placingP1pegs && current.getNumPiecesForPlayer1() < numP1PiecesNeeded) {
			
			//try insert P1
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, P1TURN);
			
			if(tmpBoard != null) {
				
				ret.addAll(solveLosingPositions1MoveAway(tmpBoard, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard, tieIsAsGoodAsLoss));
				
			}
			//solve for next pos
			
		}
		//End try to insert P1 peg
		
		

		//Try to insert P2 peg
		if(placingP1pegs == false && current.getNumPiecesForPlayer2() < numP2PiecesNeeded) {
			
			//try insert P2
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, P2TURN);
			
			if(tmpBoard != null) {
				
				ret.addAll(solveLosingPositions1MoveAway(tmpBoard, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard, tieIsAsGoodAsLoss));
			
			}
			//solve for next pos
			
		}
		//END try to insert P2 peg
		
		ret.addAll(solveLosingPositions1MoveAway(current, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard, tieIsAsGoodAsLoss));

		
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
