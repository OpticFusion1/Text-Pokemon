//This class is for every single battle, and its fields r the fighting pokemon, the enemy, and all the team pokemons.

import java.util.*;
import java.io.*;
class Battle{
	
	static Scanner in = new Scanner(System.in);
	private Pokemon currentPoke;
	private Pokemon enemyPoke;
	private ArrayList<Integer> team;
	private int round = 0;
	private boolean won = false;
	private ArrayList<Pokemon> allPoke;
	
	//Intiliazing, with picked poke, enemy poke, current turn, your team, and all pokemon in game
	public Battle(Pokemon pickedPokemon, Pokemon badPoke, boolean givenTurn, ArrayList<Integer> team, ArrayList<Pokemon> allPokemon){
		this.team = new ArrayList<Integer>();
		currentPoke = pickedPokemon;
		enemyPoke = badPoke;
		this.team = team;
		allPoke = allPokemon;
		//setting variables to naes
	}
	
	public void turnStart(){
		//starting turn
		
		//if u r stunned, u dont go but then u get unstunned
		if(currentPoke.isStunned()){
			System.out.println("Your stunned for this turn!");
			round++;
			currentPoke.unStun();
		}
		//otherwise it lets u attack, pass, or retreat
		else{
			System.out.println("\n1. attack\n2. retreat\n3. pass");
			System.out.print("choose an action:  ");
			int turnChoice = in.nextInt();
			//pick attack and do the stuff
			if(turnChoice == 1){
				pickAttack();
				round++;
			}
			//switch pokemons
			else if(turnChoice == 2){
				int newPoke = switchOut();
				currentPoke = allPoke.get(team.get(newPoke));
				System.out.println(currentPoke.getName() + ", I choose you!");
				round++;
			}
			//pass turn and do nothing
			else if(turnChoice == 3){
				System.out.println("You pass your turn");
				round++;
			}
		}
		if(won == false && round != 2){
			
			enemyTurn();
		}
		else if(won == false && round == 2){
			endRound();
			enemyTurn();
		}
		else if(won == true && round == 2){
			healEnergy();
			healHp();
			roundWin();
		}
		else if(won == true && round != 2){
			healHp();
			roundWin();
		}
		
	}
	//picking ur attack based on if u can afford, and tjen delivering the attack
	public void pickAttack(){
		currentPoke.showAttack();
		System.out.print("\nPick an Attack:  ");
		int attackNum = in.nextInt() - 1;
		boolean attackPossible = currentPoke.canAfford(currentPoke.getAttacks().get(attackNum));
		if(attackPossible == false){
			System.out.println("Not enough energy!");
			turnStart();
		}
		currentPoke.doAttack(currentPoke.getAttacks().get(attackNum), enemyPoke);
		boolean fainted = !enemyPoke.isAlive();
		if(fainted){
			won = true;
			currentPoke.unStun();
		}
	}
	//this is the turn of the enemies
	public void enemyTurn(){
		//similar to ur tutn, for the stunned and etc
		if(enemyPoke.isStunned()){
			System.out.println(enemyPoke.getName() + " is stunned!");
			round++;
			enemyPoke.unStun();
		}
		else{
			//if the enemy attack method returns null, it means it denst have energ y for any moves
			if(enemyPoke.enemyAttack()==null){
				System.out.println("\n" + enemyPoke.getName() + " passes!");
				//so it passes
				round++;
			}
			else{
				//otjerwsie it does damage and attacks
				enemyPoke.doAttack(enemyPoke.enemyAttack(), currentPoke);
				System.out.printf("%s used %s.\n", enemyPoke.getName(), enemyPoke.enemyAttack().getName());
				boolean fainted = !currentPoke.isAlive();
				//if the enemy kills u, u die and get removed from ur team
				if(fainted){
					System.out.println(currentPoke.getName() + " fainted!\n");
					team.remove(allPoke.indexOf(currentPoke));
					checkLose();
					round++;
				}
				else{
					round++;
				}
			}
		}
		if(round == 2){
			endRound();
		}
		if(won == false){
			turnStart();
		}
	}
	
	public int switchOut(){
		//switching out the pokemon, u get to see name and based on what number u select, u switch with them
		int count = 1;
		for(Integer i: team){
			String name = allPoke.get(i).getName();
			System.out.printf("%d. %s \n", count, name);
			count++;
		}
		
		System.out.print("\nChoose which pokemon to use:  ");
		int chosenPokemon = in.nextInt() - 1;
		return chosenPokemon;
	}
	//u end the cur round, recharge pokemon, andthen start again
	public void endRound(){
		System.out.println("\nNext round start!");
		for(Integer i : team){
			allPoke.get(i).recharge(10);
		}
		enemyPoke.recharge(10);
		
		round = 0;
	}
	//if u win, u go and start new round with more hp and more  energy
	public void roundWin(){
		//to say opponent fainted
		System.out.println(enemyPoke.getName() + " fainted!");
		currentPoke.displayInfo();
		System.out.println("\nNext battle start\n");
		won = true;
	}
	//check if u lose that matchm adb then brin gout other pokemon
	public void checkLose(){
		if(team.size() > 0){
			int newPoke = switchOut();
			currentPoke = allPoke.get(team.get(newPoke));
			System.out.println(currentPoke.getName() + ", I choose you!");
			enemyPoke.unStun();
			
		}
		//but if u dont have anymore, u quit and lose game
		else{
			System.out.println("Your team has been defeated!");
			won = true;
		}
	}
	//heal the energy every turn
	public void healEnergy(){
		for(Integer i : team){
			allPoke.get(i).recharge(10);
		}
	}
	//heal hp every round
	public void healHp(){
		for(int i : team){
			//System.out.println(allPoke.get(i).getName());
			allPoke.get(i).heal();
		}
	}
}
