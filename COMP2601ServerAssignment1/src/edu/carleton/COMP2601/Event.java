package edu.carleton.COMP2601;

import java.util.HashMap;

public interface Event {

	public String getType();
	
	public HashMap<String, Object> getBody();
}
