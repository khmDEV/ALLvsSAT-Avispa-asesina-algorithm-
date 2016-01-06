package es.arcri.sat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import es.arcri.sat.exceptions.IsNotASAT2;

public class Krom {
	public void main(String[] args) {
		Entrada a = new Entrada(), b = new Entrada(), c = new Entrada(), d = new Entrada();
		Clausula c1 = new Clausula();
		c1.add(new Literal(a, false));
		c1.add(new Literal(a, false));

		Clausula c2 = new Clausula();
		c2.add(new Literal(a, true));
		c2.add(new Literal(a, true));

		Clausula c3 = new Clausula();
		c3.add(new Literal(a, false));

		Clausula c4 = new Clausula();
		c4.add(new Literal(c, true));
		c4.add(new Literal(d, true));

		Clausula c5 = new Clausula();
		c5.add(new Literal(a, true));
		c5.add(new Literal(b, false));

		Problema problema = new Problema();
		problema.add(c1);
		problema.add(c2);
		problema.add(c3);
		problema.add(c4);
		problema.add(c5);
		System.out.println(problema);

		try {
			System.out.println(krom(new HashMap<Entrada, Boolean>(), problema));
		} catch (IsNotASAT2 e) {
			e.printStackTrace();
		}
	}
	
	public static boolean krom(Problema problema) throws IsNotASAT2 {
		return krom(new HashMap<Entrada,Boolean>(),problema);
	}

	public static boolean krom(HashMap<Entrada, Boolean> forced,
			LinkedList<Clausula> l) throws IsNotASAT2 {
		while (l.size()!=0 && l.get(0).size()==0) {
			l.remove(0);
		}
		if (l.size()==0) {
			return true;
		}
		Literal w = l.get(0).get(0);
		
		if (forced.containsKey(w.entrada)) {
			return kromAux(forced, l, w.entrada, forced.get(w.entrada));
		}
		
		return kromAux(forced, l, w.entrada,true)
				|| kromAux(forced, l, w.entrada,false);
	}

	public static boolean kromAux(HashMap<Entrada, Boolean> forced,
			LinkedList<Clausula> l, Entrada v, boolean entrada) throws IsNotASAT2 {
		List<Clausula> recovery = new LinkedList<Clausula>();
		List<Entrada> forceds = new LinkedList<Entrada>();
		
		if (forced.put(v, entrada)==null) {
			forceds.add(v);
		}
		
		List<Clausula> isTrue = v.getElementos(!entrada), isFalse = v
				.getElementos(entrada);

		for (Clausula clausula : isTrue) { // elimina las clausulas que se
											// cumplen
			if (l.remove(clausula)) { // Si el elemento se elimina lo guardamos
										// en recovery
				recovery.add(clausula);
			}
		}

		for (Clausula clausula : isFalse) {
			if (l.contains(clausula)) {

				switch (clausula.size()) {
				case 1: // Clausula con un unico elemento
						// La clausula no puede ser nunca true
					// Recupera estado anterior
					for (Entrada ff : forceds) {
						forced.remove(ff);
					}
					l.addAll(recovery);
					return false;
				case 2: // Clausula con 0 o >2 elementos ERROR
					Literal w = clausula.distinct(v,entrada);
					if (w == null) {
						for (Entrada ff : forceds) {
							forced.remove(ff);
						}
						l.addAll(recovery);
						return false;
					}
					
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

						if (forced.put(w.entrada, !w.negado)==null) {
							forceds.add(w.entrada);
						}
						l.remove(clausula); // El elemento se elimina
						recovery.add(clausula); // Lo guardamos en recovery

					} else {
						for (Entrada ff : forceds) {
							forced.remove(ff);
						}
						l.addAll(recovery);
						return false;
					}

					break;
				default: // Clausula con 0 o >2 elementos ERROR
					throw new IsNotASAT2(clausula);
				}
			}

		}

		if (l.size() == 0) { // Fin del algoritmo
			return true;
		} else { // SAT reducido
			boolean r=krom(forced, l);
			for (Entrada ff : forceds) {
				forced.remove(ff);
			}
			l.addAll(recovery);
			return r;
		}
	}
}
