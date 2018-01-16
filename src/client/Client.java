package client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import entities.StoreEntity;
import gui.MainBoundary;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import logic.MessageToSend;
//import common.*;
import ocsf.client.AbstractClient;

import ocsf.client.*;

public class Client extends AbstractClient {
	
	private MessageToSend msg;
//	private String fromUI;
	private String username;
//	private String operation;
	private Object messageFromServer;
	private  Boolean confirmationFromServer;
	private  ArrayList<String> ArrayListFromSrvr=null;
	private ArrayList<StoreEntity> arrayListOfStoreEntityFromServer;
	private String stringFromServer;
	
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
		//this.messageFromServer =m.getMessage();	//save the message 
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
	   * @param data	requested data
	   * @param op	wanted operation ends with ! : "<operation>!"
	   */
	  public void setDataFromUI(MessageToSend msg)
	  {
		  this.msg=msg;
//		  this.fromUI= data;
//		//  this.operation=op;
//		  this.fromUI = operation+ this.fromUI;	//set the data together with the wanted operation
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
		 * this method sends file to the server which supposed to save it
		 * 
		 * @param portNo - port for working in front of the server
		 * @param filePath - location of the file in client side
		 * @throws IOException - IOException may be thrown
		 * @throws InterruptedException 
		 */
	/*	public void uploadFileToServer(int portNo,String filePath) throws IOException, InterruptedException
		{
			FileInputStream fileInputStream = null;
			BufferedInputStream bufferedInputStream = null;

			OutputStream outputStream = null;
			ServerSocket serverSocket = null;
			Socket socket = null;

			//creating connection between sender and receiver
			try {
				
				serverSocket = new ServerSocket(portNo);
				System.out.println("Waiting for receiver...");

					try {
							
							socket = serverSocket.accept();
//							while(!this.getConfirmationFromServer())
//								Thread.sleep(100);
//							this.setConfirmationFromServer();
							System.out.println("Accepted connection : " + socket);
							//connection established successfully
		
							//creating object to send file
							File file = new File (filePath);
							byte [] byteArray  = new byte [(int)file.length()];
							fileInputStream = new FileInputStream(file);
							bufferedInputStream = new BufferedInputStream(fileInputStream);
							bufferedInputStream.read(byteArray,0,byteArray.length); // copied file into byteArray
		
							//sending file through socket
							outputStream = socket.getOutputStream();
							System.out.println("Sending " + filePath + "( size: " + byteArray.length + " bytes)");
							outputStream.write(byteArray,0,byteArray.length);			//copying byteArray to socket
							outputStream.flush();										//flushing socket
							System.out.println("Done.");								//file has been sent
						}
						finally {
							if (bufferedInputStream != null) bufferedInputStream.close();
							if (outputStream != null) bufferedInputStream.close();
							if (socket!=null) socket.close();
						}		
				} catch (IOException e) {
					
					// TODO Auto-generated catch block
					System.out.println("connection failed");
					e.printStackTrace();
				}
				finally {
					if (serverSocket != null) serverSocket.close();
				}
		}				*/
	  
		
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
	   * This method returns the username of the connection
	   * @return
	   */
	  public String getUsername()
	  {
		  return this.username;
	  }

	public MessageToSend getMessageFromServer() {
		// TODO Auto-generated method stub
		return this.msg;
	}
	  
	  


}	