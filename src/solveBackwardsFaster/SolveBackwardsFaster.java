package solveBackwardsFaster;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import solveMPMP.SolitaryBoard;
import solveUtil.SolveUtilFunctions;
import solveUtil.UtilityFunctions;

//TODO: need to debug!!!
public class SolveBackwardsFaster {

	//TODO: only deal with 1 colour
	
	//There's a bug with N=8... :(
	public static int N = 3;
	public static int NUM_CELLS = N * N;
	public static boolean FIND_PLAYER1_LOSSES = true;
	
	public static long pascalsTriangle[][] = UtilityFunctions.createPascalTriangle(NUM_CELLS + 1);

	//Not quite...
	//num player 1 losing/tying solutions for a 4x4 board: 25110
	//Searched where there were an EVEN number of empty cells left in the position
	
	
	public static void main(String[] args) {
		
		System.out.println("Start faster");
		
		int emptySpaces;
		
		boolean searchPosWhereOddNumSpacesLeft = false;
		
		if((FIND_PLAYER1_LOSSES == true && N%2 == 1) ||
				(FIND_PLAYER1_LOSSES == false && N%2 == 0)) {
			searchPosWhereOddNumSpacesLeft = true;
		}
		
		//TODO Find ties
		//solveForTiedFinalPositions();
		
		//TODO: translate ties into losing positions 1-2 moves back.
		
		//Find loses
		for(int i=1; 2*i <=NUM_CELLS; i++) {
			
			if(searchPosWhereOddNumSpacesLeft) {
				emptySpaces = 2*i - 1;
			} else {
				emptySpaces = 2*i;
			}
			
			
			System.out.println("Solve Losing positions (or tying) with " + emptySpaces + " empty spaces left:");
			solvePositionsWhereNextMoveLoses(NUM_CELLS - emptySpaces);
			//TODO: translate losing positions into losing positions 2 moves back, unless were at the very start.
			

			System.out.println("Num solutions so far: " + codes.size());
		}
		
		if(FIND_PLAYER1_LOSSES == true) {
			System.out.println("num player 1 losing/tying solutions for a " + N + "x" + N + " board: " + numSolutions);
		} else {
			System.out.println("num player 2 losing/tying solutions for a " + N + "x" + N + " board: " + numSolutions);
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

	public static HashSet<BigInteger> codes = new HashSet<BigInteger>();
	

	public static boolean P1TURN = true;
	public static boolean P2TURN = false;
	
	
	//Make it faster by only searching with 1 colour of peg

	//TODO: start with 4x4 case because it can be completely checked against AlphaBetaPrunPlayer
	
	public static ArrayList<BigInteger> solveForTiedFinalPositions() {
		return solvePositionsWhereNextMoveLoses(NUM_CELLS);
	}
	
	
	//Recursive function to get
	//Get all losing positions for player 2
	public static ArrayList<BigInteger> solvePositionsWhereNextMoveLoses(int numPegsToPlaceOnBoard) {
		
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
		
		
		return solveLosingPositions1MoveAway(board, orderToSolve, 0, numPegsToPlaceOnBoard);
	}
	
	public static ArrayList<BigInteger> solveLosingPositions1MoveAway(SolitaryBoard current, int orderToSolve[], int indexToAdd, int numPegToPlacesOnBoard) {
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		//Check for valid solution

		//See if there's a solution
		int numP1PiecesNeeded = SolveUtilFunctions.numP1PiecesNeeded(numPegToPlacesOnBoard);
		int numP2PiecesNeeded = SolveUtilFunctions.numP2PiecesNeeded(numPegToPlacesOnBoard);

		boolean isP1Losing;
		int numLosingPegsNeeded = -1;
		
		if(numPegToPlacesOnBoard % 2 == 0) {
			//Trying to find when P1 loses
			isP1Losing = true;
			numLosingPegsNeeded = numP1PiecesNeeded;
			
		} else {
			//Trying to find when P2 loses
			isP1Losing = false;
			numLosingPegsNeeded = numP2PiecesNeeded;
		}

		
		
		//Try to Add stuff:
		if(indexToAdd >= orderToSolve.length) {
			return ret;
		}
	
		int codeToConsider = orderToSolve[indexToAdd];
		int nextI = codeToConsider / N;
		int nextJ = codeToConsider % N;
		
		
		//Try to insert pegs for losing player
		if(current.getNumPieces() < numLosingPegsNeeded ){
			
			//try insert P1
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, isP1Losing);
			
			if(tmpBoard != null) {
				
				if(tmpBoard.getNumPieces() == numLosingPegsNeeded) {
					tmpBoard.draw();
					ret.addAll( findSolutionsByAddingOpponentPegs(tmpBoard, isP1Losing, numP1PiecesNeeded, numP2PiecesNeeded));
					
				} else if(tmpBoard.getNumPieces() < numLosingPegsNeeded) {
					
					ret.addAll(solveLosingPositions1MoveAway(tmpBoard, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard));
					
				} else {
					System.out.println("ERROR: there's too many pegs placed in solveLosingPositions1MoveAway");
					System.exit(1);
					
				}
				
			}
			
		}
		//End try to insert pegs for losing player
		

		//Try not filling the current space:
		int numSpacesInIndexesNotChecked = NUM_CELLS - indexToAdd - 1;
		if(current.getNumPieces() + numSpacesInIndexesNotChecked >= numLosingPegsNeeded) {
		
			ret.addAll(solveLosingPositions1MoveAway(current, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard));
		}
		//End try not filling the current space
		
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
	
	//pre: losing player's pegs are all already place in current and they
	//    are placed in legal positions.
	public static ArrayList<BigInteger> findSolutionsByAddingOpponentPegs(SolitaryBoard current, boolean isP1Losing, int numP1PiecesNeeded, int numP2PiecesNeeded) {
		
		
		int numSpacesLosingPlayerCouldMove = SolveUtilFunctions.getNumSpacesPlayerCouldMove(current, isP1Losing);
		
		int currentNumWinningPlayerPieces = -1;
		if(isP1Losing) {
			currentNumWinningPlayerPieces = numP2PiecesNeeded;
		} else {
			currentNumWinningPlayerPieces = numP1PiecesNeeded;
		}
		

		if(SolveUtilFunctions.getNumSpacesPlayerCouldMove(current, isP1Losing) > currentNumWinningPlayerPieces) {
			return new ArrayList<BigInteger>();
		}
		
		boolean isP1WinningPegs = !isP1Losing;
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		
		SolitaryBoard minimalCheckmater = SolveUtilFunctions.fillUpBoardForCheckmate(current, isP1Losing);
		
		if(minimalCheckmater == null) {
			return ret;
		}
		
		
		int freeSpaceCodes[] = SolveUtilFunctions.getMovableLocations(minimalCheckmater, isP1WinningPegs);
		
		boolean combo[] = new boolean[freeSpaceCodes.length];
		for(int i=0; i<combo.length; i++) {
			if(i < currentNumWinningPlayerPieces -  numSpacesLosingPlayerCouldMove) {
				combo[i] = true;
			} else {
				combo[i] = false;
			}
		}


		//TODO
		//Bonus: Avoid rebuilding completely after every combo iteration by identifying when the pegs placed are the same as last time
		//SolitaryBoard step[] = new SolitaryBoard[numSpacesPlayerCouldMove];
		
		//for every combination of P2 pegs:
		while(combo != null) {
			
			//try to build board with it

			SolitaryBoard comboBoard = minimalCheckmater.hardCopy();
			
			for(int i=0; i<freeSpaceCodes.length; i++) {
				if(combo[i]) {
					comboBoard = comboBoard.moveNullOnLoss(freeSpaceCodes[i], isP1WinningPegs);
					
					if(comboBoard == null) {
						break;
					}
				}
			}

			if(comboBoard != null) {
				System.out.println("Solution:");
				System.out.println("code: " + comboBoard.getUniqueCode());
				comboBoard.draw();
				numSolutions++;
				codes.add(comboBoard.getUniqueCode());
			}
		
			
			combo = UtilityFunctions.getNextCombination(combo);
		}
		
		return ret;
	}
	
}
