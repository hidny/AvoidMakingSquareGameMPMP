package consolePlayer;

import java.math.BigInteger;
import java.util.Scanner;

import env.Board;
import env.Constants;
import lossLookupDB.LossLookupDBSimple;

//TODO: define N in only 1 spot!

public class LossLookupPlayer implements player.PlayerI {

	
	public LossLookupPlayer() {
		
		LossLookupDBSimple.setup();
	}
	
	//TODO: maybe print the evals?
	public int getBestMove(Board node, int depth) {
		
		if(depth == 0 || node.currentPlayerCantMove() ) {
			return -1;
			
		}
		
		int cheatSheet[][] = new int[node.getTable().length][node.getTable().length];
		
		for(int i=0; i<cheatSheet.length; i++) {
			for(int j=0; j<cheatSheet.length; j++) {
				
				Board tmpMove = node.moveNullOnLoss(i, j);
				if(tmpMove != null) {
					
					BigInteger moveCode = tmpMove.getUniqueCode();
					
					if(LossLookupDBSimple.isP1LosingPos(moveCode)) {
						cheatSheet[i][j] = 2;
						
					} else if(LossLookupDBSimple.isP2LosingPos(moveCode)) {
						cheatSheet[i][j] = 1;

					} else {
						cheatSheet[i][j] = 0;
					}
					
					if(LossLookupDBSimple.isP1LosingPos(moveCode) && LossLookupDBSimple.isP2LosingPos(moveCode)) {
						System.out.println("ERROR: this position is losing for both players!");
						System.exit(1);
					}
					
					
				} else {
					cheatSheet[i][j] = -1;
				}
			}
		}
		

		node.draw();
		System.out.println("Cheat sheet:");
		
		for(int i=0; i<cheatSheet.length; i++) {
			for(int j=0; j<cheatSheet.length; j++) {
				if(cheatSheet[i][j] == 0) {
					System.out.print("T");
				} else if(cheatSheet[i][j] == -1) {
					System.out.print("-");
				} else {
					if(node.isP1turn()) {
						if(cheatSheet[i][j] == 2) {
							System.out.print("L");
						} else if(cheatSheet[i][j] == 1) {
							System.out.print("W");
							
						} else {
							System.out.println("????");
							System.exit(1);
						}
					} else {
						if(cheatSheet[i][j] == 2) {
							System.out.print("W");
						} else if(cheatSheet[i][j] == 1) {
							System.out.print("L");
							
						} else {
							System.out.println("????");
							System.exit(1);
						}
					}
				}
					
			}
			System.out.println();
		}
		boolean foundAnswer;

		int firstCoord = -1;
		int secondCoord = -1;
		
		Scanner in = new Scanner(System.in);

		do {
			foundAnswer = false;

			System.out.println("What's your move in the format of \"i,j\":");
			String line = in.nextLine();
			
			String array[] = line.split(",");
			
			if(array.length != 2) {
				continue;
			}
			try {
				firstCoord = Integer.parseInt(array[0]);
				secondCoord = Integer.parseInt(array[1]);
				
				if(firstCoord >= 0 && firstCoord < Constants.SIZE
						&& secondCoord >= 0 && secondCoord < Constants.SIZE) {
					
					if(node.moveNullOnLoss(firstCoord, secondCoord) != null) {

						foundAnswer = true;
					}
				}
			} catch(Exception e) {
				continue;
			}

		} while(foundAnswer == false);
		
		
		
		return firstCoord * Constants.SIZE + secondCoord;

	}
}
