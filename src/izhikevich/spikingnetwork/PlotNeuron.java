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
	float I = 100;
	double a = 0.1;
	double b = 0.2;
	double c = -65;
	double d = 2;
	
	int simLength = 100;

	// create neuron, set time = 0
	Neuron n = new Neuron(a, b, c, d);

	int shiftX = 10;
	int shiftY = 90;
	double scaleX = 2.0;
	double scaleY = 1.0;
	
	float[][] plotData = new float[simLength][2];
	float[][] uvData = new float[simLength][2];
	int curIndex = 0;	// index to fill plotdata
	int prevIndex = -1; //
	float curTime = (float) 0.0;
	
	int rgbRed, rgbGreen, rgbBlue;
	float r, g, bl;
	float colorStep = 255/simLength;

	
	public void setup() {
		size(450, 400);
		background(255);
		fill(0);
		frameRate(50);

		n.t = 0;

		plotData[0][0] = (float) 0.0;
		plotData[0][1] = (float) n.u;
		
		uvData[0][0] = (float) n.u;
		uvData[0][1] = (float) n.v;
		
		rgbRed = 100;
		rgbGreen = 0;
		rgbBlue = 255;
		
		r = (float) rgbRed;
		g = (float) rgbGreen;
		bl = (float) rgbBlue;
		
		n.timeStep = 1;

		}

	public void draw() {
		
		if (curIndex == (simLength - 2)) {
			noLoop();
			// save("../graphs/neuron_statespace_trajectory1.png");
		}
		
		r += colorStep;
		g += colorStep;
		bl -= colorStep;
		
		curIndex = (curIndex+1) % 1000;
		prevIndex = (prevIndex+1) % 1000;
		curTime += n.timeStep;

		// update neuron
		n.update(I);
		
		plotData[curIndex][0] = curTime;
		plotData[curIndex][1] = (float) n.v;
		
		uvData[curIndex][0] = (float) n.u;
		uvData[curIndex][1] = (float) n.v;

		float[][] plt = Collection.shift_and_scale(plotData, shiftX, shiftY, scaleX, scaleY);
		// float[][] plt = Collection.shift_and_scale(plotData, shiftX, shiftY+250, scaleX, 0.8);
		// scale output and draw plot
		strokeWeight(1);
		stroke(0);
		line(plt[prevIndex][0], plt[prevIndex][1], plt[curIndex][0], plt[curIndex][1]);
		
		float[][] plt2 = Collection.shift_and_scale(uvData, shiftX+20, shiftY+200, 5, scaleY);
		// float[][] plt2 = Collection.shift_and_scale(uvData, shiftX+15, shiftY+2800, scaleX, 0.3);

		strokeWeight(2);
		rgbRed = (int) r;
		rgbBlue = (int) bl;
		rgbGreen = (int) g;
		stroke(rgbGreen, 0, rgbBlue);
		line(plt2[prevIndex][0], plt2[prevIndex][1], plt2[curIndex][0], plt2[curIndex][1]);
		
	}

	public void keyPressed() {
		exit();
	}

}
