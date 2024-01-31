package unit_4_robotWar_summative;

import java.awt.Color;
import becker.robots.*;

/**
 * Creates a robot that fights other robots.
 * @author Bianca Bian
 * @version Jan 8, 2023
 */

public class Bian_TestFighterRobot8V20 extends FighterRobot {
	private int health;
	private int round;
	private final int STRMIN = 0;
	private final int AVEMIN = 0;
	private final int ROUNDPENALTY = 5; // rounds until a penalty is given
	private BianOppData [] mydata = new BianOppData[BianBattleManager.NUM_PLAYERS];

	/**
	 * Constructor for fighter robot.
	 * @param c - city the robot is in
	 * @param a - avenue the robot is on
	 * @param s - street the robot is on
	 * @param d - direction the robot is facing
	 * @param id - ID of the robot
	 * @param health - amount of health the robot has
	 */
	public Bian_TestFighterRobot8V20(City c, int a, int s, Direction d, int id, int health) {
		super(c, a, s, d, id, 2, 5, 3); // City c, int a, int s, Direction d, int id, int attack, int defence, int numMoves
		this.health = health;
		this.setLabel();
	}

	/**
	 * Override to label with health and change colour when dead.
	 */
	public void setLabel() {
		this.setColor(new Color(66, 176, 245));

		// robot is dead
		if (this.health <= 0) {
			this.setColor(Color.BLACK);
		}

		this.setLabel("ID: " + this.getID() + "H: " + this.health);
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
				while (this.getAvenue() != a && this.getAvenue() + 1 <= BianBattleManager.WIDTH - 1) {
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
			while (this.getStreet() != s && this.getStreet() + 1 <= BianBattleManager.HEIGHT - 1) {
				this.move();
			}
			// on right of avenue
			if (this.getAvenue() < a) {
				this.turnLeft();
				// move to avenue and not at border
				while (this.getAvenue() != a && this.getAvenue() + 1 <= BianBattleManager.WIDTH - 1) {
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
				while (this.getAvenue() != a && this.getAvenue() + 1 <= BianBattleManager.WIDTH - 1) {
					this.move();
				}
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
		// variables
		int ave = 0;
		int str = 0;
		int fightID;
		int index = 0;
		final int FIGHTHEALTH = 10; // doesn't fight when below this number
		final int SAVEENERGY = 5; // doesn't move when below this number to save energy unless need to or going to fight
		int oppHealth = 0;
		TurnRequest turn;

		// add values to data array
		for (int i = 0; i < this.mydata.length; i++) {
			this.mydata[i] = new BianOppData(i, data[i].getAvenue(), data[i].getStreet(), data[i].getHealth(), oppHealth);
		}

		// sorting lists 
		// 1 round until penalty
		if (this.round == this.ROUNDPENALTY - 2) {
			disSort(this.mydata); // find only closest player
		}
		// more than 1 round until penalty
		else {
			selectSort(this.mydata); // find closest lowest health player which lost the most battles
		}

		// fighting player is not dead and is not itself
		if (this.mydata[index].getHealth() > 0 && this.mydata[index].getID() != this.getID()) {
			fightID = this.mydata[index].getID();
		}
		// fighting player is dead or itself
		else {
			int disPrev;
			int disAft;
			int prevAve;
			int nextAve;
			int totalDis = distance(this.getAvenue(), this.getStreet(), this.mydata[index].getAvenue(), this.mydata[index].getStreet());

			// at first index, no prev distance and avenue
			if (index == 0) {
				disPrev = -1;
				prevAve = -1;
			}
			// get distance to robot in previous index and avenue it's on
			else {
				disPrev = distance(this.getAvenue(), this.getStreet(), this.mydata[index-1].getAvenue(), this.mydata[index-1].getStreet());
				prevAve = this.mydata[index-1].getAvenue();
			}
			// at last index, no next distance and avenue
			if (index == BianBattleManager.NUM_PLAYERS) {
				disAft = -1;
				nextAve = -1;
			}
			// get distance to robot in next index and avenue it's on
			else {
				disAft = distance(this.getAvenue(), this.getStreet(), this.mydata[index+1].getAvenue(), this.mydata[index+1].getStreet());
				nextAve = this.mydata[index+1].getAvenue();
			}

			// go to next id -- player is dead, is itself or has 2 people already at spot -- 2 PEOPLE AT SPOT DOENST WORK, dead doesn't work
			while (index < BianBattleManager.NUM_PLAYERS - 1 && (this.mydata[index].getHealth() == 0 || this.mydata[index].getID() == this.getID() || (totalDis == disPrev && this.mydata[index].getAvenue() == prevAve) || (totalDis == disAft && this.mydata[index].getAvenue() == nextAve))) {
				index += 1;
			}
			// everyone else is dead
			if (index == BianBattleManager.NUM_PLAYERS - 1 && this.mydata[index].getHealth() == 0 && this.getID() != this.mydata[index].getID()) {
				index = -1;
			}

			// opp exists
			if (index != -1) {
				fightID = this.mydata[index].getID();
			}
			// opp doesn't exist
			else {
				fightID = -1;
			}
		}

		int dis = -1;
		int aveDis = -1;
		int aveEnergy = -1;
		int movesLeft;

		// fighting actual player and not dead player
		if (index != -1) {
			dis = distance(this.getAvenue(), this.getStreet(), this.mydata[index].getAvenue(), this.mydata[index].getStreet());
			aveDis = Math.abs(this.mydata[index].getAvenue() - this.getAvenue());
			aveEnergy = aveDis * BianBattleManager.MOVES_ENERGY_COST;
		}

		// number moves based on energy
		if (dis / this.getNumMoves() > energy / BianBattleManager.MOVES_ENERGY_COST) {
			movesLeft = energy / BianBattleManager.MOVES_ENERGY_COST;
		}
		// number moves based on distance
		else {
			movesLeft = dis / this.getNumMoves();
		}

		// has more than FIGHTHEALTH amount of health, at 5 rounds or can't reach a player in 5 rounds and there are still people alive
		if (fightID != -1 && ((this.round >= this.ROUNDPENALTY - 3 || movesLeft <= this.ROUNDPENALTY - 3) || this.health >= FIGHTHEALTH)) {
			// has enough steps to go spot
			if (dis <= this.getNumMoves() && BianBattleManager.MOVES_ENERGY_COST * dis < energy) {
				turn = new TurnRequest(this.mydata[index].getAvenue(), this.mydata[index].getStreet(), fightID, this.getAttack()); // endAvenue, endStreet, fightID, numRounds
				this.round = 0;
			}
			// can't go all the way to spot
			else {
				// go left
				if (this.getAvenue() > this.mydata[index].getAvenue()) {
					// all steps go left
					if (aveDis >= this.getNumMoves()) {
						ave = this.getAvenue() - this.getNumMoves();
						// allows to subtract up until this.getAvenue()
						for (int i = 0; i < this.getNumMoves(); i++) {
							// move to place where it has enough energy to go to
							if (aveEnergy > energy && ave < BianBattleManager.WIDTH - 1) {
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
							// found place to go
							else {
								break;
							}
						}
						str = this.getStreet();
					}
					// go left then turn
					else {
						ave = this.mydata[index].getAvenue();
						// allows to subtract up until this.getAvenue()
						for (int i = 0; i < this.getNumMoves(); i++) {
							// move to place where it has enough energy to go to
							if (aveEnergy > energy && ave < BianBattleManager.WIDTH - 1) {
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
							// found place to go
							else {
								break;
							}
						}
						energy -= aveEnergy;
						str = this.goStreet(this.mydata, index, ave, energy);
					}
				}
				// go right
				else if (this.getAvenue() < this.mydata[index].getAvenue()){
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
							// found place to go
							else {
								break;
							}
						}
						str = this.getStreet();
					}
					// go right then turn
					else {
						ave = this.mydata[index].getAvenue();
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
							// found place to go
							else {
								break;
							}
						}
						energy -= aveEnergy;
						str = this.goStreet(this.mydata, index, ave, energy);
					}
				}
				// on ave
				else {
					ave = this.getAvenue();
					str = this.goStreet(this.mydata, index, ave, energy);
				}

				turn = new TurnRequest(ave, str, -1, 0); // endAvenue, endStreet, fightID, numRounds
			}
		}
		// lower than fightHealth amount of health -- run away after a fight if lose, run away if below 10 health, go where less than 2 people at that spot, fight if has highwer helath than opp, fight same operson after figthing a round instead of finding someoen else
		else {
			// retreat
			if (this.health < FIGHTHEALTH && fightID != -1) {
				// don't move to save energy
				if (energy < SAVEENERGY) {
					ave = this.getAvenue();
					str = this.getStreet();
				}
				// run away
				else {
					ave = retreat(movesLeft)[0];
					str = retreat(movesLeft)[1];
				}
			}
			// can't move or don't need to move
			else {
				ave = this.getAvenue();
				str = this.getStreet();
			}

			turn = new TurnRequest(ave, str, -1, 0); // endAvenue, endStreet, fightID, numRounds
		}

		this.round += 1;

		// reset round count to 0
		if (this.round == this.ROUNDPENALTY + 1) {
			this.round = 0;
		}

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
		this.health -= healthLost;
		
		// player exists
		if (oppID != -1 && this.mydata[oppID] != null) {
			this.mydata[oppID].setOppHealth(this.mydata[oppID].getOppHealth() + oppHealthLost);
		}
	}

	/**
	 * Finds a place that is OPPDISTANCE away from other players to retreat.
	 * @param movesLeft - moves left that the robot can take.
	 * @return coordinates of the place to retreat.
	 */
	private int [] retreat(int movesLeft) {
		int [] coord = {this.getAvenue(), this.getStreet()};
		final int MIN_OPP_DIS = 6; // minimum distance to any opponent
		int oppDis;

		// goes through all players
		for (int i = 0; i < this.mydata.length; i++) {
			OppData opp = this.mydata[i];

			// opponent exists and is not the robot itself
			if (opp.getHealth() > 0 && opp.getID() != this.getID()) {
				oppDis = distance(this.getAvenue(), this.getStreet(), opp.getAvenue(), opp.getStreet());

				// go other way if within MINOPPDISTANCE of other robot
				if (oppDis <= MIN_OPP_DIS) {
					// player ave smaller than opp ave
					if (this.getAvenue() < opp.getAvenue()) {
						coord[0] = coord[0] - (MIN_OPP_DIS - (opp.getAvenue() - this.getAvenue()));
					}
					// opp ave smaller than player ave
					else {
						coord[0] = MIN_OPP_DIS - (opp.getAvenue() - this.getAvenue());
					}

					// player str smaller than opp str
					if (this.getAvenue() < opp.getAvenue()) {
						coord[1] = coord[1] - (MIN_OPP_DIS - (opp.getStreet() - this.getStreet()));
					}
					// opp str smaller than player str
					else {
						coord[1] = MIN_OPP_DIS - (opp.getStreet() - this.getStreet());
					}

					// left of battlefield
					while (coord[0] < 0) {
						coord[0] += 1;
					}

					// right of battlefield
					while (coord[0] > BianBattleManager.WIDTH) {
						coord[0] -= 1;
					}

					// bottom of battlefield
					while (coord[1] < 0) {
						coord[1] += 1;
					}

					// top of battlefield
					while (coord[0] > BianBattleManager.HEIGHT) {
						coord[1] -= 1;
					}
				}
			}

			oppDis = distance(this.getAvenue(), this.getStreet(), coord[0], coord[1]);

			// on left of opp
			if (this.getAvenue() < coord[0]) {
				// distance greater than moves can go
				while (oppDis > movesLeft) {
					coord[0] -= 1;
				}
			}
			// on right of opp
			else if (this.getAvenue() > coord[0]) {
				// greater than moves can go
				while (oppDis > movesLeft) {
					coord[0] += 1;
				}
			}

			// on top of opp
			if (this.getStreet() < coord[1]) {
				// distance greater than moves can go
				while (oppDis > movesLeft) {
					coord[1] -= 1;
				}
			}
			// on bottom of opp
			else if (this.getStreet() > coord[1]) {
				// greater than moves can go
				while (oppDis > movesLeft) {
					coord[1] += 1;
				}
			}

		}

		return coord;
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
		if (this.getStreet() > this.mydata[index].getStreet()) {
			// go partway
			if (disStr > numMoves) {
				str = this.getStreet() - numMoves;
				// allows to subtract up until this.getStreet()
				for (int i = 0; i < numMoves; i++) {
					// move to place where it has enough energy to go to
					if (strEnergy > energy && str < BianBattleManager.HEIGHT - 1) {
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
					// found place to go
					else {
						break;
					}
				}
			}
			// go full way
			else {
				str = this.mydata[index].getStreet();
				// allows to subtract up until this.getStreet()
				for (int i = 0; i < this.getNumMoves(); i++) {
					// move to place where it has enough energy to go to
					if (strEnergy > energy && str < BianBattleManager.HEIGHT - 1) {
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
					// found place to go
					else {
						break;
					}
				}
			}
		}
		// go down
		else if (this.getStreet() < this.mydata[index].getStreet()) {
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
					// found place to go
					else {
						break;
					}
				}
			}
			// go full way
			else {
				str = this.mydata[index].getStreet();
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
					// found place to go
					else {
						break;
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
	 */private BianOppData [] selectSort(BianOppData [] list) {
		 // go through list
		 for (int i = 0; i < list.length; i++) {
			 // compare with all other values
			 for (int j = i; j < list.length; j++) {
				 int distance1 = distance(this.getAvenue(), this.getStreet(), list[j].getAvenue(), list[j].getStreet());
				 int distance2 = distance(this.getAvenue(), this.getStreet(), list[i].getAvenue(), list[i].getStreet());
				 int num1 = (int) (distance1 * 0.33 + list[j].getHealth() * 0.33 + list[j].getOppHealth() * 0.33);
				 int num2 = (int) (distance2 * 0.33 + list[i].getHealth() * 0.33 + list[i].getOppHealth() * 0.33);

				 // distance and health at j is lower than distance and health at i, swap
				 if (num1 < num2) {
					 BianOppData temp = list[i];
					 list[i] = list[j];
					 list[j] = temp;
				 }
			 }
		 }
		 return list;
	 }

	 /**
	  * Performs a selection sort on an array.
	  * @param list - array of values to sort
	  * @return sorted array
	  */
	 private BianOppData [] disSort(BianOppData [] list) {
		 // go through list
		 for (int i = 0; i < list.length; i++) {
			 // compare with all other values
			 for (int j = i; j < list.length; j++) {
				 int distance1 = distance(this.getAvenue(), this.getStreet(), list[j].getAvenue(), list[j].getStreet());
				 int distance2 = distance(this.getAvenue(), this.getStreet(), list[i].getAvenue(), list[i].getStreet());

				 // distance at j is lower than distance at i, swap
				 if (distance1 < distance2) {
					 BianOppData temp = list[i];
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

}
