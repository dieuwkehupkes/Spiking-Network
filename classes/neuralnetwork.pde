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

  NeuralNetwork(Neuron[] neurons) {
    // construct network from an array of neurons

    this.Nn = neurons.length;
    this.neurons = neurons;
    indices = new int[Nn];
    for (int i=0; i<Nn; i++) indices[i] = i;

  }

  void display(int Ncol, int Nw) {
    // Visualise the network on a grid 
    
    setNeuronCoordinates(Ncol, Nw);     // compute coordinates for neurons
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

  void displayNeurons() {
    for (int i=0; i<Nn; i++) {
      neurons[i].display();
    }
  }

  void update() {
    // update the network

    randomiseArray(indices);    // randomise update order
    for (int i=0; i<Nn; i++) {
      int j = indices[i];
      neurons[j].update();
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
