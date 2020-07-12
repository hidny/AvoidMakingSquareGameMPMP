package main;

import aiAlgo.AlphaBetaPrunePlayer;
import aiAlgo.AlphaBetaPrunePlayerWithMemory;
import aiAlgo.AlphaBetaPruneWithMemoryAndEval;
import aiAlgo.MinMaxPlayer;
import aiAlgoevaluator.BasicEval;
import aiAlgoevaluator.BasicEvalWithFraction;
import aiAlgoevaluator.halfwayEvalSUCKS;
import consolePlayer.ConsolePlayer;
import env.Board;
import env.Constants;
import player.PlayerI;

public class GameStarter {

	//According to alpha beta pruning:
	//Player 2 wins the 4x4 board.
	//I don't know if there's a bug or if that's actually true.
	//Will investigate further.
	
	//For size =4:
	//On move 1:
	//Player 1 concedes defeat!

	public static void main(String[] args) {
		
		//So far ComputerAlphaBetaMemVsAlphaBetaMemAndBasicEval is the fastest
		//ComputerAlphaBetaMemVsAlphaBetaMemAndFudgeFactorEval is just barely slower
		
		//TODO: actually solve MP MP
		//TODO2: find the "prefered spaces" for 5x5 and maybe include that in eval.
		
		//TODO3: maybe having pegs that are aligned is bad?
		
		//ComputerHumanVsAlphaBeta();
		//ComputerAlphaBetaVsAlphaBeta();
		//ComputerAlphaBetaMemVsAlphaBetaMem();
		
		//Best so far:
		ComputerAlphaBetaMemVsAlphaBetaMemAndBasicEval();
		
		
		//ComputerAlphaBetaVsAlphaBetaBothWithHalfWayEvalSucks();
		//ComputerAlphaBetaMemVsAlphaBetaMemAndFudgeFactorEval();
		//ComputerAlphaBetaMemVsAlphaBetaMemAndBasicEvalMinSaveDepth();
		
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
	
	public static void HumanVsAlphaBeta() {
		System.out.println("Human vs Computer (alpha beta)");
		PlayGame(new ConsolePlayer(), new AlphaBetaPrunePlayer());
		
	}
	
	public static void ComputerAlphaBetaMemVsAlphaBetaMem() {
		System.out.println("Computer (alpha beta with memory) vs Computer (alpha beta with memory)");
		PlayGame(new AlphaBetaPrunePlayerWithMemory(), new AlphaBetaPrunePlayerWithMemory());
		
	}
	
	public static void ComputerAlphaBetaMemVsAlphaBetaMemAndBasicEval() {
		System.out.println("Computer (alpha beta with memory and eval) vs Computer (alpha beta with memory and eval)");
		PlayGame(new AlphaBetaPruneWithMemoryAndEval(new BasicEval()), new AlphaBetaPruneWithMemoryAndEval(new BasicEval()));
		
	}
	
//Sucks
	public static void ComputerAlphaBetaVsAlphaBetaBothWithHalfWayEvalSucks() {
		System.out.println("Computer (alpha beta with half-way eval) vs Computer (alpha beta with half-way eval)");
		PlayGame(new AlphaBetaPruneWithMemoryAndEval(new halfwayEvalSUCKS()), new AlphaBetaPruneWithMemoryAndEval(new halfwayEvalSUCKS()));
		
	}
	
	//Not better than ComputerAlphaBetaMemVsAlphaBetaMemAndBasicEval
	public static void ComputerAlphaBetaMemVsAlphaBetaMemAndFudgeFactorEval() {
		System.out.println("Computer (alpha beta with memory and fudge factor eval) vs Computer (alpha beta with memoryand fudge factor val)");
		PlayGame(new AlphaBetaPruneWithMemoryAndEval(new BasicEvalWithFraction()), new AlphaBetaPruneWithMemoryAndEval(new BasicEvalWithFraction()));
		
	}
	
	//Sucks:
	//Maybe make it MIN_PERM_SAVE DEPTH?
	public static void ComputerAlphaBetaMemVsAlphaBetaMemAndBasicEvalMinSaveDepth() {
		System.out.println("Computer (alpha beta with memory and eval) vs Computer (alpha beta with memory and eval)");
		int MIN_SAVE_DEPTH = 7;
		PlayGame(new AlphaBetaPruneWithMemoryAndEval(new BasicEval(), MIN_SAVE_DEPTH), new AlphaBetaPruneWithMemoryAndEval(new BasicEval(), MIN_SAVE_DEPTH));
		
	}
	
	public static void PlayGame(PlayerI p1, PlayerI p2) {
		
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
