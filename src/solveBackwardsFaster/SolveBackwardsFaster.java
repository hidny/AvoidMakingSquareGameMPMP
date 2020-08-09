package solveBackwardsFaster;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import solveMPMP.SolitaryBoard;
import solveUtil.SolveUtilFunctions;
import solveUtil.UtilityFunctions;


//TODO: figure out if ties are already handled by both players
// I suspect it's handled by one player, but not the other.

//TODO: need to debug!!!
public class SolveBackwardsFaster {

	//TODO: only deal with 1 colour
	
	public static int N = 4;
	public static int NUM_CELLS = N * N;
	public static boolean FIND_PLAYER1_LOSSES = true;
	
	public static long pascalsTriangle[][] = UtilityFunctions.createPascalTriangle(NUM_CELLS + 1);


	public static Scanner in = new Scanner(System.in);
	
	public static int numSolutions = 0;

	public static HashSet<BigInteger> codes = new HashSet<BigInteger>();

	//Not quite...
	//num player 1 losing/tying solutions for a 4x4 board: 25110
	//Searched where there were an EVEN number of empty cells left in the position
	
	public static void main(String[] args) {
		getLosses();
	}
	
	
	public static void getTies() {

		//TODO: tihs is wrong (I find ties for one, but not the other)
		//TODO Find ties
		solveForTiedFinalPositions();
		
		System.out.println("Ties:");

		debugPrintSolutionCodes();
		
	}
	
	public static void getLosses() {
		
		System.out.println("Start faster backwards solver");
		
		int emptySpaces;
		
		boolean searchPosWhereOddNumSpacesLeft = false;
		
		if((FIND_PLAYER1_LOSSES == true && N%2 == 1) ||
				(FIND_PLAYER1_LOSSES == false && N%2 == 0)) {
			searchPosWhereOddNumSpacesLeft = true;
		}
		
		
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
			

			System.out.println("Num possible duplicated solutions so far: " + numSolutions);
			System.out.println("Num unique solutions so far: " + codes.size());
		}
		
		if(FIND_PLAYER1_LOSSES == true) {
			System.out.println("num player 1 losing/tying solutions for a " + N + "x" + N + " board: " + numSolutions);
		} else {
			System.out.println("num player 2 losing/tying solutions for a " + N + "x" + N + " board: " + numSolutions);
		}
		
		System.out.println("Num of unique solutions for faster version: " + codes.size());
		
		if(searchPosWhereOddNumSpacesLeft == true) {
			System.out.println("Searched where there were an ODD number of empty cells left in the position");
		} else {
			System.out.println("Searched where there were an EVEN number of empty cells left in the position");
		}
		
		//debugPrintSolutionCodes();
		
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
	
	
	//I made it faster by only searching with 1 colour of peg first
	// and then searching for the pegs of the other colour

	//TODO: start with 4x4 case because it can be completely checked against AlphaBetaPrunePlayer
	
	public static ArrayList<BigInteger> solveForTiedFinalPositions() {
		return solvePositionsWhereNextMoveLoses(NUM_CELLS);
	}
	
	
	//Recursive function to get
	//Get all losing positions for player 2
	public static ArrayList<BigInteger> solvePositionsWhereNextMoveLoses(int numPegsToPlaceOnBoard) {
		
		SolitaryBoard board = new SolitaryBoard(N);
		
		int orderToSolve[] = SolveUtilFunctions.initOrderToSolve(NUM_CELLS); 
		
		printOrderToSolve(orderToSolve);
		
		return solveLosingPositions1MoveAway(board, orderToSolve, 0, numPegsToPlaceOnBoard);
	}
	
	
	public static void printOrderToSolve(int orderToSolve[]) {
		
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
	}
	
	public static int debugCount = 0;
	
	public static ArrayList<BigInteger> solveLosingPositions1MoveAway(SolitaryBoard current, int orderToSolve[], int indexToAdd, int numPegToPlacesOnBoard) {
		
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();

		
		int numLosingPegsNeeded = getNumLosingPegsNeeded(numPegToPlacesOnBoard);
	

		//Edge case that I'm just going to ignore:
		if(numLosingPegsNeeded == 0 && numPegToPlacesOnBoard <= 1) {
			return ret;
		}
		
		//Try inserting another losing peg in current Coordinate:
		boolean isP1Losing = isP1Losing(numPegToPlacesOnBoard);
		
		int nextCoordCodeToPlace = orderToSolve[indexToAdd];
		int nextI = nextCoordCodeToPlace / N;
		int nextJ = nextCoordCodeToPlace % N;
		
		SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, isP1Losing);
		
