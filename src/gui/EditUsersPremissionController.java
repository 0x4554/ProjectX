package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	
	@FXML private Label usrLbl;
	@FXML private TextField usrFld;
	@FXML private Button bckBtn;
	@FXML private Button okBtn;
	@FXML private ComboBox<String> prmsCmb;
	
	private StoreManagerMenuController mmc;
	private ObservableList<String> list;
	
	/**
	 * Necessary constructor for the APP
	 */
	public EditUsersPremissionController() {
	}
	
	
	public void setConnectionData(StoreManagerMenuController m)
	{
		this.mmc=m;
	}
	
	
	/**
	 * method for handling the edit of user permissions
	 * 
	 * @param event - event for hiding the current screen
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void editPermissions(ActionEvent event) throws InterruptedException, IOException {
	
		if(!usrFld.getText().isEmpty() && !prmsCmb.getSelectionModel().isEmpty()) {
				
			String []userAndPermission=new String[2];
			userAndPermission[0]=usrFld.getText();
			userAndPermission[1]=permissionsParser(prmsCmb.getValue());
			
			MessageToSend toServer=new MessageToSend(userAndPermission,"updatePermission");
			Client.getClientConnection().setDataFromUI(toServer);
			Client.getClientConnection().accept();
			
			while(!Client.getClientConnection().getConfirmationFromServer())
				Thread.sleep(100);
			Client.getClientConnection().setConfirmationFromServer();
			
			if(Client.getClientConnection().getMessageFromServer().getMessage().equals("Updated")) {
				((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
				this.mmc.showManagerMenu();
				GeneralMessageController.showMessage("User permission updated successfully");
			}
			else if(Client.getClientConnection().getMessageFromServer().getMessage().equals("noUser")) {
				GeneralMessageController.showMessage("User does not exist");
			}
			else if(Client.getClientConnection().getMessageFromServer().getMessage().equals("customer")) {
			//	((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
				this.createNewCustomer(event);
			}
			else {
				((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
				this.mmc.showManagerMenu();
				GeneralMessageController.showMessage("Update failed\nPlease contact technical support and try again later");
			}
		}
		else {
			GeneralMessageController.showMessage("Please fill in all required fields");
		}
		
//		if(prmsCmb.getValue().equals("Customer")) {
//			TurnToCustomer();
//	}
	}
	
	
	private void createNewCustomer(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("CreateNewAccountBoundary.fxml").openStream());				//new window to open
		 
		 CreateNewAccountController cna=loader.getController();
		 cna.setConnectionData(this);
		 cna.setField(usrFld.getText());
		 Stage primaryStage=new Stage();
		 Scene scene=new Scene(root);
			
		primaryStage.setTitle("New Accout");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	private String permissionsParser(String str) {
		if(str.equals("Customer"))
			return "C";
		else if(str.equals("Store Worker"))
			return "SW";
		else if(str.equals("Store Manager"))
			return "SM";
		else
			return null;
	}


	/**
	 * Permissions varieties 
	 */
	private void premissionsComboBox()
	{
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Customer");
		al.add("Store Worker");
		al.add("Store Manager");
		
		list = FXCollections.observableArrayList(al);
		prmsCmb.setItems(list);
	}
	
	
	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.mmc.showManagerMenu();										//open previous menu
		return;
	}
	
	
	public void showEdittingOptions(ActionEvent event) throws IOException {

		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/EditUsersPremissionBoundary.fxml").openStream());				//new window to open
		 EditUsersPremissionController eup = loader.getController();
		 eup.setConnectionData(this.mmc);
		 Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("User's premission");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		premissionsComboBox();
	}

	
	
}
