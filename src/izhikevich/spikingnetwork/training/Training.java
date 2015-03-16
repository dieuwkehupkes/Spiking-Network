package izhikevich.spikingnetwork.training;

import izhikevich.spikingnetwork.neuron.*;

public interface Training {
	  /**
	   * Specifies how the weights between
	   * two neurons in a network are
	   * updated depending on their recent activity
	   */

	  // fields
	  public double incrementUnit=10.0;      // unit of increment
	    
	  public double updateExistingConnection(Neuron n1, Neuron n2);

	  public double updateNonExistingConnection(Neuron n1, Neuron n2);

	  public double findIncrement(int t1, int t2);
	
}
