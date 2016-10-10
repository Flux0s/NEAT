package com.NEAT;

abstract class Gene implements Runnable {
    private static int innovation;
    private int geneID;
    double value;

    Gene() {
        geneID = innovation;
        innovate();
    }

    Gene(int innovationNumber) {
        geneID = innovationNumber;
    }

    private static void innovate() {
        innovation++;
    }
}
