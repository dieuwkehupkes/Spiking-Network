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
    super();

    // generate parameters
    float ri=random(1.0);
    float a = 0.02+0.08*ri;
    float b = 0.25-0.05*ri;
    float c = -65;
    float d = 2;

    // set parameters
    setParams(a, b, c, d);
  }

  // constructor with specified parameters
  public InhibitoryNeuron(float a, float b, float c, float d) {
    
    // initialise empty neuron
    super();

    // check if parameters are within the range of inhibitory neurons
    IllegalArgumentException wrongRangeExceptionA = new IllegalArgumentException("Parameter a outside range of inhibitory neuron.");
    IllegalArgumentException wrongRangeExceptionB = new IllegalArgumentException("Parameter b outside range of inhibitory neuron.");
    IllegalArgumentException wrongRangeExceptionC = new IllegalArgumentException("Parameter c outside range of inhibitory neuron.");
    IllegalArgumentException wrongRangeExceptionD = new IllegalArgumentException("Parameter d outside range of inhibitory neuron.");
    if (a < 0.02 || a > 0.1) throw wrongRangeExceptionA;
    if (b < 0.2 || b > 0.3) throw wrongRangeExceptionB;
    if (c != -65) throw wrongRangeExceptionC;
    if (d != 2) throw wrongRangeExceptionD;

    // set parameters
    setParams(a, b, c, d);

  }

}
