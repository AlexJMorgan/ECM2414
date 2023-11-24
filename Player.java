import java.util.ArrayList;

/**
*Player Class to represent each player in the card game.
*Stores its hand of cards and the decks it draws/discards from.
*@author Daniel and Alex
*@version 0.1
*/
public class Player {

	private ArrayList<Card> cards;
	private int index;
	private ArrayList<Card> drawDeck;
	private ArrayList<Card> discardDeck;
	
	/**
	*Constructor method for Player class.
	*@param index        used to refer to the player in log messages and set preferred card value
	*@param drawDeck     the deck to be drawn from
	*@param discardDeck  the deck to be discarded to
	*/
	public Player(int index, ArrayList<Card> drawDeck, ArrayList<Card> discardDeck) {
		this.cards = new ArrayList<>();
		this.index = index;
		this.drawDeck = drawDeck;
		this.discardDeck = discardDeck;
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
	private boolean checkWin() {
		int val = cards.get(0).getValue();
		for (i = 1; i<4; i++) {
			if (cards.get(i)).getValue() != val) {
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
		}
	}
	
	/**
	*Gets the top card from the drawDeck deck and adds it to the player's hand.
	*/
	public void draw() {
		Card card = drawDeck.getCard();
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
		Card card;
		
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
		discardDeck.giveCard(card);
	}	
	
	
	
	
	
}
