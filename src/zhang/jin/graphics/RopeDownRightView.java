package zhang.jin.graphics;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the rope left-right object.
 *
 */
public class RopeDownRightView extends RopeView {

	public RopeDownRightView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle, Direction.Down, Direction.Right);
	}

	public RopeDownRightView(Context context, AttributeSet attrs) {
		super(context, attrs, Direction.Down, Direction.Right);
	}

	public RopeDownRightView(Context context) {
		super(context, Direction.Down, Direction.Right);
	}
	
	@Override
	protected void resetDirectionsList() {
		directions = new ArrayList<Direction>();
		directions.add(Direction.Down);
		directions.add(Direction.Right);
	}

	/**
	 * Set up the drawables for this version of the rope.
	 * @see zhang.jin.graphics.RopeView#setupDrawables(android.content.Context)
	 */
	@Override
	protected void setupDrawables(Context context) {
		addDrawable(State.Slack, context.getResources().getDrawable(R.drawable.rope_slack_left_right));
		addDrawable(State.BurningSlack, context.getResources().getDrawable(R.drawable.rope_burning_slack_left_right));
		addDrawable(State.Taut, context.getResources().getDrawable(R.drawable.rope_taut_left_right));
		addDrawable(State.BurningTaut, context.getResources().getDrawable(R.drawable.rope_burning_taut_left_right));
		addDrawable(State.Ashes, context.getResources().getDrawable(R.drawable.rope_ashes_left_right));
	}

}
