package izhikevich.spikingnetwork;

import izhikevich.spikingnetwork.neuron.*;
import izhikevich.spikingnetwork.training.*;
import izhikevich.spikingnetwork.architecture.*;
import java.util.*;

public class NeuralNetwork {
	// Fields
	public int Nn;       // number of neurons
	private int Ncol;     // number of columns
	private int Nw;       // pixel width of the neurons (for visualisation)

	private Neuron neurons[];     // the neurons forming the network
	Architecture architecture;    // the architecture of the network
	Training training;            // the way the weights are updated in training mode

	public int[] indices;        // indices to loop over the neurons of the network

	private boolean trainingMode = false;     // training mode of network is false

	// Constructors

	// Empty constructor
	NeuralNetwork() {
		/** 
		 * Construct empty neural network
		 */
	}

	NeuralNetwork(Neuron[] neurons, int Ncol, int Nw) {
		/**
		 * Construct network with neurons
		 */
		addNeurons(neurons, Ncol, Nw);
	}

	// Make two more constructors: one in which also architecture is
	// inputted and one in which only architecture is inputted

	public void addNeurons(Neuron[] neurons, int Ncol, int Nw) {
		/**
		 * Add a pre-generated list of neurons to
		 * the network
		 */
		this.setNeurons(neurons);              // add neurons
		this.Ncol = Ncol;                    // set column width network
		this.Nw = Nw;                        // set neuron width network
		this.Nn = neurons.length;            // set number of neurons of network
		this.indices = new int[Nn];          // create list with indices
		for (int i=0; i<Nn; i++) this.indices[i] = i;   // add indices

		for (Neuron neuron : neurons) neuron.network = this;    // associate neurons with network

		setNeuronCoordinates();             // compute coordinates for neurons
	}

	public void addNeurons(HashMap<String, Integer> generateNeurons, int Ncol, int Nw) {
		/**
		 * Generate neurons as specified in generateNeurons and
		 * add them to the network
		 *
		 * @param generateNeurons   a map from neurontype to number of
		 *                          neurons for this type
		 */

		// find number of neurons
		Neuron[] neurons = generateNeurons(generateNeurons);    // generate the neurons
		addNeurons(neurons, Ncol, Nw);      // add to network
	}

	public void addConnections() {
		/**
		 * Add connections to the network, using the architecture of
		 * the network
		 */
		if ( hasArchitecture()) {
			architecture.generateConnections(this);
		}
		else {
			throw new NullPointerException("No architecture set for the network");
		}
	}

	private Neuron[] generateNeurons(HashMap<String, Integer> generateNeurons) {
		/**
		 * Generate a list with neurons as specified in generateNeurons
		 *
		 * @param generateNeurons       A hashmap from strings giving the name of the
		 *                              neuron type to a number describing how many
		 *                              neurons of this type should be created
		 * @return                      a list with neurons, randomised order
		 */

		// find max nr of neurons
		int total = 0;
		for (int nr : generateNeurons.values()) total+= nr;

		Neuron[] neurons = new Neuron[total];       // create Array to store neurons

		int curIndex = 0;                           // Array index to store new neuron

		for (String neuronType : generateNeurons.keySet()) {
			int n = generateNeurons.get(neuronType);              // get number of neurons
			appendNeurons(neuronType, neurons, n, curIndex);      // add neurons to array
			curIndex += n;                                        // increase array index
		}

		randomiseArray(neurons);            // randomise order of neurons

		return neurons;
	}

