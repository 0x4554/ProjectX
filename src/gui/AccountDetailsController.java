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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class AccountDetailsController implements Initializable{
	
	 @FXML private Label cstLbl;
	 @FXML private Label idLbl;
	 @FXML private Label statLbl;
	 @FXML private Label crdLbl;
	 @FXML private Label crddatLbl;
	 @FXML private TextField Fld;
	 @FXML private TextField idFld;
	 @FXML private TextField statFld;
	 @FXML private TextField crdFld;
	 @FXML private TextField crdDateFld;
	 @FXML private Button crtBtn;
	 @FXML private Button bckBtn;
	 
	 private Client clnt;

	 /**
	  * This method is the constructor for this class
	  * @param clnt is connected client
	  */
	 public AccountDetailsController(Client clnt)
	 {
		 this.clnt=clnt;
	 }
	 
	 /**
	  * A necessary constructor for the App
	  */
	 public AccountDetailsController()
	 {
		 
	 }
	 
	 public void ShowAccountDetails(Client clnt)
	 {
		 
	 }
	 
	 @Override
		public void initialize(URL location, ResourceBundle resources) 
		{
		}

	 
	 /**
	  * when back button pressed
	  * @param event pressed back button
	  * @throws IOException
	  */
	 public void back(ActionEvent event) throws IOException
		{
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window

			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
			
			Stage primaryS=new Stage();
			Scene scene=new Scene(root);
			
			primaryS.setTitle("Generate Report");
			primaryS.setScene(scene);
			primaryS.show();
		}
	 
	 
	
}
