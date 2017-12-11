package GUI;

import java.io.IOException;

import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import client.User;
//import gui.StudentFormController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import logic.Test;
import javafx.scene.Node;

import ocsf.client.*;

import javafx.*;

public class MainBoundary extends Application {
	
	final public static int DEFAULT_PORT = 5555;
	
	private String id="";
	private String host="";
	ProductFromDBBoundary pdb;

	
	@FXML private TextField srchIDfld;
	@FXML private AnchorPane root;
	@FXML private Label srchLbl;
	@FXML private Label prdLbl;
	@FXML private Button srchProd;
	
	
	@FXML private Button insrtBtn;
	@FXML private Button mnuFndBtn;
	@FXML private Label myFlwrLbl;

/*	@FXML private Label rsltIDLbl;
	@FXML private Label rsltNmLbl;
	@FXML private Label rsltTpLbl;
	@FXML private Button srchagnBtn;		*/
	
	@FXML private Button okerrBtn;
	@FXML private Label errMsgLbl;
	@FXML private Label errLbl;
	
	
	public void setID(String s) {
		this.id=s;
	}
	
	public void setHost(String s) {
		this.host=s;
	}

	
	public void searchProductID(ActionEvent event) throws IOException {	
		
		if(srchIDfld.getText().trim().isEmpty())  {
			
			Stage secondaryStage=new Stage();
			Parent root= FXMLLoader.load(getClass().getResource("ErrorInputGUI.fxml"));
			Scene scene=new Scene(root);
			secondaryStage.setTitle("Error");
			secondaryStage.setScene(scene);
			secondaryStage.show();
			}
			
			
		else {
			this.setID(srchIDfld.getText());
			User chat = new User("localhost", DEFAULT_PORT,this.id,2);
			chat.accept(); 	 //Wait for console data
			
			ArrayList <String> dtls=null;
			dtls=chat.fromSrvr;

			
			pdb=new ProductFromDBBoundary();
			pdb.showProductDetails(event);
			
	
		}
	}	
	
	public void showSearchResults(ActionEvent event) throws IOException {
		pdb.showProductDetails(event);
	}
	
	
	public void hideError(ActionEvent event) {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	}
	
	
	public void searchAgain(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	      Stage primaryStage=new Stage();
	      Parent root= FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Product");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	
	public void searchProduct(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		Parent root= FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}								

	public void insertProduct(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 Parent root= FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Search for Product");
			primaryStage.setScene(scene);
			primaryStage.show();
		 
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		Parent root= FXMLLoader.load(getClass().getResource("MenuGUI.fxml"));
		
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Main Menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	/**
	 * Main method, getting host from command prompt or choosing the default host
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String host = "";
	    int port = 0;  //The port number		
	    
	    launch(args);
	    
	    try
	    {
	      host=args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	    }			
		
	}		

}
