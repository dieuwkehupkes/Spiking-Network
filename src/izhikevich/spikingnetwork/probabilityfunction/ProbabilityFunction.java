package izhikevich.spikingnetwork.probabilityfunction;

public interface ProbabilityFunction {
	  /**
	   * Class used as function pointer
	   */

	  public double probability(double distance);     // compute probability of connection

}
