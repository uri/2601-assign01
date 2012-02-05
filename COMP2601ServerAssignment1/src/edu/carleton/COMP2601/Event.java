package edu.carleton.COMP2601;

import java.util.HashMap;

public interface Event<E> {

	public String getType();
	
	public HashMap<String, E> getBody();
}
