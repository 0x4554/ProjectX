package client;

import java.io.IOException;
import java.util.ArrayList;

import entities.StoreEntity;
import logic.MessageToSend;
import ocsf.client.AbstractClient;

/**
 * This class is used for communication between the Client and the Server
 * Client.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class Client extends AbstractClient {
	
	private MessageToSend msg;
	private String username;
	private Object messageFromServer;
	private  Boolean confirmationFromServer;
	private  ArrayList<String> ArrayListFromSrvr=null;
	private ArrayList<StoreEntity> arrayListOfStoreEntityFromServer;
	
	/**
	 * This the the static object for holding the connected client to be used when sending data to the server , or for getting data received from the server
	 */
	private static Client clientConnection;		//the static connected client to be used every time data is to be transfered to the server;
	
	
	/**
	 * Getter for the clientConnection
	 * @return the clientConnection
	 */
	public static Client getClientConnection() {
		return clientConnection;
	}

	/**
	 * Setter for the clientConnection
	 * @param clientConnection the clientConnection to set
	 */
	public static void setClientConnection(Client clientConnection) {
		Client.clientConnection = clientConnection;
	}

	/**
	 * This is the constructor for the client
	 * the connection is opened using the openConnection from the ocsf
	 * @param host	the IP of the server
	 * @param port	the port for the connection
	 * @param username	the userName of the client
	 * @throws IOException	throw if there is connection error
	 */
	public Client(String host, int port, String username) throws IOException {
		super(host, port); //Call the superclass constructor
		this.username = username; //save the userName 
		confirmationFromServer = false; //set the confirmation from the server to false --> changed to true when server replies after conducting an action
		openConnection(); //connect to server
	}
	
	/**
	 * This method saves the client's UserName
	 * @param s the user name
	 */
	public void setClientUserName(String s) {
		this.username=s;
	}

	///Instance methods ************************************************
	
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	@Override
	public void handleMessageFromServer(Object msg)	//Receive the message sent from the server
	{
		this.msg=(MessageToSend)msg;
		confirmationFromServer = true;
	}
	

	  /**
	   * This method handles all data coming from the UI            
	   *
	   * @param message The message from the UI.    
	   */
	  public void handleMessageFromClientUI(Object message)  
	  {
	    try
	    {
	    	sendToServer(message);
	    }
	    catch(IOException e)
	    {
	      quit();
	    }
	  }
	  
	  /**
	   * This method sets the requested data and the wanted operation
	   * 
	   * @param msg	the message to send
	   */
	  public void setDataFromUI(MessageToSend msg)
	  {
		  this.msg=msg;
	  }
	  
	  /**
	   * This method recognizes the wanted operation and calls the handleMessageFromClientUI to send the data to the server.
	   */
		public void accept() {
			// TODO Auto-generated method stub
			
			try {
			this.handleMessageFromClientUI(this.msg);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
	  
	  /**
	   * This method terminates the client.
	   */
	  public void quit()
	  {
	    try
	    {
	      closeConnection();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	  }
	  
	  /**
	   * This method returns the message from the server as an ArrayList
	   */
	  public ArrayList<String> getArrayListfromSrvr() throws InterruptedException	//method for when the message returned from the server is an ArrayList
	  {
		  ArrayList<String>dtls=new ArrayList<String>();
		  this.ArrayListFromSrvr = (ArrayList<String>)this.messageFromServer;
		for(String str:this.ArrayListFromSrvr) 
				dtls.add((String)str);
				
		return dtls;
	  }
	  

	  
	  /**
	   * This method return the message from the server as an ArrayList of StoreEntities
	   * @return	ArrayList of StoreEntities
	   */
	  public ArrayList<StoreEntity> getArrayListOfStoreEntityFromServer()
	  {
		  ArrayList<StoreEntity>dtls=new ArrayList<StoreEntity>();
		  this.arrayListOfStoreEntityFromServer = (ArrayList<StoreEntity>) this.messageFromServer;
		for(StoreEntity store:this.arrayListOfStoreEntityFromServer) 
				dtls.add((StoreEntity)store);
				
		return dtls;
	  }
	  
	  /**
	   * This method returns the confirmation from the server
	   * true if message received
	   */
	  public Boolean getConfirmationFromServer()
	  {
		  return confirmationFromServer;
	  }
	  /**
	   * This method sets the confirmation from the server to false after confirmation received
	   * true if message received
	   */
	  public void setConfirmationFromServer()
	  {
		  confirmationFromServer=false;
	  }
	  
	  /**
	   * This method returns the userName of the connection
	   * @return
	   */
	  public String getUsername()
	  {
		  return this.username;
	  }

	  /**
	   * Getter for the message received from the server
	   * @return	message received from server
	   */
	public MessageToSend getMessageFromServer() {
		return this.msg;
	}
	  
	  


}	