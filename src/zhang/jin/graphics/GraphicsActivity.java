 package zhang.jin.graphics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import zhang.jin.graphics.R;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Jin F. Zhang
 *
 * Graphics activity class that handles button presses on the main view.
 */
public class GraphicsActivity extends BluetoothActivity implements PropertyChangeListener {
	
	// Enumeration type for the object to display
	private enum ObjectType {
		ChristmasTree,
		ClappingMonkey,
		Pinwheel,
		Candle,
		Clapper,
		Cleat,
		ElectricOutlet,
		Kettle,
		RopeDownRight,
		RopeLeftRight,
		RopeUpDown,
		RopeUpRight,
		RubberBand,
		TV,
		WaterCooler,
		WireLeftRight,
		WireUpDown
	}
	
	// The current object view
	private StateObjectView currentView = null;
	
	// Button click listener that calls changeState
	private OnClickListener buttonListener = new OnClickListener() {

		@Override
		public void onClick(View button) {
			Toast.makeText(GraphicsActivity.this, "button clicked: ", Toast.LENGTH_SHORT);
			// call changeState with event based on the button that was pressed
			switch (button.getId()) {
			case R.id.alexButton:
				send(Event.Alex, Direction.All);
				break;
			case R.id.electricOnButton:
				send(Event.ElectricOn, Direction.All);
				break;
			case R.id.electricOffButton:
				send(Event.ElectricOff, Direction.All);
				break;
			case R.id.heatButton:
				send(Event.Heat, Direction.Up);
				break;
			case R.id.pulseButton:
				send(Event.Pulse, Direction.All);
				break;
			case R.id.steamButton:
				send(Event.Steam, Direction.All);
				break;
			case R.id.waterButton:
				send(Event.Water, Direction.Down);
				break;
			case R.id.clapButton:
				send(Event.Clap, Direction.All);
				break;
			case R.id.pullButton:
				send(Event.Pull, Direction.All);
				break;
			case R.id.releaseButton:
				send(Event.Release, Direction.All);
				break;
			case R.id.startButton:
				send(Event.Start, Direction.All);
				break;
			case R.id.turnButton:
				send(Event.Turn, Direction.All);
				break;
			}
		}

	};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.gadgets);
        
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "starting app", Toast.LENGTH_SHORT);
        // set up all the buttons to use the same button click listener
		Button alexButton = (Button)(findViewById(R.id.alexButton));
		alexButton.setOnClickListener(buttonListener);
		Button electricOnButton = (Button)(findViewById(R.id.electricOnButton));
		electricOnButton.setOnClickListener(buttonListener);
		Button electricOffButton = (Button)(findViewById(R.id.electricOffButton));
		electricOffButton.setOnClickListener(buttonListener);
		Button heatButton = (Button)(findViewById(R.id.heatButton));
		heatButton.setOnClickListener(buttonListener);
		Button pulseButton = (Button)(findViewById(R.id.pulseButton));
		pulseButton.setOnClickListener(buttonListener);
		Button steamButton = (Button)(findViewById(R.id.steamButton));
		steamButton.setOnClickListener(buttonListener);
		Button waterButton = (Button)(findViewById(R.id.waterButton));
		waterButton.setOnClickListener(buttonListener);
		Button clapButton = (Button)(findViewById(R.id.clapButton));
		clapButton.setOnClickListener(buttonListener);
		Button pullButton = (Button)(findViewById(R.id.pullButton));
		pullButton.setOnClickListener(buttonListener);
		Button releaseButton = (Button)(findViewById(R.id.releaseButton));
		releaseButton.setOnClickListener(buttonListener);
		Button startButton = (Button)(findViewById(R.id.startButton));
		startButton.setOnClickListener(buttonListener);
		Button turnButton = (Button)(findViewById(R.id.turnButton));
		turnButton.setOnClickListener(buttonListener);
		
		// set the spinner item select listener to set up the new current view
        Spinner objectChooser = (Spinner)(findViewById(R.id.objectChooser));
        objectChooser.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				FrameLayout frameLayout = (FrameLayout)(findViewById(R.id.viewFrame));
				
				// remove the current view if it exists, this should dispose the view
				if (currentView != null)
					frameLayout.removeView(currentView);
				
				// create the new view and set the background color accordingly
				frameLayout.setBackgroundColor(Color.BLACK);
				switch (ObjectType.values()[pos]) {
				case ChristmasTree:
					currentView = new ChristmasTreeView(GraphicsActivity.this);
					break;
				case ClappingMonkey:
					currentView = new ClappingMonkeyView(GraphicsActivity.this);
					break;
				case Pinwheel:
					currentView = new PinwheelView(GraphicsActivity.this);
					frameLayout.setBackgroundColor(Color.WHITE);
					break;
				case Candle:
					currentView = new CandleView(GraphicsActivity.this);
					break;
				case Clapper:
					currentView = new ClapperView(GraphicsActivity.this);
					break;
				case Cleat:
					currentView = new CleatView(GraphicsActivity.this);
					break;
				case ElectricOutlet:
					currentView = new ElectricOutletView(GraphicsActivity.this);
					break;
				case Kettle:
					currentView = new KettleView(GraphicsActivity.this);
					break;
				case RopeDownRight:
					currentView = new RopeDownRightView(GraphicsActivity.this);
					break;
				case RopeLeftRight:
					currentView = new RopeLeftRightView(GraphicsActivity.this);
					break;
				case RopeUpDown:
					currentView = new RopeUpDownView(GraphicsActivity.this);
					break;
				case RopeUpRight:
					currentView = new RopeUpRightView(GraphicsActivity.this);
					break;
				case RubberBand:
					currentView = new RubberBandView(GraphicsActivity.this);
					break;
				case TV:
					currentView = new TVView(GraphicsActivity.this);
					break;
				case WaterCooler:
					currentView = new WaterCoolerView(GraphicsActivity.this);
					break;
				case WireLeftRight:
					currentView = new WireLeftRightView(GraphicsActivity.this);
					break;
				case WireUpDown:
					currentView = new WireUpDownView(GraphicsActivity.this);
					break;
				}

				// add this activity as a property change listener to update
				// the current state textview when the state changes
				currentView.addPropertyChangeListener(GraphicsActivity.this);
				
				// enable the appropriate event buttons for this view
				enableButtons(ObjectType.values()[pos]);
				
				frameLayout.addView(currentView);
				
				// update the current state textview since a property change
				// event won't be set for the initial state
				TextView currentStateView = (TextView)(findViewById(R.id.currentStateView));
				currentStateView.setText((String)(currentView.getCurrentState().getState().name()));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
        	
        });
    }
    
    protected void changeState(Event event, Direction direction) {
    	if (currentView != null) {
    		currentView.changeState(event, direction);
    	}
    }
    
    /**
     * Enable the appropriate event buttons based on the object type.
     * 
     * @param type - object type being displayed
     */
    private void enableButtons(ObjectType type) {
    	// toggle visibility of all buttons based on the object type
    	Button alexButton = (Button)(findViewById(R.id.alexButton));
		Button electricOnButton = (Button)(findViewById(R.id.electricOnButton));
		Button electricOffButton = (Button)(findViewById(R.id.electricOffButton));
		Button heatButton = (Button)(findViewById(R.id.heatButton));
		Button pulseButton = (Button)(findViewById(R.id.pulseButton));
		Button steamButton = (Button)(findViewById(R.id.steamButton));
		Button waterButton = (Button)(findViewById(R.id.waterButton));
		Button clapButton = (Button)(findViewById(R.id.clapButton));
		Button pullButton = (Button)(findViewById(R.id.pullButton));
		Button releaseButton = (Button)(findViewById(R.id.releaseButton));
		Button startButton = (Button)(findViewById(R.id.startButton));
		Button turnButton = (Button)(findViewById(R.id.turnButton));
		
		alexButton.setVisibility(View.VISIBLE);
		electricOnButton.setVisibility(View.VISIBLE);
		electricOffButton.setVisibility(View.VISIBLE);
		heatButton.setVisibility(View.VISIBLE);
		pulseButton.setVisibility(View.VISIBLE);
		steamButton.setVisibility(View.VISIBLE);
		waterButton.setVisibility(View.VISIBLE);
		clapButton.setVisibility(View.VISIBLE);
		pullButton.setVisibility(View.VISIBLE);
		releaseButton.setVisibility(View.VISIBLE);
		startButton.setVisibility(View.VISIBLE);
		turnButton.setVisibility(View.VISIBLE);
		
		/*
		switch (type) {
		case ChristmasTree:
			electricOnButton.setVisibility(View.VISIBLE);
			electricOffButton.setVisibility(View.VISIBLE);
			heatButton.setVisibility(View.VISIBLE);
			pulseButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			break;
		case ClappingMonkey:
			alexButton.setVisibility(View.VISIBLE);
			heatButton.setVisibility(View.VISIBLE);
			pulseButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			break;
		case Pinwheel:
			pulseButton.setVisibility(View.VISIBLE);
			steamButton.setVisibility(View.VISIBLE);
			break;
		case Candle:
			heatButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			startButton.setVisibility(View.VISIBLE);
			pulseButton.setVisibility(View.VISIBLE);
			break;
		case Clapper:
			heatButton.setVisibility(View.VISIBLE);
			startButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			pulseButton.setVisibility(View.VISIBLE);
			break;
		case Cleat:
			pulseButton.setVisibility(View.VISIBLE);
			break;
		case ElectricOutlet:
			pulseButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			heatButton.setVisibility(View.VISIBLE);
			break;
		case Kettle:
			heatButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			break;
		case Rope:
			heatButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			pullButton.setVisibility(View.VISIBLE);
			releaseButton.setVisibility(View.VISIBLE);
			break;
		case RubberBand:
			turnButton.setVisibility(View.VISIBLE);
			heatButton.setVisibility(View.VISIBLE);
			break;
		case TV:
			electricOnButton.setVisibility(View.VISIBLE);
			electricOffButton.setVisibility(View.VISIBLE);
			waterButton.setVisibility(View.VISIBLE);
			heatButton.setVisibility(View.VISIBLE);
			pulseButton.setVisibility(View.VISIBLE);
			turnButton.setVisibility(View.VISIBLE);
			break;
		case WaterCooler:
			pullButton.setVisibility(View.VISIBLE);
			releaseButton.setVisibility(View.VISIBLE);
			break;
		case Wire:
			waterButton.setVisibility(View.VISIBLE);
			heatButton.setVisibility(View.VISIBLE);
			electricOnButton.setVisibility(View.VISIBLE);
			electricOffButton.setVisibility(View.VISIBLE);
			break;
		}
		// redraw the linear layout containing the buttons
		findViewById(R.id.buttonLayout).invalidate();
		*/
    }
    
    protected void send(Event event, Direction direction) {
    	if (event == null)
    		return;
    	if (direction == Direction.All || direction == Direction.Both) {
    		for (Direction d : currentView.getDirections()) {
    			super.send(event, d);
    		}
    	}
    	else
    		super.send(event, direction);
    }

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// listen to state change events and update the current state textview
		if (event.getPropertyName().equals(StateObjectView.StateChangeEvent)) {
			TextView currentStateView = (TextView)(findViewById(R.id.currentStateView));
			currentStateView.setText(currentView.getCurrentState().getState().name());
			TransitionValue transitionValue = (TransitionValue)(event.getNewValue());
			send(transitionValue.getNextEvent(), transitionValue.getNextDirection());
		}
	}
}