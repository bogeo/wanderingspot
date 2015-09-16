package wanderingspot;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Annotations allow to draw text to the screen canvas.
 * 
 * @author Benno Schmidt
 */
public class Annotation 
{
	/**
	 * Multi-line text content
	 */
	public String[] text;
	
	/**
	 * Position (referring to the image pixel-coordinate system)
	 */
	public Position pos;
	
	/**
	 * Text color
	 */
	public Color color;
	
	/**
	 * Cycle interval for text visibility (given in msecs)
	 */
	public long cycleInterval;

	/**
	 * Duration for text visibility (given in msecs). A value < 0 indicates permanent annotation display.
	 */
	public long showDuration;

	public Annotation(
	        String[] text, 
	        Position pos, 
	        Color color) 
	{
		this.text = text;
		this.pos = pos;
		this.color = color;
		this.showDuration = -1;
	}	

	public Annotation(
			String[] text, 
			Position pos, 
			Color color,
			long cycleInterval, 
			long showDuration) 
	{
		this.text = text;
		this.pos = pos;
		this.color = color;
		this.cycleInterval = cycleInterval;
		this.showDuration = showDuration;
	}	

	/**
	 * draws the annotation.
	 *  
	 * @param g Graphics context
	 * @return <i>true<(i>, if annotation is active, else <i>false</i>
	 */
	public boolean drawAnnotation(Graphics g) 
	{
		if (this.showDuration < 0 || (
				System.currentTimeMillis() % this.cycleInterval > this.showDuration)) 
		{ 
			if (this.text != null) {
				g.setColor(this.color);
				int y = this.pos.y;
				for (String line : this.text) {
					g.drawString(line, this.pos.x, y);
					y += 15;
				}
				return true;
			}
		}
		return false;
	}
}	
