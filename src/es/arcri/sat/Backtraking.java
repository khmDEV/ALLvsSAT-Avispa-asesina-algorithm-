package es.arcri.sat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Backtraking {
	public void main(String[] args) {
		Entrada a = new Entrada(), b = new Entrada(), c = new Entrada(), d = new Entrada();
		Clausula c1 = new Clausula();
		c1.add(new Literal(a, false));
		c1.add(new Literal(a, false));

		Clausula c2 = new Clausula();
		c2.add(new Literal(a, true));
		c2.add(new Literal(a, true));
		c2.add(new Literal(b, false));

		Clausula c3 = new Clausula();
		c3.add(new Literal(a, false));

		Clausula c4 = new Clausula();
		c4.add(new Literal(c, true));
		c4.add(new Literal(d, true));

		Clausula c5 = new Clausula();
		c5.add(new Literal(a, true));
		c5.add(new Literal(b, false));

		LinkedList<Clausula> list = new LinkedList<Clausula>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		list.add(c5);
		System.out.println(list);

		System.out.println(backtraking(new HashMap<Entrada, Boolean>(), list));
	}
	
	public static boolean backtraking(LinkedList<Clausula> l) {
		return backtraking(new HashMap<Entrada,Boolean>(),l);
	}

	public static boolean backtraking(HashMap<Entrada, Boolean> forced,
			LinkedList<Clausula> problema) {
		while (problema.size()!=0 && problema.get(0).size()==0) {
			problema.remove(0);
		}
		if (problema.size()==0) {
			return true;
		}
		Literal literal = problema.get(0).get(0);
		if (forced.containsKey(literal.entrada)) {
			return backtrakingAux(forced, problema, literal.entrada, forced.get(literal.entrada));
		}

		return backtrakingAux(forced, problema, literal.entrada, true)
				|| backtrakingAux(forced, problema, literal.entrada, false);
	}

	public static boolean backtrakingAux(HashMap<Entrada, Boolean> forced,
			LinkedList<Clausula> problema, Entrada entrada, boolean valor_entrada) {
		List<Clausula> recovery = new LinkedList<Clausula>();
		
		// Forced Input v 
		boolean backup = forced.put(entrada, valor_entrada) == null;
		List<Clausula> isTrue = entrada.getElementos(!valor_entrada), isFalse = entrada
				.getElementos(valor_entrada);
		for (Clausula clausula : isTrue) { // elimina las clausulas que se
											// cumplen
			if (problema.remove(clausula)) { // Si el elemento se elimina lo guardamos
										// en recovery
				recovery.add(clausula);
			}
		}
		if (problema.size() == 0) {
			if (backup) {
				forced.remove(entrada);
			}
			problema.addAll(recovery);

			return true;
		}

		for (Clausula clausula : isFalse) {
			if (problema.contains(clausula)) {

				switch (clausula.size()) {
				case 1: // Clausula con un unico elemento
						// La clausula no puede ser nunca true
					// Recupera estado anterior
					if (backup) {
						forced.remove(entrada);
					}
					problema.addAll(recovery);
					return false;
				default:
					List<Literal> ws = clausula.distincts(entrada, valor_entrada);

					boolean forc = false;
					problema.remove(clausula); // El elemento se elimina
					recovery.add(clausula); // Lo guardamos en recovery
					for (Literal w : ws) {

						if (!forced.containsKey(w.entrada) // no existe una regla que
														// impida
														// que se cumpla
								|| forced.get(w.entrada) != w.negado) { // la regla
																	// permite
																	// dar
																	// una
																	// salida
																	// true a la
																	// clausula

							if (backtrakingAux(forced, problema, w.entrada, !w.negado)) {
								forc = true;
								break;
							}

						}
					}
					if (!forc) {
						if (backup) {
							forced.remove(entrada);
						}
						problema.addAll(recovery);
						return false;
					}

				}
			}

		}

		if (problema.size() == 0) { // Fin del algoritmo
			if (backup) {
				forced.remove(entrada);
			}
			problema.addAll(recovery);

			return true;
		} else { // SAT reducido
			boolean r = backtraking(forced, problema);
				if (backup) {
					forced.remove(entrada);
				}
				problema.addAll(recovery);
			return r;
		}
	}

	

}
