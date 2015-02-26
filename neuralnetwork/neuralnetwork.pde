
void setup() {
  int Nn=6000;
  int Ncol=120;
  int Nw=20;
  size(1200, 800);

  Neuron[] n = new Neuron[Nn];
  NeuralNetwork network;

  for (int i=0; i<Nn; i++) {
    n[i] = new Neuron(0.1, 0.2, -65, 2);
  }

  network = new NeuralNetwork(n);
  
  network.display(Ncol, Nw);
}




// Class representing a neural network

class NeuralNetwork {
  // class representing a neural network
  int Nn;       // number of neurons
  int Ncol;     // number of columns
  int Nw;       // pixel width of the neurons (for visualisation)

  float minWeight;  // minimal weight between two neurons
  float maxWeight;  // maximal weight between two neurons
  
  Neuron neurons[];     // the neurons forming the network

  NeuralNetwork(Neuron[] neurons) {
    // construct network from an array of neurons

    this.Nn = neurons.length;
    this.neurons = neurons;

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

}
