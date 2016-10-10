package com.BattleshipAI;

import java.util.Scanner;

class Battleship {
    public static void main(String[] args) {
        int turns;
        Scanner Strings = new Scanner(System.in);
        System.out.print("Enter the number of runs: ");
        turns = (int) dubz(Strings.nextLine());
        JasonOwensStrategy player = new JasonOwensStrategy();
        PlayerEvaluator evaluate = new PlayerEvaluator(player, turns);
        System.out.println("\nMax Turns: " + evaluate.maxTurns() + "\nMin Turns: " + evaluate.minTurns() + "\nAverage Turns: " + evaluate.averageTurns());
    }

    private static double dubz(String input) {
        int indexOfI = 0, minIndex = input.length() + 1, maxIndex = 0;
        for (int i = 0; i <= 9; i++) {
            indexOfI = input.indexOf(Integer.toString(i));
            if (indexOfI < minIndex && indexOfI != -1)
                minIndex = indexOfI;
        }
        maxIndex = minIndex;
        for (int j = minIndex + 1; j < input.length(); j++) {
            indexOfI = maxIndex;
            for (int i = 0; i <= 9; i++) {
                if (j > maxIndex && (input.substring(j, j + 1).equals(Integer.toString(i)) || input.substring(j, j + 1).equals("."))) {
                    maxIndex = j;
                    i = 20;
                }
            }
            if (maxIndex == indexOfI)
                j = input.length() - 1;
        }
        if (minIndex == input.length() + 1)
            return -1;
        else {
            if (minIndex != 0 && input.substring(minIndex - 1, minIndex).equals("."))
                minIndex -= 1;
            if (minIndex != 0 && input.substring(minIndex - 1, minIndex).equals("-"))
                minIndex -= 1;
            input = input.substring(minIndex, maxIndex + 1);
            return Double.parseDouble(input);
        }
    }
}
