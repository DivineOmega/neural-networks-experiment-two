package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import gui.MainWindow;

public class Main 
{	
	public static MainWindow mainWindow;
	
	public static int populationSize = 30;
	public static ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	public static void main(String[] args) 
	{
		mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		
		creatures.clear();
		
		while(creatures.size()<populationSize)
		{
			Creature newCreature = new Creature();
			
			creatures.add(newCreature);
		}
		
		long lastTime = System.currentTimeMillis();
		long currentTime;
		long elapsedTime;
		
		while(true)
		{
			currentTime = System.currentTimeMillis();
			elapsedTime = currentTime - lastTime;
			
			update(elapsedTime);
			render();
			
			lastTime = currentTime;
		}
	}
	
	public static long moveTimer = 0;
	
	public static void update(long elapsedTime)
	{
		moveTimer += elapsedTime;
		
		if (moveTimer>50)
		{
			for (Creature creature : creatures) 
			{
				creature.testRandomAngle();
				creature.testMoveForward();
			}
			moveTimer = 0;
		}
	}
	
	public static void render()
	{
		BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, 600, 600);
		
		g2d.setColor(Color.black);
		for (Creature creature : creatures) 
		{
			Arc2D arc = new Arc2D.Double(creature.x-(creature.diameter/2), creature.y-(creature.diameter/2), creature.diameter, creature.diameter, 0, 360, Arc2D.OPEN);
			Line2D line = new Line2D.Double(creature.x, creature.y, creature.x + (creature.diameter/2) * Math.sin(creature.angle), creature.y + (creature.diameter/2) * Math.cos(creature.angle));			
			
			g2d.draw(arc);
			g2d.draw(line);
		}
		
		mainWindow.drawPane.image = image;
		mainWindow.repaint();
	}

}
