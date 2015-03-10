// Manhattan distance class designed to use as function pointer

class ManhattanDistance extends DistanceMetric {

  public float distance(Neuron n1, Neuron n2) {
    /**
     * Return the manhattan distance between neuron n1
     * and neuron n2
     */
    int distance = (abs(n1.x - n2.x) + abs(n1.y - n2.y))/neuronWidth();
    return distance;
  }

}


