package izhikevich.spikingnetwork.distancemetric;

import izhikevich.spikingnetwork.neuron.*;
import java.lang.*;

public class ManhattanDistance extends DistanceMetric {
	/**
	 * Manhattan distance class designed to use as function pointer
	 */
	
	public double distance(Neuron n1, Neuron n2) {
		/**
		 * Return the manhattan distance between neuron n1
		 * and neuron n2
		 */
		int distance = (Math.abs(n1.getX() - n2.getX()) + Math.abs(n1.getY() - n2.getY()))/neuronWidth();
		return distance;
}

}