package izhikevich.spikingnetwork;

import izhikevich.spikingnetwork.neuron.Neuron;
import processing.core.*;

public class PlotNeuron extends PApplet {
	/**
	 * 
	 */

	private static final long serialVersionUID = -8259725764770574359L;

	public void setup() {
		
		size(300, 200);
		background(255);
		fill(0);

		// parameters for neuron to be plotted
		double a = 0.02;
		double b = 0.25;
		double c = -65;
		double d = 0.05;

		// create neuron and behavioral data
		Neuron n = new Neuron(a, b, c, d);
		float[][] plt = n.show_spike_behaviour(10, 1000);
		
		int shiftX = 10;
		int shiftY = 90;
		float scaleX = (float) 2.0;
		float scaleY = (float) 1.0;

		for (int i=0; i<plt.length-1; i++) {
			float xFro = scaleX * (plt[i][0] + shiftX);
			float xTo = scaleX * (plt[i+1][0] + shiftX);
			float yFro = scaleY *(-plt[i][1] + shiftY);
			float yTo = scaleY *(-plt[i+1][1] + shiftY);

			line(xFro, yFro, xTo, yTo);
		}

	}

}
