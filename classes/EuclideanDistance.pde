// Class to use as function pointer for euclidean Distance

class EuclideanDistance extends DistanceMetric {

  public float distance(Neuron n1, Neuron n2) {
  /**
   * Return the euclidean distance between neuron n1
   * and neuron n2
   */
    float distance = sqrt(pow((n1.x - n2.x)/neuronWidth(), 2) + pow((n1.y - n2.y)/neuronWidth(), 2));
    return distance;
  }

}

 
