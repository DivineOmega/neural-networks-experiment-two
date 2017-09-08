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

import javax.swing.SwingUtilities;

import util.GenomeUtils;
import worldObjects.Creature;
import worldObjects.PlayerBullet;

public class Main 
{	
	public static MainWindow mainWindow;
	
	public static long timer = 0;
	public static long tickInterval = 0;
	
	public static long renderTimer = 0;
	private static int framesPerSecond = 60;
	
	public static long tickCounter = 0;
	public static long ticksPerGeneration = 100000;
	
	public static double highestFitnessThisGeneration = 0;
	public static double highestFitnessEver = 0;
	
	public static int populationSize = 25;
	public static ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	public static int amountOfPlayerBullets = 50;
	public static ArrayList<PlayerBullet> playerBullets = new ArrayList<PlayerBullet>();
	
	public static void main(String[] args) 
	{
		simulationSpeedReset();
		
		mainWindow = new MainWindow();
		SwingUtilities.invokeLater(mainWindow);
		
		while(creatures.size() < populationSize)
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
			render(elapsedTime);
			
			lastTime = currentTime;
		}
	}
	
	
	
	public static void update(long elapsedTime)
	{
		timer += elapsedTime;
		
		if(tickCounter>ticksPerGeneration)
		{
			if (highestFitnessThisGeneration > highestFitnessEver)
			{
				highestFitnessEver = highestFitnessThisGeneration;
			}
			
			ArrayList<Creature> newCreatures = new ArrayList<Creature>();
			
			while (newCreatures.size() < populationSize)
			{
				ArrayList<Creature> parents = rouletteWheelSelection();
				Creature childCreature = GenomeUtils.crossover(parents.get(0), parents.get(1));
				
				newCreatures.add(childCreature);
			}
			
			creatures.clear();			
			creatures.addAll(newCreatures);
			
			tickCounter = 0;
		}
		
		if (timer>tickInterval)
		{
			tickCounter++;
						
			while (playerBullets.size()<amountOfPlayerBullets)
			{
				int targetCreatureIndex = (int) (Math.random()*creatures.size());
				Creature targetCreature = creatures.get(targetCreatureIndex);

				PlayerBullet newPlayerBullet = new PlayerBullet(targetCreature);
				playerBullets.add(newPlayerBullet);
			}
			
			ArrayList<Creature> deadCreatures = new ArrayList<Creature>();
			ArrayList<PlayerBullet> destroyedPlayerBullets = new ArrayList<PlayerBullet>();
			
			highestFitnessThisGeneration = 0;
			
			for (Creature creature : creatures) 
			{
				if (creature.getFitness() > highestFitnessThisGeneration) 
				{
					highestFitnessThisGeneration = creature.getFitness();
				}
				
				double distanceToClosestPlayerBullet = Double.MAX_VALUE;
				double angleToClosestPlayerBullet = 0;
				double vectorXToClosestPlayerBullet = 0;
				double vectorYToClosestPlayerBullet = 0;
				
				Point2D creatureLocation = new Point2D.Double(creature.x, creature.y);
				
				for (PlayerBullet playerBullet : playerBullets)
				{
					Point2D playerBulletLocation = new Point2D.Double(playerBullet.x, playerBullet.y);
					
					double distanceToPlayerBullet = creatureLocation.distance(playerBulletLocation);
					
					if (distanceToPlayerBullet < distanceToClosestPlayerBullet)
					{
						distanceToClosestPlayerBullet = distanceToPlayerBullet;
						
						angleToClosestPlayerBullet = Math.atan2(playerBullet.y - creature.y, playerBullet.x - creature.x);
						
						if (angleToClosestPlayerBullet<0)
						{
							angleToClosestPlayerBullet += 2*Math.PI;
						}
						
						vectorXToClosestPlayerBullet = playerBullet.x - creature.x;
						vectorYToClosestPlayerBullet = playerBullet.y - creature.y;
					}
				}
				
				ArrayList<Double> inputs = new ArrayList<Double>();
				
				inputs.add(vectorXToClosestPlayerBullet);
				inputs.add(vectorYToClosestPlayerBullet);
				
				inputs.add(-Math.sin(creature.angle));
				inputs.add(Math.cos(creature.angle));
								
				creature.tick(inputs);
				
				for (PlayerBullet playerBullet : playerBullets) 
				{
					playerBullet.tick();
					
					if (playerBullet.x > creature.x - (creature.diameter/2) &&
						playerBullet.x < creature.x + (creature.diameter/2) &&
						playerBullet.y > creature.y - (creature.diameter/2) &&
						playerBullet.y < creature.y + (creature.diameter/2))
					{
						if (!creature.isDead()) {
							destroyedPlayerBullets.add(playerBullet);
							creature.energy -= 1.0;
						}
					}
				}
				
			}
			
			for (Creature deadCreature : deadCreatures) 
			{
				creatures.remove(deadCreature);
			}
			
			for (PlayerBullet destroyedPlayerBullet : destroyedPlayerBullets) 
			{
				playerBullets.remove(destroyedPlayerBullet);
			}
			
			destroyedPlayerBullets.clear();
			
			int numberOfDeadCreatures = 0;
			for (Creature creature : creatures) 
			{
				if (creature.isDead()) {
					numberOfDeadCreatures++;
				}
			}
			
			if (numberOfDeadCreatures >= creatures.size()) {
				tickCounter += 1000;
			}
			
			timer -= tickInterval;
		}
	}
	
	public static void render(long elapsedTime)
	{
		renderTimer += elapsedTime;
		
		// Only render a fixed number of frames per second
		if (renderTimer <= 1000 / framesPerSecond )
		{
			return;
		}
		
		renderTimer = 0;
		
		BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, 800, 800);
		
		int populationGeneration = 0;
				
		for (Creature creature : creatures) 
		{
			populationGeneration = creature.generation;
			
			if (creature.isDead()) {
				g2d.setColor(Color.black);
			}
			else if (creature.getFitness()>=highestFitnessThisGeneration)
			{
				g2d.setColor(Color.yellow);
			}
			else if (creature.getFitness()>=highestFitnessEver)
			{
				g2d.setColor(Color.orange);
			}
			else
			{
				g2d.setColor(Color.white);
			}
			
			Arc2D arc = new Arc2D.Double(creature.x-(creature.diameter/2), creature.y-(creature.diameter/2), creature.diameter, creature.diameter, 0, 360, Arc2D.OPEN);
			Line2D line = new Line2D.Double(creature.x, creature.y, creature.x + (creature.diameter/2) * Math.sin(creature.angle), creature.y + (creature.diameter/2) * Math.cos(creature.angle));			
			
			String fitnessString = Integer.toString((int) creature.getFitness());
			
			g2d.draw(arc);
			g2d.draw(line);
			g2d.drawString(fitnessString,(int) (creature.x+5+(creature.diameter/2)),(int) (creature.y+(creature.diameter/2)));
			
		}
		
		g2d.setColor(Color.red);
		for (PlayerBullet playerBullet : playerBullets) 
		{
			Arc2D arc = new Arc2D.Double(playerBullet.x-(playerBullet.diameter/2), playerBullet.y-(playerBullet.diameter/2), playerBullet.diameter, playerBullet.diameter, 0, 360, Arc2D.OPEN);
			
			g2d.draw(arc);
		}
		
		g2d.setColor(Color.cyan);
		g2d.drawString("Tick: "+tickCounter+" of "+ticksPerGeneration,10,10);
		g2d.drawString("Generation: "+populationGeneration,10,30);
		g2d.drawString("Highest fitness: gen.: "+(int)highestFitnessThisGeneration+", ever: "+(int)highestFitnessEver,10,50);
		
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
			
			for (double j = 0; j < creature.getFitness(); j+=0.1) 
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
		
		if (tickInterval<0)
		{
			tickInterval = 0;
		}
	}
	
	public static void simulationSpeedDown()
	{
		tickInterval += 1;
	}
	
	public static void simulationSpeedReset()
	{
		tickInterval = 30;
	}
	
}
