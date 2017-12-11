package client;

import java.io.IOException;
import java.util.ArrayList;

import GUI.UserBoundary;
import javafx.fxml.FXML;
import javafx.stage.Stage;
//import common.*;
import ocsf.client.AbstractClient;

import ocsf.client.*;

public class User extends AbstractClient {
	
	private String fromUI;
	private  ArrayList<String> fromSrvr;
	//ChatIF clientUI; 
	
	public User(String host, int port,Object obj) throws IOException 
		  {
		    super(host, port); 			//Call the superclass constructor
		    fromUI=(String)obj;
		//    this.clientUI = clientUI;
		    openConnection();
		  }
	
	//Instance methods ************************************************
    
	public void setDataFromSever(ArrayList<String> al) {
		this.fromSrvr=al;
	}
	
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	@Override
	  public void handleMessageFromServer(Object msg) 
	  {
	    this.setDataFromSever((ArrayList<String>)msg);
	  }
	  
	  public  ArrayList<String> getValuesFromServer(){
		  return this.fromSrvr;
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
	    /* clientUI.display
	        ("Could not send message to server.  Terminating client.");			*/
	      quit();
	    }
	  }
	  
	  
		public void accept() {
			// TODO Auto-generated method stub
		
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

}
