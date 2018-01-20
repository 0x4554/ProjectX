package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.StoreManagerEntity;
import entities.StoreWorkerEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.MessageToSend;

public class CreateNewStoreManagerOrWorkerController implements Initializable {

    @FXML private TextField nmTxt;
    @FXML private TextField idTxt;
    @FXML private TextField emlTxt;
    @FXML private TextField pnumTxt;
    @FXML private TextField pswrdTxt;
    @FXML private TextField pswrd2Txt;
    @FXML private TextField brnchTxt;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;

    ChooseUserToCreateController cutc;
	AdministratorMenuController am;
	String antity;
	
	public void setAntity(String s)
	{
		this.antity=s;
	}
	
	 public void setConnectionData(ChooseUserToCreateController m)
	 {
		 this.cutc=m;
	 }
	
	public void createNewUser() throws IOException {							////////*hide window if neccessary param ActionEvent event -->event(bla bla).hide()
		if(checkRequiredFields()) 												//check required fields are ok
			if(!pswrdTxt.getText().equals(pswrd2Txt.getText())) {				//check matching passwords
				GeneralMessageController.showMessage("Passwords are not the same\nPlease try again");
				return;
			}
			else {
				if(this.antity=="Store Manager")						//check which user to create. create store manager
				{
					StoreManagerEntity sm = new StoreManagerEntity();
					sm.setBranch(Integer.parseInt(brnchTxt.getText()));
					sm.setEmailAddress(emlTxt.getText());
					sm.setPhoneNumber(pnumTxt.getText());
					sm.setPassword(pswrdTxt.getText());
					sm.setUserName(nmTxt.getText());
					sm.setCustomerID(Long.parseLong(idTxt.getText()));
					sm.setWorkerid(Integer.parseInt(idTxt.getText()));
					sm.setUserType(this.antity);
					
					MessageToSend msg=new MessageToSend(sm, "createAccount");			//defining the job for the server
					Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
					Client.getClientConnection().accept();								//sending data to server
				}
				else if(this.antity=="Store Worker")				//Create store worker
				{
					StoreWorkerEntity sw = new StoreWorkerEntity(Integer.parseInt(idTxt.getText()),Integer.parseInt(brnchTxt.getText()));
					sw.setBranch(Integer.parseInt(brnchTxt.getText()));
					sw.setCustomerID(Integer.parseInt(idTxt.getText()));
					sw.setEmailAddress(emlTxt.getText());
					sw.setPassword(pswrdTxt.getText());
					sw.setPhoneNumber(pnumTxt.getText());
					sw.setUserName(nmTxt.getText());
					sw.setUserType(this.antity);
					sw.setWorkerid(Integer.parseInt(idTxt.getText()));
					
					MessageToSend msg=new MessageToSend(sw, "createAccount");			//defining the job for the server
					Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
					Client.getClientConnection().accept();								//sending data to server
				}
				
				
				
			}
		else {
			GeneralMessageController.showMessage("Please fill in all the required fields");
		}
		
	}
	
	/**
	 * when create button pressed
	 * check if all required fields are filled in
	 * @return false if there is empty required field
	 */
	public boolean checkRequiredFields() {
		if(nmTxt.getText().isEmpty() || brnchTxt.getText().isEmpty() ||idTxt.getText().isEmpty() || pnumTxt.getText().isEmpty() || emlTxt.getText().isEmpty() || pswrdTxt.getText().isEmpty() || pswrd2Txt.getText().isEmpty())
			return false;

		return true;
	}
	
	
    /**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.am.showAdministratorMenu();								
		return;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
