package zhang.jin.graphics;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity to select a target server device, see which devices we're paired with, and make this device discoverable
 * Note that I forgot to demonstrate in class how to make a device discoverable...
 */
public class PairingActivity extends BluetoothActivity {
	// devices to show in our listview
	private List<BluetoothDeviceWrapper> wrappers = new ArrayList<PairingActivity.BluetoothDeviceWrapper>();
	
	// a text view to show our status: discoverable, connectable, etc
	private TextView scanModeView;
	
	// the data model for the list view
	private BluetoothDeviceAdapter bluetoothDeviceAdapter;
	
	// the list view
	private ListView list;

	
	/**
	 * A wrapper class that holds a device and its selection status for us to display in a list
	 */
    private static class BluetoothDeviceWrapper {
    	// generate a unique id for each device (makes the listview happy) 
    	private static int nextId = 0;
    	private int id = nextId++;
    	private BluetoothDevice bluetoothDevice;
    	private boolean selected;
    	public BluetoothDeviceWrapper(BluetoothDevice bluetoothDevice) {
    		this.bluetoothDevice = bluetoothDevice;
    	}
		public BluetoothDevice getBluetoothDevice() {
			return bluetoothDevice;
		}
		public int getId() {
			return id;
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
    }

    /** list adapter that displays the device information */
	private class BluetoothDeviceAdapter implements ListAdapter {
		// list of data set observers to notify when the list data changes
		private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();

		/** how many devices do we have? */
    	@Override public int getCount() {
			return wrappers.size();
		}

    	/** return the wrapped bluetooth device for this position */
		@Override public Object getItem(int position) {
			return wrappers.get(position);
		}

		/** return the id of the wrapped bluetooth device for this position */
		@Override public long getItemId(int position) {
			return wrappers.get(position).getId();
		}

		/** return which view we want to use to display the device - all list entries use the same id */
		@Override public int getItemViewType(int position) {
			return 0;
		}

		/** get (or fill in an existing) view to display the wrapper at the specified position */
		@Override public View getView(int position, View view, ViewGroup group) {
			if (view == null)
				view = LayoutInflater.from(PairingActivity.this).inflate(R.layout.device_item, null);
			BluetoothDeviceWrapper wrapper = wrappers.get(position);
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(wrapper.getBluetoothDevice().getName());
			TextView mac = (TextView) view.findViewById(R.id.mac);
			mac.setText(wrapper.getBluetoothDevice().getAddress());
			TextView status = (TextView) view.findViewById(R.id.status);
			if (wrapper.isSelected()) {
				name.setBackgroundColor(Color.YELLOW);
				mac.setBackgroundColor(Color.YELLOW);
				status.setBackgroundColor(Color.YELLOW);
			} else {
				name.setBackgroundColor(Color.WHITE);
				mac.setBackgroundColor(Color.WHITE);
				status.setBackgroundColor(Color.WHITE);
			}
			switch (wrapper.getBluetoothDevice().getBondState()) {
				case BluetoothDevice.BOND_BONDED:
					status.setText("BONDED");
					break;
				case BluetoothDevice.BOND_BONDING:
					status.setText("BONDING");
					break;
				case BluetoothDevice.BOND_NONE:
					status.setText("NOT BONDED");
					break;
			}
			return view;
		}

		/** we use the same view type for all entries in the list */
		@Override public int getViewTypeCount() {
			return 1;
		}

		/** all entries have consistent ids */
		@Override public boolean hasStableIds() {
			return true;
		}

		/** tell if the list is empty */
		@Override public boolean isEmpty() {
			return wrappers.isEmpty();
		}

		/** all items are always enabled */
		@Override public boolean areAllItemsEnabled() {
			return true;
		}

		/** all items are always enabled */
		@Override public boolean isEnabled(int position) {
			return true;
		}
		
		/** report that something in the list has changed so it will be redrawn */
		private void fireChange() {
			for (DataSetObserver observer : observers) {
				observer.onChanged();
			}
		}
		
