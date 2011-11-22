package zhang.jin.graphics;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The base view class for the rope object.
 */
public abstract class RopeView extends StateObjectView {

	private Map<Direction, Direction> opposites = new HashMap<Direction, Direction>();
 	
	public RopeView(Context context, AttributeSet attrs, int defStyle, Direction direction1, Direction direction2) {
		super(context, attrs, defStyle);
		setupOpposites(direction1, direction2);
		resetDirectionsList();
	}

	public RopeView(Context context, AttributeSet attrs, Direction direction1, Direction direction2) {
		super(context, attrs);
		setupOpposites(direction1, direction2);
		resetDirectionsList();
	}

	public RopeView(Context context, Direction direction1, Direction direction2) {
		super(context);
		setupOpposites(direction1, direction2);
		resetDirectionsList();
	}
	
	/**
	 * Resets the directions from all to a specific one
	 */
	protected abstract void resetDirectionsList();
	
	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Slack, Event.Heat, new TransitionValue(State.BurningSlack, Event.Heat, Direction.Up));
		addTransition(State.Slack, Event.Pull, new TransitionValue(State.Taut, Event.Pull, Direction.Opposite));
		
		addTransition(State.BurningSlack, Event.Water, new TransitionValue(State.Slack));
		addTransition(State.BurningSlack, Event.Heat, new TransitionValue(State.Ashes, Event.Heat, Direction.Up));
		
		addTransition(State.Taut, Event.Heat, new TransitionValue(State.BurningTaut, Event.Heat, Direction.Up));
		addTransition(State.Taut, Event.Pull, new TransitionValue(State.Taut, Event.Pull, Direction.Opposite));
		addTransition(State.Taut, Event.Release, new TransitionValue(State.Slack, Event.Release, Direction.Opposite));
		
		addTransition(State.BurningTaut, Event.Heat, new TransitionValue(State.Ashes, Event.Release, Direction.Both));
		addTransition(State.BurningTaut, Event.Water, new TransitionValue(State.Taut));
		
		setupDrawables(context);
		
		setCurrentState(new TransitionValue(State.Slack));
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
			Map<Event, TransitionValue> trans = transitions.get(currentState);
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
