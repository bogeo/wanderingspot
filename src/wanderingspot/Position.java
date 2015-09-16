package wanderingspot;

/**
 * Position given in pixel coordinates.
 * 
 * Note that position (0,0) refers to the upper left corner of the canvas. 
 */
public class Position 
{
	public int x, y;

	/**
	 * Constructor
	 * 
	 * @param x horizontal pixel coordinate
	 * @param y vertical pixel coordinate
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param p Position object
	 */
	public Position(Position p) {
		this.x = p.x;
		this.y = p.y;
	}
}
