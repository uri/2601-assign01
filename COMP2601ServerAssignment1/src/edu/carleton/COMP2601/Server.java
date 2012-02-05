package edu.carleton.COMP2601;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server implements Runnable, Reactor {

	private boolean serverRunning;
	private HashMap<String, EventHandler> handlers;
	
	ObjectInputStream dis;
	ObjectOutputStream dos;

	public static void main(String[] args) {
		Server ns = new Server();

		System.out.println("Launching Server.");
		ns.run();
	}

	public Server() {
		serverRunning = false;
		handlers = new HashMap<String, EventHandler>();

		// REQ_LOGIN
		registerHandler(Message.REQ_LOGIN, new EventHandler() {
			public void handleEvent(Event e) {
				// Someone is trying to log into the server
				// Send a login reply message
				try {
					dos.writeObject(new Message(Message.REPLY_LOGIN, null));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});

		// REQ_LIST_FILES
		registerHandler(Message.REQ_LIST_FILES, new EventHandler() {
			public void handleEvent(Event e) {
				// The user has requested the list of files...
				File fileDirectory = new File("files");
				
				String directories[] = fileDirectory.list();
				ArrayList<String> files = new ArrayList<String>();
				if (directories != null) {
					for (String s : fileDirectory.list()) {
						System.out.println("This is the name of the file: " + s);
						files.add(s);
					}
					
					// Send the message
					HashMap<String, Object> body = new HashMap<String, Object>();
					body.put(Message.KEY_FILE_LIST, files);
					
					try {
						// Send the response
						dos.writeObject(new Message(Message.REPLY_LIST_FILES, body));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println("Error file directory not found.");
				}
				
			}
		});

		// REQ_FILE
		registerHandler(Message.REQ_FILE, new EventHandler() {
			public void handleEvent(Event e) {
				// The user has requested the list of files...
			}
		});
	}

	/**
	 * Runnable Interface
	 */
	public void run() {
		ServerSocket listener;

		try {

			// Listen on a port
			listener = new ServerSocket(Common.PORT);
			// Connect on a scket
			
			System.out.println("Waiting for socket connection...");
			Socket s = listener.accept();
			System.out.println("Found client.");

			// Set serverRunning to true if we have connected
			if (s.isConnected()) {
				serverRunning = true;
				InputStream is = s.getInputStream();
				OutputStream os = s.getOutputStream();
				dis = new ObjectInputStream(is);
				dos = new ObjectOutputStream(os);
				
				System.out.println("Connection established.");
			} else {
				System.out.println("Not connected.");
			}

			while (serverRunning) {


				System.out.println("Runing Service...");
				handleEvents();
			}

			// Close the connection once we are done
			// TODO will this throw an error if we are not connected
			s.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * The Service to be performed while the server is mainting a connection
	 * 
	 * @param is
	 * @param os
	 * @throws HeadlessException
	 * @throws ClassNotFoundException
	 */
	public void handleEvents()
			throws HeadlessException {

		try {

		
			// Read
			Message m = (Message) dis.readObject();

			// Dispatch the method
			EventHandler h = handlers.get(m.getType());

			// If we have an event handler for the message, call it
			if (h != null) {
				h.handleEvent(m);
			}


			System.out.println("Received: message" + m.getType());

		} catch (UnknownHostException eh) {

			System.out.println("Uknown host");

		} catch (IOException eio) {
			System.out.println("IO Exceptions");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Add a handler
	 */
	public void registerHandler(String s, EventHandler ev) {
		handlers.put(s, ev);
	}

	/**
	 * Stop the server
	 */
	public void stop() {
		serverRunning = false;
	}

	public HashMap<String, EventHandler> getHandlers() {
		return handlers;
	}

	@Override
	public void waitForEvents() {
		// TODO Auto-generated method stub

	}



}
