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

	/**
 	*Main method invoked when the object is instantiated.
  	*Creates a deck object for each player.
   	*Creates n player objects.
  	*/
	public static void main(String[] args) {
		int n = GetN();
		for (int i = 0; i < n; i++) {
			try {
				File file = new File("deck"+(i+1)+"_output.txt";
				if (!file.createNewFile()) {
					file.delete();
					file.createNewFile();
			      	}
			} catch (IOException e) {
				System.out.println("An error occurred.");
			}
			decks.add(new Deck(file, i));
		}
		for (int i = 0; i < n; i++) {
			drawDeck = decks.get(i).getOutPile();
			discardDeck = decks.get((i+1)%n).getInPile();
			try {
				File file = new File("player"+(i+1)+"_output.txt");
			      	if (!file.createNewFile()) {
					file.delete();
					file.createNewFile();
			      	}
			} catch (IOException e) {
				System.out.println("An error occurred.");
			}
			
			
			players.add(new Player(i, (i+1)%n, drawDeck, discardDeck, file));
		}
		ArrayList<Card> pack = ReadPack(n);
		DealCards(players, decks, pack);
		
		for (int i=0; i<players.size(); i++) {
			Thread playerThread = new Thread(player);
			playerThreads.add(playerThread);
			playerThread.start();
		}
	}
	
	public static void finish(int winner) {
		winnerIndex = winner;
		for (int i=0; i<playerThreads.size(); i++) {
			if (i != winner-1) {
				playerThreads.get(i).interrupt();
			}
		}
		for (int i+0; i<decks.size(); i++) {
			decks.get(i).printDeckContents();
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
			System.out.print("Enter pack file name: ");
			String fileName = scanner.nextLine();
			if (!fileName.substring(-4).equals(".txt")) {
				fileName += ".txt";
			}
			
			ArrayList<Card> cards;
			String errorMsg = "";
			
			try {
				File packFile = new File(fileName);
				Scanner fileReader = newScanner(packFile);
				while (fileReader.hasNextLine()) {
					String data = fileReader.nextLine();
					Card card = new Card(data);
					cards.add(card);
				} 
						
				fileReader.close();
				
			} catch (FileNotFoundException e) {
				errorMsg = "Error finding file.";
			}

			if (cards.size() != 8*n) {
				errorMsg = (errorMsg.equals("")) ? "Pack size invalid" : errorMsg;
			} else {
				return cards;
			}
			System.out.println(errorMsg);
		}
	}
}
