// Class that implements a neighbour architecture

public class Neighbour extends Architecture {
  /**
   * A type of neural network architecture where neurons
   * are most likely to be connected to their closest neighbours
   * (according to the distance metric of the architecture).
   */

  // Constructor
  public Neighbour(int maxNumNeighbours, float maxWeight, float minWeight) {
    super();
    setMaxNumNeighbours(maxNumNeighbours);
    setMaxWeight(maxWeight);
    setMinWeight(minWeight);
  }

  public NeuralNetwork generateConnections(NeuralNetwork network, Neuron neuron, DistanceMetric distanceMetric) {
    /**
     * Explain what happens
     */

    network.randomiseArray(network.indices);

    for (int i=0; i<Nn && neuron.numNeighbours<maxNumNeighbours(); i++) { // loop over possible neigbours
      float distance = distanceMetric.distance(neuron, network.neurons[i]);     // compute distance
      if (distance != 0 && random(1.0)<pow(0.95, distance)) {
        // compute if current neuron should be a neighbour of neuron in loop
        // connect them
      }
    }
    return network;

  }

}