	private void appendNeurons(String neuronType, Neuron[] neurons, int n, int curIndex) {
		/**
		 * Add neurons of specified type to the list
		 * NB: needs to be updated if new neuron types are added!!
		 * 
		 * @param neuronType    string with the type of the neuron
		 * @param neurons       array to add neurons to
		 * @param n             number of neurons to be added
		 * @param curIndex      position in array to start appending
		 *
		 * @throws NullPointerException
		 */

		/* Maybe I could do something here as well with an interface to make this better */

		// create exception for invalid types
		NullPointerException nonExistingType = new NullPointerException("Non existing neuron type");

		// add neurons of neuronType to array neurons
		if (neuronType.equals("ChatteringNeuron")) {
			for (int i=0; i<n; i++) neurons[i+curIndex] = new ChatteringNeuron();
		}
		else if (neuronType.equals("ExcitatoryNeuron")) {
			for (int i=0; i<n; i++) neurons[i+curIndex] = new ExcitatoryNeuron();
		}
		else if (neuronType.equals("FastSpikingNeuron")) {
			for (int i=0; i<n; i++) neurons[i+curIndex] = new FastSpikingNeuron();
		}
		else if (neuronType.equals("InhibitoryNeuron")) {
			for (int i=0; i<n; i++) neurons[i+curIndex] = new InhibitoryNeuron();
		}
		else if (neuronType.equals("IntrinsicallyBurstingNeuron")) {
			for (int i=0; i<n; i++) neurons[i+curIndex] = new IntrinsicallyBurstingNeuron();
		}
		else if (neuronType.equals("RegularSpikingNeuron")) {
			for (int i=0; i<n; i++) neurons[i+curIndex] = new RegularSpikingNeuron();
		}
		else {
			System.out.println(neuronType);
			throw nonExistingType;
		}
	}

	public void setArchitecture(Architecture architecture) {
		/**
		 * Add an architecture to the network, or change the current
		 * architecture of the network;
		 * As the weight organisation is directly related to
		 * the architecture of the network, changing the architecture
		 * of the network will remove all weights.
		 */
		this.architecture = architecture;
		this.removeWeights();
	}

	private void setNeuronCoordinates() {
		/**
		 * Compute the locations the neurons should have
		 * on the grid, based on the total number of neurons,
		 * columns and the neuron width
		 */
		
		// set x and y
		for (int i=0; i<Nn; i++) {
			neurons[i].setX((i%Ncol)*Nw + 5);
			neurons[i].setY(((int)(i/Ncol))*Nw + 5);
			neurons[i].setNw(Nw);
		}
		
	}

	public void initNeighbours(int maxNumNeighbours) {
		/**
		 * create arrays for neighbours for each neuron
		 */
		for (Neuron neuron : getNeurons()) {
			neuron.setNeighbours(new int[maxNumNeighbours]);
			neuron.setWeights(new double[maxNumNeighbours]);
		}
	}

	public void mousePressed(int x, int y) {
		/**
		 * When the mouse is pressed, give the neuron
		 * it is pointing at extra activation
		 */

		int neuronIndex = findNeuron(x, y);     // find index neuron mouse points at
		try {
			Neuron neuron = getNeurons()[neuronIndex];
			neuron.setI(80);   // give activation to the neuron
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing if mouse points outside of network
		}
	}

	private int findNeuron(double x, double y) {
		/** 
		 * Find the neuron corresponding with the coordinates
		 * on the grid.
		 */
		int column = (int) x/Nw;     // compute column of coordinates
		int row = (int) y/Nw;        // compute row of coordinates
		int neuronIndex = row*Ncol + column;    // compute index from row and column
		return neuronIndex;
	}

	public void update() {
		update(0.0);
	}

	public void update(double I) {
		/**
		 * Update the values of the neurons of the
		 * network given an input given to all neurons.
		 *
		 * @param I fixed input given to all neurons
		 */
		randomiseArray(indices);    // randomise update order
		for (int i=0; i<Nn; i++) {
			Integer j = indices[i];
			getNeuron(j).update(I);
		}
	}

	public void printConnections() {
		/**
		 * Print the connections between the
		 * neurons in the network
		 */
		int counter = 0;
		for (Neuron neuron : getNeurons()) {
			System.out.println("Neuron "+counter+ " receives input from:");
			for (int i=0; i<neuron.numNeighbours; i++) {
				System.out.println("Neuron "+neuron.getNeighbours()[i]+"\t weight: "+neuron.getWeights()[i]);
			}
			counter++;
		}
	}

