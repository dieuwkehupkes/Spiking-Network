package izhikevich.spikingnetwork;

import izhikevich.spikingnetwork.neuron.Neuron;
import processing.core.*;

public class TestOscillatoryBehaviour extends PApplet {
	
	// give periodic input to neurons and tests its reaction
	
	private static final long serialVersionUID = 3320297483047002458L;

	Neuron n1, n2, n3, n4, n5, n6, n7, inputNeuron;
	NeuralNetwork network;
	Neuron[] n;
	double a = 0.01; double b = 0.2; int c = -65;
	int simulationSpeed = 500;
	boolean mouseP = false;         // indicates whether mouse is pressed
	
	// info for plotting neurons
	final int Nw_np = 40;
	final int Nw_p = 140;
	
	final int NwFixed = 30;
	int Nw = 40;
	int iN = (int) (-0.5*(NwFixed-Nw));
	
	// info for plotting v of neurons
	int simLength = 2500;
	double scaleX, scaleY;		// scale in x and y direction of plotdata
	float[][][] plt;			// store plot data
	float curTime;				// current Time
	float[] baseShift, shiftX, shiftY;	// shift for plotting data
	float[] curShift;
	float[][] data = new float[simLength][2];	// array to store shifted plot data
	int[] curIndex, prevIndex;
	int from; int to;

	boolean plot = false;
	boolean pause = false;
	boolean noShift = true;	// set to true to make computation lighter

	public void setup() {
		
		initialise();
		// Create neurons (maybe put that in a function
		Neuron n1 = new Neuron(a, b, c, 1.01);		// spike period 20
		Neuron n2 = new Neuron(a, b, c, 1.75);		// spike period 30
		Neuron n3 = new Neuron(a, b, c, 2.63);		// spike period 40
		Neuron n4 = new Neuron(a, b, c, 3.63);		// spike period 50
		Neuron n5 = new Neuron(a, b, c, 4.83);		// spike period 60
		Neuron n6 = new Neuron(a, b, c, 6.11);		// spike period 70
		Neuron n7 = new Neuron(a, b, c, 7.54);		// spike period 80
		
		Neuron inputNeuron = new Neuron(a, b, c, 1.01);	// input neuron

		n = new Neuron[] {n1, n2, n3, n4, n5, n6, n7, inputNeuron};
		
		network = new NeuralNetwork(n, 1, Nw);
		
		// change neuron coordinates
		for (Neuron neuron : new Neuron[] {n1, n2, n3, n4, n5, n6, n7}) {

			// create connection to input neuron

			neuron.setConnections(new int[] {7}, new double[] {30.});
			// set coordinates for visualisation
		}

		setNeuronCoordinates();

	}
	
	public void draw() {
		
		if (pause) {
			return;
		}
		
		// maybe with mouse pressed we actually want to be able to do some things
		if (mouseP) network.mousePressed(mouseX, mouseY);

		network.getNeuron(7).setI(10);

		/*
		for (int i=0; i<7; i++) {
			network.getNeuron(i).setI(10);
		}
		*/
		
		network.update();
		
		if (plot) {
			
			plotNeurons();
			
			}
		
		display(network);
		curTime += 0.1;
		
	}
	
	private void plotNeurons() {
		
		// clear background if data needs to be shifted
		
		if (curShift[0] >= baseShift[0]) {
			if (noShift) resetPlotData();
			background(255);
		}
		
		for (int neuronIndex = 0; neuronIndex<8; neuronIndex++) {
			plotNeuron(neuronIndex);
		}
		
	}
	
	
	private void plotNeuron(int nI) {
		
		// update indices and time
		curShift[nI] += 0.1;
		prevIndex[nI] = curIndex[nI];
		curIndex[nI] = (curIndex[nI]+1) % simLength;

		plt[nI][curIndex[nI]][0] = curTime;
		plt[nI][curIndex[nI]][1] = (float) network.getNeuron(nI).v;

		if (curShift[nI] <= baseShift[nI]) {	// if curShift <= 0, plot next datapoint and return
			data = Collection.shift_and_scale(plt[nI], baseShift[nI], shiftY[nI], scaleX, scaleY);
			// System.out.println(data[0][0]+" "+data[0][1]);
			line(data[prevIndex[nI]][0], data[prevIndex[nI]][1], data[curIndex[nI]][0], data[curIndex[nI]][1]);
			return;
		}

		// plot is in the fase where data needs to be shifted
			
		// update x-shift factor
		shiftX[nI] -= 0.1;
		
		// rescale data and plot
		data = Collection.shift_and_scale(plt[nI],  shiftX[nI], shiftY[nI], scaleX, scaleY);
		
		for (int i = 1; i<simLength; i++) {
			from = (i+curIndex[nI]) % simLength;
			to = (from +1) % simLength;
			line(data[from][0], data[from][1], data[to][0], data[to][1]);
			//System.out.println("draw line from (" + data[from][0]+", "+data[from][1]+") to ("+data[to][0]+", "+data[to][1]+")");
		}
		
	}
	
