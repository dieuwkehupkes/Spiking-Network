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
    // construct network from an array of neurons

    this.Nn = neurons.length;
    this.Ncol = Ncol;
    this.Nw = Nw;
    this.neurons = neurons;
    indices = new int[Nn];
    for (int i=0; i<Nn; i++) indices[i] = i;
    for (Neuron neuron: neurons) neuron.network = this;

    setNeuronCoordinates(Ncol, Nw); // compute the coordinates for the neurons

  }

  void display() {
    // Visualise the network on a grid 
    
    displayNeurons();           // display neurons
  }

  void setNeuronCoordinates(int Ncol, int Nw) {
    // Compute the locations the neurons should have
    // on the grid, based on the total number of neurons,
    // columns and the neuron width
    
    for (int i=0; i<Nn; i++) {
      neurons[i].x = (i%Ncol)*Nw;
      neurons[i].y = (int(i/Ncol))*Nw;
      neurons[i].Nw = Nw;
    }
  }

  void mousePressed(int x, int y) {
    // when the mouse is pressed, give the neuron
    // it is pointing at extra activation
    int neuronIndex = findNeuron(x, y);
    Neuron neuron = neurons[neuronIndex];
    neuron.setI(40);   // give activation to the neuron
  }

  int findNeuron(float x, float y) {
    // find the neuron corresponding with input coordinates
    int column = int(x/Nw);
    int row = int(y/Nw);
    int neuronIndex = row*Ncol + column;
    return neuronIndex;
  }

  void displayNeurons() {
    for (int i=0; i<Nn; i++) {
      neurons[i].display();
    }
  }

  void update() {
    update(0.0);
  }

  void update(float I) {
    // update the network

    randomiseArray(indices);    // randomise update order
    for (int i=0; i<Nn; i++) {
      int j = indices[i];
      neurons[j].update(I);
    }

    // I wonder whether this makes a difference, as I only set
    // the neurons to 'fire' a round later.
    // Does the randomiseArray make a difference in this case?
  }

  void randomiseArray(int[] ar) {
    // Fisher-Yates shuffle
    
    int l = ar.length;      // store length of input array
    for (int i=l-1; i>0; i--) {
      int index=int(random(i+1));
      int next = ar[index];
      ar[index] = ar[i];
      ar[i] = next;
    }
  }



}
