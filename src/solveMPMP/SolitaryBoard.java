package solveMPMP;

import java.math.BigInteger;

import env.Constants;

//TODO: make a new board designed for even more speed and hashing...

// board that handles to avoid square game
//It's a little bit faster because it keeps tract of moves available for Player 1 and Player2 and
// keeps that in memory.

public class SolitaryBoard {

	public int[][] getTable() {
		return table;
	}

	public int getNumPieces() {
		return numPieces;
	}

	public int getNumPiecesForPlayer1() {
		return numPiecesForPlayer1;
	}
	public int getNumPiecesForPlayer2() {
		return numPieces - numPiecesForPlayer1;
	}

	public boolean[][] getP1Movable() {
		return P1Movable;
	}

	public boolean[][] getP2Movable() {
		return P2Movable;
	}

	public static void main(String args[]) {
		
		SolitaryBoard board = new SolitaryBoard(Constants.SIZE);
		board.draw();
		
		for(int i=0; i<5; i++) {
			board = board.moveNullOnLoss(i, 0, true);
			board = board.moveNullOnLoss(i, 1, false);
			board.draw();
		}
		for(int i=0; i<5; i++) {
			board = board.moveNullOnLoss(i, 2, true);
			board = board.moveNullOnLoss(i, 3, false);
			board.draw();
		}
	}
	
	private int table[][];
	private int numPieces;
	private int numPiecesForPlayer1;

	
	private boolean P1Movable[][];
	private boolean P2Movable[][];
	//TODO:
	//Keep track of number of choices to make everything go faster...
	//(Do this later...)
	
