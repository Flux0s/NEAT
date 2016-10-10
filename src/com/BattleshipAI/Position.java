package com.BattleshipAI;

public class Position {
    private char row;
    private int col;
    private boolean hit;

    public Position(char charIn, int intIn) {
        row = Character.toLowerCase(charIn);
        col = intIn;
    }

    public Position(int charIn, int intIn) {
        row = (char) (charIn + 'a');
        col = intIn + 1;
    }

    private int column() {
        return (col);
    }

    public int columnIndex() {
        return (col - 1);
    }

    private char row() {
        return (row);
    }

    public int rowIndex() {
        return (row - 'a');
    }

    public boolean getHit() {
        return hit;
    }

    public void setHit(boolean request) {
        hit = request;
    }

    public String toString() {
        return (row() + "-" + column());
    }
}