package gui;

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

    private Client clnt;
    private ObservableList<String> list;
    private StoreManagerMenuController mmc;
    
    
	/**
	 * A necessary constructor for the App
	 */
	public CreateNewAccountController() {			//Necessary empty constructor 
		
	}
	
	/**
	 * This method is the constructor for this class
	 * @param clnt
	 */
	public void setConnectionData(StoreManagerMenuController m) {
		this.mmc=m;
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
	
	
	public boolean checkRequiredFields() {
		if(usrFld.getText().isEmpty() || idFld.getText().isEmpty() || pswrdFld.getText().isEmpty() || pswrd2Fld.getText().isEmpty() || subscrptCmb.getSelectionModel().isEmpty())
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
				
				MessageToSend msg=new MessageToSend(cust, "createAccount");			//defining the job for the server
				Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
				Client.getClientConnection().accept();								//sending data to server
				
			}
		else {
			GeneralMessageController.showMessage("Please fill in all the required fields");
		}
		
	}
	
	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.mmc.showManagerMenu();										//open previous menu
		return;
	}
/*
	/**
	 * when "create" button pressed
	 * checks if all data is correct
	 * checks if all required fields are filled in
	 * @param event
	 * @throws IOException
	 */

/*	public void pressedCreateAccount(ActionEvent event) throws IOException  {
		if(this.usrFld.getText().isEmpty() && this.idFld.getText().isEmpty() && this.pswrdFld.getText().isEmpty() && this.pswrd2Fld.getText().isEmpty() && this.crdFld.getText().isEmpty())			//if all fields are empty
			GeneralMessageController.showMessage("All fields are empty!\nPlease fill the fields");
		
		else if(this.usrFld.getText().isEmpty())
			GeneralMessageController.showMessage("Please enter user name");
		
		else if(this.idFld.getText().isEmpty())		
			GeneralMessageController.showMessage("Please enter user ID");
		else if(this.pswrdFld.getText().isEmpty() && this.pswrd2Fld.getText().isEmpty())
			GeneralMessageController.showMessage("Please enter password");
		else if(!(this.pswrdFld.getText().equals(this.pswrd2Fld.getText())))					//check if password in field1 = password in field2;
		{
			GeneralMessageController.showMessage("Password doeas not match!\nPlease enter again");
			this.pswrdFld.clear();
			this.pswrd2Fld.clear();
		}
		else if(this.subscrptCmb.getSelectionModel().isEmpty())
			GeneralMessageController.showMessage("Please enter subscription");
	/*	else
			GeneralMessageController.showMessage("succeed");*/
		
//		this.clnt.setDataFromUI(username_password, "login!");	//set the data and the operation to send from the client to the server
//		this.clnt.accept();	//sends to server
//		while(!this.clnt.getConfirmationFromServer())	//wait until server replies
//			Thread.sleep(100);
//		this.clnt.setConfirmationFromServer();		//reset confirmation to false
//		}
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		subscriptionComboBox();
	}
}
/*	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */
/*	public void back(ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ManagerMenuBoundary.fxml").openStream());
		
		Stage primaryS=new Stage();
		Scene scene=new Scene(root);
		
		primaryS.setTitle("Generate Report");
		primaryS.setScene(scene);
		primaryS.show();
	}
	
}
*/