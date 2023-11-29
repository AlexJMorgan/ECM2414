import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.ArrayList;


/**
*Test class for CardGame using JUnit4.10.
*@author Daniel and Alex
*@version 1.0
*/
public class CardGameTest {
	
	private final PrintStream stdout = System.out;
	private ByteArrayOutputStream outputStream;
	
	
	@Before
	public void GetClearOutputStream() {
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
	}
	
	@Test
	public void TestReadMissingPack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\noPack.txt".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			CardGame.ReadPack(4);
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Error finding file.";
		assertTrue(exceptionThrown);
		assertTrue(generatedOutput.contains(expectedOutput));
	}
	
	
	@Test
	public void TestReadPack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\smallPack.txt".getBytes());
		System.setIn(mockInput);
		int[] expectedValues =  {1,2,2,2,1,1,2,1};
		ArrayList<Card> cards = CardGame.ReadPack(1);
		for (int i =0; i<8; i++) {
			assertTrue(cards.get(i).getValue() == expectedValues[i]);
		}
		String generatedOutput = outputStream.toString();
		assertTrue(generatedOutput.contains("Please enter location of pack to load:"));
		
	}
	
	@Test
	public void TestReadTxtlessPack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\smallPack".getBytes());
		System.setIn(mockInput);
		int[] expectedValues =  {1,2,2,2,1,1,2,1};
		ArrayList<Card> cards = CardGame.ReadPack(1);
		for (int i =0; i<8; i++) {
			assertTrue(cards.get(i).getValue() == expectedValues[i]);
		}
		String generatedOutput = outputStream.toString();
		assertTrue(generatedOutput.contains("Please enter location of pack to load:"));
		
	}
	
	@Test
	public void TestReadWrongSizePack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\wrongSizePack.txt".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			CardGame.ReadPack(1);
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Pack size invalid";
		assertTrue(exceptionThrown);
		assertTrue(generatedOutput.contains(expectedOutput));
	}
	
	
	@Test
	public void TestReadEmptyPack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\emptyPack.txt".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			CardGame.ReadPack(1);
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Pack size invalid";
		assertTrue(exceptionThrown);
		assertTrue(generatedOutput.contains(expectedOutput));
	}
	
	@Test
	public void TestReadInvalidValuesPack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\invalidPack.txt".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			CardGame.ReadPack(1);
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Card values must be non negative integers.";
		assertTrue(exceptionThrown);
		assertTrue(generatedOutput.contains(expectedOutput));
	}
	
	@Test
	public void TestReadNegativeValuesPack() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("TestPacks\\negativeValuesPack.txt".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			CardGame.ReadPack(1);
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Card values must be non negative integers.";
		assertTrue(exceptionThrown);
		assertTrue(generatedOutput.contains(expectedOutput));
	}
	
	@Test
	public void TestGetN() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("2".getBytes());
		System.setIn(mockInput);
		int generatedN = CardGame.GetN();
		assertTrue(generatedN == 2);
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Please enter the number of players:";
		assertTrue(generatedOutput.contains(expectedOutput));
	}
	
	@Test
	public void TestNoGetN() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("\n".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			int generatedN = CardGame.GetN();
								
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Please enter a integer.";
		assertTrue(generatedOutput.contains(expectedOutput));
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void TestNotIntGetN() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("a".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			int generatedN = CardGame.GetN();
								
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Please enter a integer.";
		assertTrue(generatedOutput.contains(expectedOutput));
		assertTrue(exceptionThrown);
	}
		
	@Test
	public void TestNegativeIntGetN() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("-1".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			int generatedN = CardGame.GetN();
								
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Please enter a valid integer.";
		assertTrue(generatedOutput.contains(expectedOutput));
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void TestInvalidIntGetN() {
		ByteArrayInputStream mockInput = new ByteArrayInputStream("1".getBytes());
		System.setIn(mockInput);
		boolean exceptionThrown = false;
		try {
			int generatedN = CardGame.GetN();
								
		} catch (NoSuchElementException e) {exceptionThrown = true;}
		String generatedOutput = outputStream.toString();
		String expectedOutput = "Please enter a valid integer.";
		assertTrue(generatedOutput.contains(expectedOutput));
		assertTrue(exceptionThrown);
	}
	
	
	

	
	
}