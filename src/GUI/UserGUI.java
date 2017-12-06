package GUI;

import java.io.IOException;
import java.io.InputStreamReader;

import client.User;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import ocsf.client.*;

import javafx.*;

public class UserGUI extends Application {
	
	final public static int DEFAULT_PORT = 5555;
	
	private String id="1234";
	private User usr;
	
	@FXML private TextField srchIDfld;
	@FXML private AnchorPane root;
	@FXML private Label srchLbl;
	@FXML private Label prdLbl;
	@FXML private Button srchProd;
	
	public UserGUI() {super();}
	
	public UserGUI(String host,int port) throws IOException {
		usr=new User(host,port);
	}		
	
	/**
	 * method for searching for an item in the data base
	 */	
	
	
	private void accept() {
		// TODO Auto-generated method stub
	
		try {
		usr.handleMessageFromClientUI(this.id);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void insertedID() throws IOException {	
		this.id = srchIDfld.getText();
		 UserGUI chat= new UserGUI("localhost", DEFAULT_PORT);
	      chat.accept();  //Wait for console data	
	}				


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root= FXMLLoader.load(getClass().getResource("ClientGUI.fxml"));
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) throws IOException {
		String host = "";
	    int port = 0;  //The port number		
	    
	    launch(args);
	    
	    try
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	    }
	  /* UserGUI chat= new UserGUI(host, DEFAULT_PORT);
	      chat.accept();  //Wait for console data	*/			
		
	}		

}
