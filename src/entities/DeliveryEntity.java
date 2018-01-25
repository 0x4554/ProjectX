package entities;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class represents the delivery entity
 * DeliveryEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class DeliveryEntity implements Serializable{
	private int OrderID;				 /**the foreign key for the order delivery**/
	private String deliveryAddress;
	private String recipientName;
	private String phoneNumber;
	private final static Double deliveryPrice = 20.0;
	private Timestamp deliveryTimestamp;
	
	
	/**
	 * Constructor for the DeliveryEntity.java class
	 * @param deliveryAddress
	 * @param recipientName
	 * @param phoneNumber
	 * @param deliveryTimestamp
	 */
	public DeliveryEntity( String deliveryAddress, String recipientName, String phoneNumber, Timestamp deliveryTimestamp) {
		super();
		this.deliveryAddress = deliveryAddress;
		this.recipientName = recipientName;
		this.phoneNumber = phoneNumber;
		this.deliveryTimestamp = deliveryTimestamp;
	}
	
	
	/**
	 * Getter for the deliveryTimestamp
	 * @return the deliveryTimestamp
	 */
	public Timestamp getDeliveryTimestamp() {
		return deliveryTimestamp;
	}


	/**
	 * Setter for the deliveryTimestamp
	 * @param deliveryTimestamp the deliveryTimestamp to set
	 */
	public void setDeliveryTimestamp(Timestamp deliveryTimestamp) {
		this.deliveryTimestamp = deliveryTimestamp;
	}


	/**
	 * Getter for the orderID
	 * @return the orderID
	 */
	public int getOrderID() {
		return OrderID;
	}
	/**
	 * Setter for the orderID
	 * @param orderID the orderID to set
	 */
	public void setOrderID(int orderID) {
		OrderID = orderID;
	}
	/**
	 * Getter for the deliveryAddress
	 * @return the deliveryAddress
	 */
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	/**
	 * Setter for the deliveryAddress
	 * @param deliveryAddress the deliveryAddress to set
	 */
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	/**
	 * Getter for the recipientName
	 * @return the recipientName
	 */
	public String getRecipientName() {
		return recipientName;
	}
	/**
	 * Setter for the recipientName
	 * @param recipientName the recipientName to set
	 */
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	/**
	 * Getter for the deliveryPrice
	 * @return the deliveryPrice
	 */
	public static Double getDeliveryPrice() {
		return deliveryPrice;
	}
	/**
	 * Getter for the phoneNumber
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * Setter for the phoneNumber
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * Getter for the deliveryprice
	 * @return the deliveryprice
	 */
	public static Double getDeliveryprice() {
		return deliveryPrice;
	}
	
	
}
