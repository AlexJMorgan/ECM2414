/**
*Class for each card.
*@author Daniel
*@version 0.1
*/
public class Card {

	private int value;
	private int stale = 0;
	
	/**
	*Constructor method for the Card class.
	*@param value	value of the card.
	*@param stale	turns the card has been in the same hand
	*/
	public Card(int value) {
		this.value = value;
	}
	
	/**
	*Gets value for card
	*@return value
	*/
	public int getValue(){
		return value;	
	}
	
	/**
	*Gets staleness of card
	*@return stale	
	*/
	public int getStaleness() {
		return stale;
	}
	
	/**
	*Increments staleness by 1
	*/
	public void addStaleness(){
		this.stale ++;
	}
	
	/**
	*Resets staleness to 0
	*/
	public void resetStaleness() {
		this.stale = 0;
	}

}