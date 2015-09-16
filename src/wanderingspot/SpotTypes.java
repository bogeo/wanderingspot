package wanderingspot;

/**
 * Spot shape and type identifiers
 * 
 * @author Benno Schmidt
 */
public class SpotTypes
{
	/** 
	 * Available spot shapes 
	 * <table>
	 * <th>Shape value<td></td><td>Visual effect</td></th>
	 * <tr><tt>Circle</tt><td></td>circular shape<td></td></tr>
	 * <tr><tt>Square</tt><td></td>square shape<td></td></tr>
	 * </table>
	 */
	public enum Shape {Circle, Square};

	/** 
	 * Available spot types.
	 * <br />
	 * <table>
	 * <th>Type value<td></td><td>Visual effect</td></th>
	 * <tr><tt>SeeThrough</tt><td></td>show background image<td></td></tr>
	 * <tr><tt>Fixed label</tt><td></td>display and move fixed bitmap<td></td></tr>
	 * <tr><tt>Dynamic</tt><td></td>allow spot to change its behavior dynamically<td></td></tr>
	 * <tr><tt>Magnifier</tt><td></td>show magnified background image<td></td></tr>
	 * <tr><tt>Warp</tt><td></td>distort foreground image (experimental feature)<td></td></tr>
	 * <tr><tt>SeeThroughTail</tt><td></td>show background image and add tail-effect (performant rendering required)<td></td></tr>
	 * </table>
	 * @see LivingThing#getType()
	 */
	public enum Type {SeeThrough, FixedLabel, Dynamic, Magnifier, Warp, SeeThroughTail};
}
