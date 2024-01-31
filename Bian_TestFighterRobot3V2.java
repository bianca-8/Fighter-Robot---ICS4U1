package unit_4_robotWar_summative;

import java.awt.Color;
import becker.robots.*;

/**
 * Creates a robot that fights other robots.
 * @author Bianca Bian
 * @version Jan 8, 2023
 */

public class Bian_TestFighterRobot3V2 extends FighterRobot {
	private int health;
	private int round;
	private final int STRMIN = 0;
	private final int STRMAX = 11;
	private final int AVEMIN = 0;
	private final int AVEMAX = 19;
	final int ROUNDPENALTY = 5; // rounds until a penalty is given

	/**
	 * Constructor for fighter robot.
	 * @param c - city the robot is in
	 * @param a - avenue the robot is on
	 * @param s - street the robot is on
	 * @param d - direction the robot is facing
	 * @param id - ID of the robot
	 * @param health - amount of health the robot has
	 */
	public Bian_TestFighterRobot3V2(City c, int a, int s, Direction d, int id, int health) {
		super(c, a, s, d, id, 2, 2, 6); // City c, int a, int s, Direction d, int id, int attack, int defence, int numMoves
		this.health = health;
		this.setLabel();
	}

	/**
	 * Override to print out health and change colour when dead.
	 */
	public void setLabel() {
		this.setColor(new Color(66, 176, 245));

		// robot is dead
		if (this.health <= 0) {
			this.setColor(Color.WHITE); // CHANGE TO BLACKKKKKKKKKKKKKKKKKKKKKKK
		}

		this.setLabel("ID: " + this.getID() + "H: " + this.health);
	}

