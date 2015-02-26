// class representing a neuron

class Neuron {
  // Class representing a neuron
  float a, b, c, d;
  float u, v, I;        // u and v when they were last computed
  float last_time_fired;
  float last_time_computed;         // Time the potential was last computed
  boolean fired = false;

  int neighbours[];     // indices pointing to the neighbours of the neuron
  int weights[];        // weights to the neighbours

  int x;        // x coordinate for displaying neuron
  int y;        // y coordinate for displaying neuron
  int Nw;       // Neuron width

  // Constructor
  Neuron(float a, float b, float c, float d) {
    // Initialise neuron parameters
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;

    // Set inital values of neuron
    reset();
  }

  void reset() {
    // reset neuron and time
    last_time_computed = 0.0;
    v = c;
    u = b*c;
  }

  float[][] show_spike_behaviour(float I, int nr_of_steps) {
    // Show behaviour of neuron as a result of a constant input current
    // Isn't this a bit strange? How would a neuron receive a consistent
    // input current.. anyhow
    
    float[][] time_potential = new float[nr_of_steps][2];     // declare array to store output
    float time_step = 0.1;
    int nr_of_spikes = 0;

    // reset();     // Reset values as if neuron was just created

    // compute behaviour
    for (int i=0; i<nr_of_steps; i++) {
      float cur_time = i*0.1;
      time_potential[i][0] = cur_time;
      time_potential[i][1] = v;
      compute_next(I, cur_time, time_step);
      if (fired) {nr_of_spikes++;}
    }

    System.out.println(nr_of_spikes++);
    
    return time_potential;
  }
  
  void compute_next(float I, float cur_time, float time_step) {
    // Compute the membrane potential one time step t from now.
    // Current input to the neuron is I.

    // if potential crosses threshold, reset
    if (v>30) {
      v=c;
      u=u+d;
      fired = true;
      return;
    }

    v=v+0.5*time_step*(0.04*pow(v,2)+5*v+140-u+I); //% step 0.5 ms
    v=v+0.5*time_step*(0.04*pow(v,2)+5*v+140-u+I); //% for numerical
    u=u+time_step*a*(b*v-u); //% stability
    fired = false;

  }

  void set_v(float v) {
    // Set v to a certain value
    this.v = v;
  }

  void display() {
    // I should probably make this a little more elaborate later on
    
    fill((int)(256*((v-40)/-120.0)),(int)(256*(1.0-((v-40)/-120.0))),0); // set colour
    rect(x, y, Nw-2, Nw-2);       // create rectangle
  }

  boolean pointedAt() {
    // return whether the mouse is pointed at the neuron

    if (mouseX>x && mouseX<x+Nw && mouseY>y && mouseY<y+Nw) return true;
    else return false;
  }
    /*
  void update() {
    for (int i=0
  }
  */


}
