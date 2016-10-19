package com.NEAT;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Represents a node inputLinks the Neural Network
 * In this representation the node is not represented as a gene inputLinks the genome
 */

class Node extends Thread {
	//A temporary array to hold the activated values
	private double value[][];
	//Whether or not the node has been activated in the current
	boolean activated;
	//Whether or not the node is an input
	private boolean input;
	//The standard size of the broken up screen sections
	private static int r, c;

	//Represents an area of the screen
	private Rectangle section;

	// Used to create a new Node that is unique based on the previously set dimensions
	public Node(boolean isInput, Rectangle inputArea) {
		input = isInput;
		section = inputArea;
		r = (int) section.getHeight();
		c = (int) section.getWidth();
		value = new double[r][c];
		for (int i = 0; i < value.length; i++)
			for (int j = 0; j < value[0].length; j++)
				value[i][j] = 0.0;
		activated = false;
	}

	//Applies a Sigmoidal activation function to the input
	public double[][] activate(ArrayList<double[][]> inputLinks) {
		activated = true;
		if (input) {
			Robot scan = null;
			try {
				scan = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			BufferedImage capture = scan.createScreenCapture(section);
			for (int i = 0; i < r; i++)
				for (int j = 0; j < c; j++)
					value[i][j] = (double) capture.getRGB(i, j);
		} else {
			for (int o = 0; o < inputLinks.size(); o++) {
				for (int i = 0; i < r; i++)
					for (int j = 0; j < c; j++)
						value[i][j] += inputLinks.get(o)[i][j];
			}
			for (int i = 0; i < value.length; i++)
				for (int j = 0; j < value[0].length; j++)
					value[i][j] = 1 / (1 + Math.pow(Math.E, -4.9 * value[i][j]));
		}

		return (value);
	}

	//Returns the value of the node
	public double[][] getValue() {
		return (value);
	}

	//Requires that setIn has been called directly before starting the thread
	public void run(ArrayList<double[][]> in) {
		if (!activated)
			activate(in);
	}
}