	/**
	 * makes the robot go to the given location.
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
			while (this.getStreet() != s && this.getStreet() - 1 >= this.STRMIN) {
				this.move();
			}
			// on right of avenue
			if (this.getAvenue() > a) {
				this.turnLeft();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() - 1 >= this.AVEMIN) {
					this.move();
				}
			}
			// on left of avenue
			else if (this.getAvenue() < a){
				this.turnRight();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 <= this.AVEMAX) {
					this.move();
				}
			}
		}
		// above street
		else if (this.getStreet() < s) {
			// face south
			while (this.getDirection() != Direction.SOUTH) {
				this.turnLeft();
			}
			// go to street and not at border
			while (this.getStreet() != s && this.getStreet() + 1 <= this.STRMAX) {
				this.move();
			}
			// on right of avenue
			if (this.getAvenue() < a) {
				this.turnLeft();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 <= this.AVEMAX) {
					this.move();
				}
			}
			// on left of avenue
			else if (this.getAvenue() > a){
				this.turnRight();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() - 1 >= this.AVEMIN) {
					this.move();
				}
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
				while (this.getAvenue() != a && this.getAvenue() + 1 >= this.AVEMIN) {
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
				while (this.getAvenue() != a && this.getAvenue() + 1 <= this.AVEMAX) {
					this.move();
				}
			}
		}
	}

	// search opp data to find out how you should move, extend Oppdata to hold extra information
	/**
	 * Tells the robot what to do on its turn.
	 * @param energy - amount of energy the robot has.
	 * @param data - data about the opponents health and location.
	 * @return where the robot will go and who it will fight.
	 */
	public TurnRequest takeTurn(int energy, OppData[] data) {
		// variables
		int ave = 0;
		int str = 0;
		int fightID;
		int index = 0;
		final int FIGHTHEALTH = 10; // doesn't fight when below this number

		TurnRequest turn;

		// sorting lists 
		// find closest lowest health player
		selectSort(data);

		// fighting player is not dead and is not itself
		if (data[index].getHealth() > 0 && data[index].getID() != this.getID()) {
			fightID = data[index].getID();
		}
		// fighting player is dead or itself
		else {
			int disPrev;
			int disAft;
			int prevAve;
			int nextAve;
			int totalDis = distance(this.getAvenue(), this.getStreet(), data[index].getAvenue(), data[index].getStreet());

			// at first index, no prev distance and avenue
			if (index == 0) {
				disPrev = -1;
				prevAve = -1;
			}
			// get distance to robot in previous index and avenue it's on
			else {
				disPrev = distance(this.getAvenue(), this.getStreet(), data[index-1].getAvenue(), data[index-1].getStreet());
				prevAve = data[index-1].getAvenue();
			}
			// at last index, no next distance and avenue
			if (index == BianBattleManager.NUM_PLAYERS) {
				disAft = -1;
				nextAve = -1;
			}
			// get distance to robot in next index and avenue it's on
			else {
				disAft = distance(this.getAvenue(), this.getStreet(), data[index+1].getAvenue(), data[index+1].getStreet());
				nextAve = data[index+1].getAvenue();
			}

			// go to next id -- player is dead, is itself or has 2 people already at spot -- 2 PEOPLE AT SPOT DOENST WORK, dead doesn't work
			while (index < BianBattleManager.NUM_PLAYERS - 1 && (data[index].getHealth() == 0 || data[index].getID() == this.getID() || (totalDis == disPrev && data[index].getAvenue() == prevAve) || (totalDis == disAft && data[index].getAvenue() == nextAve))) {
				index += 1;
			}
			// everyone else is dead
			if (index == BianBattleManager.NUM_PLAYERS - 1 && data[index].getHealth() == 0 && this.getID() != data[index].getID()) {
				index = -1;
			}

			if (index != -1) {
				fightID = data[index].getID();
			}
			else {
				fightID = -1;
			}
		}

		int dis = -1;
		int aveDis = -1;
		int aveEnergy = -1;

		// fighting actual player and not dead player
		if (index != -1) {
			dis = distance(this.getAvenue(), this.getStreet(), data[index].getAvenue(), data[index].getStreet());
			aveDis = Math.abs(data[index].getAvenue() - this.getAvenue());
			aveEnergy = aveDis * BianBattleManager.MOVES_ENERGY_COST;
		}


		// Level 4 - fight player with lowest health and closest if above FIGHTHEALTH health -- MAKE SO ONLY FIGHT IF >= 2 ENERGY
		// has more than FIGHTHEALTH amount of health, at 5 rounds or can't reach a player in 5 rounds and there are still people alive
		if (fightID != -1 && (this.round == ROUNDPENALTY || Math.round(dis / 5.0) <= ROUNDPENALTY - this.round)) {  //this.health >= FIGHTHEALTH ||  -- CAN ADD IN (WAS OR)
			// has enough steps to go spot
			if (dis <= this.getNumMoves() && BianBattleManager.MOVES_ENERGY_COST * dis < energy) {
				turn = new TurnRequest(data[index].getAvenue(), data[index].getStreet(), fightID, this.getAttack()); // endAvenue, endStreet, fightID, numRounds
			}
			// can't go all the way to spot
			else {
				// go left
				if (this.getAvenue() > data[index].getAvenue()) {
					// all steps go left
					if (aveDis >= this.getNumMoves()) {
						ave = this.getAvenue() - this.getNumMoves();
						// allows to subtract up until this.getAvenue()
						for (int i = 0; i < this.getNumMoves(); i++) {
							// move to place where it has enough energy to go to
							if (aveEnergy > energy && ave < this.AVEMAX) {
								// can't move
								if (aveEnergy - 5 < 0) {
									ave = this.getAvenue();
								}
								// can move
								else {
									ave += 1;
									aveEnergy -= 5;
								}
							}
						}
						str = this.getStreet();
					}
					// go left then turn
					else {
						ave = data[index].getAvenue();
						// allows to subtract up until this.getAvenue()
						for (int i = 0; i < this.getNumMoves(); i++) {
							// move to place where it has enough energy to go to
							if (aveEnergy > energy && ave < this.AVEMAX) {
								// can't move
								if (aveEnergy - 5 < 0) {
									ave = this.getAvenue();
								}
								// can move
								else {
									ave += 1;
									aveEnergy -= 5;
								}
							}
						}
						energy -= aveEnergy;
						str = this.goStreet(data, index, ave, energy);
					}
				}
				// go right
				else if (this.getAvenue() < data[index].getAvenue()){
					// all steps go right
					if (aveDis >= this.getNumMoves()) {
						ave = this.getAvenue() + this.getNumMoves();
						// allows to subtract up until this.getAvenue()
						for (int i = 0; i < this.getNumMoves(); i++) {
							// move to place where it has enough energy to go to
							if (aveEnergy > energy && ave > this.AVEMIN) {
								// can't move
								if (aveEnergy - 5 < 0) {
									ave = this.getAvenue();
								}
								// can move
								else {
									ave -= 1;
									aveEnergy -= 5;
								}
							}
						}
						str = this.getStreet();
					}
					// go right then turn
					else {
						ave = data[index].getAvenue();
						// allows to subtract up until this.getAvenue()
						for (int i = 0; i < this.getNumMoves(); i++) {
							// move to place where it has enough energy to go to
							if (aveEnergy > energy && ave > this.AVEMIN) {
								// can't move
								if (aveEnergy - 5 < 0) {
									ave = this.getAvenue();
								}
								// can move
								else {
									ave -= 1;
									aveEnergy -= 5;
								}
							}
						}
						energy -= aveEnergy;
						str = this.goStreet(data, index, ave, energy);
					}
				}
				// on ave
				else {
					ave = this.getAvenue();
					str = this.goStreet(data, index, ave, energy);
				}

				turn = new TurnRequest(ave, str, -1, 0); // endAvenue, endStreet, fightID, numRounds
			}
		}
		// lower than fightHealth amount of health -- run away after a fight if lose, run away if below 10 health, go where less than 2 people at that spot, fight if has highwer helath than opp, fight same operson after figthing a round instead of finding someoen else
		else {
			ave = this.getAvenue();
			str = this.getStreet();
			turn = new TurnRequest(ave, str, -1, 0); // endAvenue, endStreet, fightID, numRounds
		}

		this.round += 1;

		// reset round count to 0
		if (this.round == ROUNDPENALTY + 1) {
			this.round = 0;
		}

		return turn;
	}

