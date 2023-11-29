import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
*Class for deck
*Contains three piles, an in pile to recieve cards, 
*a middle pile to act as a buffer, and an out pile for
*cards to be taken from. This avoids deadlock scenarios.
*@author Daniel and Alex
*@version 1.0
*/

public class Deck {
	
	private ArrayList<Card> inPile = new ArrayList<>();
	private ArrayList<Card> midPile = new ArrayList<>();
	private ArrayList<Card> outPile = new ArrayList<>();
	public Object inLock = new Object();
	public Object midLock = new Object();
	public Object outLock = new Object();
	private Thread inDaemonThread;
	private Thread outDaemonThread;
	private File file;
	private int index;


	/**
	*Constructor method for the Deck class.
	*@param file	the file which the deck output will be written into
	*@param index	the index of the deck as used in file output messages
	*/
	public Deck(File file, int index) {
		this.file = file;
		this.index = index;
		InDaemon inDaemon = new InDaemon(this);
		inDaemonThread = new Thread(inDaemon);		
		OutDaemon outDaemon = new OutDaemon(this);
		outDaemonThread = new Thread(outDaemon);	
		inDaemonThread.start();
		outDaemonThread.start();
	}
		
	/**
	*Inserts card directly into out pile.
	*To be used during intial set up, when dealing out cards.
	*@param card	card to be added
	*/
	public void dealCard(Card card) {
		outPile.add(card);
	}
	
	/**
	*Adds card to deck.
	*@param card	card to be added
	*/
	public void giveCard(Card card) {
		inPile.add(card);	
	}
	
	/**
	*Moves cards from the in pile to the middle pile.
	*/
	public void inPileToMid() {
		midPile.addAll(inPile);
		inPile.clear();
	}
	
	/**
	*Moves cards from middle pile to out pile.
	*Clears the midPile.
	*/
	public void midPileToOut() {
		outPile.addAll(midPile);
		midPile.clear();
	}
		
	/**
	*Gets card from deck, and removes it.
	*@return	card object that is removed
	*/
	public Card getCard() {
		Card card = outPile.get(0);
		outPile.remove(card);
		return card;
	}
	
	/**
	*Checks if outPile is empty.
	*@return	boolean for if outPile is equal to 0
	*/
	public boolean isEmpty() {
		return outPile.size() == 0;	
		
	}
	
	/**
	*Checks if midPile is empty.
	*@return	boolean for if midPile is equal to 0
	*/
	public boolean isMidPileEmpty() {
		return midPile.size() == 0;
	}
	
	/**
	*Outputs the deck contents and cleans up daemon threads.
	*Interrupts the Daemon Threads once the game has finished.
	*Outputs current Deck contents into the deck file.
	*/
	public void printDeckContents() {
		inDaemonThread.interrupt();
		outDaemonThread.interrupt();
		
		String hand = "";
		ArrayList<Card> allCards = new ArrayList<>();
		allCards.addAll(inPile);
		allCards.addAll(outPile);
		allCards.addAll(midPile);
		
		for (int i = 0; i<allCards.size(); i++) {
			hand += " "+ allCards.get(i).getValue();
		}
		String msg = "deck" + index + " contents:" +hand;		
		
		try {
			FileWriter writer = new FileWriter(file.getName(), true);
		      	writer.write(msg);
		      	writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
		
	}
		
}

/**
*Class for moving cards from inPile to midPile.
*This prevents player threads from interracting with
*the same data structure as another player
*@author Daniel and Alex
*@version 1.0
*/
class InDaemon implements Runnable{

	private Deck deck;
	
	/**
	*Constructor for InDaemon.
	*@param deck	deck this thread manages
	*/
	public InDaemon(Deck deck) {
		this.deck = deck;
		
	}
	
	/**
	*Run method for InDaemon Threads.
	*Waits until a card is inserted into the inPile.
	*Moves the card from inPile to midPile.
	*/
	public void run() {
		
		synchronized (deck.inLock) {
			try {
				while (true) {
					deck.inLock.wait();
					synchronized (deck.midLock) {
						deck.inPileToMid();
						deck.midLock.notify();
					}
				}
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}
	
}

/**
*Class for moving cards from midPile to outPile.
*This prevents player threads from interracting with
*the same data structure as another player
*@author Daniel and Alex
*@version 1.0
*/
class OutDaemon implements Runnable{
	
	private Deck deck;
	
	/**
	*Constructor for OutDaemon.
	*@param deck	deck this thread manages
	*/
	public OutDaemon(Deck deck) {
		this.deck = deck;
	}
	
	/**
	*Run method for OutDaemon Threads.
	*Waits until notified the outPile is empty.
	*Moves cards from midPile to outPile.
	*/
	public void run() {
		synchronized (deck.outLock) {
			try {
				while (true) {
					deck.outLock.wait();
					synchronized (deck.midLock) {
						if (deck.isMidPileEmpty()) {
							deck.midLock.wait();
						}
						deck.midPileToOut();
					}
					deck.outLock.notify();
				}
			} catch (InterruptedException e) {
				return;
			}				
		}
	}
}



