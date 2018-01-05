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

public class OrderController implements Initializable {

	private Client clnt;
	@FXML
	private Button swOrdrDtlsBtn;

	@FXML
	private Button crtNwOrdrBtn;
	/**
	 * This method saves the client connection to the controller
	 * @param clnt	the connection client
	 */
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	
	public void CreateNewOrder(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();	//hide last window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
		CreateNewOrderController nom = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		nom.setConnectionData(this.clnt);
		primaryStage.setTitle("New order");
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