		/** add an observer */
		@Override public void registerDataSetObserver(DataSetObserver observer) {
			observers.add(observer);
		}
		/** remove an observer */
		@Override public void unregisterDataSetObserver(DataSetObserver observer) {
			observers.remove(observer);
		}
		
		/** add a new wrapper to the list */
		public void add(BluetoothDeviceWrapper wrapper) {
			wrappers.add(wrapper);
			fireChange();
		}
    }

	// ******************************************
	// NOTE: I MISSED TALKING ABOUT THIS IN CLASS
	// ******************************************
	// The following code allows us to make the device discoverable
	// We add two new menu items, one to start and one to stop being discoverable
	// Discoverable time currently caps at 300 seconds; any higher value will be reduced to 300
	// There is no "stop" in the API, so the best we can do is ask for 1 second discoverable time
    @Override public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = new MenuInflater(this);
    	inflater.inflate(R.menu.pairing_menu, menu);
    	return true;
    }

    @Override public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	if (item.getItemId() == R.id.make_discoverable) {
    		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); // infinite
			startActivity(intent);
			return true;
    	} else if (item.getItemId() == R.id.stop_discoverable) {
    		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1); // very short to turn it off
    		startActivity(intent);
    		return true;
    	}
    	return super.onMenuItemSelected(featureId, item);
    }

    
    /** finish initializing after we know that bluetooth is available */
    protected void onBluetoothEnabled() {
        setContentView(R.layout.pairing);
        scanModeView = (TextView) findViewById(R.id.scan_mode);
        setScanModeView(getBluetoothAdapter().getScanMode());
        
        // set up the list
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> list, View view, int position, long id) {
				for (BluetoothDeviceWrapper wrapper : wrappers) {
					wrapper.setSelected(false);
				}
				wrappers.get(position).setSelected(true);
				bluetoothDeviceAdapter.fireChange();
				getIntent().putExtra("device", wrappers.get(position).getBluetoothDevice());
				setResult(RESULT_OK, getIntent());
			}});

        bluetoothDeviceAdapter = new BluetoothDeviceAdapter();
		list.setAdapter(bluetoothDeviceAdapter);

        // look up devices
        for (BluetoothDevice device : getBluetoothAdapter().getBondedDevices()) {
        	bluetoothDeviceAdapter.add(new BluetoothDeviceWrapper(device));
		}
        
        // start discovering other devices. the broadcast receiver below will be notified when 
        //   devices are found
        getBluetoothAdapter().startDiscovery();
    }

    /** a broadcast receiver that is notified when devices are found or the discoverable state of this device has changed */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override public void onReceive(Context context, Intent intent) {
			if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device != null) {
					bluetoothDeviceAdapter.add(new BluetoothDeviceWrapper(device));
				}
			// SAS - FORGOT TO TALK ABOUT MAKING DEVICES DISCOVERABLE!!!
			} else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(intent.getAction())) {
				int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
				setScanModeView(scanMode);
			}
		}};
	@Override protected void onStart() {
		super.onStart();
		registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
	}
    @Override protected void onDestroy() {
    	unregisterReceiver(broadcastReceiver);
    	super.onDestroy();
    }
    
    // SAS MAKING DEVICES DISCOVERABLE
	private void setScanModeView(int scanMode) {
		switch (scanMode) {
			case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
				scanModeView.setText("connectable");
				return;
			case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
				scanModeView.setText("discoverable");
				return;
			case BluetoothAdapter.SCAN_MODE_NONE:
				scanModeView.setText("not connectable");
				return;
		}
	}
	
	// when leaving this activity, we cancel discovery (it eats the battery alive)
	@Override protected void onPause() {
		if (getBluetoothAdapter().isDiscovering())
			getBluetoothAdapter().cancelDiscovery();
		super.onPause();
	}

	@Override
	protected void changeState(Event event, Direction direction) {
		// TODO Auto-generated method stub
		
	}

}