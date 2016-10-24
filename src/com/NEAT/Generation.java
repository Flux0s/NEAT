package com.NEAT;

import java.awt.*;
import java.util.ArrayList;

class Generation extends Thread {

	private ArrayList<Species> species;
	private Rectangle screen;
	private char[] output;
	private int maxPop;

	public Generation(Rectangle screenIn, char[] outputKeys) {
		screen = screenIn;
		output = outputKeys;
		maxPop = NEATAI.POPULATION;
	}


	@Override
	public void run() {
		while (!NEATAI.save) {

		}
	}
}
