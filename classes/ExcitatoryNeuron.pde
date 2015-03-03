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
    super();

    // generate parameters
    float re=random(1.0);
    float a = 0.02;
    float b = 0.2;
    float c = -65+15*re;
    float d = 8-6*re;

    // set parameters
    setParams(a, b, c, d);
    super.reset();

  }

  // constructor with specified parameters
  public ExcitatoryNeuron(float a, float b, float c, float d) {
    
    // initialise empty neuron
    super();

    // check if parameters are within the range of inhibitory neurons
    IllegalArgumentException wrongRangeExceptionA = new IllegalArgumentException("Parameter a outside range of inhibitory neuron.");
    IllegalArgumentException wrongRangeExceptionB = new IllegalArgumentException("Parameter b outside range of inhibitory neuron.");
    IllegalArgumentException wrongRangeExceptionC = new IllegalArgumentException("Parameter c outside range of inhibitory neuron.");
    IllegalArgumentException wrongRangeExceptionD = new IllegalArgumentException("Parameter d outside range of inhibitory neuron.");
    if (a != 0.02) throw wrongRangeExceptionA;
    if (b != 0.2) throw wrongRangeExceptionB;
    if (c < -65 || c > -50) throw wrongRangeExceptionC;
    if (d < 2 || d > 8) throw wrongRangeExceptionD;

    // set parameters
    setParams(a, b, c, d);

    super.reset();

  }

}
