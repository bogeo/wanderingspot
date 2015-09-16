package wanderingspot;

/**
 * Demo presentation 
 * 
 * @author Benno Schmidt
 */
public class DemoSceneSequence extends SceneSequence 
{
	public DemoSceneSequence() 
	{
		super();

		//this.addSequenceElement(new SceneSequenceEntry(new DemoScenes(5), 60000));

		this.addSequenceElement(new SceneSequenceEntry(new SlideConfig("./images/slide1.png"), 10000L /*duration in msecs*/));

		SceneConfig s1 = new DemoScenes(1);
		SceneSequenceEntry e1 = new SceneSequenceEntry(s1, 15000L /*duration in msecs*/);
		this.addSequenceElement(e1);

		this.addSequenceElement(new SceneSequenceEntry(new SlideConfig("./images/slide2.png"), 10000L));

		SceneConfig s2 = new DemoScenes(2);
		this.addSequenceElement(new SceneSequenceEntry(s2, 10000));

		this.addSequenceElement(new SceneSequenceEntry(new SlideConfig("./images/slide3.png"), 15000L));

		this.addSequenceElement(new SceneSequenceEntry(new SlideConfig("./images/slide4.png"), 10000L));

		this.addSequenceElement(new SceneSequenceEntry(new DemoScenes(3), 15000));

		this.addSequenceElement(new SceneSequenceEntry(new SlideConfig("./images/slide5.png"), 10000L));

		this.setRepeatMode();
	}	
}
