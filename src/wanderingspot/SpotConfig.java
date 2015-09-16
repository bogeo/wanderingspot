package wanderingspot;

/**
 * Spot definition. 
 * 
 * Note that such a spot definition holds the parameter-values as given in the <i>initial (!)</i> 
 * scene configuration. Once the <tt>WanderingSpot</tt> application has been started, usually 
 * some of these parameters will change dynamically.
 * 
 * @author Benno Schmidt
 */
public class SpotConfig 
{
	private SpotTypes.Shape mShape;
	private SpotTypes.Type mType;
	private int mSize; // the spot's initial size n pixels
	private Position mPos; // initial spot position
	private float mDir; // initial wandering direction
	private boolean mPulsatingSize = false; // (experimental function)
	private boolean mCushionBehaviour = false;
	private boolean mFollowsMousePointer = false;

	/**
	 * Constructor
	 * 
	 * @param shape Shape definition
	 * @param type Spot type
	 * @param size Spot size (for a circular shape, this gives the circle's radius in pixels)
	 * @param pos Spot position (<i>null</i> to center spot initially)
	 * @param dir Wandering direction (given in decimal degrees (0...360))
	 * @param pulsatingSize Pulsation mode (still experimental)
	 * @param cushionBehaviour Cushion behavior (when reaching canvas border)
	 */
	public SpotConfig(
			SpotTypes.Shape shape, 
			SpotTypes.Type type, 
			int size, 
			Position pos, 
			float dir, 
			boolean pulsatingSize, 
			boolean cushionBehaviour,
			boolean followsMousePointer) 
	{
		mShape = shape;
		mType = type;
		mSize = size;
		mPos = pos;
		mDir = dir;
		mPulsatingSize = pulsatingSize;
		mCushionBehaviour = cushionBehaviour;
		mFollowsMousePointer = followsMousePointer;
		
		// TODO cushionBehavior does not make sense, if followsMousePointer is set
		// TODO Modes: WANDERING, CUSHION, FOLLOW_MOUSE_POINTER
		// TODO followsMousePointer makes sense for only 1 spot per scene
	}

	public SpotTypes.Shape getShape() {
		return mShape;
	}
	
	public SpotTypes.Type getType() {
		return mType;
	}

	public int getSize() {
		return mSize;
	}
	
	public Position getPos() {
		return mPos;
	}
	
	public float getInitialWanderingDirection() {
		return mDir;
	}

	public boolean isPulsating() {
		return mPulsatingSize;
	}
	
	public boolean hasCushionBehaviour() {
		return mCushionBehaviour;
	}	
	
	public boolean followsMousePointer() {
		return mFollowsMousePointer;
	}
}
