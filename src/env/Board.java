package env;

//TODO: make a new board designed for even more speed and hashing...

// board that handles to avoid square game
//It's a little bit faster because it keeps tract of moves available for Player 1 and Player2 and
// keeps that in memory.

public class Board {

	public int[][] getTable() {
		return table;
	}

	public boolean isP1turn() {
		return P1turn;
	}

	public boolean[][] getP1Movable() {
		return P1Movable;
	}

	public boolean[][] getP2Movable() {
		return P2Movable;
	}

	public static void main(String args[]) {
		
		Board board = new Board();
		board.draw();
		
		for(int i=0; i<5; i++) {
			board = board.moveNullOnLoss(i, 0);
			board = board.moveNullOnLoss(i, 1);
			board.draw();
		}
		for(int i=0; i<5; i++) {
			board = board.moveNullOnLoss(i, 2);
			board = board.moveNullOnLoss(i, 3);
			board.draw();
		}
	}
	
	private int table[][];
	 
	private boolean P1turn;
	
	private boolean P1Movable[][];
	private boolean P2Movable[][];
	//TODO:
	//Keep track of number of choices to make everything go faster...
	//(Do this later...)
	
	public Board() {
		this.table = new int[Constants.SIZE][Constants.SIZE];
		this.P1Movable = new boolean[Constants.SIZE][Constants.SIZE];
		this.P2Movable  = new boolean[Constants.SIZE][Constants.SIZE];
		P1turn = true;
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				table[i][j] = Constants.EMPTY;
				P1Movable[i][j] = true;
				P2Movable[i][j] = true;
			}
		}
		
	}
	
	public Board(int table[][], boolean P1Movable[][], boolean P2Movable[][], boolean P1Turn) {
		this.table = new int[Constants.SIZE][Constants.SIZE];
		this.P1Movable = new boolean[Constants.SIZE][Constants.SIZE];
		this.P2Movable  = new boolean[Constants.SIZE][Constants.SIZE];
		this.P1turn = P1Turn;
		
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
		
		if(this.P1turn) {
			ret += "Player 1's turn\n\n";
		} else {
			ret += "Player 2's turn\n\n";
			
		}
		
		ret += "----------------------------";

		return ret;
	}

	public Board moveNullOnLoss(int code) {
		return moveNullOnLoss(code / Constants.SIZE, code % Constants.SIZE);
	}
	
	public Board moveNullOnLoss(int i, int j) {
		
		if( (P1turn && P1Movable[i][j]) || (!P1turn && P2Movable[i][j])) {
			return this.playMove(i, j);

		} else {
			return null;

		}
	}
	
	public Board playMove(int code) {
		return playMove(code / Constants.SIZE, code % Constants.SIZE);
	}
	
	public Board playMove(int i, int j) {
		Board newBoard = hardCopyForNextRound();
		
		//Take the space:
		newBoard.P1Movable[i][j] = false;
		newBoard.P2Movable[i][j] = false;

		if(newBoard.P1turn) {
			newBoard.table[i][j] = Constants.P1_COLOUR;
		} else {
			newBoard.table[i][j] = Constants.P2_COLOUR;
		}
		
		newBoard.updateMoveable(i, j);
		
		newBoard.P1turn = !newBoard.P1turn;
		
		return newBoard;
	}
	
	private void updateMoveable(int imove, int jmove) {
		
		if(this.P1turn) {
			for(int i=0; i<Constants.SIZE; i++) {
				for(int j=0; j<Constants.SIZE; j++) {

					if(table[i][j] != Constants.P2_COLOUR) {
						int dy = i-imove;
						int dx = j-jmove;
						
						
						if(imove - dx >= 0         && imove - dx < Constants.SIZE
								&& jmove + dy >= 0 && jmove + dy < Constants.SIZE
								&& i - dx >= 0     && i - dx < Constants.SIZE
								&& j + dy >= 0     && j + dy < Constants.SIZE) {
							
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
			for(int i=0; i<Constants.SIZE; i++) {
				for(int j=0; j<Constants.SIZE; j++) {

					if(table[i][j] != Constants.P1_COLOUR) {
						int dy = i-imove;
						int dx = j-jmove;
						
						
						if(imove - dx >= 0         && imove - dx < Constants.SIZE
								&& jmove + dy >= 0 && jmove + dy < Constants.SIZE
								&& i - dx >= 0     && i - dx < Constants.SIZE
								&& j + dy >= 0     && j + dy < Constants.SIZE) {
							
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
	
	public Board hardCopyForNextRound() {
		
		return new Board(this.table, this.P1Movable, this.P2Movable, this.P1turn);
		
	}
	
	//TODO: make getPlayableMoves that orders the list from most to least promissing
	public int[] getPlayableMovesNaive() {
		int ret[];
		
		int curNumPlayable = 0;
		
		for(int i=0; i<Constants.SIZE; i++) {
			for(int j=0; j<Constants.SIZE; j++) {
				if(P1turn && P1Movable[i][j]) {
					curNumPlayable++;
				} else if(P1turn == false && P2Movable[i][j]) {
					curNumPlayable++;
				}
			}
		}
		
		ret = new int[curNumPlayable];
		curNumPlayable = 0;
		
		for(int i=0; i<Constants.SIZE; i++) {
			for(int j=0; j<Constants.SIZE; j++) {
				if(P1turn && P1Movable[i][j]) {
					ret[curNumPlayable] = i * Constants.SIZE + j;
					curNumPlayable++;
					
				} else if(P1turn == false && P2Movable[i][j]) {
					ret[curNumPlayable] = i * Constants.SIZE + j;
					curNumPlayable++;
					
				}
			}
		}
		
		return ret;
	}
	
	
	public boolean currentPlayerCantMove() {
		if(this.P1turn) {
			for(int i=0; i<Constants.SIZE; i++) {
				for(int j=0; j<Constants.SIZE; j++) {
					if(P1Movable[i][j]) {
						return false;
					}
					
				}
			}
			
			return true;
		} else {
			for(int i=0; i<Constants.SIZE; i++) {
				for(int j=0; j<Constants.SIZE; j++) {
					if(P2Movable[i][j]) {
						return false;
					}
					
				}
			}
			
			return true;
		}
	}
	//TODO: make less naive
	public double naiveShallowEval() {
		if(this.P1turn) {
			for(int i=0; i<Constants.SIZE; i++) {
				for(int j=0; j<Constants.SIZE; j++) {
					if(P1Movable[i][j]) {
						return 0.0;
					}
					
				}
			}
			
			return -Double.MAX_VALUE;
		} else {
			for(int i=0; i<Constants.SIZE; i++) {
				for(int j=0; j<Constants.SIZE; j++) {
					if(P2Movable[i][j]) {
						return 0.0;
					}
					
				}
			}
			
			return Double.MAX_VALUE;
		}
	}

	//TODO I should make a hash with collisions
	public static int hash() {
		return -1;
	}
	
}
