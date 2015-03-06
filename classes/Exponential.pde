// Probability decreases exponentially with distance

class Exponential implements ProbabilityFunction {

  // fields
  float param;

  // Constructor
  public Exponential(float param) {
    this.param = param;
  }

  public float probability(float distance) {
    /**
     * Return the probability that neurons with
     * this distance apart are connected
     */
    float prob = pow(param, distance);
    return prob;
  }
}

