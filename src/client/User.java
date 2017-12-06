package client;

import java.io.IOException;

import GUI.UserGUI;
import javafx.fxml.FXML;
import javafx.stage.Stage;
//import common.*;
import ocsf.client.AbstractClient;
public class User extends AbstractClient {

	//ChatIF clientUI; 
	
	public User(String host, int port) throws IOException 
		  {
		    super(host, port); //Call the superclass constructor
		//    this.clientUI = clientUI;
		    openConnection();
		  }
	
	//Instance methods ************************************************
    
	
	
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	  public void handleMessageFromServer(Object msg) 
	  {
	    //clientUI.display(msg.toString());
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
