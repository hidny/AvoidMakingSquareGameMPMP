package aiAlgoevaluator;

import env.Board;
import env.Constants;

public class BasicEvalWithFraction extends BasicEval implements EvaluatorI {

	public double evalPosition(Board node) {
		
		double ret = 0.0;
		
		int table[][] = node.getTable();
		boolean p1Movable[][] = node.getP1Movable();
		boolean p2Movable[][] = node.getP2Movable();
		
		int numEmptyBothCant = 0;
		int numEmptyBothCan = 0;
		
		
		for(int i=0; i<p1Movable.length; i++) {
			for(int j=0; j<p1Movable[i].length; j++) {
				if(p1Movable[i][j] && p2Movable[i][j] == false) {
					ret += 1.0;

				} else if(p1Movable[i][j] == false && p2Movable[i][j]) {
					ret -= 1.0;
				
				} else if(p1Movable[i][j] == false && p2Movable[i][j] == false && table[i][j] == Constants.EMPTY) {
					//Just make up a number...
					ret -= 0.1;
				}
				/*} else if(p1Movable[i][j] == false && p2Movable[i][j] == false && table[i][j] == Constants.EMPTY) {
					numEmptyBothCant++;
					
				} else if(p1Movable[i][j] && p2Movable[i][j] && table[i][j] == Constants.EMPTY) {
					numEmptyBothCan++;
					
				}*/
			}
		}
		
		
		//System.out.println(ret);
	
		return ret;
	}

}
