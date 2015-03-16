package izhikevich.spikingnetwork.distancemetric;

import izhikevich.spikingnetwork.neuron.*;

public abstract class DistanceMetric {
	/** 
	   * class used as function
	   * pointer for distance functions
	   */

	  private int neuronWidth = 1;

	  abstract public double distance(Neuron n1, Neuron n2);

	  // setters

	  public void setNeuronWidth(int neuronWidth) {
	    this.neuronWidth = neuronWidth;
	  }

	  // getters

	  public int neuronWidth() {
	    return this.neuronWidth;
	  }


}
