package com.BattleshipAI;

import java.util.Scanner;

public class BattleshipPlayer {
    private String name;
    private Scanner strings = new Scanner(System.in);
    protected BattleshipGrid peggs;

    public BattleshipPlayer(String nameIn) {
        name = nameIn;
    }

    public void startGame() {
        peggs = new BattleshipGrid();
        if (name == null) {
            System.out.print("What is your name: ");
            name = strings.nextLine();
        }
    }

    public String playerName() {
        return (name);
    }

    public Position shoot() {
        String input;
        Position output;
        int row, col;
        System.out.print("Enter a position to shoot at: ");
        input = strings.nextLine();
        row = input.charAt(0);
        col = Integer.parseInt(input.substring(1, 1));
        while (row < 0 || row > 10 || col < 'a' || col > 'j') {
            System.out.print("Try again/nEnter a position to shoot at: ");
            input = strings.nextLine();
            row = input.charAt(0);
            col = Integer.parseInt(input.substring(1, 1));
        }
        output = new Position(row, col);
        return (output);
    }

    void updateGrid(Position pos, boolean hit, char initial) {
        peggs.shotAt(pos, hit, initial);
    }

    public void updatePlayer(Position pos, boolean hit, char initial, String boatName, boolean sunk, boolean gameOver, boolean tooManyTurns, int turns) {
        updateGrid(pos, hit, initial);
        if (hit)
            System.out.println("The " + boatName + " was hit.");
        if (sunk)
            System.out.println("It was sunk!");
        if (gameOver)
            System.out.println("All boats have been sunk!");
        if (tooManyTurns)
            System.out.println("the turn limit has been reached!");
        System.out.println("You have had " + turns + " turns.");
        System.out.println(peggs);
    }
}
