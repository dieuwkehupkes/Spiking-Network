package izhikevich.spikingnetwork;

import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
	
	public static double[] getUserInputNeuron() {
		return getUserInputNeuron(0.01, 0.2, -65, 2, 10);
	}

	public static double[] getUserInputNeuron(double a, double b, double c, double d, double I) {
		
		DecimalFormat df = new DecimalFormat("#.##");

		double[] outputArray = new double[5];
		JTextField aVal = new JTextField(df.format(a).replace(",", "."));
		JTextField bVal = new JTextField(df.format(b).replace(",", "."));
		JTextField cVal = new JTextField(df.format(c).replace(",", "."));
		JTextField dVal = new JTextField(df.format(d).replace(",", "."));
		JTextField IVal = new JTextField(df.format(I).replace(",", "."));
		
		Object[] message = {
				"a", aVal,
				"b", bVal,
				"c", cVal,
				"d", dVal,
				"I", IVal,
				""
		};
		
		int out = JOptionPane.showConfirmDialog(null, message, "Specify Parameters", JOptionPane.OK_CANCEL_OPTION);
		
		if (out == JOptionPane.OK_OPTION) {
			outputArray[0] = Double.parseDouble(aVal.getText());	// a
			outputArray[1] = Double.parseDouble(bVal.getText());			// b
			outputArray[2] = Double.parseDouble(cVal.getText());			// c
			outputArray[3] = Double.parseDouble(dVal.getText());			// d
			outputArray[4] = Double.parseDouble(IVal.getText());			// I

		}
		
		else if (out == JOptionPane.CANCEL_OPTION) {
			// do nothing
		}
		
		return outputArray;
	}
	
}
