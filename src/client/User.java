package client;

import java.io.IOException;
import java.util.ArrayList;

import GUI.MainBoundary;
import javafx.fxml.FXML;
import javafx.stage.Stage;
//import common.*;
import ocsf.client.AbstractClient;

import ocsf.client.*;

public class User extends AbstractClient {
	
	private String fromUI;
	private int operation;
	private Object messageFromServer;
	private  ArrayList<String> ArrayListFromSrvr=null;
	private String stringFromServer;
	//ChatIF clientUI; 
	
	public User(String host, int port,Object obj,int opr) throws IOException 
		  {
		    super(host, port); 			//Call the superclass constructor
		    fromUI=(String)obj;
		    operation=opr;
		//    this.clientUI = clientUI;
		    openConnection();
		  }
	
	//Instance methods ************************************************
	
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	@Override
	public void handleMessageFromServer(Object msg)	//Receive the message sent from the server
	{
		this.messageFromServer =msg;	//save the message 
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
	  
	  
		public void accept() {
			// TODO Auto-generated method stub
			if(this.operation==1) {
				this.fromUI="create "+this.fromUI;
			}
			if(this.operation==2) {
				this.fromUI="find "+this.fromUI;
			}
			
			
			try {
			this.handleMessageFromClientUI(this.fromUI);
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
	  
	  public ArrayList<String> getArrayListfromSrvr() throws InterruptedException	//method for when the message returned from the server is an ArrayList
	  {
		  ArrayList<String>dtls=new ArrayList<String>();
		  this.ArrayListFromSrvr = (ArrayList<String>)this.messageFromServer;
		for(String str:this.ArrayListFromSrvr) 
				dtls.add((String)str);
				
		return dtls;
	  }
	  
	  public String getStringFromServer()	//method for when the message form the server is a String
	  {
		  String retMessage;;
		  this.stringFromServer = (String)this.messageFromServer;
		  retMessage = new String(this.stringFromServer);
		  return retMessage;
	  }


}	
	
