package es.arcri.sat.exceptions;

@SuppressWarnings("serial")
public class InvalidId extends Exception{
	
	public InvalidId(String v){
		super(v+" is a ID invalid. Correct format: [A-Za-z][A-Za-z1-9_]*");
	}

}
