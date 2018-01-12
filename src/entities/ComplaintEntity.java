package entities;

import java.io.Serializable;

/**
 *this class represents a complaint entity  
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 */
public class ComplaintEntity implements Serializable{
	public enum Status{processing,handled};
		
	private int orderID;
	private String description;
	private Status status;
	
	/**
	 * class contructor for ComplaintEntity
	 * 
	 * @param orderID
	 * @param description
	 * @param status
	 */
	public ComplaintEntity(int orderID, String description, Status status) {
		this.orderID = orderID;
		this.description = description;
		this.status = status;
	}

	/**
	 *Getter for the orderID
	 * @return the orderID
	 */
	public int getOrderID() {
		return orderID;
	}

	/**
	 *Setter for the orderID
	 * @param orderID the orderID to set
	 */
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	/**
	 *Getter for the description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *Setter for the description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *Getter for the status
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 *Setter for the status
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
