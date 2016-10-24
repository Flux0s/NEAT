package com.NEAT;

/**
 * Represents a genome; a single Neural Network
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Genome {
	//This is a representation of the genome including the nodes
	//This means that the actual manipulable genome must be created from this
	private Map<Node, ArrayList<Link>> genes;
	private char[] outputKeys;
	private double fitness;
	private ArrayList<Link> links;

	//Used to create the first genome in the NEAT
	public Genome(Rectangle screen, char[] output) {
		genes = new HashMap<Node, ArrayList<Link>>();
		links = new ArrayList<Link>();
		Iterator it = genes.entrySet().iterator();
		while (it.hasNext()) {
			links.add((Link) it.next());
		}
		ArrayList<Rectangle> optimal = findOptimalInputs();
		for (int i = 0; i < optimal.size(); i++)
			genes.put(new Node(Node.INPUT, optimal.get(i)), null);
		for (int i = 0; i < outputKeys.length; i++) {
			genes.put(new Node(Node.OUTPUT, new Rectangle()), null);
		}
	}

	private ArrayList<Rectangle> findOptimalInputs() {
		//TODO: Determine the optimal size and positioning of the specified number of screen divisions.
	}

	// Used during crossover
	public Genome(ArrayList<Link> geneIn, ArrayList<Node> nodeIn) {

	}

	private boolean unactivated() {
		Iterator it = genes.entrySet().iterator();
		while (it.hasNext()) {
			Node check = (Node) it.next();
			if (!check.activated()) {
				return (false);
			}
		}
		return (true);
	}


	// Use if mutation has been determined as true
	public Genome(Genome base, boolean isLink) {
	}

	public void calculateOutput() {
		while (unactivated()) {
			Iterator it = genes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Node activate = (Node) pair.getKey();
				ArrayList<Link> itLinks = (ArrayList<Link>) pair.getValue();
				if (itLinks != null) {
					for (int i = 0; i < itLinks.size(); i++) {
						ArrayList<double[][]> inputs = new ArrayList<double[][]>();
						//Loops through all connected links to check for links that cannot be calculated
						if (!activate.activated()) {
							try {
								inputs.add(itLinks.get(i).calculate());
							} catch (Exception e) {
								//If the link's prerequisite node hasn't been activated yet skip this node for now
								i = itLinks.size();
							}
						}
						activate.run(inputs);
					}
				} else if (activate.getType() != Node.INPUT) {
					activate.setActivated(true);
				}
			}
		}
		//TODO: Actually implement the AI to push the specified keys here.
	}

	public int size() {
		return (links.size());
	}

	public double getFitness() {
		return (fitness);
	}

	public void setFitness(double fitnessIn) {
		fitness = fitnessIn;
	}

	public ArrayList<Link> getGenes() {
		return (links);
	}
}
