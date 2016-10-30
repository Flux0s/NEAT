package com.NEAT;

/**
 * Represents a gene in the genome and contains the node that passes it a normalized value
 * Each Gene, or Link, is represented individually by an innovation number
 */

class Link {
	//Represents the total number of genes that have been created
	private static int innovation = 0;
	//represents the actual number that the particular gene is
	private int geneNum;
	//The value multiplied by the previous node's value
	private double weight;
	//The node that gives a value to the
	private Node preCondition, postCondition;
	//Used to show whether or not the
	private boolean enabled;

	//Used to create an new link for the first time
	Link(Node pre, Node post) {
		geneNum = innovate();
		preCondition = pre;
		postCondition = post;
		weight = 1.0;
	}

	//Used to create a copy of a link that has previously been created
	Link(Link copiedLink) {
		innovation = copiedLink.getID();
		preCondition = copiedLink.getPre();
		postCondition = copiedLink.getPost();
		weight = copiedLink.getWeight();
		enabled = copiedLink.isEnabled();
	}

	//Increments the total number of genes created in the NEAT by one
	private static int innovate() {
		return (innovation++);
	}

	//This method will throw an exception if the previous node in the sequence has not been activated
	double[][] calculate() throws Exception {
		if (preCondition.activated())
			throw new Exception();
		double output[][] = preCondition.getValue();
		for (int i = 0; i < output.length; i++)
			for (int j = 0; j < output[0].length; j++)
				output[i][j] *= weight;
		return (output);
	}

	void randomizeWeight() {
		weight = Math.random() * 3 - 1;
		if (weight > 1)
			weight = Math.floor(weight);
	}

	double getWeight() {
		return (weight);
	}

	public void setWeight(double weightIn) {
		weight = weightIn;
	}

	boolean isEnabled() {
		return (enabled);
	}

	Node getPre() {
		return (new Node(preCondition));
	}

	int getID() {
		return (geneNum);
	}

	void setEnabled(boolean en) {
		enabled = en;
	}

	void setPreCondition(Node newPre) {
		preCondition = newPre;
		geneNum = innovate();
	}

	public Node getPost() {
		return (new Node(postCondition));
	}
}
