package com.BattleshipAI;

public class PlayerEvaluator {
    private int avgTurns, maxTurns, minTurns;

    public PlayerEvaluator(BattleshipPlayer player, int runs) {
        avgTurns = 0;
        maxTurns = 0;
        minTurns = 100;
        for (int i = 0; i < runs; i++) {
            BattleshipGame game = new BattleshipGame(player);
            int currentTurns = game.play();
            if (currentTurns < minTurns)
                minTurns = currentTurns;
            if (currentTurns > maxTurns)
                maxTurns = currentTurns;
            avgTurns += currentTurns;
        }
        avgTurns /= runs;
    }

    public int maxTurns() {
        return (maxTurns);
    }

    public int minTurns() {
        return (minTurns);
    }

    public int averageTurns() {
        return (avgTurns);
    }
}
