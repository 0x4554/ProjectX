package entities;
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
public class DeliveryEntity {
	private String OrderID;				 /**the foreign key for the order delivery**/
	private String deliveryAddress;
	private String recipientName;
	private long phoneNumber;
	private final Double deliveryPrice = 20.0;
	private String deliveryDate;
	private String deliveryTime;
	
	/**
	 * Getter for the orderID
	 * @return the orderID
	 */
	public String getOrderID() {
		return OrderID;
	}
	/**
	 * Setter for the orderID
	 * @param orderID the orderID to set
	 */
	public void setOrderID(String orderID) {
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
	 * Getter for the phoneNumber
	 * @return the phoneNumber
	 */
	public long getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * Setter for the phoneNumber
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * Getter for the deliveryDate
	 * @return the deliveryDate
	 */
	public String getDeliveryDate() {
		return deliveryDate;
	}
	/**
	 * Setter for the deliveryDate
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	/**
	 * Getter for the deliveryTime
	 * @return the deliveryTime
	 */
	public String getDeliveryTime() {
		return deliveryTime;
	}
	/**
	 * Setter for the deliveryTime
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	/**
	 * Getter for the deliveryPrice
	 * @return the deliveryPrice
	 */
	public Double getDeliveryPrice() {
		return deliveryPrice;
	}
}