	public void randomiseArray(int[] ar) {
		/**
		 * Shuffle input array using Fisher-Yates algorithm
		 */
		Random r = new Random();
		int l = ar.length;      // store length of input array
		for (int i=l-1; i>0; i--) {
			int index = r.nextInt(i+1);
			int next = ar[index];
			ar[index] = ar[i];
			ar[i] = next;
		}
	}

	private void randomiseArray(Object[] ar) {
		/**
		 * Shuffle input array using Fisher-Yates algorithm
		 */
		Random r = new Random();
		int l = ar.length;      // store length of input array
		for (int i=l-1; i>0; i--) {
			int index = r.nextInt(i+1);
			Object next = ar[index];
			ar[index] = ar[i];
			ar[i] = next;
		}
	}

	void removeWeights() {
		/** 
		 * Remove any connection between neurons
		 */
		for (Neuron neuron : this.getNeurons()) {
			neuron.removeConnections();
		}
	}

	private boolean hasArchitecture() {
		/**
		 * Return if architecture is set
		 */
		if (this.architecture == null) {
			return false;
		} else {
			return true;
		}
	}

	// Setters

	public void toggleTrainingMode() {
		/**
		 * Toggle training mode
		 */
		this.trainingMode = this.trainingMode ? false :true;
		for (Neuron neuron : this.getNeurons()) {
			neuron.setTrainingMode(this.trainingMode);  // change training mode neurons
		}
		printConnections();
	}

	public void setTrainingMethod(Training trainingMethod) {
		/**
		 * Set method for training
		 */
		this.training = trainingMethod;
	}

	// Getters

	public int getMaxNumNeighbours() {
		/**
		 * Return maximum number of neighbours
		 * of the neurons
		 */
		int maxNumNeighbours;
		try {
			maxNumNeighbours = this.architecture.maxNumNeighbours();
		} catch (NullPointerException e) {
			throw new NullPointerException("Architecture of Network is not set, maximum number of neighbours unknown");
		}
		return maxNumNeighbours;
	}

	public double getMinWeight() {
		/**
		 * Return maximum number of neighbours
		 * of the neurons
		 */
		double minWeight;
		try {
			minWeight = this.architecture.minWeight();
		} catch (NullPointerException e) {
			throw new NullPointerException("Architecture of Network is not set, maximum number of neighbours unknown");
		}
		return minWeight;
	}

	public double getMaxWeight() {
		/**
		 * Return maximum number of neighbours
		 * of the neurons
		 */
		double maxWeight;
		try {
			maxWeight = this.architecture.maxWeight();
		} catch (NullPointerException e) {
			throw new NullPointerException("Architecture of Network is not set, maximum number of neighbours unknown");
		}
		return maxWeight;
	}

	public boolean getTrainingMode() {
		/**
		 * Return true if the network is in
		 * training mode false otherwise
		 */
		return this.trainingMode;
	}

	public Training getTrainingMethod() {
		/**
		 * Return method of training for the
		 * network
		 */
		return this.training;
	}

	public Neuron[] getNeurons() {

		return neurons;
	}
	
	public Neuron getNeuron(int i) {
		/**
		 * get Neuron with index i
		 */
		return neurons[i];
	}

	public void setNeurons(Neuron neurons[]) {
		this.neurons = neurons;
	}

	public double updateExistingConnection(Neuron neuron, Neuron neuron2) {
		/**
		 * Update an existing connection between two neurons
		 */
		double update = this.training.updateExistingConnection(neuron, neuron2);
		return update;
	}

	public void updateNonExistingConnection(Neuron neuron, Neuron neuron2) {
		/**
		 * Update a non existing connection between
		 * two neurons that have potential of
		 * connecting.
		 */
		// TODO implement
	}
}
