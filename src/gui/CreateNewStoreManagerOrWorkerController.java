package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.StoreManagerEntity;
import entities.StoreWorkerEntity;
import entities.UserInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
				if(this.antity=="Store Manager")						//check which user to create. create store manager
				{
					StoreManagerEntity sm = new StoreManagerEntity();
					sm.setBranch(Integer.parseInt(brnchTxt.getText()));
					sm.setEmailAddress(emlTxt.getText());
					sm.setPhoneNumber(pnumTxt.getText());
					sm.setPassword(pswrdTxt.getText());
					sm.setUserName(nmTxt.getText());
					sm.setID(Long.parseLong(idTxt.getText()));
					sm.setUserType("SM");
					
					MessageToSend msg=new MessageToSend(sm, "createUser");			//defining the job for the server
					Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
					Client.getClientConnection().accept();								//sending data to server
				}
				else if(this.antity=="Store Worker")				//Create store worker
				{
					StoreWorkerEntity sw = new StoreWorkerEntity(Integer.parseInt(idTxt.getText()),Integer.parseInt(brnchTxt.getText()));
					sw.setBranch(Integer.parseInt(brnchTxt.getText()));
					sw.setID(Long.parseLong(idTxt.getText()));
					sw.setEmailAddress(emlTxt.getText());
					sw.setPassword(pswrdTxt.getText());
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
		
	}

}
