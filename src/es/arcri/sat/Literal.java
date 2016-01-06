package es.arcri.sat;

public class Literal {
	public final Entrada entrada;
	public final boolean negado;
	public Literal(Entrada e,boolean negado){
		this.entrada=e;
		this.negado=negado;
	}
	@Override
	public String toString(){
		
		return (!negado?"":"-")+entrada;
	}
}
