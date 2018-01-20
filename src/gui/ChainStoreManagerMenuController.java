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

public class ChainStoreManagerMenuController implements Initializable {

	@FXML
    private Button lgOutBtn;

    @FXML
    private Button vwRprtsBtn;
	/**
	 * A necessary constructor for the App
	 */
	public ChainStoreManagerMenuController() {
	}

	/**
	 * This method loads the main menu window
	 * @throws IOException 
	 */
	public void showChainStoreManagerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainStoreManagerMenuBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);		
		primaryStage.setTitle("Chain store manager main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method loads the stores window to choose which store's report to view
	 * @param event	pressed view reports
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void viewReports(ActionEvent event) throws IOException, InterruptedException {
		((Node)event.getSource()).getScene().getWindow().hide();	//hide last window
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainStoreManagerSelectStoreBoundary.fxml").openStream());
		ChainStoreManagerSelectStoreController csmscc = new ChainStoreManagerSelectStoreController();
		csmscc = loader.getController();
		csmscc.showStores();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Store reports");
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
