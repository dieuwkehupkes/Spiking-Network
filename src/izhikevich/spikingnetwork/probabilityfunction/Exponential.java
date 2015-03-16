package izhikevich.spikingnetwork.probabilityfunction;

public class Exponential implements ProbabilityFunction{
	// fields
	  double param;

	  // Constructor
	  public Exponential(double d) {
	    this.param = d;
	  }

	  public double probability(double distance) {
	    /**
	     * Return the probability that neurons with
	     * this distance apart are connected
	     */
	    double prob = Math.pow(param, distance);
	    return prob;
	  }
	
}
