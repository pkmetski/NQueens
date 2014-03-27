package Logic;

import java.util.LinkedList;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class BDDBuilder {

	public BDD rulez;
	private BDDFactory fact;
	private BDD[][] board;

	public BDDBuilder(int boardSize) {

		int nodesCount = boardSize * boardSize;
		nodesCount = nodesCount == 1 ? 10 : nodesCount;
		fact = JFactory.init(nodesCount, nodesCount);
		fact.setVarNum(nodesCount);

		board = new BDD[boardSize][boardSize];

		// init the board
		for (int i = 0, varIndx = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++, varIndx++) {
				board[i][j] = fact.ithVar(varIndx);
			}
		}

		 checkBDD();
	}

	private void checkBDD() {

		rulez = fact.one();
		for (int i = 0; i < board.length; i++) {
			BDD temp = fact.zero();
			for (int j = 0; j < board[i].length; j++) {
				for (int k = j + 1; k < board[i].length; k++) {
					rulez = rulez.and(board[i][j].not().or(board[i][k].not()));// hor
					rulez = rulez.and(board[j][i].not().or(board[k][i].not()));// ver
				}
				// positive diag
				for (int _i = i + 1, _j = j + 1; _i < board.length
						&& _j < board[i].length; _i++, _j++) {
					rulez = rulez
							.and(board[i][j].not().or(board[_i][_j].not()));
				}
				// one queen at least
				temp = temp.or(board[i][j]);
			}
			rulez = rulez.and(temp);
			// negative diag
			for (int j = board[i].length - 1; 0 <= j; j--) {
				for (int _i = i + 1, _j = j - 1; _i < board.length && 0 <= _j; _i++, _j--) {
					rulez = rulez
							.and(board[i][j].not().or(board[_i][_j].not()));
				}
			}
		}
	}

	public boolean satisfiable(int x, int y) {
		this.board[x][y] = fact.one();
		checkBDD();
		return !this.rulez.isZero();
	}

	public void findCrosses(int[][] board2) {
		int emptySpaceCounter = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board2[i][j] != 1) {
					int count = 0;
					for (byte[] list : (LinkedList<byte[]>) rulez.allsat()) {
						if (list[i * board[i].length + j] == 1) {
							break;
						} else {
							count++;
						}
					}
					if (count == rulez.allsat().size()) {
						board2[i][j] = -1;
					}
					if (board2[i][j] == 0) {
						emptySpaceCounter++;
					}
				}
			}
		}
		if (emptySpaceCounter <= board2.length) {
			putQueens(board2);
		}
	}

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
