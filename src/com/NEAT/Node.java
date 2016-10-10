package com.NEAT;

import java.util.ArrayList;

/**
 * Created by Jason on 5/27/2016.
 */

class Node extends Gene {
    public static final int INPUT = 0;
    public static final int HIDDEN = 1;
    public static final int OUTPUT = 2;
    private int type;

    // Used to create a new Node gene that is unique
    public Node(int nodeType) {
        super();
        type = nodeType;
    }

    // Used to create a gene that has already been evolved by the GE
    public Node(int nodeType, int innovation) {
        super(innovation);
        type = nodeType;
    }

    public double activate(ArrayList<Integer> in) {
        double sum = 0;
        for (int i = 0; i < in.size(); i++) {
            sum += in.get(i);
        }
        Integer i = 1;
        value = 1 / (1 + Math.pow(Math.E, -4.9 * sum));
        return (value);
    }

    public int getType() {
        return (type);
    }

    public void run() {

    }

}
