package com.BattleshipAI;

import java.util.ArrayList;

class JasonOwensStrategy extends ComputerBattleshipPlayer {
    private ArrayList<Integer> boatCount;
    private ArrayList<Integer> boatMax;

    public JasonOwensStrategy() {
        super("Jason Owens");
        boatCount = new ArrayList<Integer>();
        for (int i = 0; i <= 'S'; i++)
            boatCount.add(0);
        boatMax = new ArrayList<Integer>();
        for (int i = 0; i <= 'Z'; i++)
            boatMax.add(0);
        boatMax.set('A', 5);
        boatMax.set('B', 4);
        boatMax.set('C', 3);
        boatMax.set('S', 3);
        boatMax.set('D', 2);
        boatMax.set('M', 100);
        boatMax.set('E', 100);

    }

    public Position shoot() {
        countBoats();
        Position pos = null;
        Position found = foundBoat();
        Position next = nextTo();
        if (next != null)
            return (next);
        else if (found != null)
            return (found);
        else {
            int col = (int) (Math.random() * 10);
            int row = (int) (Math.random() * 10);
            pos = new Position(row, col);
            while (!peggs.empty(pos)) {
                col = (int) (Math.random() * 10);
                row = (int) (Math.random() * 10);
                pos = new Position(row, col);
            }
        }
        return (pos);
    }

    private Position nextTo() {
        Position out, pos, check;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                out = new Position(i, j);
                if (peggs.hit(out))
                    for (int x = -1; x <= 1; x += 2) {
                        pos = new Position(out.rowIndex() + x, out.columnIndex());
                        check = new Position(out.rowIndex() - x, out.columnIndex());
                        if (pos.rowIndex() >= 0 && pos.rowIndex() <= 9 && pos.columnIndex() >= 0 && pos.columnIndex() <= 9 && check.rowIndex() >= 0 && check.rowIndex() <= 9 && check.columnIndex() >= 0 && check.columnIndex() <= 9)
                            if (peggs.empty(pos) && peggs.boatInitial(check) == peggs.boatInitial(out) && boatCount.get(peggs.boatInitial(out)) < boatMax.get(peggs.boatInitial(out)))
                                return (pos);
                        pos = new Position(out.rowIndex(), out.columnIndex() + x);
                        check = new Position(out.rowIndex(), out.columnIndex() - x);
                        if (pos.rowIndex() >= 0 && pos.rowIndex() <= 9 && pos.columnIndex() >= 0 && pos.columnIndex() <= 9 && check.rowIndex() >= 0 && check.rowIndex() <= 9 && check.columnIndex() >= 0 && check.columnIndex() <= 9)
                            if (peggs.empty(pos) && peggs.boatInitial(check) == peggs.boatInitial(out) && boatCount.get(peggs.boatInitial(out)) < boatMax.get(peggs.boatInitial(out)))
                                return (pos);
                    }
            }
        }
        return (null);
    }

    private Position foundBoat() {
        Position out, pos;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                out = new Position(i, j);
                if (peggs.hit(out))
                    for (int x = -1; x <= 1; x += 2) {
                        pos = new Position(out.rowIndex() + x, out.columnIndex());
                        if (pos.rowIndex() >= 0 && pos.rowIndex() <= 9 && pos.columnIndex() >= 0 && pos.columnIndex() <= 9)
                            if (peggs.empty(pos) && boatCount.get(peggs.boatInitial(out)) < boatMax.get(peggs.boatInitial(out)))
                                return (pos);
                        pos = new Position(out.rowIndex(), out.columnIndex() + x);
                        if (pos.rowIndex() >= 0 && pos.rowIndex() <= 9 && pos.columnIndex() >= 0 && pos.columnIndex() <= 9)
                            if (peggs.empty(pos) && boatCount.get(peggs.boatInitial(out)) < boatMax.get(peggs.boatInitial(out)))
                                return (pos);
                    }
            }
        }
        return (null);
    }

    private void countBoats() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                Position pos = new Position(i, j);
                boatCount.set(peggs.boatInitial(pos), 0);
            }
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                Position pos = new Position(i, j);
                boatCount.set(peggs.boatInitial(pos), 1 + boatCount.get(peggs.boatInitial(pos)));
            }
    }
}
