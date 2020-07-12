package aiAlgoevaluator;

import env.Board;

public class BasicEval implements EvaluatorI {

	//TODO: maybe this is a util function?
	@Override
	public Board[] getPlayableMovesSortedBasedOnEval(Board node) {
		
		//TODO: extend board class to make this faster
		//NAH: .... it won't even make it twice as fast.
		
		int moveCode[]  = node.getPlayableMovesNaive();
		
		double evalList[] = new double[moveCode.length];
		
		Board moveList[] = new Board[moveCode.length];
		
		for(int k=0; k<moveCode.length; k++) {

			moveList[k] = node.playMove(moveCode[k]);
			
			evalList[k] = evalPosition(moveList[k]);
		}
		
		//TODO: double check that this works?

		//Sort from most promising to least promising:
		for(int i=0; i<moveCode.length; i++) {
			int bestIndex = i;

			for(int j=i+1; j<moveCode.length; j++) {
				
				if((node.isP1turn() && evalList[j] > evalList[bestIndex])
						|| (node.isP1turn() == false && evalList[j] < evalList[bestIndex])){
					bestIndex = j;
				}
			}
			
			//swap
			Board tmpBoard = moveList[i];
			moveList[i] = moveList[bestIndex];
			moveList[bestIndex] = tmpBoard;
			
			double tmpEval = evalList[i];
			evalList[i] = evalList[bestIndex];
			evalList[bestIndex] = tmpEval;
			
		}
		
		
		return moveList;
	}
	
	public double evalPosition(Board node) {
		
		double ret = 0.0;
		
		boolean p1Movable[][] = node.getP1Movable();
		boolean p2Movable[][] = node.getP2Movable();
		
		for(int i=0; i<p1Movable.length; i++) {
			for(int j=0; j<p1Movable[i].length; j++) {
				if(p1Movable[i][j] && p2Movable[i][j] == false) {
					ret += 1.0;

				} else if(p1Movable[i][j] == false && p2Movable[i][j]) {
					ret -= 1.0;
				}
			}
		}
		
		return ret;
	}

}
