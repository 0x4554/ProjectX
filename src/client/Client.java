package client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import gui.MainBoundary;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.stage.Stage;
//import common.*;
import ocsf.client.AbstractClient;

import ocsf.client.*;

public class Client extends AbstractClient {
	
	private String fromUI;
	private String username;
//	private String operation;
	private Object messageFromServer;
	private  Boolean confirmationFromServer;
	private  ArrayList<String> ArrayListFromSrvr=null;
	private String stringFromServer;
	
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

	///Instance methods ************************************************
	
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	@Override
	public void handleMessageFromServer(Object msg)	//Receive the message sent from the server
	{
		this.messageFromServer =msg;	//save the message 
		confirmationFromServer = true;
	}
	

	  /**
	   * This method handles all data coming from the UI            
	   *
	   * @param message The message from the UI.    
	   */
	  public void handleMessageFromClientUI(String message)  
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
	  public void setDataFromUI(String data,String operation)
	  {
		  this.fromUI= data;
		//  this.operation=op;
		  this.fromUI = operation+ this.fromUI;	//set the data together with the wanted operation
	  }
	  
	  /**
	   * This method recognizes the wanted operation and calls the handleMessageFromClientUI to send the data to the server.
	   */
		public void accept() {
			// TODO Auto-generated method stub
//			if(this.operation == -1) {
//				this.fromUI = "close "+ this.fromUI;
//			}
//			if(this.operation == 1) {
//				this.fromUI = "login "+this.fromUI;
//			}
//			if(this.operation==2) {
//				this.fromUI="create "+this.fromUI;
//			}
//			if(this.operation==3) {
//				this.fromUI="find "+this.fromUI;
//			}
//			if(this.operation==4) {						//for create new account
//				this.fromUI="create new account";
//			}
			
			try {
			this.handleMessageFromClientUI(this.fromUI);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
		/**
		 * this method sends file to the server
		 * 
		 * @param portNo - port for working in front of the server
		 * @param filePath - location of the file in client side
		 * @throws IOException - IOException may be thrown
		 */
		public void uploadFileToServer(int portNo,String filePath) throws IOException
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
					e.printStackTrace();
				}
				finally {
					if (serverSocket != null) serverSocket.close();
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
	   * This method returns the message from the server as a String
	   */
	  public String getStringFromServer()	//method for when the message form the server is a String
	  {
		  String retMessage;
		  this.stringFromServer = (String)this.messageFromServer;
		  retMessage = new String(this.stringFromServer);
		  return retMessage;
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
	  
	  


}	