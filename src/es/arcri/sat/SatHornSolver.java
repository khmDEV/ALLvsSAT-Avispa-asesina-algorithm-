package es.arcri.sat;

import java.util.List;

public class SatHornSolver {
	
	
	//Lo suponemos bien formado para la primera iteracion
	public boolean UnitPropagation(List<Clausula> problema) {
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