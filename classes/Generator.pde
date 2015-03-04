// A class to generate different types of neural networks

class Generator {
  // 
  private int maxNumNeigbours;          // the maximum number of neighbours of a neuron
  private float maxWeight, minWeight;   // the minimum and maximum connection weights

  Generator() {
  }

  public void setRandomSeed(int n) {
    // set random seed to n
    randomSeed(n);
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

    // create neuron Array
    Neuron[] neurons = new Neuron[total];

    // loop over pairs
    int curIndex = 0;

    for (String neuronType : neuronNumber.keySet()) {
      // get number of neurons
      int n = neuronNumber.get(neuronType);
      addNeurons(neuronType, neurons, n, curIndex);
      curIndex += n;
    }

    // randomise neurons
    randomiseArray(neurons);

    return neurons;
  }

  private void addNeurons(String neuronType, Neuron[] neurons, int n, int curIndex) {
    /**
     * Add neurons of specified type to the list
     * 
     * @param neuronType    string with the type of the neuron
     * @param neurons       array to add neurons to
     * @param n             number of neurons to be added
     * @param curIndex      position in array to start appending
     */

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

}

