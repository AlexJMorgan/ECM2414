import java.util.ArrayList;
import java.io.File;

/**
*Class for deck
*Contains three piles, an in pile to recieve cards, 
*a middle pile to act as a buffer, and an out pile for
*cards to be taken from. This avoids deadlock scenarios.
*@author Daniel and Alex
*@version 0.1
*/
public class Deck{
	
	private ArrayList<Card> inPile = new ArrayList<>();
	private ArrayList<Card> midPile = new ArrayList<>();;
	private ArrayList<Card> outPile = new ArrayList<>();;
	private File file;

	public Deck(File file) {
		this.file = file;
	}
	
	/**
	*Checks if the deck is empty
	*@return	true if the deck is empty, otherwise false
	*/
	public boolean outPileIsEmpty() {
		return outPile.size() == 0;
	}
	
	/**
	*Inserts card directly into out pile
	*To be used during intial set up, when dealing out cards
	*@param card	card to be added
	*/
	public void dealCard(Card card) {
		outPile.add(card);
	}
	
	/**
	*Adds card to deck
	*@param card	card to be added
	*/
	public void giveCard(Card card) {
		inPile.add(card);	
	}
	
	/**
	*Moves cards from in pile to middle pile
	*/
	public void inPileToMid() {
		midPile.addAll(inPile);
		inPile.clear();
	}
	
	/**
	*Moves cards from middle pile to out pile
	*/
	public void midPileToOut() {
		outPile.addAll(midPile);
		midPile.clear();
	}
		
	/**
	*Gets card from deck, and removes it.
	*@return	card object that is removed
	*/
	public Card getCard() {
		Card card = outPile.get(0);
		outPile.remove(card);
		return card;
	}
	
	public ArrayList<Card> getInPileReference() {
		return inPile;
	}
	
	public ArrayList<Card> getOutPileReference() {
		return outPile;
	}
	
}
