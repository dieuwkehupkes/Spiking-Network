package izhikevich.spikingnetwork.neuron;

public class ChatteringNeuron extends ExcitatoryNeuron {
	/**
	 * Chattering neuron
	 * a = 0.02, b=0.2, c=-50, d=2
	 */

	  // Constructor
	  public ChatteringNeuron() {
	    super(0.02, 0.2, -50, 2);
	  }

}
