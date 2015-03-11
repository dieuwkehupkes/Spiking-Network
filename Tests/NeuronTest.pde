import org.junit.Test;
import org.junit.*;

class NeuronTest extends UnitTest {
  /**
   * Tests for methods in
   * neuron
   */

  @Test
    public void testConstruction() {
      Neuron neuron = new Neuron(1.0, 2.0, 3.0, 4.0);

      // test if parameters are correctly assigned
      Assert.assertEquals(neuron.a, 1.0, 10e-10);
      Assert.assertEquals(neuron.b, 2.0, 10e-10);
      Assert.assertEquals(neuron.c, 3.0, 10e-10);
      Assert.assertEquals(neuron.d, 4.0, 10e-10);

      // test if u and v are initialised correctly
      Assert.assertEquals(neuron.v, 3.0, 10e-10);
      Assert.assertEquals(neuron.u, 6.0, 10e-10);
    }

}
