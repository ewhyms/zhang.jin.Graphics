package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
  * @author Jin F. Zhang
 *
 * The view class for the candle object.
 *
 */
public class CandleView extends StateObjectView {

	public CandleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CandleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CandleView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Burning, Event.Pulse, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		addTransition(State.Burning, Event.Water, new TransitionValue(State.Unlit));
		addTransition(State.Unlit, Event.Start, new TransitionValue(State.Burning, Event.Heat, Direction.Up));	
		addTransition(State.Unlit, Event.Heat, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		
		addDrawable(State.Burning, context.getResources().getDrawable(R.drawable.candle_burning));
		addDrawable(State.Unlit, context.getResources().getDrawable(R.drawable.candle_unlit));
		
		setCurrentState(new TransitionValue(State.Unlit));
	}

}
