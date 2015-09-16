package wanderingspot;

/**
 * Clip rectangle geometry.
 * 
 * Note that position (0,0) refers to the upper left corner of the canvas. 
 */
public class ClipRectangle 
{
	public int x, y, w, h;

	/**
	 * Constructor
	 * 
	 * @param upperLeftCorner Upper left corner of the rectangle (given in pixel-coordinates)
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public ClipRectangle(Position upperLeftCorner, int width, int height) 
	{
		this.x = upperLeftCorner.x;
		this.y = upperLeftCorner.y;
		this.w = width;
		this.h = height;
	}
	
	public ClipRectangle(int x, int y, int width, int height) 
	{
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}
}
