package izhikevich.spikingnetwork.distancemetric;

import izhikevich.spikingnetwork.neuron.Neuron;
import java.lang.*;

public class EuclideanDistance extends DistanceMetric {

	public double distance(Neuron n1, Neuron n2) {
		/**
		 * Return the euclidean distance between neuron n1
		 * and neuron n2
		 */
		double distance = Math.sqrt(Math.pow((n1.getX() - n2.getX())/neuronWidth(), 2) + Math.pow((n1.getY() - n2.getY())/neuronWidth(), 2));
		return distance;
	}
}
