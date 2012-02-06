package edu.carleton.COMP2601;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class MessageTesting {


	
	@Test
	public void testGetType() {
		String type = "uri";
		Message m = new Message(type, null);
		assertEquals(type, m.getType());
	}
	
	
	@Test
	public void testGetBody() {
		String type = "uri";
		HashMap<String, Object> body = new HashMap<String, Object>();
		body.put("test", "test");
		
		
		Message m = new Message(type, body);
		
		
		assertEquals(body, m.getBody());
	}

}
