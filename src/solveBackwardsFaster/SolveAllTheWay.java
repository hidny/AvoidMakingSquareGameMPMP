package solveBackwardsFaster;

import java.math.BigInteger;
import java.util.HashSet;

import env.Constants;
import solveMPMP.SolitaryBoard;

//TODO N=3 contradicts results of other program... :(
public class SolveAllTheWay {

	public static int N = 4;
	public static int NUM_CELLS = N * N;
	
	

	public static HashSet<BigInteger> P1LossCodes[] = new HashSet[NUM_CELLS + 1];
	public static HashSet<BigInteger> P2LossCodes[] = new HashSet[NUM_CELLS + 1];
	
	
	public static int numLossesFound = 0;
	
	public static void main(String args[]) {
		init();
		
		//getLosses(false);
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
	
	//TODO: get Draws!

	public static void getLosses(boolean findP1Losses) {
		
		HashSet<BigInteger> PlayerLossCodes[];

		if(findP1Losses) {
			PlayerLossCodes = P1LossCodes;
		} else {
			PlayerLossCodes = P2LossCodes;
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
				
				System.out.println("Num positions to go thru: " + losingCodes2MovesForward.length);
				
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
					
					//TODO: materialize board 2 moves back:
					
					for(int p1RM = 0; p1RM < NUM_CELLS; p1RM++) {
						
						if(losingBoard.getTable()[p1RM / N][p1RM % N] != Constants.P1_COLOUR) {
							continue;
						}
						
						for(int p2RM = 0; p2RM < NUM_CELLS; p2RM++) {
							if(losingBoard.getTable()[p2RM / N][p2RM % N] != Constants.P2_COLOUR) {
								continue;
								
								//Is losing position already taken?
							} else if(PlayerLossCodes[emptySpaces].contains(losingBoard.getUniqueCode())) {
								//System.out.println("CONTINUE");
								//System.exit(1);
								continue;
							}
							//End is losing position already taken

							//REMOVE P1 and P2
							losingBoard.removePeg(p1RM);
							losingBoard.removePeg(p2RM);
							

							//TODO: function:
							//Figure out if it's a losing position

							boolean losingSoFar = true;
							
							for(int losingPlayerPlayInd=0; losingPlayerPlayInd < NUM_CELLS && losingSoFar == true; losingPlayerPlayInd++) {
								
								if(losingBoard.getTable()[losingPlayerPlayInd / N][losingPlayerPlayInd % N] != Constants.EMPTY) {
									continue;
								}

								//Check if position is illegal!
								//TODO: function is bugged?
								if(losingBoard.isAddingPegIllegalMoveSlow(losingPlayerPlayInd, findP1Losses)) {
									continue;
								}
								
								losingBoard.addPeg(losingPlayerPlayInd, findP1Losses);
								
								boolean couldBeWinningWith1stMove = true;
								
								for(int winningPlayerPlayInd=0; winningPlayerPlayInd < NUM_CELLS && losingSoFar == true && couldBeWinningWith1stMove == true; winningPlayerPlayInd++) {
									if(losingBoard.getTable()[winningPlayerPlayInd / N][winningPlayerPlayInd % N] != Constants.EMPTY) {
										continue;
									}

									//Check if position is losing!
									if(losingBoard.isAddingPegIllegalMoveSlow(winningPlayerPlayInd, !findP1Losses)) {
										continue;
									}
									
									losingBoard.addPeg(winningPlayerPlayInd, findP1Losses == false);
									
									//AH: didn't have .getUniqueCode() before!
									if(PlayerLossCodes[emptySpaces - 2].contains(losingBoard.getUniqueCode())) {
										//System.out.println("Found something!");
										//System.exit(1);
										
										
										
										couldBeWinningWith1stMove = false;
									}
									
									//WARNING: no breaking!

									losingBoard.removePeg(winningPlayerPlayInd);
								}
								
								if(couldBeWinningWith1stMove == true) {
									losingSoFar = false;
								}
								
								losingBoard.removePeg(losingPlayerPlayInd);
							}

							
							//Add code:
							if(losingSoFar == true) {
								//Check code
								PlayerLossCodes[emptySpaces].add(losingBoard.getUniqueCode());
								//System.out.println("BOOM!");
								//System.exit(1);
								
								
								/*
								//SANITY TEST
								SolitaryBoard losingBoardTEST2 = SolitaryBoard.createBoardFromCode(N, losingBoard.getUniqueCode());
								if(NUM_CELLS - losingBoardTEST2.getNumPieces() != emptySpaces) {
									System.out.println("ERROR: incorrect number of empty Spaces!");
									losingBoardTEST2.draw();
									System.exit(1);
								}
								
								//END SANITY TEST
								*/
								
								/*
								//DEBUG:
								SolitaryBoard losingBoardDEBUG = SolitaryBoard.createBoardFromCode(N, losingBoard.getUniqueCode());
								System.out.println("Losing position " + j + ":");
								losingBoardDEBUG.draw();
								System.out.println();
								//END DEBUG
								 */
								
							} else {
								//System.out.println("Correct");
							}
							//END TEST
							
							
							//READ P1 and P2
							losingBoard.addPeg(p1RM, true);
							losingBoard.addPeg(p2RM, false);
							
						}
					}
					//END TODO Figure out if it's a losing position
					

				}
				
				
			}
			//END TODO: Translate losing positions into losing positions 2 moves back
			
			System.out.println("Number of losing positions for " + emptySpaces + " empty spaces: " + PlayerLossCodes[emptySpaces].size());
			/*//TODO: move elsewhere
			if(emptySpaces == 7) {
				System.out.println("Debug:");
				
				Object tmpBoards[] = PlayerLossCodes[emptySpaces].toArray();
				
				System.out.println("Num positions to go thru: " + tmpBoards.length);
				
				for(int j=0; j<tmpBoards.length; j++) {
					
					
					SolitaryFastBoard losingBoard = new SolitaryFastBoard(N, (BigInteger)tmpBoards[j]);
					//jSystem.out.println("Sanity:");
					//SANITY TEST:
					SolitaryBoard losingBoardTEST = SolitaryBoard.createBoardFromCode(N, (BigInteger)tmpBoards[j]);
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
					
					//TEST
						System.out.println("Printing losing board:");
						losingBoardTEST.draw();
					
				}
			}*/
			
		}
		
		if(searchPosWhereOddNumSpacesLeft == true) {
			System.out.println("Searched where there were an ODD number of empty cells left in the position");
		} else {
			System.out.println("Searched where there were an EVEN number of empty cells left in the position");
		}
		
		//debugPrintSolutionCodes();
		
	}
	
}
