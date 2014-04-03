package Logic;

import java.util.LinkedList;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;
import net.sf.javabdd.MicroFactory.bdd;

public class BDDBuilder {

	public BDD rulez;// rules we created for the board
	private BDDFactory fact;
	private int boardSize;

	public BDDBuilder(int boardSize) {

		this.boardSize = boardSize;
		int nodesCount = boardSize * boardSize;
		nodesCount = nodesCount == 1 ? 2 : nodesCount;// to make it work for size 1*1
		
		int size = 782 * (int) Math.pow(2, boardSize);
		fact = JFactory.init(size * 10, size);
		fact.setVarNum(nodesCount);// create a variable for each square in the board

		buildBDD();
	}

	// create the rules for the board
	private void buildBDD() {

		rulez = fact.one();
		
		//vertical
		rulez.andWith(buildVerticalBdd());
		
		//horizontal
		rulez.andWith(buildHorizontalBdd());

		// positive diagonal
		rulez.andWith(buildPosDiagonalBdd());

		// negative diagonal
		rulez.andWith(buildNegDiagonalBdd());

		// at least one queen in each row
		rulez.andWith(buildMinCountBdd());
	}

	private BDD buildVerticalBdd() {
		BDD temp = fact.one();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int k = j + 1; k < boardSize; k++) {
					temp.andWith(fact.nithVar(j * boardSize + i).or(
							fact.nithVar(k * boardSize + i)));
				}
			}
		}
		return temp;
	}

	private BDD buildHorizontalBdd() {
		BDD temp = fact.one();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int k = j + 1; k < boardSize; k++) {
					temp.andWith(fact.nithVar(i * boardSize + j).or(
							fact.nithVar(i * boardSize + k)));
				}
			}
		}
		return temp;
	}

	private BDD buildPosDiagonalBdd() {
		BDD temp = fact.one();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int _i = i + 1, _j = j + 1; _i < boardSize
						&& _j < boardSize; _i++, _j++) {
					temp.andWith(fact.nithVar(i * boardSize + j).or(
							fact.nithVar(_i * boardSize + _j)));
				}
			}
		}
		return temp;
	}

	private BDD buildNegDiagonalBdd() {
		BDD temp = fact.one();
		for (int i = 0; i < boardSize; i++) {
			for (int j = boardSize - 1; 0 <= j; j--) {
				for (int _i = i + 1, _j = j - 1; _i < boardSize && 0 <= _j; _i++, _j--) {
					temp.andWith(fact.nithVar(i * boardSize + j).or(
							fact.nithVar(_i * boardSize + _j)));
				}
			}
		}
		return temp;
	}

	private BDD buildMinCountBdd() {
		BDD temp = fact.one();
		for (int i = 0; i < boardSize; i++) {
			BDD rowBdd = fact.zero();
			for (int j = 0; j < boardSize; j++) {
				rowBdd.orWith(fact.ithVar(i * boardSize + j));
			}
			temp.andWith(rowBdd);
		}
		return temp;
	}

	public boolean satisfiable(int x, int y) {

		this.rulez.restrictWith(fact.ithVar(x * boardSize + y));
		return !this.rulez.isZero();// return true if its satisfiable
	}

	public void findCrosses(int[][] board2, int numberOfQueens) {
		int emptySpaceCounter = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board2[i][j] != 1) {
					// a counter for checking if all the sat have zero value
					int count = 0;
					for (byte[] list : (LinkedList<byte[]>) rulez.allsat()) {
						// check all the sat for putting a cross in the board
						if (list[i * boardSize + j] == 1) {
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
		if (emptySpaceCounter == board2.length - numberOfQueens) {
			// when the number of queens left is equal to the empty spaces
			putQueens(board2);
		}
	}

	// put queeens in the empty spaces
	private void putQueens(int[][] board2) {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board2[i][j] == 0) {
					board2[i][j] = 1;
				}
			}
		}
	}
}
