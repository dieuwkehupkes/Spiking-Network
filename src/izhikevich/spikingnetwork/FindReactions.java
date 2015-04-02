package izhikevich.spikingnetwork;

import processing.core.*;

import izhikevich.spikingnetwork.neuron.*;

public class FindReactions extends PApplet {

	/**
	 * Check how the response of a neuron depends on input of other
	 * neurons and the weights between these neurons
	 */

	Neuron n1, n2, n3, o1;
	int[] neighbours;
	NeuralNetwork network;
	int Nn = 4;
	int Nw = 20;
	float a = (float) 0.02;
	float b = (float) 0.2;
	float c = (float) -65;
	float d = (float) 0.1;
	double[] weights = {0, 0, 0};

	private static final long serialVersionUID = 7416187811109690854L;

	public void setup() {

		// setup window
		size(900, 500);
		background(255);

		// draw axis
		float scaleX = (float) 10;
		float scaleY = (float) 10;

		int xStart = 30; int xEnd = 400; int yStart = 300; int yEnd = 30;
		drawAxis(xStart, xEnd, yStart, yEnd);
		int xBenchValue = 500; int yBenchValue = 500;
		int xBench = (int) (xStart+xBenchValue/scaleX); int yBench = (int) (yStart - yBenchValue/scaleY);
		drawScale(xStart, xEnd, xBench, xBenchValue, yStart, yEnd, yBench, yBenchValue);
		System.out.println("scaledrawn");
		
		// set timestep
		double timeStep = 0.1;
		int simLength = 1000;

		// create input neurons
		n1 = new Neuron(a, b, c, d);	// regular spiking neuron
		n2 = new Neuron(a, b, c, d);	// regular spiking neuron
		n3 = new Neuron(a, b, c, d);	// regular spiking neuron

		// create output neuron to model
		o1 = new Neuron(0.02, 0.2, -65, 8);

		// Create network with neurons
		Neuron[] neurons = {n1, n2, n3, o1};
		network = new NeuralNetwork(neurons, Nn, Nw);

		// add neighbours to neuron o1 
		neighbours = new int[]{1, 2, 3};
		o1.setNeighbours(neighbours);

		// set fixed input for n1, n2 and n3
		n1.setI(100.0);
		n2.setI(100.0);
		n3.setI(100.0);

		// set timestep
		n1.timeStep = timeStep;
		n2.timeStep = timeStep;
		n3.timeStep = timeStep;
		o1.timeStep = timeStep;

		// plot behaviour of input neuron
		
		/*
		float[][] plt1 = network.plotV(1, 1000);
		float[][] pltData = Collection.shift_and_scale(plt1, 40, 400, 2.0, 1.0);
		for (int i=0; i<plt1.length-1; i++) {
			line(pltData[i][0], pltData[i][1], pltData[i+1][0], pltData[i+1][1]);
		}
		*/
		


		// Plot nr of spikes against sum inputweights
		// plotSpikesWeights(simLength, (float) 0.1, (float) 2., xStart, yStart);
		
		// plot nr of spikes against input frequence
		plotSpikesInputFreq(simLength, scaleX, scaleY, xStart, yStart);

		// save("../graphs/reactionsRS3RSi01.jpg");
	}

	public void draw() {

	}
	
	private void plotSpikesInputFreq(int simLength, float scaleX, float scaleY, int x0, int y0) {
		
		fill(2);
		weights[0] = 700; weights[1] = 700; weights[2] = 700;
		o1.setWeights(weights);
		
		for (d= (float) 0.1; d<=20; d+=0.2) {
			n1.setParameters(a, b, c, d);
			n2.setParameters(a, b, c, d);
			n3.setParameters(a, b, c, d);
			
			int spikes = network.getNrOfSpikes(3, 20);
			// int spikes1 = network.getNrOfSpikes(0, 20);
			// network.plotV(0, simLength);
			// int spikes2 = n1.getNrOfSpikes(simLength);
			// int spikes3 = n2.getNrOfSpikes(simLength);
			
			//System.out.println("d: "+d+"\tspikes: "+spikes+"\tspikes1: "+spikes1);
			// System.out.println("d: "+d+"\tspikes: "+spikes);
			
			point(scaleX*d+30, -(scaleY*spikes) + 200);
			
			}	
		}
	
	@SuppressWarnings("unused")
	private void plotSpikesWeights(int simLength, float scaleX, float scaleY, int x0, int y0) {
		// print axis info
		fill(50);
		textSize(15);
		text("sum weights", 390, 235);
		text("#spikes", 10, 18);
		textSize(10);

		// plot for different sums of weights
		float sumWeights =(float) 0.0;

		// set strokeweight
		strokeWeight(2);
		for (int i=0; i<simLength; i++) {
			// increment weights
			weights[i%3]+= 0.2;
			sumWeights+=0.2;

			// add weights from neurons n1, n2 and n3 to 01
			o1.setConnections(neighbours, weights);

			network.reset();
			int nrOfSpikes = network.getNrOfSpikes(3, 100);

			point(scaleX*sumWeights+30, -(scaleY*nrOfSpikes) + 200);
		}
	}


	private void drawAxis(int xStart, int xEnd, int yStart, int yEnd) {
		line(xStart, yStart, xStart, yEnd);
		line(xStart, yStart, xEnd, yStart);
	}

	private void drawScale(int x0, int xEnd, int xBench, int xBenchValue, int y0, int yEnd, int yBench, int yBenchValue) {
		// draw x-scale
		fill(50);
		int x = xBench;
		int i = xBenchValue;
		while (x < xEnd) {
			line(x, y0-2, x, y0+2);
			text(i, x-10, y0+15);
			i += xBenchValue;
			x += (xBench-x0);
		}

		// draw y-scale
		int y = yBench;
		i = yBenchValue;
		while (y > yEnd) {
			line(x0-2, y, x0+2, y);
			text(i, x0-25, y+5);
			i += yBenchValue;
			y += (yBench-y0);
		}
	}

	public void keyPressed() {
		exit();
	}

}
