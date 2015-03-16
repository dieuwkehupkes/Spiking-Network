package izhikevich.spikingnetwork.training;

import izhikevich.spikingnetwork.neuron.*;
import izhikevich.spikingnetwork.training.*;

public class HeuristicalHebbian implements Training {
	// Heuristic implementation of Hebbian learning

	public double updateExistingConnection(Neuron n1, Neuron n2) {
		/** 
		 * Update existing connection based on recent activity
		 * of both neurons.
		 */
		double update = 0.0;

		if (n1.fired) {     // check if n2 contributed to firing
			update = findIncrement(n1.lastTimeFired, n2.lastTimeFired);
		} else if (n2.lastTimeFired <= n1.lastTimeFired) {  // weight was updated when n1 fired last time
		} else {        // n1 was not activated through activation of n2
			update = 0.1*findIncrement(n1.lastTimeFired, n2.lastTimeFired);
		}

		return update;
	}

	public double updateNonExistingConnection(Neuron n1, Neuron n2) {
		double update = 0.0;
		return update;
	}

	public double findIncrement(int fireTime1, int fireTime2) {
		/**
		 * Compute increment based on how far apart
		 * the two neurons fired.
		 */
		double increment = incrementUnit;
		if (fireTime1 == fireTime2) {   // no causal relationship
			increment *= 0.0;
		} else {        // if firetime2 is close enough to firetime1, increase weight
			increment/=(fireTime1-fireTime2);
		}

		return increment;
	}

}