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
import logic.MessageToSend;

/**
 * This class is the controller for the customer service worker menu boundary
 * 
 * CustomerServiceWorkerMenuController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerServiceWorkerMenuController extends MenuController implements Initializable {

	@FXML
	private Button cmplntBtn;
	@FXML
	private Button lgOutBtn;
	@FXML
	private Button updtSrvyBtn;
	@FXML
	private Button bckBtn;
	@FXML 
	private Button vwCtlgBtn;

	/**
	 * A necessary constructor for the App
	 */
	public CustomerServiceWorkerMenuController()  {
		
	}
	
	/**
	 * This method loads the main menu window
	 * @throws IOException 
	 */
	public void showMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerMenuBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);	
		scene.getStylesheets().add("/gui/LoginStyle.css");

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
		scene.getStylesheets().add("/gui/LoginStyle.css");

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
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		GeneralMessageController.showMessage("Logged out");
	}
	
	/**
	 * This method loads the update survey window
	 * @param event	pressed update survey
	 * @throws IOException	for the loader
	 * @throws InterruptedException	for the sleep
	 */
	public void updatePressed(ActionEvent event) throws IOException, InterruptedException {
		
		int cnt=0;
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/UpdateSurveyBoundary.fxml").openStream());
		UpdateSurveyController usc = loader.getController();
		usc.setConnectionData(this);
		
		MessageToSend msg=new MessageToSend(null,"getSurveyQs");
		Client.getClientConnection().setDataFromUI(msg);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		String[] questions = (String[])Client.getClientConnection().getMessageFromServer().getMessage();
		for(String s:questions)
			if(s==null)
				cnt++;
		if(cnt==0)
			usc.setTextFields(questions);
		
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	/**
	 * This method loads the save service expert report window
	 * @param event pressed save service expert report
	 * @throws IOException  for the loader
	 * @throws InterruptedException 
	 */
	public void saveServiceExpertReport(ActionEvent event) throws IOException, InterruptedException
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerSaveVerbalReportsBoundary.fxml").openStream());
		CustomerServiceWorkerSaveVerbalReportsController cswsvrc = loader.getController();
		cswsvrc.setConnectionData(this);
		cswsvrc.showReports();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Customer service expert verbal reports");
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
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
