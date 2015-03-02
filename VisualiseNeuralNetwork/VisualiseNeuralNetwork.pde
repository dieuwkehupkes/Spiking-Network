// Create and visualise a neural network
int Nn=6000;
int Ncol=120;
int Nw=20;

NeuralNetwork network;

void setup() {
  size(1300, 900);
  frameRate(20);    // Nb: frame rate doesn't correspond with
                    // timestep in update, time is slowed down

  Neuron[] n = new Neuron[Nn];

  for (int i=0; i<Nn; i++) {
    n[i] = new Neuron();
  }

  network = new NeuralNetwork(n, Ncol, Nw);
  network.display();
  
}

void draw() {

  // float I = 1;

  network.update();
  network.display();

}

void mousePressed() {
  // when the mouse is pressed, give the neuron
  // it is pointing at extra activation
  network.mousePressed(mouseX, mouseY);
}
