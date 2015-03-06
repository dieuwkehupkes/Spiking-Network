// Class representing a neural network

class NeuralNetwork {

  // Fields
  private int Nn;       // number of neurons
  private int Ncol;     // number of columns
  private int Nw;       // pixel width of the neurons (for visualisation)

  Neuron neurons[];     // the neurons forming the network
  Architecture architecture;     // the architecture of the network

  private int[] indices;        // indices to loop over the neurons of the network

  // Constructors

  // Empty constructor
  NeuralNetwork() {
    /** 
     * Construct empty neural network
     */
  }

  NeuralNetwork(Neuron[] neurons, int Ncol, int Nw) {
    /**
     * Construct network with neurons
     */
    addNeurons(neurons, Ncol, Nw);
  }

  // Make two more constructors: one in which also architecture is
  // inputted and one in which only architecture is inputted

  public void addNeurons(Neuron[] neurons, int Ncol, int Nw) {
    /**
     * Add a pre-generated list of neurons to
     * the network
     */
    this.neurons = neurons;              // add neurons
    this.Ncol = Ncol;                    // set column width network
    this.Nw = Nw;                        // set neuron width network
    this.Nn = neurons.length;            // set number of neurons of network
    this.indices = new int[Nn];          // create list with indices
    for (int i=0; i<Nn; i++) this.indices[i] = i;   // add indices

    for (Neuron neuron : neurons) neuron.network = this;    // associate neurons with network

    setNeuronCoordinates();             // compute coordinates for neurons
  }

  public void addNeurons(HashMap<String, Integer> generateNeurons, int Ncol, int Nw) {
    /**
     * Generate neurons as specified in generateNeurons and
     * add them to the network
     *
     * @param generateNeurons   a map from neurontype to number of
     *                          neurons for this type
     */

    // find number of neurons
    Neuron[] neurons = generateNeurons(generateNeurons);    // generate the neurons
    addNeurons(neurons, Ncol, Nw);      // add to network
  }

  public void addConnections() {
    /**
     * Add connections to the network, using the architecture of
     * the network
     */
    if ( hasArchitecture()) {
      architecture.generateConnections(this);
    }
    else {
      throw new NullPointerException("No architecture set for the network");
    }
  }

  private Neuron[] generateNeurons(HashMap<String, Integer> generateNeurons) {
    /**
     * Generate a list with neurons as specified in generateNeurons
     *
     * @param generateNeurons       A hashmap from strings giving the name of the
     *                              neuron type to a number describing how many
     *                              neurons of this type should be created
     * @return                      a list with neurons, randomised order
     */

    // find max nr of neurons
    int total = 0;
    for (int nr : generateNeurons.values()) total+= nr;

    Neuron[] neurons = new Neuron[total];       // create Array to store neurons

    int curIndex = 0;                           // Array index to store new neuron

    for (String neuronType : generateNeurons.keySet()) {
      int n = generateNeurons.get(neuronType);              // get number of neurons
      appendNeurons(neuronType, neurons, n, curIndex);      // add neurons to array
      curIndex += n;                                        // increase array index
    }

    randomiseArray(neurons);            // randomise order of neurons

    return neurons;
  }

  private void appendNeurons(String neuronType, Neuron[] neurons, int n, int curIndex) {
    /**
     * Add neurons of specified type to the list
     * NB: needs to be updated if new neuron types are added!!
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

  public void setArchitecture(Architecture architecture) {
    /**
     * Add an architecture to the network, or change the current
     * architecture of the network;
     * As the weight organisation is directly related to
     * the architecture of the network, changing the architecture
     * of the network will remove all weights.
     */
    this.architecture = architecture;
    this.removeWeights();
  }

  void display() {
    /**
     * Visualise the network on a grid 
     */
    
    displayNeurons();           // display neurons
  }

  private void setNeuronCoordinates() {
    /**
     * Compute the locations the neurons should have
     * on the grid, based on the total number of neurons,
     * columns and the neuron width
     */
    
    for (int i=0; i<Nn; i++) {
      neurons[i].x = (i%Ncol)*Nw + 5;
      neurons[i].y = (int(i/Ncol))*Nw + 5;
      neurons[i].Nw = Nw;
    }
  }

