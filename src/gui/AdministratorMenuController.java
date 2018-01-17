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

/**
 * this class shows user account
 * and allows to change user details
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */

public class AdministratorMenuController implements Initializable {

	@FXML private Button rprtBtn;
    @FXML private Button usrBtn;
    @FXML private Button lgBtn;
    
    private Client clnt;
	
	/**
	 * A necessary constructor for the App
	 */
	public AdministratorMenuController() {}
	
	public AdministratorMenuController(Client clnt) {
		this.clnt=clnt;
	}
	
	/**
	 * method that shows Administrator menu
	 * @throws IOException
	 */
	public void showAdministratorMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/AdministratorMenuBoundary.fxml").openStream());
		
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		AdministratorMenuController am = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		am.setConnectionData(this.clnt);
		primaryStage.setTitle("Administrator's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	private void setConnectionData(Client clnt2) {
		this.clnt=clnt2;
	}
	
	
	public void enterCreateNewUser(ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChooseUserToCreateBoundary.fxml").openStream());
		ChooseUserToCreateController cnu=loader.getController();
		cnu.setConnectionData(this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Choose premission");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	/**
	 * when log out button pressed
	 * @param event
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException	
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		LoginController.signalAppClose();
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		GeneralMessageController.showMessage("Bye Bye "+Client.getClientConnection().getUsername()+" we hope to see you soon");
		
	}

}
