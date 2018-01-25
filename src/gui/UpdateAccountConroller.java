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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.MessageToSend;


/**
 *	this class is for controlling the update account screen, handles the new account's details
 *	and helps to update them in the Database 
 * 
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 */
public class UpdateAccountConroller implements Initializable{
	
	@FXML
	private Label aprvChngsLbl;
	
	@FXML
    private TextField phnNumTxtFld;

    @FXML
    private TextField mailTxtFld;

    @FXML
    private TextField crdtCrdTxtFld;

    @FXML
    private TextField adrsTxtFld;

    @FXML
    private Button cnclBtn;

    @FXML
    private Button updtBtn;
    
    @FXML
    private CheckBox aprvChkBox;
    
    private CustomerEntity customerEnt;
    private AccountDetailsController adc;
    
    
    /**
     * 
     *constructor for the UpdateAccountConroller.java class
     */
    public UpdateAccountConroller(){
    	
    }
    
    /**
     * method for passing the data between two different objects
     * 
     * @param accountDetailsController - previous screen that was showed
     * @param customer - CustomerEntity object that should be updated
     */
	public void setConnectionData(AccountDetailsController accountDetailsController, CustomerEntity customer) {
		// TODO Auto-generated method stub
		this.customerEnt=customer;
		this.adc=accountDetailsController;
	}

    
    /**
     * method to verify all needed fields are updated
     * 
     * @return true if all fields are OK and false if one or more is empty
     * @throws IOException 
     */
    public int checkValidFields() throws IOException {
    	if(phnNumTxtFld.getText().isEmpty() || mailTxtFld.getText().isEmpty() || crdtCrdTxtFld.getText().isEmpty() || adrsTxtFld.getText().isEmpty())
    		return -1;
    	else {
    		try {
        		Long.parseLong(phnNumTxtFld.getText());
        	}
        	catch(Exception e) {
        		GeneralMessageController.showMessage("Ilegal phone number");
        		return 0;
        	}
        	
        	try {
        		Long.parseLong(crdtCrdTxtFld.getText());
        	}
        	catch(Exception e) {
        		GeneralMessageController.showMessage("Ilegal credit card number");
        		return 0;
        	}
        	return 1;
    	}
    }
    
    
    /**
     * method for handling the button press of "Update"
     * 
     * @param event event of the current window in order to hide it when procedure is done
     * @throws IOException 
     * @throws InterruptedException 
     */
    public void updateAccountDetailsPressed(ActionEvent event) throws IOException, InterruptedException {
    	int chkflds=checkValidFields();
    	if(chkflds==1) {													//checks all fields are filled
    		if(aprvChkBox.isSelected()) {											//checks verification checkbox
    			updateFieldsData();													//reads data from fields for customers details update
    			if(sendUpdates()) {													//handles send to server
    				((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
    				this.adc.getCustomerMenu().enterToAccount(event);				//presents the previous window
    				
    				GeneralMessageController.showMessage("Account was updated successfully");
    			
    			}
    			else
    				GeneralMessageController.showMessage("Update failed\nplease contact the store for help");
    		}
    		else 
    			aprvChngsLbl.setVisible(true);										//reveals hidden label
    	}
    	
    	else if(chkflds==-1){
    		GeneralMessageController.showMessage("Please fill in all the fields");
    	}

    }
    
    
    /**
     * method to send updates to the server in order to insert them into the Database
     * 
     * @return true if update succeeded or false if update failed
     * @throws InterruptedException
     */
    public boolean sendUpdates() throws InterruptedException {							//changes are verified and if everything is OK it is sent to the server
    	MessageToSend toServer= new MessageToSend(customerEnt,"updateAccount");
		Client.getClientConnection().setDataFromUI(toServer);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		MessageToSend fromServer=Client.getClientConnection().getMessageFromServer();
		if(fromServer.getMessage().equals("accountUpdated")) 
			return true;
		
		return false;
    }
    
    
    /**
     * method for updating the current customer details
     * @throws IOException 
     * 
     */
    public void updateFieldsData(){		
    	
    	this.customerEnt.setPhoneNumber(phnNumTxtFld.getText());			//all changes are saved to the current customer
    	this.customerEnt.setEmailAddress(mailTxtFld.getText());
    	this.customerEnt.setCreditCardNumber(Long.parseLong(crdtCrdTxtFld.getText()));
    	this.customerEnt.setAddress(adrsTxtFld.getText());
    }
    
    /**
     * method to load data into the fields before update happens
     * 
     */
    public void setFields() {
    	aprvChngsLbl.setVisible(false);										//hidden label, showed only when customer didnt approve changes
		phnNumTxtFld.setText(customerEnt.getPhoneNumber());					//other text fields are filled automatically in current details
		adrsTxtFld.setText(customerEnt.getAddress());
		crdtCrdTxtFld.setText(Long.toString(customerEnt.getCreditCardNumber()));
		mailTxtFld.setText(customerEnt.getEmailAddress());
    }
    
    /**
     * This method loads the previou window
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void cancelUpdate(ActionEvent event) throws IOException, InterruptedException {
    	
    	((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.adc.getCustomerMenu().enterToAccount(event);				//load previous window
    }
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
