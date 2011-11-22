package zhang.jin.graphics;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the wire left-right object.
 *
 */
public class WireLeftRightView extends WireView {

	public WireLeftRightView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle, Direction.Left, Direction.Right);
	}

	public WireLeftRightView(Context context, AttributeSet attrs) {
		super(context, attrs, Direction.Left, Direction.Right);
	}

	public WireLeftRightView(Context context) {
		super(context, Direction.Left, Direction.Right);
	}

	/**
	 * Set up the drawables for this version of the wire.
	 * @see zhang.jin.graphics.RopeView#setupDrawables(android.content.Context)
	 */
	@Override
	protected void setupDrawables(Context context) {
		addDrawable(State.NoCurrent, context.getResources().getDrawable(R.drawable.wire_no_current_left_right));
		addDrawable(State.Wet, context.getResources().getDrawable(R.drawable.wire_wet_left_right));
		addDrawable(State.HasCurrent, context.getResources().getDrawable(R.drawable.wire_has_current_left_right));
		addDrawable(State.Shorted, context.getResources().getDrawable(R.drawable.wire_shorted_left_right));
		addDrawable(State.Toasted, context.getResources().getDrawable(R.drawable.wire_toasted_left_right));
	}

}
