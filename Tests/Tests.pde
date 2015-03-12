/** 
 * Test suite for neural networks with Izhikevich neurons
 * JUnittests is used to write the tests, but as I have no clue
 * how to properly integrate this in Processing the tests are 
 * executed manually (i.e., all test classes contain a method
 * testAll() that executes all available tests in the class.
 *
 * @author Dieuwke Hupkes
 */

void setup() {

  NeuronTest neuronTest = new NeuronTest();
  neuronTest.testAll();
  System.out.println("Testing finished");
  exit();

}
