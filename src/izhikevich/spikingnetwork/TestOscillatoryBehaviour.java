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
	final int Nw_p = 150;
	
	int Nw = 40;
	int iN = - (35-Nw);
	
	// info for plotting v of neurons
	int simLength = 1500;
	double scaleX, scaleY;		// scale in x and y direction of plotdata
	float[][][] plt;			// store plot data
	float curTime;				// current Time
	float[] baseShift, shiftX, shiftY;	// shift for plotting data
	float[] curShift;
	float[][] pltDataShifted = new float[simLength][2];
	int[] curIndex;
	int from; int to;
	boolean plot = false;

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
		
		Neuron inputNeuron = new Neuron(a, b, c, 7.54);	// input neuron

		n = new Neuron[] {n1, n2, n3, n4, n5, n6, n7, inputNeuron};
		
		network = new NeuralNetwork(n, 1, Nw);
		
		// change neuron coordinates
		for (Neuron neuron : new Neuron[] {n1, n2, n3, n4, n5, n6, n7}) {

			// create connection to input neuron

			neuron.setConnections(new int[] {7}, new double[] {80.});
			// set coordinates for visualisation
		}

		setNeuronCoordinates();

	}
	
	public void draw() {
		
		// maybe with mouse pressed we actually want to be able to do some things
		if (mouseP) network.mousePressed(mouseX, mouseY);

		for (int i=0; i<7; i++) {
			network.getNeuron(i).setI(10);
		}
		
		network.update();
		display(network);
		
		if (plot) {
			
			for (int iNeuron=0; iNeuron<8; iNeuron++) {
				plotNeuron(iNeuron);
			}
			
			/*
			curIndex = (curIndex+1) % simLength;
			for (int iNeuron=0; iNeuron<8; iNeuron++) {
				// plotNeuron(iNeuron);
			}
			*/
		}
		
		curTime += 0.1;
		
	}
	
	
	private void plotNeuron(int neuronIndex) {
		
		fill(255);
		rect(shiftX[neuronIndex]-100, shiftY[neuronIndex]-140, 140, 140);
		/*
		// update indices
		prevIndex = curIndex[neuronIndex];
		curTime += 0.1;
		curShift[neuronIndex] +=0.1;

		plt[neuronIndex][curIndex[neuronIndex]][0] = curTime;
		plt[neuronIndex][curIndex[neuronIndex]][1] = (float) network.getNeuron(neuronIndex).v;
		
		if (curShift[neuronIndex] <= 0) {	// if curShift <= 0, plot next datapoint and return
			data = Collection.shift_and_scale(plt[neuronIndex], baseShift[neuronIndex], shiftY[neuronIndex], scaleX, scaleY);
			line(data[prevIndex][0], data[prevIndex][1], data[curIndex][0], data[curIndex][1]);
			return;
		}

		
		// update x-shift factor
		shiftX[neuronIndex] -= 0.1;
		
		// rescale data and plot
		float[][] data = Collection.shift_and_scale(plt[neuronIndex],  shiftX[neuronIndex], shiftY[neuronIndex], scaleX, scaleY);
		
		for (int i = 1; i<simLength; i++) {
			from = (i+curIndex) % simLength;
			to = (from +1) % simLength;
			line(data[from][0], data[from][1], data[to][0], data[to][1]);
		}
		*/
		
	}
	
	private void setNeuronCoordinates() {
		
		
		// set coordinates for output neurons
		for (int i=0; i<7; i++) {
			Neuron neuron = network.getNeuron(i);
			neuron.setX(4*this.Nw+20);
			neuron.setY(i*Nw+iN);
		}
		
		// set coordinates for input neuron
		Neuron neuron = network.getNeuron(7);
		neuron.setX(20);
		neuron.setY(3*this.Nw + iN);
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
		if (key=='q' || key=='Q' || key=='x') exit();
		if (key =='p' ) {
			plot = plot ? false : true;
			plotInit(plot);
		}
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
	    rect(x, y, this.Nw-this.iN, this.Nw-this.iN);
	}

	private void initialise() {
		// set the size of the simulation
		size(7*Nw, 7*Nw+iN);
		
		// initialising of all the values used for plotting
		scaleX = 1.2; scaleY = 1.0;		// initialise scaling data
		resetPlotData();	// empty the data matrix for the plot and reset curTime

		// fill baseShift
		baseShift = new float[8];
		baseShift[0] = 30+Nw_p; for (int i=1; i<8; i++) baseShift[i] = 5*Nw_p+30;
		// initialise shiftX array
		shiftX = baseShift;
		// set shiftY value
		shiftY = new float[8];
		shiftY[0] = 50+4*Nw_p; for (int i=1; i<8; i++) shiftY[i] = 50+ i *Nw_p;

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
			network.getNeuron(0).setNw(Nw);
		}
		
		this.iN = -(35-this.Nw);
		setNeuronCoordinates();
		size(7*Nw, 7*Nw+iN);
	}

	private void resetPlotData() {
		// set all values back as if plotting just started
		// fill first element of array with current values
		curTime = 0;
		plt = new float[8][simLength][2];
		/*
		plt = new float[8][simLength][2];
		for (int i=0; i<8; i++) {
		curIndex[i] = 0;
			// System.out.println(plt[i]);
			//plt[i][0][0] = curTime;
			plt[i][curIndex][1] = (float) network.getNeuron(i).v;
			// curShift[i] = (float) (baseShift[i] - (0.1*simLength));
		}
	*/
	}
		
}