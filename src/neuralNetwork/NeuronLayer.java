package neuralNetwork;

import java.util.ArrayList;

public class NeuronLayer
{
	ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	
	public NeuronLayer(int numNeuronsPerHiddenLayer, int numInputs)
	{
		for (int i = 0; i < numNeuronsPerHiddenLayer; ++i)
		{
			Neuron newNeuron = new Neuron(numInputs);
			
			neurons.add(newNeuron);
		}
	}
}
