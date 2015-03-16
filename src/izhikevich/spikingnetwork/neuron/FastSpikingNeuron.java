package izhikevich.spikingnetwork.neuron;


public class FastSpikingNeuron extends InhibitoryNeuron {
	/**
	 * Fast Spiking Neuron
	 * a = 0.1, b = 0.2, c = -65, d = 2 
	 */

// Constructor
public FastSpikingNeuron()  {
 super(0.1, 0.2, -65, 2);
}

}
