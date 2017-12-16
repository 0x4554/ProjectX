package GUI;

import java.io.IOException;


import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import client.User;

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
import javafx.scene.Node;

import ocsf.client.*;

import javafx.*;

public class MainBoundary extends Application {
	
	final public static int DEFAULT_PORT = 5555;
	
	private String id="";
	private String host="";
	private Parameters params;
	private ProductFromDBBoundary pdb;
	private Stage primaryStage;

	
	@FXML private TextField srchIDfld;
	@FXML private AnchorPane root;
	@FXML private Label srchLbl;
	@FXML private Label prdLbl;
	@FXML private Button srchProdBtn;
	
	
	@FXML private Button okerrBtn;
	@FXML private Label errMsgLbl;
	@FXML private Label errLbl;
	
	
	
	private CreateProductBoundary cpd;
	private ProductFromDBBoundary pfdb;
	
	public MainBoundary() {

		this.pfdb=new ProductFromDBBoundary();
	}
	
	
	public void setID(String s) {
		this.id=s;
	}
	
	public void setHost(String parameters) {
		this.host=parameters;
	}

	
	public void searchProductID(ActionEvent event) throws IOException, InterruptedException {		//if pressed "Search"
		
		if(srchIDfld.getText().trim().isEmpty())  {	//check if the search field is empty
			
			GeneralMessageBoundary message = new GeneralMessageBoundary();
			message.showGeneralMessage("Search field is empty.\nPlease insert ID to search.");		//show a message 
			

			}
			
			
		else {
			this.setID(srchIDfld.getText());	//collect the ID entered
			User chat = new User(this.host, DEFAULT_PORT,this.id,2);
			chat.accept(); 	 //Wait for console data
			ArrayList<String> data=null;
			Thread.sleep(3000);			//wait for server's message
			data=chat.getArrayListfromSrvr();	//get the message returned from the server
				
			
			if(!(data.isEmpty()))	//check if product ID is found in the Data Base
					{
						((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
						
						this.pfdb=new ProductFromDBBoundary();
						this.pfdb.showProductDetails(data, this);	//send the data and main instance to the product view window
						
					}
			else	//if no such ID found show error
			{
				
				GeneralMessageBoundary message = new GeneralMessageBoundary();
				message.showGeneralMessage("There is no such ID in the DataBase.\nPlease try again.");		//show a message 
				
			}

		}
	}	
	
	
	public void hideError(ActionEvent event) {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	}
	
	
	
	public void searchProduct(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 Parent root = FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));	//load the search product page
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	

	public void showNewProductGUI(ActionEvent event) throws IOException {	//when click the add new product in main menu
		
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		cpd=new CreateProductBoundary(this.host,DEFAULT_PORT,this);
		cpd.showNewProductGUI();	//call method to show the add new product menu
	}

	
	public void showMainMenu(ActionEvent event) throws IOException	//show the main menu
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		Stage primaryStage=new Stage();
		Parent root= FXMLLoader.load(getClass().getResource("MenuGUI.fxml"));
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		params=getParameters();
		List<String> lst=params.getRaw();
		 try
		    {
			 this.setHost(lst.get(0));		//set the host IP from command prompt input  
			 }
		    catch(IndexOutOfBoundsException e)
		    {
		      this.host = "localhost";						//set the host IP to localhost
		    }			
		
		this.primaryStage = primaryStage;
		Parent root= FXMLLoader.load(getClass().getResource("MenuGUI.fxml"));	//load the main menu page
		
		Scene scene=new Scene(root);
		
		this.primaryStage.setTitle("Main Menu");
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
	}
	
	
	
	/**
	 * Main method, getting host from command prompt or choosing the default host
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	    launch(args);
	}		

}
