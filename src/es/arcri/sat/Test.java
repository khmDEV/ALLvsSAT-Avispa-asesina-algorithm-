package es.arcri.sat;

import es.arcri.sat.exceptions.InvalidId;
import es.arcri.sat.exceptions.IsNotASAT2;
import es.arcri.sat.exceptions.IsNotHorn;
import es.arcri.sat.exceptions.SATSolverException;

public class Test {
	public static final int DEBUG = 2;
	public static final boolean VERBOSE = true;
	public static final int MAX_CLAUSULES = 40;
	public static final int MIN_CLAUSULES = 10;
	public static final int MAX_UELEMENTS = 20;
	public static final int MIN_UELEMENTS = 2;
	public static final int MAX_ELEMENTS = 10;
	public static final int MIN_ELEMENTS = 1;
	public static final String DEFAULT_TEST = "(-I100846 + I100845) * (I100846) * (-I100846) * (I100846 + I100847) * (-I100846 + -I100845) * (I100846)";

	public static void main(String[] args) {
		switch (DEBUG) {
		case 1:
			testAll();
			return;
		case 2:
			Problema p2;
			try {
				p2 = new Problema(DEFAULT_TEST);
				System.out.println(p2.DIMACSFormat());
				solve(p2);
			} catch (InvalidId e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SATSolverException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		case 3:
			Problema p=null;
			boolean b = true;
			while (b) {
				try {

					p = Problema.createRandomProblem(10, 5, 4, 1,
							MAX_UELEMENTS, MIN_UELEMENTS);
					b=SatHornSolver.UnitPropagation(p);
					System.out.println(b);
				} catch (Exception e) {
					System.out.println("eer");
					b=true;
				}
			}
			System.out.println(p);
			System.out.println(p.DIMACSFormat());

		default:
			break;
		}

	}

	/*
	 * Test all methods
	 */
	private static void testAll() {
		try {
			Problema p = new Problema(
					"(-I75937 + I75937) * (I75936) * (-I75937) * (-I75937 + -I75937) * (I75936 + I75937) * (I75936 + I75937) * (-I75936) * (-I75937 + -I75936)");
			System.out.println("TEST HORN-SAT unsat");
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			if (!SatHornSolver.UnitPropagation(p)) {
				throw new SATSolverException("HORN SAT failed");
			}

			p = new Problema(
					"(I113220) * (I113222) * (I113222 + -I113223) * (I113221) * (I113220) * (I113222) * (-I113221) * (I113221 + -I113221)");
			System.out.println("HORN-SAT sat");
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			if (SatHornSolver.UnitPropagation(p)) {
				throw new SATSolverException("HORN SAT failed");
			}

			p = new Problema(
					"(-I100846 + I100845) * (I100846) * (-I100846) * (I100846 + I100847) * (-I100846 + -I100845) * (I100846)");
			System.out.println("TEST SAT2 unsat");
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			if (Krom.krom(p)) {
				throw new SATSolverException("KROM SAT failed");
			}

			p = new Problema(
					"(-I85532 + -I85536) * (-I85533 + I85532) * (-I85536 + -I85534) * (-I85536 + I85532) * (I85530 + I85535) * (-I85533 + I85536) * (-I85531)");
			System.out.println("TEST SAT2 sat");
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			if (!Krom.krom(p)) {
				throw new SATSolverException("KROM SAT failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Problema p;
		System.out.println("////////////////////////////////////");
		System.out.println("/////////////HORN TESTS/////////////");
		System.out.println("////////////////////////////////////");
		int N = 3;
		for (int i = 0; i < N; i++) {
			do {
				p = Problema.createRandomProblem(10, 5, 5, 1, MAX_UELEMENTS,
						MIN_UELEMENTS);
			} while ((!p.HornCanSolve()));
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			try {
				test(p);
			} catch (SATSolverException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				return;
			}
		}
		System.out.println();
		System.out.println("////////////////////////////////////");
		System.out.println("/////////////KROM TESTS/////////////");
		System.out.println("////////////////////////////////////");
		N = 3;
		for (int i = 0; i < N; i++) {
			p = Problema.createRandomProblem(10, 5, 2, 1, 5, 1);
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			try {
				test(p);
			} catch (SATSolverException e2) {
				e2.printStackTrace();
				return;
			}
		}
		System.out.println();
		System.out.println("///////////////////////////////////");
		System.out.println("/////////Prueba de volumen/////////");
		System.out.println("///////////////////////////////////");
		N = 1000;
		for (int i = 0; i < N; i++) {
			p = Problema.createRandomProblem(MAX_CLAUSULES, MIN_CLAUSULES,
					MAX_ELEMENTS, MIN_ELEMENTS, MAX_UELEMENTS, MIN_UELEMENTS);
			System.out.println("///Problema///");
			System.out.println(p);
			if (VERBOSE) {
				System.out.println("////DIMACS////");
				System.out.println(p.DIMACSFormat());
			}
			System.out.println("/////////////\n");

			try {
				test(p);
			} catch (SATSolverException e2) {
				System.out.println("ee");
				e2.printStackTrace();
				return;
			}
		}
	}

	/*
	 * Solve problem
	 */
	public static boolean solve(Problema problema) throws SATSolverException {
		System.out.println();
		System.out.println(problema);
		System.out.println("SatHornSolver");
		boolean canSolve = false;
		if (problema.HornCanSolve()) {
			long start = System.currentTimeMillis(), finish;
			try {
				canSolve = SatHornSolver.UnitPropagation(problema);
				finish = System.currentTimeMillis();

				System.out.println(canSolve ? "The output can be true"
						: "The output can't be true");
				System.out.println("time: " + (finish - start) + " ms");
				return canSolve;
			} catch (IsNotHorn e) {
				System.out.println("Unit propagation cant solve");
			}

		} else {
			System.out.println("Is not a HORNSAT problem");
		}
		System.out.println("Krom");
		if (problema.kromCanSolve()) {
			long start = System.currentTimeMillis(), finish;
			try {
				canSolve = Krom.krom(problema);
				finish = System.currentTimeMillis();
				System.out.println(canSolve ? "The output can be true"
						: "The output can't be true");
				System.out.println("time: " + (finish - start) + " ms");
				return canSolve;
			} catch (IsNotASAT2 e) {
				System.out.println("Is not a SAT2 problem");
			}

		} else {
			System.out.println("Is not a SAT2 problem");
		}

		System.out.println("Backtraking");
		long start = System.currentTimeMillis(), finish;
		canSolve = Backtraking.backtraking(problema);
		finish = System.currentTimeMillis();
		System.out.println(canSolve ? "The output can be true"
				: "The output can't be true");
		System.out.println("time: " + (finish - start) + " ms");
		return canSolve;
	}

	/*
	 * Test if exist conflicts between the solvers
	 */
	public static boolean test(Problema problema) throws SATSolverException {
		if (VERBOSE) {
			System.out.println();
			System.out.println(problema);
			System.out.println("SatHornSolver");
		}
		boolean horn = false;
		if (problema.HornCanSolve()) {
			long start = System.currentTimeMillis(), finish;
			try {
				horn = SatHornSolver.UnitPropagation(problema);
				finish = System.currentTimeMillis();
				if (VERBOSE) {
					System.out.println(horn ? "The output can be true"
							: "The output can't be true");
					System.out.println("time: " + (finish - start) + " ms");
				}
			} catch (IsNotHorn e) {
				if (VERBOSE) {
					System.out.println("Unit propagation cant solve");
				}
			}

		} else {
			if (VERBOSE) {
				System.out.println("Is not a HORNSAT problem");
			}
		}
		boolean krom = true;
		if (VERBOSE) {
			System.out.println("Krom");
		}
		if (problema.kromCanSolve()) {
			long start = System.currentTimeMillis(), finish;
			try {
				krom = Krom.krom(problema);
				finish = System.currentTimeMillis();
				if (VERBOSE) {
					System.out.println(krom ? "The output can be true"
							: "The output can't be true");
					System.out.println("time: " + (finish - start) + " ms");
				}
			} catch (IsNotASAT2 e) {
				if (VERBOSE) {
					System.out.println("Is not a SAT2 problem");
				}
			}

		} else {
			if (VERBOSE) {
				System.out.println("Is not a SAT2 problem");
			}
		}
		if (VERBOSE) {
			System.out.println("Backtraking");
		}
		long start = System.currentTimeMillis(), finish;
		boolean backtraking = Backtraking.backtraking(problema);
		finish = System.currentTimeMillis();
		if (VERBOSE) {
			System.out.println(backtraking ? "The output can be true"
					: "The output can't be true");
			System.out.println("time: " + (finish - start) + " ms");
		}

		if (problema.kromCanSolve() && backtraking != krom) {
			throw new SATSolverException("Krom is failed");
		}

		if (!backtraking && backtraking != horn) {
			throw new SATSolverException("Horn is failed");
		}
		return backtraking;
	}
}
