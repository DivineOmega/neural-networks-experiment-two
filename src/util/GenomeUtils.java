package util;

import java.util.ArrayList;
import java.util.Random;

import worldObjects.Creature;

public abstract class GenomeUtils
{
	
	public static ArrayList<Creature> crossover(Creature mum, Creature dad)
	{
		Random random = new Random();
				
		ArrayList<Double> mumGenome = mum.getGenome();
		ArrayList<Double> dadGenome = dad.getGenome();
		
		ArrayList<Double> child1Genome = new ArrayList<Double>();
		child1Genome.addAll(mumGenome);
		
		ArrayList<Double> child2Genome = new ArrayList<Double>();
		child2Genome.addAll(dadGenome);
		
		if (random.nextFloat()<=0.7)
		{
			int crossOverPoint = random.nextInt(child1Genome.size());
						
			for (int i = 0; i < child1Genome.size(); i++)
			{
				if (i>crossOverPoint)
				{
					child1Genome.set(i, dadGenome.get(i));
				}
			}
			
			for (int i = 0; i < child2Genome.size(); i++)
			{
				if (i>crossOverPoint)
				{
					child2Genome.set(i, mumGenome.get(i));
				}
			}
		}
		
		for (int i = 0; i < child1Genome.size(); i++) 
		{
			if (random.nextFloat()<=0.01)
			{
				double mutationAmount = -0.3 + (Math.random()*0.6);
				
				child1Genome.set(i, child1Genome.get(i) + mutationAmount);
			}
		}
		
		for (int i = 0; i < child2Genome.size(); i++) 
		{
			if (random.nextFloat()<=0.01)
			{
				double mutationAmount = -0.3 + (Math.random()*0.6);
				
				child2Genome.set(i, child2Genome.get(i) + mutationAmount);
			}
		}
		
		Creature child1 = new Creature();
		child1.setGenome(child1Genome);
		child1.generation = Math.min(mum.generation, dad.generation) + 1;
		
		Creature child2 = new Creature();
		child2.setGenome(child2Genome);
		child2.generation = Math.min(mum.generation, dad.generation) + 1;
				
		ArrayList<Creature> children = new ArrayList<Creature>();
		children.add(child1);
		children.add(child2);
				
		return children;
	}
	
}
