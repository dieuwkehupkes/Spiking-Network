// A class to generate different types of neural networks

class Generator {
  // 
  private int maxNumNeigbours;          // the maximum number of neighbours of a neuron
  private float maxWeight, minWeight;   // the minimum and maximum connection weights

  Generator() {
    System.out.println("Create generator");
  }

  public void setRandomSeed(int n) {
    // set random seed to n
    randomSeed(n);
  }

  

  public Neuron[] createNeurons(HashMap<String, Integer> neuronNumber) {
    // create a list with neurons with types as specifed in
    // neuronNumber
    // i.e. if neuronNumber says "Inhibitory: 5, Exhibitory: 1000"
    // generate a list with 1000 exhibitory neurons and 5 inhibitory
    // neurons
    
    // find max nr of neurons
    int total = 0;
    for (int nr : neuronNumber.values()) {
      total += nr;
    }

    // create neuron Array
    Neuron[] neurons = new Neuron[total];

    // create exception for invalid types
    NullPointerException nonExistingType = new NullPointerException("Non existing neuron type");

    // loop over pairs
    int curIndex = 0;

    for (String neuronType : neuronNumber.keySet()) {
      // get number of neurons
      int n = neuronNumber.get(neuronType);
      // go over all possible types
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

      curIndex += n;
      System.out.println(curIndex);
      
    }

    // randomise neurons
    randomiseArray(neurons);

    return neurons;
  }

  private void randomiseArray(Neuron[] ar) {
    // Fisher-Yates shuffle
    
    int l = ar.length;      // store length of input array
    for (int i=l-1; i>0; i--) {
      int index=int(random(i+1));
      Neuron next = ar[index];
      ar[index] = ar[i];
      ar[i] = next;
    }
  }

}

