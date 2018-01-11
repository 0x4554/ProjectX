package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import javafx.application.Application.Parameters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CatalogController implements Initializable{
	
	private Parameters params;
	private Stage primaryStage;

	//*buttons of the customer menu*//
		@FXML private Label pro;
		@FXML private Button cartBtn;
		@FXML private Button backBtn;
		private CustomerMenuController cstmc;
	/**
	 * A necessary constructor for the App
	 */
	public CatalogController()
	{
		
	}
	public void setConnectionData(CustomerMenuController cmc) {
		this.cstmc=cmc;
	}

	public void AddItemToCart(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("CustomerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void bckToMainMenu(ActionEvent event)throws IOException{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cstmc.showCustomerMenu();									       //open previous menu
		return;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}
