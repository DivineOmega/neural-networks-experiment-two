package neuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class Neuron
{
	private int numInputs;
	public ArrayList<Double> weights = new ArrayList<Double>();
	
	public Neuron()
	{
		Random random = new Random();
		
		for (Double weight : weights)
		{
			weight = random.nextDouble();
		}
	}
}
