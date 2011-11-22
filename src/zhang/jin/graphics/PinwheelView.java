package zhang.jin.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * @author Jin F. Zhang
 * 
 * The view class for the pinwheel object.
 */
public class PinwheelView extends StateObjectView {

	// The scaled bitmap of the pinwheel
	private Bitmap scaledBitmap = null;
	
	// The rotation animation
	private Animation animation = null;
	
	public PinwheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PinwheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinwheelView(Context context) {
		super(context);
	}

	/**
	 * Set up transitions map and drawables map, and the initial state.
	 * @param context - the context of this view
	 */
	protected void setup(Context context) {
		addTransition(State.Stopped, Event.Steam, new TransitionValue(State.Turning, Event.Turn, Direction.All));
		addTransition(State.Turning, Event.Pulse, new TransitionValue(State.Stopped));

		addDrawable(State.Stopped, context.getResources().getDrawable(R.drawable.pinwheel));
		addDrawable(State.Turning, context.getResources().getDrawable(R.drawable.pinwheel));
		
		setCurrentState(new TransitionValue(State.Stopped));
	}

	/**
	 * Create the scaled bitmap and the rotation animation.
	 */
	private void createGraphics() {
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.pinwheel);
		int max = Math.max(image.getWidth(), image.getHeight());
		int min = Math.min(this.getWidth(), this.getHeight());
		float scale = ((float)min) / max / 1.5f;
		Matrix matrix = new Matrix();
		matrix.preScale(scale, scale);
		scaledBitmap = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

		// set the pivot point to the center of this view
		animation = new RotateAnimation(0, 360, this.getWidth()/2, this.getHeight()/2);
		animation.setRepeatCount(0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setDuration(3000);
	}
	
	/**
	 * Start the rotation animation when the state is changed to Turning
	 * @param event - the event to apply
	 */
	public void changeState(Event event, Direction direction) {
		super.changeState(event, direction);
		if (getCurrentState().getState() == State.Turning) {
			startAnimation(animation);
		}
	}
	
	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		// trigger a pulse event when the rotation animation is complete
		changeState(Event.Pulse, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (animation == null || scaledBitmap == null) {
			createGraphics();
		}

		// draw the scaled pinwheel at the center of the view
		// center of pinwheel is a bit off in the image so need to offset by a few pixels
		canvas.drawBitmap(scaledBitmap, 
				this.getLeft() + (this.getWidth() - scaledBitmap.getWidth())/2 + 2, 
				this.getTop() + (this.getHeight() - scaledBitmap.getHeight())/2 - 6,
				null);
	}

}
