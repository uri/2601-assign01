package edu.carleton.COMP2601;


import java.io.Serializable;
import java.util.HashMap;



public class Message<E> implements Serializable, Event{
	private String type;
	private HashMap<String, E> body;
	private Object valueType;
	
	public static final String REQ_LIST_FILES= "list_file_req";
	public static final String REPLY_LIST_FILES= "list_file_reply";
	
	public Message(String type, HashMap<String, E> map) {
		this.type = type;
		this.body = map;
		valueType = (E)new Object();
	}
	
	
	public String getType() {
		return type;
	}


	public HashMap<String, E> getBody() {
		return body;
	}
	

}
