package wanderingspot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

/**
 * Object holding a moving spot's animation definition as well as the spot's state.
 * 
 * @author Benno Schmidt
 */
public class LivingThing 
{
	private SpotConfig mSpotDef;
	private Position mPos; // spot position
	private float mDir; // the spot's wandering direction
	private int mSize; // the spot's initial size n pixels
	private boolean mPulsatingSize = false; // (experimental function)
	private boolean mCushionBehaviour = false;
	private SpotTypes.Type mType = SpotTypes.Type.SeeThrough;
	long dynamicFixedLabelDisplayDuration = 1700L + (long)(500. * Math.random()); // TODO 
	long dynamicFixedLabelRepeatCycle = 10323L + (long)(5000. * Math.random()); // TODO

	private ScreenSize mScreenSize;

	/**
	 * Constructor
	 * 
	 * @param spot Spot definition
	 * @param resol Screen resolution (given in pixels)
	 */
	public LivingThing(SpotConfig spot, ScreenSize resol) 
	{
		mSpotDef = spot;

		mScreenSize = resol; // necessary "habitat info"...

		if (spot.getPos() != null)
			mPos = spot.getPos();
		else { 
			// center spot:
			int x = mScreenSize.width / 2; 
			int y = mScreenSize.height / 2;
			mPos = new Position(x, y);
		}
		
		mDir = spot.getInitialWanderingDirection();
		mSize = spot.getSize();
		
		mType = spot.getType();
		
		mPulsatingSize = spot.isPulsating();		
		mCushionBehaviour = spot.hasCushionBehaviour();
	}
	
	/**
	 * gets the spot's definition (initial configuration).
	 * 
	 * @return Spot definition
	 */
	public SpotConfig getSpotDef() {
		return mSpotDef;
	}

	/**
	 * gets the "living" spot's current position.
	 * 
	 * @return Position referring to pixel coordinates
	 */
	public Position getPosition() 
	{
		if (mSpotDef.followsMousePointer()) {
			PointerInfo a = MouseInfo.getPointerInfo();
			Point b = a.getLocation();
			mPos.x = (int) b.getX();
			mPos.y = (int) b.getY();
			// TODO this can be set for a single spot per scene only; e.g., introduce a flag getPosByMouse for spots? 
		}

		return mPos;
	}
	
	/**
	 * gets the "living" spot's current wandering direction.
	 * 
	 * @return Direction in decimal degrees (0...360)
	 */
	public float getWanderingDirection() {
		return mDir;
	}

	static long mCounter = 0;
	
	/**
	 * gets the "living" spot's current size.
	 * 
	 * @return Size in pixels
	 */	
	public int getSize() 
	{
		if (mPulsatingSize) {
			// TODO This is still experimental...
			double factor = ((double) mSize/2.) * Math.cos(((double) mCounter / 8.0) * Math.PI / 20.); 
			return (int) (((double) mSize/2.) + factor);
		}
		
		return mSize;
	}

	/**
	 * determines a new location for the "living" spot (and maybe change some other state values).
	 */
	public void justLive() 
	{
		mDir += Math.round(10. * Math.random() - 5);
		if (mDir < 0.f) mDir += 360.f;
		if (mDir >= 360.f) mDir -= 360.f;
		double dx = 2.5 * Math.cos(mDir * Math.PI / 180.);
		double dy = 2.5 * Math.sin(mDir * Math.PI / 180.);
		mPos.x += dx;
		mPos.y += dy;
		
		if (! mCushionBehaviour) 
		{
			if (mPos.x < 0) { 
				mPos.x += mScreenSize.width; 
			} else {
				if (mPos.x >= mScreenSize.width) {
					mPos.x -= mScreenSize.width; 
				}
			}
			if (mPos.y < 0) {
				mPos.y += mScreenSize.height; 
			} 
			else {
				if (mPos.y >= mScreenSize.height) {
					mPos.y -= mScreenSize.height; 
				}
			}
		}
		else {
			if (mPos.x < mSize) { 
				mPos.x = mSize;
				mDir = this.cushion(true, false, mDir); 
			} 
			else {
				if (mPos.x > mScreenSize.width - mSize) {
					mPos.x = mScreenSize.width - mSize;
					mDir = this.cushion(true, false, mDir); 
				}
			}
			if (mPos.y < mSize) {
				mPos.y = mSize;
				mDir = this.cushion(false, true, mDir); 
			} 
			else {
				if (mPos.y > mScreenSize.height - mSize) {
					mPos.y = mScreenSize.height - mSize;
					mDir = this.cushion(false, true, mDir); 
				}
			}
		}
		
		//System.out.println("" + mPos.x + " " + mPos.y);			

		mCounter++; // still experimental...
	}
	
	private float cushion(boolean x, boolean y, float dir) 
	{
		float lDir = dir;
		while (lDir < 0.f) lDir += 360.f;
		while (lDir > 360.f) lDir -= 360.f;
		if (x) {
			lDir = 180.f - dir; 
		}
		if (y) {
			lDir = 360.f - dir; 
		}
		if (lDir < 0.f) lDir += 360.f;
		if (lDir > 360.f) lDir -= 360.f;
		//System.out.println("" +  dir + " -> " + lDir);
		return lDir;
	}

	/**
	 * get the current spot type. Note that for spots that are defined as "dynamic", the
	 * type might be both "SeeThrough" or "FixedLabel".
	 * 
	 * @return Current spot type
	 * 
	 * @see SpotTypes.Type
	 */
	public SpotTypes.Type getType() 
	{
		SpotTypes.Type res = SpotTypes.Type.SeeThrough;
	
		if (mType.equals(SpotTypes.Type.Dynamic)) {
			if (System.currentTimeMillis() % dynamicFixedLabelRepeatCycle < dynamicFixedLabelDisplayDuration) {
				res = SpotTypes.Type.FixedLabel;
			}
		}
		
		if (mType.equals(SpotTypes.Type.Magnifier))
			res = SpotTypes.Type.Magnifier;
		if (mType.equals(SpotTypes.Type.Warp))
			res = SpotTypes.Type.Warp;
		if (mType.equals(SpotTypes.Type.SeeThroughTail))
			res = SpotTypes.Type.SeeThroughTail;
		
		return res;
	}	
}
