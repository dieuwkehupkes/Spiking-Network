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
			output[counter][0] = (float) (tuple[0]*scalex);
			output[counter][0] += shiftx;

			// apply scaling and shifting of potential variable
			output[counter][1] = (float) (-tuple[1]*scaley);	// scale and flip sign
			output[counter][1] += shifty;

			// increase counter
			counter++;
		}

		return output;	
	}
	
}
