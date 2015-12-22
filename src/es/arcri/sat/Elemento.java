package es.arcri.sat;

import java.util.LinkedList;
import java.util.List;

public class Elemento {
	public List<Clausula> clausulasTrue = new LinkedList<>();
	public List<Clausula> clausulasFalse = new LinkedList<>();
	public final int id;

	public Elemento() {
		id = uid();
	}

	private static int uid = 0;

	private static int uid() {
		return uid++;
	}

	public void addClausulaTrue(Clausula c) {
		if (!clausulasTrue.contains(c)) {
			clausulasTrue.add(c);
		}
	}

	public void addClausulaFalse(Clausula c) {
		if (!clausulasFalse.contains(c)) {
			clausulasFalse.add(c);
		}
	}

	public List<Clausula> getElementos(boolean negado) {
		if (negado) {
			return clausulasFalse;
		} else {
			return clausulasTrue;
		}
	}

	@Override
	public String toString() {
		return id + "";
	}
}
