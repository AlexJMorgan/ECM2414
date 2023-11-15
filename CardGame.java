import java.util.ArrayList;

/**
*The main class for the card game.
*Manages set up and finishing, and keeps references
*to all players and decks.
*/
public class CardGame {
	
	private int n;
	private arrayList<Player> players;
	private arrayList<Deck> decks;
	
	/**
	*Deals cards from initial pack to players and decks in round robin fashion.
	*@param cards	The arrayList of cards in the pack, extracted from the pack file.
	*/
	public void DealCards(arrayList<Card> cards) {
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
}