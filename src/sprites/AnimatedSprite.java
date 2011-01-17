package sprites;

import java.awt.image.BufferedImage;

import util.Timer;

/**
 * The Animated Sprite takes in an array of images and rotates through them in order
 * to create an animation.
 * @author Tim Butram
 *
 */
@SuppressWarnings("serial")
public class AnimatedSprite extends Sprite
{
	public final int serial = 3;
	
	protected BufferedImage[] images = null;
	protected Timer time = new Timer(200);
	
	protected int currentImg = 0;
	
	/**
	 * Creates a default AnimatedSprite.  This sprite has no animation as it is
	 * simple a null image.
	 */
	public AnimatedSprite()
	{
		super();
	}
	
	public AnimatedSprite(BufferedImage image, int x, int y, int layer)
	{
		super(image, x, y, layer);
		
		images = new BufferedImage[1];
		
		images[0] = image;
	}
	
	/**
	 * Creates a new Animated Sprite with the animation being defined as the array of
	 * Images.  Each frame persists for 200 milliseconds (1/5th of a second).
	 * @param img - The array of images the Animated Sprite will flip through. 
	 * @param x - The initial X coordinate of the Sprite.
	 * @param y - The initial Y coordinate of the Sprite.
	 * @param layer - The initial Layer position of the Sprite.
	 */
	public AnimatedSprite(BufferedImage[] img, int x, int y, int layer)
	{
		super(img[0], x, y, layer);
		
		this.images = img;
	}
	
	/**
	 * Creates a new Animated Sprite with the animation being defined as the array of
	 * Images.  Each from persists for the user specified time of <i>time</i>
	 * @param img - The array of images the Animated Sprite will flip through.
	 * @param x - The initial X coordinate of the Sprite.
	 * @param y - The initial Y coordinate of the Sprite.
	 * @param layer - The initial Layer position of the Sprite.
	 * @param time - The period of time that each from persists.
	 */
	public AnimatedSprite(BufferedImage[] img, int x, int y, int layer, long time)
	{
		super(img[0], x, y, layer);
		
		this.images = img;
		this.time = new Timer(time);
	}
	
	/**
	 * Sets a new animation sequence for the Animated Sprite.
	 * @param im - The array of images to replace the old array.
	 */
	public void setSprite(BufferedImage[] im)
	{
		images = im;
	}
	
	public BufferedImage print() 
	{
		if(this.time.hasRung())
		{
			nextFrame();
		}
		
		return images[currentImg];
	}
	
	private void nextFrame()
	{
		currentImg++;
		
		if(currentImg >= images.length)
		{
			currentImg = 0;
		}
	}
}
