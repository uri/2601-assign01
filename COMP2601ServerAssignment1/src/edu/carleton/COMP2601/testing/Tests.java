package edu.carleton.COMP2601.testing;

import java.util.HashMap;

import edu.carleton.COMP2601.Message;

public class Tests {

	public static void main(String[] args) {
		
		Message m = null;
		
		String body = "Uri is the best";
		String type = "req_type";
		HashMap<String, String> map;
		
		
		
		map = new HashMap<String, String>();
		map.put("list", "a b c");
		map.put("value", "sexy");
		
		m = new Message<String>(body, map);
		
//		System.out.println("Get the type of the body= " + (m.getValueType() instanceof String));
	}

}
