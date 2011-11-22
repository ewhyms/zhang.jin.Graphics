package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the christmas tree object.
 */
public class ChristmasTreeView extends StateObjectView {

	public ChristmasTreeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ChristmasTreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChristmasTreeView(Context context) {
		super(context);
	}
	
	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Burning, Event.Heat, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.Burning, Event.Water, new TransitionValue(State.Unlit));
		addTransition(State.Burning, Event.Pulse, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.Lit, Event.ElectricOff, new TransitionValue(State.Unlit));
		addTransition(State.Lit, Event.Heat, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		addTransition(State.Lit, Event.Water, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		addTransition(State.Unlit, Event.ElectricOn, new TransitionValue(State.Lit));
		addTransition(State.Unlit, Event.Heat, new TransitionValue(State.Burning, Event.Heat, Direction.Up));
		addTransition(State.Unlit, Event.Water, new TransitionValue(State.Wet));
		addTransition(State.Wet, Event.Heat, new TransitionValue(State.Unlit, Event.Heat, Direction.Up));
		addTransition(State.Wet, Event.ElectricOn, new TransitionValue(State.Fried, Event.Heat, Direction.Up));
		
		addDrawable(State.Burning, context.getResources().getDrawable(R.drawable.tree_burning));
		addDrawable(State.Lit, context.getResources().getDrawable(R.drawable.tree_lit));
		addDrawable(State.Unlit, context.getResources().getDrawable(R.drawable.tree_unlit));
		addDrawable(State.Wet, context.getResources().getDrawable(R.drawable.tree_wet));
		addDrawable(State.Fried, context.getResources().getDrawable(R.drawable.tree_burnt));
		
		setCurrentState(new TransitionValue(State.Unlit, null, null));
	}

}
