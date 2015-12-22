package es.arcri.sat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Brute {
	public static void main(String[] args) {
		Elemento a = new Elemento(), b = new Elemento(), c = new Elemento(), d = new Elemento();
		Clausula c1 = new Clausula();
		c1.addElementoClausula(new ElementoClausula(a, false));
		c1.addElementoClausula(new ElementoClausula(a, false));

		Clausula c2 = new Clausula();
		c2.addElementoClausula(new ElementoClausula(a, true));
		c2.addElementoClausula(new ElementoClausula(a, true));
		c2.addElementoClausula(new ElementoClausula(b, false));

		Clausula c3 = new Clausula();
		c3.addElementoClausula(new ElementoClausula(a, false));

		Clausula c4 = new Clausula();
		c4.addElementoClausula(new ElementoClausula(c, true));
		c4.addElementoClausula(new ElementoClausula(d, true));

		Clausula c5 = new Clausula();
		c5.addElementoClausula(new ElementoClausula(a, true));
		c5.addElementoClausula(new ElementoClausula(b, false));

		LinkedList<Clausula> list = new LinkedList<Clausula>();
		list.add(c1);
		list.add(c2);
		list.add(c3);
		list.add(c4);
		list.add(c5);
		System.out.println(list);

		System.out.println(brute(new HashMap<Elemento, Boolean>(), list));
	}

	public static boolean brute(HashMap<Elemento, Boolean> forced,
			LinkedList<Clausula> l) {
		ElementoClausula w = l.get(0).getElementos().get(0);

		try {
			return bruteAux(forced, l, w)
					|| bruteAux(forced, l, new ElementoClausula(w.e, !w.negado));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public static boolean bruteAux(HashMap<Elemento, Boolean> forced,
			LinkedList<Clausula> l, ElementoClausula vct) throws Exception {

		List<Clausula> recovery = new LinkedList<Clausula>();

		forced.put(vct.e, vct.negado);

		Elemento v = vct.e;

		List<Clausula> isTrue = v.getElementos(vct.negado), isFalse = v
				.getElementos(!vct.negado);
		for (Clausula clausula : isTrue) { // elimina las clausulas que se
											// cumplen
			if (l.remove(clausula)) { // Si el elemento se elimina lo guardamos
										// en recovery
				recovery.add(clausula);
			}
		}

		for (Clausula clausula : isFalse) {
			if (l.contains(clausula)) {

				List<ElementoClausula> els = clausula.getElementos();
				switch (els.size()) {
				case 1: // Clausula con un unico elemento
						// La clausula no puede ser nunca true
					// Recupera estado anterior
					forced.remove(vct.e);
					l.addAll(recovery);
					return false;
				default: // Clausula con 0 o >2 elementos ERROR
					List<ElementoClausula> ws = clausula.distincts(vct);

					boolean forc=false;
					for (ElementoClausula w : ws) {
						if (!forced.containsKey(w.e) // no existe una regla que
														// impida
														// que se cumpla
								|| forced.get(w.e) == w.negado) { // la regla
																	// permite
																	// dar
																	// una
																	// salida
																	// true a la
																	// clausula
							l.remove(clausula); // El elemento se elimina
							recovery.add(clausula); // Lo guardamos en recovery

							if (!bruteAux(forced, l, w)) {
								l.add(clausula);
							} else {
								forc=true;
								break;
							}

						}
					}
					if(!forc){
						forced.remove(vct.e);
						l.addAll(recovery);
						return false;
					}

					break;
				}
			}

		}

		if (l.size() == 0) { // Fin del algoritmo
			return true;
		} else { // SAT reducido
			return brute(forced, l);
		}
	}

}
