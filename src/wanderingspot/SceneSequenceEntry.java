package wanderingspot;

/**
 * Scene-sequence entry
 * 
 * @see SceneSequence
 * @author Benno Schmidt
 */
public class SceneSequenceEntry 
{
	private SceneConfig scene;
	private long duration;
	
	// TODO This class is not yet used yet! Maybe we need another main-App...

	/**
	 * Constructor
	 * 
	 * @param scene Scene definition
	 * @param duration Display duration (in msecs)
	 */
	public SceneSequenceEntry(SceneConfig scene, long duration) {
		this.scene = scene;
		this.duration = duration;
	}
	
	public SceneConfig getScene() {
		return scene;
	}

	public long getDuration() {
		return duration;
	}
}
