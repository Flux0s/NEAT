package com.BattleshipAI;

import java.util.ArrayList;

class Ocean {
    private ArrayList<Boat> fleet;
    private char[][] grid;

    public Ocean() {
        grid = new char[10][10];
        fleet = new ArrayList<Boat>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid[i][j] = '*';
            }
        }
    }

    private void placeBoat(String boatName, String direction, Position pos) throws Exception {
        Boat tempBoat = new Boat(boatName, direction, pos);
        if ((tempBoat.direction().substring(0, 1).equals("h")) && (tempBoat.position().columnIndex() + tempBoat.size() - 1 > 9) || (tempBoat.direction().substring(0, 1).equals("v")) && (tempBoat.position().rowIndex() + tempBoat.size() - 1 > 9) || (tempBoat.position().rowIndex() < 0) || (tempBoat.position().columnIndex() < 0))
            throw (new Exception());
        if (tempBoat.direction().substring(0, 1).equals("h")) {
            for (int i = 0; i < fleet.size(); i++)
                for (int j = 0; j < tempBoat.size(); j++) {
                    Position pos1 = new Position(pos.rowIndex(), pos.columnIndex() + j);
                    if (fleet.get(i).onBoat(pos1))
                        throw (new Exception());
                }
        } else
            for (int i = 0; i < fleet.size(); i++)
                for (int j = 0; j < tempBoat.size(); j++) {
                    Position pos1 = new Position(pos.rowIndex() + j, pos.columnIndex());
                    if (fleet.get(i).onBoat(pos1))
                        throw (new Exception());
                }
        fleet.add(tempBoat);
        if (tempBoat.direction().substring(0, 1).equals("h"))
            for (int i = 0; i < tempBoat.size(); i++)
                grid[tempBoat.position().rowIndex()][i + tempBoat.position().columnIndex()] = tempBoat.abbreviation();
        else
            for (int i = 0; i < tempBoat.size(); i++)
                grid[i + tempBoat.position().rowIndex()][tempBoat.position().columnIndex()] = tempBoat.abbreviation();
    }

    public void placeAllBoats() {
        while (fleet.size() < 5) {
            int randY = (int) (Math.random() * 10);
            int randX = (int) (Math.random() * 10);
            int randL = (int) (Math.random() * 2);
            Position pos = new Position(randY, randX);
            String direction;
            String name = null;
            if (randL == 0)
                direction = "Horizontal";
            else
                direction = "Vertical";
            switch (fleet.size()) {
                case 0:
                    name = "Aircraft Carrier";
                    break;
                case 1:
                    name = "BattleshipAI";
                    break;
                case 2:
                    name = "Cruiser";
                    break;
                case 3:
                    name = "Submarine";
                    break;
                case 4:
                    name = "Destroyer";
                    break;
            }
            try {
                placeBoat(name, direction, pos);
            } catch (Exception ignored) {
            }
        }
    }

    public void shootAt(Position pos) {
        if (grid[pos.rowIndex()][pos.columnIndex()] != '*') {
            grid[pos.rowIndex()][pos.columnIndex()] = Character.toLowerCase(grid[pos.rowIndex()][pos.columnIndex()]);
            for (int i = 0; i < fleet.size(); i++)
                if (fleet.get(i).onBoat(pos))
                    fleet.get(i).hit(pos);
        }
    }

    public boolean hit(Position pos) {
        return (grid[pos.rowIndex()][pos.columnIndex()] != Character.toUpperCase(grid[pos.columnIndex()][pos.rowIndex()]));
    }

    public String boatName(Position pos) {
        switch (Character.toLowerCase(grid[pos.rowIndex()][pos.columnIndex()])) {
            case 'a':
                return ("Aircraft Carrier");
            case 'b':
                return ("BattleshipAI");
            case 'c':
                return ("Cruiser");
            case 's':
                return ("Submarine");
            case 'd':
                return ("Destroyer");
        }
        return (null);
    }

    public boolean sunk(Position pos) {
        for (int i = 0; i < fleet.size(); i++)
            if (fleet.get(i).sunk() && fleet.get(i).onBoat(pos))
                return (true);
        return (false);
    }

    public boolean allSunk() {
        for (int i = 0; i < fleet.size(); i++)
            if (!fleet.get(i).sunk())
                return (false);
        return (true);
    }

    public String toString() {
        char current = 'A';
        String output = "  1 2 3 4 5 6 7 8 9 10";
        for (int i = 0; i < grid.length; i++) {
            output += "\n" + (char) (current + i) + " ";
            for (int j = 0; j < grid[0].length; j++) {
                output += (grid[i][j] + " ");
            }
        }
        // output += "\n" + fleet.toString();
        return (output);
    }
}