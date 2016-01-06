package es.arcri.sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import es.arcri.sat.exceptions.InvalidId;
import es.arcri.sat.exceptions.IsNotASAT2;
import es.arcri.sat.exceptions.IsNotHorn;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {

			BufferedReader reader = null;

			try {
				reader = new BufferedReader(new InputStreamReader(System.in));
				while (true) {
					System.out
							.println("Format example: (id + id) * (-id + id)");
					System.out.println("ID format: [A-Za-z][A-Za-z0-9_]*");
					System.out.println("+: OR");
					System.out.println("*: AND");
					System.out.println("-: NOT");
					System.out.println("Write a SAT problem(0 to exit):");
					String line = reader.readLine();
					if (line.equalsIgnoreCase("0")) {
						break;
					}
					try {
						Problema problema = new Problema(line);
						solve(problema);
					} catch (InvalidId e) {
						System.err.println(e.getMessage() + "\n");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			for (String string : args) {
				try {
					List<Problema> problemas = readDirectory(string);
					for (Problema problema : problemas) {
						System.out.println("File: "+problema.getFileName());
						solve(problema);
						System.out.println("\n\n");
					}
				} catch (InvalidId e) {
					System.err.println(e.getMessage() + "\n");
				}
			}
		}

	}
	
	public static List<Problema> readDirectory(String dir) throws InvalidId {
		return readDirectory(new File(dir),new LinkedList<Problema>());
	}

	public static List<Problema> readDirectory(File dir,List<Problema> problemas) throws InvalidId {
		if (!dir.exists() || !dir.canRead()) {
			return problemas;
		}
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				readDirectory(f,problemas);
			}
		} else {
			System.out.println();
			problemas.add(new Problema(dir));
		}
		return problemas;
	}

	/*
	 * Solve problem
	 */
	public static boolean solve(Problema problema) {
		System.out.println(problema);
		System.out.println("//////Horn Solver");
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
		System.out.println("//////Krom Solver");
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

		System.out.println("//////Backtraking");
		long start = System.currentTimeMillis(), finish;
		canSolve = Backtraking.backtraking(problema);
		finish = System.currentTimeMillis();
		System.out.println(canSolve ? "The output can be true"
				: "The output can't be true");
		System.out.println("time: " + (finish - start) + " ms");
		return canSolve;
	}

}
