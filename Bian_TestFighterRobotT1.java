package unit_4_robotWar_summative;

import java.awt.Color;

import becker.robots.*;

/**
 * Creates a robot that fights other robots.
 * @author Bianca Bian
 * @version Jan 8, 2023
 */

public class Bian_TestFighterRobotT1 extends FighterRobot {
	private int health = 100;
	
	/**
	 * Constructor for fighter robot.
	 * @param c - city the robot is in
	 * @param a - avenue the robot is on
	 * @param s - street the robot is on
	 * @param d - direction the robot is facing
	 * @param id - ID of the robot
	 * @param health - amount of health the robot has
	 */
	public Bian_TestFighterRobotT1(City c, int a, int s, Direction d, int id, int health) {
		super(c, a, s, d, id, 1, 1, 8); // City c, int a, int s, Direction d, int id, int attack, int defence, int numMoves
		this.setLabel();
	}

	/**
	 * Override to print out health and change colour when dead.
	 */
	public void setLabel() {
		this.setColor(new Color(66, 176, 245));
		
		// robot is dead
		if (health <= 0) {
			this.setColor(Color.WHITE);
		}

		this.setLabel("ID: " + this.getID() + "H: " + health);
	}

	/**
	 * Makes the robot go to the given location.
	 * @param a - avenue the robot needs to go to.
	 * @param s - street the robot needs to go to.
	 */
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

	/**
	 * Tells the robot what to do on its turn.
	 * @param energy - amount of energy the robot has.
	 * @param data - data about the opponents health and location.
	 * @return where the robot will go and who it will fight.
	 */
	public TurnRequest takeTurn(int energy, OppData[] data) {
		TurnRequest turn = new TurnRequest(this.getAvenue(), this.getStreet(), -1, 0); // endAvenue, endStreet, fightID, numRounds
		return turn;
	}

	/**
	 * Receive information from the battle to track the opponent health lost from battle.
	 * @param healthLost - health lost from the battle.
	 * @param oppID - ID of the opponent
	 * @param oppHealthLost - health lost by the opponent from the battle.
	 * @param numRoundsFought - number of rounds fought.
	 */
	public void battleResult(int healthLost, int oppID, int oppHealthLost, int numRoundsFought) {
		health -= healthLost;
	}

}
