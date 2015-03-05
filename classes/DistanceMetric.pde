// Interface for different distance functions that can be used

interface DistanceMetric {
  /** 
   * class used as function
   * pointer for distance functions
   */

  public float distance(Neuron n1, Neuron n2);

}
