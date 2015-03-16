package izhikevich.spikingnetwork.runnables;

import java.util.HashMap;

import processing.core.*;

import izhikevich.spikingnetwork.NeuralNetwork;
import izhikevich.spikingnetwork.distancemetric.*;
import izhikevich.spikingnetwork.neuron.ExcitatoryNeuron;
import izhikevich.spikingnetwork.neuron.Neuron;
import izhikevich.spikingnetwork.probabilityfunction.*;
import izhikevich.spikingnetwork.training.*;
import izhikevich.spikingnetwork.architecture.*;

public class Visualise extends PApplet {
	/**
	 * 
	 */

	// serial ID for serializable class
	private static final long serialVersionUID = 7416187811109690854L;

	// Parameters for visual network
	int Nexhib = 800;               // number of random exhibitory neurons
	int Ninhib = 400;               // number of random inhibitory neurons
	int Nn = Nexhib + Ninhib;       // total number of neurons
	int Ncol=40;                    // number of columns
	int Nw=30;                      // neuron width
	int maxNumNeighbours=10;        // maximum nr of neigbours for neuron
	double minWeight = -5.0;           // minimal weight between neurons
	double maxWeight = 15.0;           // maximum weight between neurons
	DistanceMetric distMetric = new ManhattanDistance();        // distance metric used
	ProbabilityFunction probFunction = new Exponential(0.95);   // funct to determine prob of connecting
	Training trainingFunction = new HeuristicalHebbian();   // training function for learning

	// variables to use for visualisation
	boolean mouseP = false;         // indicates whether mouse is pressed
	boolean trainingMode = false;   // indicate whether network is in training mode
	int simulationSpeed = 50;

	NeuralNetwork network;

	public void setup() {
		// determine size based on Nn, Ncol and Nw
		int width = Nw*Ncol + 5;
		int height = (int) (Nw*Nn/Ncol) + 5;
		size(width, height);
		frameRate(simulationSpeed);    // Nb: frame rate only corresponds with real time
		// when enough processing power is available

		HashMap<String, Integer> neuronDistr = new HashMap<String, Integer>();   // nrs of diff neuron types

		neuronDistr.put("ExcitatoryNeuron", Nexhib);
		neuronDistr.put("InhibitoryNeuron", Ninhib);

		network = new NeuralNetwork();
		network.addNeurons(neuronDistr, Ncol, Nw);

		Architecture a = new Neighbour(maxNumNeighbours, minWeight, maxWeight);
		a.setDistanceMetric(distMetric, Nw);
		a.setProbabilityFunction(probFunction);
		network.setArchitecture(a);
		network.addConnections();
		network.setTrainingMethod(trainingFunction);

		display(network);

	}

	public void draw() {

		double I = 2 * Math.random();

		if (mouseP) network.mousePressed(mouseX, mouseY);
		network.update(I);
		display(network);

	}

	public void mousePressed() {
		// when the mouse is pressed, give the neuron
		// it is pointing at extra activation
		mouseP = true;
		network.mousePressed(mouseX, mouseY);
	}

	public void mouseReleased() {
		mouseP = false;
	}

	public void keyPressed() {

		
		// specify what happens when a key is pressed
		// not implemented yet
		if (key=='+'|| key=='-') {    // change simulationSpeed
			simulationSpeed = (key=='+') ? simulationSpeed+10 : simulationSpeed -10;    //change speed
			if (simulationSpeed <=0) simulationSpeed = 1;
			System.out.println("new frameRate"+ simulationSpeed);
			frameRate(simulationSpeed);
		}
		if (key=='q' || key=='Q') exit();
		if (key=='t' || key=='T') {   // toggle training mode
			trainingMode = trainingMode ? false : true;
			String print = trainingMode ? "Training mode on" : "Training mode off";
			System.out.println(print);
			network.toggleTrainingMode();
		}
	}
	
	public void display(NeuralNetwork network) {
		/**
		 * Display the neural network
		 */
		for (int i=0; i<Nn; i++) {
			display(network.getNeuron(i));
		}
	}

	public void display(Neuron neuron) {
		/**
		 * Display the neuron according to its
		 * display info
		 */
		
		// set colour
	    fill((int)(256*((neuron.v-40)/-120.0)),0, (int)(256*(1.0-((neuron.v-40)/-120.0)))); // set colour

	    int x = neuron.getX();
	    int y = neuron.getY();
	    int Nw = neuron.getNw();

	    // draw rectangle
	    rect(x, y, Nw-2, Nw-2);

	    // if neuron is excitatory, draw extra rectangle
	    if (neuron instanceof ExcitatoryNeuron) {
		rect(x+1, y+1, Nw-4, Nw-4);
		rect(x+2, y+2, Nw-6, Nw-6);
	    }
	}
}
