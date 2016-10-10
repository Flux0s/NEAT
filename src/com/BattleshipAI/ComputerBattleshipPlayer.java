package com.BattleshipAI;

public class ComputerBattleshipPlayer extends BattleshipPlayer {

    public ComputerBattleshipPlayer() {
        super("A.I. Player");
    }

    public ComputerBattleshipPlayer(String nameIn) {
        super(nameIn);
    }

    public void startGame() {
        peggs = new BattleshipGrid();
    }

    public void updatePlayer(Position pos, boolean hit, char initial, String boatName, boolean sunk, boolean gameOver, boolean tooManyTurns, int turns) {
        updateGrid(pos, hit, initial);
    }

    protected void updateGrid(Position pos, boolean hit, char initial) {
        peggs.shotAt(pos, hit, initial);
    }

    public Position shoot() {
        int col = (int) (Math.random() * 10);
        int row = (int) (Math.random() * 10);
        Position pos = new Position(row, col);
        while (!peggs.empty(pos)) {
            col = (int) (Math.random() * 10);
            row = (int) (Math.random() * 10);
            pos = new Position(row, col);
        }
        return (pos);
    }
}
