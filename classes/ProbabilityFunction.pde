// Interface for probability distributions computing
// how likely neurons are to connect with each other

interface ProbabilityFunction {
  /**
   * Class used as function pointer
   */

  public float probability(float distance);     // compute probability of connection

}
