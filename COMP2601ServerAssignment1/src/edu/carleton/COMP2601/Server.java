package edu.carleton.COMP2601;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;


public class Server implements Runnable, Reactor {

	private boolean serverRunning;
	
	private HashMap<String, EventHandler> handlers;

	public static void main(String[] args) {
		Server ns = new Server();

		System.out.println("Launching Server.");
		ns.run();
	}

	public Server() {
		serverRunning = false;
		
		// REQ_LIST_FILES
		registerHandler(Message.REQ_LIST_FILES, new EventHandler() {
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
			Socket s = listener.accept();

			// Set serverRunning to true if we have connected
			if (s.isConnected())
				serverRunning = true;

			while (serverRunning) {

				InputStream is = s.getInputStream();
				OutputStream os = s.getOutputStream();
				System.out.println("Runing Service...");
				if (s.isConnected()) {
					service(is, os);
				} else {
					System.out.println("Not connected.");
				}
			}
			
			// Close the connection once we are done
			// TODO will this throw an error if we are not connected 
			s.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
	public void service(InputStream is, OutputStream os)
			throws HeadlessException, ClassNotFoundException {

		try {
			ObjectInputStream dis = new ObjectInputStream(is);
			ObjectOutputStream dos = new ObjectOutputStream(os);

			System.out.println("Reading...");
			System.out.println("Available: " + dis.available());

			// Read
			Message m = (Message) dis.readObject();

			// Show that we read
			// JOptionPane.showMessageDialog(null,
			// "Received: "+ message);

			System.out.println("Received: message" + m.getType());

			dos.writeObject(new Message(Message.REPLY_LIST_FILES, null));

			// Now we send back the formatted time
			// Date date = new Date(longtime);
			// String dateMessage = DateFormat.getInstance().format(date);
			// dos.writeUTF(dateMessage);

		} catch (UnknownHostException eh) {

			System.out.println("Uknown host");

		} catch (IOException eio) {
			System.out.println("IO Exceptions");
		}

	}


	/** Add a handler
	 */
	public void registerHandler(String s, EventHandler ev) {
		handlers.put(s, ev);
	}





	/** Stop the server
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

	@Override
	public void handleEvents() {
		// TODO Auto-generated method stub
		
	}

}
