package unit_4_robotWar_summative;

import java.awt.Color;

import becker.robots.*;

/**
 * Creates a robot that fights other robots.
 * @author Bianca Bian
 * @version Jan 8, 2023
 */

public class Bian_TestFighterRobot3 extends FighterRobot {
	private int health;

	/**
	 * Constructor for fighter robot.
	 * @param c - city the robot is in
	 * @param a - avenue the robot is on
	 * @param s - street the robot is on
	 * @param d - direction the robot is facing
	 * @param id - ID of the robot
	 * @param health - amount of health the robot has
	 */
	public Bian_TestFighterRobot3(City c, int a, int s, Direction d, int id, int health) {
		super(c, a, s, d, id, 1, 1, 8); // City c, int a, int s, Direction d, int id, int attack, int defence, int numMoves
		this.health = health;
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
		health = data[this.getID()].getHealth();

		// variables
		int ave;
		int str = 0;
		int fightID;
		int index = 0;

		TurnRequest turn;

		// find closest lowest health player
		selectSort(data);

		// fighting player is not dead and is not itself
		if (data[index].getHealth() > 0 && data[index].getID() != this.getID()) {
			fightID = data[index].getID();
		}
		// fighting player is dead or itself
		else {
			// go to next id
			while (data[index].getHealth() <= 0 || data[index].getID() == this.getID()) {
				index += 1;
			}
			fightID = data[index].getID();
		}

		// Level 3 - fight player with lowest health and closest
		// has enough steps to go spot
		if (distance(this.getAvenue(), this.getStreet(), data[index].getAvenue(), data[index].getStreet()) < this.getNumMoves()) {
			turn = new TurnRequest(data[index].getAvenue(), data[index].getStreet(), fightID, this.getAttack()); // endAvenue, endStreet, fightID, numRounds
		}
		// can't go all the way to spot
		else {
			// go left
			if (this.getAvenue() > data[index].getAvenue()) {
				// all steps go left
				if (Math.abs(data[index].getAvenue() - this.getAvenue()) >= this.getNumMoves()) {
					ave = this.getAvenue() - this.getNumMoves();
					str = this.getStreet();
				}
				// go left then turn
				else {
					ave = data[index].getAvenue();
					str = this.goStreet(data, fightID, ave);
				}
			}
			// go right
			else if (this.getAvenue() < data[index].getAvenue()){
				// all steps go right
				if (Math.abs(data[index].getAvenue() - this.getAvenue()) >= this.getNumMoves()) {
					ave = this.getAvenue() + this.getNumMoves();
					str = this.getStreet();
				}
				// go right then turn
				else {
					ave = data[index].getAvenue();
					str = this.goStreet(data, fightID, ave);
				}
			}
			// on ave
			else {
				ave = this.getAvenue();
				str = this.goStreet(data, fightID, ave);
			}

			turn = new TurnRequest(ave, str, -1, 0); // endAvenue, endStreet, fightID, numRounds
		}

		return turn;
	}

	// level 4 to use information
	public void battleResult(int healthLost, int oppID, int oppHealthLost, int numRoundsFought) {
		health -= healthLost;

	}

	/**
	 * Finds the street the robot needs to go to.
	 * @param data - list of opponent data
	 * @param index - place of opponent the robot is fighting
	 * @param ave - avenue the robot needs to go
	 * @param str - street the robot is on
	 * @return street the robot needs to go to.
	 */
	private int goStreet(OppData [] data, int index, int ave) {
		int movesLeft = this.getNumMoves() - Math.abs(this.getAvenue() - ave);
		int str;

		// go up
		if (this.getStreet() > data[index].getStreet()) {
			// go partway
			if (Math.abs(data[index].getStreet() - this.getStreet()) > movesLeft) {
				str = this.getStreet() - (this.getNumMoves() - Math.abs(this.getAvenue() - ave));
			}
			// go full way
			else {
				str = data[index].getStreet();
			}
		}
		// go down
		else if (this.getStreet() < data[index].getStreet()) {
			// go partway
			if (Math.abs(data[index].getStreet() - this.getStreet()) > movesLeft) {
				str = this.getStreet() + (this.getNumMoves() - Math.abs(this.getAvenue() - ave));
			}
			// go full way
			else {
				str = data[index].getStreet();
			}
		}
		// on street
		else {
			str = this.getStreet();
		}

		return str;
	}

	/**
	 * Performs a selection sort on an array.
	 * @param list - array of values to sort
	 * @return sorted array
	 */
	private OppData [] selectSort(OppData [] list) {
		// variables
		double disWeight = 0.5;
		double healWeight = 0.5;

		// go through list
		for (int i = 0; i < list.length; i++) {
			// compare with all other values
			for (int j = i; j < list.length; j++) {
				// sort by health and distance
				int distance1 = distance(this.getAvenue(), this.getStreet(), list[j].getAvenue(), list[j].getStreet());
				int distance2 = distance(this.getAvenue(), this.getStreet(), list[i].getAvenue(), list[i].getStreet());
				int num1 = (int) (distance1 * disWeight + list[j].getHealth() * healWeight);
				int num2 = (int) (distance2 * disWeight + list[i].getHealth() * healWeight);

				// distance and health at j is lower than distance and health at i, swap
				if (num1 < num2) {
					OppData temp = list[i];
					list[i] = list[j];
					list[j] = temp;
				}
			}
		}
		return list;
	}

	/**
	 * Calculates the amount of steps to get to the other coordinate.
	 * @param a1 - avenue the robot is on.
	 * @param s1 - street the robot is on.
	 * @param a2 - avenue the robot needs to go to.
	 * @param s2 - street the robot needs to go to.
	 * @return amount of steps to get to the other coordinate.
	 */
	private int distance(int a1, int s1, int a2, int s2) {
		return Math.abs(a2 - a1) + Math.abs(s2 - s1);
	}

	// submit 1 battle manager with commented out different test cases with labels

}
