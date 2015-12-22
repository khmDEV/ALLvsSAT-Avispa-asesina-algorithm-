package es.arcri.sat;

public class ElementoClausula {
	public final Elemento e;
	public final boolean negado;
	public ElementoClausula(Elemento e,boolean negado){
		this.e=e;
		this.negado=negado;
	}
	@Override
	public String toString(){
		
		return (!negado?"":"!")+e;
	}
}
