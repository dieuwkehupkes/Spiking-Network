// Parameters for visual network
int Nexhib = 8;               // number of random exhibitory neurons
int Ninhib = 4;               // number of random inhibitory neurons
int Nn = Nexhib + Ninhib;       // total number of neurons
int Ncol=4;                    // number of columns
int Nw=40;                      // neuron width
int maxNumNeighbours=10;        // maximum nr of neigbours for neuron
float minWeight=-5.0;           // minimal weight between neurons
float maxWeight=15.0;           // maximum weight between neurons
DistanceMetric distMetric = new ManhattanDistance();        // distance metric used
ProbabilityFunction probFunction = new Exponential(0.95);   // funct to determine prob of connecting
Training trainingFunction = new HeuristicalHebbian();   // training function for learning

// variables to use for visualisation
boolean mouseP = false;         // indicates whether mouse is pressed
boolean trainingMode = false;   // indicate whether network is in training mode
int simulationSpeed = 50;

NeuralNetwork network;

void setup() {
  // determine size based on Nn, Ncol and Nw
  int width = Nw*Ncol + 5;
  int height = int(Nw*Nn/Ncol) + 5;
  size(width, height);
  frameRate(simulationSpeed);    // Nb: frame rate only corresponds with real time
                     // when enough processing power is available

  Neuron[] n = new Neuron[Nn];
  HashMap<String, Integer> neuronDistr = new HashMap<String, Integer>();   // nrs of diff neuron types

  neuronDistr.put("ExcitatoryNeuron", Nexhib);
  neuronDistr.put("InhibitoryNeuron", Ninhib);

  network = new NeuralNetwork();
  network.addNeurons(neuronDistr, Ncol, Nw);

  Architecture a = new Neighbour(maxNumNeighbours, minWeight, maxWeight);
  a.setDistanceMetric(distMetric, Nw);
  a.setProbabilityFunction(probFunction);
  network.setArchitecture(a);
  network.addConnections();
  network.setTrainingMethod(trainingFunction);

  network.display();
  
}

void draw() {

  float I = random(2.0);

  if (mouseP) network.mousePressed(mouseX, mouseY);
  network.update(I);
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
  if (key=='+'|| key=='-') {    // change simulationSpeed
    simulationSpeed = (key=='+') ? simulationSpeed+10 : simulationSpeed -10;    //change speed
    if (simulationSpeed <=0) simulationSpeed = 1;
    System.out.println("new frameRate"+ simulationSpeed);
    frameRate(simulationSpeed);
  }
  if (key=='q' || key=='Q') exit();
  if (key=='t' || key=='T') {   // toggle training mode
    trainingMode = trainingMode ? false : true;
    String print = trainingMode ? "Training mode on" : "Training mode off";
    network.toggleTrainingMode();
  }
}
