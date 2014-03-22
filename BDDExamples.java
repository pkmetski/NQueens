import net.sf.javabdd.*;

public class BDDExamples {

	

	/**
	 * Gives some examples on how the BDD package is used
	 */
	public static void main() {

		int boardSize = 5;

		int nodesCount = boardSize * boardSize;
		nodesCount = nodesCount == 1 ? 10 : nodesCount;
		BDDFactory fact = JFactory.init(nodesCount, nodesCount);
		fact.setVarNum(nodesCount);

		BDD[][] board = new BDD[boardSize][boardSize];

		// init the board
		for (int i = 0, varIndx = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++, varIndx++) {
				board[i][j] = fact.ithVar(varIndx);
			}
		}

//		BDD rulez = checkBDD(board, fact);

		BDD True = fact.one();
		BDD False = fact.zero();

		// the expression x0
		BDD x_0 = fact.ithVar(0);

		// the expression not x1
		BDD nx_1 = fact.nithVar(1);

		BDD nA = fact.nithVar(2);
		BDD A = fact.ithVar(2);
		BDD nB = fact.nithVar(3);
		BDD nC = fact.nithVar(4);
		BDD nD = fact.nithVar(5);
		BDD nE = fact.nithVar(6);
		BDD AHor = (nA.or(nB)).and(nA.or(nC)).and(nA.or(nD)).and(nA.or(nE));
		// Checks whether or not expression is unsat
		System.out.println("b is unsat? : " + True.isZero());

		// checks whether expression is tautology
		System.out.println("b is tautology? : " + True.isOne());

		// the expression (not x1 or x0) and (True or false)
		// BDD b = nx_1.or(x_0).and(True.or(False));
		// BDD b = True.or(False).or(AHor);
		BDD b = A.or(nA);
		A = fact.one();
		BDD c = A;
		System.out.println("b is tautology? : " + b.isOne());
		System.out.println("b is tautology? : " + c.isOne());

		// Checks whether or not expression is unsat
		System.out.println("b is unsat? : " + b.isZero());

		// checks whether expression is tautology
		System.out.println("b is tautology? : " + b.isOne());

		// In order to restrict or quantify the expression to a given assignment
		// we give the assignment as a conjunction where positive variables
		// indicate that the variable should be restricted to false, and vice
		// versa.
		BDD restriction_x1_true_x0_false = fact.ithVar(1).and(fact.nithVar(0));

		BDD restricted = b.restrict(restriction_x1_true_x0_false);
		BDD existed = b.exist(x_0);

		// Exist. should be tautology
		System.out
				.println("Existiential quant. cause taut: " + existed.isOne());

		// Restriction shoule be unsat:
		System.out.println("Restriction caused unsat: " + restricted.isZero());

		// how to perform replacement
		BDDPairing replacement = fact.makePair();
		int[] from = { 1 };
		int[] to = { 0 };
		replacement.set(from, to);

		BDD b_replaced = existed.replace(replacement);

	}
}
