import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
*The main class for the card game.
*Manages set up and finishing, and keeps references
*to all players and decks.
*/
public class CardGame {
	
	private ArrayList<Player> players;
	private ArrayList<Deck> decks;
	
	public static void main(String[] args) {
		
		int n = GetN();
		ArrayList<Player> players;
		ArrayList<Deck> decks;
		for (int i = 0; i < n; i++) {
			decks.add(new Deck());
		}
		for (int i = 0; i < n; i++) {
			drawDeck = decks.get(i).getOutPile();
			discardDeck = decks.get((i+1)%n).getInPile();
			players.add(new Player(i, drawDeck, discardDeck));
		}
		ArrayList<Card> pack = ReadPack(n);
		DealCards(players, decks, pack);
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
	*
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