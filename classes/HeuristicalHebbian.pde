// Heuristic implementation of Hebbian learning

public class HeuristicalHebbian implements Training {

  public float updateExistingConnection(Neuron n1, Neuron n2) {
    /** 
     * Update existing connection based on recent activity
     * of both neurons.
     */
    float update = 0.0;

    if (n1.fired) {     // check if n2 contributed to firing
      update = findIncrement(n1.lastTimeFired, n2.lastTimeFired);
    } else if (n2.lastTimeFired <= n1.lastTimeFired) {  // weight was updated when n1 fired last time
    } else {        // n1 was not activated through activation of n2
        update = 0.1*findIncrement(n1.lastTimeFired, n2.lastTimeFired);
    }

    return update;
  }

  public float updateNonExistingConnection(Neuron n1, Neuron n2) {
    float update = 0.0;
    return update;
  }

  public float findIncrement(int fireTime1, int fireTime2) {
    /**
     * Compute increment based on how far apart
     * the two neurons fired.
     */
    float increment = incrementUnit;
    if (fireTime1 == fireTime2) {   // no causal relationship
      increment *= 0.0;
    } else {        // if firetime2 is close enough to firetime1, increase weight
      increment/=(fireTime1-fireTime2);
    }

    return increment;
  }

}
