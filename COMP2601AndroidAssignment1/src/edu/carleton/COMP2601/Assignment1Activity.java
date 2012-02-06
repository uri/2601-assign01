package edu.carleton.COMP2601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

	public static ObjectOutputStream dos;
	public static ObjectInputStream dis;
	private Socket client;
	private InetSocketAddress address;
	private Assignment1Activity parent;
	private ClientState state;

	private int portNum = 7001;

	private Handler handler = new Handler();
	private Toast toast;

	private Button b;

	private EditText ipField;
	private EditText portField;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		state = ClientState.NOT_CONNECTED;

		ipField = (EditText) findViewById(R.id.ipAddressField);
		portField = (EditText) findViewById(R.id.portField);

		ipField.setText("" + Common.IP_ADDRESS);
		portField.setText("" + portNum);

		// Start the activity
		b = (Button) findViewById(R.id.buttonStart);
		parent = this;

		// Create a listener
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread(parent).start();
			}
		});
	}

	public void run() {

		try {

			// Check if the socket is connected

			if (state == ClientState.NOT_CONNECTED) {
				client = new Socket(Common.IP_ADDRESS, portNum);
				if (client.isConnected()) {
					state = ClientState.CONNECTED;
					handler.post(new Runnable() {

						public void run() {
							b.setText("Login");
						}
					});

				}

			} else if (state == ClientState.CONNECTED) {
				dos = new ObjectOutputStream(client.getOutputStream());
				dis = new ObjectInputStream(client.getInputStream());

				// Send a message
				dos.writeObject(new Message(Message.REQ_LOGIN, null));

				// Now we read a message
				getReply(Message.REPLY_LOGIN, ClientState.LOGGED_IN);
				handler.post(new Runnable() {

					public void run() {
						b.setText("Grab File List");
					}
				});
				

			} else if (state == ClientState.LOGGED_IN) {
				// We are logged in, we would like to get the list of files
				// Send the request files message
				dos.writeObject(new Message(Message.REQ_LIST_FILES, null));
				toastMessage("Sent" + Message.REQ_LIST_FILES);

				// Get the reply
				Message reply = getReply(Message.REPLY_LIST_FILES, ClientState.VIEWING_FILES);
				if (reply != null) {
					Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
					intent.putExtra("fileList", (ArrayList<String>)reply.getBody().get(Message.KEY_FILE_LIST));
					intent.putExtra("state", state);
					startActivity(intent);
				}

			} else {
				// toastMessage("The date is " + dis.readUTF());
				toastMessage("Could not connect");
			}

		} catch (IOException e) {
			// Something has happened
			e.printStackTrace();
			toastMessage("Error.");
			Log.i("urinet", "ERROR");
		} catch (ClassNotFoundException e) {

		}
	}

	private Message getReply(String expectedReply, ClientState newState)
			throws OptionalDataException, ClassNotFoundException, IOException {
		Message reply = (Message) dis.readObject();

		if (reply != null) {
			toastMessage("The reply is" + reply.getType());

			if (reply.getType().equals(expectedReply)) {
				// Start a new activty with the list file view
				state = newState;
				return reply;
			}
		}

		return null;
	}

	public void toastMessage(final CharSequence message) {
		handler.post(new Runnable() {
			public void run() {
				Context context = getApplicationContext();
				CharSequence text = message;
				int duration = Toast.LENGTH_SHORT;

				toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
	}
}