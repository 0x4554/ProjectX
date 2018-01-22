package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.corba.se.impl.ior.GenericTaggedComponent;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ConnectedClients;
import logic.MessageToSend;

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
    @FXML private PasswordField pswrdFld;
    @FXML private PasswordField pswrd2Fld;
    @FXML private TextField crdFld;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;
    @FXML private ComboBox<String> subscrptCmb;
    @FXML private TextField emlFld;
    @FXML private TextField phnFld;
    @FXML private TextField adrsFld;


    private ObservableList<String> list;
    private StoreManagerMenuController mmc=null;
    private EditUsersPremissionController eupc=null;
    
    
	/**
	 * A necessary constructor for the App
	 */
	public CreateNewAccountController() {
		
	}
	

	public void setConnectionData(StoreManagerMenuController m) {
		this.mmc=m;
	}
	
	
	public void setConnectionData(EditUsersPremissionController edit) {
		this.eupc=edit;
	}
		
	
	private void subscriptionComboBox()
	{
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Monthly");
		al.add("Yearly");
		al.add("None");
				
		list = FXCollections.observableArrayList(al);
		subscrptCmb.setItems(list);
	
	}
	
	
	/**
	 * when create button pressed
	 * check if all required fields are filled in
	 * @return false if there is empty required field
	 */
	public boolean checkRequiredFields() {
		if(usrFld.getText().isEmpty() ||emlFld.getText().isEmpty() || phnFld.getText().isEmpty() || idFld.getText().isEmpty() || pswrdFld.getText().isEmpty() || pswrd2Fld.getText().isEmpty() || adrsFld.getText().isEmpty() || subscrptCmb.getSelectionModel().isEmpty())
			return false;

		return true;
	}
	
	
	/**
	 * when "create" button pressed
	 * checks if all data is correct
	 * checks if all required fields are filled in
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void createNewUser(ActionEvent event) throws IOException, InterruptedException {							////////*hide window if neccessary param ActionEvent event -->event(bla bla).hide()
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
				cust.setEmailAddress(emlFld.getText());
				cust.setPhoneNumber(phnFld.getText());
				cust.setAddress(adrsFld.getText());

				if(!crdFld.getText().isEmpty()) 								//if credit card is entered
					cust.setCreditCardNumber(Long.parseLong(crdFld.getText()));
				
				
				MessageToSend msg=new MessageToSend(cust, "createAccount");			//defining the job for the server
				Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
				Client.getClientConnection().accept();								//sending data to server
				
				
				while(!Client.getClientConnection().getConfirmationFromServer())
					Thread.sleep(100);
				
				Client.getClientConnection().setConfirmationFromServer();
				
				if(Client.getClientConnection().getMessageFromServer().getMessage().equals("added")) {
					((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					this.mmc.showManagerMenu();										//open previous menu
					GeneralMessageController.showMessage("New customer "+cust.getUserName()+" was added succesfully");
				}
				else {
					GeneralMessageController.showMessage("There was a problem, please try again");
				}
			}
		else {
			GeneralMessageController.showMessage("Please fill in all the required fields");
		}
		
	}
	
	
	/*public boolean checkRequiredFields() {
		if(usrFld.getText().isEmpty() ||emlFld.getText().isEmpty() || phnFld.getText().isEmpty() || idFld.getText().isEmpty() || pswrdFld.getText().isEmpty() || pswrd2Fld.getText().isEmpty() || subscrptCmb.getSelectionModel().isEmpty())
			return false;

		return true;
	}*/
	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		if(this.mmc!=null)
			this.mmc.showManagerMenu();										//open previous menu
		else
			this.eupc.showEdittingOptions(event);
		return;
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		subscriptionComboBox();
	}


	public void setField(String text) {
		// TODO Auto-generated method stub
		usrFld.setText(text);
	}	
}
