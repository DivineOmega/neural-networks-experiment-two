package util;

import java.util.ArrayList;
import java.util.Random;

import worldObjects.Creature;

public abstract class GenomeUtils
{
	
	public static Creature crossover(Creature mum, Creature dad)
	{
		Random random = new Random();
		
		System.out.println("New cross over.");
		
		System.out.print("Mum: ");
		for (Double genomePart : mum.getGenome()) {
			System.out.print(genomePart);
			System.out.print("|");
		}
		System.out.println();
		
		System.out.print("Dad: ");
		for (Double genomePart : dad.getGenome()) {
			System.out.print(genomePart);
			System.out.print("|");
		}
		System.out.println();
		
		ArrayList<Double> mumGenome = mum.getGenome();
		
		ArrayList<Double> childGenome = new ArrayList<Double>();
		childGenome.addAll(mumGenome);
		
		if (random.nextFloat()<=0.7)
		{
			int crossOverPoint = random.nextInt(childGenome.size());
			
			ArrayList<Double> dadGenome = dad.getGenome();
			
			for (int i = 0; i < childGenome.size(); i++)
			{
				if (i>crossOverPoint)
				{
					childGenome.set(i, dadGenome.get(i));
				}
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
		
		System.out.print("Chi: ");
		for (Double genomePart : child.getGenome()) {
			System.out.print(genomePart);
			System.out.print("|");
		}
		System.out.println();
		
		return child;
	}
	
}
