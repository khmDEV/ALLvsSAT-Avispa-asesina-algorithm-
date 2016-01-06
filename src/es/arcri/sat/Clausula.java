package es.arcri.sat;
import java.util.LinkedList;
import java.util.List;


@SuppressWarnings("serial")
public class Clausula extends LinkedList<Literal>{

	public boolean add(Literal e){
		if (e.negado) {
			e.entrada.addClausulaFalse(this);
		}else{
			e.entrada.addClausulaTrue(this);
		}
		return super.add(e);
	}
	
	public List<Literal> distincts(Literal e){
		List<Literal> els=new LinkedList<Literal>();
		for (Literal el : this) {
			if(el.entrada!=e.entrada||el.negado!=e.negado){
				els.add(el);
			}
		}
		return els;
	}
	@Override
	public String toString(){
		String s="(";
		for (int i = 0; i < size(); i++) {
			s+=get(i);
			if (size()>(i+1)) {
				s+=" + ";
			}
		}
		return s+")";
	}

	public Literal distinct(Entrada e, boolean entrada) {
		for (Literal el : this) {
			if(el.entrada!=e||el.negado!=entrada){
				return el;
			}
		}
		return null;	
	}

	public Literal distinct(Literal e) {
		for (Literal el : this) {
			if(el.entrada!=e.entrada||el.negado!=e.negado){
				return el;
			}
		}
		return null;	
	}

	public List<Entrada> positive() {
		List<Entrada> elems=new LinkedList<Entrada>();
		for (Literal ec : this) {
			if (!ec.negado) {
				elems.add(ec.entrada);
			}
		}
		return elems;
	}

	public List<Literal> distincts(Entrada v, boolean entrada) {
		List<Literal> els=new LinkedList<Literal>();
		for (Literal el : this) {
			if(el.entrada!=v||el.negado!=entrada){
				els.add(el);
			}
		}
		return els;
	}
}
