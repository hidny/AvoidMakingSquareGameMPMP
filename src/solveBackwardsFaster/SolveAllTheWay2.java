package solveBackwardsFaster;

import java.math.BigInteger;
import java.util.HashSet;

import env.Constants;
import solveMPMP.SolitaryBoard;

//TODO N=3 contradicts results of other program... :(
public class SolveAllTheWay2 {

	public static int N = 4;
	public static int NUM_CELLS = N * N;
	
	

	public static HashSet<BigInteger> P1LossCodes[] = new HashSet[NUM_CELLS + 1];
	public static HashSet<BigInteger> P2LossCodes[] = new HashSet[NUM_CELLS + 1];
	
	
	public static int numLossesFound = 0;
	
	public static void main(String args[]) {
		init();
		
		//getLosses(false);
		//Number of immediate losing positions for 4 empty spaces: 15506404
		//Num positions to go thru: 2761596
		
		getLosses(true);
	}
	
	public static void init() {
		SolveBackwardsFaster.N= N;
		SolveBackwardsFaster.NUM_CELLS = NUM_CELLS;
		
		for(int i=0; i<P1LossCodes.length; i++) {
			P1LossCodes[i] = new HashSet<BigInteger>();
			P2LossCodes[i] = new HashSet<BigInteger>();
		}
		
	}
	
	public static long numLosingPos = 0L;
	
	//TODO: get Draws!

