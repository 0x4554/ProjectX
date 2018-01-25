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
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
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
import logic.MessageToSend;
/**
 *  This class is the controller for the store worker menu controller
 *  
 * StoreWorkerMenuController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class StoreWorkerMenuController extends MenuController implements Initializable{

    @FXML private Button ctlgBtn;
    @FXML private Button lgBtn;
    @FXML private Button srvBtn;
    @FXML private Button vwCtlgBtn;
    
    private StoreWorkerMenuController swm;
    private StoreEntity store;
    
    /**
     * Necessary constructor for the App
     */
	public StoreWorkerMenuController() {
		// TODO Auto-generated constructor stub
	}
	public void setConnectionData(StoreWorkerMenuController m) {
		this.swm=m;
	}

	/**
	 * This method loads the store worker menu boundary
	 * @throws IOException
	 */
	public void showMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreWorkerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		StoreWorkerMenuController swm = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		swm.setConnectionData(this);
		swm.setStore();
		primaryStage.setTitle("Store worker main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	/**
	 * The method open's the Catalog Window
	 * 
	 * @param event
	 *            on clicking "View Catalog" Button
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void enterCatalog(ActionEvent event) throws IOException, InterruptedException {
		((Node) event.getSource()).getScene().getWindow().hide(); //Hide Current window
		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/ViewCatalogBoundary.fxml").openStream()); //Load the fxml class
		CatalogController catl = loader.getController();
		catl.setPreviousController(this);
		catl.showCatalog(); //Call the method show catalog
		Stage primaryStage = new Stage(); //Set Stage->Show()
		Scene scene = new Scene(pRoot);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Zer-Li Catalog");
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
	 * when back button pressed
	 * go back to main menu
	 * @param event
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
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		GeneralMessageController.showMessage("Store worker "+Client.getClientConnection().getUsername()+" logged out");
		Client.getClientConnection().setClientUserName(null);
	}
	
	/**
	 * method to be called when store worker wishes to upload new survey
	 * 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void startNewSurvey(ActionEvent event) throws IOException, InterruptedException {
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChooseSurveyNumberBoundary.fxml").openStream());
		ChooseSurveyNumberController csnc=loader.getController();
		csnc.setConnectionData(this);
		
		MessageToSend toServer = new MessageToSend(null, "getNumberOfSurveys");
		Client.getClientConnection().setDataFromUI(toServer);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		ArrayList<Integer> allSurveys=(ArrayList<Integer>)Client.getClientConnection().getMessageFromServer().getMessage();

		csnc.initComboBox(allSurveys);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("New Survey");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method loads the promote sale window
	 * @param event pressed promote sale
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void PromoteSale(ActionEvent event) throws IOException, InterruptedException {
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreWorkerPromoteSale.fxml").openStream());
		StoreWorkerPromoteSaleController sc=loader.getController();
		//sc.setConnectionData(this);
		sc.setStore(this.store);
		sc.ShowAllProduct();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Promote Sale");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		setStore();
	}
}
