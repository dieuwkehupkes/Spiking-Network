// Interface for different training implementations

interface Training {
  /**
   * Specifies how the weights between
   * two neurons in a network are
   * updated depending on their recent activity
   */

  // fields
  public float incrementUnit=10.0;      // unit of increment
    
  public float updateExistingConnection(Neuron n1, Neuron n2);

  public float updateNonExistingConnection(Neuron n1, Neuron n2);

  public float findIncrement(int t1, int t2);

}

