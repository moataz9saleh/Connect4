package Game;

import java.util.*;

public class Board {
	public static final int ROWS = 6;
	public static final int COLUMNS = 7;

	public Piece[][] board;
	public int[] columnsCounter = new int[COLUMNS];

	public Piece[][] getBoard() {
		return board;
	}

	public int getRows() {
		return ROWS;
	}

	public int getColumns() {
		return COLUMNS;
	}

	public Board() {
		board = new Piece[ROWS][COLUMNS];
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				board[row][col] = new Piece();
				board[row][col].setValue(0);
			}
		}
	}

	public int addPiece(int colToAdd, String playerType) {
		// within normal range
		
		int value=setPlayer(playerType);

		if (colToAdd >= 0 && colToAdd < COLUMNS) {
			// valid column
			if (columnsCounter[colToAdd] < 6) {
				// that column is not full
				int rowToAdd = 5 - columnsCounter[colToAdd]; // the next empty row in the column
				board[rowToAdd][colToAdd].setPlayerType(playerType);
				board[rowToAdd][colToAdd].setValue(value);
				columnsCounter[colToAdd]++;
				return rowToAdd;
			} else {
				// that column is full
				System.err.println("This column is full, please choose another.");
				return -1;
			}
		} else {
			// outside normal range
			System.err.println("You are trying to add somewhere that is not supported.");
			return -1;
		}
		
	}

	public void printBoard(String spaces) {
		
		for (int i = 0; i < ROWS; i++) {
			System.out.print(spaces);
			for (int j = 0; j < COLUMNS; j++) {
				System.out.print(" | " + this.board[i][j].getValue());
			}
			System.out.print(" |\n");
		}
	}

	public int getScore(String playerType,int depth,int maxDepth) {
		
		int score = 0;
		int player = setPlayer(playerType);
		String opponentType = (playerType=="AI") ? "Human" : "AI";
		/* We scan each element in the board */
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				// This is the current element in our board
				int element = board[row][col].value;
				
				int countEmpty = 0; // counter of the places where 0 exists
				int countPlayer = 0; // counter of the places where 1 exists
				int countOpponent = 0; // counter of the places where 2 exists
				
				 // (Checking Horizontally)
				//  there is 4 places to right
				if (col <= board[row].length - 4) {
					for(int i = 0; i < 4; i++) {
						if(board[row][col + i].value == player) {
							countPlayer++;
						} else if (board[row][col + i].value == 0) {
							countEmpty++;
						} else {
							countOpponent++;
						}
					}
					
					score += calculateScore(countPlayer, countEmpty, countOpponent,playerType,opponentType,depth,maxDepth);
				}
				/* (vertical) */
				countEmpty = 0;
				countPlayer = 0;
				countOpponent = 0;
				if (row <= board.length - 4) {
					for(int i = 0; i < 4; i++) {
						if(board[row + i][col].value == player) {
							countPlayer++;
						} else if (board[row + i][col].value == 0) {
							countEmpty++;
						} else {
							countOpponent++;
						}
					}
					score += calculateScore(countPlayer, countEmpty, countOpponent,playerType,opponentType,depth,maxDepth);

				}
				// (Checking Right Diagonal *\*) 
				countEmpty = 0;
				countPlayer = 0;
				countOpponent = 0;
				if (row <= board.length - 4 && col <= board[row].length - 4)
				{
					for(int i = 0; i < 4; i++) {
						if(board[row + i][col + i].value == player) {
							countPlayer++;
						} else if (board[row + i][col + i].value == 0) {
							countEmpty++;
						} else {
							countOpponent++;
						}
					}
					score += calculateScore(countPlayer, countEmpty, countOpponent,playerType,opponentType,depth,maxDepth);
					
				}
				// (Checking Left Diagonal */*) //row 2 col 3
				countEmpty = 0;
				countPlayer = 0;
				countOpponent = 0;
				if (row <= board.length - 4 && col >= board[row].length - 4) {
					for(int i = 0; i < 4; i++) {
						if(board[row + i][col - i].value == player) {
							countPlayer++;
						} else if (board[row + i][col - i].value == 0) {
							countEmpty++;
						} else {
							countOpponent++;
						}
					}
					score += calculateScore(countPlayer, countEmpty, countOpponent,playerType,opponentType,depth,maxDepth);
				}
			}
		}
		/* return the score of the board */
		return score;

	}
	
	public boolean isBoardFull(){
		for (int row=0;row<ROWS;row++){
			for (int col=0;col<COLUMNS;col++){
				if (board[row][col].getValue()==0){
					return false;
				}
			}
		}
		return true;
	}

	public int countFours(String playerType) {
		
		int connectedFours = 0;
		int player = setPlayer(playerType);
		
		/* We scan each element in the board */
		for( int row = 0; row < board.length; row++ )
		{
			for( int col = 0; col < board[row].length; col++ )
			{
				// This is the current element in our board that we are scanning
				int element = board[row][col].value;

				if (element==0) { // if it's an empty place then skip to the next
					continue;
				}

				// (Checking Horizontally)
				//  there is 4 places to right && all the next four elements = element = player
				if( col <= board[row].length-4 && element == board[row][col+1].value && element == board[row][col+2].value && element == board[row][col+3].value && element==player ) 
				{
					connectedFours++;
				}

				// (Checking Vertically)
				//  there is 4 places to down && all the next four elements = element = player
				if( row <= board.length - 4 && element == board[row+1][col].value && element == board[row+2][col].value && element == board[row+3][col].value && element==player )
				{
					connectedFours++;
				}

				// (Checking Right Diagonal)
				//  there is 4 places in the right diagonal
				if( row <= board.length-4 && col <= board[row].length-4 )
				{
					// all the next four elements in the right diagonal = element = player
					if( element == board[row+1][col+1].value && element == board[row+2][col+2].value && element == board[row+3][col+3].value && element==player)
						connectedFours++;
				}

				// (Checking Left Diagonal)
				// there is 4 places in the left diagonal
				if( row <= board.length-4 && col >= board[row].length-4 ) {
					// all the next four elements in the left diagonal = element = player
					if (element == board[row + 1][col - 1].value && element == board[row + 2][col - 2].value && element == board[row + 3][col - 3].value && element == player)
						connectedFours++;
				}
			}
		}

		return connectedFours;
	}

	public int checkWinner() {
		if(isBoardFull()) {
			if(countFours("AI") > countFours("Human")) {
				System.out.println("AI agent won by connecting "+countFours("AI")+" fours");
				return 1;
			} else if (countFours("AI") < countFours("Human")) {
				System.out.println("Human won by connecting "+countFours("Human")+" fours");
				return 2;
			}
		}
		return 0;
	}
	
	public void undoMove(int rowIndex, int columnIndex) {
		board[rowIndex][columnIndex].setValue(0);
		this.columnsCounter[columnIndex]--;
	}

	public int setPlayer(String playerType) {
		if (playerType=="Ai" || playerType=="AI"){
			return 1;
		} else if (playerType=="Human" || playerType=="human"){
			return 2;
		} 
		return 0;
	}
	
	public int calculateScore(int countPlayer, int countEmpty, int countOpponent,String playerType, String opponent, int depth, int maxDepth) {
		int score = 0;

		if (countPlayer == 4 && !this.isBoardFull()) { // player has line of four
			score += 65 * countFours(playerType);
		} else if (countPlayer == 3 && countEmpty == 1) { // player has line of three
			score += 5;
		} else if (countPlayer == 2 && countEmpty == 2) { // player has line of two
			score += 2;
		}
		if (countOpponent == 4 && !this.isBoardFull()) { // opponent has line of four
			score -= 20 * countFours(opponent) * (maxDepth);
		} else if (countOpponent == 3 && countEmpty == 1) { // opponent has line of three
			score -= 5*countFours(opponent)*maxDepth;
		} else if (countOpponent == 2 && countEmpty == 2) { // opponent has line of two
			score -= 2*countFours(opponent)*maxDepth;
		} 
		return score;
	}
}
