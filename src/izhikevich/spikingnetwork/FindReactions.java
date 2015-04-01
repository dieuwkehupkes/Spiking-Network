package izhikevich.spikingnetwork;

import processing.core.*;

import izhikevich.spikingnetwork.neuron.*;

public class FindReactions extends PApplet {

	/**
	 * Check how the response of a neuron depends on input of other
	 * neurons and the weights between these neurons
	 */

	Neuron n1, n2, n3, o1;
	NeuralNetwork network;
	int Nn = 4;
	int Nw = 20;

	private static final long serialVersionUID = 7416187811109690854L;

	public void setup() {

		size(1200, 600);
		background(255);

		// set timestep
		double timeStep = 0.1;
		int simLength = 3500;

		// create input neurons
		n1 = new Neuron(0.02, 0.2, -65., 0.1);	// regular spiking neuron
		n2 = new Neuron(0.02, 0.2, -65., 0.1);	// regular spiking neuron
		n3 = new Neuron(0.02, 0.2, -65., 0.1);	// regular spiking neuron

		// create output neuron to model
		o1 = new Neuron(0.02, 0.2, -65, 8);

		// Create network with neurons
		Neuron[] neurons = {n1, n2, n3, o1};
		network = new NeuralNetwork(neurons, Nn, Nw);

		// add neighbours to neuron o1 
		int[] neighbours = {1, 2, 3};
		o1.setNeighbours(neighbours);

		// set fixed input for n1, n2 and n3
		n1.setI(10.0);
		n2.setI(10.0);
		n3.setI(10.0);

		// set timestep
		n1.timeStep = timeStep;
		n2.timeStep = timeStep;
		n3.timeStep = timeStep;
		o1.timeStep = timeStep;

		// plot behaviour of input neuron

		float[][] plt1 = network.plotV(1, 1000);
		float[][] pltData = Collection.shift_and_scale(plt1, 40, 400, 2.0, 1.0);
		for (int i=0; i<plt1.length-1; i++) {
			line(pltData[i][0], pltData[i][1], pltData[i+1][0], pltData[i+1][1]);
		}

		// plot for different sums of weights
		double[] weights = {0, 0, 0};
		float sumWeights =(float) 0.0;

		fill(50);
		textSize(15);
		text("sum weights", 390, 235);
		text("#spikes", 10, 18);
		textSize(10);

		// draw axis
		int xStart = 30; int xEnd = 800; int yStart = 200; int yEnd = 30;
		drawAxis(xStart, xEnd, yStart, yEnd);
		int x500 = 80; int y50 = 100;
		drawScale(xStart, x500, xEnd, yStart, y50, yEnd);
		
		// generate points and draw plot
		strokeWeight(2);
		for (int i=0; i<simLength; i++) {
			// increment weights
			weights[i%3]+= 2;
			sumWeights+=2;

			// add weights from neurons n1, n2 and n3 to 01
			o1.setConnections(neighbours, weights);

			network.reset();
			int nrOfSpikes = network.getNrOfSpikes(3, 100);

			point((float) (0.1*sumWeights+30), -(2*nrOfSpikes) + 200);

		}

		// save("../graphs/reactions.jpg");
	}

	public void draw() {

	}

	private void drawAxis(int xStart, int xEnd, int yStart, int yEnd) {
		line(xStart, yStart, xStart, yEnd);
		line(xStart, yStart, xEnd, yStart);
	}

	private void drawScale(int x0, int x500, int xEnd, int y0, int y50, int yEnd) {
		// draw x-scale
		int x = x500;
		int i = 500;
		while (x < xEnd) {
			line(x, y0-2, x, y0+2);
			text(i, x-10, y0+15);
			i += 500;
			x += (x500-x0);
		}

		// draw y-scale
		int y = y50;
		i = 50;
		while (y > yEnd) {
			line(x0-2, y, x0+2, y);
			text(i, x0-25, y+5);
			i += 50;
			y += (y50-y0);
		}
	}

	public void keyPressed() {
		exit();
	}

}
