package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.CustomerServiceWorkerEntity;
import entities.ServiceExpertEntity;
import entities.StoreManagerEntity;
import entities.StoreWorkerEntity;
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
public class CreateNewServiceExpertOrWorkerController implements Initializable{

	 @FXML private TextField nmTxt;
	 @FXML private TextField idTxt;
	 @FXML private TextField emlTxt;
	 @FXML private TextField pnumTxt;
	 @FXML private PasswordField pswrd1Txt;
	 @FXML private PasswordField pswrd2Txt;
	 @FXML private Button bckBtn;
	 @FXML private Button crtBtn;
	
	ChooseUserToCreateController cutc;
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
	  * if all field are filled in
	  * sets data to the server
	  * @throws IOException
	 * @throws InterruptedException 
	  */
	 public void createNewUser(ActionEvent event) throws IOException, InterruptedException {							////////*hide window if neccessary param ActionEvent event -->event(bla bla).hide()
			if(checkRequiredFields()) 												//check required fields are ok
			{
				if(!pswrd1Txt.getText().equals(pswrd2Txt.getText())) {				//check matching passwords
					GeneralMessageController.showMessage("Passwords are not the same\nPlease try again");
					return;
				}
				else {
					if(this.antity=="Service Expert")						//check which user to create. create store manager
					{
						ServiceExpertEntity se = new ServiceExpertEntity();
						se.setEmailAddress(emlTxt.getText());
						se.setPhoneNumber(pnumTxt.getText());
						se.setPassword(pswrd1Txt.getText());
						se.setUserName(nmTxt.getText());
						//sm.setWorkerid(Long.parseLong(idTxt.getText()));
						se.setID(Long.parseLong(idTxt.getText()));
						se.setUserType("SE");
						
						MessageToSend msg=new MessageToSend(se, "createUser");			//defining the job for the server
						Client.getClientConnection().setDataFromUI(msg);					//arranging the sending of the wanted message
						Client.getClientConnection().accept();								//sending data to server
					}
					else if(this.antity=="Customer Servise Worker")				//Create store worker
					{
						CustomerServiceWorkerEntity csw = new CustomerServiceWorkerEntity();
						csw.setID(Long.parseLong(idTxt.getText()));
						csw.setEmailAddress(emlTxt.getText());
						csw.setPassword(pswrd1Txt.getText());
						csw.setPhoneNumber(pnumTxt.getText());
						csw.setUserName(nmTxt.getText());
						csw.setUserType("CSW");
						//sw.setWorkerid(Integer.parseInt(idTxt.getText()));
						
						MessageToSend msg=new MessageToSend(csw, "createUser");			//defining the job for the server
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
				
			}
			}
			else 
			{
				GeneralMessageController.showMessage("Please fill in all the required fields");
			}
			}
			
			
			/**
			 * when create button pressed
			 * check if all required fields are filled in
			 * @return false if there is empty required field
			 */
			public boolean checkRequiredFields() {
				if(nmTxt.getText().isEmpty() ||idTxt.getText().isEmpty() || pnumTxt.getText().isEmpty() || emlTxt.getText().isEmpty() || pswrd1Txt.getText().isEmpty() || pswrd2Txt.getText().isEmpty())
					return false;

				return true;
			}
	
			
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
