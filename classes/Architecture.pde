// class for neural network architectures

abstract class Architecture {
  /**
   * describe class
   */

  // fields
  private int maxNumNeighbours;          // The maximum number of neighbours for a neuron
  private float maxWeight, minWeight;    // The maximum and minimum weights of the connections
  private String distanceMetric;         // The distance metric used in tha architecture

  // methods
  abstract public NeuralNetwork generateConnections(NeuralNetwork network, Neuron neuron, DistanceMetric distanceMetric);

 // Setters

  public void setDistanceMetric(String distanceMetric) {
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
  
  public String distanceMetric() {
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
