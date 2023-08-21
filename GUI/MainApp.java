package GUI;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import Algorithm.AlphaBeta;
import Algorithm.Minimax;
import Game.Board;

public class MainApp extends JFrame{
	private JFrame frame;
    private JButton[][] slots;
    private ButtonGroup[] columnsGroups;
    //variables used in grid
    private int columns = Board.COLUMNS;
    private int rows = Board.ROWS;
    Board board = new Board();
    int player=1;
    int selected = 0;
    
    public MainApp() {

        frame = new JFrame("Connect Four");
        String[] options = {"Without AlphaBeta","With AlphaBeta"};
        selected = JOptionPane.showOptionDialog(null,
			    "Please choose an algorithm!",
			    "Algorithm",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
        String depthStr=JOptionPane.showInputDialog(this,"Enter Depth");
        int depth = Integer.parseInt(depthStr);
        Minimax.maxDepth = depth;
        AlphaBeta.maxLevel = depth;
        
        JPanel mainPanel = (JPanel) frame.getContentPane();
        mainPanel.setLayout(new GridLayout(rows, columns));
        
        slots = new JButton[columns][rows];
        columnsGroups = new ButtonGroup[columns];
       
        for (int column = 0; column < rows; column++) {

            for (int row = 0; row < columns; row++) {
                slots[row][column] = new JButton();
                slots[row][column].setHorizontalAlignment(SwingConstants.CENTER);
                slots[row][column].setBorder(new LineBorder(Color.DARK_GRAY));
                mainPanel.add(slots[row][column]);
            }
            //mainPanel.add(currentPanel);
        }
        for(int i = 0; i<slots.length; i++) {
        	for(int j = 0; j<slots[i].length; j++) {
        		slots[i][j].setName("Column "+i);
        		slots[i][j].addActionListener(new Listener());
        	}
        }
        frame.setContentPane(mainPanel);
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void aiTurn() {
    	int col = 0;
    	if(selected==0) {
    		 col = Minimax.playBestMove(board,"AI");
    	} else if(selected == 1) {
    		 col = AlphaBeta.playBestMove(board,"AI");
    	}
		
		int rowAdded = board.addPiece(col,"AI");
		slots[col][rowAdded].setBackground(Color.RED);
		player = 1;
		if(board.isBoardFull()) {
			checkWinning();
		}
	
    }
    
    public void checkWinning() {
    	if(board.checkWinner() == 1) {
    		JOptionPane.showMessageDialog(null, "AI Won by connecting "+board.countFours("AI")+" fours - You connected "+board.countFours("Human"), "Hard Luck", JOptionPane.INFORMATION_MESSAGE);
    	} else if(board.checkWinner() == 2) {
    		JOptionPane.showMessageDialog(null, "You Won by connecting "+board.countFours("Human")+" fours - Ai connected "+board.countFours("AI"), "Well Done!", JOptionPane.INFORMATION_MESSAGE);
    	} else {
    		JOptionPane.showMessageDialog(null, "The game ended draw, you both connected "+board.countFours("AI")+" fours", "Good Game", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
    private class Listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(player == 1) {
				if( ((JButton) e.getSource()).getName().contains("Column")) {
					
					String colStr =  (String) (((JButton) e.getSource()).getName()).substring(7);
					int colNum = Integer.parseInt(colStr);
					int rowAdded = board.addPiece(colNum, "Human");
					if(rowAdded != -1){
						slots[colNum][rowAdded].setBackground(Color.BLUE);
						if(board.isBoardFull()) {
							checkWinning();
						}
						player = 2;
						
						aiTurn();
					}
					else {
						JOptionPane.showMessageDialog(null, "This column is full", "Not Valid Column", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
    }
}
