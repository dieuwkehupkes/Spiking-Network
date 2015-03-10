// class representing a neuron

class Neuron {
  // Class representing a neuron
  private float a, b, c, d;
  float u, v;        // u and v when they were last computed
  private float I;      // extrenal input to neuron
  boolean fired = false;
  boolean trainingMode = false;
  float lastTimeFired;
  float timeStep = 0.1;

  NeuralNetwork network;    // the network the neuron is part of
  int neighbours[];     // indices pointing to the neighbours of the neuron
  float weights[];        // weights to the neighbours
  int numNeighbours = 0;
  int numPotential = 30;        // CHANGE THIS LATER TO BE PART OF ARCHITECTURE!
  int potentialNeighbours[] = new int[numPotential];        // indices pointing to potential neighbours

  int x;        // x coordinate for displaying neuron
  int y;        // y coordinate for displaying neuron
  int Nw;       // Neuron width

  
  // Constructor
  Neuron(float a, float b, float c, float d) {
    // Initialise neuron parameters
    setParams(a, b, c, d);
    reset();
  }


  public void setParams(float a, float b, float c, float d) {
    // set parameters of the neuron
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  float[][] show_spike_behaviour(float I, int nr_of_steps) {
    // Show behaviour of neujon as a result of a constant input current
    
    float[][] time_potential = new float[nr_of_steps][2];     // declare array to store output
    int nr_of_spikes = 0;

    // reset();     // Reset values as if neuron was just created

    // compute behaviour
    for (int i=0; i<nr_of_steps; i++) {
      float cur_time = i*0.1;
      time_potential[i][0] = cur_time;
      time_potential[i][1] = v;
      computeNext(I);
      if (fired) nr_of_spikes++;
    }

    System.out.println(nr_of_spikes++);
    
    return time_potential;
  }

  void setTimeStep(float timeStep) {
    // Set the timestep for updating

    this.timeStep = timeStep;
  }

  private void computeNext(float I) {
    // Compute the membrane potential one time step t from now.
    // Current input to the neuron is I.

    // if potential crosses threshold, reset

    resetI();    // reset the input that was used to compute the update

    if (v>30) {
      v=c;
      u=u+d;
      this.fired = true;
      this.lastTimeFired=millis();
      return;
    }

    v=v+0.5*timeStep*(0.04*pow(v,2)+5*v+140-u+I); //% step 0.5 ms
    v=v+0.5*timeStep*(0.04*pow(v,2)+5*v+140-u+I); //% for numerical
    u=u+timeStep*a*(b*v-u); //% stability
    fired = false;

  }

  void update() {
    // Update with 0.0 input from outside the network
    update(0.0);
  }

  void update(float thalamicInput) {
    /**
     * Update values v and u of the network, and
     * update the weights if network is in
     * training mode.
     */
    
    float neighbourActivation = computeNeighbourActivation();
    float inputActivation = this.I + thalamicInput + neighbourActivation;
    computeNext(inputActivation);

    if (trainingMode) updateWeights();        // update weights
  }

  private float computeNeighbourActivation() {
    // Compute the activation the neuron gets from its
    // neighbours
    
    float neighbourActivation = 0;
    for (int i=0; i<numNeighbours; i++) {
      if (network.neurons[neighbours[i]].fired) neighbourActivation+=weights[i];
    }

    return neighbourActivation;
  }

  private void updateWeights() {
    /** 
     * Update the connections of the network
     * to his neighbours, based on their
     * recent firing activity and the learning
     * function
     */
    // loop over neurons with which a connection is already established
    for (int i=0; i < this.numNeighbours; i++) {
      float update = network.training.updateExistingConnection(this, network.neurons[i]);
    }

    rmLowConnections();     // remove connections below minimum weight of the network
    
    // loop over close neurons to see if a connection should be established
    int i = 0;
    while (numNeighbours < maxNumNeighbours && i < potentialNeighbours.length) {
      network.training.updateNonExistingConnection(this, network.neurons[potentialNeighbours[i]]);
      i++;
    }

  }

  private void rmLowConnections() {
    /**
     * Remove connections whose weight got
     * lower than the minimum weight of the network
     * the neuron is part of
     * THIS IS NOT REALLY CORRECT, FIND A DIFFERENT WAY TO REMOVE
     * CONNECTIONS
     */
    int i = 0;      // index of neighbour
    /*
    while (i < numNeighbours) {
      if (weights[i] < network.architecture.minWeight()) {      // remove weights that are too low
        weights[i] = weights[numNeighbours-1];      // remove connection and
        neighbours[i] = neighbours[numNeighbours-1];   // replace with last connection
        numNeighbours--;                            // decrease nr of neighbours
        potentialNeighbours[numPotential - numNeighbours] = i;  // add neuron to potential connections
        numPotential++;
      } else {
        i++;
      }
    }
    */
  }

  public void display() {
    // I should probably make this a little more elaborate later on
    
    fill((int)(256*((v-40)/-120.0)),0, (int)(256*(1.0-((v-40)/-120.0)))); // set colour
    rect(x, y, Nw-2, Nw-2);       // create rectangle
  }

  boolean pointedAt() {
    // return whether the mouse is pointed at the neuron

    if (mouseX>x && mouseX<x+Nw && mouseY>y && mouseY<y+Nw) return true;
    else return false;
  }

  public void validateParameters(float aMin, float aMax, float bMin, float bMax, float cMin, float cMax, float dMin, float dMax) {

    // create exceptions
    IllegalArgumentException invalidA = new IllegalArgumentException("Parameter a invalid for type of neuron "+this.getClass());
    IllegalArgumentException invalidB = new IllegalArgumentException("Parameter b invalid for type of neuron "+this.getClass());
    IllegalArgumentException invalidC = new IllegalArgumentException("Parameter c invalid for type of neuron "+this.getClass());
    IllegalArgumentException invalidD = new IllegalArgumentException("Parameter d invalid for type of neuron "+this.getClass());

    // validate parameters
    if (a < aMin || a > aMax) throw invalidA;
    if (b < bMin || b > bMax) throw invalidB;
    if (c < cMin || c > cMax) throw invalidC;
    if (d < dMin || d > dMax) throw invalidD;

  }

  private void reset() {
    // reset neuron and time
    v = c;
    u = b*c;
  }

  public void resetI() {
    // Reset the input variable I to its standard value
    this.I = 0;
  }

  public void printConnections() {
    // print the connections of the neurons and their weights

    for (int i=0; i<numNeighbours; i++) {
      System.out.println(neighbours[i] + "\t" + weights[i]);
    }
  }

  public void removeConnections() {
    // reset any existing weights from the
    // neuron to other neurons
    this.weights = new float[0];
    this.neighbours = new int[0];
  }

  // getters

  public float a() {
    return this.a;
  }

  public float b() {
    return this.b;
  }

  public float c() {
    return this.c;
  }

  public float d() {
    return this.d;
  }

  // setters

  public void setConnections(int[] connectTo, float[] weights) throws IllegalArgumentException {
    // set weights to the list of neurons that is
    // inputted

    // check if input is valid
    IllegalArgumentException unequalLengthException = new IllegalArgumentException("Number of neurons and number of weights should be equal");
    if (connectTo.length != weights.length) {
      throw unequalLengthException;
    }

    this.neighbours = connectTo;
    this.weights = weights;
    this.numNeighbours = neighbours.length;
  }

  public void setI(float activation) {
    // increase I with activation
    this.I = activation;
  }

  public void set_v(float v) {
    // Set v to a certain value
    this.v = v;
  }

  public void setParameters(float a, float b, float c, float d) {
    // change parameters a, b, c and d of the network
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

}
