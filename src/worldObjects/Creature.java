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
	
	public NeuralNetwork neuralNetwork = new NeuralNetwork(); 
	
	public Creature() 
	{
		Random random = new Random();
		
		x = random.nextInt(600);
		y = random.nextInt(600);
		angle = random.nextDouble()*(Math.PI*2);
	}
	
	public void checkPosition()
	{
		while (x>600) 
		{
			x -= 1;
		}
		
		while (x<0) 
		{
			x += 1;
		}
		
		while (y>600) 
		{
			y -= 1;
		}
		
		while (y<0) 
		{
			y += 1;
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
		
		double angleOffset = -(2*Math.PI) + (outputs.get(0)*(4*Math.PI));
		
		adjustAngle(angleOffset);
		
		this.moveRate = outputs.get(1) * maxMoveRate;
		
		moveForward();
	}

	public void reduceEnergy()
	{
		energy -= 0.001;
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

