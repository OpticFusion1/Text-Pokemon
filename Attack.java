/*This class is for each and every attack
 *It stores data on the name, cost, damage, and any special abilities of the attack
 *The only methods it posses are based on returning the fields
 */

public class Attack{

	
	private String name;
	private int cost;
	private int damage;
	private String special;
	
	//Initializing the object
	public Attack(String name, int cost, int damage, String special) {
		this.name = name;
		this.cost = cost;
		this.damage = damage;
		this.special = special;
	}
	//Receiving the name
	public String getName() {
		return name;
	}
	//Finding how much costs to do
	public int getCost() {
		return cost;
	}
	//Seeing how much damage it does
	public int getDamage() {
		return damage;
	}
	//Checking if it has any specials 
	public String getSpecial() {
		return special;
	}
	/*This method is exclusively for the wild storm special
	 *where you need to chang ethe cost to 0 so u can do free attacks*/ 
	public void changeCost(int cost) {
		this.cost = cost;
	}
	/*This is just printing the attack values to see its contents*/
	public String toString() {
		return "Name - " + name + "\nCost - " + cost + "\nDamage - " + damage + "\nSpecial - " + special;
	}
}