package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the rubber band object.
 *
 */
public class RubberBandView extends StateObjectView {

	public RubberBandView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RubberBandView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RubberBandView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Intact, Event.Turn, new TransitionValue(State.Intact, Event.Turn, Direction.All));
		addTransition(State.Intact, Event.Heat, new TransitionValue(State.Melted, Event.Heat, Direction.Up));
		
		addDrawable(State.Intact, context.getResources().getDrawable(R.drawable.rubberband_intact));
		addDrawable(State.Melted, context.getResources().getDrawable(R.drawable.rubberband_melted));
		
		setCurrentState(new TransitionValue(State.Intact));
	}

}
