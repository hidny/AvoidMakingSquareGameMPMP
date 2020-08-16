package lossLookupDB;

import java.math.BigInteger;
import java.util.HashSet;

import solveBackwardsFaster.SolveAllTheWay2;

public class LossLookupDBSimple {

	public static HashSet<BigInteger> P1LossCodes[];
	public static HashSet<BigInteger> P2LossCodes[];
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		setup();
		
	}
	
	public static void setup() {

		SolveAllTheWay2.init();
		
		System.out.println("GET P1 losses:");
		SolveAllTheWay2.getLosses(true);
		P1LossCodes = SolveAllTheWay2.P1LossCodes;
		
		
		System.out.println("Get P2 losses:");
		SolveAllTheWay2.getLosses(false);
		P2LossCodes = SolveAllTheWay2.P2LossCodes;
		
		
	}
	
	public static boolean isP1LosingPos(BigInteger code) {
		for(int i=0; i<P1LossCodes.length; i++) {
			if(isP1LosingPos(code, i)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isP2LosingPos(BigInteger code) {
		for(int i=0; i<P2LossCodes.length; i++) {
			if(isP2LosingPos(code, i)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isP1LosingPos(BigInteger code, int numEmptySpaces) {
		if(P1LossCodes[numEmptySpaces].contains(code)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isP2LosingPos(BigInteger code, int numEmptySpaces) {
		if(P2LossCodes[numEmptySpaces].contains(code)) {
			return true;
		} else {
			return false;
		}
	}

}
