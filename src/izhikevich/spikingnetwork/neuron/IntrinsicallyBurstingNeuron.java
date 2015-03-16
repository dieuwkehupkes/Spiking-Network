package izhikevich.spikingnetwork.neuron;

public class IntrinsicallyBurstingNeuron extends ExcitatoryNeuron {
	/**
	 * Intrinsically Bursting Neuron
	 * a = 0.02, b=0.2, c=-55, d=4
	 */

	// Constructor
	public IntrinsicallyBurstingNeuron() {
		super(0.02, 0.2, -55, 4);
	}
}