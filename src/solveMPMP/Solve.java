package solveMPMP;

import java.util.Scanner;

import env.Constants;
import solveUtil.SolveUtilFunctions;

public class Solve {

	
	public static int N = 7;

	public static boolean NO_LIMIT_PEGS_OF_ONE_COLOUR = true;

	public static int NUM_CELLS = N*N;
	
	//RIGHT ANSWER:
	//1: 1
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

		SolitaryBoard board = new SolitaryBoard(N);
		
		int orderToSolve[] = SolveUtilFunctions.initOrderToSolve(NUM_CELLS); 
		
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
	
	
	
	public static Scanner in = new Scanner(System.in);
	
	public static int numSolutions = 0;
	

	
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
				break;
			}
		}
		//End get next empty spot
		
		
		//Try playing a move for P1:
		if(NO_LIMIT_PEGS_OF_ONE_COLOUR || current.getNumPiecesForPlayer1() < PIECE_NEEDED_P1) {
		
			//try insert P1
			SolitaryBoard tmpBoard = current.moveNullOnLoss(nextI, nextJ, SolveUtilFunctions.P1TURN);
			
			if(tmpBoard != null) {
				//And insert all implied pegs
				SolitaryBoard nextPosToAnalyze = SolveUtilFunctions.insertAllImpliedPegsForTieGame(tmpBoard, NO_LIMIT_PEGS_OF_ONE_COLOUR);
				
				if(nextPosToAnalyze != null) {
					solve(nextPosToAnalyze, orderToSolve);
				}
			}
			//solve for next pos
		}
		
		
		//Try playing a move for P2 instead:
		if(NO_LIMIT_PEGS_OF_ONE_COLOUR || current.getNumPiecesForPlayer2() < PIECE_NEEDED_P2) {
			
			//try insert P2
			SolitaryBoard tmpBoard2 = current.moveNullOnLoss(nextI, nextJ, SolveUtilFunctions.P2TURN);
			
			if(tmpBoard2 != null) {
				//And insert all implied pegs
				
				SolitaryBoard nextPosToAnalyze = SolveUtilFunctions.insertAllImpliedPegsForTieGame(tmpBoard2, NO_LIMIT_PEGS_OF_ONE_COLOUR);
				
				if(nextPosToAnalyze != null) {
					solve(nextPosToAnalyze, orderToSolve);
				}
				
			}
			//solve for next pos
		
		}
		//Done.
			
		
	}
	
}
