package edu.carleton.COMP2601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.uri.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Assignment1Activity extends Activity implements Runnable {
	/** Called when the activity is first created. */

	Message m;
	
	private ObjectOutputStream dos;
	private ObjectInputStream dis;
	private Socket client;
	private InetSocketAddress address;
	private Assignment1Activity parent;
	
	private String ipAddress = "172.17.144.45";
	private int portNum = 7001;
	
	private Handler handler = new Handler();
	private Toast toast;
	
	private Button b;
	
	private int buttonState;
	private EditText ipField;
	private EditText portField;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		buttonState = 0;

		ipField = (EditText)findViewById(R.id.ipAddressField);
		portField = (EditText)findViewById(R.id.portField);
		
		ipField.setText("" +ipAddress);
		portField.setText("" +portNum);
		
		// Start the activity
		b = (Button) findViewById(R.id.buttonStart);
		parent = this;
		
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if (buttonState == 0) {
					// Change the message
					b.setText("Send Message");
					buttonState++;
					
					new Thread(new Runnable() {
						public void run() {
							// Connect the socket
							try {
								client = new Socket(ipAddress, portNum);
							} catch (UnknownHostException e) {
							} catch (IOException e) {
							}
						}
					}).start();
				} else {
					// Send the message
					new Thread(parent).start();	
				}
				
				
			}
		});
	}

	public void run() {
		
		try {
			
			// Check if the socket is connected
			if (client.isConnected()) {
				dos = new ObjectOutputStream(client.getOutputStream());
				dis = new ObjectInputStream(client.getInputStream());
				
				// Send a message
				dos.writeObject(new Message(Message.REQ_LIST_FILES, null));
				
				// Now we read a message
				Message reply = null;
				try {
					reply = (Message)dis.readObject();
				} catch (ClassNotFoundException e) {
				}
				
				if (reply != null) {
					toastMessage("The reply is" + reply.getType());
				}
//				toastMessage("The date is " + dis.readUTF());
				
				
					
			} else {
				toastMessage("Could not connect");
			}
			
			
		} catch (IOException e) {
			// Something has happened
			e.printStackTrace();
			toastMessage("Error.");
			Log.i("urinet",  "ERROR");
		}
	}
	
	public void toastMessage(final CharSequence message) {
		handler.post(new Runnable() {
			public void run() {
				Context context = getApplicationContext();
				CharSequence text = message;
				int duration = Toast.LENGTH_SHORT;

				toast = Toast.makeText(context, text,
						duration);
				toast.show();
			}
		});
	}
}