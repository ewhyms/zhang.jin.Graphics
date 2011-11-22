package zhang.jin.graphics;

import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Demonstrate Bluetooth communication between two android devices
 */
public abstract class BluetoothActivity extends Activity {
	// the bluetooth adapter on the device
    private BluetoothAdapter bluetoothAdapter;
    
    // dialog constants for reporting bluetooth status
    private static final int DIALOG_NO_BLUETOOTH = 11;
    private static final int DIALOG_WE_HAVE_BLUETOOTH = 12;
    private static final int DIALOG_BLUETOOTH_ENABLED = 13;
    private static final int DIALOG_BLUETOOTH_ALREADY_ENABLED = 14;
    private static final int DIALOG_USER_IS_EVIL = 15;
    private static final int DIALOG_SENDING = 16;
    private static final int DIALOG_RECEIVING = 17;
    
    // constants used for startActivityForResult calls, so we know which call is responding
    private static final int BLUETOOTH_ENABLED = 21;
	private static final int SELECTING_DEVICE = 42;

	// constant to represent a message being passed from the server thread back to our GUI thread
    private static final int BLUETOOTH_MESSAGE = 98;

    // a UUID that represents this service
    // THIS MUST BE UNIQUE FOR THE SERVICE AND BOTH THE CLIENT AND SERVER NEED TO KNOW ITS VALUE
    private static final UUID uuid = UUID.fromString("bb37d560-09b3-11e1-be50-0800200c9a66");
    
    // the currently-selected target server device
	private BluetoothDevice selectedDevice;
	
	// the server thread that will listen for and accept incoming requests
	private ServerThread serverThread;
	
	// a textview that displays the started/stopped status of the server
	private TextView serverStatus;
	
	private String grid = "5,5";

	private String sendMessage = "";
	private String receiveMessage = "";
	
	/** Give subclasses access to the adapter */
    protected BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	
	/** Called when the activity is first created. */
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
        // find the adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        // if there is no adapter, show a dialog (note that our onCreateDialog will set this dialog up to finish() this activity)
        if (bluetoothAdapter == null)
        	showDialog(DIALOG_NO_BLUETOOTH);
        
        else {
//        	showDialog(DIALOG_WE_HAVE_BLUETOOTH); // CLASS DEMONSTRATION ONLY - DON'T REALLY DO THIS...
        }
        
