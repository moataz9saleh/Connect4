package Algorithm;

import Game.Board;

public class AlphaBeta {
	public static int maxLevel = 3;
	static int MAX = Integer.MAX_VALUE;
	static int MIN = Integer.MIN_VALUE;
	static int pruned  = 0;
	private static int minimax(Board board, int levels, boolean isMax, String playerType, int alpha, int beta, String currentPlayer) {
		String opponent = (currentPlayer=="AI") ? "Human" : "AI";
		//Calculating score of board
		int score = board.getScore(currentPlayer,levels,maxLevel);
		//Terminal States:
		if (board.isBoardFull()) {
			score = score * (board.countFours(currentPlayer)- board.countFours(opponent)) * (maxLevel);
			return score;
		}

		if (levels == maxLevel) {
			if (board.countFours(playerType) > board.countFours(opponent)) {
				return score = score * board.countFours(playerType) * (maxLevel);
			} else if (board.countFours(playerType) < board.countFours(opponent)) {
				return score = score * board.countFours(opponent) * (maxLevel);
			} else {
				return score;
			}
		}

		// if max will play (AI)
		if (isMax) {
			int best = MIN;

			// iterate on all columns
			for(int i = 0; i < Board.COLUMNS; i++) {
				if(board.columnsCounter[i] > 5) { // columns is full
					continue;
				}
				int rowAdded = board.addPiece(i, playerType);
				
				String repeated = new String(new char[Math.abs(levels-maxLevel)*2]).replace("\0", "-");
				System.out.println(repeated+">"+i+") In depth = "+levels);
				board.printBoard(new String(new char[Math.abs(levels-maxLevel)*2]).replace("\0", " ")+" ");
				
				// Call minimax recursively and return the maximum to best
				int value = minimax(board, levels + 1, !isMax, playerType,alpha,beta,currentPlayer);

				// Undo the move
				board.undoMove(rowAdded, i);

				best = Math.max(best, value);
				alpha = Math.max(alpha, best);

				// Alpha Beta Pruning
				if (beta <= alpha) {
					pruned++;
					break;
				}

			}
			return best;
		}


		// if min will play (Human)
		else {
			int best = MAX;

			// iterate on all columns
			for(int i = 0; i < Board.COLUMNS; i++) {
				if(board.columnsCounter[i] > 5) { // columns is full
					continue;
				}
				int rowAdded = board.addPiece(i, opponent);
				
				String repeated = new String(new char[Math.abs(levels-maxLevel)*2]).replace("\0", "-");
				System.out.println(repeated+">"+i+") In depth = "+levels);
				board.printBoard(new String(new char[Math.abs(levels-maxLevel)*2]).replace("\0", " ")+" ");
				
				// Call minimax recursively and return the minimum to best
				int value = minimax(board, levels +1, !isMax, playerType,alpha,beta,currentPlayer);

				// Undo the move
				board.undoMove(rowAdded, i);

				best = Math.min(best, value);
				beta = Math.min(beta, best);

				// Alpha Beta Pruning
				if (beta <= alpha) {
					pruned++;
					break;
				}
			}
			return best;
		}
	}
	// This will return the best possible next move for the current board
	public static int playBestMove(Board board, String playerType) {
		double start= System.nanoTime();
		int bestVal = Integer.MIN_VALUE;
		int colToPlay = 0;
		// Apply minimax algorithm to get the best move
		for(int i = 0; i < Board.COLUMNS; i++) {
			if(board.columnsCounter[i] > 5) { // columns is full
				continue;
			}
			int rowAdded = board.addPiece(i, playerType);

			// apply the minimax algorithm to that possible move
			int nextStateVal = minimax(board, 0, false, playerType,MIN,MAX,playerType); //false because min will play

			board.undoMove(rowAdded, i);

			// If the value of the next state is more than the best value, then update best.
			if (nextStateVal > bestVal) {
				colToPlay=i;
				bestVal = nextStateVal;
			}

		}

		System.out.println("Pruned nodes: "+pruned);
		double end=System.nanoTime();
		double time=(end-start)/ 1000000.0;
		System.out.println("Minimax with Alpha Beta took ("+time+") milliseconds");
		return colToPlay;
	}
}
