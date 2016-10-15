package com.NEAT;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents a node in the Neural Network
 * In this representation the node is not represented as a gene in the genome
 */

//TODO: consider implementing each node as a thread
class Node {
	private double value[][];
	public boolean activated;

	// Used to create a new Node gene that is unique
	public Node() {
		for (int i = 0; i < value.length; i++)
			for (int j = 0; j < value[0].length; j++)
				value[i][j] = 0.0;
		activated = false;
	}

	//Applies a Sigmoidal activation function to the input
	public double[][] activate(ArrayList<double[][]> in) {
		activated = true;
		for (int o = 0; o < in.size(); o++) {
			for (int i = 0; i < in.get(o).length; i++)
				for (int j = 0; j < in.get(o).length; j++)
					value[i][j] += in.get(o)[i][j];
		}
		for (int i = 0; i < value.length; i++)
			for (int j = 0; j < value[0].length; j++)
				value[i][j] = 1 / (1 + Math.pow(Math.E, -4.9 * value[i][j]));
		return (value);
	}

	//Returns the value that the
	public double[][] getValue() {
		return (value);
	}

}