package gui;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Dimension;
import java.io.IOException;
import java.util.LinkedList;

import level.Room;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import sprites.Sprite;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

/**
 * This is the main class for the Fluxware Game Engine.  This class handles and maintains the Windowing System.
 * @author Fluxware
 *
 */
public class Game
{
	//Sets the default size of the window that the game is displayed in.
	private Dimension size = new Dimension(800, 600);

	//Sets whether the window will display in fullscreen. Default false.
	private boolean fullscreen = false;

	//The current room that the game is displaying.
	private Room room = null;

	//The title of the window. Defaults to 'Fluxware Game Engine'
	private String title = "Fluxware Game Engine";

	private static long ticksPerSecond = Sys.getTimerResolution();
	private long delta = 0;
	private long lastLoopTime = 0;
	private int framesPerSecond = 60;
	
	protected boolean isRunning = true;

	//Each of the differing managers.
	//SoundManager
	//CollisionManager
	GUI gui;
	Widget root = new Widget();
	/**
	 * Creates a Game with the given Room. Uses defaults of 800x600 and not in fullscreen.
	 * @param room
	 */
	public Game(Room room)
	{
		this.room = room;
		construct();
	}

	/**
	 * Creates a new Game based on the given room and if it should be fullscreen or not.
	 * @param room - The room to be displayed.
	 * @param fullscreen - True to enable fullscreen. 
	 */
	public Game(Room room, boolean fullscreen)
	{	
		this.room = room;
		this.fullscreen = fullscreen;
		construct();
	}

	/**
	 * Creates a new Game based upon the Room given to it.
	 * @param room - The room the Game will start displaying.
	 * @param fullscreen - true if the game is fullscreen.
	 * @param size - The resolution of the game.
	 */
	public Game(Room room, boolean fullscreen, Dimension size)
	{
		this.room = room;
		this.fullscreen = fullscreen;
		this.size = size;
		construct();
	}

	/**
	 * Creates a new Game based upon the Room given to it.
	 * @param room - The Room the Game will start displaying.
	 * @param fullscreen - true if the game is to be in fullscreen.
	 * @param size - The resolution of the game.
	 * @param title - The title of the window, if not fullscreen.
	 */
	public Game(Room room, boolean fullscreen, Dimension size, String title)
	{
		this.room = room;
		this.fullscreen = fullscreen;
		this.size = size;
		this.title = title;
		construct();
	}


	/**
	 * Sets a new Room as the default, generally used when changing rooms or levels.
	 * @param room
	 */
	public void setRoom(Room room)
	{
		this.room = room;
	}

	/**
	 * Returns the current Room
	 */
	public Room getRoom()
	{
		return room;
	}

	public long getDelta()
	{
		return delta;
	}

	public long getTime()
	{
		return (Sys.getTime() * 1000) / ticksPerSecond;
	}
	
	public void setFramesPerSecond(int fps)
	{
		framesPerSecond = fps;
	}
	
	public int getFramesPerSecond()
	{
		return framesPerSecond;
	}
	
	public void addWidget(Widget w)
	{
		root.add(w);
	}

	private void gameLoop()
	{
		while(isRunning)
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();

			drawFrame();

			gui.update();
			Display.update();
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
				isRunning = false;
		}
		gui.destroy();
		Display.destroy();
	}

	public void drawFrame()
	{
		Display.sync(framesPerSecond);
		
		delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		LinkedList<Sprite> as = room.getSprites();

		for(Sprite s: as) //Move
		{
			s.move(delta);
		}
		
//		CollisionManager.checkBoundingBoxCollisions(as);
//		CollisionManager.checkPixelPerfectCollisions(as);

		for(Sprite s: as) //Logic
		{
			s.logic();
		}

		for(Sprite s: as) //Draw
		{
			s.draw();
		}
	}
	
	public void startGame()
	{
		gameLoop();
	}

	/*
	 * The standard default method calls that are needed for every constructor.
	 */
	private void construct()
	{
		try
		{
			lastLoopTime = getTime();
			setDisplayMode();
			Display.setTitle(title);
			Display.setFullscreen(fullscreen);
			Display.create();
			
			LWJGLRenderer render = new LWJGLRenderer(); 
			gui = new GUI(root, render);
			
			ThemeManager theme = ThemeManager.createThemeManager(Game.class.getResource("/tests/resources/gui/gameui.xml"), render);
			root.setTheme("gameuidemo");
			gui.applyTheme(theme);

			Mouse.setGrabbed(true);

			glEnable(GL_TEXTURE_2D); //Enable textures in 2D mode.
			glDisable(GL_DEPTH_TEST); //Disable depth test as we are only doing 2D items.
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();

			glOrtho(0, size.width, size.height, 0, -1, 1);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glViewport(0, 0, size.width, size.height);
		}
		catch(LWJGLException e)
		{
			System.err.println("Game Exiting - Error in initialization: ");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Sets the display mode for fullscreen if possible.
	private void setDisplayMode()
	{
		try
		{
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(size.width, size.height, -1, -1, -1, -1, 60, 60);

			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
					"width="+size.width,
					"height="+size.height,
					"freq=60",
					"bpp="+org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Unable to enter into fullscreen, continuing in windowed mode.");
		}
	}
}
