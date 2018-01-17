package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.CustomerEntity;
import entities.StoreManagerEntity;
import entities.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logic.MessageToSend;
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
public class CreateNewUserController implements Initializable{

	@FXML private TextField nmTxt;
    @FXML private TextField idTxt;
    @FXML private TextField phnNumTxt;
    @FXML private TextField emlTxt;
    @FXML private TextField wnumTxt;
    @FXML private TextField slrTxt;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;
    @FXML private PasswordField pswrdTxt;
    @FXML private PasswordField pswrd2Txt;
	
    
	AdministratorMenuController am;
	private ObservableList<String> list;
	
	/**
	 * Necessary constructor for the App
	 */
	public CreateNewUserController() {
		
	}
	
	public void setConnectionData(AdministratorMenuController m)
	{
		this.am=m;
	}
	
	
	
	
	/*
	public void createNewUser() throws IOException {							////////*hide window if neccessary param ActionEvent event -->event(bla bla).hide()
		if(checkRequiredFields()) 												//check required fields are ok
			if(!pswrdTxt.getText().equals(pswrd2Txt.getText())) {				//check matching passwords
				GeneralMessageController.showMessage("Passwords are not the same\nPlease try again");
				return;
			}
			else {
				
				{
					StoreManagerEntity sm=new StoreManagerEntity();
					sm.set
				}
				
				cust.setUserName(nmTxt.getText());						//set Fields of the new customer
				cust.setCustomerID(Long.parseLong(idTxt.getText()));
				cust.setPassword(pswrdTxt.getText());
				cust.setSubscriptionDiscount((String)subscrptCmb.getValue());
				cust.setEmailAddress(emlTxt.getText());
				cust.setPhoneNumber(phnNumTxt.getText());
				
				MessageToSend msg=new MessageToSend(cust, "createAccount");			//defining the job for the server
				Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
				Client.getClientConnection().accept();								//sending data to server
			}
		else {
			GeneralMessageController.showMessage("Please fill in all the required fields");
		}
		
	}*/
	
	public boolean checkRequiredFields() {
		if(nmTxt.getText().isEmpty() ||idTxt.getText().isEmpty() || phnNumTxt.getText().isEmpty() || emlTxt.getText().isEmpty() || wnumTxt.getText().isEmpty() || slrTxt.getText().isEmpty()|| pswrdTxt.getText().isEmpty() || pswrd2Txt.getText().isEmpty())
			return false;

		return true;
	}
	
	/**
	 * when back button pressed, return to previous window 
	 * @param event back button pressed
	 * @throws IOException
	 */
    public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();				//hide current window
		this.am.showAdministratorMenu();										//open previous menu
		return;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

}
