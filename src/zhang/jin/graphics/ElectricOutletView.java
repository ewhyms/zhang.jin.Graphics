package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the electric outlet object.
 *
 */
public class ElectricOutletView extends StateObjectView {

	public ElectricOutletView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ElectricOutletView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ElectricOutletView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.On, Event.Pulse, new TransitionValue(State.On, Event.ElectricOn, Direction.All));
		addTransition(State.On, Event.Water, new TransitionValue(State.Shocked, Event.ElectricOff, Direction.All));
		addTransition(State.On, Event.Heat, new TransitionValue(State.Burnt, Event.ElectricOff, Direction.All));
		
		addDrawable(State.On, context.getResources().getDrawable(R.drawable.electric_outlet_on));
		addDrawable(State.Shocked, context.getResources().getDrawable(R.drawable.electric_outlet_shocked));
		addDrawable(State.Burnt, context.getResources().getDrawable(R.drawable.electric_outlet_burnt));
		
		setCurrentState(new TransitionValue(State.On));
	}

}
