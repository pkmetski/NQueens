package Logic;

import java.util.LinkedList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;
import net.sf.javabdd.MicroFactory.bdd;

public class BDDBuilder {

	public BDD rulez;// rules we created for the board
	private BDDFactory fact;
	private BDD[][] board;

	public BDDBuilder(int boardSize) {

		int nodesCount = boardSize * boardSize;
		nodesCount = nodesCount == 1 ? 2 : nodesCount;// to make it work for size 1*1
		fact = JFactory.init(2000000, 200000);
		fact.setVarNum(nodesCount);// create a variable for each square in the board

		board = new BDD[boardSize][boardSize];

		// init the board
		for (int i = 0, varIndx = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++, varIndx++) {
				board[i][j] = fact.ithVar(varIndx);
			}
		}

		 checkBDD();
	}

	//create the rules for the board
	private void checkBDD() {

		rulez = fact.one();// init the rulez 
		for (int i = 0; i < board.length; i++) {
			BDD temp = fact.zero();
			for (int j = 0; j < board[i].length; j++) {
				for (int k = j + 1; k < board[i].length; k++) {
					rulez = rulez.and(board[i][j].not().or(board[i][k].not()));// horizontal rule
					rulez = rulez.and(board[j][i].not().or(board[k][i].not()));// vertical rule
				}
				// positive diagonal rule 
				for (int _i = i + 1, _j = j + 1; _i < board.length
						&& _j < board[i].length; _i++, _j++) {
					rulez = rulez
							.and(board[i][j].not().or(board[_i][_j].not()));
				}
				// one queen at least in a row rule
				temp = temp.or(board[i][j]);
			}
			rulez = rulez.and(temp);
			// negative diagonal rule
			for (int j = board[i].length - 1; 0 <= j; j--) {
				for (int _i = i + 1, _j = j - 1; _i < board.length && 0 <= _j; _i++, _j--) {
					rulez = rulez
							.and(board[i][j].not().or(board[_i][_j].not()));
				}
			}
		}
	}

	public boolean satisfiable(int x, int y) {
		this.board[x][y] = fact.one();// add the queen to the BDD board
		checkBDD();// create the new rules from the current positions of the queens
		return !this.rulez.isZero();// return true if its satisfiable
	}

	public void findCrosses(int[][] board2, int numberOfQueens) {
		int emptySpaceCounter = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board2[i][j] != 1) {
					int count = 0;// a counter for checking if all the sat have zero value 
					for (byte[] list : (LinkedList<byte[]>) rulez.allsat()) {// check all the sat for putting a cross in the board
						if (list[i * board[i].length + j] == 1) {
							break;
						} else {
							count++;
						}
					}
					if (count == rulez.allsat().size()) {
						board2[i][j] = -1;// put a cross in that position
					}
					if (board2[i][j] == 0) {
						emptySpaceCounter++;
					}
				}
			}
		}
		if (emptySpaceCounter <= numberOfQueens) {// when the number of queens left is equal to the empty spaces
			putQueens(board2);
		}
	}

	// put queeens in the empty spaces
	private void putQueens(int[][] board2) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board2[i][j] == 0) {
					board2[i][j] = 1;
				}
			}
		}
	}
}
