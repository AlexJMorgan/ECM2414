import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;

public class DeckTest {
	
	private Deck deck;
	
	@Before
	public void CreateDeck() {
		File file = new File("BlankFile.txt");
		deck = new Deck(file);
	}
	
	@Test
	public void TestReturnsArrayLists() {
		ArrayList<Card> inPile = deck.getInPileReference();
		ArrayList<Card> outPile = deck.getOutPileReference();
		assertTrue(inPile instanceof ArrayList<Card>);
		assertTrue(outPile instanceof ArrayList<Card>);
	}
	
	@Test
	public void TestEmptyOnInit() {
		ArrayList<Card> inPile = deck.getInPileReference();
		ArrayList<Card> outPile = deck.getOutPileReference();
		assertTrue(inPile.size() == 0);
		assertTrue(outPile.size() == 0);
	}
}