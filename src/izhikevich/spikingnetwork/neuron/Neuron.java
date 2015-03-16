package izhikevich.spikingnetwork.neuron;

import izhikevich.spikingnetwork.*;
import processing.core.*;

public class Neuron {
	/**
	 * Class representing a neuron
	 */
	  private double a, b, c, d;
	  double u;
	public double v;        // u and v when they were last computed
	  private double I;      // external input to neuron
	  public boolean fired = false;
	  private boolean trainingMode = false;
	  public int lastTimeFired;    // last round the neuron fired
	  int t=0;            // cur round
	  double timeStep = 0.1;

	  public NeuralNetwork network;    // the network the neuron is part of
	  private int neighbours[];     // indices pointing to the neighbours of the neuron
	  private double weights[];        // weights to the neighbours
	  public int numNeighbours = 0;
	  int numPotential = 30;        // CHANGE THIS LATER TO BE PART OF ARCHITECTURE!
	  int potentialNeighbours[] = new int[numPotential];        // indices pointing to potential neighbours
	  
	  protected int x;          // x coordinate for displaying neuron
	  protected int y;          // y coordinate for displaying neuron
	  protected int Nw;			// neuron width

	  // Constructor
	  public Neuron(double a, double b, double c, double d) {
	    // Initialise neuron parameters
	    setParams(a, b, c, d);
	    reset();
	  }

	  public void setParams(double a, double b, double c, double d) {
	    // set parameters of the neuron
	    this.a = a;
	    this.b = b;
	    this.c = c;
	    this.d = d;
	  }

	  public float[][] show_spike_behaviour(double I, int nr_of_steps) {
	    // Show behaviour of neuron as a result of a constant input current
	    
	    float[][] time_potential = new float[nr_of_steps][2];     // declare array to store output
	    int nr_of_spikes = 0;

	    // reset();     // Reset values as if neuron was just created

	    // compute behaviour
	    for (int i=0; i<nr_of_steps; i++) {
	      double cur_time = i*0.1;
	      time_potential[i][0] = (float) cur_time;
	      time_potential[i][1] = (float) v;
	      computeNext(I);
	      if (fired) nr_of_spikes++;
	    }

	    System.out.println(nr_of_spikes++);
	    
	    return time_potential;
	  }

	  void setTimeStep(double timeStep) {
	    // Set the timestep for updating

	    this.timeStep = timeStep;
	  }

	  private void computeNext(double I) {
	    // Compute the membrane potential one time step t from now.
	    // Current input to the neuron is I.

	    // if potential crosses threshold, reset

	    resetI();   // reset the input that was used to compute the update
	    t++;    // increase round

	    if (v>30) {
	      v=c;
	      u=u+d;
	      this.fired = true;
	      this.lastTimeFired = t;
	      return;
	    }

	    v=v+0.5*timeStep*(0.04*Math.pow(v,2)+5*v+140-u+I); //% step 0.5 ms
	    v=v+0.5*timeStep*(0.04*Math.pow(v,2)+5*v+140-u+I); //% for numerical
	    u=u+timeStep*a*(b*v-u); //% stability
	    fired = false;

	  }

	  void update() {
	    // Update with 0.0 input from outside the network
	    update(0.0);
	  }

	  public void update(double thalamicInput) {
	    /**
	     * Update values v and u of the network, and
	     * update the weights if network is in
	     * training mode.
	     */
	    
	    double neighbourActivation = computeNeighbourActivation();
	    double inputActivation = this.I + thalamicInput + neighbourActivation;
	    computeNext(inputActivation);

	    if (isTrainingMode()) updateWeights();        // update weights
	  }

	  private double computeNeighbourActivation() {
	    // Compute the activation the neuron gets from its
	    // neighbours
	    
	    double neighbourActivation = 0;
	    for (int i=0; i<numNeighbours; i++) {
	      if (network.getNeurons()[getNeighbours()[i]].fired) neighbourActivation+=getWeights()[i];
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
	      double update = network.updateExistingConnection(this, network.getNeurons()[i]);
	      getWeights()[i] = ( getWeights()[i]+update < network.getMinWeight() || getWeights()[i]+update > network.getMaxWeight()) ? getWeights()[i] : getWeights()[i]+update;
	    }

	    rmLowConnections();     // remove connections below minimum weight of the network
	    
	    // loop over close neurons to see if a connection should be established
	    int i = 0;
	    while (numNeighbours < network.getMaxNumNeighbours() && i < potentialNeighbours.length) {
	      // network.updateNonExistingConnection(this, network.getNeurons()[potentialNeighbours[i]]);
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
	    /*
	    int i = 0;      // index of neighbour
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

	  public void validateParameters(double aMin, double aMax, double bMin, double bMax, double cMin, double cMax, double dMin, double dMax) {

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

	  public void reset() {
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
	      System.out.println(getNeighbours()[i] + "\t" + getWeights()[i]);
	    }
	  }

	  public void removeConnections() {
	    // reset any existing weights from the
	    // neuron to other neurons
	    this.setWeights(new double[0]);
	    this.setNeighbours(new int[0]);
	  }

	  // getters

	  public double a() {
	    return this.a;
	  }

	  public double b() {
	    return this.b;
	  }

	  public double c() {
	    return this.c;
	  }

	  public double d() {
	    return this.d;
	  }

	  // setters

	  public void setConnections(int[] connectTo, double[] weights) throws IllegalArgumentException {
	    // set weights to the list of neurons that is
	    // inputted

	    // check if input is valid
	    IllegalArgumentException unequalLengthException = new IllegalArgumentException("Number of neurons and number of weights should be equal");
	    if (connectTo.length != weights.length) {
	      throw unequalLengthException;
	    }

	    this.setNeighbours(connectTo);
	    this.setWeights(weights);
	    this.numNeighbours = getNeighbours().length;
	  }

	  public void setI(double activation) {
	    // increase I with activation
	    this.I = activation;
	  }

	  public void set_v(double v) {
	    // Set v to a certain value
	    this.v = v;
	  }

	  public void setParameters(double a, double b, double c, double d) {
	    // change parameters a, b, c and d of the network
	    this.a = a;
	    this.b = b;
	    this.c = c;
	    this.d = d;
	  }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int[] getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(int neighbours[]) {
		this.neighbours = neighbours;
	}

	public boolean isTrainingMode() {
		return trainingMode;
	}

	public void setTrainingMode(boolean trainingMode) {
		this.trainingMode = trainingMode;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double weights[]) {
		this.weights = weights;
	}

	public int getNw() {
		return Nw;
	}

	public void setNw(int nw) {
		Nw = nw;
	}

	}
