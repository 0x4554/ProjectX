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

public class ManagerMenuController implements Initializable {
	private Client clnt;
	
	
	@FXML	
	private Button newAcntBtn;
	@FXML	
	private Button gnrtRprtBtn;
	@FXML
	private Button shwCnclBtn;
	@FXML	
	private Button edtPrmsnsBtn;
	
	
	/**
	 * This method will be called for moving to the window of creating new account.
	 * 
	 * @param event - calling window for hiding it
	 * @throws IOException
	 */
	
	/**
	 * necessary constructor for the application
	 */
	public ManagerMenuController(){
		
	}
	
	/**
	 * 
	 * Constructor for saving the calling client for moving it to the controller
	 * @param clnt
	 */
	public ManagerMenuController(Client clnt){
		this.clnt=clnt;
	}
	
	/**
	 * 
	 * This method will present the menu for manager user
	 * @throws IOException
	 */
	public void showManagerMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ManagerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		ManagerMenuController mmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		mmc.setConnectionData(this.clnt);
		primaryStage.setTitle("Manager's main menu");
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
		 /*load here needed controller*/
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
		 Parent root = loader.load(getClass().getResource("/******ToImplement*****/.fxml").openStream());				//new window to open
		 /*load here needed controller*/
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
		 Parent root = loader.load(getClass().getResource("/******ToImplement*****/.fxml").openStream());				//new window to open
		 /*load needed controller here*/
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Generate Report");
			primaryStage.setScene(scene);
			primaryStage.show();
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
