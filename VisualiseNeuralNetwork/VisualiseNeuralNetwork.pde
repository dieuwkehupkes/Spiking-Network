// Create and visualise a neural network
int Nn=600;
int Ncol=20;
int Nw=20;
boolean mouseP =false;  // indicates whether mouse is pressed

NeuralNetwork network;

void setup() {
  // determine size based on Nn, Ncol and Nw
  int width = Nw*Ncol + 10;
  int height = int(Nw*Nn/Ncol) + 10;
  System.out.println(width);
  System.out.println(height);
  size(width, height);
  frameRate(200);    // Nb: frame rate only corresponds with real time
                     // when enough processing power is available

  Neuron[] n = new Neuron[Nn];

  for (int i=0; i<Nn; i++) {
    n[i] = new ExcitatoryNeuron();
  }

  network = new NeuralNetwork(n, Ncol, Nw);
  
  int[] connectedTo = {1, 2, 3, 4, 5, 6};
  float[] w = {100, 400, 5, 9, 8, 10};
  
  network.neurons[0].setConnections(connectedTo, w);
  network.display();
  
}

void draw() {

  float I = 0;

  if (mouseP) network.mousePressed(mouseX, mouseY);
  network.update();
  network.display();

}

void mousePressed() {
  // when the mouse is pressed, give the neuron
  // it is pointing at extra activation
  mouseP = true;
  network.mousePressed(mouseX, mouseY);
}

void mouseReleased() {
  mouseP = false;
}

void keyPressed() {
  // specify what happens when a key is pressed
  // not implemented yet
  if (key=='q' || key=='Q') exit();
}
