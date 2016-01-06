package es.arcri.sat.exceptions;

import es.arcri.sat.Clausula;

@SuppressWarnings("serial")
public class IsNotASAT2 extends Exception{
	
	public IsNotASAT2(Clausula c){
		super(c+" is not a SAT2 clausule");
	}

}
