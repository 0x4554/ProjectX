package GUI;

import java.io.IOException;

import client.User;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserGUI extends Application {
	
	private static final int DEFAULT_PORT = 5555;
	
	@FXML
	private TextField srchIDfld;
	@FXML
	private AnchorPane root;
	
	public UserGUI(String host,int port) throws IOException {
		User usr=new User(host,port);
		 
	}
	
	/**
	 * method for searching for an item in the data base
	 */
/*	public String idToSearch() {
		String str=srchIDfld.getText();
		System.out.println("Tal the.... ha! gayyyyy" + str);
		
		return str;
	}	*/
	
	
	private void accept() {
		// TODO Auto-generated method stub
		String str=srchIDfld.getText();
		System.out.println("<User> Tal the.... ha! gayyyyy" + str);
		
		//return str;
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root= FXMLLoader.load(getClass().getResource("Fgui.fxml"));
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) throws IOException {
		String host = "";
	    int port = 0;  //The port number

	    try
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "10.10.6.116";
	    }
	   UserGUI chat= new UserGUI(host, DEFAULT_PORT);
	    chat.accept();  //Wait for console data
		
		launch(args);
		
	}

	
}
