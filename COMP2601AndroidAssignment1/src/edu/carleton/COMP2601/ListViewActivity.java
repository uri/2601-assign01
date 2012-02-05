package edu.carleton.COMP2601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewActivity extends ListActivity implements Runnable {

	private ArrayList<String> fileList;
	
	private ObjectInputStream dis;
	private ObjectOutputStream dos;
	
	private int position;
	
	private ClientState state;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Bundle extras = getIntent().getExtras();
	    
	    // Get the input streams
	    if (extras != null) {
	    	fileList = (ArrayList<String>)extras.getSerializable("fileList");
	    	dis = (ObjectInputStream)extras.getSerializable("outputStream");
	    	dos = (ObjectOutputStream)extras.getSerializable("inputStream");
	    	state = (ClientState)extras.getSerializable("state");
	    }
	    
	    CustomListViewAdapter adapter = new CustomListViewAdapter(this, fileList);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
		
		this.position = position;
		new Thread(this).start();
		

		
	}

	@Override
	public void run() {

		// Send the request for a file
		if(state != ClientState.WAITING_FOR_FILE) {
			HashMap<String, Object> body = new HashMap<String, Object>();
			body.put(Message.KEY_FILE, fileList.get(position));
			try {
				dos.writeObject(new Message(Message.REQ_FILE,body));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} else {
			
		}
	
		
		// Listen for a response
	}
	

}
