package edu.carleton.COMP2601;


import java.io.Serializable;
import java.util.HashMap;



public class Message implements Serializable, Event{
	private String type;
	private HashMap<String, Object> body;
	
	
	public static final String REQ_LIST_FILES= "list_file_req";
	public static final String REPLY_LIST_FILES= "list_file_reply";
	
	public static final String REQ_LOGIN= "login_req";
	public static final String REPLY_LOGIN= "login_reply";
	
	public static final String REQ_FILE= "file_req";
	public static final String REPLY_FILE= "file_reply";
	
	public static final String KEY_FILE_LIST = "key_file_list";
	public static final String KEY_FILE = "key_file";
	
	public Message(String type, HashMap<String, Object> body) {
		this.type = type;
		this.body = body;

	}
	
	
	public String getType() {
		return type;
	}


	public HashMap<String, Object> getBody() {
		return body;
	}


	

}
