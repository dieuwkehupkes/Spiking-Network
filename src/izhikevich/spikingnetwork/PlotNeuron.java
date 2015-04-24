package izhikevich.spikingnetwork;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import izhikevich.spikingnetwork.neuron.Neuron;
import processing.core.*;
import izhikevich.spikingnetwork.Collection;

public class PlotNeuron extends PApplet {
	/**
	 * Dynamic plot of a neuron and its path through statespace
	 */

	private static final long serialVersionUID = -8259725764770574359L;
	
	// button for creating new neuron
	float buttonPosX = 10;
	float buttonPosY = 8;
	float buttonWidth = 200;
	float buttonHeight = 100;
	int textSize = 30;
	String textString = "Press \"n\" to restart\nPress SPACE to pause";
	boolean buttonOver = false;
	
	// parameters for neuron to be plotted
	float I;
	double a, b, c, d;
	Neuron n;
	
	// shift for neuron behaviour
	float shiftXv = 20;
	float shiftYv = 100;
	double scaleXv = 1.3;
	double scaleYv = 1.0;
	float shiftXsS = 20;
	float shiftYsS = 100;
	double scaleXsS = 1.3;
	double scaleYsS = 1.0;
	int simLength = 1500;
	int stateSpaceSimLength = 500;
	
	// create arrays to store data, assumes timestep taken is 0.1
	float[][] pltV = new float[simLength][2];
	float[][] dataV = new float[simLength][2];
	float[][] pltSs = new float[stateSpaceSimLength][2];
	float[][] dataSs = new float[stateSpaceSimLength][2];
	int curIndex = 0;	// index to fill plotdata
	int curIndexSs = 0;	// index to plot statespace
	int prevIndex = -1; //
	float curTime = (float) 0.0;
	float curShift = (float) (-0.1*simLength);
	int from, to;
	
	boolean pause = false;
	boolean plotStateSpace = false;
	int simulationSpeed = 150;
	boolean reDraw = false;
	boolean pltSsFilled = false;
	
	
	public void setup() {

		size(250, 200);
		if (frame != null) frame.setResizable(true);		// allow reseizing of screen
		background(255);
		fill(50);
		frameRate(simulationSpeed);
		//textSize(textSize);
		text(textString, buttonPosX, buttonPosY, buttonWidth, buttonHeight);

		getUserInput();

		n.t = 0;
		n.v = 35;
		
		pltV[0][0] = 0;
		pltV[0][1] = (float) n.v;
		
		strokeWeight(1);
		stroke(0);
		
	}

	public void draw() {
		
		if (pause) {
			return;
		}
		
		prevIndex = curIndex;				// store previous index
		curTime += n.timeStep;				// increment time
		curShift += n.timeStep;				// increment shift
		curIndex = (curIndex +1) % simLength;	// increment index
	
		// generate new datapoint
		n.update(I);
		pltV[curIndex][0] = curTime;
		pltV[curIndex][1] = (float) n.v;

		if (curShift > 0 || pltSsFilled) reDraw = true;

		if (curShift <= 0) {	// if curShift <= 0, plot next datapoint and return
			dataV = Collection.shift_and_scale(pltV, shiftXv, shiftYv, scaleXv, scaleYv);
			line(dataV[prevIndex][0], dataV[prevIndex][1], dataV[curIndex][0], dataV[curIndex][1]);
			return;
		}

		// clear background
		background(255);
		text(textString, buttonPosX, buttonPosY, buttonWidth, buttonHeight);
		
		// update x-shift factor
		shiftXv -= 0.1;
		
		// rescale data and plot
		float[][] data = Collection.shift_and_scale(pltV,  shiftXv, shiftYv, scaleXv, scaleYv);
		
		for (int i = 1; i<simLength; i++) {
			from = (i+curIndex) % simLength;
			to = (from +1) % simLength;
			line(data[from][0], data[from][1], data[to][0], data[to][1]);
		}
		
	}
	
	private void getUserInput() {

		JTextField aVal = new JTextField("0.01");
		JTextField bVal = new JTextField("0.2");
		JTextField cVal = new JTextField("-65");
		JTextField dVal = new JTextField("2");
		JTextField IVal = new JTextField("10");
		
		Object[] message = {
				"a", aVal,
				"b", bVal,
				"c", cVal,
				"d", dVal,
				"I", IVal,
				""
		};
		
		int out = JOptionPane.showConfirmDialog(null, message, "Specify Parameters", JOptionPane.OK_CANCEL_OPTION);
		
		if (out == JOptionPane.OK_OPTION) {
			a = Double.parseDouble(aVal.getText());
			b = Double.parseDouble(bVal.getText());
			c = Double.parseDouble(cVal.getText());
			d = Double.parseDouble(dVal.getText());
			I = Float.parseFloat(IVal.getText());

			// create neuron, set time = 0
			n = new Neuron(a, b, c, d);
			
			reset();
		
		}
		
		else if (out == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}
	}
	
	public void plotInit(boolean plot) {
		background(255);
		drawGrid();
		if (plot) {
			resetStateSpaceData();
			size(500, 250);
		} else {
			size(250, 250);
		}
	}
	
	public void drawGrid() {
		/**
		 * Draw the grid for plotting the state space
		 */
		stroke(0);
		fill(0);
		point(10, 10);
		rect(200, 10, 100, 100);
		
	}
	
	public void resetStateSpaceData() {
		// reset the data for plotting the statespace
		curIndexSs = 0;
		pltSs = new float[stateSpaceSimLength][2];
		pltSs[0][0] = (float) n.v;
		pltSs[0][1] = (float) n.u;
	}
	
	public void reset() {
		// reset values to start plotting for new neuron
		shiftXv = 20;

		pltV = new float[simLength][2];
		dataV = new float[simLength][2];
		curIndex = 0;	// index to fill plotdata
		curTime = (float) 0.0;
		curShift = (float) (-0.1*simLength);
		background(255);
		text(textString, buttonPosX, buttonPosY, buttonWidth, buttonHeight);
	}
		
	public void keyPressed() {
		if (key == ' ') pause = pause ? false : true;
		if (key == 'q' || key == 'x') exit();
		if (key == 'n' ) getUserInput();
		if (key == 'p') {
			plotStateSpace = plotStateSpace ? false : true;
			plotInit(plotStateSpace);
		}
		if (key=='+'|| key=='-') {    // change simulationspeed
			simulationSpeed = (key=='+') ? simulationSpeed+5 : Math.abs(simulationSpeed -5);    //change speed
			System.out.println("New framerate: "+simulationSpeed);
			frameRate(simulationSpeed);
		}
	}
	
	public void keyReleased() {
		//keyP = false;
	}
	
}
