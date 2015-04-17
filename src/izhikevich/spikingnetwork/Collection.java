package izhikevich.spikingnetwork;

public class Collection {
	/**
	 * Collection of functions used throughout the
	 * package
	 */

	public static float[][] shift_and_scale(float[][] i, float shiftx, float shifty, double scalex, double scaley) {
		// function for shifting and scaling values to plot them
		float[][] output = new float[i.length][i[0].length];

		int counter = 0;
		for (float[] tuple: i) {

			// apply scaling and shifting of time variable
			output[counter][0] = tuple[0] + shiftx;
			output[counter][0] *= scalex;

			// apply scaling and shifting of potential variable
			output[counter][1] = -1*tuple[1];        // flip sign to get direction right
			output[counter][1] += shifty;
			output[counter][1] *= scaley;

			// increase counter
			counter++;
		}

		return output;	
	}
	
}
