// Create and visualise a neural network
int Nn=10;
int Ncol=5;
int Nw=20;
boolean mouseP =false;  // indicates whether mouse is pressed

NeuralNetwork network;

void setup() {
  // determine size based on Nn, Ncol and Nw
  int width = Nw*Ncol + 10;
  int height = int(Nw*Nn/Ncol) + 10;
  size(width, height);
  frameRate(200);    // Nb: frame rate only corresponds with real time
                     // when enough processing power is available

  Neuron[] n = new Neuron[Nn];
  HashMap<String, Integer> neuronDistr = new HashMap<String, Integer>();   // nrs of diff neuron types

  neuronDistr.put("ExcitatoryNeuron", 10);
  // neuronDistr.put("InhibitoryNeuron", 500);

  network = new NeuralNetwork();
  network.addNeurons(neuronDistr, Ncol, Nw);

  Architecture a = new Neighbour(5, -5.0, 5.0);
  a.setDistanceMetric(new ManhattanDistance());
  network.setArchitecture(a);
  network.addConnections();

  network.printConnections();
  
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
