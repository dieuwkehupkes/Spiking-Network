// Class representing a neural network

class NeuralNetwork {
  // class representing a neural network
  int Nn;       // number of neurons
  int Ncol;     // number of columns
  int Nw;       // pixel width of the neurons (for visualisation)

  float minWeight;  // minimal weight between two neurons
  float maxWeight;  // maximal weight between two neurons
  
  Neuron neurons[];     // the neurons forming the network
  int[] indices;

  NeuralNetwork(Neuron[] neurons, int Ncol, int Nw) {
    /**
     * Construct a neural network from an array of neurons
     *
     * @param neurons   an array with objects of type Neuron
     * @param Ncol      the number of columns of the network
     * @param Nw        neuron width for visualisation
     */

    this.Nn = neurons.length;
    this.Ncol = Ncol;
    this.Nw = Nw;
    this.neurons = neurons;
    indices = new int[Nn];
    for (int i=0; i<Nn; i++) indices[i] = i;
    for (Neuron neuron: neurons) neuron.network = this;

    setNeuronCoordinates(); // compute the coordinates for the neurons

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
      int j = indices[i];
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

  void removeWeights() {
    /** 
     * Remove any connection between neurons
     */
    for (Neuron neuron : this.neurons) {
      neuron.removeConnections();
    }
  }

}
