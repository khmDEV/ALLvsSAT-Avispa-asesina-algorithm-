package es.arcri.sat;
import java.util.LinkedList;
import java.util.List;


public class Clausula {
	private List<ElementoClausula> elementos=new LinkedList<ElementoClausula>();
	
	public List<ElementoClausula> getElementos(){
		return elementos;
	}
	
	public void addElementoClausula(ElementoClausula e){
		elementos.add(e);
		if (e.negado) {
			e.e.addClausulaFalse(this);
		}else{
			e.e.addClausulaTrue(this);
		}
	}
	
	public List<ElementoClausula> distincts(ElementoClausula e){
		List<ElementoClausula> els=new LinkedList<ElementoClausula>();
		for (ElementoClausula el : elementos) {
			if(el.e!=e.e||el.negado!=e.negado){
				els.add(el);
			}
		}
		return els;
	}
	@Override
	public String toString(){
		String s="(";
		for (int i = 0; i < elementos.size(); i++) {
			s+=elementos.get(i);
			if (elementos.size()>(i+1)) {
				s+=" + ";
			}
		}
		return s+")";
	}

	public int size() {
		return elementos.size();
	}

	public ElementoClausula distinct(ElementoClausula e) {
		for (ElementoClausula el : elementos) {
			if(el.e!=e.e||el.negado!=e.negado){
				return el;
			}
		}
		return null;	
	}
}
