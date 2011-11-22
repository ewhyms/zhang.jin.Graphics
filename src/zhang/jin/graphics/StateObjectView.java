package zhang.jin.graphics;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jin F. Zhang
 * 
 * The base class for a state object view. Handles the state changes and
 * basic image drawing.
 */
public abstract class StateObjectView extends View {

	// The property name for state changes
	public static final String StateChangeEvent = "StateChangeEvent";
	
	// The current state of the object
	protected TransitionValue currentState = null;
	
	// The map of transitions between states
	protected Map<State, Map<Event, TransitionValue>> transitions = null;
	
	// The map of all drawables for this object
	private Map<State, Drawable> drawables = null;
	
	// The property change support for firing state changes
	protected PropertyChangeSupport propertyChangeSupport = null;
	
	protected List<Direction> directions = null;
	
	public StateObjectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		setup(context);
	}

	public StateObjectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		setup(context);
	}

	public StateObjectView(Context context) {
		super(context);
		init();
		setup(context);
	}

	/**
	 * Initialize any members of the class that need initializing.
	 */
	private void init() {
		transitions = new HashMap<State, Map<Event, TransitionValue>>();
		drawables = new HashMap<State, Drawable>();
		propertyChangeSupport = new PropertyChangeSupport(this);
		directions = new ArrayList<Direction>();
		directions.add(Direction.All);
	}
	
	protected abstract void setup(Context context);
	
	/**
	 * Add a transition to the transition map.
	 * @param initialState - the starting state for the transition
	 * @param event - the event triggered
	 * @param nextState - the state entered after the event occurs on the starting state
	 */
	protected void addTransition(State initialState, Event event, TransitionValue nextState) {
		HashMap<Event, TransitionValue> trans = (HashMap<Event, TransitionValue>) transitions.get(initialState);
		if (trans == null) {
			trans = new HashMap<Event, TransitionValue>();
			transitions.put(initialState, trans);
		}
		trans.put(event, nextState);
	}
	
	/**
	 * Add a drawable image to the drawables map.
	 * @param state - the state associated with the image
	 * @param drawable - the drawable image
	 */
	protected void addDrawable(State state, Drawable drawable) {
		drawables.put(state, drawable);
	}
	
	/**
	 * Add a property change listener.
	 * @param listener - the listener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	/**
	 * Set the current state - should only be used for initial conditions.
	 * @param state - the state to set as current
	 */
	protected void setCurrentState(TransitionValue state) {
		currentState = state;
		propertyChangeSupport.firePropertyChange(StateChangeEvent, null, currentState.getState().name());
	}
	
	/**
	 * Get the current state of the object.
	 * @return the current state
	 */
	public TransitionValue getCurrentState() {
		return currentState;
	}
	
	/**
	 * Get the list of directions to send the next event
	 * @return the list of directions
	 */
	public List<Direction> getDirections() {
		return directions;
	}
	
	/**
	 * Change the current state based on the event triggered. Do nothing if
	 * the event is not in the transition map for the current state.
	 * @param event - the event triggered
	 */
	public void changeState(Event event, Direction direction) {
		Map<Event, TransitionValue> trans = transitions.get(currentState);
		if (trans != null) {
			TransitionValue newState = trans.get(event);
			if (newState != null) {
				currentState = newState;
				propertyChangeSupport.firePropertyChange(StateChangeEvent, null, currentState);
				invalidate();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw the image for the current state
		Drawable drawable = drawables.get(currentState.getState());
		setBackgroundDrawable(drawable);
	}
	
}
