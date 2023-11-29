import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;


/**
*Test class for Deck using JUnit4.10.
*@author Daniel and Alex
*@version 1.0
*/
public class DeckTest {
	
	private Deck deck;
	private Card card;
	private ArrayList<Card> inPile;
	private ArrayList<Card> outPile;
	
	
	@Before
	public void Setup() {
		File file = new File("BlankFile.txt");
		deck = new Deck(file, 1);
		card = new Card(1);
	}
	
	@Test
	public void TestEmptyOnInit() {
		assertTrue(deck.isEmpty());
		assertTrue(deck.isMidPileEmpty());
	}
		
	@Test
	public void TestDealCard() {
		assertTrue(deck.isEmpty());
		deck.dealCard(card);
		assertFalse(deck.isEmpty());
	}
	
	@Test
	public void TestCardThroughDeck() {
		deck.giveCard(card);
		assertTrue(deck.isMidPileEmpty());
		deck.inPileToMid();
		assertFalse(deck.isMidPileEmpty());
		assertTrue(deck.isEmpty());
		deck.midPileToOut();
		assertFalse(deck.isEmpty());
		assertTrue(deck.isMidPileEmpty());
		Card recievedCard = deck.getCard();
		assertTrue(card.getValue() == recievedCard.getValue());
		assertTrue(deck.isEmpty());
	}
	
	@Test
	public void TestGetCard() {
		assertTrue(deck.isEmpty());
		deck.dealCard(card);
		assertFalse(deck.isEmpty());
		Card card2 = deck.getCard();
		assertTrue(deck.isEmpty());
		assertTrue(card2.getValue() == 1);
	}
	
	@Test
	public void TestFIFO() {
		Card card2 = new Card(2);
		deck.giveCard(card);
		deck.giveCard(card2);
		deck.inPileToMid();
		deck.midPileToOut();
		Card card3 = deck.getCard();
		Card card4 = deck.getCard();
		assertTrue(card3.getValue() == card.getValue());
		assertTrue(card4.getValue() == card2.getValue());
	}
	

	
	
	
}