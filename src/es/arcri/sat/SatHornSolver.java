package es.arcri.sat;

import java.util.LinkedList;
import java.util.List;

import es.arcri.sat.exceptions.IsNotHorn;

public class SatHornSolver {

	public static void main(String[] args) {
		Entrada a = new Entrada(), b = new Entrada(), c = new Entrada();
		Clausula c1 = new Clausula();
		c1.add(new Literal(a, false));
		c1.add(new Literal(a, true));

		Clausula c2 = new Clausula();
		c2.add(new Literal(a, true));
		c2.add(new Literal(a, false));

		Clausula c3 = new Clausula();
		c3.add(new Literal(a, false));

		Clausula c4 = new Clausula();
		c4.add(new Literal(b, true));
		c4.add(new Literal(c, false));

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
			System.out.println(UnitPropagation(problema));
		} catch (IsNotHorn e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Lo suponemos bien formado para la primera iteracion
	public static boolean UnitPropagation(List<Clausula> problema)
			throws IsNotHorn {
		boolean unitClause = false;
		while (problema.size()!=0 && problema.get(0).size()==0) {
			problema.remove(0);
		}
		if (problema.size()==0) {
			return true;
		}

		// If G contains a unit clause (L), delete every occurrence of L from
		// clauses
		// of G, and delete every clause containing L from G.
		for (int i = 0; i < problema.size() && !unitClause; i++) {
			Clausula clausulaAuxiliar = problema.get(i);
			if (clausulaAuxiliar.size() == 1) {
				unitClause = true;
				Literal elementoAuxiliar = clausulaAuxiliar.get(0); // Elemento unit Clause
				List<Clausula> isFalse = !elementoAuxiliar.negado ? elementoAuxiliar.entrada.clausulasNegadas
						: elementoAuxiliar.entrada.clausulasSinNegar;
				List<Clausula> isTrue = elementoAuxiliar.negado ? elementoAuxiliar.entrada.clausulasNegadas
						: elementoAuxiliar.entrada.clausulasSinNegar;
				for (Clausula cn : isFalse) {
					List<Literal> ecs = new LinkedList<Literal>();
					for (Literal ec : cn) {
						if (ec.entrada == elementoAuxiliar.entrada
								&& elementoAuxiliar.negado == ec.negado) {
							ecs.add(ec);
						}
					}
					// La clausula solo contiene uno o varias entradas de
					// elementoAuxiliar que no pueden tener una salida positiva
					if (ecs.size() == cn.size()) {
						if(cn.size()==0){
							problema.remove(cn);
						}
						return false;
					}
					for (Literal tr : ecs) {
						cn.remove(tr);
					}
					if(cn.size()==0){
						problema.remove(cn);
					}
				}
				for (int z = 0; z < isTrue.size(); z++) { // delete every clause
															// containing L from
															// G.
					problema.remove(isTrue.get(z));
				}
			}
		}
		if (!unitClause) {
			throw new IsNotHorn(problema);
		} else {
			return UnitPropagation(problema);
		}

	}
}