import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter; 
import java.io.IOException;


/**
*Player Class to represent each player in the card game.
*Stores its hand of cards and the decks it draws/discards from.
*@author Daniel and Alex
*@version 1.0
*/

public class Player implements Runnable{

	private ArrayList<Card> cards;
	private int index;
	private int discardIndex;
	private File file;
	private Deck drawDeck;
	private Deck discardDeck;
	
	
	/**
	*Constructor method for Player class.
	*@param index			used to refer to the player in log messages and set preferred card value
	*@param discardIndex	used to refer to the discard deck in log messages
	*@param drawDeck		deck the player draws cards from
	*@param discardDeck		deck the player discards cards to
	*@param file			file reference where player outputs their moves
	*/
	public Player(int index, int discardIndex, Deck drawDeck, Deck discardDeck, File file) {
		this.cards = new ArrayList<>();
		this.index = index;
		this.discardIndex = discardIndex;
		this.drawDeck = drawDeck;
		this.discardDeck = discardDeck;
		this.file = file;
	}
	
	
	/**
	*Run method for Player Threads.
	*Draws and discards cards as required by the game.
	*Declares victory to CardGame, if player has a winning hand.
	*Can be interrupted if another player wins first.
	*Keeps a running log of activities in output file.
	*/
	public void run() {
		
		playerInitialHand();
		while (!checkWin()) {
			synchronized (discardDeck.inLock) {
				synchronized (drawDeck.outLock) {
					try {
						if (drawDeck.isEmpty()) {
							drawDeck.outLock.notify();
							drawDeck.outLock.wait();
						}
						if (Thread.interrupted()) {
							throw new InterruptedException();
						}
						
						draw();
						discard();
					} catch (InterruptedException e) {
						int winner = CardGame.getWinner();
						writeToFile("\nplayer "+winner+" has informed player "+index+" that player "+winner+" has won");
						writeToFile("\nplayer "+index+" exits");
						String cardValues = "";
						for (int i=0; i<4; i++) {
							cardValues += " " + cards.get(i).getValue();
						}
						writeToFile("\nplayer "+index+" hand:"+cardValues);
						return;
					}
				}
				
				discardDeck.inLock.notify();				
			}
			String cardValues = "";
			for (int i=0; i<4; i++) {
				cardValues += " " + cards.get(i).getValue();
			}
			writeToFile("\nplayer "+index+" current hand is"+cardValues);
		}
		CardGame.finish(index);
		writeToFile("\nplayer "+index+" wins");
		writeToFile("\nplayer "+index+" exits");
		String cardValues = "";
		for (int i=0; i<4; i++) {
			cardValues += " " + cards.get(i).getValue();
		}
		writeToFile("\nplayer "+index+" final hand is"+cardValues);
	}
	
	/**
	*Adds card to current hand.
	*This method is to be used when setting up the initial hand of each player, and when drawing a card.
	*@param card  the card to be added
	*/
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	*Checks if the player has four matching cards.
	*@return  true if player has won
	*/
	public boolean checkWin() {
		int val = cards.get(0).getValue();
		for (int i = 1; i<4; i++) {
			if (cards.get(i).getValue() != val) {
				return false;
			}
		}
		return true;		
	}
	
	/**
	*Writes the initial hand of player to the output file.
	*/
	public void playerInitialHand() {
		String initialHand = "";
		for (int i=0; i<4; i++) {
			initialHand += " " + cards.get(i).getValue();
		}
		writeToFile("player "+index+" initial hand is"+initialHand);
	}
	
	/**
	*Gets the top card from the drawDeck deck and adds it to the player's hand.
	*/
	public void draw() {
		Card card = drawDeck.getCard();
		writeToFile("\nplayer "+index+" draws a "+card.getValue()+" from deck "+index);
		cards.add(card);
	}
	
	/**
	*Discards a random card from the player's hand.
	*Discards the first card with a staleness over 4.
	*Otherwise will discard the first card in the hand.
	*Will not discard cards of preferred value.
	*/
	public void discard() {
		boolean decided = false;
		Card card = new Card(3000);
		
		for (int i = 0; i<4; i++) {
			card = cards.get(i);
			if (card.getValue() != index) {
				if (card.getStaleness() > 4) {
					decided = true;
					cards.remove(card);
					break;
				}
			} 
		}	
		if (!decided) {
			for (int i = 0; i<4; i++) {
				card = cards.get(i);
				if (card.getValue() != index) {
					decided = true;
					cards.remove(card);

					break;
				}
			}
		}
		card.resetStaleness();
		writeToFile("\nplayer "+index+" discards a "+card.getValue()+" to deck "+discardIndex);
		discardDeck.giveCard(card);
	}	

	/**
	*Writes the given message to the players output file.
	*@param msg		message to be written
	*/
	public void writeToFile(String msg) {
		try {
			FileWriter writer = new FileWriter(file.getName(), true);
		    writer.write(msg);
			writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}
	
	
	
	
	
	
}
