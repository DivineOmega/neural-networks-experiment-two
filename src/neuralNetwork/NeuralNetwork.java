package neuralNetwork;

import java.util.ArrayList;

public class NeuralNetwork
{
	int numInputs = 3;
	int numOutputs = 2;
	
	int numHiddenLayers = 1;
	int numNeuronsPerHiddenLayer = 6;
	
	double bias = -1.0;
	double activationResponse = 1.0;
	
	ArrayList<NeuronLayer> neuronLayers = new ArrayList<NeuronLayer>();
	
	
	public NeuralNetwork()
	{
		createNetwork();
	}
	
	public void createNetwork()
	{
		//create the layers of the network
		if (numHiddenLayers > 0)
		{
			//create first hidden layer
			NeuronLayer firstHiddenLayer = new NeuronLayer(numNeuronsPerHiddenLayer, numInputs);
			neuronLayers.add(firstHiddenLayer);
	    
		    for (int i=0; i<numHiddenLayers-1; ++i)
		    {
		    	NeuronLayer newHiddenLayer = new NeuronLayer(numNeuronsPerHiddenLayer, numNeuronsPerHiddenLayer);
				neuronLayers.add(newHiddenLayer);
		    }
	
		    //create output layer
		    NeuronLayer outputLayer = new NeuronLayer(numOutputs, numNeuronsPerHiddenLayer);
			neuronLayers.add(outputLayer);
		}
		else
		{
			//create output layer
			NeuronLayer outputLayer = new NeuronLayer(numOutputs, numInputs);
			neuronLayers.add(outputLayer);
		}
	}
	
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
		for (int i=0; i < numHiddenLayers + 1; ++i)
		{
			NeuronLayer neuronLayer = neuronLayers.get(i);
			
			if (!inputLayer)
			{
				inputs.clear();
				inputs.addAll(outputs);
			}
			else
			{
				inputLayer = false;
			}
			
			outputs.clear();
			
			cWeight = 0;
			
			// For each neuron sum the (inputs * corresponding weights).
			// Throw the total at our sigmoid function to get the output.
			for (int j=0; j < neuronLayer.neurons.size(); ++j)
			{
				Neuron neuron = neuronLayer.neurons.get(j);
				
				double totalInput = 0;
				
				// For each weight...
				for (int k=0; k < neuron.numInputs - 1; ++k)
				{
					// Multiply it with the input.
					totalInput += neuron.weights.get(k) * 
							inputs.get(cWeight);
					
					cWeight++;
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
		return ( 1 / ( 1 + Math.exp(-totalInput / activationResponse)));
	}
	
	public ArrayList<Double> getWeights()
	{
		ArrayList<Double> weights = new ArrayList<Double>();
		
		//for each layer
		for (int i=0; i<numHiddenLayers + 1; ++i)
		{

			//for each neuron
			for (int j=0; j<neuronLayers.get(i).neurons.size(); ++j)
			{
				//for each weight
				for (int k=0; k<neuronLayers.get(i).neurons.get(j).numInputs; ++k)
				{
					weights.add(neuronLayers.get(i).neurons.get(j).weights.get(k));
				}
			}
		}

		return weights;
	}
	
	public void setWeights(ArrayList<Double> weights)
	{
		int cWeight = 0;
		
		//for each layer
		for (int i=0; i<numHiddenLayers + 1; ++i)
		{

			//for each neuron
			for (int j=0; j<neuronLayers.get(i).neurons.size(); ++j)
			{
				//for each weight
				for (int k=0; k<neuronLayers.get(i).neurons.get(j).numInputs; ++k)
				{
					neuronLayers.get(i).neurons.get(j).weights.add(weights.get(cWeight++));
				}
			}
		}
	}
}
