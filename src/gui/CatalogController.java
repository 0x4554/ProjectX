package gui;

import java.io.IOException;

import client.Client;
import javafx.application.Application.Parameters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CatalogController {
	
	private Parameters params;
	private Stage primaryStage;
	private Client clnt;
	
	//*buttons of the customer menu*//
		@FXML private Label pro;
		@FXML private Button cartBtn;
		@FXML private Button backBtn;
	/**
	 * This method is the constructor for this class
	 * @param clnt	the connected client
	 */
	public CatalogController(Client clnt)
	{
		this.clnt=clnt;
	}
	
	/**
	 * A necessary constructor for the App
	 */
	public CatalogController()
	{
		
	}
	
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	
	public void AddItemToCart(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("CustomerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		cmc.setConnectionData(this.clnt);
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
}