  public void initNeighbours(int maxNumNeighbours) {
    /**
     * create arrays for neighbours for each neuron
     */
    for (Neuron neuron : neurons) {
      neuron.neighbours = new int[maxNumNeighbours];
      neuron.weights = new float[maxNumNeighbours];
    }
  }

  public void mousePressed(int x, int y) {
    /**
     * When the mouse is pressed, give the neuron
     * it is pointing at extra activation
     */

    int neuronIndex = findNeuron(x, y);     // find index neuron mouse points at
    try {
      Neuron neuron = neurons[neuronIndex];
      neuron.setI(40);   // give activation to the neuron
     } catch (ArrayIndexOutOfBoundsException e) {
       // do nothing if mouse points outside of network
    }
  }

  private int findNeuron(float x, float y) {
    /** 
     * Find the neuron corresponding with the coordinates
     * on the grid.
     */
    int column = int(x/Nw);     // compute column of coordinates
    int row = int(y/Nw);        // compute row of coordinates
    int neuronIndex = row*Ncol + column;    // compute index from row and column
    return neuronIndex;
  }

  public void displayNeurons() {
    /**
     * Display all neurons
     */
    for (int i=0; i<Nn; i++) {
      neurons[i].display();
    }
  }

  public void update() {
    update(0.0);
  }

  public void update(float I) {
    /**
     * Update the values of the neurons of the
     * network given an input given to all neurons.
     *
     * @param I fixed input given to all neurons
     */
    randomiseArray(indices);    // randomise update order
    for (int i=0; i<Nn; i++) {
      Integer j = indices[i];
      neurons[j].update(I);
    }

    // I wonder whether this makes a difference, as I only set
    // the neurons to 'fire' a round later.
    // Does the randomiseArray make a difference in this case?
  }

  public void printConnections() {
    /**
     * Print the connections between the
     * neurons in the network
     */
    int counter = 1;
    for (Neuron neuron : neurons) {
      System.out.println("Neuron "+counter+ " receives input from:");
      for (int i=0; i<neuron.numNeighbours; i++) {
        System.out.println("Neuron "+neuron.neighbours[i]+"\t weight: "+neuron.weights[i]);
      }
      counter++;
    }
  }

  private void randomiseArray(int[] ar) {
    /**
     * Shuffle input array using Fisher-Yates algorithm
     */
    int l = ar.length;      // store length of input array
    for (int i=l-1; i>0; i--) {
      int index=int(random(i+1));
      int next = ar[index];
      ar[index] = ar[i];
      ar[i] = next;
    }
  }

  private void randomiseArray(Object[] ar) {
    /**
     * Shuffle input array using Fisher-Yates algorithm
     */
    int l = ar.length;      // store length of input array
    for (int i=l-1; i>0; i--) {
      int index=int(random(i+1));
      Object next = ar[index];
      ar[index] = ar[i];
      ar[i] = next;
    }
  }

  void removeWeights() {
    /** 
     * Remove any connection between neurons
     */
    for (Neuron neuron : this.neurons) {
      neuron.removeConnections();
    }
  }

  private boolean hasArchitecture() {
    /**
     * Return if architecture is set
     */
    if (this.architecture == null) {
      return false;
    } else {
      return true;
    }
  }

  // Setters

  // Getters
  
  public int getMaxNumNeighbours() {
    /**
     * Return maximum number of neighbours
     * of the neurons
     */
    int maxNumNeighbours;
    try {
        maxNumNeighbours = this.architecture.maxNumNeighbours();
    } catch (NullPointerException e) {
      throw new NullPointerException("Architecture of Network is not set, maximum number of neighbours unknown");
    }
    return maxNumNeighbours;
  }

  public float getMinWeight() {
    /**
     * Return maximum number of neighbours
     * of the neurons
     */
    float minWeight;
    try {
        minWeight = this.architecture.minWeight();
    } catch (NullPointerException e) {
      throw new NullPointerException("Architecture of Network is not set, maximum number of neighbours unknown");
    }
    return minWeight;
  }

  public float getMaxWeight() {
    /**
     * Return maximum number of neighbours
     * of the neurons
     */
    float maxWeight;
    try {
        maxWeight = this.architecture.maxWeight();
    } catch (NullPointerException e) {
      throw new NullPointerException("Architecture of Network is not set, maximum number of neighbours unknown");
    }
    return maxWeight;
  }


}
