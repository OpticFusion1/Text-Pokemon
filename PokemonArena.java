import java.util.*;
import java.io.*;

/*Arvind Bolisetti
 *Pokemon Arena
 *This is where all the magic happens. Everything is put together and it somhow wokrs out. Using fields of mainly pokemon and wehtehror not a battle is occuring
 */
public class PokemonArena {

	//lists of pokemons and game terms
	private static ArrayList<Pokemon> allPokemon = new ArrayList();
	private static ArrayList<Integer> yourPokemons = new ArrayList();
	private static ArrayList<Integer> enemyPokemons = new ArrayList();
	private static boolean running = true;
	private static boolean givenTurn;

	// File to read data of all Pokemon
	private static final String file = "pokemon.txt";
	public static Scanner in;


	public static Random rand;

	// Main method to play game
	public static void main(String[] args) {

		// initialize scanner
		in = new Scanner(System.in);

		// initialize random
		rand = new Random();
		//start game
		input("Press enter to Start");
		//extract data from txt file
		int numberOfPokemon = getData();
		//pick ur pokemon
		pickPoke(numberOfPokemon);
		//do the battle loops
		while(running){
			chooseTurn();
			choosePokemon();
			boolean win = checkWin();
			if(win){
				running = false;
			}
		}
		System.out.println("Congratulations! You are now Trainer Supreme!");

	}
	//Finds the data, gets everything and feeds into objects
	public static int getData(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			// number of Pokemons to read from file
			int n = Integer.parseInt(br.readLine());
			// read all Pokemons, create Pokemon object from each and add to allPokemon
			for (int i = 0; i < n; i++){
				// read details of Pokemon 
				String[] line = br.readLine().split(",");
				String name = line[0];
				int hp = Integer.parseInt(line[1]);
				String type = line[2];
				String resistance = line[3];
				String weakness = line[4];
				ArrayList<Attack> attacks = new ArrayList();

				
				int attacksNum = Integer.parseInt(line[5]);

				// read all attacks, create Attack object from each and add to attacks
				for (int j = 0; j < attacksNum; j++){
					// read details of Attack and validate
					String attackName = line[6+(j*4)];
					int cost = Integer.parseInt(line[7+(j*4)]);
					int damage = Integer.parseInt(line[8+(j*4)]);
					String special = line[9+(j*4)];
					
					Attack attack = new Attack(attackName, cost, damage, special);
					attacks.add(attack);
				}
				// create new Pokemon object and add to arrarrrayList of all Pokemons
				Pokemon pokemon = new Pokemon(name, hp, type, resistance, weakness, attacks);
				allPokemon.add(pokemon);

		
			}
			return n;
		}
		//check if fil not found
		catch (FileNotFoundException e){
			System.out.println("The file " + file + " was not found.");
			
			System.exit(-1);
			return 0;
			
		}//check iferror happened
		catch(IOException e){
			System.out.println("There was a problem trying to read the file.");
			
			System.exit(-1);
			return 0;
		}
	}
	//pick ur 4 pokemon
	public static void pickPoke(int n){
    	int remainder=n%2;
    	//copy of the array so i can get dyplicates and not duplicates
    	ArrayList<Pokemon> avail = new ArrayList<Pokemon>(allPokemon);
    	System.out.println("Pick four pokemon");
    	//4 times,i t prints everything out
    	for (int i=0;i<4;i++){
    		
    		for (int j=0;j<n/2;j++){
    			if (remainder==1){
    				//some string formatting to make it look even
    				System.out.printf("%2d. %-12s %d. %s \n",j+1,avail.get(j).getName(),j+n/2+2,avail.get(j+n/2+1).getName());
    				if (j==(n/2)-1){
	    				System.out.printf("%d. %s \n",j+2,avail.get(j+1).getName());
	    			}
    			}
    			else{
    				System.out.printf("%2d. %-12s %d. %s \n",j+1,avail.get(j).getName(),j+n/2+1,avail.get(j+n/2).getName());
    			}
    		}
    		//Reads in the nfo
    		int pokePos=in.nextInt();
    		avail.get(pokePos-1).displayInfo();
    		//displays it
    		
    		if (pokePos<=n){
    			//checks the pokemons index
    			
    			yourPokemons.add(allPokemon.indexOf(avail.get(pokePos-1)));
	    		
	    		avail.remove(pokePos-1);
	    		n--;
	    		remainder=n%2;
    		}
    		else{
    			System.out.println("Invalid Entry");
    			i--;
    		}
    		
    	}
		//this is for the other adding to ur list and adding enemies to enemies list
    	System.out.println("You have selected the following Pokemon: ");
		for (int i = 0; i < yourPokemons.size(); i++) {
			System.out.println(allPokemon.get(yourPokemons.get(i)).getName());
		}
		// add left over Pokemon to enemy list
		for (int i = 0; i < allPokemon.size(); i++) {
			if (!yourPokemons.contains(i)) {
				enemyPokemons.add(i);
			}
		}
    	
    }
    //method to danrmly chhose turn
    public static void chooseTurn() {
		int choice = rand.nextInt(2);
		if (choice == 0) {
			givenTurn = true;
			System.out.println("You go first!");
		}
		else{
			givenTurn = false;
			System.out.println("The enemy goes first!");
		}
	}

	//choose what pokemon u want, and then lead to enemy choose, which leads to battle	
	public static void choosePokemon(){
		int count = 1;
		for(Integer i : yourPokemons){
			
			System.out.printf("%d. %s \n", count, allPokemon.get(i).getName());
			count++;
		}
		System.out.println("\nChoose which pokemon to use:  ");
		int chosenPokemon = in.nextInt() - 1;
		Pokemon currentPoke = allPokemon.get(yourPokemons.get(chosenPokemon));
		enemyChoose(currentPoke);
	}
	//opp pokemon gets chosen randomly
	public static void enemyChoose(Pokemon currentPoke){
		int choice = rand.nextInt(enemyPokemons.size());
		Pokemon enemyPoke = allPokemon.get(enemyPokemons.get(choice));
		enemyPokemons.remove(enemyPoke);
		Battle battle = new Battle(currentPoke, enemyPoke, givenTurn, yourPokemons, allPokemon);
		//initializing the battle object
		System.out.println("Battle Start!");
		System.out.println("Vs. " + enemyPoke.getName());
		System.out.println(currentPoke.getName() + ", I choose you!");
		//starting turn based on coin flip
		if(givenTurn){
			System.out.println("You go first");
			
			battle.turnStart();
		}
		else{
			System.out.println("The enemy goes first");
			battle.enemyTurn();
		}
	}
	//its like a python kind of input, where print and input at the same time.
	public static String input(String val){
		System.out.println(val);
		return in.nextLine();
	}
	
	//check if u won or not
	public static boolean checkWin(){
		if(enemyPokemons.size() <= 0){
			return true;
		}
		else{
			return false;
		}
	}
}
