package Algorithm;

import Game.Board;

public class Minimax {
	//static int maxLevel = 1;
	public static int maxDepth = 3;
	static int nodes=0;
	private static int minimax(Board board, int depth, boolean isMax, String playerType, String currentPlayer) {
		//Setting player
		String opponent = (currentPlayer=="AI") ? "Human" : "AI";
		nodes++;
		//Calculating score of board
		int score = board.getScore(currentPlayer,depth,maxDepth);
		//Terminal States:
		if (board.isBoardFull()) {
			score = score * (board.countFours(currentPlayer)- board.countFours(opponent)) * (maxDepth);
			return score;
		}

		if (depth == maxDepth) {
			if (board.countFours(playerType) > board.countFours(opponent)){
				return score = score * board.countFours(playerType) * (maxDepth);
			}
			else if (board.countFours(playerType) < board.countFours(opponent)) {
				return score = score * board.countFours(opponent) * (maxDepth);
			} else {
				return score;
			}
		}

		// if max will play (AI)
		if (isMax) {
			int best = Integer.MIN_VALUE;

			// iterate on all columns
			for(int i = 0; i < Board.COLUMNS; i++) {
				if(board.columnsCounter[i] > 5) { // columns is full
					continue;
				}
				int rowAdded = board.addPiece(i, playerType);
				String repeated = new String(new char[Math.abs(depth-maxDepth)*2]).replace("\0", "-");
				System.out.println(repeated+">"+i+") In depth = "+depth);
				board.printBoard(new String(new char[Math.abs(depth-maxDepth)*2]).replace("\0", " ")+" ");
				
				// Call minimax recursively and return the maximum to best
				best = Math.max(best, minimax(board, depth + 1, !isMax,playerType,currentPlayer));

				// Undo the move
				board.undoMove(rowAdded, i);
			}
			return best;
		}


		// If this minimizer's move
		else {
			int best = Integer.MAX_VALUE;
			
			// iterate on all columns
			for(int i = 0; i < Board.COLUMNS; i++) {
				if(board.columnsCounter[i] > 5) { // columns is full
					continue;
				}
				int rowAdded = board.addPiece(i, opponent);
				String repeated = new String(new char[Math.abs(depth-maxDepth)*2]).replace("\0", "-");
				System.out.println(repeated+">"+i+") In depth = "+depth);
				board.printBoard(new String(new char[Math.abs(depth-maxDepth)*2]).replace("\0", " ")+" ");
				// Call minimax recursively and return the minimum to best
				best = Math.min(best, minimax(board, depth +1, !isMax,playerType,currentPlayer));

				// Undo the move
				board.undoMove(rowAdded, i);
			}
			return best;
		}
	}

	// This will return the best possible next move for the current board
	public static int playBestMove(Board board, String playerType) {
		int bestVal = Integer.MIN_VALUE;
		double start=System.nanoTime();
		int colToPlay = 0;
		// Apply minimax algorithm to get the best move
		for(int i = 0; i < Board.COLUMNS; i++) {
			if(board.columnsCounter[i] > 5) { // columns is full
				continue;
			}
			int rowAdded = board.addPiece(i, playerType);

			// apply the minimax algorithm to that possible move
			int nextStateVal = minimax(board, 0, false, playerType,playerType); //false because min will play

			board.undoMove(rowAdded, i);
			
			// If the value of the next state is more than the best value, then update best.
			if (nextStateVal > bestVal) {
				colToPlay=i;
			}

		}
		System.out.println("nodes expanded in Minimax= "+nodes);
		double end=System.nanoTime();
		double time=(end-start)/ 1000000.0;
		System.out.println("Minimax without Alpha Beta took ("+time+") milliseconds");
		return colToPlay;
	}
}
