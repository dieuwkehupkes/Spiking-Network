// class for neural network architectures

abstract class Architecture {
  /**
   * describe class
   */

  // Constructor
  public Architecture(int maxNumNeighbours, float minWeight, float maxWeight) {
    setMaxNumNeighbours(maxNumNeighbours);
    setMaxWeight(maxWeight);
    setMinWeight(minWeight);
  }

  // fields
  private int maxNumNeighbours;          // The maximum number of neighbours for a neuron
  private float maxWeight, minWeight;    // The maximum and minimum weights of the connections
  private DistanceMetric distanceMetric;         // The distance metric used in tha architecture

  public void generateConnections(NeuralNetwork network) {
    network.initNeighbours(maxNumNeighbours());     // create arrays for weights and neighbours
    for (Neuron neuron : network.neurons) {     // add connections for all neurons
      addConnections(network, neuron);
    }
  }

  // methods
  abstract protected void addConnections(NeuralNetwork network, Neuron neuron);     // add connections for a neuron

 // Setters

  public void setDistanceMetric(DistanceMetric distanceMetric) {
    this.distanceMetric = distanceMetric;
  }

  public void setMaxNumNeighbours(int maxNumNeighbours) {
    this.maxNumNeighbours = maxNumNeighbours;
  }

  public void setMinWeight(float minWeight) {
    this.minWeight = minWeight;
  }

  public void setMaxWeight(float maxWeight) {
    this.maxWeight = maxWeight;
  }

  // Getters
  
  public DistanceMetric distanceMetric() {
    return distanceMetric;
  }

  public int maxNumNeighbours() {
    return maxNumNeighbours;
  }

  public float minWeight() {
    return minWeight;
  }

  public float maxWeight() {
    return maxWeight;
  }

}
