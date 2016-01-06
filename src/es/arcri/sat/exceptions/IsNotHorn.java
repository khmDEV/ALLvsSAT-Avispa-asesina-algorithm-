package es.arcri.sat.exceptions;

import java.util.List;

import es.arcri.sat.Clausula;

@SuppressWarnings("serial")
public class IsNotHorn extends Exception{
	
	public IsNotHorn(List<Clausula> c){
		super(c+" is not a Horn problem");
	}

}
