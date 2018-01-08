package entities;

import java.io.Serializable;

/**
 * This class represents the card added to the order
 * CardEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CardEntity implements Serializable{
	
	private String text;
	/**
	 * 
	 * Constructor for the CardEntity.java class
	 * @param txt
	 */
	public CardEntity(String txt)
	{
		this.text=txt;
	}

	/**
	 * Getter for the text
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for the text
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

}
