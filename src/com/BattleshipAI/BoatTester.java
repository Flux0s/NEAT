package com.BattleshipAI;

class BoatTester {
	public static void main(String args[]) {
		Position boatSpot = new Position('b', 4);
		Boat testBoat = new Boat("Destroyer", "Horizontal", boatSpot);

		System.out.println("Name: " + testBoat.name());
		System.out.println("Position: " + testBoat.position());
		System.out.println("Direction: " + testBoat.direction());
		System.out.println("Abbreviation: " + testBoat.abbreviation());
		System.out.println("Size: " + testBoat.size());

		System.out.print("The sunk method works! " + !testBoat.sunk());
	}
}