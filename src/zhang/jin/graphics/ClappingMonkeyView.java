package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the clapping monkey.
 */
public class ClappingMonkeyView extends StateObjectView {

	public ClappingMonkeyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClappingMonkeyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClappingMonkeyView(Context context) {
		super(context);
	}
	
	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Bored, Event.Alex, new TransitionValue(State.ClappingWide, Event.Clap, Direction.All));
		addTransition(State.Bored, Event.Water, new TransitionValue(State.Wet));
		addTransition(State.Bored, Event.Heat, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		addTransition(State.Burning, Event.Water, new TransitionValue(State.Bored));
		addTransition(State.Burning, Event.Heat, new TransitionValue(State.Ashes, Event.Heat, Direction.Up));
		addTransition(State.Burning, Event.Pulse, new TransitionValue(State.Ashes, Event.Heat, Direction.Up));
		addTransition(State.ClappingClosed, Event.Pulse, new TransitionValue(State.ClappingWide, Event.Clap, Direction.All));
		addTransition(State.ClappingClosed, Event.Heat, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		addTransition(State.ClappingClosed, Event.Water, new TransitionValue(State.WetClappingWide, Event.Clap, Direction.All));
		addTransition(State.ClappingWide, Event.Pulse, new TransitionValue(State.ClappingClosed, Event.Clap, Direction.All));
		addTransition(State.ClappingWide, Event.Heat, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		addTransition(State.ClappingWide, Event.Water, new TransitionValue(State.WetClappingClosed, Event.Clap, Direction.All));
		addTransition(State.Wet, Event.Alex, new TransitionValue(State.WetClappingWide, Event.Clap, Direction.All));
		addTransition(State.Wet, Event.Heat, new TransitionValue(State.Bored));
		addTransition(State.WetClappingClosed, Event.Pulse, new TransitionValue(State.WetClappingWide, Event.Clap, Direction.All));
		addTransition(State.WetClappingClosed, Event.Heat, new TransitionValue(State.ClappingWide, Event.Heat, Direction.Up));
		addTransition(State.WetClappingWide, Event.Pulse, new TransitionValue(State.WetClappingClosed, Event.Clap, Direction.All));
		addTransition(State.WetClappingWide, Event.Heat, new TransitionValue(State.ClappingClosed, Event.Clap, Direction.All));
		
		addDrawable(State.Ashes, context.getResources().getDrawable(R.drawable.monkey_ashes));
		addDrawable(State.Bored, context.getResources().getDrawable(R.drawable.monkey_bored));
		addDrawable(State.Burning, context.getResources().getDrawable(R.drawable.monkey_burning));
		addDrawable(State.ClappingClosed, context.getResources().getDrawable(R.drawable.monkey_clapping_closed));
		addDrawable(State.ClappingWide, context.getResources().getDrawable(R.drawable.monkey_clapping_wide));
		addDrawable(State.Wet, context.getResources().getDrawable(R.drawable.monkey_wet));
		addDrawable(State.WetClappingClosed, context.getResources().getDrawable(R.drawable.monkey_wet_clapping_closed));
		addDrawable(State.WetClappingWide, context.getResources().getDrawable(R.drawable.monkey_wet_clapping_wide));

		setCurrentState(new TransitionValue(State.Bored));
	}
	
}
