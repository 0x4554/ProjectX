package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.CustomerEntity;
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
import logic.MessageToSend;
/**
 * This class is the controller for the customer main menu
 * 
 * CustomerMenuController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerMenuController extends MenuController implements Initializable {

	final public static int DEFAULT_PORT = 5555;

	private Parameters params;
	private LoginController logcon;

	//*buttons of the customer menu*//
	@FXML
	private Label mmlbl;
	@FXML
	private Button ordBtn;
	@FXML
	private Button updeatilsBtn;
	@FXML
	private Button viewcatBtn;
	@FXML
	private Button watchaccBtn;
	@FXML
	private Button cmplntBtn;
	@FXML
	private Button logoutBtn;

	/**
	 * This method is the constructor for this class
	 * 
	 * @param lc
	 *            the login controller
	 */
	public CustomerMenuController(LoginController lc) {
		this.logcon = lc;
	}

	/**
	 * A necessary constructor for the App
	 */
	public CustomerMenuController() {

	}

	/**
	 * Getter for the client connection
	 * 
	 * @return
	 */
	public Client getClient() {
		return Client.getClientConnection();
	}

	/**
	 * This method loads the customer menu boundary
	 * 
	 * @throws IOException
	 *             for the loader
	 */
	public void showMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());

		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		CustomerMenuController cmc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method loads the order menu from customer main menu
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void enterToOrder(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerOrderMenuBoundary.fxml").openStream());
		CustomerOrderController ord = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window
		ord.setConnectionData(this);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * The method open's the Catalog Window
	 * 
	 * @param event
	 *            on clicking "View Catalog" Button
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void enterCatalog(ActionEvent event) throws IOException, InterruptedException {
		((Node) event.getSource()).getScene().getWindow().hide(); //Hide Current window
		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/ViewCatalogBoundary.fxml").openStream()); //Load the fxml class
		CatalogController catl = loader.getController();
		catl.setPreviousController(this);
		catl.showCatalog(); //Call the method show catalog
		Stage primaryStage = new Stage(); //Set Stage->Show()
		Scene scene = new Scene(pRoot);
		primaryStage.setTitle("Zer-Li Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * This method loads the Account details menu from customer main menu*
	 * 
	 * @param event
	 *            pressed watch account
	 * @throws IOException
	 *             for the loader
	 * @throws InterruptedException
	 */
	public void enterToAccount(ActionEvent event) throws IOException, InterruptedException {
		CustomerEntity custent = this.getCustomerData(Client.getClientConnection().getUsername());
		if (custent == null)
			GeneralMessageController.showMessage("There was a problem loading customer\nPlease try again later");

		else
		{
			((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/AccountDetailsBoundary.fxml").openStream());
			AccountDetailsController adc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window
			adc.setLabels(custent);
			adc.setConnectionData(this, custent);
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);

			primaryStage.setTitle("Account details");
			primaryStage.setScene(scene);
			primaryStage.show();

		}
	}

	/**
	 * This method gets the customer details from the server
	 * 
	 * @param customerName
	 *            Username
	 * @return the customer entity
	 * @throws InterruptedException
	 */
	public CustomerEntity getCustomerData(String customerName) throws InterruptedException {

		MessageToSend msg = new MessageToSend(customerName, "getUserDetails");
		Client.getClientConnection().setDataFromUI(msg);
		Client.getClientConnection().accept();

		while (!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);

		Client.getClientConnection().setConfirmationFromServer();

		msg = Client.getClientConnection().getMessageFromServer();

		if (msg.getOperation().equals("customerExist"))
			return (CustomerEntity) msg.getMessage();

		else
			return null;

	}

	/**
	 * This method loads the Update details Window from customer main menu
	 * 
	 * @param event
	 *            pressed update account
	 * @throws IOException
	 */
	public void enterToUpdateDetails(ActionEvent event) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/UpdateAccountBoundary.fxml").openStream());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Details");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * this method sends you back to the previous window
	 * 
	 * 
	 * @param event
	 *            pressed log out
	 * @throws IOException
	 */
	public void logOutCustomer(ActionEvent event) throws IOException //when click "Back" return to main menu
	{
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		LoginController.signalLogOut();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		GeneralMessageController.showMessage("Bye Bye " + Client.getClientConnection().getUsername() + " we hope to see you soon");
		Client.getClientConnection().setClientUserName(null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	
	}

}
