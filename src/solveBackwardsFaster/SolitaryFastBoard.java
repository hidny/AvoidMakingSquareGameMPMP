package solveBackwardsFaster;

import java.math.BigInteger;

import env.Constants;

public class SolitaryFastBoard {

	private int table[][];
	
	public int[][] getTable() {
		return table;
	}

	public SolitaryFastBoard(int size, BigInteger code) {
			
			this.table = new int[size][size];
			
			BigInteger BASE = new BigInteger("" + Constants.NUM_STATES_PER_ELEMENT);
			BigInteger TWO = new BigInteger("2");
			
			BigInteger currentCode = code;
			
			for(int i=0; i<this.table.length; i++) {
				for(int j=0; j<this.table[i].length; j++) {
					
					int iBackwards = this.table.length - 1 - i;
					int jBackwards = this.table[i].length - 1 - j;
					
					
					BigInteger array[] = currentCode.divideAndRemainder(BASE);
					
					currentCode = array[0];
					BigInteger remainder = array[1];
					
					if(remainder.compareTo(BigInteger.ZERO) == 0) {
						this.table[iBackwards][jBackwards] = 0;
						
					} else if(remainder.compareTo(new BigInteger("" + Constants.P1_COLOUR)) == 0) {
						this.table[iBackwards][jBackwards] = 1;
						
					} else if(remainder.compareTo(new BigInteger("" + Constants.P2_COLOUR)) == 0) {
						this.table[iBackwards][jBackwards] = 2;
						
					} else {
						System.out.println("ERROR: unexpected answer in SolitaryBoard(int size, BigInteger code)");
						System.exit(1);
					}
					
					
				}
			}
			
	}
	
	public void removePeg(int index) { 
		this.table[index / SolveAllTheWay.N][index % SolveAllTheWay.N] = 0;
	}
	
	public void addPeg(int index, boolean isP1) {
		int pieceCode = 0;
		if(isP1) {
			pieceCode = Constants.P1_COLOUR;
		} else {
			pieceCode = Constants.P2_COLOUR;
		}
		
		this.table[index / SolveAllTheWay.N][index % SolveAllTheWay.N] = pieceCode;
	}
	
	//This could be a little bit faster if the isP1turn condition is outside the for loops,
	//but It's fast enough...
	public boolean isAddingPegLegalMoveSlow(int index, boolean isP1turn) {
		
		int imove = index / SolveAllTheWay.N;
		int jmove = index % SolveAllTheWay.N;; 
		
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table.length; j++) {

					int dy = i-imove;
					int dx = j-jmove;
					
					//Check if square within grid:
					if(imove - dx >= 0         && imove - dx < table.length
							&& jmove + dy >= 0 && jmove + dy < table.length
							&& i - dx >= 0     && i - dx < table.length
							&& j + dy >= 0     && j + dy < table.length) {
						
						if(isP1turn) {
							if(table[i][j] == Constants.P1_COLOUR
									&& table[imove - dx][jmove + dy] == Constants.P1_COLOUR
									&& table[i - dx][j + dy] == Constants.P1_COLOUR) {
								return false;
							}
						} else {
							if(table[i][j] == Constants.P2_COLOUR
									&& table[imove - dx][jmove + dy] == Constants.P2_COLOUR
									&& table[i - dx][j + dy] == Constants.P2_COLOUR) {
								return false;
							}
						}
						
					}// END check if square within grid
					
			}// END inner for loop
		
		}//END outer for loop
		
		return true;
		
	}
	
	
	
	//Copied from Solitary Board
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
	//END copy
}
