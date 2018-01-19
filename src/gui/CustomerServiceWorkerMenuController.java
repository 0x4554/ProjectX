package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CustomerServiceWorkerMenuController implements Initializable {

	@FXML
	private Button cmplntBtn;
	@FXML
	private Button lgOutBtn;

	/**
	 * A necessary constructor for the App
	 * @throws IOException 
	 */
	public CustomerServiceWorkerMenuController()  {
		
	}
	
	/**
	 * This method loads the main menu window
	 * @throws IOException 
	 */
	public void showCostumerServiceWorkerMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerMenuBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);		
		primaryStage.setTitle("Customer Service worker main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method loads the complaints window
	 * @param event	pressed complaints
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void complaints(ActionEvent event) throws IOException, InterruptedException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerComplaintsBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerServiceWorkerComplaintController cswmcc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		cswmcc.showComplaints();
		
		primaryStage.setTitle("Complaints");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method logs out the user
	 * @param event	pressed log out
	 * @throws IOException 
	 */
	public void logOut(ActionEvent event) throws IOException {

		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		LoginController.signalLogOut();
		Client.getClientConnection().setClientUserName(null);
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		GeneralMessageController.showMessage("Logged out");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
