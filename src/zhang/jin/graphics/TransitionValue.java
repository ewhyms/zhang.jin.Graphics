package zhang.jin.graphics;

public class TransitionValue {

	private State state;
	
	private Event nextEvent;
	
	private Direction nextDirection;
	
	public TransitionValue(State state) {
		this(state, null, null);
	}
	
	public TransitionValue(State state, Event nextEvent, Direction nextDirection) {
		this.state = state;
		this.nextEvent = nextEvent;
		this.nextDirection = nextDirection;
	}

	public State getState() {
		return state;
	}

	public Event getNextEvent() {
		return nextEvent;
	}

	public Direction getNextDirection() {
		return nextDirection;
	}

	public void setNextDirection(Direction direction) {
		this.nextDirection = direction;
	}
	
}
