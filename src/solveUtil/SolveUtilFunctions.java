package solveUtil;

import env.Constants;
import solveMPMP.SolitaryBoard;

public class SolveUtilFunctions {


	public static int[] initOrderToSolve(int numCells) {
		
		int currentOrder[] = new int[numCells];
		for(int i=0; i<numCells; i++) {
			currentOrder[i] = i;
		}
		
		int numSqaresWithCellInCorner[] = new int[numCells];
		
		for(int i=0; i<numCells; i++) {
			numSqaresWithCellInCorner[i] = getNumSqaresCellCouldParticipateIn(i, numCells);
		}
		
		
		for(int i=0; i<numCells; i++) {
			int bestIndex = i;
			for(int j=i+1; j<numCells; j++) {
				
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
	

	public static int getNumSqaresCellCouldParticipateIn(int code, int numCells) {
		
		int N = (int)Math.sqrt(numCells);
		
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
	
	
	//TODO: should be in Solve
	// And should be in SolitaryBoard
	public static boolean[][] getEmptyCells(SolitaryBoard current) {
		int table[][] = current.getTable();
		boolean emptySpots[][] = new boolean[table.length][table.length];

		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				if(table[i][j] == Constants.EMPTY) {
					emptySpots[i][j] = true;
				}
			}
		}
		
		return emptySpots;
	}
	
	//Get next location to insert
	public static int[] getNextCellToConsider(boolean emptyCells[][], int orderToSolve[]) {
		return getNextCellToConsider(emptyCells, orderToSolve, 0);
	}
	public static int[] getNextCellToConsider(boolean emptyCells[][], int orderToSolve[], int numEmptyCellsToSkip) {	
		int nextI = -1;
		int nextJ = -1;
		
		int numEmptyCellsSkipped = 0;
		
		for(int k=0; k<orderToSolve.length; k++) {
			int i = orderToSolve[k] / emptyCells.length;
			int j = orderToSolve[k] % emptyCells.length;
			
			if(emptyCells[i][j]) {
				
				if(numEmptyCellsSkipped == numEmptyCellsToSkip) {
					nextI = i;
					nextJ = j;
					break;
				}
				numEmptyCellsSkipped++;

			}
		}
		
		return new int[] { nextI, nextJ };
	}
	
	public static int getIndexNextCellToConsider(boolean emptyCells[][], int orderToSolve[]) {
		return getIndexNextCellToConsider(emptyCells, orderToSolve, 0);
	}
	public static int getIndexNextCellToConsider(boolean emptyCells[][], int orderToSolve[], int numEmptyCellsToSkip) {	
		int nextI = -1;
		int nextJ = -1;
		
		int numEmptyCellsSkipped = 0;
		
		int ret=0;
		for(; ret<orderToSolve.length; ret++) {
			int i = orderToSolve[ret] / emptyCells.length;
			int j = orderToSolve[ret] % emptyCells.length;
			
			if(emptyCells[i][j]) {
				
				if(numEmptyCellsSkipped == numEmptyCellsToSkip) {
					nextI = i;
					nextJ = j;
					break;
				}
				numEmptyCellsSkipped++;

			}
		}
		
		return ret;
	}

	

	public static boolean P1TURN = true;
	public static boolean P2TURN = false;
	
	
	public static int NO_LIMIT = -1;


	public static SolitaryBoard insertAllImpliedPegsForTieGame(SolitaryBoard current, boolean noLimitPegsOfOneColour) {

		int table[][] = current.getTable();
		boolean p1Movable[][] = current.getP1Movable();
		boolean p2Movable[][] = current.getP2Movable();
		
		boolean tryAgain = true;
		boolean reinitBecausePegWasInserted = false;
		
		int curNumP1Pegs = current.getNumPiecesForPlayer1();
		int curNumP2Pegs = current.getNumPiecesForPlayer2();
		
		int limitP1Pegs = numP1PiecesNeededForTie(table);
		int limitP2Pegs = numP2PiecesNeededForTie(table);
		
		while(tryAgain == true) {
			tryAgain = false;
			
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table[i].length; j++) {
					
					if(table[i][j] == Constants.EMPTY) {
						
						if(p1Movable[i][j] == false && 
								p2Movable[i][j] == false) {

							return null;
							
						
						} else if(p1Movable[i][j] && 
								p2Movable[i][j] == false) {
							
							curNumP1Pegs++;
							if(noLimitPegsOfOneColour == false && curNumP1Pegs > limitP1Pegs) {
								return null;
							}
							
							current = current.moveNullOnLoss(i, j, P1TURN);
							
							tryAgain = true;
							reinitBecausePegWasInserted = true;
							
						} else if(p1Movable[i][j] == false && p2Movable[i][j]) {
	
							curNumP2Pegs++;
							if(noLimitPegsOfOneColour == false && curNumP2Pegs > limitP2Pegs) {
								return null;
							}
							
							current = current.moveNullOnLoss(i, j, P2TURN);
							tryAgain = true;
							reinitBecausePegWasInserted = true;
							
						}
						
						if(reinitBecausePegWasInserted) {
							
							if(current == null) {
								return null;
							}
							
							table= current.getTable();
							p1Movable = current.getP1Movable();
							p2Movable = current.getP2Movable();
							
							reinitBecausePegWasInserted = false;
						}
					}
					
				}
			}
		}
		
		return current;
	}
	
	
	
