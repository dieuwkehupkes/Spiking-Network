// Interface for different training implementations

interface Training {
  /**
   * Specifies how the weights between
   * two neurons in a network are
   * updated depending on their recent activity
   */
    
  public float updateExistingConnection(Neuron n1, Neuron n2);

  public float updateNonExistingConnection(Neuron n1, Neuron n2);

}

