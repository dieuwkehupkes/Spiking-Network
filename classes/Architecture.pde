// class for neural network architectures

abstract class Architecture {
  /**
   * Abstract class describing the architecture of a neural network;
   * an architecture object describes how likeli different neurons in
   * a network are to be connected, what the minimum and maximum weights
   * between the neurons are and how many neighbours every neuron
   * maximally has
   */


  // fields
  private int maxNumNeighbours;                     // Maximum number of neighbours for a neuron
  private float maxWeight, minWeight;               // Maximum and minimum weights of the connections
  private DistanceMetric distanceMetric;            // Distance metric used in tha architecture
  private ProbabilityFunction probabilityFunction;  // Function used to compute how likely a
                                                    // neuron is to connect with another neuron
  private int potentialNumNeighbours;               // number of neurons that are a potential
                                                    // neighbour of each neuron

  // Constructor
  public Architecture(int maxNumNeighbours, float minWeight, float maxWeight) {
    setMaxNumNeighbours(maxNumNeighbours);
    setMaxWeight(maxWeight);
    setMinWeight(minWeight);
    potentialNumNeighbours = 5*maxNumNeighbours;     // default value potential neighbours
  }

  // Methods
  public void generateConnections(NeuralNetwork network) {
    network.initNeighbours(maxNumNeighbours());     // create arrays for weights and neighbours
    for (int i=0; i<network.Nn; i++) {     // add connections for all neurons
      addConnections(network, i);
    }
  }

  abstract protected void addConnections(NeuralNetwork network, int neuronI);  // add connections for 1 neuron

 // Setters

  public void setDistanceMetric(DistanceMetric distanceMetric, int neuronWidth) {
    distanceMetric.setNeuronWidth(neuronWidth);
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

  public void setProbabilityFunction(ProbabilityFunction probabilityFunction) {
    this.probabilityFunction = probabilityFunction;
  }

  // Getters
  
  public DistanceMetric distanceMetric() {
    return distanceMetric;
  }

  public ProbabilityFunction probabilityFunction() {
    return probabilityFunction;
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
