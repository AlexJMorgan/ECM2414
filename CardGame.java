import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter; 

/**
*The main class for the card game.
*Manages set up and finishing, and keeps references
*to all players and decks.
*/
public class CardGame {
	
	private static ArrayList<Player> players = new ArrayList<>();
	private static ArrayList<Deck> decks = new ArrayList<>();
	private static ArrayList<Thread> playerThreads = new ArrayList<>();
	private static int winnerIndex = 0;
	private static int n;

	/**
 	*Main method invoked when the object is instantiated.
  	*Creates a deck object for each player.
   	*Creates n player objects.
  	*/
	public static void main(String[] args) {
		n = GetN();
		for (int i = 0; i < n; i++) {
			try {
				File file = new File("deck"+(i+1)+"_output.txt");
				if (!file.createNewFile()) {
					file.delete();
					file.createNewFile();
			    }
				decks.add(new Deck(file, i+1));
			} catch (IOException e) {
				System.out.println("An error occurred.");
			}
		}
		for (int i = 0; i < n; i++) {
			Deck drawDeck = decks.get(i);
			Deck discardDeck = decks.get((i+1)%n);
			try {
				File file = new File("player"+(i+1)+"_output.txt");
			      	if (!file.createNewFile()) {
						file.delete();
						file.createNewFile();
			      	}
					players.add(new Player(i+1, (i+1)%n +1, drawDeck, discardDeck, file));
			} catch (IOException e) {
				System.out.println("An error occurred.");
			}
			

		}
		ArrayList<Card> pack = ReadPack(n);
		DealCards(players, decks, pack);
		
		
		for (int i=0; i<players.size(); i++) {
			Thread playerThread = new Thread(players.get(i));
			playerThreads.add(playerThread);
			playerThread.start();
		}
		System.out.println("game started! ! ");
	}
	
	public static void finish(int winner) {
		if (winnerIndex == 0) {
			winnerIndex = winner;
			for (int i=0; i<playerThreads.size(); i++) {
				if (i != winner-1) {
					playerThreads.get(i).interrupt();
				}
			}
			for (int i=0; i<decks.size(); i++) {
				decks.get(i).printDeckContents();
			}
			System.out.println("Player "+winner+" wins");
		}

	}
	
	public static int getWinner() {
		return winnerIndex;
	}
	
	/**
	*Deals cards from initial pack to players and decks in round robin fashion.
	*@param cards	The ArrayList of cards in the pack, extracted from the pack file.
	*/
	public static void DealCards(ArrayList<Player> players, ArrayList<Deck> decks, ArrayList<Card> cards) {
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < n; j++) {
				players.get(j).addCard(cards.get(0));
				cards.remove(0);
			}
		}
		
		while (cards.size() > 0) {
			for (int j = 0; j < n; j++) {
				decks.get(j).dealCard(cards.get(0));
				cards.remove(0);
			}
			
		}
	}
	
	/**
	*Reads the pack the user provided. 
 	*Continues asking the user for a pack, until they provide a valid input.
  	*Outputs the necessary error message to inform the user of the issue.
   	*@param n	Provides the number of players to ensure there are 8*n cards provided.
	*/	
	public static ArrayList<Card> ReadPack(int n) {
		while (true) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please enter location of pack to load:");
			String fileName = scanner.nextLine();
			int fileNameLength = fileName.length();
			if (fileNameLength < 4 || !fileName.substring(fileNameLength-4).equals(".txt")) {
				fileName += ".txt";
			}
						
			ArrayList<Card> cards = new ArrayList<>();
			String errorMsg = "";
			
			try {
				File packFile = new File(fileName);
				Scanner fileReader = new Scanner(packFile);
				while (fileReader.hasNextLine()) {
					String data = fileReader.nextLine();
					int intData = Integer.parseInt(data);
					if (intData < 0) {
						throw new NumberFormatException();
					}					
					Card card = new Card(intData);
					cards.add(card);
				} 
						
				fileReader.close();
				
			} catch (FileNotFoundException e) {
				errorMsg = "Error finding file.";
			} catch (NumberFormatException e) {
				errorMsg = "Card values must be non negative integers.";
			}

			if (cards.size() != 8*n) {
				errorMsg = (errorMsg.equals("")) ? "Pack size invalid" : errorMsg;
			} else {
				return cards;
			}
			System.out.println(errorMsg);
		}
	}
	
	public static int GetN() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Please enter the number of players:");
			String n = scanner.nextLine();
			try {
				int intN = Integer.parseInt(n);
				if (intN > 1) {
					return intN;
				}
				System.out.println("Please enter a valid integer.");
			} catch (NumberFormatException e) {
				System.out.println("Please enter a integer.");
			}
								
			
		}
		

	}
}
