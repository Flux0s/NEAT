package com.BattleshipAI;

class BattleshipGame {
    private Ocean water;
    private BattleshipPlayer player1;

    public BattleshipGame(BattleshipPlayer player) {
        water = new Ocean();
        water.placeAllBoats();
        player1 = player;
    }

    public int play() {
        int turn = 1;
        player1.startGame();
        while (turn <= 100 && !water.allSunk()) {
            Position pos = player1.shoot();
            water.shootAt(pos);
            if (pos.getHit())
                player1.updatePlayer(pos, water.hit(pos), water.boatName(pos).charAt(0), water.boatName(pos), water.sunk(pos), water.allSunk(), false, turn);
            else
                player1.updatePlayer(pos, water.hit(pos), 'M', "Miss", water.sunk(pos), water.allSunk(), false, turn);
            turn++;
        }
        if (turn == 101)
            turn--;
        return (turn);
    }

}
