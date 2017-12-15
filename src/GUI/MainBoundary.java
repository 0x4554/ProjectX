package GUI;

import java.io.IOException;


import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
//import logic.Test;
import javafx.scene.Node;

import ocsf.client.*;

import javafx.*;

public class MainBoundary extends Application {
	
	final public static int DEFAULT_PORT = 5555;
	
	private String id="";
	private String host="";
	private ProductFromDBBoundary pdb;

	
	@FXML private TextField srchIDfld;
	@FXML private AnchorPane root;
	@FXML private Label srchLbl;
	@FXML private Label prdLbl;
	@FXML private Button srchProdBtn;
	
	
	@FXML private Button insrtBtn;
	@FXML private Button mnuFndBtn;
	@FXML private Label myFlwrLbl;

	
	@FXML private Button okerrBtn;
	@FXML private Label errMsgLbl;
	@FXML private Label errLbl;
	
	@FXML private Button crtBtn;
	@FXML private TextField idFld;
	@FXML private TextField nmFld;
	@FXML private TextField typFld;
	
	@FXML private Button bckMnuBtn;
	
	private CreateProductBoundary cpd;
	private ProductFromDBBoundary pfdb;
	
	public MainBoundary() {
		this.cpd=new CreateProductBoundary();
		this.pfdb=new ProductFromDBBoundary();
	}
	
	
	public void setID(String s) {
		this.id=s;
	}
	
	public void setHost(String s) {
		this.host=s;
	}

	
	public void backMenu(ActionEvent event) throws IOException{
		this.pfdb.backToMainMenu(event);
	}
	
	public void searchProductID(ActionEvent event) throws IOException, InterruptedException {		//if pressed "Search"
		
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
			ArrayList<String> dets=null;
			Thread.sleep(1000);
			dets=chat.getfromSrvr();
				
			/*try {
				//System.out.println("Failed loading array exception");
				dets=new ArrayList<String>();
				for(Object obj:chat.getfromSrvr())
					dets.add((String)obj);*/
			
			if(!(dets.isEmpty()))
					{
						((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
						
						Stage secondaryStage=new Stage();
						FXMLLoader loader = new FXMLLoader();	//create an FXMLLoader
						Parent root= loader.load(getClass().getResource("ProductFromDBGUI.fxml").openStream());
						Scene scene=new Scene(root);
						
						ProductFromDBBoundary pdb = (ProductFromDBBoundary)loader.getController();	//get the FXMLLoader controller for use in the pdb (for using it's functions as a controller)
						
						pdb.setMainBoundary(this);	//set reference of (this) mainBoundary to the pdb controller
						pdb.setLabels(dets);	//set the labels as returned from the DB
						
						secondaryStage.setTitle("Product Details");
						secondaryStage.setScene(scene);
						secondaryStage.show();	//show ProductFromDBBoundary
					}
			else
			{
				Stage secondaryStage=new Stage();
				Parent root= FXMLLoader.load(getClass().getResource("ErrorInputGUI.fxml"));
				Scene scene=new Scene(root);
				secondaryStage.setTitle("Error");
				secondaryStage.setScene(scene);
				secondaryStage.show();
			}

		}
	}	
	
	
	public void hideError(ActionEvent event) {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	}
	
	
	public void searchAgain(ActionEvent event) throws IOException {		//load window for searching an item in the DB
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
		// FXMLLoader loader = new FXMLLoader();
		 Parent root = FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
//		Parent root= FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	

	public void showNewProductGUI(ActionEvent event) throws IOException {
		
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		cpd=new CreateProductBoundary();
		cpd.showNewProductGUI();
	}
	
	public void getNewData(ActionEvent event) throws IOException {
		String newData="";
		newData=newData+idFld.getText()+" "+nmFld.getText()+" "+typFld.getText();
		User chat = new User("localhost", DEFAULT_PORT,newData,1);
		chat.accept();
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
