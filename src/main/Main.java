package main;

import java.awt.Color;
import java.awt.Graphics;
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
				creature.testRandomMovement();
			}
			moveTimer = 0;
		}
	}
	
	public static void render()
	{
		BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = image.getGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 600);
		
		g.setColor(Color.black);
		for (Creature creature : creatures) 
		{
			g.drawRect(creature.x-5, creature.y-5, 10, 10);
		}
		
		mainWindow.drawPane.image = image;
		mainWindow.repaint();
	}

}
