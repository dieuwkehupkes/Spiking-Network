// class representing a neuron

class Neuron {
  // Class representing a neuron
  private float a, b, c, d;
  float u, v;        // u and v when they were last computed
  private float I;      // extrenal input to neuron
  boolean fired = false;
  float timeStep = 0.1;

  NeuralNetwork network;    // the network the neuron is part of
  int neighbours[];     // indices pointing to the neighbours of the neuron
  float weights[];        // weights to the neighbours
  int numNeighbours=0;

  int x;        // x coordinate for displaying neuron
  int y;        // y coordinate for displaying neuron
  int Nw;       // Neuron width

  boolean mousePointedAt; //set to true when the mouse is pointed at the neuron

  // Constructor1 generate neuron with specifief values
  Neuron(float a, float b, float c, float d) {
    // Initialise neuron parameters
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;

    // Set inital values of neuron
    reset();
  }

  // Constructor2 generate random excitatory neuron
  Neuron() {
    float re=random(0.1,1.0);
    a=0.02;
    b=0.2;
    c=-65+15*pow(re,2);
    d=8-6*pow(re,2);

    reset();
  }


  float[][] show_spike_behaviour(float I, int nr_of_steps) {
    // Show behaviour of neuron as a result of a constant input current
    
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
      fired = true;
      return;
    }

    v=v+0.5*timeStep*(0.04*pow(v,2)+5*v+140-u+I); //% step 0.5 ms
    v=v+0.5*timeStep*(0.04*pow(v,2)+5*v+140-u+I); //% for numerical
    u=u+timeStep*a*(b*v-u); //% stability
    fired = false;

  }

  void update() {
    // update the neuron from the weights
    float inputActivation = this.I + computeNeighbourActivation();
    if (this.I > 0.0) System.out.println(this.I+" "+inputActivation);
    computeNext(inputActivation);
  }

  void update(float thalamicInput) {
    // compute the update considering also
    // thalamic input
    
    float neighbourActivation = computeNeighbourActivation();
    float inputActivation = this.I + thalamicInput + neighbourActivation;
    computeNext(inputActivation);
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

  public void display() {
    // I should probably make this a little more elaborate later on
    
    fill((int)(256*((v-40)/-120.0)),(int)(256*(1.0-((v-40)/-120.0))),0); // set colour
    rect(x, y, Nw-2, Nw-2);       // create rectangle
  }

  boolean pointedAt() {
    // return whether the mouse is pointed at the neuron

    if (mouseX>x && mouseX<x+Nw && mouseY>y && mouseY<y+Nw) return true;
    else return false;
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

  public void removeConnections() {
    // reset any existing weights from the
    // neuron to other neurons
    this.weights = new float[0];
    this.neighbours = new int[0];
  }

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


