/*The pokemon class is to store data and keep methods to attack, heal, recharge, and etc.
 *This class posses the fields of hp, attacks, types, resistences, weaknesses, and statuses.
 */

import java.util.*;

public class Pokemon {
	//Most of the fields r self explanatory
	private String name;
	private int hp;
	private int maxHP;//This is to keep track of max hp so it doesnt heal extra
	private int energy;
	private String type;
	private String resistance;
	private String weakness;
	private ArrayList<Attack> attacks = new ArrayList();//Arraylist of attacks
	private boolean disabled = false;//Flags of the statuses of the pokemon
	private boolean stunned = false;

	private Random rand = new Random();//Making a randon object for the 'wild' specials

	public Pokemon(String name, int hp, String type, String resistance, String weakness, ArrayList<Attack> attacks) {
		//Setting the variables to the values
		this.name = name;
		this.hp = hp;
		this.maxHP = hp;
		this.type = type;
		this.resistance = resistance;
		this.weakness = weakness;
		this.attacks = attacks;

		energy = 50;
	}

	//This method wll have the attack with all the specials ad everything
	public String doAttack(Attack attack, Pokemon enemy) {
		// Pokemon cannot attack while stunned so it immediately makes it skip turn
		if (isStunned()) {
			return "";
		}

		// calculate damage by taking in disabled into account
		int damage = 0;
		if (isDisabled()) {
			damage = 10;
		}

		// checks if it can afford it, and then reduces it by that much amount
		if (canAfford(attack)) {
			reduceEnergy(attack.getCost());
		}
		else {
			System.out.println("You cannot afford that attack.");
			return ""; // cant afford
		}
		//AThis is when its attacking, it has 50% cchance of stunning
		if(attack.getSpecial().equals("stun")){
		
			enemy.getHit(enemy.getType(), attack.getDamage() - damage);
			//when u take in enemy as a paramter, it will do damage based on its typing (weakness etc.)
			
			if (chance(50)) {
				enemy.stun();
			}
		}
		//checks for wild card, f it hits it hits otehrwise it dont
		if(attack.getSpecial().equals("wild card")){
			if (chance(50)) {
				enemy.getHit(enemy.getType(), attack.getDamage() - damage);
			}
		}	
		//wild storm
		if(attack.getSpecial().equals("wild storm")){
			if (chance(50)) {
				//if it hits, it gets another chance to do again
				//u make a copy of the attack but change the cost so its 0 
				//so its free
				enemy.getHit(enemy.getType(), attack.getDamage() - damage);
				Attack substitute = attack;
				substitute.changeCost(0);
				doAttack(substitute, enemy);
			}
		}
		//diable is simple
		if(attack.getSpecial().equals("disable")){
			enemy.getHit(enemy.getType(), attack.getDamage() - damage);
			enemy.disable();
		}
		//recharge just gets hp back
		if(attack.getSpecial().equals("recharge")){
			enemy.getHit(enemy.getType(), attack.getDamage() - damage);			
			recharge();
		}
		//if no special, it just does normal stuff
		else{
			enemy.getHit(enemy.getType(), attack.getDamage() - damage);
		}
	
		return "";
	}
	
	//check how much damage i sbeing done based on weakness, res, or nothing
	public void getHit(String type, int dmg){
		if(resistance.equals(type)){
			reduceHp(dmg/2);
		}
		else if(weakness.equals(type)){
			reduceHp(dmg*2);
		}
		else{
			reduceHp(dmg);
		}
	}
	//reduces energy, basically ghetto version of max(x, 0)
	public void reduceEnergy(int energyReduce){
		if(energy > energyReduce){
			energy -= energyReduce;
		}
		else{
			energy = 0;
		}
	}
	//samething as previous but for hp
	public void reduceHp(int hpReduce) {
		if (hp > hpReduce) {
			hp -= hpReduce;
		}
		else {
			hp = 0;
		}
	}
	//Stuns poke
	public void stun() {
		stunned = true;
	}
	//makes poke not stunned
	public void unStun() {
		stunned = false;
	}
	//checks if it stunneded
	public boolean isStunned() {
		return stunned;
	}
	//disabled poke 
	public void disable() {
		disabled = true;
	}
	//checks if it is disabled
	public boolean isDisabled() {
		return disabled;
	}

	//recharges, ghetto version of min(x,50)
	public void recharge() {
		if (energy <= 30) {
			energy += 20;
		} else {
			energy = 50;
		}
	}

	//recharging based on certain amount
	public void recharge(int amt) {
		if (energy <= 50 - amt) {
			energy += amt;
		} else {
			energy = 50;
		}
	}

	//heal the pokemon
	
	public void heal() {
		if(hp+20>maxHP){
			hp = maxHP;
		}
		else{
			hp+=20;
		}	
	}

	//check if it has enough cost to do the attack
	public boolean canAfford(Attack attack) {
		try {
			return energy >= attack.getCost();
		} catch (NullPointerException e) {
			return false;
		}
	}
	//doing sketchy chance things
	public boolean chance(int percentage) {
		return percentage <= rand.nextInt(101);
	}
	//nezxt bit is just erecievnng fields
	public String getName() {
		return name;
	}
	//energy
	public int getEnergy() {
		return energy;
	}
	//get hp
	public int getHp() {
		return hp;
	}
	//check type
	public String getType() {
		return type;
	}
	//check res
	public String getResistance() {
		return resistance;
	}
	//check weakness	
	public String getWeakness() {
		return weakness;
	}
	//check if it aint ded yet
	public boolean isAlive() {
		return hp > 0;
	}

	//list of all attacks
	public ArrayList<Attack> getAttacks() {
		return attacks;
	}
	//shows all the attacks
	public void showAttack(){
		int count = 0;
		for(Attack atk: attacks){
			count++;
			System.out.println(count+". "+atk.getName());
			
		}
	}

	//gives first ude that it can do, otherwise return null ucz it cand do anything
	public Attack enemyAttack() {
		for (Attack attack : attacks) {
			if (canAfford(attack)) {
				return attack;
			}
		}
		return null;
	}
	
	//displaying  ateeny bit of info
	public void displayInfo() {
		System.out.println("Name: " + name+ "\nHP - " + hp + "\nEnergy - " + energy);
	}

}