	// level 4 to use information - track their energy, attack or defense number
	/**
	 * Receive information from the battle.
	 * @param healthLost - health lost from the battle.
	 * @param oppID - ID of the opponent
	 * @param oppHealthLost - health lost by the opponent from the battle.
	 * @param numRoundsFought - number of rounds fought.
	 */
	public void battleResult(int healthLost, int oppID, int oppHealthLost, int numRoundsFought) {
		this.health -= healthLost;
		if (numRoundsFought > 0) {

		}
		else {

		}
	}

	/**
	 * Finds the street the robot needs to go to.
	 * @param data - list of opponent data
	 * @param index - place of opponent the robot is fighting
	 * @param ave - avenue the robot needs to go
	 * @param str - street the robot is on
	 * @param energy - amount of energy the robot has
	 * @return street the robot needs to go to.
	 */
	private int goStreet(OppData [] data, int index, int ave, int energy) {
		int numMoves = this.getNumMoves() - Math.abs(this.getAvenue() - ave);
		int disStr = Math.abs(data[index].getStreet() - this.getStreet());
		int str;
		int strEnergy = disStr * BianBattleManager.MOVES_ENERGY_COST;
		
		// go up
		if (this.getStreet() > data[index].getStreet()) {
			// go partway
			if (disStr > numMoves) {
				str = this.getStreet() - numMoves;
				// allows to subtract up until this.getStreet()
				for (int i = 0; i < numMoves; i++) {
					// move to place where it has enough energy to go to
					if (strEnergy > energy && str < this.STRMAX) {
						// can't move
						if (strEnergy - 5 < 0) {
							str = this.getStreet();
						}
						// can move
						else {
							str += 1;
							strEnergy -= 5;
						}
					}
				}
			}
			// go full way
			else {
				str = data[index].getStreet();
				// allows to subtract up until this.getStreet()
				for (int i = 0; i < this.getNumMoves(); i++) {
					// move to place where it has enough energy to go to
					if (strEnergy > energy && str < this.STRMAX) {
						// can't move
						if (strEnergy - 5 < 0) {
							str = this.getStreet();
						}
						// can move
						else {
							str += 1;
							strEnergy -= 5;
						}
					}
				}
			}
		}
		// go down
		else if (this.getStreet() < data[index].getStreet()) {
			// go partway
			if (disStr > numMoves) {
				str = this.getStreet() + numMoves;
				// allows to subtract up until this.getStreet()
				for (int i = 0; i < numMoves; i++) {
					// move to place where it has enough energy to go to
					if (strEnergy > energy && str > this.STRMIN) {
						// can't move
						if (strEnergy - 5 < 0) {
							str = this.getStreet();
						}
						// can move
						else {
							str -= 1;
							strEnergy -= 5;
						}
					}
				}
			}
			// go full way
			else {
				str = data[index].getStreet();
				// allows to subtract up until this.getStreet()
				for (int i = 0; i < this.getNumMoves(); i++) {
					// move to place where it has enough energy to go to
					if (strEnergy > energy && str > this.STRMIN) {
						// can't move
						if (strEnergy - 5 < 0) {
							str = this.getStreet();
						}
						// can move
						else {
							str -= 1;
							strEnergy -= 5;
						}
					}
				}
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
		// go through list
		for (int i = 0; i < list.length; i++) {
			// compare with all other values
			for (int j = i; j < list.length; j++) {
				int distance1 = distance(this.getAvenue(), this.getStreet(), list[j].getAvenue(), list[j].getStreet());
				int distance2 = distance(this.getAvenue(), this.getStreet(), list[i].getAvenue(), list[i].getStreet());
				int num1 = (int) (distance1 * 0.5 + list[j].getHealth() * 0.5);
				int num2 = (int) (distance2 * 0.5 + list[i].getHealth() * 0.5);

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
