package neuralNetwork;

import java.util.ArrayList;

public class NeuralNetwork
{
	int numInputs;
	int numOutputs;
	
	int numHiddenLayers;
	int numNeuronsPerHiddenLayer;
	
	Double bias;
	
	ArrayList<NeuronLayer> neuronLayers = new ArrayList<NeuronLayer>();
	private double activationResponse;
	
	public ArrayList<Double> update(ArrayList<Double> inputs)
	{
		ArrayList<Double> outputs = new ArrayList<Double>();
		
		int cWeight = 0;
		
		// If the number of inputs supplied is incorrect...
		if (inputs.size()!=numInputs)
		{
			return outputs; // Return empty outputs
		}
		
		// Loop through all layers
		boolean inputLayer = true;
		for (NeuronLayer neuronLayer : neuronLayers)
		{
			if (!inputLayer)
			{
				inputs = outputs;
			}
			else
			{
				inputLayer = false;
			}
			
			outputs.clear();
			
			cWeight = 0;
			
			// For each neuron sum the (inputs * corresponding weights).
			// Throw the total at our sigmoid function to get the output.
			for (Neuron neuron : neuronLayer.neurons)
			{
				double totalInput = 0;
				
				// For each weight...
				for (Double weight : neuron.weights)
				{
					// If it is not the final weight...
					if (!weight.equals(neuron.weights.get(neuron.weights.size()-1)))
					{
						// Multiply it with the input.
						totalInput += weight * inputs.get(cWeight++);
					}
				}
				
				// Add in the bias (final weight)
				totalInput += neuron.weights.get(neuron.weights.size()-1) * bias;
				
				// We can store the outputs from each layer as we generate them.
			    // The combined activation is first filtered through the sigmoid function
				outputs.add(sigmoid(totalInput, activationResponse));
				
				cWeight = 0;
			}
		}
		
		return outputs;
	}

	private Double sigmoid(double totalInput, double activationResponse)
	{
		return null;
	}
}
