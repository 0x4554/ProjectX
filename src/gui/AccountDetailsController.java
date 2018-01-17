package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.CustomerEntity;
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
	
	@FXML private Button bckBtn;
    @FXML private Button updBtn;
    @FXML private Label cstNmLbl;
    @FXML private Label idLbl;
    @FXML private Label phnLbl;
    @FXML private Label mailLbl;
    @FXML private Label sbscrptLbl;
    @FXML private Label crdtLbl;
    @FXML private Label adrsLbl;
	 
	 CustomerMenuController cm;
	 
	 public void setConnectionData(CustomerMenuController m)
		{
			this.cm=m;
		}
	 
	 /**
	  * A necessary constructor for the App
	  */
	 public AccountDetailsController()
	 {
		 
	 }
	 

	 
	 /**
	  * when back button pressed
	  * @param event pressed back button
	  * @throws IOException
	  */
	 public void bckBtnHandler(ActionEvent event) throws IOException {
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			this.cm.showCustomerMenu();										//open previous menu
			return;
		}
	 
	 
	 public void setLabels(CustomerEntity c) {
		 cstNmLbl.setText(c.getUserName());
		 idLbl.setText(Long.toString(c.getCustomerID()));
		 phnLbl.setText(c.getPhoneNumber());
		 mailLbl.setText(c.getEmailAddress());
		 sbscrptLbl.setText(c.getSubscriptionDiscount().toString());
		 crdtLbl.setText(Long.toString(c.getCreditCardNumber()));
		 adrsLbl.setText(c.getAddress());
	 }
	 
	 @Override
		public void initialize(URL location, ResourceBundle resources) 
		{
		}
	
}
