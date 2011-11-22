package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the tv object.
 *
 */
public class TVView extends StateObjectView {

	public TVView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TVView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TVView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.NoPower, Event.ElectricOn, new TransitionValue(State.Standby));
		addTransition(State.NoPower, Event.Water, new TransitionValue(State.Wet));
		
		addTransition(State.Standby, Event.Water, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.Standby, Event.ElectricOff, new TransitionValue(State.NoPower));
		addTransition(State.Standby, Event.Turn, new TransitionValue(State.PowerOn, Event.Alex, Direction.All));

		addTransition(State.PowerOn, Event.Pulse, new TransitionValue(State.PowerOn, Event.Alex, Direction.All));
		addTransition(State.PowerOn, Event.Water, new TransitionValue(State.Fried, Event.Heat, Direction.Up));

		addTransition(State.Wet, Event.Heat, new TransitionValue(State.NoPower));
		addTransition(State.Wet, Event.ElectricOn, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		
		addDrawable(State.NoPower, context.getResources().getDrawable(R.drawable.tv_no_power));
		addDrawable(State.Standby, context.getResources().getDrawable(R.drawable.tv_standby));
		addDrawable(State.PowerOn, context.getResources().getDrawable(R.drawable.tv_power_on));
		addDrawable(State.Wet, context.getResources().getDrawable(R.drawable.tv_wet));
		addDrawable(State.Fried, context.getResources().getDrawable(R.drawable.tv_fried));
		
		setCurrentState(new TransitionValue(State.NoPower));
	}

}
