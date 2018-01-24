package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import logic.MessageToSend;

public class ReleaseUserBlockController implements Initializable{
	
	 @FXML
	 private ListView<?> usrsLstVw;

     @FXML
     private Button unblckBtn;

     @FXML
     private Button bckBtn;

    @FXML
    private Label noUsrLbl;

	
	private AdministratorMenuController amc;
	private ObservableList<String> users;
	private ArrayList<String> listOfUsers;
	
	
	
	
	/**
	 * method for connecting the current scene
	 * with the previous scene
	 * 
	 * @param adminScr
	 */
	public void setConnectionData(AdministratorMenuController adminScr) {
		this.amc=adminScr;
	}
	
	
	/**
	 * method for unblocking user
	 * 
	 */
	public void releaseBlockedUser() {
		
		MessageToSend msg = new MessageToSend(username,"releaseBlock");
		
	}
	
	
	/**
	 * method for getting all the blocked users
	 * 
	 * @throws InterruptedException
	 */
	public void getBlockedUsers() throws InterruptedException {
		MessageToSend message = new MessageToSend("", "getBlockedUsers");
		Client.getClientConnection().setDataFromUI(message);						//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		listOfUsers = (ArrayList<String>)m.getMessage();
		
		this.usrsLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		//unable multiple selection
		this.users = FXCollections.observableArrayList();		//the observable list to enter to the list  view
		
		for(String user : listOfUsers)		//build list view to contain all orders
		{
			this.users.add("User name: "+user.split("~")[0] + " User type: "+user.split("~")[1]);
		}
		
		this.usrsLstVw.setItems(this.users);		//set items to the list

	}
	
	
	
	/**
	 * method for going back to the previous scene
	 * 
	 * @param event - for hiding the current scene
	 * @throws IOException 
	 */
	public void bckBtnHandler(ActionEvent event) throws IOException {
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.amc.showAdministratorMenu();
	}
	
	
	public void setLabels() {
		// TODO Auto-generated method stub
		noUsrLbl.setVisible(false);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
}
