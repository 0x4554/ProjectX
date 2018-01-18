package entities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import logic.FilesConverter;

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
	private byte [] file;
	private String storeReply;
	private Double compensation;
	
	/**
	 * getter for the store's reply method
	 * 
	 * @return
	 */
	public String getStoreReply() {
		return storeReply;
	}
	
	/**setter for the storeReply method
	 * 
	 * @param storeReply
	 */
	public void setStoreReply(String storeReply) {
		this.storeReply = storeReply;
	}

	
	/**
	 * getter for the compensation for the customer
	 * 
	 * @return
	 */
	public Double getCompensation() {
		return compensation;
	}

	/**
	 * setter for the compensation method
	 * 
	 * @param compensation
	 */
	public void setCompensation(Double compensation) {
		this.compensation = compensation;
	}
	
	
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
	
	/**
	 * setter for the Object's file
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void setFile(String filePath) throws IOException {
		File file = new File (filePath);
		this.file = FilesConverter.convertFileToByteArray(file);
	}

	/**
	 * getter for the Object's file
	 * 
	 * @return
	 */
	public byte[] getFile() {
		return this.file;
	}
	
}
