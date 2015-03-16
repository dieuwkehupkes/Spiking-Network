package izhikevich.spikingnetwork.neuron;


public class RegularSpikingNeuron extends ExcitatoryNeuron {
	/*
	 *  Regular Spiking Neuron
	 *  a = 0.02, b = 0.2, c = -65, d = 8
	 */

	// Constructor
	public RegularSpikingNeuron() {
		super(0.02, 0.2, -65, 8);
	}
}