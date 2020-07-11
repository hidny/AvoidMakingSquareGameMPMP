package main;

import aiAlgo.AlphaBetaPrunePlayer;
import aiAlgo.MinMaxPlayer;
import consolePlayer.ConsolePlayer;
import env.Board;
import env.Constants;
import player.Player;

public class GameStarter {

	//According to alpha beta pruning:
	//Player 2 wins the 4x4 board.
	//I don't know if there's a bug or if that's actually true.
	//Will investigate further.
	

	public static void main(String[] args) {
		Constants.SIZE = 4;
		//ComputerHumanVsAlphaBeta();
		ComputerAlphaBetaVsAlphaBeta();
	}
	
	public static void ComputerMinMaxVsHuman() {
		System.out.println("Computer (minmax) vs Human");
		PlayGame(new MinMaxPlayer(), new ConsolePlayer());
		
	}
	
	public static void ComputerAlphaBetaVsHuman() {
		System.out.println("Computer (alpha beta) vs Human");
		PlayGame(new AlphaBetaPrunePlayer(), new ConsolePlayer());
		
	}
	
	public static void ComputerAlphaBetaVsAlphaBeta() {
		System.out.println("Computer (alpha beta) vs Computer (alpha beta)");
		PlayGame(new AlphaBetaPrunePlayer(), new ConsolePlayer());
		
	}
	
	public static void ComputerHumanVsAlphaBeta() {
		System.out.println("Human vs Computer (alpha beta)");
		PlayGame(new ConsolePlayer(), new AlphaBetaPrunePlayer());
		
	}
	
	public static void PlayGame(Player p1, Player p2) {
		
		Board board = new Board();
		
		while(board.currentPlayerCantMove() == false) {

			System.out.println();
			System.out.println("****************");
			if(board.isP1turn()) {
				if(p1 instanceof ConsolePlayer ) {
					System.out.println("Player 1's turn (Your turn):");
				} else {
					System.out.println("Player 1's turn (Computer's turn):");
				}
			} else {
				if(p2 instanceof ConsolePlayer ) {
					System.out.println("Player 2's turn (Your turn):");
				} else {
					System.out.println("Player 2's turn (Computer's turn):");
				}
			}
			
			board.draw();
			
			int moveCode = -1;
			//TODO: simplify code.
			if(board.isP1turn()) {
				moveCode = p1.getBestMove(board, Constants.SIZE * Constants.SIZE);
			} else {
				moveCode = p2.getBestMove(board, Constants.SIZE * Constants.SIZE);
			}
			
			if(moveCode == -1) {
				if(board.isP1turn()) {
					System.out.println("Player 1 concedes defeat!");
					break;
				} else {
					System.out.println("Player 2 concedes defeat!");
					break;
				}
			}
			board = board.playMove(moveCode);
			
			if(board == null) {
				System.out.println("You made a bad move, and now you lose!");
			}
		}


		System.out.println();
		System.out.println("****************");
		System.out.println("Final position:");
		board.draw();
		
		//TODO: record outcome
		System.out.println("Game Over (TODO: who won? or was it a tie?)");
	}

}
