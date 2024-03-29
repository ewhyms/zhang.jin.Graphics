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
public class RopeUpRightView extends RopeView {

	public RopeUpRightView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle, Direction.Up, Direction.Right);
	}

	public RopeUpRightView(Context context, AttributeSet attrs) {
		super(context, attrs, Direction.Up, Direction.Right);
	}

	public RopeUpRightView(Context context) {
		super(context, Direction.Up, Direction.Right);
	}
	
	@Override
	protected void resetDirectionsList() {
		directions = new ArrayList<Direction>();
		directions.add(Direction.Up);
		directions.add(Direction.Right);
	}

	/**
	 * Set up the drawables for this version of the rope.
	 * @see zhang.jin.graphics.RopeView#setupDrawables(android.content.Context)
	 */
	@Override
	protected void setupDrawables(Context context) {
		addDrawable(State.Slack, context.getResources().getDrawable(R.drawable.rope_slack_up_right));
		addDrawable(State.BurningSlack, context.getResources().getDrawable(R.drawable.rope_burning_slack_up_right));
		addDrawable(State.Taut, context.getResources().getDrawable(R.drawable.rope_taut_up_right));
		addDrawable(State.BurningTaut, context.getResources().getDrawable(R.drawable.rope_burning_taut_up_right));
		addDrawable(State.Ashes, context.getResources().getDrawable(R.drawable.rope_ashes_up_right));
	}

}
