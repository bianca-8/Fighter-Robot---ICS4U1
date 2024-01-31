package unit_4_robotWar_summative;

/**
 * Extends OppData to store additional information from battle results.
 * @author Bianca
 * @version Jan 20, 2023
 */

public class BianOppData extends OppData {
	private int oppHealth;
	
	/**
	 * The OppData constructor used to make records of this type.
	 * @param id		the ID number of the player's ID for this OppData record
	 * @param a			the player's avenue for this OppData
	 * @param s			the player's street for this OppData
	 * @param health	the player's health for this OppData
	 * @param oppHealth	the player's health lost from battles for this OppData
	 */
	public BianOppData(int id, int a, int s, int health, int oppHealth) {
		super(id, a, s, health);
		this.oppHealth = oppHealth;
	}
	
	/**
	 * Accessor method for the player's health lost from battles for this OppData record.
	 * @return	the player's health lost from battles for this OppData
	 */
	public int getOppHealth()
	{
		return this.oppHealth;
	}
	
	/**
	 * Modifier method for the player's health lost from battles for this OppData record.
	 * @param oppHealth the player's health lost from battles for this OppData
	 */
	public void setOppHealth(int oppHealth)
	{
		this.oppHealth = oppHealth;
	}
}
