package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the cleat object.
 *
 */
public class CleatView extends StateObjectView {

	public CleatView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CleatView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CleatView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Exists, Event.Pulse, new TransitionValue(State.Exists, Event.Pull, Direction.Left));
		
		addDrawable(State.Exists, context.getResources().getDrawable(R.drawable.cleat_exists));
		
		setCurrentState(new TransitionValue(State.Exists));
	}

}
