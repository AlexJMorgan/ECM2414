import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter; 
import java.io.IOException;

/**
*Player Class to represent each player in the card game.
*Stores its hand of cards and the decks it draws/discards from.
*@author Daniel and Alex
*@version 0.1
*/


public class Player {

	private ArrayList<Card> cards;
	private int index;
	private int discardIndex;
	private File file;
	private Deck drawDeck;
	private Deck discardDeck;
	
	
	/**
	*Constructor method for Player class.
	*@param index			used to refer to the player in log messages and set preferred card value
	*@param discardIndex	used to refer to the discard deck in log messages
	*@param drawDeck		deck the player draws cards from
	*@param discarDeck		deck the player discards cards to
	*@param file			file reference where player outputs their moves
	*/
	public Player(int index, int discardIndex, Deck drawDeck, Deck discardDeck, File file) {
		this.cards = new ArrayList<>();
		this.index = index;
		this.discardIndex = discardIndex;
		this.drawDeck = drawDeck;
		this.discardDeck = discardDeck;
		this.file = file;
	}
	
	/**
	*Adds card to current hand.
	*This method is to be used when setting up the initial hand of each player, and when drawing a card.
	*@param card  the card to be added
	*/
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	*Checks if the player has four matching cards.
	*@return  true if player has won
	*/
	public boolean checkWin() {
		int val = cards.get(0).getValue();
		for (int i = 1; i<4; i++) {
			if (cards.get(i).getValue() != val) {
				return false;
			}
		}
		return true;		
	}
	
	/**
	*Draws a card from the draw pile, and discards an unwanted card to the discard pile.
	*Will wait if draw deck is empty, and notify waiting threads when discarding.
	*/
	public void makeTurn() {
		synchronized (drawDeck) {
			try {
				if (drawDeck.isEmpty()) {
				drawDeck.wait();
				}
				synchronized (discardDeck) {
					draw();
					discard();
					for (int i = 0; i<4; i++) {
						cards.get(i).addStaleness();
					}
					discardDeck.notify();
				}
			} catch (InterruptedException e) {
				System.out.println("Interrupted makeTurn for Player "+index);
			}
		}
		String cardValues = "";
		for (int i=0; i<4; i++) {
			cardValues += " " + cards.get(i).getValue();

		}
	
		
		writeToFile(file, "\nplayer "+index+" current hand is"+cardValues);
		
	}
	
	public void playerInitialHand() {
		String initialHand = "";
		for (int i=0; i<4; i++) {
			initialHand += " " + cards.get(i).getValue();
		}
		writeToFile(file, "player "+index+" initial hand is"+initialHand);
	}
	
	/**
	*Gets the top card from the drawDeck deck and adds it to the player's hand.
	*/
	public void draw() {
		Card card = drawDeck.getCard();
		writeToFile(file, "\nplayer "+index+" draws a "+card.getValue()+" from deck "+index);
		cards.add(card);
	}
	
	/**
	*Discards a random card from the player's hand.
	*Discards the first card with a staleness over 4.
	*Otherwise will discard the first card in the hand.
	*Will not discard cards of preferred value.
	*/
	public void discard() {
		boolean decided = false;
		Card card = new Card(3000);
		
		for (int i = 0; i<4; i++) {
			card = cards.get(i);
			if (card.getValue() != index) {
				if (card.getStaleness() > 4) {
					decided = true;
					cards.remove(card);
					break;
				}
			} 
		}	
		if (!decided) {
			for (int i = 0; i<4; i++) {
				card = cards.get(i);
				if (card.getValue() != index) {
					decided = true;
					cards.remove(card);

					break;
				}
			}
		}
		card.resetStaleness();
		writeToFile(file, "\nplayer "+index+" discards a "+card.getValue()+" to deck "+discardIndex);
		discardDeck.giveCard(card);
	}	

	public void writeToFile(File file, String msg) {
		try {
			FileWriter writer = new FileWriter(file.getName(), true);
		      	writer.write(msg);
		      	writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}
	
	
	
	
	
	
}
