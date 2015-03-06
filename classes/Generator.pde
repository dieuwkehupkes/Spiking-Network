// A class to generate different types of neural networks

class Generator {
  // 
  private int maxNumNeigbours;          // the maximum number of neighbours of a neuron
  private float maxWeight, minWeight;   // the minimum and maximum connection weights

  Generator() {
  }

  public Neuron[] createNeurons(HashMap<String, Integer> neuronNumber) {
    /**
     * create a list with neurons with types as specifed in neuronNumber
     * 
     * @param neuronNumber  a hashmap from strings giving the name of the neuron
     *                      type to the number of neurons of this type that needs
     *                      to be created
     * @return              a list with neurons
     */
    
    // find max nr of neurons
    int total = 0;
    for (int nr : neuronNumber.values()) {
      total += nr;
    }

    Neuron[] neurons = new Neuron[total];       // create array to store neurons

    int curIndex = 0;       // loop over pairs

    for (String neuronType : neuronNumber.keySet()) {
      int n = neuronNumber.get(neuronType);             // get number of neurons
      addNeurons(neuronType, neurons, n, curIndex);     // add neurons to list
      curIndex += n;                                    // increase index
    }

    randomiseArray(neurons);     // randomise order of neurons

    return neurons;
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
    

  private void addNeurons(String neuronType, Neuron[] neurons, int n, int curIndex) {
    /**
     * Add neurons of specified type to the list
     * 
     * @param neuronType    string with the type of the neuron
     * @param neurons       array to add neurons to
     * @param n             number of neurons to be added
     * @param curIndex      position in array to start appending
     *
     * @throws NullPointerException
     */

    /* Maybe I could do something here as well with an interface to make this better */

    // create exception for invalid types
    NullPointerException nonExistingType = new NullPointerException("Non existing neuron type");

    // add neurons of neuronType to array neurons
      if (neuronType.equals("ChatteringNeuron")) {
        for (int i=0; i<n; i++) neurons[i+curIndex] = new ChatteringNeuron();
      }
      else if (neuronType.equals("ExcitatoryNeuron")) {
        for (int i=0; i<n; i++) neurons[i+curIndex] = new ExcitatoryNeuron();
      }
      else if (neuronType.equals("FastSpikingNeuron")) {
        for (int i=0; i<n; i++) neurons[i+curIndex] = new FastSpikingNeuron();
      }
      else if (neuronType.equals("InhibitoryNeuron")) {
        for (int i=0; i<n; i++) neurons[i+curIndex] = new InhibitoryNeuron();
      }
      else if (neuronType.equals("IntrinsicallyBurstingNeuron")) {
        for (int i=0; i<n; i++) neurons[i+curIndex] = new IntrinsicallyBurstingNeuron();
      }
      else if (neuronType.equals("RegularSpikingNeuron")) {
        for (int i=0; i<n; i++) neurons[i+curIndex] = new RegularSpikingNeuron();
      }
      else {
        System.out.println(neuronType);
        throw nonExistingType;
      }
  }

  private void randomiseArray(Neuron[] ar) {
    /**
     * Shuffle input array using Fisher-Yates shuffling
     */
    
    int l = ar.length;      // store length of input array
    for (int i=l-1; i>0; i--) {
      int index=int(random(i+1));
      Neuron next = ar[index];
      ar[index] = ar[i];
      ar[i] = next;
    }
  }

  public void setRandomSeed(int n) {
    /**
     * Set the random seed of the generator
     */
    randomSeed(n);      // set random seed to n
  }

}

