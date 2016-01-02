package worldObjects;

import java.util.ArrayList;
import java.util.Random;

import util.GenomeUtils;

import neuralNetwork.NeuralNetwork;

public class Creature 
{
	public int generation = 1;
	
	public double x;
	public double y;
	public double angle;
	public double diameter = 15.0;	
	public double moveRate = 1.5;
	public final double maxMoveRate = 3.0;
	public double energy = 1;
	public int lifeSpan = 0;
	public final int oldAge = 100000;
	
	public NeuralNetwork neuralNetwork = new NeuralNetwork(); 
	
	public Creature() 
	{
		Random random = new Random();
		
		x = random.nextInt(800);
		y = random.nextInt(800);
		angle = random.nextDouble()*(Math.PI*2);
	}
	
	public void checkPosition()
	{
		while (x>800) 
		{
			x -= 800;
		}
		
		while (x<0) 
		{
			x += 800;
		}
		
		while (y>800) 
		{
			y -= 800;
		}
		
		while (y<0) 
		{
			y += 800;
		}
	}
	
	public void adjustAngleRandomly()
	{
		Random random = new Random();
		
		double randomOffset = -((Math.PI*2)*0.02) + (random.nextDouble()*((Math.PI*2)*0.04));
		adjustAngle(randomOffset);
	}
	
	public void adjustAngle(double offset)
	{
		angle += offset;
		
		while (angle > (2*Math.PI))
		{
			angle -= (2*Math.PI);
		}
		
		while (angle < 0)
		{
			angle += (2*Math.PI);
		}
	}
	
	public void moveForward()
	{
		x += (moveRate * Math.sin(angle));
		y += (moveRate * Math.cos(angle));
		
		checkPosition();
	}
	
	public void tick(ArrayList<Double> inputs)
	{
		ArrayList<Double> outputs = neuralNetwork.update(inputs);
		
		double angleOffset = -(Math.PI*0.1) + (outputs.get(0)*(Math.PI*0.2));
				
		adjustAngle(angleOffset);
		
		this.moveRate = outputs.get(1) * maxMoveRate;
		
		moveForward();
	}

	public void reduceEnergy()
	{
		lifeSpan++;
		
		if (lifeSpan>oldAge)
		{
			energy -= 0.1;
		}
		else
		{
			energy -= 0.001;
		}
	}
	
	public boolean isDead()
	{
		return (energy<=0);
	}
	
	public ArrayList<Double> getGenome()
	{
		ArrayList<Double> genome = new ArrayList<Double>();
		
		genome.addAll(neuralNetwork.getWeights());
		
		return genome;
	}
	
	public void setGenome(ArrayList<Double> genome)
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		
		weights.addAll(genome);
		
		neuralNetwork.setWeights(weights);
		
	}
}

