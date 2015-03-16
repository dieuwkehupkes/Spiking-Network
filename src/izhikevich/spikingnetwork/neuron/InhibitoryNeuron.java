package izhikevich.spikingnetwork.neuron;

public class InhibitoryNeuron extends Neuron {
	/**
	 * Class for inhibitory neurons, i.e.:
	 * 0.02 <= a <= 0.1
	 * 0.2 <= b <= 0.3
	 * c = -65
	 * d = 2
	 */

	  // Constructor1 generate inhibitory neuron with
	  // random parameters
	  public InhibitoryNeuron() {
	    // call constructor super
	    super(0.02+0.08*Math.random(), 0.25-0.05*Math.random(), -65, 2);
	  }

	  // constructor with specified parameters
	  public InhibitoryNeuron(double a, double b, double c, double d) {
	    
	    // call constructor super
	    super(a, b, c, d);
	    validateParameters(0.02, 0.1, 0.2, 0.3, -65, -65, 2, 2);
	  }

	}