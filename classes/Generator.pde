// A class to generate different types of neural networks

class Generator {
  // 
  private int maxNumNeigbours;          // the maximum number of neighbours of a neuron
  private float maxWeight, minWeight;   // the minimum and maximum connection weights

  Generator() {
  }

  public void connectToNeighbours(NeuralNetwork network, int maxNumNeighbours, float minWeight, float maxWeight, Architecture architecture) {
    /**
     * Generate connections between neurons in the network
     * which connections are generated and what their weights
     * are is random, but can be steered towards a certain
     * architecture setting different parameters
     *
     * @param NeuralNetwork              The network to create connections for
     * @param maxNumNeigbours            The maximum number of neigbours for every neuron
     * @param minWeight                  The minimum weight for a connection
     * @param maxWeight                  The maximum weight for a connection
     */ 

    architecture.setMaxNumNeighbours(maxNumNeighbours);
    architecture.setMinWeight(minWeight);
    architecture.setMaxWeight(maxWeight);

    
    network.initNeighbours(maxNumNeighbours);     // assign neighbour arrays to the neurons in the network
    architecture.generateConnections(network);
  }
    

  public void setRandomSeed(int n) {
    /**
     * Set the random seed of the generator
     */
    randomSeed(n);      // set random seed to n
  }

}

