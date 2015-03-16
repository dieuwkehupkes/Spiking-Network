package izhikevich.spikingnetwork.architecture;

import izhikevich.spikingnetwork.NeuralNetwork;
import izhikevich.spikingnetwork.neuron.*;


public class Neighbour extends Architecture {
	  /**
	   * A type of neural network architecture where neurons
	   * are most likely to be connected to their closest neighbours
	   * (according to the distance metric of the architecture).
	   */

	  // Constructor
	  public Neighbour(int maxNumNeighbours, double maxWeight, double minWeight) {
	    super(maxNumNeighbours, maxWeight, minWeight);
	  }

	  public void addConnections(NeuralNetwork network, int neuronI) {
	    /**
	     * Explain what happens
	     */

	    Neuron neuron = network.getNeurons()[neuronI];

	    network.randomiseArray(network.indices);    // randomise order of neighbours

	    for (int i=0; i<network.Nn && neuron.numNeighbours<maxNumNeighbours(); i++) { // loop over possible neigbours
	      int neuronIndex = network.indices[i];
	      Neuron otherNeuron = network.getNeurons()[neuronIndex];
	      double distance = distanceMetric().distance(neuron, otherNeuron);     // compute distance
	      if (distance != 0 && Math.random()<probabilityFunction().probability(distance)) {   // there is some variation possible here
	        neuron.getNeighbours()[neuron.numNeighbours] = neuronIndex;  // set nth neighbour
	        neuron.getWeights()[neuron.numNeighbours] = getRandom(minWeight(), maxWeight()); 
	        neuron.numNeighbours++;     // increment neighbour counter
	      }
	    }
	  }
	  
	  private double getRandom(double minWeight, double maxWeight) {
		  /**
		   * Generate a random double between minWeight
		   * and maxWeight
		   */
		  // TODO check if this does what you want
		  double r = Math.random();
		  r *= (maxWeight - minWeight);
		  r += minWeight;
		  return r;
	  }


}
