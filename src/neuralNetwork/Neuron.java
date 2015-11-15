package neuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class Neuron
{
	public ArrayList<Double> weights = new ArrayList<Double>();
	public int numInputs;
	
	public Neuron(int numInputs)
	{
		this.numInputs = numInputs;
		
		Random random = new Random();
		
		for (int i=0; i<numInputs+1; ++i)
		{
			double newWeight = -1 + (random.nextDouble()*2);
			
			weights.add(newWeight);
		}
	}
}
