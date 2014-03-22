package Logic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

		rulez = checkBDD();
	}

	private BDD checkBDD() {

		rulez = fact.one();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				for (int k = j + 1; k < board[i].length; k++) {
					rulez = rulez.and(board[i][j].not().or(board[i][k].not()));// hor
					rulez = rulez.and(board[j][i].not().or(board[k][i].not()));// ver
				}
			}
		}

		// positive diag
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				for (int _i = i + 1, _j = j + 1; _i < board.length
						&& _j < board[i].length; _i++, _j++) {
					rulez = rulez
							.and(board[i][j].not().or(board[_i][_j].not()));
				}
			}
		}

		// negative diag
		for (int i = 0; i < board.length; i++) {
			for (int j = board[i].length - 1; 0 <= j; j--) {
				for (int _i = i + 1, _j = j - 1; _i < board.length && 0 <= _j; _i++, _j--) {
					rulez = rulez
							.and(board[i][j].not().or(board[_i][_j].not()));
				}
			}
		}

		// one queen at least
		for (int i = 0; i < board.length; i++) {
			BDD temp = fact.zero();
			for (int j = 0; j < board[i].length; j++) {
				temp = temp.or(board[i][j]);
			}
			rulez = rulez.and(temp);
		}
		return rulez;
	}

	public boolean satisfiable(int x, int y, int[][] board) {
		this.board[x][y] = fact.one();
		return !this.rulez.isZero();
	}

	public void findCrosses() {
		for (byte[] list : (LinkedList<byte[]>) rulez.allsat()) {
			byte[] bt = list;

		}
	}
}
