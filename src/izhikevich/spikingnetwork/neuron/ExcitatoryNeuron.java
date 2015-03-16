package izhikevich.spikingnetwork.neuron;

import processing.core.*;

public class ExcitatoryNeuron extends Neuron {
	/**
	 * class for excitatory neurons, i.e.:
	 * a = 0.02
	 * b = 0.2
	 * -65 < c < -50
	 * 2 < d < 8
	 */
	// Constructor1 generate inhibitory neuron with
	// random parameters
	public ExcitatoryNeuron() {
		// call constructor super
		super(0.02, 0.2, -65+15*Math.random(), 8-6*Math.random());
	}

	// constructor with specified parameters
	public ExcitatoryNeuron(double a, double b, double c, double d) {

		// call constructor super
		super(a, b, c, d);
		validateParameters(0.02, 0.02, 0.2, 0.2, -65, -50, 2, 8);
	}

}
