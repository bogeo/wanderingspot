package wanderingspot;

import java.util.ArrayList;

/**
 * Sequence of scene definitions.
 * 
 * The order of scene presentation will be defined by the index <tt>i</tt>. I.e., by the order 
 * the elements have been of added to the sequence. 
 *
 * Note that one and the same scene can be added to the sequence more than on time (as a part
 * of different sequence elements).
 *
 * @author Benno Schmidt
 */
public class SceneSequence 
{
	private ArrayList<SceneSequenceEntry> entries;
	private boolean repeatMode = false;
	private long startTime;
	
	// TODO: Consider intro-image / "slide"-image handling
	
	/**
	 * Constructor. Creates an empty scene sequence. 
	 */
	public SceneSequence() {
		this.entries = null;
	}

	/**
	 * Constructor. Creates a sequence consisting of a single scene.
	 * 
	 * @param entry Scene sequence element with scene definition
	 */
	public SceneSequence(SceneSequenceEntry entry) {
		this.entries = new ArrayList<SceneSequenceEntry>();
		this.entries.add(entry);
	}

	/**
	 * Constructor. Creates a sequence consisting of multiple scenes.
	 * 
	 * @param entries Array holding scene sequence elements
	 */
	public SceneSequence(SceneSequenceEntry[] entries) {
		this.entries = new ArrayList<SceneSequenceEntry>();
		for (SceneSequenceEntry e : entries) {
			this.entries.add(e);
		}
	}

	/**
	 * adds an element to the sequence.
	 * 
	 * @param entry Scene sequence element with scene definition
	 */
	public void addSequenceElement(SceneSequenceEntry entry) {
		if (this.entries == null) {
			this.entries = new ArrayList<SceneSequenceEntry>();
			this.entries.add(entry);
			return;
		}
		this.entries.add(entry); 
	}

	/**
	 * gets all sequence elements as an array.
	 * 
	 * @return Array holding scene sequence elements
	 */
	public SceneSequenceEntry[] getSequenceElements() {
		SceneSequenceEntry[] ret = new SceneSequenceEntry[this.entries.size()];
		for (int i = 0; i < this.entries.size(); i++) {
			ret[i] = this.entries.get(i);
		}
		return ret;
	}

	/**
	 * returns the number of elements in the scene sequence.
	 * 
	 * @return Number of elements
	 */
	public int numberOfSequenceElements() {
		if (this.entries == null)
			return 0;
		return this.entries.size();
	}

	/**
	 * gets the i-th element from the scene sequence. 
	 * 
	 * Note that the condition 0 &lt;= <tt>i</tt> &lt; <tt>this.numberOfSequenceElements</tt> must hold,
	 * otherwise the return-value will be <i>null</i>.
	 * 
	 * @param i Element index inside sequence
	 * @return Scene sequence element (or <i>null</i>, if not present)
	 */
	public SceneSequenceEntry getSequenceElement(int i) {
		if (this.entries == null)
			return null;
		if (i < 0 || i >= this.entries.size())
			return null;
		return this.entries.get(i);
	}
	
	/**
	 * resets the class-internal clock to 0. This method always has to be called before
	 * querying the current scene.
	 * 
	 * @see this{@link #getCurrentScene()}
	 */
	public void resetClock() {
		this.startTime = System.currentTimeMillis();
	}
	
	/**
	 * returns the definition of the scene that currently has to be presented. 
	 * 
	 * @return Scene definition (<i>null</i>, if nothing has to be presented)
	 * @see this{@link #resetClock()}
	 */
	public SceneConfig getCurrentScene() 
	{
		// TODO Shorten code by calling getCurrentSceneIndex()!
		
		long currentTime = System.currentTimeMillis();
		long t = currentTime - startTime;
		
		int i = 0;
		long dur = 0;
		
		while (true) {
			if (i >= this.numberOfSequenceElements()) {
				if (this.repeatMode)
					i = 0;
				else
					return null;
			}
			SceneSequenceEntry e = this.entries.get(i);
			dur += e.getDuration();
			if (t < dur)
				return e.getScene();
			i++;			
		}
	}

	/**
	 * returns the index <tt>i</tt> of the scene that currently has to be presented. 
	 * 
	 * Note that the condition 0 &lt;= <tt>i</tt> &lt; <tt>this.numberOfSequenceElements</tt> must hold 
	 * for a valid scene definition.
	 * 
	 * @return Index, or -1 if nothing has to be presented
	 * 
	 * @see this{@link #resetClock()}
	 */
	public int getCurrentSceneIndex() 
	{
		long currentTime = System.currentTimeMillis();
		long t = currentTime - startTime;
		
		int i = 0;
		long dur = 0;
		
		while (true) {
			if (i >= this.numberOfSequenceElements()) {
				if (this.repeatMode)
					i = 0;
				else
					return -1;
			}
			SceneSequenceEntry e = this.entries.get(i);
			dur += e.getDuration();
			if (t < dur)
				return i;
			i++;			
		}
	}

	/**
	 * activates repeat-mode. I.e.: The scene-sequence presentation will be repeated after ending.
	 */
	public void setRepeatMode() {
		this.repeatMode = true;
	}
	
	/**
	 * sets repeat-mode to the specified value.
	 * 
	 * @param repeatMode <i>false</i> to stop the sequence presentation after ending, <i>true</i> to repeat
	 */
	public void setRepeatMode(boolean repeatMode) {
		this.repeatMode = repeatMode;
	}
	
	/**
	 * gets the value of the repeat-mode flag. 
	 *   
	 * @return <i>true</i>, if the sequence presentation will be repeated after ending, else <i>false</i> 
	 */
	public boolean isRepeatMode() {
		return repeatMode;
	}	
}