	public static void getLosses(boolean findP1Losses) {
		
		HashSet<BigInteger> PlayerLossCodes[];

		int winningColour = -1;
		int losingColour = -1;
		
		if(findP1Losses) {
			PlayerLossCodes = P1LossCodes;
			losingColour = Constants.P1_COLOUR;
			winningColour = Constants.P2_COLOUR;
		} else {
			PlayerLossCodes = P2LossCodes;
			losingColour = Constants.P2_COLOUR;
			winningColour = Constants.P1_COLOUR;
		}
		
		System.out.println("Start faster backwards solver");
		
		int emptySpaces;
		
		boolean searchPosWhereOddNumSpacesLeft = false;
		
		if((findP1Losses == true && N%2 == 1) ||
				(findP1Losses == false && N%2 == 0)) {

			searchPosWhereOddNumSpacesLeft = true;
		
		}
		
		
		//Find loses
		for(int i=1; 
				2*i <=NUM_CELLS
				|| (searchPosWhereOddNumSpacesLeft && 2*i - 1 <=NUM_CELLS);
			i++) {
			
			if(searchPosWhereOddNumSpacesLeft) {
				emptySpaces = 2*i - 1;
			} else {
				emptySpaces = 2*i;
			}
			
			
			
			System.out.println("Solve 1 move Losing positions with " + emptySpaces + " empty spaces left:");
			
			//TOOD: put in function
			SolveBackwardsFaster.solvePositionsWhereNextMoveLoses(NUM_CELLS - emptySpaces);
			//Get the position codes found:
			PlayerLossCodes[emptySpaces] = SolveBackwardsFaster.codes;
			//Clear the position codes found:
			SolveBackwardsFaster.codes = new HashSet<BigInteger>();
			//END TODO put in function
			/*
			//DEBUG:
			Object losingCodes[] = PlayerLossCodes[emptySpaces].toArray();
			for(int j=0; j<losingCodes.length; j++) {
				SolitaryBoard losingBoardDEBUG = SolitaryBoard.createBoardFromCode(N, (BigInteger)losingCodes[j]);
				System.out.println("Losing position " + j + ":");
				losingBoardDEBUG.draw();
				System.out.println();
			}
			
			//END DEBUG
			*/
			System.out.println("Number of immediate losing positions for " + emptySpaces + " empty spaces: " + PlayerLossCodes[emptySpaces].size());
			
			//TODO: translate losing positions into losing positions 2 moves back, unless were at the very start.
			if(emptySpaces > 2) {
				
				Object losingCodes2MovesForward[] = PlayerLossCodes[emptySpaces - 2].toArray();
				
				//STEP 1:
				System.out.println("Num positions to go thru step 1: " + losingCodes2MovesForward.length);
				
				for(int j=0; j<losingCodes2MovesForward.length; j++) {
					
					//PROGRESS BAR:
					if(((j * 10) % losingCodes2MovesForward.length) < ((j-1) * 10 % losingCodes2MovesForward.length)) {

						System.out.println("j = " + j);
					}
					//END PROGRESS BAR
					
					
					SolitaryFastBoard losingBoard = new SolitaryFastBoard(N, (BigInteger)losingCodes2MovesForward[j]);
/*
					//jSystem.out.println("Sanity:");
					//SANITY TEST:
					SolitaryBoard losingBoardTEST = SolitaryBoard.createBoardFromCode(N, (BigInteger)losingCodes2MovesForward[j]);
					for(int i2=0; i2<losingBoardTEST.getTable().length; i2++) {
						for(int j2=0; j2<losingBoardTEST.getTable()[0].length; j2++) {
							if(losingBoardTEST.getTable()[i2][j2] != losingBoard.getTable()[i2][j2]) {
								System.out.println("ERROR: boards don't match!");
								System.exit(1);
							}
						}
					}
					
					if(losingBoard.getUniqueCode().compareTo(losingBoardTEST.getUniqueCode()) != 0) {

						System.out.println("ERROR: boards don't match!");
						System.exit(1);
					}
	*/				
					//END SANITY TEST
					
						
					for(int pegwinRM = 0; pegwinRM < NUM_CELLS; pegwinRM++) {
						if(losingBoard.getTable()[pegwinRM / N][pegwinRM % N] != winningColour) {
							continue;
						}
						
						//REMOVE winning peg
						losingBoard.removePeg(pegwinRM);
						
						if(PlayerLossCodes[emptySpaces - 1].contains(losingBoard.getUniqueCode())) {
							losingBoard.addPeg(pegwinRM, findP1Losses == false);
							continue;
						}
						
						PlayerLossCodes[emptySpaces - 1].add(losingBoard.getUniqueCode());
						
						//add winning peg
						losingBoard.addPeg(pegwinRM, findP1Losses == false);
						
					}

				}
				
				
				//STEP 2:

				Object losingCodes1MoveForward[] = PlayerLossCodes[emptySpaces - 1].toArray();
				
				
				System.out.println("Num positions to go thru step 2: " + losingCodes1MoveForward.length);
				
				for(int j=0; j<losingCodes1MoveForward.length; j++) {
					
					//PROGRESS BAR:
					if(((j * 10) % losingCodes1MoveForward.length) < ((j-1) * 10 % losingCodes1MoveForward.length)) {

						System.out.println("j = " + j);
					}
					//END PROGRESS BAR
					
					SolitaryFastBoard losingBoard = new SolitaryFastBoard(N, (BigInteger)losingCodes1MoveForward[j]);
					
					
					for(int pegLossRM = 0; pegLossRM < NUM_CELLS; pegLossRM++) {
						
						//Make sure to RM correct tile type:
						if(losingBoard.getTable()[pegLossRM / N][pegLossRM % N] != losingColour) {
							continue;
						} 

						//REMOVE winning peg
						losingBoard.removePeg(pegLossRM);
						
						if(PlayerLossCodes[emptySpaces].contains(losingBoard.getUniqueCode())) {
							
							losingBoard.addPeg(pegLossRM, findP1Losses);
							continue;
						}
						
						boolean foundNonLosingMove = false;
						
						for(int pegReAddIndex=0; foundNonLosingMove == false && pegReAddIndex<NUM_CELLS; pegReAddIndex++) {
							
							//Are we place peg on empty space
							if(losingBoard.getTable()[pegReAddIndex / N][pegReAddIndex % N] != Constants.EMPTY) {
								continue;
								
							//Is the move legal?
							} else if(losingBoard.isAddingPegIllegalMoveSlow(pegReAddIndex, findP1Losses)) {
								continue;
							}
							
							losingBoard.addPeg(pegReAddIndex, findP1Losses);
							
							if(PlayerLossCodes[emptySpaces - 1].contains(losingBoard.getUniqueCode()) == false) {
								foundNonLosingMove = true;
							}
							

							losingBoard.removePeg(pegReAddIndex);
							
							
						}
						
						
						if(foundNonLosingMove == false) {
							PlayerLossCodes[emptySpaces].add(losingBoard.getUniqueCode());
						}
						
						//add winning peg
						losingBoard.addPeg(pegLossRM, findP1Losses);
						
					}
					
				}
				
			}
			//END TODO: Translate losing positions into losing positions 2 moves back
			
			System.out.println("Number of losing positions for " + emptySpaces + " empty spaces: " + PlayerLossCodes[emptySpaces].size());
			
			
		}
		
		if(searchPosWhereOddNumSpacesLeft == true) {
			System.out.println("Searched where there were an ODD number of empty cells left in the position");
		} else {
			System.out.println("Searched where there were an EVEN number of empty cells left in the position");
		}
		
		//debugPrintSolutionCodes();
		
	}
	
}
