package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
	public void newAccount(ActionEvent event) throws IOException {		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/*****ToImplement*****/.fxml").openStream());				//new window to open
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
