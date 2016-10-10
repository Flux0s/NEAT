package com.BattleshipAI;

class Boat {

    private String type, direction;
    private Position spot;
    private boolean[] hits;
    private int size;

    public Boat(String boatType, String directionIn, Position spotIn) {
        type = boatType.toLowerCase();
        spot = spotIn;
        direction = directionIn.toLowerCase();
        hits = new boolean[this.size()];
        size = this.size();
    }

    public String name() {
        return type;
    }

    public Position position() {
        return spot;
    }

    public String direction() {
        return direction;
    }

    public char abbreviation() {
        return Character.toUpperCase(type.charAt(0));
    }

    public int size() {
        switch (this.abbreviation()) {
            case 'A':
                return (5);
            case 'B':
                return (4);
            case 'C':
                return (3);
            case 'S':
                return (3);
            case 'D':
                return (2);
        }
        return (0);
    }

    public boolean onBoat(Position spotIn) {
        if (direction.substring(0, 1).equals("h") && spotIn.columnIndex() >= this.spot.columnIndex() && spotIn.columnIndex() < this.spot.columnIndex() + size() && spotIn.rowIndex() == this.spot.rowIndex())
            return (true);
        return (direction.substring(0, 1).equals("v") && spotIn.rowIndex() >= this.spot.rowIndex() && spotIn.rowIndex() < this.spot.rowIndex() + size() && spotIn.columnIndex() == this.spot.columnIndex());
    }

    public boolean isHit(Position spotIn) {
        return (spotIn.getHit() && onBoat(spotIn));
    }

    public void hit(Position spotIn) {
        spotIn.setHit(onBoat(spotIn));
        for (int i = 0; i < hits.length; i++)
            if (!hits[i]) {
                hits[i] = true;
                i = hits.length;
            }
    }

    public boolean sunk() {
        for (int i = 0; i < hits.length; i++)
            if (!hits[i])
                return (false);
        return (true);
    }

    public String toString() {
        String output = (this.name());
        for (int i = 0; i <= 9; i++)
            for (int j = 0; j <= 9; j++) {
                Position pos = new Position(i, j);
                if (onBoat(pos))
                    output += (" " + pos);
            }
        output += ("\n");
        return (output);
    }
}