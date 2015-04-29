package izhikevich.spikingnetwork;

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
	float shiftXsS; float shiftYsS;
	double scaleXsS;
	double scaleYsS;
	int simLength = 1500;
	int stateSpaceSimLength = 50;
	
	// create arrays to store data, assumes timestep taken is 0.1
	float[][] pltV = new float[simLength][2];
	float[][] dataV = new float[simLength][2];
	float[][] pltSs = new float[stateSpaceSimLength][2];
	float[][] dataSs;
	int curIndex = 0;	// index to fill plotdata
	int curIndexSs = 0;	// index to plot statespace
	int prevIndex = -1; //
	int prevIndexSs = -1; //
	float curTime = (float) 0.0;
	float curShift = (float) (-0.1*simLength);
	int from, to;
	
	boolean pause = false;
	boolean plotStateSpace = false;
	int simulationSpeed = 150;
	boolean reDraw = false;
	int counterSs = 0;
	int counterVt = 0;
	
	// details for plotting the statespace grid
	int rectSize = 170;
	int rectStartX = 270; int rectStartY = 30;
	float[] gridDetails;
	
	public void setup() {

		size(250, 200);
		if (frame != null) frame.setResizable(true);		// allow reseizing of screen
		background(255);
		fill(50);
		frameRate(simulationSpeed);
		setGridDetails();
		//textSize(textSize);
		// text(textString, buttonPosX, buttonPosY, buttonWidth, buttonHeight);

		getUserInput(0.01, 0.2, -65, 2, 10);

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

		increaseIndices();
		
		// Update network, store u, v and t in plot arrays
		n.update(I);
		// updateDataArrays();
		pltV[curIndex][0] = curTime;			// store time in pltV
		pltV[curIndex][1] = (float) n.v;		// store v in pltV
		pltSs[curIndexSs][0] = (float) n.v;	// store u in pltSs
		pltSs[curIndexSs][1] = (float) n.u;	// store v in pltSs
		
		// compute shifted data for xt plot
		if (curShift >= 0) {
			reDraw = true;			// animation needs to be redrawn
			shiftXv -= 0.1*this.scaleXv;		// update shiftXv
		}
		
		float[][] dataVt = Collection.shift_and_scale(pltV,  shiftXv, shiftYv, scaleXv, scaleYv);

		// compute shifted data for uv plot
		if (plotStateSpace) {
			this.dataSs = Collection.shift_and_scale(pltSs, shiftXsS, shiftYsS, scaleXsS, scaleYsS);
			counterSs ++;
			if (counterSs == stateSpaceSimLength) reDraw = true;
		}

		// redraw or continue, depending on whether redraw is true
		if (reDraw) {
			// redraw everything
			background(255);
			// text(textString, buttonPosX, buttonPosY, buttonWidth, buttonHeight);

			// plot vt
			int end = this.counterVt > simLength ? simLength: this.counterVt;
			for (int i=end-1; i>0; i--) {
				from = (i+curIndex) % simLength;
				to = (from+1) % simLength;
				line(dataVt[from][0], dataVt[from][1], dataVt[to][0], dataVt[to][1]);
			}
				
			// plot statespace
			if (plotStateSpace) {
				drawGrid();

				int until = counterSs;
				
				if (counterSs > stateSpaceSimLength) until = stateSpaceSimLength;
				for (int i=1; i<until; i++) {
					from = (i+curIndexSs) % stateSpaceSimLength;
					to = (from +1) % stateSpaceSimLength;
					line(dataSs[from][0], dataSs[from][1], dataSs[to][0], dataSs[to][1]);
				}
			}
		} else {	// redrawing is not necessary

			// continue plotting vt
			line(dataVt[prevIndex][0], dataVt[prevIndex][1], dataVt[curIndex][0], dataVt[curIndex][1]);
			
			if (plotStateSpace) {
				drawGrid();

				// continue plotting uv
				line(dataSs[prevIndexSs][0], dataSs[prevIndexSs][1], dataSs[curIndexSs][0], dataSs[curIndexSs][1]);
				
			}
		}

	}
	
	private void increaseIndices() {
		
		prevIndex = curIndex;					// store previous index
		curTime += n.timeStep;					// increment time
		curShift += n.timeStep;					// increment shift
		curIndex = (curIndex +1) % simLength;		// increment index
		prevIndexSs = curIndexSs;				// increment prev statespace indices
		curIndexSs = (curIndexSs+ 1) % stateSpaceSimLength;					// increment statespace index
		counterVt ++;
	}
	
	private void getUserInput(double a, double b, double c, double d, double I) {
		double[] out = Collection.getUserInputNeuron(a, b, c, d, I);
		this.a = out[0]; this.b = out[1]; this.c = out[2]; this.d = out[3]; this.I = (float) out[4];
		n = new Neuron(a, b, c, d);
		reset();
	}
	
	public void plotInit(boolean plot) {
		// background(255);
		if (plot) {
			// resetStateSpaceData();
			drawGrid();
			size(500, 250);
		} else {
			size(250, 250);
		}
	}
	
	private void drawGrid() {
		/**
		 * Draw the grid for plotting the state space
		 */
		stroke(0);
		noFill();
		rect(rectStartX, rectStartY, rectSize, rectSize);
		textSize(10);
		textAlign(CENTER, CENTER);
		text("u", rectStartX-30, gridDetails[10]+25);
		text("v", gridDetails[5], gridDetails[1]+25);
		// set x-coordinates
		for (int i=0; i<=6; i++) {
			line(gridDetails[i+2], gridDetails[1]-2, gridDetails[i+2], gridDetails[1]+2);
			text(-80+i*20, gridDetails[i+2], gridDetails[1]+13);
		}
		// set y-coordinates
		for (int i=0; i<=3; i++) {
			line(gridDetails[0]-2, gridDetails[i+9], gridDetails[0]+2, gridDetails[i+9]);
			text(10-i*10, gridDetails[0]-20, gridDetails[i+9]);
		}
	}
	
	private void setGridDetails() {
		/**
		 * Compute the coordinates for the statspace grid
		 */
		float fromSide = 16;
		float xScale = (this.rectSize-fromSide)/6;
		float yScale = (this.rectSize-fromSide)/3;
		float xGrid = this.rectStartX; float yGrid = this.rectStartY+this.rectSize;
		this.gridDetails = new float[13];
		this.gridDetails[0] = xGrid; this.gridDetails[1] = yGrid;
		// set x-details
		for (int i=2; i<=8; i++) {
			this.gridDetails[i] = fromSide/2 + xGrid + (i-2)*xScale;
		}
		for (int i=9; i<=12; i++) {
			this.gridDetails[i] = fromSide/2 + rectStartY +(i-9)*yScale;
		}
		
		// set scale and shift details
		this.shiftXsS = this.gridDetails[6];
		this.shiftYsS = this.gridDetails[10];
		this.scaleXsS = xScale/20;
		this.scaleYsS = yScale/10;
	}
		
	
	private void resetStateSpaceData() {
		// reset the data for plotting the statespace
		curIndexSs = 0;
		pltSs = new float[stateSpaceSimLength][2];
		pltSs[0][0] = (float) n.v;
		pltSs[0][1] = (float) n.u;
	}
	
	private void reset() {
		// reset values to start plotting for new neuron
		shiftXv = 20;

		pltV = new float[simLength][2];
		dataV = new float[simLength][2];
		curIndex = 0;	// index to fill plotdata
		curTime = (float) 0.0;
		curShift = (float) (-0.1*simLength);
		resetStateSpaceData();
		background(255);
		// text(textString, buttonPosX, buttonPosY, buttonWidth, buttonHeight);
	}
		
	public void keyPressed() {
		if (key == ' ') pause = pause ? false : true;
		if (key == 'q' || key == 'x') exit();
		if (key == 'n' ) getUserInput(a, b, c, d, I);
		if (key == 'p') {
			plotStateSpace = plotStateSpace ? false : true;
			plotInit(plotStateSpace);
		}
		if (key=='+'|| key=='-') {    // change simulationspeed
			simulationSpeed = (key=='+') ? simulationSpeed+5 : Math.abs(simulationSpeed -5);    //change speed
			frameRate(simulationSpeed);
		}
	}
	
	public void keyReleased() {
		//keyP = false;
	}
	
}
