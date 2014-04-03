/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;

import Logic.BDDBuilder;
import net.sf.javabdd.*;

public class QueensLogic {
	private int x = 0;
	private int y = 0;
	private int[][] board;
	private BDDBuilder bdd;
	private int numberOfQueens=0;

	public QueensLogic() {
		// constructor
	}

	public void initializeGame(int size) {
		this.x = size;
		this.y = size;
		this.board = new int[x][y];
		bdd = new BDDBuilder(size);// create the BDD board and the rules
		if (bdd.rulez.isZero()) {// check if the whole board is not satisfiable like when the size is 2*2
			setValues(this.board, -1);// put crosses on all the squares in the board
		}
		bdd.findCrosses(board,numberOfQueens());// check for squares that should be crossed
	}

	public int[][] getGameBoard() {
		return board;
	}

	public boolean insertQueen(int column, int row) {

		if (board[column][row] == -1 || board[column][row] == 1) {
			return true;
		}

		board[column][row] = 1;
		
		bdd.satisfiable(column, row);
		bdd.findCrosses(board, ++numberOfQueens);
		return true;
	}

	private void setValues(int[][] board, int value) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = value;
			}
		}
	}
	
	// find number of queens in the board
	private int numberOfQueens() {
		int count=0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j]==1){
					count++;
				}
			}
		}
		return count;
	}
}
