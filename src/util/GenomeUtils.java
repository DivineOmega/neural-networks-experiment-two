package util;

import java.util.ArrayList;
import java.util.Random;

import worldObjects.Creature;

public abstract class GenomeUtils
{
	
	public static Creature crossover(Creature mum, Creature dad)
	{
		Random random = new Random();
		
		ArrayList<Double> childGenome = mum.getGenome();
		
		if (random.nextFloat()<=0.7)
		{
			int crossOverPoint = random.nextInt(childGenome.size());
			
			ArrayList<Double> dadGenome = dad.getGenome();
			
			int genomePartCount = 0;
			for (int i = 0; i < childGenome.size(); i++)
			{
				if (genomePartCount>crossOverPoint)
				{
					childGenome.set(genomePartCount, dadGenome.get(genomePartCount));
				}
				genomePartCount++;
			}
		}
		
		for (int i = 0; i < childGenome.size(); i++) 
		{
			if (random.nextFloat()<=0.01)
			{
				double mutationAmount = -0.3 + (Math.random()*0.6);
				
				childGenome.set(i, childGenome.get(i) + mutationAmount);
			}
		}
		
		Creature child = new Creature();
		child.setGenome(childGenome);
		child.generation = Math.min(mum.generation, dad.generation) + 1;
		
		return child;
	}
	
}
