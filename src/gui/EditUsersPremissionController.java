package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.ComplaintEntity;
import entities.CustomerEntity;
import entities.CustomerServiceWorkerEntity;
import entities.OrderEntity;
import entities.ServiceExpertEntity;
import entities.StoreManagerEntity;
import entities.StoreWorkerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.MessageToSend;

/**
 * this class allows to change users permission
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */


public class EditUsersPremissionController implements Initializable{
	
	@FXML private Button bckBtn;
	@FXML private Button okBtn;
	@FXML private ComboBox<String> prmsCmb;
	@FXML private ListView<String> userLstVw;
	
	private AdministratorMenuController amc;
	private ObservableList<String> list;
	private ObservableList<String> users;
	private ArrayList<String> listOfUsers;
	
	/**
	 * Necessary constructor for the APP
	 */
	public EditUsersPremissionController() {
	}
	
	
	public void getUsers() throws InterruptedException
	{
		
		MessageToSend message = new MessageToSend("", "getAllUsers");
		Client.getClientConnection().setDataFromUI(message);						//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		listOfUsers = (ArrayList<String>)m.getMessage();
		
		this.userLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		//unable multiple selection
		
		this.users = FXCollections.observableArrayList();		//the observable list to enter to the list  view
		
		for(String user : listOfUsers)		//build list view to contain all orders
		{
			this.users.add("User name: "+user.split("~")[0] + " User type: "+user.split("~")[1]);
		}
		
		this.userLstVw.setItems(this.users);		//set items to the list

//		EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
//			showUsers(event);
//		};

//		this.userLstVw.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 


	}
	
	public void setConnectionData(AdministratorMenuController m)
	{
		this.amc=m;
	}
	
	
	/**
	 * Permissions varieties in combobox
	 */
	private void premissionsComboBox()
	{
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Customer");
		al.add("Store Worker");
		al.add("Store Manager");
		al.add("Customer Service Expert");
		al.add("Customer Service Worker");
		
		list = FXCollections.observableArrayList(al);
		prmsCmb.setItems(list);
	}
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void editPremission(ActionEvent event) throws IOException
	{
		if(checkInsert())
		{
			if(prmsCmb.getSelectionModel().getSelectedItem().equals("Customer"))
			{
				
			}
			else if(prmsCmb.getSelectionModel().getSelectedItem().equals("Store Worker"))
			{
				
			}
			else if(prmsCmb.getSelectionModel().getSelectedItem().equals("Store Manager"))
			{
				
			}
			else if(prmsCmb.getSelectionModel().getSelectedItem().equals("Customer Service Expert"))
			{
				
			}
			else if(prmsCmb.getSelectionModel().getSelectedItem().equals("Customer Service Worker"))
			{
				
			}
		}
		else
			GeneralMessageController.showMessage("Choose user's premission");
			
	}

	
	public boolean checkInsert()
	{
		if(prmsCmb.getSelectionModel().isEmpty())
			return false;
		else
			return true;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		premissionsComboBox();
	}
	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.amc.showAdministratorMenu();										//open previous menu
		return;
	}

	
	
}
