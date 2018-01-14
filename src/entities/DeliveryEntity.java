package entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

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
	private Date deliveryDate;
	private Time deliveryTime;
	
	
	/**
	 * Constructor for the DeliveryEntity.java class
	 * @param orderID
	 * @param deliveryAddress
	 * @param recipientName
	 * @param phoneNumber
	 * @param deliveryDate
	 * @param deliveryTime
	 */
	public DeliveryEntity( String deliveryAddress, String recipientName, String phoneNumber, Date deliveryDate, Time deliveryTime) {
		super();
		this.deliveryAddress = deliveryAddress;
		this.recipientName = recipientName;
		this.phoneNumber = phoneNumber;
		this.deliveryDate = deliveryDate;
		this.deliveryTime = deliveryTime;
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
	 * Getter for the deliveryDate
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	/**
	 * Setter for the deliveryDate
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	/**
	 * Getter for the deliveryTime
	 * @return the deliveryTime
	 */
	public Time getDeliveryTime() {
		return deliveryTime;
	}
	/**
	 * Setter for the deliveryTime
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(Time deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	/**
	 * Getter for the deliveryprice
	 * @return the deliveryprice
	 */
	public static Double getDeliveryprice() {
		return deliveryPrice;
	}
	
	
}
