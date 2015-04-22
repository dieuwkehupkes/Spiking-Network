package izhikevich.spikingnetwork;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import izhikevich.spikingnetwork.neuron.Neuron;
import processing.core.*;

public class TestOscillatoryBehaviour extends PApplet {
	
	// give periodic input to neurons and tests its reaction
	
	private static final long serialVersionUID = 3320297483047002458L;

	Neuron n1, n2, n3, n4, n5, n6, n7, inputNeuron, o1, o2, o3;
	NeuralNetwork network;
	Neuron[] n;

	double a = 0.01; double b = 0.2; int c = -65;
	double I = 10;		// input current for input neuron
	int simulationSpeed = 500;
	boolean mouseP = false;         // indicates whether mouse is pressed
	
	// info for plotting neurons
	final int Nw_np = 40;
	final int Nw_p = 140;
	final int nN = 11;		// number of neurons
	
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
	int neuronAv;

	boolean plot = false;
	boolean pause = false;
	boolean noShift = true;	// set to true to make computation lighter
	int screenshotNr = 0;
	int mouseOver;
	
	public void setup() {
		
		size(10*Nw, 10*Nw);								// start with setting window size
		if (frame != null) frame.setResizable(true);		// allow reseizing of screen

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
		
		Neuron o1 = new Neuron(a, b, c, 1);
		Neuron o2 = new Neuron(a, b, c, 1);
		Neuron o3 = new Neuron(a, b, c, 1);

		n = new Neuron[] {n1, n2, n3, n4, n5, n6, n7, inputNeuron, o1, o2, o3};
		
		network = new NeuralNetwork(n, 1, Nw);
		
		// change neuron coordinates
		for (Neuron neuron : new Neuron[] {n1, n2, n3, n4, n5, n6, n7}) {

			// create connection to input neuron
			neuron.setConnections(new int[] {7}, new double[] {30.});

		}
		
		// create connection to output neurons
		int[] allNeurons = new int[] {0, 1, 2, 3, 4, 5, 5};
		o1.setConnections(allNeurons, new double[] {20.0, 20.0, 5.0, 0.0, -5.0, -5.0, -5.0});
		o2.setConnections(allNeurons, new double[] {-5.0, -5.0, 5.0, 20.0, 20.0, 0.0, 0.0});
		o3.setConnections(allNeurons, new double[] {-25.0, 5.0, 5.0, 5.0, 7.0, 10.0, 10.0});

		// set coordinates for visualisation
		setNeuronCoordinates();

	}
	
	public void draw() {
		
		mouseOver();
		
		if (pause) {
			return;
		}
		
		network.getNeuron(7).setI(this.I);

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
			writeInstructions();
		}
		
