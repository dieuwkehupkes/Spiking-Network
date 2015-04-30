package izhikevich.spikingnetwork;

import izhikevich.spikingnetwork.neuron.Neuron;

public class Main {
	/**
	 * 
	 */
	public static void main(String[] args) {

		// Run one of the processing applications
		// MakeGraph.main("izhikevich.spikingnetwork.MakeGraph");
		//Visualise.main("izhikevich.spikingnetwork.Visualise");
		// FindReactions.main("izhikevich.spikingnetwork.FindReactions");
		//PlotNeuron.main("izhikevich.spikingnetwork.PlotNeuron");
		// TestOscillatoryBehaviour.main("izhikevich.spikingnetwork.TestOscillatoryBehaviour");
		
		System.out.println("plt.figure(1)");
		System.out.println("plt.subplot(221)");
		plotSpikePeriod("a", 0.01, 0.1, 0.01, "d", 0., 8.01, 0.1, 10, 0.0, 0.18, -55, 0, 1);
		System.out.println("plt.axis([0, 8, 0, 100])");
		System.out.println("plt.legend(loc="+2+")");
		System.out.println("plt.subplot(222)");
		plotSpikePeriod("a", 0.01, 0.1, 0.01, "d", 0., 8.01, 0.1, 10, 0.0, 0.18, -65, 0, 1);
		System.out.println("plt.axis([0, 8, 0, 100])");
		System.out.println("plt.subplot(223)");
		plotSpikePeriod("a", 0.01, 0.1, 0.01, "d", 0., 8.01, 0.1, 10, 0.0, 0.25, -55, 0, 1);
		System.out.println("plt.axis([0, 8, 0, 100])");
		System.out.println("plt.subplot(224)");
		plotSpikePeriod("a", 0.01, 0.1, 0.01, "d", 0., 8.01, 0.1, 10, 0.0, 0.25, -65, 0, 1);
		System.out.println("plt.axis([0, 8, 0, 100])");
		System.out.println("plt.show()");
		// plotSpikePeriod("d", 1, 8, 1, "b", 0.11, 0.3, 0.01, 10, 0.1,0, -65, 0, 1);
		//plotSpikePeriod("a", 0.01, 0.1, 0.01, "d", 0.0, 8.01, 0.1, 10, 0.1, 0.2, -65, 0, 2);
		
	}
	
	public static void plotSpikeFrequency(String var1, double start1, double end1, double step1, String var2, double start2, double end2, double step2, double I, double a, double b, double c, double d, int legend_loc) {
		plot("frequency", var1, start1, end1, step1, var2, start2, end2, step2, I, a, b, c, d, legend_loc);
		
	}

	public static void plotSpikePeriod(String var1, double start1, double end1, double step1, String var2, double start2, double end2, double step2, double I, double a, double b, double c, double d, int legend_loc) {
		plot("period", var1, start1, end1, step1, var2, start2, end2, step2, I, a, b, c, d, legend_loc);
	}
	
	public static void plot(String plotVar, String var1, double start1, double end1, double step1, String var2, double start2, double end2, double step2, double I, double a, double b, double c, double d, int legend_loc) {
		
		int simLength = 20000;
		
		// print x variable
		System.out.println("x=np.arange("+start2+", "+end2+", "+step2+")");
		
		// loop over var1
		for (double i = start1; i<end1; i+=step1) {
			String iValue = String.format("%.0f", i);
			String varName = "y"+iValue;
			System.out.print(varName+"= [");
			for (double j = start2; j<end2; j+=step2) {
				Neuron n = getNeuron(var1, i, var2, j, a, b, c, d);
				int spikes = n.getNrOfSpikes(I, simLength);
				//System.out.println("a= "+n.a()+" b= "+n.b()+" c= "+n.c()+" d= "+n.d()+ " nr of spikes: "+ spikes);
				double simDuration = simLength*n.timeStep;
				double toPlot;
				if (plotVar == "period") toPlot = simDuration/(double) spikes;
				else if (plotVar == "frequency") toPlot = spikes/simDuration;
				else toPlot = 0.0;
				System.out.print(String.format("%.2f",  toPlot).replace(",",".")+", ");
			}
			System.out.println("]");
			System.out.println("plt.plot(x, "+varName+", label=\""+var1+" = "+String.format("%.2f", i).replace(",",".")+"\", linewidth=2)");

		}

		// System.out.println("plt.legend(loc="+legend_loc+")");
		if (plotVar =="period") System.out.println("plt.ylabel(\"Spike Period\")\nplt.suptitle(\"Spike period as function of "+var2+"\", fontsize=20)");
		else if (plotVar =="frequency") System.out.println("plt.ylabel(\"Spike Frequency\")\nplt.suptitle(\"Spike frequency as function of "+var2+"\", fontsize=20)");
		System.out.println("plt.xlabel(\""+var2+"\")");
		printSubTitle(var1, var2, a, b, c, d, I);
		// System.out.println("plt.axis([0.14, 0.30, 0, 140])");
		//System.out.println("plt.show()");
		
		
	}
	
