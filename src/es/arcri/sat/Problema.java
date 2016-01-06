package es.arcri.sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import es.arcri.sat.exceptions.InvalidId;

@SuppressWarnings("serial")
public class Problema extends LinkedList<Clausula> {
	private static final boolean RECHECK = true;
	private boolean kromCanSolve = true;
	private boolean HornCanSolve = true;
	private boolean hasLiteral = false;
	private String file_name = null;
	public Problema() {

	}

	public Problema(File file) throws InvalidId {
		loadFile(file);
	}

	public Problema(String problem) throws InvalidId {
		loadProblem(problem);
	}

	public boolean kromCanSolve() {
		if (RECHECK) {
			kromCanSolve = true;
			for (Clausula c : this) {
				if (c.size() > 2) {
					kromCanSolve = false;
					break;
				}
			}
		}

		return kromCanSolve;
	}

	public boolean HornCanSolve() {
		if (RECHECK) {
			hasLiteral = false;
			HornCanSolve = true;
			boolean u = false;
			for (Clausula c : this) {
				u = false;	
				for (Literal e : c) {
					if (!e.negado) {
						if (u) {
							HornCanSolve = false;
							break;
						} else {
							u = true;
						}
					}
				}
				hasLiteral = hasLiteral || c.size() == 1;
			}
			if (!u) {
				HornCanSolve = true;
			}

		}
		return HornCanSolve && hasLiteral;
	}

	/*
	 * Para mantener actualizados los flags no se debera modificar las clausulas
	 * después de añadirlas
	 * 
	 * @see java.util.LinkedList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Clausula c) {
		if (c.size() > 2) {
			kromCanSolve = false;
		}
		boolean u = false;

		for (Literal e : c) {
			if (!e.negado) {
				if (u) {
					HornCanSolve = false;
					break;
				} else {
					u = true;
				}
			}
		}
		hasLiteral = hasLiteral || c.size() == 1;
		if (!u) {
			HornCanSolve = false;
		}

		return super.add(c);
	}

	public void loadProblem(String problem) throws InvalidId {
		// remove stop-words
		problem = problem.replace(" ", "");
		problem = problem.replace("\n", "");
		problem = problem.replace("(", "");
		problem = problem.replace(")", "");
		HashMap<String, Entrada> elms = new HashMap<String, Entrada>();
		for (String clausula : problem.split("\\*")) {
			Clausula c = new Clausula();
			for (String el : clausula.split("\\+")) {
				boolean negado = false;
				if (el.startsWith("-")) {
					el = el.substring(1);
					negado = true;
				}
				Entrada e;
				if (elms.containsKey(el)) {
					e = elms.get(el);
				} else {
					e = new Entrada(el);
					elms.put(el, e);
				}
				c.add(new Literal(e, negado));
			}
			add(c);
		}
	}

	public void loadFile(String file) throws InvalidId {
		loadFile(new File(file));
	}

	public void loadFile(File file) throws InvalidId {
		file_name=file.getName();
		FileInputStream in = null;
		BufferedReader reader = null;

		try {
			in = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			String p = "";
			while ((line = reader.readLine()) != null) {
				p += line;
			}
			loadProblem(p);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	@Override
	public String toString() {
		String r = "";
		for (int i = 0; i < size(); i++) {
			r += get(i).toString();
			if (i != size() - 1) {
				r += " * ";
			}
		}
		return r;
	}

	public String DIMACSFormat() {
		String r = "";

		List<Entrada> els = new LinkedList<Entrada>();
		for (Clausula c : this) {
			for (Literal elc : c) {
				if (!els.contains(elc.entrada)) {
					els.add(elc.entrada);
				}
			}
		}
		r += "p cnf " + els.size() + " " + size() + "\n";

		for (int i = 0; i < size(); i++) {
			Clausula c = get(i);
			for (int j = 0; j < c.size(); j++) {
				Literal ec = c.get(j);
				if (ec.negado) {
					r += "-";
				}
				for (int j2 = 0; j2 < els.size(); j2++) {
					if (els.get(j2) == ec.entrada) {
						r += j2 + 1;
						break;
					}
				}
				if (j != c.size() - 1) {
					r += " ";
				}
			}
			if (i != size() - 1) {
				r += "\n";
			}
		}
		return r;
	}

	/*
	 * Create Random Problem
	 */
	public static Problema createRandomProblem(int maxClausules,
			int minClausules, int maxElements, int minElements,
			int maxUElements, int minUElements) {
		Problema problem = new Problema();
		int nuele = Math.round((float) Math.random()
				* (maxClausules - minClausules) + minClausules);
		List<Entrada> els = new LinkedList<Entrada>();
		for (int i = 0; i < nuele; i++) {
			els.add(new Entrada());
		}

		int nclau = Math.round((float) Math.random()
				* (maxClausules - minClausules) + minClausules);
		for (int i = 0; i < nclau; i++) {
			int nele = Math.round((float) Math.random()
					* (maxElements - minElements) + minElements);
			Clausula c = new Clausula();
			for (int j = 0; j < nele; j++) {
				int el = Math.round((float) Math.random() * (nuele - 1));
				boolean n = Math.random() > 0.5;
				c.add(new Literal(els.get(el), n));
			}
			problem.add(c);
		}
		return problem;
	}

	@Override
	public Problema clone() {
		Problema p = new Problema();
		HashMap<String, Entrada> els = new HashMap<String, Entrada>();
		for (Clausula clausula : this) {
			Clausula c = new Clausula();
			for (Literal e : clausula) {
				String id = e.entrada.id;
				if (els.containsKey(id)) {
					c.add(new Literal(els.get(id), e.negado));
				} else {
					Literal elc;
					try {
						elc = new Literal(new Entrada(id), e.negado);
						c.add(elc);
						els.put(id, elc.entrada);
					} catch (InvalidId e1) {
						e1.printStackTrace();
					}

				}
			}
			p.add(c);
		}
		return p;
	}

	public String getFileName() {
		return file_name;
	}
}
