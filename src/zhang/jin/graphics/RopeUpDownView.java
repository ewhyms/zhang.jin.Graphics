package zhang.jin.graphics;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Jin F. Zhang
 *
 * The view class for the rope up-down object.
 *
 */
public class RopeUpDownView extends RopeView {

	public RopeUpDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle, Direction.Up, Direction.Down);
	}

	public RopeUpDownView(Context context, AttributeSet attrs) {
		super(context, attrs, Direction.Up, Direction.Down);
	}

	public RopeUpDownView(Context context) {
		super(context, Direction.Up, Direction.Down);
	}
	
	@Override
	protected void resetDirectionsList() {
		directions = new ArrayList<Direction>();
		directions.add(Direction.Down);
		directions.add(Direction.Up);
	}

	/**
	 * Set up the drawables for this version of the rope.
	 * @see zhang.jin.graphics.RopeView#setupDrawables(android.content.Context)
	 */
	@Override
	protected void setupDrawables(Context context) {
		addDrawable(State.Slack, context.getResources().getDrawable(R.drawable.rope_slack_up_down));
		addDrawable(State.BurningSlack, context.getResources().getDrawable(R.drawable.rope_burning_slack_up_down));
		addDrawable(State.Taut, context.getResources().getDrawable(R.drawable.rope_taut_up_down));
		addDrawable(State.BurningTaut, context.getResources().getDrawable(R.drawable.rope_burning_taut_up_down));
		addDrawable(State.Ashes, context.getResources().getDrawable(R.drawable.rope_ashes_up_down));
	}

}
