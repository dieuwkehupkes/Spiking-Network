package izhikevich.spikingnetwork;

import java.util.HashMap;

import processing.core.*;

import izhikevich.spikingnetwork.distancemetric.*;
import izhikevich.spikingnetwork.neuron.ExcitatoryNeuron;
import izhikevich.spikingnetwork.neuron.Neuron;
import izhikevich.spikingnetwork.probabilityfunction.*;
import izhikevich.spikingnetwork.training.*;
import izhikevich.spikingnetwork.architecture.*;
import javax.swing.*;

public class Visualise extends PApplet {
	/**
	 * 
	 */

	// serial ID for serializable class
	private static final long serialVersionUID = 7416187811109690854L;

	// Parameters for visual network
	int Nexhib, Ninhib, Nn;         // variables to store number of neurons
	int Ncol;                    // number of columns
	int Nw;                      // neuron width
	int maxNumNeighbours;        // maximum nr of neigbours for neuron
	double minWeight;           // minimal weight between neurons
	double maxWeight;           // maximum weight between neurons
	DistanceMetric distMetric = new ManhattanDistance();        // distance metric used
	ProbabilityFunction probFunction = new Exponential(0.95);   // funct to determine prob of connecting
	Training trainingFunction = new HeuristicalHebbian();   // training function for learning

	// variables to use for visualisation
	boolean mouseP = false;         // indicates whether mouse is pressed
	boolean trainingMode = false;   // indicate whether network is in training mode
	int simulationSpeed = 50;

	NeuralNetwork network;
	
	public void setup() {
		
		getUserInput();
		
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
	
	private void getUserInput() {
		JTextField neuronsEx = new JTextField("800");
		JTextField neuronsIn = new JTextField("400");
		JTextField columnNumber = new JTextField("40");
		JTextField neuronWidth = new JTextField("30");
		JTextField maxNn = new JTextField("10");
		JTextField minConnectionWeight = new JTextField("-5.0");
		JTextField maxConnectionWeight = new JTextField("15.0");
		
		Object[] message = {
			"Number of Excitatory Neurons", neuronsEx,
			"Number of Inhibitory Neurons", neuronsIn,
			"Number of colums", columnNumber,
			"Neuron Width", neuronWidth,
			"Maximum Number of Neighbours", maxNn,
			"Minimum connection weight", minConnectionWeight,
			"Maximum connection weight", maxConnectionWeight,
			""
		};
		
		int out = JOptionPane.showConfirmDialog(null, message, "Enter", JOptionPane.OK_CANCEL_OPTION);
		
		if (out == JOptionPane.OK_OPTION) {
			try {
				Nexhib = Integer.parseInt(neuronsEx.getText());
				Ninhib = Integer.parseInt(neuronsIn.getText());
				Nw = Integer.parseInt(neuronWidth.getText());
				Nn = Nexhib + Ninhib;
				Ncol = Integer.parseInt(columnNumber.getText());
				maxNumNeighbours = Integer.parseInt(maxNn.getText());
				maxWeight = Double.parseDouble(minConnectionWeight.getText());
				minWeight = Double.parseDouble(maxConnectionWeight.getText());
			} catch (java.lang.NumberFormatException e1) {
				JOptionPane.showMessageDialog(null, "Please provide a value for all variables");
				getUserInput();
			} catch (java.lang.NullPointerException e2) {
				JOptionPane.showMessageDialog(null, "Please provide a value for all variables");
				getUserInput();
			}
		}
		
		else if (out == JOptionPane.CANCEL_OPTION) {
			System.out.println();
			System.exit(out);
		}
		
		checkUserInput();
		
	}
	
	private void checkUserInput() {
		// check if number of columns is sensible?
		// check if maxweight is larger than minweight
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
