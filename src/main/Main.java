package main;

import gui.MainWindow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import util.GenomeUtils;
import worldObjects.Creature;
import worldObjects.FoodPellet;

public class Main 
{	
	public static MainWindow mainWindow;
	
	public static long timer = 0;
	public static long tickInterval = 0;
	
	public static int populationSize = 30;
	public static ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	public static int amountOfFood = 20;
	public static ArrayList<FoodPellet> foodPellets = new ArrayList<FoodPellet>();
	
	public static void main(String[] args) 
	{
		simulationSpeedReset();
		
		mainWindow = new MainWindow();
		mainWindow.setVisible(true);
						
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
	
	
	
	public static void update(long elapsedTime)
	{
		timer += elapsedTime;
		
		if (timer>tickInterval)
		{
			if (creatures.size() == 0)
			{
				while(creatures.size() < populationSize)
				{
					Creature newCreature = new Creature();
					creatures.add(newCreature);
				}
			}
			else if (creatures.size() < populationSize)
			{
				ArrayList<Creature> newCreatures = new ArrayList<Creature>();
								
				while (newCreatures.size() < populationSize - creatures.size())
				{
					ArrayList<Creature> parents = rouletteWheelSelection();
					Creature childCreature = GenomeUtils.crossover(parents.get(0), parents.get(1));
					Creature secondChildCreature = GenomeUtils.crossover(parents.get(1), parents.get(0));
					
					newCreatures.add(childCreature);
					newCreatures.add(secondChildCreature);
				}
				
				creatures.addAll(newCreatures);
			}
			
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
				
				double distanceToClosestFood = Double.MAX_VALUE;
				double angleToClosestFood = 0;
				double vectorXToClosestFood = 0;
				double vectorYToClosestFood = 0;
				
				Point2D creatureLocation = new Point2D.Double(creature.x, creature.y);
				
				for (FoodPellet foodPellet : foodPellets)
				{
					Point2D foodLocation = new Point2D.Double(foodPellet.x, foodPellet.y);
					
					double distanceToFood = creatureLocation.distance(foodLocation);
					
					if (distanceToFood < distanceToClosestFood)
					{
						distanceToClosestFood = distanceToFood;
						
						angleToClosestFood = Math.atan2(foodPellet.y - creature.y, foodPellet.x - creature.x);
						
						if (angleToClosestFood<0)
						{
							angleToClosestFood += 2*Math.PI;
						}
						
						vectorXToClosestFood = foodPellet.x - creature.x;
						vectorYToClosestFood = foodPellet.y - creature.y;
					}
				}
				
				ArrayList<Double> inputs = new ArrayList<Double>();
				
				inputs.add(vectorXToClosestFood);
				inputs.add(vectorYToClosestFood);
				
				inputs.add(-Math.sin(creature.angle));
				inputs.add(Math.cos(creature.angle));
								
				creature.tick(inputs);
				
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
		BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, 800, 800);
		
		
		for (Creature creature : creatures) 
		{
			if (creature.lifeSpan>creature.oldAge)
			{
				g2d.setColor(Color.red);
			}
			else
			{
				g2d.setColor(Color.white);
			}
			
			Arc2D arc = new Arc2D.Double(creature.x-(creature.diameter/2), creature.y-(creature.diameter/2), creature.diameter, creature.diameter, 0, 360, Arc2D.OPEN);
			Line2D line = new Line2D.Double(creature.x, creature.y, creature.x + (creature.diameter/2) * Math.sin(creature.angle), creature.y + (creature.diameter/2) * Math.cos(creature.angle));			
			
			String energyString = Double.toString(creature.energy);
			if (energyString.length()>4) {
				energyString = energyString.substring(0, 4);
			}
			
			String genString = Integer.toString(creature.generation);
			
			g2d.draw(arc);
			g2d.draw(line);
			g2d.drawString(energyString,(int) (creature.x+(creature.diameter/2)),(int) (creature.y+(creature.diameter/2)));
			
			g2d.drawString(genString,(int) (creature.x+(creature.diameter/2)),(int) (creature.y-(creature.diameter/2)));
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
	
	public static ArrayList<Creature> rouletteWheelSelection()
	{
		ArrayList<Creature> selectedCreatures = new ArrayList<Creature>();
		
		ArrayList<Integer> routletteWheel = new ArrayList<Integer>();
		
		for (int i = 0; i < creatures.size(); i++)
		{
			Creature creature = creatures.get(i);
			
			for (double j = 0; j < creature.energy; j+=0.1) 
			{
				routletteWheel.add(i);
				
				if (j>=1000)
				{
					break;
				}
			}
		}
		
		Collections.shuffle(routletteWheel);
		
		selectedCreatures.add(creatures.get(routletteWheel.get(0)));
		selectedCreatures.add(creatures.get(routletteWheel.get(1)));
		
		return selectedCreatures;
	}
		
	public static void simulationSpeedUp()
	{
		if (tickInterval>0)
		{
			tickInterval -= 1;
		}
		
		if (tickInterval<=0)
		{
			tickInterval = 1;
		}
	}
	
	public static void simulationSpeedDown()
	{
		tickInterval += 1;
	}
	
	public static void simulationSpeedReset()
	{
		tickInterval = 75;
	}
	
}
