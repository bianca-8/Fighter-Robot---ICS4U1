package unit_4_robotWar_summative;

import java.awt.Color;

import becker.robots.*;

/**
 * Creates a robot that fights other robots.
 * @author Bianca Bian
 * @version Jan 8, 2023
 */

public class Bian_TestFighterRobot1 extends FighterRobot {
	private int health = 100;

	public Bian_TestFighterRobot1(City c, int a, int s, Direction d, int id, int health) {
		super(c, a, s, d, id, 1, 1, 8); // City c, int a, int s, Direction d, int id, int attack, int defence, int numMoves
		this.setLabel();
	}

	/**
	 * Override to print out health and change colour when dead.
	 */
	public void setLabel() {
		// robot is dead
		if (health <= 0) {
			this.setColor(Color.WHITE);
		}
		// robot is not dead
		else {
			this.setColor(new Color(66, 176, 245));
		}

		this.setLabel("ID: " + this.getID() + "H: " + health);
	}

	public void goToLocation(int a, int s) {
		// below street
		if (this.getStreet() > s) {
			// face north
			while (this.getDirection() != Direction.NORTH) {
				this.turnRight();
			}
			// go to street and not at border
			while (this.getStreet() != s && this.getStreet() - 1 >= 0) {
				this.move();
			}
			// on right of avenue
			if (this.getAvenue() > a) {
				this.turnLeft();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() - 1 >= 0) {
					this.move();
				}
			}
			// on left of avenue
			else if (this.getAvenue() < a){
				this.turnRight();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 <= 19) {
					this.move();
				}
			}
			// on avenue
			else {
				// on avenue
			}
		}
		// above street
		else if (this.getStreet() < s) {
			// face south
			while (this.getDirection() != Direction.SOUTH) {
				this.turnLeft();
			}
			// go to street and not at border
			while (this.getStreet() != s && this.getStreet() + 1 <= 11) {
				this.move();
			}
			// on right of avenue
			if (this.getAvenue() < a) {
				this.turnLeft();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 <= 19) {
					this.move();
				}
			}
			// on left of avenue
			else if (this.getAvenue() > a){
				this.turnRight();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() - 1 >= 0) {
					this.move();
				}
			}
			// on avenue
			else {
				// on avenue
			}
		}
		// at street
		else {
			// on right of avenue
			if (this.getAvenue() > a) {
				// turn to facing left
				while (this.getDirection() != Direction.WEST) {
					this.turnLeft();
				}
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 >= 0) {
					this.move();
				}
			}
			// on left of avenue
			else if (this.getAvenue() < a){
				// turn to facing right
				while (this.getDirection() != Direction.EAST) {
					this.turnRight();
				}
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 <= 19) {
					this.move();
				}
			}
			// on street
			else {
				// on street
			}
		}
	}

	// search opp data to find out how you should move, extend Oppdata to hold extra information
	public TurnRequest takeTurn(int energy, OppData[] data) {
		// variables
		int ave;
		int str = 0;
		int fightID = 1;

		TurnRequest turn;

		// fight player 1
		// has enough steps to go spot
		if (Math.abs(data[fightID].getAvenue() - this.getAvenue()) + Math.abs(data[fightID].getStreet() - this.getStreet()) < this.getNumMoves()) {
			// not the robot trying to fight
			if (this.getID() != fightID) {
				turn = new TurnRequest(data[fightID].getAvenue(), data[fightID].getStreet(), fightID, this.getAttack()); // endAvenue, endStreet, fightID, numRounds
			}
			// is the robot trying to fight -- can't fight itself
			else {
				turn = new TurnRequest(this.getAvenue(), this.getStreet(), -1, 0); // endAvenue, endStreet, fightID, numRounds
			}
		}
		// can't go all the way to spot
		else {
			// go left
			if (this.getAvenue() > data[fightID].getAvenue()) {
				// all steps go left
				if (Math.abs(data[fightID].getAvenue() - this.getAvenue()) >= this.getNumMoves() && data[fightID].getAvenue() >= 0) {
					ave = this.getAvenue() - this.getNumMoves();
					str = this.getStreet();
				}
				// go left then turn
				else {
					ave = data[fightID].getAvenue();
					str = this.goStreet(data, fightID, ave, str);
				}
			}
			// go right
			else if (this.getAvenue() < data[fightID].getAvenue()){
				// all steps go right
				if (Math.abs(data[fightID].getAvenue() - this.getAvenue()) >= this.getNumMoves()) {
					ave = this.getAvenue() + this.getNumMoves();
					str = this.getStreet();
				}
				// go right then turn
				else {
					ave = data[fightID].getAvenue();
					str = this.goStreet(data, fightID, ave, str);
				}
			}
			// on ave
			else {
				ave = this.getAvenue();
				str = this.goStreet(data, fightID, ave, str);
			}

			// fightID robot not dead
			if (data[fightID].getAvenue() >= 0) {
				turn = new TurnRequest(ave, str, -1, 0); // endAvenue, endStreet, fightID, numRounds
			}
			// fightID robot is dead
			else {
				turn = new TurnRequest(this.getAvenue(), this.getStreet(), -1, 0); // endAvenue, endStreet, fightID, numRounds
			}
		}

		return turn;
	}

	public void battleResult(int healthLost, int oppID, int oppHealthLost, int numRoundsFought) {
		health -= healthLost;
	}

	private int goStreet(OppData [] data, int fightID, int ave, int str) {
		// go up
		if (this.getStreet() > data[fightID].getStreet()) {
			// go partway
			if (Math.abs(data[fightID].getStreet() - this.getStreet()) > this.getNumMoves() - Math.abs(this.getAvenue() - ave)) {
				str = this.getStreet() - (this.getNumMoves() - Math.abs(this.getAvenue() - ave));
			}
			// go full way
			else {
				str = data[fightID].getStreet();
			}
		}
		// go down
		else if (this.getStreet() < data[fightID].getStreet()) {
			// go partway
			if (Math.abs(data[fightID].getStreet() - this.getStreet()) > this.getNumMoves() - Math.abs(this.getAvenue() - ave)) {
				str = this.getStreet() + (this.getNumMoves() - Math.abs(this.getAvenue() - ave));
			}
			// go full way
			else {
				str = data[fightID].getStreet();
			}
		}
		// on street
		else {
			str = this.getStreet();
		}
		
		return str;
	}
}
