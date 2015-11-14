package main;

import gui.MainWindow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import worldObjects.Creature;
import worldObjects.FoodPellet;

public class Main 
{	
	public static MainWindow mainWindow;
	
	public static int populationSize = 30;
	public static ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	public static int amountOfFood = 30;
	public static ArrayList<FoodPellet> foodPellets = new ArrayList<FoodPellet>();
	
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
	
	public static long timer = 0;
	public static long tickInterval = 50;
	
	public static void update(long elapsedTime)
	{
		timer += elapsedTime;
		
		if (timer>tickInterval)
		{
			while (foodPellets.size()<amountOfFood)
			{
				FoodPellet newFoodPellet = new FoodPellet();
				foodPellets.add(newFoodPellet);
			}
			
			ArrayList<Creature> deadCreatures = new ArrayList<Creature>();
			ArrayList<FoodPellet> eatenFoodPellets = new ArrayList<FoodPellet>();
			
			for (Creature creature : creatures) 
			{
				creature.reduceEnergy();
				
				if (creature.isDead()) {
					deadCreatures.add(creature);
					continue;
				}
				
				double xDistanceToClosest = Double.MAX_VALUE;
				double yDistanceToClosest = Double.MAX_VALUE;
				
				for (FoodPellet foodPellet : foodPellets)
				{
					double xDistance = Math.abs(foodPellet.x-creature.x);
					
					if (xDistance < xDistanceToClosest)
					{
						xDistanceToClosest = xDistance; 
					}
					
					double yDistance = Math.abs(foodPellet.y-creature.y);
					
					if (yDistance < yDistanceToClosest)
					{
						yDistanceToClosest = yDistance; 
					}
				}
				
				creature.tick(xDistanceToClosest, yDistanceToClosest);
				
				for (FoodPellet foodPellet : foodPellets) 
				{
					if (foodPellet.x > creature.x - (creature.diameter/2) &&
						foodPellet.x < creature.x + (creature.diameter/2) &&
						foodPellet.y > creature.y - (creature.diameter/2) &&
						foodPellet.y < creature.y + (creature.diameter/2))
					{
						eatenFoodPellets.add(foodPellet);
						creature.energy += 1.0;
					}
				}
			}
			
			for (Creature deadCreature : deadCreatures) 
			{
				creatures.remove(deadCreature);
			}
			
			for (FoodPellet eatenFoodPellet : eatenFoodPellets) 
			{
				foodPellets.remove(eatenFoodPellet);
			}
			
			eatenFoodPellets.clear();
			
			timer -= tickInterval;
		}
	}
	
	public static void render()
	{
		BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, 600, 600);
		
		g2d.setColor(Color.white);
		for (Creature creature : creatures) 
		{
			Arc2D arc = new Arc2D.Double(creature.x-(creature.diameter/2), creature.y-(creature.diameter/2), creature.diameter, creature.diameter, 0, 360, Arc2D.OPEN);
			Line2D line = new Line2D.Double(creature.x, creature.y, creature.x + (creature.diameter/2) * Math.sin(creature.angle), creature.y + (creature.diameter/2) * Math.cos(creature.angle));			
			
			String energyString = Double.toString(creature.energy);
			if (energyString.length()>4) {
				energyString = energyString.substring(0, 4);
			}
			
			g2d.draw(arc);
			g2d.draw(line);
			g2d.drawString(energyString,(int) (creature.x+(creature.diameter/2)),(int) (creature.y+(creature.diameter/2)));
		}
		
		g2d.setColor(Color.green);
		for (FoodPellet foodPellet : foodPellets) 
		{
			Arc2D arc = new Arc2D.Double(foodPellet.x-(foodPellet.diameter/2), foodPellet.y-(foodPellet.diameter/2), foodPellet.diameter, foodPellet.diameter, 0, 360, Arc2D.OPEN);
			
			g2d.draw(arc);
		}
		
		mainWindow.drawPane.image = image;
		mainWindow.repaint();
	}

}