	private void setNeuronCoordinates() {
		
		
		// set coordinates for output neurons
		for (int i=0; i<7; i++) {
			Neuron neuron = network.getNeuron(i);
			neuron.setX(3*this.Nw+this.iN);
			neuron.setY(i*this.Nw+this.iN+10);
			//System.out.println("Neuron "+i+": ("+neuron.getX()+", "+neuron.getY()+")");
		}
		
		// set coordinates for input neuron
		Neuron neuron = network.getNeuron(7);
		neuron.setX(this.iN);
		neuron.setY(network.getNeuron(3).getY());
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
		if (key=='+'|| key=='-') {    // change simulationspeed
			simulationSpeed = (key=='+') ? simulationSpeed+5 : simulationSpeed -5;    //change speed
			if (simulationSpeed <=0) simulationSpeed = 1;
			System.out.println("new frameRate"+ simulationSpeed);
			frameRate(simulationSpeed);
		}
		// quit program by pressing x or q
		if (key=='q' || key=='Q' || key=='x') exit();

		// plot individual neuron behaviour by pressing 'p'
		if (key =='p' ) {
			plot = plot ? false : true;
			plotInit(plot);
		}

		// Pause simulation with space
		if (key == ' ') pause = pause ? false : true;
		
		if (key == 's') noShift = noShift ? false : true;
	}
	
	public void display(NeuralNetwork network) {

		/**
		 * Display the neural network
		 */
		for (int i=0; i<network.Nn; i++) {
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

	    // draw rectangle
	    rect(x, y, this.NwFixed, this.NwFixed);
	    
	    strokeWeight(10);
	    point(x, y);
	    strokeWeight(1);
	}

	private void initialise() {
		// set the size of the simulation
		frameRate(simulationSpeed);
		size(7*Nw, 7*Nw+20);
		
		// initialising of all the values used for plotting
		scaleX = 1.2; scaleY = 0.8;		// initialise scaling data

		// fill baseShift
		baseShift = new float[8];
		baseShift[7] = Nw_p+20; for (int i=0; i<7; i++) baseShift[i] = 4*Nw_p+20;
		// initialise shiftX array
		shiftX = baseShift.clone();
		// set shiftY value
		shiftY = new float[8];
		shiftY[7] = (float) (3.5*Nw_p+this.iN); for (int i=0; i<7; i++) shiftY[i] = (float) (i*Nw_p+this.iN+0.5*Nw_p);
		
		// create curShift
		curShift = new float[8];

		// create curIndex, prevIndex
		curIndex = new int[8];
		for (int i=0; i<8; i++) curIndex[i] = 0;
		prevIndex = new int[8];
		for (int i=0; i<8; i++) curIndex[i] = -1;

	}

	public void plotInit(boolean plot) {
		// Change layout of the plots to add the
		// individual spike behaviour of the neuron
		background(255);
		if (plot) {
			resetPlotData();

			this.Nw = Nw_p;
		} else {
			this.Nw = Nw_np;
			// network.getNeuron(0).setNw(Nw);
		}
		
		this.iN = -(35-this.Nw);

		setNeuronCoordinates();
		size(7*Nw, 7*Nw);
	}

	private void resetPlotData() {
		// set all values back as if plotting just started
		// fill first element of array with current values
		curTime = 0;
		plt = new float[8][simLength][2];
		for (int i=0; i<8; i++) {
			curIndex[i] = 0;	// reset current index
			plt[i][0][0] = curTime;		// reset plot data
			plt[i][0][1] = (float) network.getNeuron(i).v;
			curShift[i] = (float) (baseShift[i] - (0.1*simLength));
		}
	}
		
}