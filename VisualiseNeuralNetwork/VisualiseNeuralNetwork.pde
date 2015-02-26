// Create and visualise a neural network


void setup() {
  int Nn=6000;
  int Ncol=120;
  int Nw=20;
  size(1300, 900);

  Neuron[] n = new Neuron[Nn];
  NeuralNetwork network;

  for (int i=0; i<Nn; i++) {
    n[i] = new Neuron(0.1, 0.2, -65, 2);
  }

  network = new NeuralNetwork(n);
  
  network.display(Ncol, Nw);
}
