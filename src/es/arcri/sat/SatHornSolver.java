package es.arcri.sat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SatHornSolver {
	
	public static void main(String[] args) {
		Elemento a = new Elemento(), b = new Elemento(), c = new Elemento(), d = new Elemento();
		Clausula c1 = new Clausula();
		c1.addElementoClausula(new ElementoClausula(a, false));
		c1.addElementoClausula(new ElementoClausula(a, false));

		Clausula c2 = new Clausula();
		c2.addElementoClausula(new ElementoClausula(a, true));
		c2.addElementoClausula(new ElementoClausula(a, true));

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

		System.out.println(UnitPropagation(list));
	}
	
	
	//Lo suponemos bien formado para la primera iteracion
	public static boolean UnitPropagation(List<Clausula> problema) {
		boolean unitClause = false;
		
		if (problema.size()  == 0){
			return true;
		}
	
	//If G contains a unit clause (L), delete every occurrence of L from clauses
	//of G, and delete every clause containing L from G.
		int i = 0;
		while(i < problema.size() && !unitClause){
			Clausula clausulaAuxiliar = problema.get(i);
			if(clausulaAuxiliar.size()==1){
				if(!clausulaAuxiliar.getElementos().get(0).negado){
					unitClause = true;
					Elemento elementoAuxiliar = clausulaAuxiliar.getElementos().get(0).e; //Elemento unit Clause
					
					for(int z=0; z < elementoAuxiliar.clausulasFalse.size();z++){ //delete every occurrence of L from clauses of G,			
						elementoAuxiliar.clausulasFalse.get(z).getElementos().remove(elementoAuxiliar);
						if(elementoAuxiliar.clausulasFalse.get(z).size()==0){
							problema.remove(elementoAuxiliar.clausulasTrue.get(z));
						}
					}
					for(int z=0; z < elementoAuxiliar.clausulasTrue.size();z++){ //delete every clause containing L from G.
						problema.remove(elementoAuxiliar.clausulasTrue.get(z));
					}
				}
			}
		}
		if (!unitClause){
			return false;
		}else{
			return UnitPropagation(problema);
		}
		
		
		
		
	
	}
}