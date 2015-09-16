package wanderingspot;

/**
 * Configuration to define a static image showing some content. E.g., you might want to display a screenshot 
 * of a Powerpoint slide for some time inside a scene sequence. This class allows you to define the image
 * content for this slide.
 *   
 * @author Benno Schmidt
 */
public class SlideConfig extends SceneConfig 
{
	public SlideConfig(String imageFile) 
	{
		super();
		
		// This is a very dirty hack:
		backgroundImageFile = imageFile; 
		spotImageFile = imageFile;
		
		lensImageFile = null;
		fixedLabelImageFile = null;
		introImageFile = null;
		spots = null; 
		annotation = null; 
	}
}