	public SolitaryBoard(int size) {
		this.table = new int[size][size];
		this.P1Movable = new boolean[size][size];
		this.P2Movable  = new boolean[size][size];
		this.numPieces = 0;
		this.numPiecesForPlayer1 = 0;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				table[i][j] = Constants.EMPTY;
				P1Movable[i][j] = true;
				P2Movable[i][j] = true;
			}
		}
		
	}
	
	public SolitaryBoard(int table[][], boolean P1Movable[][], boolean P2Movable[][], int numPieces, int numPiecesForPlayer1) {
		this.table = new int[table.length][table.length];
		this.P1Movable = new boolean[table.length][table.length];
		this.P2Movable  = new boolean[table.length][table.length];
		this.numPieces = numPieces;
		this.numPiecesForPlayer1 = numPiecesForPlayer1;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				this.table[i][j] = table[i][j];
				this.P1Movable[i][j] = P1Movable[i][j];
				this.P2Movable[i][j] = P2Movable[i][j];
			}
		}
		
	}
	
	public void draw() {
		System.out.println(this);
	}
	
	//TODO: might have to take this away to make hash function work because JAVA
	public String toString() {
		String ret = "----------------------------";
		
		ret += "\n";
		
		for(int i=0; i<table.length; i++) {
			ret+="|";
			for(int j=0; j<table[i].length; j++) {
				if(table[i][j] == Constants.EMPTY) {
					ret += "_";
				} else if(table[i][j] == Constants.P1_COLOUR) {
					ret += "G";
					
				} else {
					ret += "T";
				}
			}
			ret += "|\n";
		}
		ret += "\n";
		
		ret += " DEBUG: P1 movable table:\n";
		ret += "\n";
		
		for(int i=0; i<table.length; i++) {
			ret+="|";
			for(int j=0; j<table[i].length; j++) {
				if(table[i][j] == Constants.EMPTY && P1Movable[i][j]) {
					ret += "1";
				} else if(table[i][j] == Constants.P1_COLOUR) {
					ret += "G";
					
				} else if(table[i][j] == Constants.P2_COLOUR) {
					ret += "T";
				} else {
					ret += "_";
				}
			}
			ret += "|\n";
		}
		ret += "\n";
		
		ret += " DEBUG: P2 movable table:\n";
		ret += "\n";
		
		for(int i=0; i<table.length; i++) {
			ret+="|";
			for(int j=0; j<table[i].length; j++) {
				if(table[i][j] == Constants.EMPTY && P2Movable[i][j]) {
					ret += "2";
				} else if(table[i][j] == Constants.P1_COLOUR) {
					ret += "G";
					
				} else if(table[i][j] == Constants.P2_COLOUR) {
					ret += "T";
				} else {
					ret += "_";
				}
			}
			ret += "|\n";
		}
		ret += "\n";
		
		
		ret += "----------------------------";

		return ret;
	}

	public SolitaryBoard moveNullOnLoss(int code, boolean isP1turn) {
		return moveNullOnLoss(code / table.length, code % table.length, isP1turn);
	}
	
	public SolitaryBoard moveNullOnLoss(int i, int j, boolean isP1turn) {
		
		if( (isP1turn && P1Movable[i][j]) || (!isP1turn && P2Movable[i][j])) {
			return this.playMove(i, j, isP1turn);

		} else {
			return null;

		}
	}
	
	public SolitaryBoard playMove(int code, boolean isP1turn) {
		return playMove(code / table.length, code % table.length, isP1turn);
	}
	
	public SolitaryBoard playMove(int i, int j, boolean isP1turn) {
		SolitaryBoard newBoard = hardCopy();
		
		//Take the space:
		newBoard.P1Movable[i][j] = false;
		newBoard.P2Movable[i][j] = false;

		newBoard.numPieces++;
		if(isP1turn) {
			newBoard.table[i][j] = Constants.P1_COLOUR;
			newBoard.numPiecesForPlayer1++;
		} else {
			newBoard.table[i][j] = Constants.P2_COLOUR;
		}
		
		newBoard.updateMoveable(i, j, isP1turn);
		
		return newBoard;
	}
	
	private void updateMoveable(int imove, int jmove, boolean isP1turn) {
		
		if(isP1turn) {
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table.length; j++) {

					if(table[i][j] != Constants.P2_COLOUR) {
						int dy = i-imove;
						int dx = j-jmove;
						
						
						if(imove - dx >= 0         && imove - dx < table.length
								&& jmove + dy >= 0 && jmove + dy < table.length
								&& i - dx >= 0     && i - dx < table.length
								&& j + dy >= 0     && j + dy < table.length) {
							
							if(table[imove - dx][jmove + dy] == Constants.P2_COLOUR
									|| table[i - dx][j + dy] == Constants.P2_COLOUR) {
								continue;
							}
							
							int numUsed = 0;
							if(table[i][j] == Constants.P1_COLOUR) {
								numUsed++;
							}
							if(table[imove - dx][jmove + dy] == Constants.P1_COLOUR) {
								numUsed++;
							}
							if(table[i - dx][j + dy] == Constants.P1_COLOUR) {
								numUsed++;
							}
							
							if(numUsed == Constants.NUM_SIDES_SQUARE - 2) {
								if(table[i][j] == Constants.EMPTY) {
									this.P1Movable[i][j] = false;

								} else if(table[imove - dx][jmove + dy] == Constants.EMPTY) {
									this.P1Movable[imove - dx][jmove + dy] = false;

								} else if(table[i - dx][j + dy] == Constants.EMPTY) {
									this.P1Movable[i - dx][j + dy] = false;

								}
							}
							
						}// END IF could make square
						
					} //END IF point to make square not opposing colour.
				}// END inner for loop
			
			}//END outer for loop
		
		} else {
			//Reflect for P2...
			//Copy paste code that might be faster.
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table.length; j++) {

					if(table[i][j] != Constants.P1_COLOUR) {
						int dy = i-imove;
						int dx = j-jmove;
						
						
						if(imove - dx >= 0         && imove - dx < table.length
								&& jmove + dy >= 0 && jmove + dy < table.length
								&& i - dx >= 0     && i - dx < table.length
								&& j + dy >= 0     && j + dy < table.length) {
							
							if(table[imove - dx][jmove + dy] == Constants.P1_COLOUR
									|| table[i - dx][j + dy] == Constants.P1_COLOUR) {
								continue;
							}
							
							int numUsed = 0;
							if(table[i][j] == Constants.P2_COLOUR) {
								numUsed++;
							}
							if(table[imove - dx][jmove + dy] == Constants.P2_COLOUR) {
								numUsed++;
							}
							if(table[i - dx][j + dy] == Constants.P2_COLOUR) {
								numUsed++;
							}
							
							if(numUsed == Constants.NUM_SIDES_SQUARE - 2) {
								if(table[i][j] == Constants.EMPTY) {
									this.P2Movable[i][j] = false;

								} else if(table[imove - dx][jmove + dy] == Constants.EMPTY) {
									this.P2Movable[imove - dx][jmove + dy] = false;

								} else if(table[i - dx][j + dy] == Constants.EMPTY) {
									this.P2Movable[i - dx][j + dy] = false;

								}
							}
							
						}// END IF could make square
						
					} //END IF point to make square not opposing colour.
				}// END inner for loop
			
			}//END outer for loop

		}
		
	}
	
	public SolitaryBoard hardCopy() {
		
		return new SolitaryBoard(this.table, this.P1Movable, this.P2Movable, this.numPieces, this.numPiecesForPlayer1);
		
	}
	
	//TODO: make getPlayableMoves that orders the list from most to least promissing
	public int[] getPlayableMovesNaive(boolean isP1turn) {
		int ret[];
		
		int curNumPlayable = 0;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table.length; j++) {
				if(isP1turn && P1Movable[i][j]) {
					curNumPlayable++;
				} else if(isP1turn == false && P2Movable[i][j]) {
					curNumPlayable++;
				}
			}
		}
		
		ret = new int[curNumPlayable];
		curNumPlayable = 0;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table.length; j++) {
				if(isP1turn && P1Movable[i][j]) {
					ret[curNumPlayable] = i * table.length + j;
					curNumPlayable++;
					
				} else if(isP1turn == false && P2Movable[i][j]) {
					ret[curNumPlayable] = i * table.length + j;
					curNumPlayable++;
					
				}
			}
		}
		
		return ret;
	}
	
	
	public boolean currentPlayerCantMove(boolean isP1turn) {
		if(isP1turn) {
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table.length; j++) {
					if(P1Movable[i][j]) {
						return false;
					}
					
				}
			}
			
			return true;
		} else {
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table.length; j++) {
					if(P2Movable[i][j]) {
						return false;
					}
					
				}
			}
			
			return true;
		}
	}

	//TODO: make less naive
	public double naiveShallowEval(boolean isP1turn) {
		if(isP1turn) {
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table.length; j++) {
					if(P1Movable[i][j]) {
						return 0.0;
					}
					
				}
			}
			
			return -Double.MAX_VALUE;
		} else {
			for(int i=0; i<table.length; i++) {
				for(int j=0; j<table.length; j++) {
					if(P2Movable[i][j]) {
						return 0.0;
					}
					
				}
			}
			
			return Double.MAX_VALUE;
		}
	}

	
	//TODO: might be able to make it keep track of uniqueCode after every mode
	// I might do it if I extend the Board class.
	
	public BigInteger getUniqueCode() {
		
		BigInteger ret = BigInteger.ZERO;
		
		BigInteger BASE = new BigInteger("" + Constants.NUM_STATES_PER_ELEMENT);

		BigInteger TWO = new BigInteger("2");
		
		for(int i=0; i<this.table.length; i++) {
			for(int j=0; j<this.table[i].length; j++) {
				
				if(table[i][j] == Constants.EMPTY) {
					ret = ret.multiply(BASE);
					
				} else if(table[i][j] == Constants.P1_COLOUR) {
					ret = ret.multiply(BASE).add(BigInteger.ONE);
					
				} else {
					ret = ret.multiply(BASE).add(TWO);
					
				}
				
			}
		}
		
		return ret;
	}
	
}
