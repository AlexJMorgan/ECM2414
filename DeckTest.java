import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;

public class DeckTest {
	
	private Deck deck;
	private Card card;
	private ArrayList<Card> inPile;
	private ArrayList<Card> outPile;
	
	
	@Before
	public void Setup() {
		File file = new File("BlankFile.txt");
		deck = new Deck(file);
		inPile = deck.getInPileReference();
		outPile = deck.getOutPileReference();
		card = new Card(1);
	}
	
	@Test
	public void TestReturnsArrayLists() {
		assertTrue(inPile instanceof ArrayList<Card>);
		assertTrue(outPile instanceof ArrayList<Card>);
	}
	
	@Test
	public void TestEmptyOnInit() {
		assertTrue(inPile.size() == 0);
		assertTrue(outPile.size() == 0);
	}
	
	@Test
	public void TestGiveCard() {
		deck.giveCard(card);
		assertTrue(inPile.size() == 1);
		assertTrue(inPile.get(0) == card);
	}
	
	@Test
	public void TestDealCard() {
		deck.dealCard(card);
		assertTrue(outPile.size() == 1);
		assertTrue(outPile.get(0) == card);
	}
	
	@Test
	public void TestCardThroughDeck() {
		deck.giveCard(card);
		assertTrue(outPile.size() == 0);
		deck.inPileToMid();
		deck.midPileToOut();
		assertTrue(outPile.size() == 1);
		assertTrue(outPile.get(0) == card);
		assertTrue(inPile.size() == 0);
	}
	
	@Test
	public void TestGetCard() {
		assertTrue(outPile.size() == 0);
		deck.dealCard(card);
		assertTrue(outPile.size() == 1);
		Card card2 = deck.getCard();
		assertTrue(outPile.size() == 0);
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
		assertTrue(outPile.size() == 0);
		assertTrue(inPile.size() == 0);
		assertTrue(card3.getValue() == card.getValue());
		assertTrue(card4.getValue() == card2.getValue());
	}
	
	
	
}