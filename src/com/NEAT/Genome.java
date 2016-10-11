package com.NEAT;

/**
 * Represents a genome; a single Neural Network
 */

import java.util.ArrayList;
import java.util.Map;

class Genome {
    private Map<Node, ArrayList<Link>> genes;

    public Genome(ArrayList<Node> input) {

    }

    // Used during crossover
    public Genome(ArrayList<Link> geneIn, ArrayList<Node> nodeIn) {

    }


    // Use if mutation has been determined as true
    public Genome(Genome base, boolean isLink) {
    }

    public ArrayList<Double> calculateOutput(ArrayList<Double> inputs) {
    }

    public int size() {
    }

    private ArrayList<Link> getGenes() {
    }

    private ArrayList<Node> getNodes() {
    }

    public double getFitness() {
    }

    public void setFitness(double fitIn) {
    }

    public ArrayList<Node> getOutput() {
    }

    private boolean outputEmpty() {
    }

    private void clearNetwork() {
    }

    private static int newGene() {
    }

}