		for (int neuronIndex = 0; neuronIndex<nN; neuronIndex++) {
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
	
	private void mouseOver() {
		/**
		 * Check if mouse is currently on any neuron,
		 * set this.mouseOver to neuronIndex
		 */
		
		for (int i=0; i<nN; i++) {
			Neuron n = network.getNeuron(i);
			if (mouseX <= n.getX()+NwFixed && mouseX >= n.getX() && mouseY <= n.getY()+NwFixed  && mouseY >= n.getY()) {
				this.mouseOver = i;
				//if (prevMouse != mouseOver) System.out.println("Mouse over "+i);
				return;
			}
		}
		
		this.mouseOver = -1;
		//if (prevMouse != mouseOver) System.out.println("Mouse not over anything");
		
		
	}
	
	private void setNeuronCoordinates() {
		
		
		// set coordinates for second layer neurons
		for (int i=0; i<7; i++) {
			Neuron neuron = network.getNeuron(i);
			neuron.setX(3*this.Nw+this.iN);
			if (this.Nw <=50) neuron.setY(i*this.Nw+this.iN+10);
			else neuron.setY(i*this.Nw+this.iN);
		}
		
		// set coordinates for input neuron
		Neuron neuron = network.getNeuron(7);
		neuron.setX(this.iN);
		neuron.setY(network.getNeuron(3).getY());
		
		// set coordinates for output neurons
		for (int i=8; i<nN; i++) {
			neuron = network.getNeuron(i);
			neuron.setX(6*this.Nw+this.iN);
			if (this.Nw <=50) neuron.setY((i-6)*this.Nw+this.iN+10);
			else neuron.setY((i-6)*this.Nw+this.iN);
		}
	}
	
	private void changeNeuronParams(int neuronIndex) {
		Neuron neuron = network.getNeuron(neuronIndex);
		double[] newParams = Collection.getUserInputNeuron(neuron.a(), neuron.b(), neuron.c(), neuron.d(), this.I);
		neuron.setParameters(newParams[0], newParams[1], newParams[2], newParams[3]);
		this.I = newParams[4];
	}

	public void mousePressed() {
		
		// use mouse to change values of neuron
		mouseP = true;
		
		if (mouseOver > -1) {
			pause = true;
			changeNeuronParams(mouseOver);		// change parameters neuron
			// reset time and number of spikes
			for (int i=0; i<nN; i++) {
				network.getNeuron(i).t = 0.0;
				network.getNeuron(i).nSpikes = 0;
			}
		}
		
		pause = false;
	}
	
	public void mouseReleased() {
		mouseP = false;
	}
	
	public void keyPressed() {
		// specify what happens when a key is pressed
		if (key=='+'|| key=='-') {    // change simulationspeed
			simulationSpeed = (key=='+') ? simulationSpeed+5 : simulationSpeed -5;    //change speed
			if (simulationSpeed <=0) simulationSpeed = 1;
			System.out.println("new frameRate = "+ simulationSpeed);
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
		
		if (key == 'f') makeScreenShot();
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
	    fill(0);
	    textSize(15);
	    neuronAv = (int) neuron.averageSpikePeriod();
	    textAlign(CENTER);
	    text(neuronAv, x+14, y+20);
	}
	
	private void initialise() {

		frameRate(simulationSpeed);		// set the framerate

		writeInstructions();			// write instructions
		
		// initialising of all the values used for plotting
		scaleX = 1.2; scaleY = 0.8;		// initialise scaling data

		// fill baseShift
		baseShift = new float[nN];
		baseShift[7] = Nw_p+20;
		for (int i=0; i<7; i++) baseShift[i] = 4*Nw_p+20;
		for (int i=8; i<11; i++) baseShift[i] = 7*Nw_p+20;
		// initialise shiftX array
		shiftX = baseShift.clone();
		// set shiftY value
		shiftY = new float[nN];
		shiftY[7] = (float) (3.5*Nw_p+this.iN);
		for (int i=0; i<7; i++) shiftY[i] = (float) (i*Nw_p+this.iN+0.5*Nw_p);
		for (int i=8; i<11; i++) shiftY[i] = (float) ((i-6)*Nw_p+this.iN+0.5*Nw_p);
		
		// create curShift
		curShift = new float[nN];

		// create curIndex, prevIndex
		curIndex = new int[nN];
		for (int i=0; i<nN; i++) curIndex[i] = 0;
		prevIndex = new int[nN];
		for (int i=0; i<nN; i++) curIndex[i] = -1;

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
		}
		
		this.iN = -(35-this.Nw);

		setNeuronCoordinates();
		setSize();
		writeInstructions();
	}
	
	private void setSize() {

		if (Nw < 50) {
			size(10*Nw, 10*Nw);
		} else {
			size(11*Nw, 7*Nw+30);
		}
	}
	
	private void writeInstructions() {
		
		String instr = 	"Press SPACE to pause simulation\n" +
						"Press x or q to ext simulation\n" +
						"Press +\\- to change the speed of the simulation\n" +
						"Press p to toggle v/t-plot\n" +
						"Press f to make screenshot\n" +
						"Press s to toggle shift/redraw mode of v/t-plot\n" +
						"(using shift-mode may slow down the simlation significantly";
		fill(0);
		textAlign(LEFT);
		text(instr, 10, this.height-100);
	}
	
	private void makeScreenShot() {
		pause = true;
		String defaultName = "../graphs/screenshot"+String.format("%02d", screenshotNr)+".jpg";
		JTextField storeTo = new JTextField(defaultName);
		Object[] message = {"Save screenshot to", storeTo,""};
		int out = JOptionPane.showConfirmDialog(null, message, "", JOptionPane.OK_OPTION);
		
		if (out == JOptionPane.OK_OPTION) {
			// maybe some check here if the extention makes sense
			save(storeTo.getText());
		}
		
		screenshotNr ++;
		pause=false;
	}

	private void resetPlotData() {
		// set all values back as if plotting just started
		// fill first element of array with current values
		curTime = 0;
		plt = new float[nN][simLength][2];
		for (int i=0; i<nN; i++) {
			curIndex[i] = 0;	// reset current index
			plt[i][0][0] = curTime;		// reset plot data
			plt[i][0][1] = (float) network.getNeuron(i).v;
			curShift[i] = (float) (baseShift[i] - (0.1*simLength));
		}
	}
		
}