package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the wire up-down object.
 *
 */
public class WireUpDownView extends WireView {

	public WireUpDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle, Direction.Up, Direction.Down);
	}

	public WireUpDownView(Context context, AttributeSet attrs) {
		super(context, attrs, Direction.Up, Direction.Down);
	}

	public WireUpDownView(Context context) {
		super(context, Direction.Up, Direction.Down);
	}

	/**
	 * Set up the drawables for this version of the wire.
	 * @see zhang.jin.graphics.RopeView#setupDrawables(android.content.Context)
	 */
	@Override
	protected void setupDrawables(Context context) {
		addDrawable(State.NoCurrent, context.getResources().getDrawable(R.drawable.wire_no_current_up_down));
		addDrawable(State.Wet, context.getResources().getDrawable(R.drawable.wire_wet_up_down));
		addDrawable(State.HasCurrent, context.getResources().getDrawable(R.drawable.wire_has_current_up_down));
		addDrawable(State.Shorted, context.getResources().getDrawable(R.drawable.wire_shorted_up_down));
		addDrawable(State.Toasted, context.getResources().getDrawable(R.drawable.wire_toasted_up_down));
	}

}
