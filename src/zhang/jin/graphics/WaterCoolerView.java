package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the water cooler object.
 *
 */
public class WaterCoolerView extends StateObjectView {

	public WaterCoolerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WaterCoolerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WaterCoolerView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Empty, Event.Pull, new TransitionValue(State.Full));
		addTransition(State.Full, Event.Release, new TransitionValue(State.Empty, Event.Water, Direction.Down));
		
		addDrawable(State.Empty, context.getResources().getDrawable(R.drawable.water_cooler_empty));
		addDrawable(State.Full, context.getResources().getDrawable(R.drawable.water_cooler_full));
		
		setCurrentState(new TransitionValue(State.Empty));
	}

}
