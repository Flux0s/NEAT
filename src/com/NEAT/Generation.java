package com.NEAT;

import java.awt.*;
import java.util.ArrayList;

class Generation {

	private ArrayList<Species> species;
	private Rectangle screen;
	private char[] output;
	private int maxPop;

	public Generation(Rectangle screenIn, char[] outputKeys) {
		screen = screenIn;
		output = outputKeys;
		maxPop = NEATAI.POPULATION;
	}

	public void startGeneration() {
		while (!NEATAI.save) {
			//TODO: call the appropriate methods of genomes and species in order.
		}
	}
}
