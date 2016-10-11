package com.NEAT;

/**
 * Represents a node in the Neural Network
 * In this representation the node is not represented as a gene in the genome
 */

//TODO: consider implementing each node as a thread
class Node {
    private double value;
    public boolean activated;

    // Used to create a new Node gene that is unique
    public Node() {
        value = 0.0;
        activated = false;
    }

    //Applies a Sigmoidal activation function to the input
    public double activate(double in) {
        activated = true;
        return (value = 1 / (1 + Math.pow(Math.E, -4.9 * in)));
    }

    //Returns the value that the
    public double getValue() {
        return (value);
    }

}
