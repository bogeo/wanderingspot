package wanderingspot;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main application.
 *
 * The program can be started from the command line, e.g., for Windows systems:
 * <tt>
 * cd %Wanderingspot%\bin
 * PATH=C:\Program Files\Java\jdk1.6.0\bin;%PATH%
 * java wanderingspot/WanderingSpotApp
 * </tt>
 *  
 * @author Benno Schmidt
 */
@SuppressWarnings("serial")
public class WanderingSpotApp extends JPanel 
{
	static private JFrame mf;
	static public ScreenSize mScreenSize;

	public static void main(String[] a) 
	{
		System.out.println(
				"WanderingSpot v0.4 - Geovisualization Lab, Bochum Univ. of Applied Sciences");

		WanderingSpotApp.init();
		WanderingSpotApp app; 

		boolean singleScene = false;
		if (singleScene) {
			SceneConfig conf = new DemoScenes(2); 
			// TODO: Better call with XML configuration (not yet implemented): 
			// conf = new SceneConfig(a[0]); 
 				
			app = new WanderingSpotApp(conf);
		}
		else {
			SceneConfig conf = new SceneConfig(); // to obtain some default values  
			SceneSequence p = new DemoSceneSequence();
			// TODO: Again, better call with XML configuration (not yet implemented)...
			app = new WanderingSpotApp(p, conf);
		}

		mf.getContentPane().add(app);				
		app.run();
	}
	
	private static void init() 
	{	
		Toolkit tk = Toolkit.getDefaultToolkit(); 
		mScreenSize = new ScreenSize(
				(int) tk.getScreenSize().getWidth(),
				(int) tk.getScreenSize().getHeight());
		System.out.println("Canvas-size: " + 
				mScreenSize.width + "x" + mScreenSize.height + " pixels");

		mf = new JFrame();
		mf.setSize(mScreenSize.width, mScreenSize.height);		
		mf.setUndecorated(true);
		mf.setVisible(true);
	}

	private SceneConfig mConf;
	private SceneSequence mSeq;
	
	static boolean sInit = false;

	private Painter painter;
	
	/**
	 * Constructor
	 * 
	 * @param conf Scene definition
	 */
	public WanderingSpotApp(SceneConfig conf) 
	{
		this.setSize(mScreenSize.width, mScreenSize.height);
		
		this.mConf = conf;
		this.mSeq = null;

		painter = new Painter(null, conf, mScreenSize);		
	}

	/**
	 * Constructor
	 * 
	 * @param p Presentation as scene sequence
	 * @param conf TODO Should this parameter be eliminated?
	 */
	public WanderingSpotApp(SceneSequence p, SceneConfig conf) 
	{
		this.setSize(mScreenSize.width, mScreenSize.height);

		this.mConf = conf;
		this.mSeq = p;

		painter = new Painter(p, conf, mScreenSize);
	}

	protected BufferedImage mIntroImage; // Intro image (optional)

	public void run() 
	{
		try {
			if (mConf != null && mConf.introImageFile != null)
				mIntroImage = ImageIO.read(new File(mConf.introImageFile));
		} 
		catch (IOException e) {
			System.out.println("IOException: " + mConf.introImageFile);
			e.printStackTrace();
		}

		if (mIntroImage != null) {
			//System.out.println("WanderingSpot is going to show the intro image.");
			sInit = false;
			mf.repaint(); 
			try {
				Thread.sleep(mConf.introDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sInit = true;
		}
		
		if (this.mSeq != null)
			this.mSeq.resetClock();
		System.out.println("WanderingSpot daemon has been started...");

		while (true)  {
			try {
				Thread.sleep(mConf.threadSleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mf.repaint(); 
			// TODO Stopping the application by [CTRL]+[C] would be useful.
		}
	}

	public void paint(Graphics g) 
	{
		if (! sInit) {			
			g.drawImage(mIntroImage, 0, 0, null); 
			return;
		}

		// TODO Probably we will not need this feature:
		// if (System.currentTimeMillis() % 90000 < 2500) { g.drawImage(mIntroImage, 0, 0, null); return; }
	
		if (mConf.singleSpotMode) 
			painter.paint1(g);
		else {
			if (this.mSeq == null)
				painter.paintN(g);
			else
				painter.paintNSeq(g);
		}
	}
}
