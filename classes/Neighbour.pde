// Class that implements a neighbour architecture

public class Neighbour extends Architecture {
  /**
   * A type of neural network architecture where neurons
   * are most likely to be connected to their closest neighbours
   * (according to the distance metric of the architecture).
   */

  // Constructor
  public Neighbour(int maxNumNeighbours, float maxWeight, float minWeight) {
    super(maxNumNeighbours, maxWeight, minWeight);
  }

  public void addConnections(NeuralNetwork network, int neuronI) {
    /**
     * Explain what happens
     */

    Neuron neuron = network.neurons[neuronI];

    network.randomiseArray(network.indices);    // randomise order of neighbours

    for (int i=0; i<network.Nn && neuron.numNeighbours<maxNumNeighbours(); i++) { // loop over possible neigbours
      int neuronIndex = network.indices[i];
      Neuron otherNeuron = network.neurons[neuronIndex];
      float distance = distanceMetric().distance(neuron, otherNeuron);     // compute distance
      if (distance != 0 && random(1.0)<probabilityFunction().probability(distance)) {   // there is some variation possible here
        neuron.neighbours[neuron.numNeighbours] = neuronIndex;  // set nth neighbour
        neuron.weights[neuron.numNeighbours] = random(minWeight(), maxWeight());
        neuron.numNeighbours++;     // increment neighbour counter
      }
    }
  }

}
