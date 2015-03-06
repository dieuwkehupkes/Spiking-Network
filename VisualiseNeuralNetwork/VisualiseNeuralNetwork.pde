// Create and visualise a neural network
int Nn=120;
int Ncol=12;
int Nw=20;
int maxNumNeighbours=50;
float minWeight=-5.0;
float maxWeight=15.0;
DistanceMetric distMetric = new ManhattanDistance();
ProbabilityFunction probFunction = new Exponential(0.95);

boolean mouseP =false;  // indicates whether mouse is pressed

NeuralNetwork network;

void setup() {
  // determine size based on Nn, Ncol and Nw
  int width = Nw*Ncol + 5;
  int height = int(Nw*Nn/Ncol) + 5;
  size(width, height);
  frameRate(200);    // Nb: frame rate only corresponds with real time
                     // when enough processing power is available

  Neuron[] n = new Neuron[Nn];
  HashMap<String, Integer> neuronDistr = new HashMap<String, Integer>();   // nrs of diff neuron types

  neuronDistr.put("ExcitatoryNeuron", 100);
  // neuronDistr.put("InhibitoryNeuron", 500);

  network = new NeuralNetwork();
  network.addNeurons(neuronDistr, Ncol, Nw);

  Architecture a = new Neighbour(maxNumNeighbours, minWeight, maxWeight);
  a.setDistanceMetric(distMetric);
  a.setProbabilityFunction(probFunction);
  network.setArchitecture(a);
  network.addConnections();

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
