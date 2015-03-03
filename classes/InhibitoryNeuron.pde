// Class for inhibitory neurons, i.e.:
// 0.02 <= a <= 0.1
// 0.2 <= b <= 0.3
// c = -65
// d = 2

public class InhibitoryNeuron extends Neuron {

  // Constructor1 generate inhibitory neuron with
  // random parameters
  public InhibitoryNeuron() {
    // call constructor super
    super(0.02+0.08*random(1.0), 0.25-0.05*random(1.0), -65, 2);
  }

  // constructor with specified parameters
  public InhibitoryNeuron(float a, float b, float c, float d) {
    
    // call constructor super
    super(a, b, c, d);
    validateParameters(0.02, 0.1, 0.2, 0.3, -65, -65, 2, 2);
  }

}
