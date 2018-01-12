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

public class CustomerOrderController implements Initializable {

	@FXML
	private Button swOrdrDtlsBtn;

	@FXML
	private Button crtNwOrdrBtn;

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

	public void showOrderDetails(ActionEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
}
