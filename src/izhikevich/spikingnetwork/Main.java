package izhikevich.spikingnetwork;

import izhikevich.spikingnetwork.neuron.Neuron;

public class Main {
	/**
	 * 
	 */
	public static void main(String[] args) {
		// Run one of the processing applications
		// MakeGraph.main("izhikevich.spikingnetwork.MakeGraph");
		// Visualise.main("izhikevich.spikingnetwork.Visualise");
		// PlotNeuron.main("izhikevich.spikingnetwork.PlotNeuron");
		// FindReactions.main("izhikevich.spikingnetwork.FindReactions");
		
		getSpikes();

	}
	
	public static void plotSpikePeriodA() {
		float I = 10;
		double a;
		double b = 0.2;
		double c = -65;
		double d;

		int simLength = 20000;
		
		for (d = 1; d<=8; d++) {
			String dValue = String.format("%.0f",  d);
			String varName = "y"+dValue;
			System.out.print(varName + "= [");
			for (a=0.005; a<0.1; a+= 0.005){
				Neuron n = new Neuron(a, b, c, d);
				int spikes = n.getNrOfSpikes(I, simLength);
				double simDuration = simLength*n.timeStep;
				double spikePeriod = simDuration/(double)spikes;
				System.out.print(String.format("%.2f", spikePeriod).replace(",",".")+", ");
			}
			System.out.println("]");
			System.out.println("plt.plot(x, "+varName+", label=\"d = "+d+"\")");
		}
		System.out.println("plt.legend(loc=1)");
		System.out.println("plt.ylabel(\"Spike Period\")");
		System.out.println("plt.xlabel(\"a\")");
		System.out.println("plt.title(\"Spike period as function of a\")");
		System.out.println("plt.text(0.015, 130, \"b = 0.2\\nc = -65\")");
		System.out.println("plt.show()");
	}
	
	public static void getSpikes() {
		float I=10;
		double a = 0.01;
		double b = 0.2;
		double c = -65;
		
		int simLength = 50000;
		
		double[] dValues = {1.01, 1.75, 2.63, 3.63, 4.83, 6.11, 7.54};
		for (double d : dValues) {
			Neuron n = new Neuron(a, b, c, d);
			int spikes = n.getNrOfSpikes(I,  simLength);
			double simDuration = simLength*n.timeStep;
			double spikePeriod = simDuration/(double)spikes;
			System.out.println("d = "+d+"\t spike period = "+spikePeriod);
		}
	}

}
