import org.junit.*;
import static org.junit.Assert.*;

public class CardTest {
	@Test
	public void TestInitialParams() {
		Card card = new Card(1);
		assertTrue(card.getValue() == 1);
		assertTrue(card.getStaleness() == 0);
	}
	
	@Test
	public void TestAddStaleness() {
		Card card = new Card(1);
		assertTrue(card.getStaleness() == 0);
		card.addStaleness();
		assertTrue(card.getStaleness() == 1);
		card.addStaleness();
		assertTrue(card.getStaleness() == 2);
	}
	
	@Test
	public void TestResetStaleness() {
		Card card = new Card(1);
		assertTrue(card.getStaleness() == 0);
		card.addStaleness();
		assertTrue(card.getStaleness() == 1);
		card.resetStaleness();
		assertTrue(card.getStaleness() == 0);
	}
}