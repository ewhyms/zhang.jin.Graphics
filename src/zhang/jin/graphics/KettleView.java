package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the kettle object.
 *
 */
public class KettleView extends StateObjectView {

	public KettleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public KettleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public KettleView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Empty, Event.Heat, new TransitionValue(State.Hot));
		addTransition(State.Empty, Event.Water, new TransitionValue(State.Full));
		addTransition(State.Hot, Event.Water, new TransitionValue(State.Full, Event.Steam, Direction.Right));
		addTransition(State.Full, Event.Heat, new TransitionValue(State.Full, Event.Steam, Direction.Right));
		addTransition(State.Full, Event.Steam, new TransitionValue(State.Full, Event.Steam, Direction.Right));
		
		addDrawable(State.Empty, context.getResources().getDrawable(R.drawable.kettle_empty));
		addDrawable(State.Hot, context.getResources().getDrawable(R.drawable.kettle_hot));
		addDrawable(State.Full, context.getResources().getDrawable(R.drawable.kettle_full));
		
		setCurrentState(new TransitionValue(State.Empty));
	}

}
