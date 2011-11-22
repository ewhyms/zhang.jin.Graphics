package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the clapper object.
 *
 */
public class ClapperView extends StateObjectView {

	public ClapperView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClapperView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClapperView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.PowerOff, Event.ElectricOn, new TransitionValue(State.SwitchOff));
		addTransition(State.PowerOff, Event.Heat, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.PowerOff, Event.Water, new TransitionValue(State.Wet));
		
		addTransition(State.SwitchOff, Event.ElectricOff, new TransitionValue(State.PowerOff, Event.ElectricOff, Direction.Right));
		addTransition(State.SwitchOff, Event.Heat, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.SwitchOff, Event.Water, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.SwitchOff, Event.Clap, new TransitionValue(State.SwitchOn, Event.ElectricOn, Direction.Right));
		
		addTransition(State.SwitchOn, Event.ElectricOff, new TransitionValue(State.PowerOff, Event.ElectricOff, Direction.Right));
		addTransition(State.SwitchOn, Event.Heat, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.SwitchOn, Event.Water, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.SwitchOn, Event.ElectricOn, new TransitionValue(State.SwitchOn, Event.ElectricOn, Direction.Right));
		addTransition(State.SwitchOn, Event.Clap, new TransitionValue(State.SwitchOff, Event.ElectricOff, Direction.Right));
		
		addTransition(State.Wet, Event.Heat, new TransitionValue(State.PowerOff, Event.Heat, Direction.Up));
		addTransition(State.Wet, Event.ElectricOn, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		
		addDrawable(State.PowerOff, context.getResources().getDrawable(R.drawable.clapper_power_off));
		addDrawable(State.SwitchOff, context.getResources().getDrawable(R.drawable.clapper_switch_off));
		addDrawable(State.SwitchOn, context.getResources().getDrawable(R.drawable.clapper_switch_on));
		addDrawable(State.Wet, context.getResources().getDrawable(R.drawable.clapper_wet));
		addDrawable(State.Fried, context.getResources().getDrawable(R.drawable.clapper_fried));
		
		setCurrentState(new TransitionValue(State.PowerOff));
	}

}
