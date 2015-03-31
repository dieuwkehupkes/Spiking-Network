package izhikevich.spikingnetwork;

import processing.core.*;
import izhikevich.spikingnetwork.neuron.*;

public class MakeGraph extends PApplet {
	/*
	 * class to reproduce the figures
	 * in Izhikevich (year?).
	 * Can be run from main.
	 */

	private static final long serialVersionUID = -1066206885220849095L;

	public void setup() {

		// setup plot
		size(900,400);
		background(255);
		PFont f = createFont("Arial",16,true);
		textFont(f, 16);
		fill(0);
		
		double timeStep = 0.01;
		int steps = (int) (200/timeStep);
		int steps05 = (int) (0.5*steps);
		int steps005 = (int) (0.05*steps);
		int steps045 = (int) (0.45*steps);

		// create neurons
		RegularSpikingNeuron n1 = new RegularSpikingNeuron();    // regular spiking
		IntrinsicallyBurstingNeuron n2 = new IntrinsicallyBurstingNeuron();    // intrinsically bursting
		ChatteringNeuron n3 = new ChatteringNeuron();    // chattering
		FastSpikingNeuron n4 = new FastSpikingNeuron();     // fast-spiking
		Neuron n5 = new Neuron(0.02, 0.25, -65, 0.05);     // thalamo-cortical
		Neuron n6 = new Neuron(0.1, 0.23, -65, -1);     // resonator
		Neuron n7 = new Neuron(0.02, 0.25, -65, 2);   // low-threshold spiking
		
		n1.timeStep = timeStep;
		n2.timeStep = timeStep;
		n3.timeStep = timeStep;
		n4.timeStep = timeStep;
		n5.timeStep = timeStep;
		n6.timeStep = timeStep;
		n7.timeStep = timeStep;

		// create data arrays with their behaviour
		float[][] plotdata1 = n1.plot_v(10, steps);
		float[][] plotdata2 = n2.plot_v(10, steps);
		float[][] plotdata3 = n3.plot_v(10, steps);
		float[][] plotdata4 = n4.plot_v(10, steps);

		n5.plot_v(0, steps005); float[][] plotdata5 = n5.plot_v(1, steps);

		n5.reset();
		float[][] n5plt1 = n5.plot_v(-25, steps05);
		float[][] n5plt2 = n5.plot_v(0, steps05);

		float[][] n6plt1 = n6.plot_v(1.5, steps045);
		float[][] n6plt2 = n6.plot_v(3, steps005);
		float[][] n6plt3 = n6.plot_v(1.5, steps05);

		float[][] plotdata8 = n7.plot_v(10, steps);

		float[][] plotdata6 = new float[steps][2];
		float[][] plotdata7 = new float[steps][2];

		// create arrays for neurons with input variation 
		for (int i=0; i<steps05; i++) {
			int j = i+steps05;
			plotdata6[i] = n5plt1[i];
			plotdata6[j][0] = n5plt2[i][0]+90;
			plotdata6[j][1] = n5plt2[i][1];

			if (i<steps045) { // in first bit of input
				plotdata7[i] = n6plt1[i];
			}
			else { // in extra input bit
				plotdata7[i][1] = n6plt2[i-steps045][1]; plotdata7[i][0] = n6plt2[i-steps045][0] + 90;
			}

			plotdata7[j][0] = n6plt3[i][0]+90;
			plotdata7[j][1] = n6plt3[i][1];
		}

		for (int i=0; i<steps005; i++) {
			plotdata7[i+steps045][1] = n6plt2[i][1];
		}

		// transform to plot
		float[][] plt1 = Collection.shift_and_scale(plotdata1, 20, 90, 0.9, 1);
		float[][] plt2 = Collection.shift_and_scale(plotdata2, 260, 90, 0.9, 1);
		float[][] plt3 = Collection.shift_and_scale(plotdata3, 500, 100, 0.9, 1);
		float[][] plt4 = Collection.shift_and_scale(plotdata4, 740, 100, 0.9, 1);
		float[][] plt5 = Collection.shift_and_scale(plotdata5, 20, 290, 0.9, 1);
		float[][] plt6 = Collection.shift_and_scale(plotdata6, 260, 290, 0.9, 1);
		float[][] plt7 = Collection.shift_and_scale(plotdata7, 500, 290, 0.9, 1);
		float[][] plt8 = Collection.shift_and_scale(plotdata8, 740, 290, 0.9, 1);

		// plot data
		float[][][] plot_data = {plt1, plt2, plt3, plt4, plt5, plt6, plt7, plt8};
		for (float[][] plt: plot_data) {
			// line(plt[0][0]-20, plt[0][1], plt[0][0], plt[0][1]);
			for (int i=0; i<plt.length-1; i++) {
				line(plt[i][0], plt[i][1], plt[i+1][0], plt[i+1][1]);
			}
		}

		// create titles
		text ("Regular spiking (RS)", 20, 20);
		text ("Intrinsically bursting (IB)", 250, 20);
		text ("Chattering (CH)", 470, 20);
		text ("Fast spiking (FS)", 690, 20);
		text ("Thalamo-cortical (TC)", 20, 210);
		text ("Thalamo-cortical (TC)", 240, 210);
		text ("Resonator (RZ)", 460, 210);
		text ("low-threshold spiking (LTS)", 680, 210);

		// text("a=0.02, b=0.2\nc=-50, d=1.2\nnr of spikes=26", 40, 200);
		// text("a=0.1, b=0.2\nc=-65, d=2\nnr of spikes=26", 270, 200);

		// save("../graphs/izhikevich_timestep01.jpg");

	}
	
	public void draw() {
	}
	
	public void keyPressed() {
		exit();
	}

}
