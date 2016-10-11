package com.NEAT;

/**
 * Represents a gene in the genome and contains the node that passes it a normalized value
 * Each Gene, or Link, is represented individually by an innovation number
 * s
 */

class Link {
    //Represents the total number of genes that have been created
    private static int innovation = 0;
    //represents the actual number that the particular gene is
    private int geneNum;
    //The value multiplied by the previous node's value
    private double weight;
    //The node that provides a value to the link
    private Node preCondition;
    //Used to show whether or not the
    private boolean enabled;

    //Used to create an new link for the first time
    public Link(Node pre) {
        geneNum = innovate();
        preCondition = pre;
        weight = 1.0;
    }

    //Increments the total number of genes greated in the NEAT by one
    private static int innovate() {
        return (innovation++);
    }

    //This method will throw an exception if the previous node in the sequence has not been activated
    public double calculate() throws Exception {
        if (preCondition.activated)
            throw new Exception();
        return (preCondition.getValue() * weight);
    }

    public void setWeight(double in) {
        weight = in;
    }

    public boolean getEnabled() {
        return (enabled);
    }
}
