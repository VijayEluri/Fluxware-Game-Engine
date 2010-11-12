package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import level.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprites.Sprite;
import util.ImageUtil;
import util.Point2D;

public class RoomTest {
	
	public Sprite a,b,c,d,e;
	public Room room;
	public LinkedList<Sprite> sprites;
	
	@Before
	public void setup()
	{
		a = new Sprite(ImageUtil.getBufferedImage("/resources/purple square.png"), 60, 60, 0);
		b = new Sprite(ImageUtil.getBufferedImage("/resources/red square.png"), 60, 0, 0);
		c = new Sprite(ImageUtil.getBufferedImage("/resources/yellow square.png"), 0, 60, 0);
		d = new Sprite(ImageUtil.getBufferedImage("/resources/green square.png"), 60, 120, 0);
		e = new Sprite(ImageUtil.getBufferedImage("/resources/blue square.png"), 120, 60, 0);
		
		a.id = 1;
		b.id = 2;
		c.id = 3;
		d.id = 4;
		e.id = 5;
		
		room = new Room(500,500,0);

		room.addSprite(a);
		room.addSprite(b);
		room.addSprite(c);
		room.addSprite(d);
		room.addSprite(e);
	}
	
	@After
	public void tearDown()
	{
		a = null;
		b = null;
		c = null;
		d = null;
		e = null;
		
		room = null;
	}
	
	@Test
	public void testGetSprites()
	{
		sprites = room.getOverlaps(new Point2D(60,60,0),a);
		for(Sprite s: sprites)
		{
			System.out.println(s.id);
		}
		assertTrue(sprites.isEmpty());
	}
	
	@Test
	public void testGetAllSprites()
	{
		sprites = room.getSprites();
		assertEquals(5, sprites.size());
	}
	
	@Test
	public void testPPC_No_Collision()
	{
		sprites = room.getCollisions(new Point2D(60,60,0), a);
		
		assertTrue(sprites.isEmpty());
	}

	@Test
	public void testPPC_TransparentOverlap()
	{

		b.setY(20);
		b.update(1,1);
		c.setX(20);
		c.update(1,1);
		d.setY(100);
		d.update(1,1);
		e.setX(100);
		e.update(1,1);

		sprites = room.getCollisions(new Point2D(60,60,0), a);
		
		assertTrue(sprites.isEmpty());
	}

	@Test 

	public void testPPC_NonTransparentOverlap()
	{

		b.setY(21);
		b.update(1,1);
		c.setX(21);
		c.update(1,1);
		d.setY(99);
		d.update(1,1);
		e.setX(99);
		e.update(1,1);

		sprites = room.getCollisions(new Point2D(60,60,0), a);

		assertEquals(4,sprites.size());
	}
	
	@Test
	public void testPPC_NonTransparentOverlap2()
	{
		b.setX(15);
		b.setY(15);
		b.update(1,1);
		
		sprites = room.getCollisions(new Point2D(10,10,0), a);
		assertEquals(1, sprites.size());
	}


}