import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.io.File;


public class PlayerTest {
	
	private File file;
	private Deck drawDeck;
	private Deck discardDeck;
	private Player player;
	
	@Before
	public void setup() {
		file = new File("PlayerTestFile.txt");
		drawDeck = new Deck();
		discardDeck = new Deck();
		drawDeck.giveCard(new Card(1));
		drawDeck.giveCard(new Card(2));
		drawDeck.giveCard(new Card(3));
		drawDeck.giveCard(new Card(4));
		drawDeck.giveCard(new Card(5));
		player = new Player(1, 2, drawDeck, discardDeck, file);
		
	}
	
	@After
	public void cleanup() {
		file.delete();
	}
	
	@Test
	public void TestCheckWin() {
		for (int i =0; i<4; i++) {
			player.addCard(new Card(4));
		}
		assertTrue(player.checkWin());
	}
	
	@Test
	public void TestCheckNotWin() {
		for (int i=0; i<4; i++) {
			player.addCard(new Card(i));
		}
		assertFalse(player.checkWin());
	}
	
	@Test
	public void TestDiscard() {
		for (int i=0; i<5; i++) {
			player.addCard(new Card(i));
		}
		assertTrue(discardDeck.deck.size() == 0);
		player.discard();
		assertTrue(discardDeck.deck.size() == 1);
	}
	
	@Test
	public void TestDiscardIndex() {
		for (int i=0; i<3; i++) {
			player.addCard(new Card(1));
		}
		player.addCard(new Card(2));
		for (int i=0; i<25; i++) {
			player.addCard(new Card(2));
			player.discard();
			assertFalse(discardDeck.getCard().getValue() == 1);
		}
	}
	
	@Test
	public void TestDiscardStaleness() {
		//Setup hand to be 2,2,2,3
		for (int j=0; j<3; j++) {
			player.addCard(new Card(2));
		}
		player.addCard(new Card(3));
		//Repeat 50 times to mitigate impact of randomness
		for (int i=0; i<50; i++) {
			//Add a 2 five times, discarding each
			for (int j=0; j<5; j++) {
				player.addCard(new Card(2));
				player.discard();
			}
			//Check if 3 was discarded
			boolean discarded3 = false;
			for (int j=0; j<5; j++) {
				if (discardDeck.getCard().getValue() == 3) {
					discarded3 = true;
				}
			}
			assertTrue(discarded3);
			//Add a 3 for next test- it won't be immediately discarded as a 2 will be excessively stale
			player.addCard(new Card(3));
			player.discard();
			assertFalse(discardDeck.getCard().getValue() == 3);
		}
	}
		
}

//Mock object of Deck class.
class Deck {
	
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