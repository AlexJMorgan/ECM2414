import java.util.ArrayList;

/**
*Class for deck
*@author Daniel and Alex
*@version 0.1
*/
public class Deck{
	
	private ArrayList<Card> cards;
	
	/**
	*Constructor for Deck
	*/
	public Deck() {
	}
	
	/**
	*Checks if the deck is empty
	*@return	true if the deck is empty, otherwise false
	*/
	public boolean isEmpty() {
		return cards.size() == 0;
	}
	
	/**
	*Adds card to deck
	*@param card	card to be added
	*/
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	*Gets card from deck, and removes it.
	*@return	card object that is removed
	*/
	public Card getCard() {
		Card card = cards.get(0);
		cards.remove(card);
		return card;
	}
	
}