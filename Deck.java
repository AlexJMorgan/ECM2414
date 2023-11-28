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
*@version 0.1
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
	*Checks if the deck is empty
	*@return	true if the deck is empty, otherwise false
	*/
	public boolean outPileIsEmpty() {
		return outPile.size() == 0;
	}
	
	/**
	*Inserts card directly into out pile
	*To be used during intial set up, when dealing out cards
	*@param card	card to be added
	*/
	public void dealCard(Card card) {
		outPile.add(card);
	}
	
	/**
	*Adds card to deck
	*@param card	card to be added
	*/
	public void giveCard(Card card) {
		inPile.add(card);	
	}
	
	/**
	*Moves cards from in pile to middle pile
	*/
	public void inPileToMid() {
		midPile.addAll(inPile);
		inPile.clear();
	}
	
	/**
	*Moves cards from middle pile to out pile
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
	*Checks if outpile is empty.
	*@return	boolean for if outpile is equal to 0
	*/
	public boolean isEmpty() {
		return outPile.size() == 0;	
		
	}
	
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

class InDaemon implements Runnable{

	private Deck deck;
	
	public InDaemon(Deck deck) {
		this.deck = deck;
		
	}
	
	public void run() {
		
		synchronized (deck.inLock) {
			try {
				while (true) {
					deck.inLock.wait();
					synchronized (deck.midLock) {
						deck.inPileToMid();
					}
				}
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}
	
}

class OutDaemon implements Runnable{
	
	private Deck deck;
	
	public OutDaemon(Deck deck) {
		this.deck = deck;
	}
	
	public void run() {
		synchronized (deck.outLock) {
			try {
				while (true) {
					deck.outLock.wait();
					synchronized (deck.midLock) {
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



