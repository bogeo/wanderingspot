package wanderingspot;

import java.awt.Color;

/**
 * Demo and test scenes
 * 
 * @author Benno Schmidt
 */
public class DemoScenes extends SceneConfig 
{
	/**
	 * Constructor
	 * 
	 * @param number Scene number
	 */
	public DemoScenes(int number) 
	{
		super();
		
		switch (number) {
			case 1: demo1(); break;
			case 2: demo2(); break;
			case 3: demo3(); break;
			case 4: demo4(); break;
			case 5: demo5(); break;
			default:
				System.out.println("Sorry, demo #" + number + " is not present!");
				break;
		}
	}

	private void demo1() 
	{
		// Scene consisting of a single, circular "see-through" spot.

		this.spots = new SpotConfig[1];

		this.spots[0] = new SpotConfig(
				SpotTypes.Shape.Circle, 
				SpotTypes.Type.SeeThrough, 
				50, // size in pixels (circle radius)
				new Position(512, 400), // initial position
				270.f, // initial wandering direction 
				false,  
				true, // cushion-behavior 
				false); 

		this.singleSpotMode = true;

		this.spotImageFile = "./images/EarthAtNight_dymax_small.png"; 
		this.backgroundImageFile = "./images/LandUseCoverage_dymax_small.png";

		this.annotation = new Annotation(
				new String[]{
						"Map layers: Earth at night / land-use classification (NASA)", 
						"Map projection: Buckminster Fuller's Dymaxion (performed by hsbo-geo)"},
				new Position(1400,60),
				new Color(255,255,128),
				20000,
				6000);
	}

	private void demo2() 
	{
		// A richer scene with three different spots types.
		
		this.singleSpotMode = false;

		this.spots = new SpotConfig[3];

		this.spots[0] = new SpotConfig(
				SpotTypes.Shape.Circle, 
				SpotTypes.Type.SeeThrough, 
				50, // size
				null, // position (centered)
				240.f, // direction
				true, // pulsating
				false, // no cushion-behavior 
				false); 

		this.spots[1] = new SpotConfig(
				SpotTypes.Shape.Circle, 
				SpotTypes.Type.Dynamic, // show fixed label sometimes
				60, 
				null, 
				120.f, 
				false, // not pulsating 
				true, // cushion-behavior 
				false); 
				
		this.spots[2] = new SpotConfig(
				SpotTypes.Shape.Square, 
				SpotTypes.Type.Magnifier, 
				60, 
				null, // initial position is irrelevant for followsMousePointer = true
				0.f, 
				false, 
				true, // cushion-behavior 
				false);
				//true); // follows mouse-pointer
	
		this.backgroundImageFile = "./images/EarthAtNight_dymax.png"; 
		this.spotImageFile = "./images/LandUseCoverage_dymax.png";
		this.lensImageFile = "./images/LandUseCoverage_dymax_x2.png";

		this.annotation = new Annotation(
				new String[]{
						"Map layers: Earth at night / land-use classification (NASA)", 
						"Map projection: Buckminster Fuller's Dymaxion (performed by hsbo-geo)"},
				new Position(1400,60),
				new Color(255,255,128),
				20000,
				6000);
	}

	private void demo3() 
	{
		// Warp test, very experimental... 
		
		this.backgroundImageFile = "./images/LandUseCoverage_dymax_small.png"; 
		this.spotImageFile = "./images/SeaLevelRise6m_dymax_small.png";

		this.singleSpotMode = false;

		this.spots = new SpotConfig[1];

		this.spots[0] = new SpotConfig(
				SpotTypes.Shape.Circle, 
				SpotTypes.Type.Warp, // TODO works in multi-spot mode only
				220, 
				null,  
				180.f, 
				false, 
				true, // works not well, if no cushion-behavior 
				false); 

		this.threadSleepTime = 0L;
		
		this.annotation = new Annotation(
				new String[]{
						"WanderingSpot daemon has been started.", 
						"Return to the Windows desktop by [Alt]+[Tab]."},
				new Position(1200,60),
				new Color(255,255,128));
	}
	
	private void demo4() 
	{
		// Intro image... 
		
		this.singleSpotMode = true;
		this.spots = null;
		this.threadSleepTime = 0L;
	}

	private void demo5() 
	{
		// "Poster effect" test (i.e., a pointing-device-controlled see-through tail)
		
		this.backgroundImageFile = "./images/back.jpg"; 
		this.spotImageFile = "./images/fore.jpg";

		this.singleSpotMode = false;
		this.threadSleepTime = 0L;
		
		this.spots = new SpotConfig[1];

		this.spots[0] = new SpotConfig(
				SpotTypes.Shape.Circle, 
				SpotTypes.Type.SeeThroughTail, // TODO works in multi-spot mode only
				50, 
				null,  
				42.f,  
				false, 
				false, // works not well, if no cushion-behavior 
				true); // must be true to realize "Poster effect" 

		/*
		this.spots[1] = new SpotConfig(
				SpotTypes.Shape.Circle, 
				SpotTypes.Type.SeeThrough, 
				50, // size
				null, // position (centered)
				240.f, // direction
				false, // pulsating
				true, // no cushion-behavior 
				false); 
		*/
	}
}
