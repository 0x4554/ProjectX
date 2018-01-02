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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerMenuController implements Initializable{
	
	final public static int DEFAULT_PORT = 5555;
	//@FXML private AnchorPane root;
	
	private Parameters params;
	private Stage primaryStage;
	
	private Client clnt;
	
	//*buttons of the customer menu*//
	@FXML private Label mmlbl;
	@FXML private Button ordBtn;
	@FXML private Button updeatilsBtn;
	@FXML private Button viewcatBtn;
	@FXML private Button watchaccBtn;
	@FXML private Button complBtn;
	@FXML private Button backBtn;
	
	public CustomerMenuController(Client clnt)
	{
		this.clnt=clnt;
	}
	
	public CustomerMenuController()
	{
		
	}
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	public void showCustomerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("CustomerMenu.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerMenuController Cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		Cmc.setConnectionData(this.clnt);
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
		int n;
		int a;
		int b;
		//kuhlkjhlkuh;u;oiu
	}			
	
	//*Open  Account details  menu from customer main menu*//
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
		
		//*Open  Update details Window from customer main menu*//
				public void enterToUpdateDetails(ActionEvent event) throws IOException {
					 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					 FXMLLoader loader = new FXMLLoader();
					 Parent root = loader.load(getClass().getResource("UpdateAccount.fxml").openStream());
					 UpdateAccountController upac= loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
				//	 ord.setConnectionData(DEFAULT_PORT, this);
					Stage primaryStage=new Stage();
					Scene scene=new Scene(root);
					primaryStage.setTitle("Details");
					primaryStage.setScene(scene);
					primaryStage.show();
				}			
		
		/*jhkjjh*/
				public void backToMainMenu(ActionEvent event) throws IOException	//when click "Back" return to main menu
				{
					((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					//this.main.showMainMenu(event);
				}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			
		}

		
}