        // if Bluetooth is not enabled, send a request
        // this request will start an activity that asks the user if it's ok to start bluetooth
        // when the result returns (with request code BLUETOOTH_ENABLED), we will call onBluetoothEnabled to continue our setup
        if (!bluetoothAdapter.isEnabled()) {
        	startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BLUETOOTH_ENABLED);
        } else {
//        	showDialog(DIALOG_BLUETOOTH_ALREADY_ENABLED);  // CLASS DEMONSTRATION ONLY - DON'T REALLY DO THIS
        	onBluetoothEnabled();
        }
        
        serverStatus = (TextView) findViewById(R.id.server_status);
    }
    
    /** called when the framework needs to create a dialog */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
			case DIALOG_NO_BLUETOOTH:
				return create("No bluetooth... sadness...", true);
			case DIALOG_WE_HAVE_BLUETOOTH:
				return create("We have bluetooth! Yippie!", false);
			case DIALOG_BLUETOOTH_ENABLED:
				return create("Bluetooth was enabled", false);
			case DIALOG_BLUETOOTH_ALREADY_ENABLED:
				return create("Bluetooth was already enabled", false);
			case DIALOG_USER_IS_EVIL:
				return create("User is evil and didn't enable bluetooth!", true);
			case DIALOG_SENDING:
				return create("Sending bluetooth message " + sendMessage, false);
			case DIALOG_RECEIVING:
				return create("Received bluetooth message " + receiveMessage, false);
		}
		return super.onCreateDialog(id, args);
	}

	/** our dialog creation helper method. quitAfter=true will cause the dialog to finish() this activity */ 
    private Dialog create(String message, final boolean quitAfter) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	return builder.setMessage(message)
    					.setPositiveButton("Ok", new OnClickListener() {
							@Override public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								if (quitAfter)
									finish();
							}})
							.create();
    }

    /** Helper method to continue initialization AFTER we know that bluetooth is enabled */
    protected void onBluetoothEnabled() {
    	
        
    }

    /** set up our menu */    
    @Override public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = new MenuInflater(this);
    	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    /**
     * A handler to manage communication between the GUI thread and server thread. This handler will be
     * initialized on the GUI thread (as an instance field) and will execute its actions on the GUI thread.
     * The server thread will post messages to it to; these messages are queued for it to handle when the GUI
     * thread is available.
     */
    private final Handler handler = new Handler() {
		@Override public void handleMessage(Message msg) {
			// the message data will be:
			//    msg.what - the message id - we always set it to BLUETOOTH_MESSAGE on the sending side
			//    msg.obj  - a byte buffer containing a string representation of the command we want to execute
			//    msg.arg1 - the length of the string in the byte buffer
			switch (msg.what) {
				case BLUETOOTH_MESSAGE:
					byte[] buffer = (byte[]) msg.obj;
					String content = new String(buffer, 0, msg.arg1);
					String[] eventList = content.split("|");
					try {
					if (eventList.length >= 2) {
						Event event = Event.valueOf(eventList[1]);
						Direction direction = null;
						if (eventList.length > 2)
							direction = Direction.valueOf(eventList[2]);
						changeState(event, direction);
					}
					}catch (Exception e) {
						receiveMessage = eventList[0] + " - " + eventList[1] + " - ";
						showDialog(DIALOG_RECEIVING);
						e.printStackTrace();
					}
//					Commands command = Commands.valueOf(content);
//					// based on the command, change the background color of the selected device text view
//					switch (command) {
//						case MakeBlue:
//							((TextView)findViewById(R.id.selected_device)).setBackgroundColor(Color.BLUE);
//							break;
//						case MakeGreen:
//							((TextView)findViewById(R.id.selected_device)).setBackgroundColor(Color.GREEN);
//							break;
//						case MakeRed:
//							((TextView)findViewById(R.id.selected_device)).setBackgroundColor(Color.RED);
//							break;
//					}
					break;
			}
		}
    };
    
    protected abstract void changeState(Event event, Direction direction);
    
    /** 
     * client-side helper method to send a request to its selected server device. this method is called when
     * one of the color buttons is pressed
     */
    protected void send(Event event, Direction direction) {
    	Toast.makeText(this, "selectedDevice: " + selectedDevice, Toast.LENGTH_SHORT);
    	if (selectedDevice == null)
    		return;
    	try {
    		BluetoothSocket socket = null;
    		try {
	    		// open a socket to the server
	    		// note that if these two devices have not yet been paired, the users of BOTH devices will be
	    		//   prompted to ok the pairing
				socket = selectedDevice.createRfcommSocketToServiceRecord(uuid);
				socket.connect();
				String output = grid + "|" + event.name();
				if (direction != null)
					output = output.concat("|" + direction.name());
				// write the name of the command to the stream
				socket.getOutputStream().write(output.getBytes());
				socket.getOutputStream().flush();
				Log.d("zhang.jin.is.awesome", "sending: " + event + " - " + direction);
				// wait for ack from the server so we know they received our data
				Toast.makeText(this, "sending: " + event + " - " + direction, Toast.LENGTH_SHORT);
				sendMessage = output;
				showDialog(DIALOG_SENDING);
				socket.getInputStream().read();
    		} finally {
    			if (socket != null)
    				socket.close();
    		}
		} catch (IOException e) {
			showDialog(DIALOG_USER_IS_EVIL);
			e.printStackTrace();
		}
    }
    
    /**
     * The server thread. This thread will listen for incoming requests and then transfer them to the GUI
     * thread for processing.
     */
    private class ServerThread extends Thread {
    	// the server socket that listens for requests
		private BluetoothServerSocket serverSocket;

		public ServerThread() {
			try {
				// start listening for requests to our UUID for the service
				serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Rube", uuid);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Our listening loop - infinite until an error occurs, including stopping the server by closing its socket.
		 *  I'm looking into better ways to do this so we can tell the difference between a real error and cancel()
		 */
		@Override public void run() {
			while(true) {
				try {
					byte[] buffer = new byte[128];
					int numRead = 0;
					BluetoothSocket socket = null;
					try {
						// accept the incoming client connection
						socket = serverSocket.accept();
						// read the data from the client
						// note that all of our data happens to fit in this buffer; longer data may require
						//   multiple reads to obtain all the data
						// you may want to send the length of the data first, followed by the data 
						numRead = socket.getInputStream().read(buffer);
						// send an ack to the client so they know it's ok to close
						socket.getOutputStream().write(1); // ack
						socket.getOutputStream().flush();
					} finally {
						if (socket != null)
							socket.close();
					}
					handler.obtainMessage(BLUETOOTH_MESSAGE, numRead, -1, buffer).sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}
		public void cancel() {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}
    }
    
    /** handle menu selection to turn the server on and off */
    @Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.pair_item:
    			startActivityForResult(new Intent(this, PairingActivity.class), SELECTING_DEVICE);
    			return true;
    		case R.id.start_server:
    			if (serverThread == null) {
    				serverThread = new ServerThread();
    				serverThread.start();
    				serverStatus.setText("Server running");
    			}
    			return true;
    		case R.id.stop_server:
    			if (serverThread != null) {
    				serverThread.cancel();
    				serverThread = null;
    				serverStatus.setText("Server NOT running");
    			}
    			return true;
    	}
    	return super.onMenuItemSelected(featureId, item);
    }
    
    /** be sure to stop the server thread on pause */
    @Override protected void onPause() {
    	if (serverThread != null) {
	    	serverThread.cancel();
	    	serverThread = null;
    	}
    	if (serverStatus != null)
    		serverStatus.setText("Server NOT running");
    	super.onPause();
    }

    /** handle results from startActivityForResult() calls */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// update our status for "enable bluetooth" requests
    	if (requestCode == BLUETOOTH_ENABLED) {
    		if (resultCode == RESULT_OK) {
    			onBluetoothEnabled(); // CONTINUE INITIALIZATION
//    			showDialog(DIALOG_BLUETOOTH_ENABLED); // ONLY USED FOR DEMONSTRATION IN CLASS - DO NOT REALLY DO THIS
    		} else {
    			showDialog(DIALOG_USER_IS_EVIL);
    		}
    	}
    	
    	// update selected device from our pairing activity
    	if (requestCode == SELECTING_DEVICE) {
    		if (resultCode == RESULT_OK) {
    			selectedDevice = data.getParcelableExtra("device");
    			((TextView)findViewById(R.id.selected_device)).setText(selectedDevice.getName());
    		} else {
    			((TextView)findViewById(R.id.selected_device)).setText("NO DEVICE SELECTED");
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}