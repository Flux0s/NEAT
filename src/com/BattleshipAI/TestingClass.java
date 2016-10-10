package com.BattleshipAI;

class TestingClass {
	public static void main(String[] args) {
		Position spot1 = new Position('c', 4);
		Position spot2 = new Position(3, 4);

		System.out.println("The position given is located at: " + spot1 + "\nIndexed at: (" + spot1.rowIndex() + ", " + spot1.columnIndex() + ")");
		System.out.println("The position given is located at: " + spot2 + "\nIndexed at: (" + spot2.rowIndex() + ", " + spot2.columnIndex() + ")");

		Boat testBoat = new Boat("Aircraft Carrier", "Horizontal", spot1);
		System.out.println(testBoat);

		Ocean no = new Ocean();
		no.placeAllBoats();
		System.out.println(no);
	}
}