	//TODO: this is slower than not using it.
	//TODO: Maybe try to extend the board and make this dynamic!
	// ex: getBlanksUsedIntentionally should not be called all the time
	
	public static boolean isStillAbleToGetToPartiallyDoneGame(SolitaryBoard current, int limitP1Pegs, int limitP2Pegs, int orderToSolve[], int numSpotsUsed) {


		//Get table properties:
		int table[][] = current.getTable();
		boolean p1Movable[][] = current.getP1Movable();
		boolean p2Movable[][] = current.getP2Movable();

		int curNumP1Pegs = current.getNumPiecesForPlayer1();
		int curNumP2Pegs = current.getNumPiecesForPlayer2();
		
		int curNumBlanksUsed = getNumBlanksUsedIntentionally(current, orderToSolve, numSpotsUsed);
		boolean blankUsed[][] = getBlanksUsedIntentionally(current, orderToSolve, numSpotsUsed);
		
		//Get limit blanks used:
		int numCells = table.length * table[0].length;
		int limitBlankSpacesUsed = numCells - limitP1Pegs - limitP2Pegs;
		
		//Setup variables:
		int numCouldBeP1Colour = 0;
		int numCouldBeP2Colour = 0;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				
				if(blankUsed[i][j] == false && table[i][j] == Constants.EMPTY) {
					
					if(p1Movable[i][j]) {
						numCouldBeP1Colour++;
					}
					
					if(p2Movable[i][j]) {
						numCouldBeP2Colour++;
					}
					
					if(p1Movable[i][j] == false && 
							p2Movable[i][j] == false) {

						curNumBlanksUsed++;
						
						if(curNumBlanksUsed > limitBlankSpacesUsed) {
							return false;
						}
					
					}
					
				}
				
			}
		}
		
		//test if it's not impossible that the pegs could still be filled up:
		if(curNumP1Pegs + numCouldBeP1Colour < limitP1Pegs) {
			return false;
	
		} else if(curNumP2Pegs + numCouldBeP2Colour < limitP2Pegs) {
			return false;
		
		} else { 
			//If still possible that the pegs could be filled up, 
			return true;
		}
	}
	
	public static boolean[][] getBlanksUsedIntentionally(SolitaryBoard current, int orderToSolve[], int numSpotsUsed) {
		int table[][] = current.getTable();
		int numBlanksUsed = 0;
		boolean ret[][] = new boolean[table.length][table[0].length];
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret.length; j++) {
				ret[i][j] = false;
			}
		}
		
		
		
		for(int k=0; k<numSpotsUsed; k++) {
			int i = orderToSolve[k] / table.length;
			int j = orderToSolve[k] % table.length;
			if(table[i][j] == Constants.EMPTY) {
				ret[i][j] = true;
			}
		}
		
		return ret;
	}
	
	public static int getNumBlanksUsedIntentionally(SolitaryBoard current, int orderToSolve[], int numSpotsUsed) {
		int table[][] = current.getTable();
		int numBlanksUsed = 0;
		
		for(int k=0; k<numSpotsUsed; k++) {
			int i = orderToSolve[k] / table.length;
			int j = orderToSolve[k] % table.length;
			if(table[i][j] == Constants.EMPTY) {
				numBlanksUsed++;
			}
		}
		
		return numBlanksUsed;
	}


	public static int numP1PiecesNeededForTie(int table[][]) {
		return numP1PiecesNeeded(table.length * table.length);
	}
	
	public static int numP2PiecesNeededForTie(int table[][]) {
		return numP2PiecesNeeded(table.length * table.length);
	}
	
	public static int numP1PiecesNeeded(int numPegsOnBoard) {
		return numPegsOnBoard - numP2PiecesNeeded(numPegsOnBoard);
	}
	
	public static int numP2PiecesNeeded(int numPegsOnBoard) {
		return numPegsOnBoard/2;
	}
	
	
	public static int getNumSpacesPlayerCouldMove(SolitaryBoard current, boolean isTryingToFindP1loss) {
		int ret = 0;
		
		boolean optionsToMove[][];
		if(isTryingToFindP1loss) {
			optionsToMove = current.getP1Movable();
		} else  {
			optionsToMove = current.getP2Movable();
		}
		
		for(int i=0; i<optionsToMove.length; i++) {
			for(int j=0; j<optionsToMove[i].length; j++) {
				if(optionsToMove[i][j]) {
					ret++;
				}
			}
		}
		
		return ret;
	}
	
}
