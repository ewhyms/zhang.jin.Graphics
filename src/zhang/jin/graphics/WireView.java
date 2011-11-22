package zhang.jin.graphics;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The base view class for the wire object.
 */
public abstract class WireView extends StateObjectView {

	private Map<Direction, Direction> opposites = new HashMap<Direction, Direction>();
 	
	public WireView(Context context, AttributeSet attrs, int defStyle, Direction direction1, Direction direction2) {
		super(context, attrs, defStyle);
		setupOpposites(direction1, direction2);
	}

	public WireView(Context context, AttributeSet attrs, Direction direction1, Direction direction2) {
		super(context, attrs);
		setupOpposites(direction1, direction2);
	}

	public WireView(Context context, Direction direction1, Direction direction2) {
		super(context);
		setupOpposites(direction1, direction2);
	}
	
	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.NoCurrent, Event.Heat, new TransitionValue(State.Toasted, Event.Heat, Direction.Up));
		addTransition(State.NoCurrent, Event.Water, new TransitionValue(State.Wet));
		addTransition(State.NoCurrent, Event.ElectricOn, new TransitionValue(State.HasCurrent, Event.ElectricOn, Direction.Opposite));
		
		addTransition(State.Wet, Event.ElectricOn, new TransitionValue(State.Shorted, Event.Heat, Direction.Up));
		addTransition(State.Wet, Event.Heat, new TransitionValue(State.NoCurrent, Event.Heat, Direction.Up));
		
		addTransition(State.HasCurrent, Event.ElectricOff, new TransitionValue(State.NoCurrent, Event.ElectricOff, Direction.Opposite));
		addTransition(State.HasCurrent, Event.ElectricOn, new TransitionValue(State.HasCurrent, Event.ElectricOn, Direction.Opposite));
		addTransition(State.HasCurrent, Event.Heat, new TransitionValue(State.Toasted, Event.Heat, Direction.Up));
		
		setupDrawables(context);
		
		setCurrentState(new TransitionValue(State.NoCurrent));
	}

	/**
	 * Set up the drawables in the subclass
	 * @param context - the context of this view
	 */
	protected abstract void setupDrawables(Context context);

	protected void setupOpposites(Direction direction1, Direction direction2) {
		opposites.clear();
		opposites.put(direction1, direction2);
		opposites.put(direction2, direction1);
	}
	
	public Direction getOpposite(Direction direction) {
		return opposites.get(direction);
	}
	
	@Override
	public void changeState(Event event, Direction direction) {
		if (opposites.containsKey(direction)) {
			Map<Event, TransitionValue> trans = transitions.get(currentState.getState());
			if (trans != null) {
				TransitionValue newState = trans.get(event);
				if (newState != null) {
					currentState = newState;
					if (currentState.getNextDirection() == Direction.Opposite)
					{
						Direction sendingDirection = getOpposite(direction);
						currentState.setNextDirection(sendingDirection);
					}
					propertyChangeSupport.firePropertyChange(StateChangeEvent, null, currentState);
					invalidate();
				}
			}
		}
	}
}
