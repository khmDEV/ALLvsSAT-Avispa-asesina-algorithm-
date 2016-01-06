package es.arcri.sat;

import java.util.LinkedList;
import java.util.List;

import es.arcri.sat.exceptions.InvalidId;

public class Entrada {
	public List<Clausula> clausulasSinNegar = new LinkedList<>();
	public List<Clausula> clausulasNegadas = new LinkedList<>();
	public final String id;

	public Entrada() {
		id = "I"+uid();
	}
	
	public Entrada(String s) throws InvalidId {
		if (!isAValidId(s)) {
			throw new InvalidId(s);
		}
		id = s;
	}
	
	private boolean isAValidId(String s){
		return s.matches("[A-Za-z][A-Za-z0-9_]*");
	}

	private static int uid = 0;

	private static int uid() {
		return uid++;
	}

	public void addClausulaTrue(Clausula c) {
		if (!clausulasSinNegar.contains(c)) {
			clausulasSinNegar.add(c);
		}
	}

	public void addClausulaFalse(Clausula c) {
		if (!clausulasNegadas.contains(c)) {
			clausulasNegadas.add(c);
		}
	}

	public List<Clausula> getElementos(boolean negado) {
		if (negado) {
			return clausulasNegadas;
		} else {
			return clausulasSinNegar;
		}
	}

	@Override
	public String toString() {
		return id + "";
	}
}
