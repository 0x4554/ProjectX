package logic;

import java.io.Serializable;

/**
 *  This class will be used for sending data between the client and server
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 */
public class MessageToSend implements Serializable {

	private String operation;
	private Object message;


	/**
	 *constructor for the MessageToSend.java class
	 * @param operation
	 * @param message
	 */
	public MessageToSend( Object message,String operation) {

		this.operation = operation;
		this.message = message;
	}
	
	
	/**
	 *Getter for the operation
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}
	
	
	/**
	 *Getter for the message
	 * @return the message
	 */
	public Object getMessage() {
		return message;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}


	public void setMessage(Object message) {
		this.message = message;
	}
	
}
