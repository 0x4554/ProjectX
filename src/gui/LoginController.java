package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
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
import logic.ConnectedClients;
import logic.MessageToSend;

/**
 * This class uses as the controller to the log in menu and the log in process
 * LoginController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class LoginController implements Initializable {

	final public static int DEFAULT_PORT = 5555;
	
	@FXML
	private TextField usrNmTxtFld;
	@FXML
	private PasswordField psswrdTxtFld;
	@FXML
	private TextField srvrIPTxtFld;
	@FXML
	private Button lgnBtn;
	@FXML
	private Button crtNwAccntBtn;
	
	public static String hostIP;
	private Client clnt;
	private CustomerMenuController cmc;
	private ManagerMenuController mmc;
	private AdministratorMenuController amc;
	private StoreWorkerMenuController swmc;
	private CustomerServiceWorkerMenuController cswmc;
	private ChainStoreManagerMenuController csmmc;
	private CustomerServiceExpertMenuController csemc;
	
	/**
	 * This method sets the host's IP to the static parameter
	 * @param parameters The host's ip
	 */
	public void setHost(String parameters) {	//set the host's IP to static String
		LoginController.hostIP=parameters;
	}
	
	/**
	 * This is a static method, used for getting the host's IP to be accessed from each part of the program
	 * @return	the host's ip
	 */
	public static String getHost()	//static method for holding the host's IP (accessible to all classes using static String)
	{
		return LoginController.hostIP;
	}
			
	/**
	 * This method handles the logging in processes
	 * starts when the user presses the log in button
	 * 
	 * @param event	clicked login button
	 * @throws IOException	for the FXMLLoader in GeneralMessageController
	 * @throws InterruptedException	for the Thread.sleep()
	 */
	public void pressedLogin(ActionEvent event) throws IOException, InterruptedException	//when pressed login button
	{
		if(this.usrNmTxtFld.getText().isEmpty()&& this.psswrdTxtFld.getText().isEmpty() && this.srvrIPTxtFld.getText().isEmpty())	//check if all fields are empty
		{
			GeneralMessageController.showMessage("All fields are Empty.\nPlease fill all fields.");
		} 
		else if (this.usrNmTxtFld.getText().isEmpty() || this.psswrdTxtFld.getText().isEmpty())	//check if user's login info fields are empty
		{	
			GeneralMessageController.showMessage("Please fill all user's login information fields");
		} 
		else if (this.srvrIPTxtFld.getText().isEmpty()) //check if server's ip field is empty
		{	
			GeneralMessageController.showMessage("Server IP is missing.\nMust fill server's IP for connecting");
		} 
		else 
		{
			String username_password = "";
			username_password = username_password + this.usrNmTxtFld.getText()+'~'+this.psswrdTxtFld.getText();	//set the new data as string
			MessageToSend mts=new MessageToSend(username_password, "login!");
			ArrayList<String> dataFromServer = null;
			try
			{
			this.clnt = new Client(LoginController.getHost(), DEFAULT_PORT,this.usrNmTxtFld.getText());	//attempt to create a connection from client to server
			}catch(IOException e){	//if there were a connection exception

				GeneralMessageController.showMessage("Failed connecting to the server.\nCheck entered IP");

			}
			this.clnt.setDataFromUI(mts);							//set the data and the operation to send from the client to the server
			this.clnt.accept();										//sends to server
			while(!this.clnt.getConfirmationFromServer())			//wait until server replies
				Thread.sleep(100);
			this.clnt.setConfirmationFromServer();		//reset confirmation to false
			MessageToSend m = this.clnt.getMessageFromServer();
			dataFromServer = (ArrayList<String>)m.getMessage();
//			dataFromServer = this.clnt.getArrayListfromSrvr();	//get the returned ArrayList from the server
			if(dataFromServer.get(0).equals("success"))		//if login info matches the data base
			{	
				try
				{
				((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
				toUserMenu(dataFromServer.get(1));	//call method to sort the user's type
				}catch(Exception e) {	//finally close connection to the server from the client
					e.printStackTrace();
					signalAppClose();	//close connection
				}
			}
			else	//check for reason of failure
			{
				if(dataFromServer.get(1).equals("user does not exists"))	//user does not exists
				{
					GeneralMessageController.showMessage("User does not exists in the system.\nPlease check the entered username or contact a store for creating a new user.");
				}
				else if (dataFromServer.get(1).equals("user is blocked"))	//user is blocked
				{
					GeneralMessageController.showMessage("Too many logins - user is blocked\nPlease contact a store.");
				}
				else if (dataFromServer.get(1).equals("password does not match"))	//password does not match
				{
					String message = "";
					//show message of how many login attempts are left before blocking the user
					message = message +"Password entered does not match the username.\nPlease try again.\n" + (3-Integer.parseInt(dataFromServer.get(2))) + "/3 attempts left before blocking account!!!";
					if(dataFromServer.get(2).equals("3"))	//check if the attempt was the third attempt
						message += "\nThe user is now blocked.Please contact a store.";
					GeneralMessageController.showMessage(message);
				} 
				else if (dataFromServer.get(1).equals("user is already logged in"))	//user is already logged in
				{
					GeneralMessageController.showMessage("User is already connected to the system.");
				}
			}
		}
	}
	
	/**
	 * This method sort the menu to present to the UI depending on the user's type
	 * @param userType the type of the user who logged in
	 * @throws IOException for the FXMLLoader in GeneralMessageController
	 */
	private void toUserMenu(String userType) throws IOException
	{
		//Depending on the user's type (customer,store manager...) choose which menu to present to the UI
		switch (userType)
		{
		case "AD":	//system administrator (system manager)
			amc = new AdministratorMenuController(this.clnt);
			amc.showAdministratorMenu();
			GeneralMessageController.showMessage("Logged in as an administrator");
			break;
		case "C":	//customer
			cmc = new CustomerMenuController(this.clnt,this);
			cmc.showCustomerMenu();
			GeneralMessageController.showMessage("Logged in as a customer");
			break;
		case "SW":	//store worker
			swmc = new StoreWorkerMenuController(this.clnt);
			swmc.showStoreWorkerMenu();
			GeneralMessageController.showMessage("Logged in as a store worker");
			break;
		case "SM":	//store manager
			mmc=new ManagerMenuController(this.clnt);
			mmc.showManagerMenu();
			GeneralMessageController.showMessage("Logged in as a store manager");
			break;
		case "CSW":	//customer service worker
			cswmc = new CustomerServiceWorkerMenuController(this.clnt);
			cswmc.showCostumerServiceWorkerMenu();
			GeneralMessageController.showMessage("Logged in as a customer service worker");
			break;
		case "CSM":	//chain store manager
			csmmc = new ChainStoreManagerMenuController(this.clnt);
			csmmc.showChainStoreManagerMenu();
			GeneralMessageController.showMessage("Logged in as a chain store manager");
			break;
		case "CSE":	//customer service expert
			csemc = new CustomerServiceExpertMenuController(this.clnt);
			csemc.showCustomerServiceExpertMenu();
			GeneralMessageController.showMessage("Logged in as a customer service expert");
			break;
		}
	}
	
	/**
	 * This method handling the signaling of a closing connection from the client to the server
	 */
	public void signalAppClose()
	{
		if(this.clnt!=null) {
		this.clnt.setDataFromUI(new MessageToSend(this.clnt.getUsername(),"exitApp!"));
		this.clnt.accept();
		this.clnt.quit();
		}
	}

	
	/**
	 * This is an implementation of the initialize method for implementing Initializable
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.srvrIPTxtFld.setText("localhost");	//set the default server's ip to local host
		this.usrNmTxtFld.setText("katya");
		this.psswrdTxtFld.setText("123");
	}

}