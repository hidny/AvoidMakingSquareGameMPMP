package consolePlayer;

import java.util.Scanner;

import env.Board;
import env.Constants;

public class ConsolePlayer implements player.PlayerI {

	public static Scanner in = new Scanner(System.in);
	
	public int getBestMove(Board node, int depth) {
		int retMoveCode = -1;
		
		while(true) {
			retMoveCode = ConsolePlayer.getBestMoveInner(node, Constants.SIZE * Constants.SIZE);
			Board boardTmp =  node.moveNullOnLoss(retMoveCode);
			if(boardTmp == null) {
				node.draw();
				System.out.println("You made a bad move! Try again!");
				System.out.println();
				System.out.println("Your turn:");
				
			} else {
				break;
			}
		}
		
		return retMoveCode;
		
	}
	
	public static int getBestMoveInner(Board node, int depth) {
		
		node.draw();
		boolean foundAnswer;

		int firstCoord = -1;
		int secondCoord = -1;

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
					
					//TODO: if not playable, maybe do a warning?

					foundAnswer = true;
				}
			} catch(Exception e) {
				continue;
			}

		} while(foundAnswer == false);
		
		
		
		return firstCoord * Constants.SIZE + secondCoord;
	}
}
