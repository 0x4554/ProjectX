package gui;

import java.io.IOException;

/**
 * 
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yaakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This method is the controller for the customer order boundary
 * 
 * CustomerOrderController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerOrderController implements Initializable {

	@FXML private Button swOrdrDtlsBtn;
	@FXML private Button crtNwOrdrBtn;
	@FXML private Button bckBtn;

	private CustomerMenuController cm;
	
	/**
	 * Necessary constructor for the App
	 */
	public CustomerOrderController() {
	}
	
	/**
	 * setter for the previous controller 
	 * @param m the controller
	 */
	public void setConnectionData(CustomerMenuController m) {
		this.cm=m;
	}
	
	
	/**
	 * This method handles when the create new order has been pressed
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void CreateNewOrder(ActionEvent event) throws IOException, InterruptedException {
		((Node)event.getSource()).getScene().getWindow().hide();	//hide last window
		SelectStoreController scc = new SelectStoreController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SelectStoreBoundary.fxml"));
		loader.setController(scc);									//set the controller
		scc.showStores();											//call method to load the list of stores
		Parent root = loader.load();								//load the SelectStoreBoundary (sets the window data)
		
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Select store");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method loads the order details window
	 * @param event pressed view orders details
	 * @throws IOException for the loader
	 */
	public void showOrderDetails(ActionEvent event) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/CustomerOrderDetailsBoundary.fxml").openStream());
		CustomerOrderDetailsController cocdc = loader.getController();
		
		Stage primaryStage=new Stage();
		Scene scene=new Scene(pRoot);
		primaryStage.setTitle("Your Orders");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		CustomerMenuController cmc = new CustomerMenuController();
		cmc.showMenu();										//open previous menu
		return;
	}
	
}
