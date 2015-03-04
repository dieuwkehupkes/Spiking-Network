// Class for inhibitory neurons, i.e.:
// a = 0.02
// b = 0.2
// -65 < c < -50
// 2 < d < 8

public class ExcitatoryNeuron extends Neuron {

  // Constructor1 generate inhibitory neuron with
  // random parameters
  public ExcitatoryNeuron() {
    // call constructor super
    super(0.02, 0.2, -65+15*random(1.0), 8-6*random(1.0));
  }

  // constructor with specified parameters
  public ExcitatoryNeuron(float a, float b, float c, float d) {
    
    // call constructor super
    super(a, b, c, d);
    validateParameters(0.02, 0.02, 0.2, 0.2, -65, -50, 2, 8);
  }

  public void display() {
    // give extra border to excitatory neurons
    super.display();
    rect(x+1, y+1, Nw-4, Nw-4);
    rect(x+2, y+2, Nw-6, Nw-6);
  }

}
