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
import logic.ConnectedClients;

public class StoreManagerMenuController implements Initializable {
	private Client clnt;
	
	
	@FXML	
	private Button newAcntBtn;
	@FXML	
	private Button gnrtRprtBtn;
	@FXML
	private Button shwCnclBtn;
	@FXML	
	private Button edtPrmsnsBtn;
	@FXML
	private Button logOutBtn;
	
	
	/**
	 * This method will be called for moving to the window of creating new account.
	 * 
	 * @param event - calling window for hiding it
	 * @throws IOException
	 */
	
	/**
	 * necessary constructor for the application
	 */
	public StoreManagerMenuController(){
		
	}
	
	/**
	 * 
	 * Constructor for saving the calling client for moving it to the controller
	 * @param clnt
	 */
	public StoreManagerMenuController(Client clnt){
		this.clnt=clnt;
	}
	
	/**
	 * 
	 * This method will present the menu for manager user
	 * @throws IOException
	 */
	public void showManagerMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreManagerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		StoreManagerMenuController mmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		mmc.setConnectionData(this.clnt);
		primaryStage.setTitle("Store manager's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	private void setConnectionData(Client clnt2) {
		// TODO Auto-generated method stub
		this.clnt=clnt2;
	}
	public void newAccount(ActionEvent event) throws IOException {		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("CreateNewAccountBoundary.fxml").openStream());				//new window to open
		 CreateNewAccountController cna=loader.getController();
		 cna.setConnectionData(this);
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("New Accout");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	
	/**
	 * This method will be called for moving to the window of generating new report.
	 * 
	 * @param event - calling window for hiding it
	 * @throws IOException
	 */
	public void generateReport(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/******ToImplement*****/.fxml").openStream());				//new window to open
		 /*load here needed controller*/
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Generate Report");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	/**
	 * This method will be called for moving to the window that presents all cancellation requests.
	 * 
	 * @param event - calling window for hiding it
	 * @throws IOException
	 */
	public void showCancelations(ActionEvent event) throws IOException {
		
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/CancellationRequestsBoundary.fxml").openStream());				//new window to open
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Cancelation Requests");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	/**
	 * This method will be called for moving to the window of changing users permissions.
	 * 
	 * @param event - calling window for hiding it
	 * @throws IOException
	 */
	public void usersPermissions(ActionEvent event) throws IOException{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/EditUsersPremissionBoundary.fxml").openStream());				//new window to open
		 EditUsersPremissionController eup = loader.getController();
		 eup.setConnectionData(this);
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("User's premission");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	
	public void logOutManager(ActionEvent event) throws IOException	//when click "Back" return to main menu
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
//		
		LoginController.signalLogOut();									//log out user from system
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		GeneralMessageController.showMessage("Store Manager "+Client.getClientConnection().getUsername()+" logged out");
		Client.getClientConnection().setClientUserName(null);
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
