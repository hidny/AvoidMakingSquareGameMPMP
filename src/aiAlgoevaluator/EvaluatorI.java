package aiAlgoevaluator;

import env.Board;

public interface EvaluatorI {

	Board[] getPlayableMovesSortedBasedOnEval(Board node);
	
	public double evalPosition(Board node);
}
