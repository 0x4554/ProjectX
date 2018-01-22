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

public class CustomerServiceExpertMenuController implements Initializable {

	  @FXML private Button avrBtn;
	  @FXML private Button vsrBtn;
	  @FXML private Button logBtn;
	  
	  private Client clnt;

	/**
	 * A necessary constructor for the App
	 * @throws IOException 
	 */
	public CustomerServiceExpertMenuController() throws IOException {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/CustomerServiceExpertMenuBoundary.fxml").openStream());
			
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			CustomerServiceExpertMenuController csem = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
			csem.setConnectionData(this.clnt);
			primaryStage.setTitle("Customer service expert menu");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	private void setConnectionData(Client clnt2) {
		this.clnt=clnt2;
	}
	
	public void showCustomerServiceExpertMenu()
	{
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	/**
	 * when log out button pressed
	 * @param event log out button pressed
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException	
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		LoginController.signalLogOut();
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		GeneralMessageController.showMessage("Customer service expert "+Client.getClientConnection().getUsername()+" logged out");
		Client.getClientConnection().setClientUserName(null);
	}

}