	private static void printSubTitle(String var1, String var2, double a, double b, double c, double d, double I) {
		System.out.print("plt.title(\"");
		if ((var1 == "a" && var2 == "b") || (var1=="b" && var2 =="a")) System.out.println("c = "+c+",  d = "+d+",  I = "+I+"\", fontsize=12)");
		if ((var1 == "a" && var2 == "c") || (var1=="c" && var2 =="a")) System.out.println("b = "+b+",  d = "+d+",  I = "+I+"\", fontsize=12)");
		if ((var1 == "a" && var2 == "d") || (var1=="d" && var2 =="a")) System.out.println("b = "+b+",  c = "+c+",  I = "+I+"\", fontsize=12)");
		if ((var1 == "b" && var2 == "c") || (var1=="c" && var2 =="b")) System.out.println("a = "+a+",  d = "+d+",  I = "+I+"\", fontsize=12)");
		if ((var1 == "b" && var2 == "d") || (var1=="d" && var2 =="b")) System.out.println("a = "+a+",  c = "+c+",  I = "+I+"\", fontsize=12)");
		if ((var1 == "c" && var2 == "d") || (var1=="d" && var2 =="c")) System.out.println("a = "+a+",  b = "+b+",  I = "+I+"\", fontsize=10)");
	}
	
	private static Neuron getNeuron(String var1, double val1, String var2, double val2, double a, double b, double c, double d) {
		// return a neuron with the appropriate values
		if (var1 == "a" && var2 == "b") return new Neuron(val1, val2, c, d);
		else if (var1 == "a" && var2 == "c") return new Neuron(val1, b, val2, d);
		else if (var1 == "a" && var2 == "d") return new Neuron(val1, b, c, val2);
		else if (var1 == "b" && var2 == "a") return new Neuron(val2, val1, c, d);
		else if (var1 == "b" && var2 == "c") return new Neuron(a, val1, val2, d);
		else if (var1 == "b" && var2 == "d") return new Neuron(a, val1, c, val2);
		else if (var1 == "c" && var2 == "a") return new Neuron(val2, b, val1, d);
		else if (var1 == "c" && var2 == "b") return new Neuron(a, val2, val1, d);
		else if (var1 == "c" && var2 == "d") return new Neuron(a, b, val1, val2);
		else if (var1 == "d" && var2 == "a") return new Neuron(val2, b, c, val1);
		else if (var1 == "d" && var2 == "b") return new Neuron(a, val2, c, val1);
		else /*(var1 == "d" && var2 == "c")*/ return new Neuron(val2, b, val2, d);
	}

	
	public static void plotSpikePeriodSpikes() {
		float I = 10;
		double a = 0.01; double b =0.2; int c = -65;
		double[] dList = new double[] {1.01, 1.75, 2.63, 3.63, 4.83, 6.11, 7.54};
		int simLength = 20000;
		for (double d: dList) {
			double fireTime = 0;
			String dValue = String.format("%.2f",  d);
			String varName = "y"+dValue.replace(",", "");
			Neuron n = new Neuron(a, b, c, d);
			System.out.print(varName + "= [0, ");
			for (int i=0; i<500; i++) n.update();
			for (int i=0; i<simLength; i++) {
				n.update(I);
				if (n.fired) {
					if (fireTime != 0) {
						System.out.print(n.lastTimeFired-fireTime+", ");
					}
					fireTime = n.lastTimeFired;
				}
			}
			System.out.println("]");
			System.out.println("plt.plot("+varName+", label=\"d = "+d+"\", linewidth=1.5)");
		}
		System.out.println("plt.legend(loc=1)");
		System.out.println("plt.ylabel(\"Inter spike time\")");
		System.out.println("plt.xlabel(\"# spikes\")");
		System.out.println("plt.title(\"Development of spikeperiod with number of spikes\")");
		System.out.println("plt.text(30, 7, \"a = 0.01\\nb = 0.2\\nc = -65\")");
		System.out.println("plt.axis([0, 40, 0, 90])");
		System.out.println("plt.show()");
	}


	public static void plotSpikePeriodT() {
		float I = 10;
		double a = 0.01; double b =0.2; int c = -65;
		double[] dList = new double[] {1.01, 1.75, 2.63, 3.63, 4.83, 6.11, 7.54};
		int simLength = 5000;
		for (double d: dList) {
			double fireTime = 0;
			String dValue = String.format("%.2f",  d);
			String varName = "y"+dValue.replace(",", "");
			Neuron n = new Neuron(a, b, c, d);
			System.out.print(varName + "= np.array([");
			System.out.println("\n\n"+n.v);
			for (int i=0; i<500; i++) n.update();
			System.out.println(n.v+"\n\n");
			n.t = 0.0;
			for (int i=0; i<simLength; i++) {
				n.update(I);
				if (n.fired) {
					if (fireTime != 0) {
						System.out.print(n.lastTimeFired-fireTime+", ");
					}
					fireTime = n.lastTimeFired;
				}
			}
			System.out.println("])");
			String xName = "x"+dValue.replace(",", "");
			System.out.println(xName+ "= "+varName+".cumsum()");
			System.out.println("plt.plot("+xName+ ", "+varName+", label=\"d = "+d+"\", linewidth=1.5)");
		}
		System.out.println("plt.legend(loc=1)");
		System.out.println("plt.ylabel(\"Inter spike time\")");
		System.out.println("plt.xlabel(\"t\")");
		System.out.println("\"Development of spikeperiod with number of spikes\"");
		System.out.println("plt.text(400, 7, \"a = 0.01\\nb = 0.2\\nc = -65\")");
		System.out.println("plt.axis([0, 500, 0, 90])");
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
