package izhikevich.spikingnetwork;

import izhikevich.spikingnetwork.neuron.Neuron;
import processing.core.*;
import izhikevich.spikingnetwork.Collection;

public class PlotNeuron extends PApplet {
	/**
	 * Dynamic plot of a neuron and its path through statespace
	 */

	private static final long serialVersionUID = -8259725764770574359L;

	// parameters for neuron to be plotted
	float I = 10;
	double a = 0.01;
	double b = 0.2;
	double c = -65;
	double d = 1;
	
	// create neuron, set time = 0
	Neuron n = new Neuron(a, b, c, d);

	// shift for neuron behaviour
	float shiftX = 20;
	float shiftY = 90;
	double scaleX = 1.5;
	double scaleY = 1.0;
	int simLength = 800;
	
	// create arrays to store data, assumes timestep taken is 0.1
	float[][] plt = new float[simLength][2];
	float[][] data = new float[simLength][2];
	int curIndex = 0;	// index to fill plotdata
	int prevIndex = -1; //
	float curTime = (float) 0.0;
	float curShift = (float) (-0.1*simLength);
	int from, to;
	
	boolean keyP = false;
	
	
	public void setup() {
		size(200, 200);
		background(255);
		fill(0);
		frameRate(100);

		n.t = 0;
		n.v = 35;
		
		plt[0][0] = 0;
		plt[0][1] = (float) n.v;
		
		strokeWeight(1);
		stroke(0);
		
	}

	public void draw() {
		
		if (! keyP) {
			return;
		}
		
		prevIndex = curIndex;				// store previous index
		curTime += n.timeStep;				// increment time
		curShift += n.timeStep;				// increment shift
		curIndex = (curIndex +1) % simLength;	// increment index
	
		// generate new datapoint
		n.update(I);
		plt[curIndex][0] = curTime;
		plt[curIndex][1] = (float) n.v;


		if (curShift <= 0) {	// if curShift <= 0, plot next datapoint and return
			data = Collection.shift_and_scale(plt, shiftX, shiftY, scaleX, scaleY);
			line(data[prevIndex][0], data[prevIndex][1], data[curIndex][0], data[curIndex][1]);
			return;
		}

		// clear background
		background(255);
		
		// update x-shift factor
		shiftX -= 0.1;
		
		// rescale data and plot
		float[][] data = Collection.shift_and_scale(plt,  shiftX, shiftY, scaleX, scaleY);
		
		for (int i = 1; i<simLength; i++) {
			from = (i+curIndex) % simLength;
			to = (from +1) % simLength;
			line(data[from][0], data[from][1], data[to][0], data[to][1]);
		}

		
	}

	public void keyPressed() {
		if (key == ' ') {
			keyP = true;
		}
		if (key == 'q') {
			exit();
		}
	}
	
	public void keyReleased() {
		//keyP = false;
	}
	
}
