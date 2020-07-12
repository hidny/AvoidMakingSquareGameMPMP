package aiAlgoevaluator;

import aiAlgo.AlphaBetaPruneWithMemoryAndEval;
import env.Board;
import env.Constants;

public class halfwayEvalSUCKS extends BasicEval  {

	public AlphaBetaPruneWithMemoryAndEval halfWaySearch = new AlphaBetaPruneWithMemoryAndEval(new BasicEval());

	
	//This made it slower :(
	
	
	@Override
	public double evalPosition(Board node) {
		
		//TODO: do I want to bring in alpha and beta?
		int depth = (Constants.NUM_CELLS - node.getNumPieces()) / 2;
		
		if(node.getNumPieces() > 5 && depth > 5) {
			
			if(node.getNumPieces() <= 10) {
				System.out.println("Start half-way search with " + node.getNumPieces() + " pieces on the board.");
			}

			double ret =  halfWaySearch.alphaBetaPrune(node, depth);
			
			halfWaySearch.clearMemoryRecords();
			
			if(node.getNumPieces() <= 10) {
				System.out.println("End half-way search: " + ret);
			}

			return ret;
			
		} else {
			return super.evalPosition(node);
		}
	}

}
