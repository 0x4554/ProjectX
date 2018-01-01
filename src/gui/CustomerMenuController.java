package gui;
import java.io.IOException;

import client.Client;
import javafx.application.Application.Parameters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerMenuController {
	
	final public static int DEFAULT_PORT = 5555;
	//@FXML private AnchorPane root;
	
	private Parameters params;
	private Stage primaryStage;
	
	private int port;
	private Client clnt;
	
	//*buttons of the customer menu*//
	@FXML private Label mmlbl;
	@FXML private Button ordBtn;
	@FXML private Button updeatilsBtn;
	@FXML private Button viewcatBtn;
	@FXML private Button watchaccBtn;
	@FXML private Button complBtn;
	@FXML private Button backBtn;
	
	public CustomerMenuController(int port,Client clnt)
	{
		this.port=port;
		this.clnt=clnt;
	}
	public void setConnectionData(int port,Client clnt)
	{
		this.port=port;
		this.clnt=clnt;
	}
	public void showCustomerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("CustomerMenuController.fxml").openStream());
		 CustomerMenuController Cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		 Cmc.setConnectionData(DEFAULT_PORT,this.clnt);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	//*Open order menu from customer main menu*//
	public void enterToOrder(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("VOrder.fxml").openStream());
		 OrderController ord = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
	//	 ord.setConnectionData(DEFAULT_PORT, this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	//*Open  catalog  menu from customer main menu*//
	public void enterCatalog(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("Catalog.fxml").openStream());
		 CatalogController catg = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
	//	 ord.setConnectionData(DEFAULT_PORT, this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	//*Open  catalog  menu from customer main menu*//
		public void enterToAccount(ActionEvent event) throws IOException {
			 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			 FXMLLoader loader = new FXMLLoader();
			 Parent root = loader.load(getClass().getResource("AccountD.fxml").openStream());
			 AccountController acc= loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		//	 ord.setConnectionData(DEFAULT_PORT, this);
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			primaryStage.setTitle("Account details");
			primaryStage.setScene(scene);
			primaryStage.show();
		}			
		
		
		
		
}
