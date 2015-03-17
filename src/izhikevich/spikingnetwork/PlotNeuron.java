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
		float I = 10;
		double a = 0.02;
		double b = 0.18;
		double c = -65;
		double d = 0.05;

		// create neuron and behavioral data
		Neuron n = new Neuron(a, b, c, d);
		float[][] plt_v = n.plot_v(I, 1000);

		int shiftX = 10;
		int shiftY = 90;
		double scaleX = 2.0;
		double scaleY = 1.0;
		
		float[][] pltV = shift_and_scale(plt_v, shiftX, shiftY, scaleX, scaleY);


		for (int i=0; i<pltV.length-1; i++) {
			line(pltV[i][0], pltV[i][1], pltV[i+1][0], pltV[i+1][1]);
		}
	}

	public float[][] shift_and_scale(float[][] input, int shiftx, int shifty, double scalex, double scaley) {
		// function for shifting and scaling values to plot them
		float[][] output = new float[input.length][input[0].length];

		int counter = 0;
		for (float[] tuple: input) {
			output[counter]=tuple;

			// apply scaling and shifting of time variable
			output[counter][0] += shiftx;
			output[counter][0] *= scalex;

			// apply scaling and shifting of potential variable
			output[counter][1] *= -1;        // flip sign to get direction right
			output[counter][1] += shifty;
			output[counter][1] *= scaley;

			// increase counter
			counter++;
		}

		return output;
	}

	public void keyPressed() {
		exit();
	}

}
