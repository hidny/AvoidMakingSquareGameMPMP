package solveMPMP;

import java.util.Scanner;

import env.Constants;

public class Solve {

	
	public static int N = 5;

	//TODO
	public static boolean NO_LIMIT_PEGS_OF_ONE_COLOUR = false;

	public static int NUM_CELLS = N*N;
	
	//RIGHT ANSWER:
	//1: 2
	//2: 6
	//3: 92
	//4: 2094
	//5: 2704  //Solution in youtube comments!
	//6: 24
	//7: 0
	//8: 0
	
	
	//Tie solutions without correct amount of Player 1 pegs:
	//1: 2
	//2: 14
	//3: 248
	//4: 5006
	//5: 7120
	//6: 56
	//7: 0
	//8: 0
	//...
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SolitaryBoard board = new SolitaryBoard(N);
		
		int orderToSolve[] = initOrderToSolve(); 
		
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
		
		solve(board, orderToSolve);
		
		System.out.println("Done");
		
		if(NO_LIMIT_PEGS_OF_ONE_COLOUR == false) {
			System.out.println("In the case when Player1 and Player2 take turns playing,");
			System.out.println("I Found " + numSolutions + " solutions for a " + N + "x" + N + " board.");
			
		} else {
			System.out.println("In the case where we just need to colour the board such that there's no squares:");
			System.out.println("I Found " + numSolutions + " solutions for a " + N + "x" + N + " board.");
		}
		
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
	
	
	public static void solve(SolitaryBoard current, int orderToSolve[]) {
		
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
		
		if(curSolved) {
			
			if(NO_LIMIT_PEGS_OF_ONE_COLOUR || current.getNumPiecesForPlayer1() == PIECE_NEEDED_P1) {
				System.out.println("Found a solution!");
				numSolutions++;
				System.out.println("Solution #" + numSolutions);
				current.draw();
				System.out.println();
			}
			return;
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
		
		
		

		
		//Try playing a move for P1:
		if(NO_LIMIT_PEGS_OF_ONE_COLOUR || current.getNumPiecesForPlayer1() < PIECE_NEEDED_P1) {
		
			//try insert P1
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, P1TURN);
			
			if(tmpBoard != null) {
				//And insert all implied pegs
				SolitaryBoard nextPosToAnalyze = insertAllImpliedPegsForATie(tmpBoard);
				
				if(nextPosToAnalyze != null) {
					solve(nextPosToAnalyze, orderToSolve);
				}
			}
			//solve for next pos
		}
		
		
		//Try playing a move for P2 instead:
		if(NO_LIMIT_PEGS_OF_ONE_COLOUR || current.getNumPiecesForPlayer2() < PIECE_NEEDED_P2) {
			
			//try insert P2
			SolitaryBoard tmpBoard2 = current.moveNullOnLoss(nextI, nextJ, P2TURN);
			
			if(tmpBoard2 != null) {
				//And insert all implied pegs
				
				SolitaryBoard nextPosToAnalyze = insertAllImpliedPegsForATie(tmpBoard2);
				
				if(nextPosToAnalyze != null) {
					solve(nextPosToAnalyze, orderToSolve);
				}
				
			}
			//solve for next pos
		
		}
		//Done.
			
		
	}
	
	//mutates input
	public static SolitaryBoard insertAllImpliedPegsForATie(SolitaryBoard current) {
		
		int table[][] = current.getTable();
		boolean p1Movable[][] = current.getP1Movable();
		boolean p2Movable[][] = current.getP2Movable();
		
		boolean tryAgain = true;
		boolean reinitBecausePegWasInserted = false;
		
		while(tryAgain == true) {
			tryAgain = false;
			
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table[i].length; j++) {
					
					if(table[i][j] == Constants.EMPTY) {
						
						if(p1Movable[i][j] == false && p2Movable[i][j] == false) {
							return null;
						
						} else if(p1Movable[i][j] && p2Movable[i][j] == false) {
							current = current.moveNullOnLoss(i, j, P1TURN);
							
							
							tryAgain = true;
							reinitBecausePegWasInserted = true;
							
						} else if(p1Movable[i][j] == false && p2Movable[i][j]) {
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

}
