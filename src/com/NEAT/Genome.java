package com.NEAT;

/**
 * Represents a genome; a single Neural Network
 */

import java.awt.*;
import java.lang.reflect.Array;
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
	Genome(Rectangle screen, char[] output) {
		genes = new HashMap<Node, ArrayList<Link>>();
		links = new ArrayList<Link>();
		Iterator it = genes.entrySet().iterator();
		while (it.hasNext()) {
			links.add((Link) it.next());
		}
		ArrayList<Rectangle> screenSections = new ArrayList<Rectangle>();
		screenSections.add(screen);
		ArrayList<Rectangle> optimal = findOptimalInputs(screenSections);
		for (Rectangle anOptimal : optimal)
			genes.put(new Node(Node.INPUT, anOptimal), new ArrayList<Link>());
		for (char outputKey : outputKeys) {
			genes.put(new Node(Node.OUTPUT, new Rectangle()), new ArrayList<Link>());
		}
		outputKeys = output;
	}

	// Use if mutation has been determined as true
	void mutateGenome(Genome base, boolean isLink) {
		Map.Entry openLink = findOpenLinkSpace();
		if (isLink && openLink != null) {
			genes.replace((Node) openLink.getKey(), (ArrayList<Link>) openLink.getValue());
		} else if (getGenes().size() < 0) {
			Node newNode = new Node(Node.HIDDEN, new Rectangle(getNodes().get(0).getSection()));
			Link split = getGenes().get((int) (Math.random() * getGenes().size()));
			Iterator it = genes.entrySet().iterator();
			while (it != null) {
				Map.Entry pair = (Map.Entry) it.next();
				links = (ArrayList<Link>) pair.getValue();
				for (int i = 0; i < links.size(); i++) {
					if (links.get(i) == split) {
						//Creates a new link in the place of a random previous link with the new node as destination
						ArrayList<Link> newNodeLinks = new ArrayList<>();
						newNodeLinks.add(new Link(links.get(i).getPre(), links.get(i).getPost()));
						//Disables the previous link
						links.get(i).setEnabled(false);
						//Searches for the old link's destination to add a new link from the new node to the old destination
						for (int j = 0; j < getNodes().size(); j++) {
							Node checkNode = getNodes().get(j);
							for (int k = 0; k < genes.get(checkNode).size(); k++)
								if (genes.get(checkNode).get(k) == links.get(i)) {
									ArrayList<Link> replacement = genes.get(checkNode);
									replacement.add(new Link(newNode, checkNode));
									genes.replace(checkNode, replacement);
								}
						}
						genes.put(newNode, newNodeLinks);
						it = null;
					}
				}
			}
		}
	}

	private Map.Entry findOpenLinkSpace() {
		Iterator it = genes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Node node = (Node) pair.getKey();
			ArrayList<Link> links = (ArrayList<Link>) pair.getValue();
			if (links.size() < getNodes().size() - 1) {
				//There is an opportunity for a new node
				Iterator it2 = genes.entrySet().iterator();
				Node check = null;
				while (check != node) {
					check = (Node) it2.next();
				}
				links.add(new Link(check, node));
				Map<Node, ArrayList<Link>> temp = new HashMap<>();
				temp.put(node, links);
				it2 = temp.entrySet().iterator();
				return ((Map.Entry) it2.next());
			}
		}
		return (null);
	}

	private ArrayList<Rectangle> findOptimalInputs(ArrayList<Rectangle> big) {
		if (big.size() < NEATAI.NUMINPUTNODES)
			return (findOptimalInputs(splitRectangles(big)));
		else
			return (big);
	}

	private ArrayList<Rectangle> splitRectangles(ArrayList<Rectangle> big) {
		ArrayList<Rectangle> small = new ArrayList<Rectangle>();
		//If the height needs to be divided in two
		if (big.get(0).getHeight() > big.get(0).getWidth())
			for (int i = 0; i < big.size(); i++) {
				Rectangle rec = big.get(i);
				small.add(new Rectangle(rec.x, rec.y, rec.width, rec.height / 2));
				small.add(new Rectangle(rec.x, rec.y + rec.height / 2, rec.width, rec.height / 2));
			}
		else
			for (int i = 0; i < big.size(); i++) {
				Rectangle rec = big.get(i);
				small.add(new Rectangle(rec.x, rec.y, rec.width / 2, rec.height));
				small.add(new Rectangle(rec.x + rec.width / 2, rec.y, rec.width / 2, rec.height));
			}
		return (small);
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

	//This begins the process of determining the keyboard output and automatically pressing assigned keys based on screen at the time of running
	public void output() {
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
		Iterator it = genes.entrySet().iterator();
		ArrayList<Node> nodes = new ArrayList<Node>();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Node out = (Node) pair.getKey();
			nodes.add((Node) it.next());
		}
		for (int i = nodes.size() - outputKeys.length; i < nodes.size(); i++) {
			Robot keyboard = null;
			try {
				keyboard = new Robot();
			} catch (Exception ignore) {
			}
			if (nodes.get(i).getValue()[0][0] == 1) {
				keyboard.keyPress(i - nodes.size() - outputKeys.length);
				keyboard.keyRelease(i - nodes.size() - outputKeys.length);
			}
		}
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

	public ArrayList<Node> getNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		Iterator it = genes.entrySet().iterator();
		while (it.hasNext()) {
			nodes.add((Node) it.next());
		}
		return nodes;
	}
}
