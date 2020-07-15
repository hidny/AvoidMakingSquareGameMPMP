package solveUtil;
import java.math.BigInteger;
import java.util.Comparator;

//TODO: learn to make sieves!
public class UtilityFunctions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public static int getSum(int array[]) {
		int sum=0;
		for(int i=0; i<array.length; i++) {
			sum += array[i];
		}
		return sum;
	}

	public static long[][] createPascalTriangle(int size) {
		size = size+1;
		long pascalTriangle[][] = new long[size][size];
		
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				pascalTriangle[i][j] = 0;
			}
		}
		
		pascalTriangle[0][0] = 1;
				
		for(int i=1; i<size; i++) {
			for(int j=0; j<size; j++) {
				pascalTriangle[i][j] = pascalTriangle[i-1][j];
				if(j>0) {
					pascalTriangle[i][j] += pascalTriangle[i-1][j-1];
				}
			}
		}
		
		return pascalTriangle;
	}
	
	public static long[][] createPascalTriangle(int size, long modulo) {
		size = size+1;
		long pascalTriangle[][] = new long[size][size];
		
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				pascalTriangle[i][j] = 0;
			}
		}
		
		pascalTriangle[0][0] = 1;
				
		for(int i=1; i<size; i++) {
			for(int j=0; j<size; j++) {
				pascalTriangle[i][j] = pascalTriangle[i-1][j];
				if(j>0) {
					pascalTriangle[i][j] = (pascalTriangle[i][j] + pascalTriangle[i-1][j-1]) % modulo;
				}
			}
		}
		
		return pascalTriangle;
	}

	public static void printTriangle(long pascalTriangle[][]) {
		String space = "          ";
		
		for(int i=0; i<pascalTriangle.length; i++) {
			for(int j=0; j<pascalTriangle[0].length; j++) {
				System.out.print(pascalTriangle[i][j] + space.substring( ("" + pascalTriangle[i][j]).length()));
				
			}
			System.out.println();
		}
	}

	
	//get the next combonation of true values given current array
	public static boolean[] getNextCombination(boolean current[]) {
		/*Example: series of executions:
		 *  11000
			10100
			10010
			10001
			01100
			01010
			01001
			00110
			00101
			00011
		 */
		//we know that we are going to readjust at least 1 element:
		int numToReadjust = 1;
		
		boolean foundSpaceToFill = false;
		
		int spaceToFill;
		
		//this loops counts the number of elements we have to readjust
		//and finds out if there exist a space to fill.
		for(spaceToFill=current.length - 1; spaceToFill>=0; spaceToFill--) {
			if(current[spaceToFill] == false) {
				foundSpaceToFill = true;
				break;
			} else {
				numToReadjust++;
			}
		}
		
		if(foundSpaceToFill) {
			//Find the rightmost 1 that we will have to move to the right:
			int indexToMove;
			for(indexToMove = spaceToFill-1; indexToMove>=0; indexToMove--) {
				if(current[indexToMove] == true) {
					break;
				}
			}
			
			if(indexToMove>=0) {
				current[indexToMove] = false;
				//goal:
				//got from: 00010111
				//to        00001111
				int startInput1s = indexToMove+1;
				int stopInput1s = startInput1s + numToReadjust;
				for(int i=startInput1s; i<stopInput1s; i++) {
					current[i] = true;
				}
				//input 0s for the rest:
				for(int i=stopInput1s; i<current.length; i++) {
					current[i] = false;
				}
				
			} else {
				//This should only happen is we've gone through all of the combinations
				//or there is no element in current that is true.
				return null;
			}
			
		} else {
			//This should only happen if current[] is filled with only true.
			return null;
		}
		
		return current;
	}
	
		
}
