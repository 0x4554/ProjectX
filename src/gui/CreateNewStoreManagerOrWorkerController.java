package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.StoreEntity;
import entities.StoreManagerEntity;
import entities.StoreWorkerEntity;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
public class CreateNewStoreManagerOrWorkerController implements Initializable {

    @FXML private TextField nmTxt;
    @FXML private TextField idTxt;
    @FXML private TextField emlTxt;
    @FXML private TextField pnumTxt;
    @FXML private PasswordField pswrdTxt;
    @FXML private PasswordField pswrd2Txt;
    @FXML private TextField brnchTxt;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;
    @FXML private ComboBox<String> strCmbBx;

   private ChooseUserToCreateController cutc;
	private AdministratorMenuController am;
	private String entity;
	private ObservableList<String> ObsListOfStores;
	private ArrayList<StoreEntity> listOfStores;
	
	public void setEntity(String s)
	{
		this.entity=s;
	}
	
	/**
	 * setter for the previous controller
	 * @param m controller
	 */
	 public void setConnectionData(ChooseUserToCreateController m)
	 {
		 this.cutc=m;
	 }
	 
	 /**
	  * This method loads the store in the DB
	  * 
	  * @throws InterruptedException Thread sleep
	  */
	 public void loadStores() throws InterruptedException
	 {
		 MessageToSend messageToSend = new MessageToSend("", "getAllStores");
			Client.getClientConnection().setDataFromUI(messageToSend); //set operation to get all stores from DB
			Client.getClientConnection().accept();
			while (!(Client.getClientConnection().getConfirmationFromServer())) //wait for server response
				Thread.sleep(100);
			Client.getClientConnection().setConfirmationFromServer(); //reset to false
			//		ArrayList<StoreEntity> listOfStoresFromDB = new ArrayList<StoreEntity>();
			messageToSend = Client.getClientConnection().getMessageFromServer();
			listOfStores = (ArrayList<StoreEntity>) messageToSend.getMessage(); //get the list of stores from the message class
			
			ObsListOfStores = FXCollections.observableArrayList();
			
			for(StoreEntity store : listOfStores)
			{
				ObsListOfStores.add("Branch ID : "+Integer.toString(store.getBranchID()) +", "+ store.getBranchName());
			}
			
			this.strCmbBx.setItems(ObsListOfStores);
	 }
	
	 
	 /**
	  * when create button pressed 
	  * sets data to the server
	  * @param event
	  * @throws IOException
	 * @throws InterruptedException 
	  */
	public void createNewUser(ActionEvent event) throws IOException, InterruptedException {							////////*hide window if neccessary param ActionEvent event -->event(bla bla).hide()
		if(checkRequiredFields()) 												//check required fields are ok
		{
			if(!pswrdTxt.getText().equals(pswrd2Txt.getText())) {				//check matching passwords
				GeneralMessageController.showMessage("Passwords are not the same\nPlease try again");
				return;
			}
			else {
				if(this.entity=="Store Manager")						//check which user to create. create store manager
				{
					StoreManagerEntity sm = new StoreManagerEntity();
					if(this.strCmbBx.getSelectionModel().isEmpty())
					{
						GeneralMessageController.showMessage("Please select branch");
						return;
					}
					
					sm.setBranch(Integer.parseInt(this.strCmbBx.getSelectionModel().getSelectedItem().substring(12,13)));
					sm.setEmailAddress(emlTxt.getText());
					try {
					    double number=Double.parseDouble(pnumTxt.getText());
					}catch(NumberFormatException e)
					{
						GeneralMessageController.showMessage("Invaild phone number");
						return;
					}
					sm.setPhoneNumber(pnumTxt.getText());
					sm.setPassword(pswrdTxt.getText());
					sm.setUserName(nmTxt.getText());
					try {
						long id=Long.parseLong(idTxt.getText());
					}catch(NumberFormatException e)
					{
						GeneralMessageController.showMessage("Invaild ID");
						return;
					}
					sm.setID(Long.parseLong(idTxt.getText()));
					sm.setUserType("SM");
					
					MessageToSend msg=new MessageToSend(sm, "createUser");			//defining the job for the server
					Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
					Client.getClientConnection().accept();								//sending data to server
				}
				else if(this.entity=="Store Worker")				//Create store worker
				{
					try {
						int idW=Integer.parseInt(idTxt.getText());
					}catch(NumberFormatException e)
					{
						GeneralMessageController.showMessage("Invaild ID");
						return;
					}
					
					StoreWorkerEntity sw = new StoreWorkerEntity();
					if(this.strCmbBx.getSelectionModel().isEmpty())
					{
						GeneralMessageController.showMessage("Please select branch");
						return;
					}
					
					sw.setBranch(Integer.parseInt(this.strCmbBx.getSelectionModel().getSelectedItem().substring(12,13)));
					try {
						long id=Long.parseLong(idTxt.getText());
					}catch(NumberFormatException e)
					{
						GeneralMessageController.showMessage("Invaild ID");
						return;
					}
					sw.setID(Long.parseLong(idTxt.getText()));
					sw.setEmailAddress(emlTxt.getText());
					sw.setPassword(pswrdTxt.getText());
					try {
					    double number=Double.parseDouble(pnumTxt.getText());
					}catch(NumberFormatException e)
					{
						GeneralMessageController.showMessage("Invaild phone number");
						return;
					}
					sw.setPhoneNumber(pnumTxt.getText());
					sw.setUserName(nmTxt.getText());
					sw.setUserType("SW");
					
					MessageToSend msg=new MessageToSend(sw, "createUser");			//defining the job for the server
					Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
					Client.getClientConnection().accept();								//sending data to server
				}
				
				while(!Client.getClientConnection().getConfirmationFromServer())
					Thread.sleep(100);
				
				Client.getClientConnection().setConfirmationFromServer();
				
				if(Client.getClientConnection().getMessageFromServer().getMessage().equals("added")) {
					((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					 FXMLLoader loader = new FXMLLoader();
					 Parent root = loader.load(getClass().getResource("/gui/AdministratorMenuBoundary.fxml").openStream());
					 
					Stage primaryStage=new Stage();
					Scene scene=new Scene(root);
					primaryStage.setTitle("Administrator menu");
					primaryStage.setScene(scene);
					primaryStage.show();									//open previous menu
					GeneralMessageController.showMessage("New user was added succesfully");
				}
				else {
					GeneralMessageController.showMessage("There was a problem, please try again");
				}
			}
			
		}
		else {
			GeneralMessageController.showMessage("Please fill in all the required fields");
			}
		
	}
	
	/**
	 * when create button pressed
	 * if all fields are filled in
	 * check if all required fields are filled in
	 * @return false if there is empty required field
	 */
	public boolean checkRequiredFields() {
		if(nmTxt.getText().isEmpty() || idTxt.getText().isEmpty() || pnumTxt.getText().isEmpty() || emlTxt.getText().isEmpty() || pswrdTxt.getText().isEmpty() || pswrd2Txt.getText().isEmpty())
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
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/ChooseUserToCreateBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Choose user");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			loadStores();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
