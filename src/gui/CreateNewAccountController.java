package gui;

import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.CustomerEntity;
import entities.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

public class CreateNewAccountController implements Initializable {
	
	
    @FXML private Label usrLbl;
    @FXML private Label idLbl;
    @FXML private Label pswrdLbl;
    @FXML private Label pswrd2Lbl;
    @FXML private Label subscrptLbl;
    @FXML private Label crdLbl;
    @FXML private TextField usrFld;
    @FXML private TextField idFld;
    @FXML private TextField pswrdFld;
    @FXML private TextField pswrd2Fld;
    @FXML private TextField subscrptFld;
    @FXML private TextField crdFld;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;
    @FXML private ComboBox<String> subscrptCmb;

    ObservableList<String> list;
    
    
	/**
	 * A necessary constructor for the App
	 */
	public CreateNewAccountController() {			//Necessary empty constructor 
		
	}
		
	
	private void subscriptionComboBox()
	{
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Mounthly");
		al.add("Yearly");
		al.add("None");
		
		list = FXCollections.observableArrayList(al);
		subscrptCmb.setItems(list);
	}
	
	
	public void ShowNewUserAcount() throws IOException {
			
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("NewAccountBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CreateNewAccountController nac = loader.getController();	//set the controller to the NewAccountController
		primaryStage.setTitle("New Customer");
		primaryStage.setScene(scene);
		primaryStage.show();
					
	}
	
	public boolean checkRequiredFields() {
		if(usrFld.getText().isEmpty() || idFld.getText().isEmpty() || pswrdFld.getText().isEmpty() || pswrd2Fld.getText().isEmpty()) 
			return false;
			
		return true;
	}
	
	
	public void createNewUser() throws IOException {							////////*hide window if neccessary param ActionEvent event -->event(bla bla).hide()
		if(checkRequiredFields()) 												//check required fields are ok
			if(!pswrdFld.getText().equals(pswrd2Fld.getText())) {				//check matching passwords
				GeneralMessageController.showMessage("Passwords are not the same\nPlease try again");
				return;
			}
			else {
				CustomerEntity cust=new CustomerEntity();
				cust.setUserName(usrFld.getText());						//set Fields of the new customer
				cust.setCustomerID(Long.parseLong(idFld.getText()));
				cust.setPassword(pswrdFld.getText());
				cust.setSubscriptionDiscount((String)subscrptCmb.getValue());
				if(!crdFld.getText().isEmpty()) 								//if credit card is entered
					cust.setCreditCardNumber(Long.parseLong(crdFld.getText()));
			}
		
		
	}
	

//	public void pressedCreateAccount(ActionEvent event)  {		//when pressed "Create" button
//		
//		
//		
//		if(this.usrFld.getText().isEmpty() || this.idFld.getText().isEmpty() || this.pswrdFld.getText().isEmpty() || this.pswrd2Fld.getText().isEmpty() || this.crdFld.getText().isEmpty())			//if all fields are empty
//			GeneralMessageController.showMessage("All fields are empty!\nPlease fill the fields");
//		
//		else if(this.usrFld.getText().isEmpty())
//			GeneralMessageController.showMessage("Please enter user name");
//		
//		else if(this.idFld.getText().isEmpty())			GeneralMessageController.showMessage("Please enter user ID");
//		
//		else if(this.subscrptFld.getText().isEmpty())
//			GeneralMessageController.showMessage("Please enter subscription field");
//		
//		else if(this.pswrdFld.getText().isEmpty() && this.pswrd2Fld.getText().isEmpty())
//			GeneralMessageController.showMessage("Please enter password");
//		
//		
//		
//		if(!(this.pswrdFld.getText().equals(this.pswrd2Fld.getText())))		//check if password in field1 = password in field2;
//		{
//			
//		}
			
//	}
	
	
	
	//GeneralMessageController.showMessage("");
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Mounthly");
		al.add("Yearly");
		al.add("None");
		
		list = FXCollections.observableArrayList(al);
		subscrptCmb.setItems(list);
	}
	
}
