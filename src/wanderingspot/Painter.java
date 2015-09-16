package wanderingspot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * The painter is responsible for image rendering.
 * 
 * @author Benno Schmidt
 */
public class Painter 
{
	private ScreenSize mScreenSize;

	// For use with a single SceneConfig:
	protected SceneConfig mConf;
	protected LivingThing[] mLivingThings; 

	// For use with a SceneConfig-sequence:
	protected SceneSequence mSeq;
	protected LivingThing[][] mAllLivingThings; 

	protected BufferedImage mBackLayer; // Background layer 
	protected BufferedImage mForeLayer; // Foreground layer holding the spot's image content
	protected BufferedImage mLensLayer; // Layer used for magnifying-glass effect
	protected BufferedImage mSpotLayer; // Spot layer 
	protected BufferedImage mFixedLabelImage; // Fixed label image (optional)
	protected short[][] mTailEffectMaskLayer = null;

	public Painter(SceneSequence p, SceneConfig conf, ScreenSize screenSize) 
	{
		this.mSeq = p;
		this.mConf = conf;
		this.mScreenSize = screenSize;
		
		if (p == null) 
		{ 
			// -> use with a single SceneConfig

			// Create basic image layers:
			try {
				mBackLayer = ImageIO.read(new File(mConf.backgroundImageFile));
				mForeLayer = ImageIO.read(new File(mConf.spotImageFile));
				mLensLayer = ImageIO.read(new File(mConf.lensImageFile));
				mSpotLayer = new BufferedImage(mScreenSize.width, mScreenSize.height, BufferedImage.TYPE_INT_RGB);
				if (mConf.fixedLabelImageFile != null)
					mFixedLabelImage = ImageIO.read(new File(mConf.fixedLabelImageFile));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}

			// Instantiate spots:
			mLivingThings = new LivingThing[mConf.getSpots().length];
			for (int i = 0; i < mConf.getSpots().length; i++) {
				mLivingThings[i] = new LivingThing(mConf.getSpots()[i], mScreenSize);
			}
		} 
		else { 			
			// -> use with a SceneConfig-sequence

			int N = this.mSeq.getSequenceElements().length;
			
			// Create basic image layers (will be done later in paintNSeq()-method):
			try {
				mBackLayer = null; 
				mForeLayer = null;
//				mBackLayer = ImageIO.read(new File(mConf.backgroundImageFile));
//				mForeLayer = ImageIO.read(new File(mConf.spotImageFile));
//				mLensLayer = ImageIO.read(new File(mConf.lensImageFile));
				mSpotLayer = new BufferedImage(mScreenSize.width, mScreenSize.height, BufferedImage.TYPE_INT_RGB);
				if (mConf != null && mConf.fixedLabelImageFile != null) {
					mFixedLabelImage = ImageIO.read(new File(mConf.fixedLabelImageFile));
				}
				mTailEffectMaskLayer = new short[mScreenSize.width][mScreenSize.height]; // TODO this should not be created, if no tail effects are part of a scene 
				for (int i = 0; i < mScreenSize.width; i++) {
					for (int j = 0; j < mScreenSize.height; j++) {
						mTailEffectMaskLayer[i][j] = 0;
					}	
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}

			// Instantiate spots:
			mAllLivingThings = new LivingThing[N][];
			for (int i = 0; i < N; i++) {
				SceneConfig s = this.mSeq.getSequenceElement(i).getScene();
				if (s.getSpots() != null) {
					mAllLivingThings[i] = new LivingThing[s.getSpots().length];
					for (int j = 0; j < s.getSpots().length; j++) {
						mAllLivingThings[i][j] = new LivingThing(s.getSpots()[j], mScreenSize);
					}			
				}
			}
		}
			
		System.out.println(
					mConf.singleSpotMode 
					? "Application has been initialized (single-spot mode)."
					: "Application has been initialized (multi-spot mode).");
	}

	public void paint1(Graphics g) 
	{
		LivingThing livingThing = mLivingThings[0];
		int spotSize = livingThing.getSize();
		int x = livingThing.getPosition().x;
		int y = livingThing.getPosition().y;

		// Note that the following algorithm works for a single spot (LivingThing) only.

		for (int ix = -spotSize; ix <= +spotSize; ix++) {
			for (int iy = -spotSize; iy <= +spotSize; iy++) {
				if (! (x + ix < 0 || x + ix >= mScreenSize.width || y + iy < 0 || y + iy >= mScreenSize.height)) {
					boolean inside = false;
					switch (livingThing.getSpotDef().getShape()) { 
						case Circle:
							if (ix * ix + iy * iy < spotSize * spotSize) 
								inside = true;
							break;
						case Square:
							inside = true; // always true!
							break;
						default:
							inside = true; 
							break;
					}
					if (inside) {
						mSpotLayer.setRGB(x + ix, y + iy, this.getRGB(mForeLayer, x + ix, y + iy));					
					} else {
						mSpotLayer.setRGB(x + ix, y + iy, this.getRGB(mBackLayer, x + ix, y + iy));					
					}
				}
			}
		}

		g.drawImage(mBackLayer, 0, 0, null);
		boolean activeAnnotation = false;
		if (mConf.annotation != null) {
			activeAnnotation = mConf.annotation.drawAnnotation(g);
		}
		g.clipRect(x - spotSize, y - spotSize, 2 * spotSize + 1, 2 * spotSize + 1);
		g.drawImage(mSpotLayer, 0, 0, null);
		if (activeAnnotation) { 
			mConf.annotation.drawAnnotation(g);
		}	

		// "Staying alive":
		livingThing.justLive();
	}

	private int getRGB(BufferedImage layer, int x, int y) {
		// Helper function preventing from ArrayIndexOutOfBoundsExceptions...
		if (x < 0 || x >= layer.getWidth() || y < 0 || y >= layer.getHeight())
			return 0;
		return layer.getRGB(x, y);
	}
	
	public void paintN(Graphics g) 
	{
		// Algorithm for > 1 spots (LivingThings)

		if (mLivingThings == null || mLivingThings.length < 1) {
			g.drawImage(mBackLayer, 0, 0, null);	
			return; // TODO Should be checked when reading-in the scene configuration... 
		}
		
		// Help variables to keep code short:
		int N = mLivingThings.length;
		int[] spotSize = new int[N];
		int[] x = new int[N];
		int[] y = new int[N];
		for (int i = 0; i < N; i++) {
			spotSize[i] = mLivingThings[i].getSize();
			x[i] = mLivingThings[i].getPosition().x;
			y[i] = mLivingThings[i].getPosition().y;			
		}

		// First, determine affected clip rectangle:
		ClipRectangle clipArea = this.determineClipArea(x, y, spotSize);		

		// Initially, set background values for all pixels inside the clipping area:
		for (int ix = clipArea.x; ix <= clipArea.x + clipArea.w; ix++) {
			for (int iy = clipArea.y; iy <= clipArea.y + clipArea.h; iy++) {
				mSpotLayer.setRGB(ix, iy, this.getRGB(mBackLayer, ix, iy));
			}
		}
		boolean hasTailEffect = false;

		// Then determine pixels inside the spots:		
		for (int i = 0; i < N; i++) 
		{
			// Note: Spots are rendering according to the "painter's model".
			// Thus, rendering order can be defined in the scene configuration.
			
			for (int ix = -spotSize[i]; ix <= +spotSize[i]; ix++) {
				for (int iy = -spotSize[i]; iy <= +spotSize[i]; iy++) {
					if (! (x[i] + ix < 0 || x[i] + ix >= mScreenSize.width || y[i] + iy < 0 || y[i] + iy >= mScreenSize.height)) {
						boolean inside = false;
						switch (mLivingThings[i].getSpotDef().getShape()) {
							case Circle:
								if (ix * ix + iy * iy < spotSize[i] * spotSize[i]) 
									inside = true;
								break;
							case Square:
								inside = true; // always true!
								break;
							default:
								inside = true; 
								break;
						}
						if (inside) {
							SpotTypes.Type type = mLivingThings[i].getType();
							try {
								switch (type) {
									case SeeThrough: 
										mSpotLayer.setRGB(x[i] + ix, y[i] + iy, 
												mForeLayer.getRGB(x[i] + ix, y[i] + iy));					
										mSpotLayer.setRGB(x[i] + ix, y[i] + iy, 
												this.getRGB(mForeLayer, x[i] + ix, y[i] + iy));					
										break;
									case FixedLabel:
										mSpotLayer.setRGB(x[i] + ix, y[i] + iy, 
												this.getRGB(mFixedLabelImage, ix + spotSize[i], iy + spotSize[i])); 
										break;
									case Magnifier:
										mSpotLayer.setRGB(x[i] + ix, y[i] + iy, 
												this.getRGB(mLensLayer, 2 * x[i] + ix, 2 * y[i] + iy));					
										// TODO: This is still experimental (and will not work for circles):
										if (mLivingThings[i].getSpotDef().getShape() == SpotTypes.Shape.Square) {
											if (ix == -spotSize[i] || ix == +spotSize[i] - 1 || iy == -spotSize[i] || iy == +spotSize[i] - 1) {
												mSpotLayer.setRGB(x[i] + ix, y[i] + iy, (255 << 24) | (0 << 16) | (0 << 8) | 255); // blue 
											}
										}
										break;
									case Warp:
										// Experimental implementation:
										double dist = Math.sqrt((double)(ix * ix) + (double)(iy * iy));
										mSpotLayer.setRGB(x[i] + ix, y[i] + iy, 
												this.getRGB(mBackLayer, x[i] + ix, y[i] + iy));					
										if (dist <= spotSize[i]) {
											double f = 1.75 - 0.75 * Math.abs(dist) / (double)spotSize[i]; 
											mSpotLayer.setRGB(x[i] + ix, y[i] + iy, 
													this.getRGB(mBackLayer, 
															x[i] + (int)(f * (double)ix), 
															y[i] + (int)(f * (double)iy))); 
										}					
										break;
									case SeeThroughTail:
										if (ix * ix + iy * iy < spotSize[i] * spotSize[i] * 80 / 100) 
										mTailEffectMaskLayer[x[i] + ix][y[i] + iy] = 250;
										else {
											mTailEffectMaskLayer[x[i] + ix][y[i] + iy] += 175;
											if (mTailEffectMaskLayer[x[i] + ix][y[i] + iy] > 250)
												mTailEffectMaskLayer[x[i] + ix][y[i] + iy] = 250;
										}
										hasTailEffect = true;
										break;
									default: 
										// TODO
										break;
								}
							} catch (ArrayIndexOutOfBoundsException e) {
								; // System.out.println(e);
							}
						}
					}
				}
			}
			
			if (mLivingThings[i].getType() == SpotTypes.Type.SeeThroughTail) {
				for (int iix = 0; iix < this.mScreenSize.width; iix++) {
					for (int iiy = 0; iiy < this.mScreenSize.height; iiy++) {
						short val = mTailEffectMaskLayer[iix][iiy];
						if (val > 0) mTailEffectMaskLayer[iix][iiy] -= 20;
						if (val < 10) mSpotLayer.setRGB(iix, iiy, this.getRGB(mBackLayer, iix, iiy)); // TODO can be optimized; better use break-statement
						if (val > 100) mSpotLayer.setRGB(iix, iiy, this.getRGB(mForeLayer, iix, iiy));
						else {
							if (val < 0) val = 0;
							int c1 = mForeLayer.getRGB(iix, iiy);
							int a1 = (c1 & 0xff000000) >> 24;
							int r1 = (c1 & 0xff0000) >> 16;
							int g1 = (c1 & 0xff00) >> 8;
							int b1 = c1 & 0xff;
							int c2 = mBackLayer.getRGB(iix, iiy);
							int a2 = (c2 & 0xff000000) >> 24;
							int r2 = (c2 & 0xff0000) >> 16;
							int g2 = (c2 & 0xff00) >> 8;
							int b2 = c2 & 0xff;
							float f2 = ((float)val)/100.f;
							float f1 = 1.f - f2;
							int a = (int)(f1 * (float)a2 + f2 * (float)a1);
							int r = (int)(f1 * (float)r2 + f2 * (float)r1);
							int gx= (int)(f1 * (float)g2 + f2 * (float)g1);
							int b = (int)(f1 * (float)b2 + f2 * (float)b1);
							int c = (a << 24) | (r << 16) | (gx << 8) | b;
							mSpotLayer.setRGB(iix, iiy, c);
						}
					} // TODO 
					//TODO 1. Maybe it would be better to determine the relevant BBOX permanently / limit the calculation area 
					// 2. Would it be advantageous to process groups of 4 pixels instead of single pixels (speed-up factor 16?)?
					// 3. Maybe for SingleMode this could be optimized (do not paint the complete back-image, but limit processing
					// to the last recent clip-rectangle... ???
				}
			}
		}

		// Draw resulting image:
		g.drawImage(mBackLayer, 0, 0, null);	
		boolean activeAnnotation = false;
		if (mConf.annotation != null) {
			activeAnnotation = mConf.annotation.drawAnnotation(g);
		}
		if (! hasTailEffect)
			g.clipRect(clipArea.x, clipArea.y, clipArea.w, clipArea.h);
		g.drawImage(mSpotLayer, 0, 0, null);
		if (activeAnnotation) { 
			mConf.annotation.drawAnnotation(g);
		}	

		// "Staying alive":
		for (int i = 0; i < N; i++)
			mLivingThings[i].justLive();
	}

	static int lastSeqElem = -1;
	
	public void paintNSeq(Graphics g) 
	{
		int i = mSeq.getCurrentSceneIndex();
		if (i < 0) return;
		
		if (i != lastSeqElem) 
		{
			// Context switch:
			
			this.mLivingThings = this.mAllLivingThings[i];
			SceneConfig s = mSeq.getSequenceElement(i).getScene();
			this.mConf = s;
			
			// Create basic image layers:
			try {
				mBackLayer = ImageIO.read(new File(s.backgroundImageFile));
				mForeLayer = ImageIO.read(new File(s.spotImageFile));
				if (s.lensImageFile != null)
					mLensLayer = ImageIO.read(new File(s.lensImageFile));
				// TODO for the other layers...
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
			lastSeqElem = i;
		}
		
		this.paintN(g);
	}
	
	private ClipRectangle determineClipArea(int[] x, int[] y, int[] spotSize) 
	{
		int N = mLivingThings.length;

		int cx = x[0] - spotSize[0];
		for (int i = 0; i < N; i++) {
			cx = Math.min(cx, x[i] - spotSize[i]);		
		}
		if (cx < 0) 
			cx = 0;

		int cy = y[0] - spotSize[0];
		for (int i = 0; i < N; i++) {
			cy = Math.min(cy, y[i] - spotSize[i]);		
		}
		if (cy < 0) 
			cy = 0;

		int cx2 = x[0] + spotSize[0];
		for (int i = 0; i < N; i++) {
			cx2 = Math.max(cx2, x[i] + spotSize[i]);		
		}
		int cw = cx2 - cx;		
		if (cx + cw >= mScreenSize.width)
			cw = mScreenSize.width - 1 - cx;
		
		int cy2 = y[0] + spotSize[0];
		for (int i = 0; i < N; i++) {
			cy2 = Math.max(cy2, y[i] + spotSize[i]);		
		}
		int ch = cy2 - cy;		
		if (cy + ch >= mScreenSize.height) 
			ch = mScreenSize.height - 1 - cy;

		return new ClipRectangle(cx, cy, cw, ch);
	}
}
