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

	// simulation length
	int simLength = 500;

	// scale fa8ctor
	float scaleX = (float) 4;
	float scaleY = (float) 10;

	// start and endpoint of graph
	float xStart =40; float xEnd = 850; float yStart = 800; float yEnd = 30;
	
	// benchmark values for scale graph
	float xBenchValue = 50; float yBenchValue = 10;
		
	private static final long serialVersionUID = 7416187811109690854L;

	public void setup() {

		// setup window
		size(1000, 900);
		background(255);

		// draw axis

		drawAxis(xStart, xEnd, yStart, yEnd);
		System.out.println("Axis drawn");
		float xBench = xStart+xBenchValue*scaleX; float yBench = yStart - yBenchValue*scaleY;
		drawScale(xStart, xEnd, xBench, xBenchValue, yStart, yEnd, yBench, yBenchValue);
		System.out.println("scale drawn");
		
		// set timestep
		double timeStep = 0.1;

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

		// Plot nr of spikes against sum inputweights
		// plotSpikesWeights(simLength, (float) 0.1, (float) 2., xStart, yStart);
		
		// plot nr of spikes against input frequence
		plotSpikesInputFreq();
		
		// plot dependency of spike frequency on input weight
		// plotDependencyD();

	}

	public void draw() {

	}
	
	public void plotSpikesInputFreq() {

		weights[0] = 100; weights[1] = 100; weights[2] = 100;
		o1.setWeights(weights);
		
		textSize(24);
		text("Input", 450, 850);
		text("Output", 20, 25);
		

		save("../graphs/Input3nOutput.jpg");
		
		strokeWeight(5);
		
		for (float k = (float) 0.1; k<=40; k+=0.1) {
			n1.setParameters(a, b, c, k);
			n2.setParameters(a, b, c, k);
			n3.setParameters(a, b, c, k);
			
			int spikes = network.getNrOfSpikes(3, simLength);
			int spikes1 = network.getNrOfSpikes(0, simLength);
			int spikes2 = network.getNrOfSpikes(0, simLength);
			int spikes3 = network.getNrOfSpikes(0, simLength);
			int sumSpikes = spikes1 + spikes2 + spikes3;
			
			// System.out.println("d: "+d+"\tspikes o: "+spikes+"\tspikes1: "+spikes1+"\tspikes2: "+spikes2+"\tspikes3: "+spikes3);
			
			stroke(35, 35, 35);
			point(scaleX*sumSpikes+xStart, -(scaleY*spikes) + yStart);
			
			}	
		
	}
	
	private void plotDependencyD() {
		
		fill(255);
		rect(545, 75, 400, 340);
		strokeWeight(8);
		textSize(26);
		fill(255,50,50);
		stroke(255, 50, 50);
		point(590, 90); point(580, 90); point(570, 90); point(560, 90);
		text("Input neurons", 602, 100);
		fill(0, 0, 0);
		stroke(0);
		text("Output neuron", 600, 225);
		point(590, 215); point(580, 215); point(570, 215); point(560, 215);
		
		textSize(20);
		text("a= "+a, 560, 125);
		text("b= "+b, 560, 145);
		text("c= "+c, 560, 165);

		text("a= "+a, 560, 245);
		text("b= "+b, 560, 265);
		text("c= "+c, 560, 285);
		text("d= "+d, 560, 305);
		
		weights[0] = 100; weights[1] = 100; weights[2] = 100;
		o1.setWeights(weights);
		
		textSize(24);
		text("d", 450, 850);
		text("#spikes", 20, 25);
		

		save("../graphs/dependencyD.jpg");
		
		strokeWeight(5);
		
		for (float k = (float) 0.1; k<=40; k+=0.1) {
			n1.setParameters(a, b, c, k);
			n2.setParameters(a, b, c, k);
			n3.setParameters(a, b, c, k);
			
			int spikes = network.getNrOfSpikes(3, simLength);
			int spikes1 = network.getNrOfSpikes(0, simLength);
			
			// System.out.println("d: "+d+"\tspikes o: "+spikes+"\tspikes1: "+spikes1+"\tspikes2: "+spikes2+"\tspikes3: "+spikes3);
			
			stroke(35, 35, 35);
			point(scaleX*k+xStart, -(scaleY*spikes) + yStart);
			// point(scaleX*d+xStart, -(scaleY*sumSpikes) + yStart);
			stroke(255, 50, 50);
			point(scaleX*k+xStart, -(scaleY*spikes1) + yStart);
			
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


	private void drawAxis(float xStart, float xEnd, float yStart, float yEnd) {
		line(xStart, yStart, xStart, yEnd);
		line(xStart, yStart, xEnd, yStart);
		System.out.println("Axis drawn");
	}

	private void drawScale(float x0, float xEnd, float xBench, float xBenchValue, float y0, float yEnd, float yBench, float yBenchValue) {
		// draw x-scale
		fill(50);
		float x = xBench;
		float i = xBenchValue;

		System.out.println("draw X");
		System.out.println(x+" "+xEnd+" "+xBench);
		while (x < xEnd) {
			line(x, y0-2, x, y0+2);
			text((int) i, (int) x-10, (int) y0+15);
			i += xBenchValue;
			x += (xBench-x0);
		}
		

		// draw y-scale
		float y = yBench;
		i = yBenchValue;
		while (y > yEnd) {
			line(x0-2, y, x0+2, y);
			text((int) i, (int) x0-25, (int) y+5);
			i += yBenchValue;
			y += (yBench-y0);
		}
		System.out.println("drawn Y");
	}

	public void keyPressed() {
		exit();
	}

}
