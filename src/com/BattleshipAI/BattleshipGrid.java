package com.BattleshipAI;

public class BattleshipGrid {
    private char[][] HME;

    public BattleshipGrid() {
        HME = new char[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                HME[i][j] = 'E';
    }

    public void shotAt(Position pos, boolean hit, char initial) {
        if (hit)
            HME[pos.rowIndex()][pos.columnIndex()] = Character.toUpperCase(initial);
        else
            HME[pos.rowIndex()][pos.columnIndex()] = 'M';
    }

    public boolean hit(Position pos) {
        return (HME[pos.rowIndex()][pos.columnIndex()] != 'E' && HME[pos.rowIndex()][pos.columnIndex()] != 'M');
    }

    public char boatInitial(Position pos) {
        return (HME[pos.rowIndex()][pos.columnIndex()]);
    }

    public boolean miss(Position pos) {
        return (HME[pos.rowIndex()][pos.columnIndex()] == 'M');
    }

    public boolean empty(Position pos) {
        return (HME[pos.rowIndex()][pos.columnIndex()] == 'E');
    }

    public String toString() {
        char current = 'B';
        String output = "  1 2 3 4 5 6 7 8 9 10\n" + "A ";
        for (int i = 0; i < HME.length; i++) {
            for (int j = 0; j < HME.length; j++) {
                output += (HME[i][j]) + " ";
            }
            output += "\n" + (char) (current + i) + " ";
        }
        return (output);
    }
}