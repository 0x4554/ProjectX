package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.ProductEntity;
import entities.StoreEntity;
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
import logic.MessageToSend;

/**
 * This class is the controller for the store manager menu boundary
 * 
 * StoreManagerMenuController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class StoreManagerMenuController implements Initializable {
	private Client clnt;
	
	
	@FXML	
	private Button newAcntBtn;
	@FXML	
	private Button gnrtRprtBtn;
	@FXML
	private Button shwCnclBtn;
	@FXML
	private Button logOutBtn;
	private StoreEntity store;
	
	
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
		StoreManagerMenuController smmc = new StoreManagerMenuController();
		smmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		primaryStage.setTitle("Store manager's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	/**
	 * This method loads the create new account window
	 * @param event pressed create new account
	 * @throws IOException
	 */
	public void newAccount(ActionEvent event) throws IOException {		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("CreateNewAccountBoundary.fxml").openStream());				//new window to open
		 CreateNewAccountController cna=loader.getController();
		 cna.setConnectionData(this,this.store);
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("New Accout");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	/**
	 * This method sets the store of the manager
	 * @throws InterruptedException for the sleep 
	 */
	public void setStore() 
	{
		try {
		MessageToSend messageToSend = new MessageToSend(Client.getClientConnection().getUsername(), "getSpecificStore");
		Client.getClientConnection().setDataFromUI(messageToSend);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend message = Client.getClientConnection().getMessageFromServer();
		
		StoreEntity store = (StoreEntity) message.getMessage();

		this.store= store;				//save the store
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			LoginController.signalLogOut();
		}
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
		 Parent root = loader.load(getClass().getResource("/gui/StoreManagerViewReportsBoundary.fxml").openStream());				//new window to open
		 /*load here needed controller*/
		 StoreManagerViewReportsConroller s = new StoreManagerViewReportsConroller();
		 s = loader.getController();
		 s.setStore(this.store);
		 s.showStoreDetails();
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
	 * @throws InterruptedException  for sleep
	 */
	public void showCancelations(ActionEvent event) throws IOException, InterruptedException {
		
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 StoreManagerCancellationRequestsController s = new StoreManagerCancellationRequestsController();
		 Parent root = loader.load(getClass().getResource("/gui/CancellationRequestsBoundary.fxml").openStream());	
		
		 s = loader.getController();
		 s.setStore(this.store);
		 s.showOrders();
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Cancelation Requests");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	
	
	/**
	 * This method logs out the manager and loads the log in window
	 * @param event pressed log out
	 * @throws IOException
	 */
	public void logOutManager(ActionEvent event) throws IOException	//when click "Back" return to main menu
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	
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
	
	/**
	 * method for saving the current store
	 * that is active
	 * 
	 * @param s
	 */
	public void setStore(StoreEntity s) {
		this.store=s;
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		setStore();
	}

}
