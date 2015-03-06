// Create and visualise a neural network
int Nn=1500;
int Ncol=50;
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
  Generator G = new Generator();    // generator to generate neural network

  neuronDistr.put("ExcitatoryNeuron", 1500);
  // neuronDistr.put("InhibitoryNeuron", 500);

  n = G.createNeurons(neuronDistr);

  network = new NeuralNetwork(n, Ncol, Nw);

  Architecture a = new Neighbour(50, 0.0, 10.0);
  a.setDistanceMetric(new ManhattanDistance());
  G.connectToNeighbours(network, 50, 0.0, 10.0, a); 
  
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