		if(tmpBoard != null) {
			
			if(tmpBoard.getNumPieces() == numLosingPegsNeeded) {
				ret.addAll( findSolutionsByAddingOpponentPegs(tmpBoard, isP1Losing, numPegToPlacesOnBoard));
				
				debugCount++;
				
				if(debugCount % 10000 == 0) {
					System.out.println("Debug count: " + debugCount);
				}
				
			} else if(tmpBoard.getNumPieces() < numLosingPegsNeeded) {
				
				ret.addAll(solveLosingPositions1MoveAway(tmpBoard, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard));
				
				
			} else {
				System.out.println("ERROR: there's too many pegs placed in solveLosingPositions1MoveAway");
				System.exit(1);
				
			}
			
		}
		//End try inserting another losing peg in current Coord
		
		//Try not filling the current space:
		int numSpacesInIndexesNotChecked = NUM_CELLS - indexToAdd - 1;
		if(current.getNumPieces() < numLosingPegsNeeded
				&& current.getNumPieces() + numSpacesInIndexesNotChecked >= numLosingPegsNeeded) {
		
			ret.addAll(solveLosingPositions1MoveAway(current, orderToSolve, indexToAdd+1, numPegToPlacesOnBoard));
		}
		//End try not filling the current space
		
		return ret;
	}
	

	public static boolean isP1Losing(int numPegToPlacesOnBoard) {
		if(numPegToPlacesOnBoard % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//get Num losing pegs to place based on total number of pegs to place:
	public static int getNumLosingPegsNeeded(int numPegToPlacesOnBoard) {
		
		if(numPegToPlacesOnBoard % 2 == 0) {
			return SolveUtilFunctions.numP1PiecesNeeded(numPegToPlacesOnBoard);
			
		} else {
			return SolveUtilFunctions.numP2PiecesNeeded(numPegToPlacesOnBoard);
		}
	}
	
	//pre: losing player's pegs are all already place in current and they
	//    are placed in legal positions.
	public static ArrayList<BigInteger> findSolutionsByAddingOpponentPegs(SolitaryBoard current, boolean isP1Losing, int numPegToPlacesOnBoard) {
		
		
		int numSpacesLosingPlayerCouldMove = SolveUtilFunctions.getNumSpacesPlayerCouldMove(current, isP1Losing);
		
		int currentNumWinningPlayerPieces = -1;
		if(isP1Losing) {
			currentNumWinningPlayerPieces = SolveUtilFunctions.numP2PiecesNeeded(numPegToPlacesOnBoard);
		} else {
			currentNumWinningPlayerPieces = SolveUtilFunctions.numP1PiecesNeeded(numPegToPlacesOnBoard);
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
		
		
		// After adding the minimum amount of winning pegs to 'checkmate' the loser,
		// we now have to try to add the rest to the required winning pegs.
		// There are (numMovableLocation(or freeSpaceCodes.length) choose (num winning peg left to add)) combinations to check
		int freeSpaceCodes[] = SolveUtilFunctions.getMovableLocations(minimalCheckmater, isP1WinningPegs);
		
		boolean combo[] = new boolean[freeSpaceCodes.length];
		if( currentNumWinningPlayerPieces -  numSpacesLosingPlayerCouldMove <= freeSpaceCodes.length) {
			for(int i=0; i<combo.length; i++) {
				if(i < currentNumWinningPlayerPieces -  numSpacesLosingPlayerCouldMove) {
					combo[i] = true;
				} else {
					combo[i] = false;
				}
			}
		} else {
			//if numMovableLocation(or freeSpaceCodes.length) < num winning peg left to add,
			//that means (numMovableLocation choose num winning peg left to add) = 0 and there's nothing to check
			combo = null;
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
				
				//System.out.println("Solution:");
				//System.out.println("code: " + comboBoard.getUniqueCode());
				//comboBoard.draw();
				
				numSolutions++;
				
				//TODO: use codes later...
				codes.add(comboBoard.getUniqueCode());
			}
		
			
			combo = UtilityFunctions.getNextCombination(combo);
		}
		
		return ret;
	}
	
}

/*
//After 2 empty spaces: 2761596
//After 4 empty spaces: Num possible duplicated solutions so far: 18268000
Num possible duplicated solutions so far: 20295096
Num unique solutions so far: 0
num player 2 losing/tying solutions for a 5x5 board: 20295096
Num of unique solutions for faster version: 0
Searched where there were an EVEN number of empty cells left in the position
*/


/*
 * 
 * Num possible duplicated solutions so far: 16240656
Num unique solutions so far: 0
Solve Losing positions (or tying) with 5 empty spaces left:
Printing order to solve:

 * Num possible duplicated solutions so far: 37381452
Num unique solutions so far: 0
Solve Losing positions (or tying) with 7 empty spaces left:
Printing order to solve:

 * Num possible duplicated solutions so far: 38244068
Num unique solutions so far: 0
num player 1 losing/tying solutions for a 5x5 board: 38244068
Num of unique solutions for faster version: 0
Searched where there were an ODD number of empty cells left in the position
Solution codes listed in order:
*/
