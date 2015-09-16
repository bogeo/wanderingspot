package wanderingspot;

import java.io.FileNotFoundException;

/**
 * Scene definition
 * 
 * @author Benno Schmidt
 */
public class SceneConfig 
{
	// TODO Read parameters from an XML configuration file.

	public String backgroundImageFile = null; 
	public String spotImageFile = null;
	public String lensImageFile = null;
	public String fixedLabelImageFile = "./images/bo-logo.png";
	public String introImageFile = "./images/intro.png";
	public long threadSleepTime = 10; // Refresh delay-time (given in msecs) //
	// TODO: Control parameter threadSleepTime dynamically to get smoother images!?
	public long introDuration = 1500; // Intro-image display time (given in msecs)
	/**
	 * Single-spot mode allows more efficient rendering, but processes one spot only.
	 */
	public boolean singleSpotMode = false; 
	protected SpotConfig[] spots = null; 

	public long dynamicFixedLabelDisplayDuration = 1800; // TODO!! (per spot)
	public long dynamicFixedLabelRepeatCycle = 10323; // TODO!! (per spot)
	
	protected  Annotation annotation = null; 
	
	// TODO Some parameters should be configurable for each spot individually!

	protected SceneConfig() {
	}

	/**
	 * Constructor
	 * 
	 * @param filename XML configuration file name (with path)
	 */
	public SceneConfig(String filename) throws FileNotFoundException 
	{
		throw new FileNotFoundException("This method has not been implemented yet!"); // TODO
	}
	
	/** 
	 * get the initial configurations of the scene's spots.
	 *  
	 * @return Array of spots definitions
	 */
	public SpotConfig[] getSpots() {
		return spots;
	}
}
