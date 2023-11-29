import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

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
}