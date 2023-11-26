import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.io.File;


public class PlayerTest {
	
	
	private Deck deck;
	private Player player;
	
	@Before
	public void setup() {
		drawDeck = new Deck();
		discardDeck = new Deck();
		drawDeck.giveCard(new Card(1));
		drawDeck.giveCard(new Card(2));
		drawDeck.giveCard(new Card(3));
		drawDeck.giveCard(new Card(4));
		drawDeck.giveCard(new Card(5));
		player = new Player(1, drawDeck, discardDeck);
		
	}
	
	@Test
	public void TestCheckWin() {
		for (int i =0; i<4; i++) {
			player.addCard(new Card(4));
		}
		assertTrue(player.checkWin());
	}
		
}

//Mock object of Deck class.
private class Deck {
	
	public ArrayList<Card> deck = new ArrayList<>();
	
	public Card getCard() {
		Card card = deck.get(0);
		deck.remove(0);
		return card;
		
	}
	
	public void giveCard(Card card) {
		deck.add(card);
	}
	
	public boolean isEmpty() {
		return deck.size() == 0;
	}
	
}