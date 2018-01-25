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

	@FXML private Button prmtBtn;
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
	
	/**
	 * opens new window to choose which user to create
	 * @param event create new user button pressed
	 * @throws IOException
	 */
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
	
	/**
	 * This method will be called for moving to the window of changing users permissions.
	 * 
	 * @param event - calling window for hiding it
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void usersPermissions(ActionEvent event) throws IOException, InterruptedException{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/EditUsersPremissionBoundary.fxml").openStream());				//new window to open
		 EditUsersPremissionController eup = loader.getController();
		 eup.setConnectionData(this);
		 eup.getUsers();
		 eup.setLabels();
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("User's premission");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	
	/**
	 * method for handling the press
	 * of the Release user button
	 * 
	 * @param event - current screen to hide
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void releaseUserBtn(ActionEvent event) throws IOException, InterruptedException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ReleaseUserBlockBoundary.fxml").openStream());				//new window to open
		ReleaseUserBlockController rubc = loader.getController();
		rubc.setConnectionData(this);
		rubc.setLabels();
		rubc.getBlockedUsers();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Unblock users");
		primaryStage.setScene(scene);
		primaryStage.show();
		
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
		
		GeneralMessageController.showMessage("Administrator "+Client.getClientConnection().getUsername()+" logged out");
		Client.getClientConnection().setClientUserName(null);
	}

}
