package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	final public static int DEFAULT_PORT = 5555;
	
	@FXML
	private TextField usrNmTxtFld;
	@FXML
	private TextField psswrdTxtFld;
	@FXML
	private TextField srvrIPTxtFld;
	@FXML
	private Button lgnBtn;
	@FXML
	private Button crtNwAccntBtn;
	
	public static String hostIP;
	
	public void setHost(String parameters) {	//set the host's IP to static String
		LoginController.hostIP=parameters;
	}
	
	public static String getHost()	//static method for holding the host's IP (accessible to all classes using static String)
	{
		return LoginController.hostIP;
	}
	
	public void pressedLogin(ActionEvent event) throws IOException, InterruptedException	//when pressed login button
	{
		if(this.usrNmTxtFld.getText().isEmpty()&& this.psswrdTxtFld.getText().isEmpty() && this.srvrIPTxtFld.getText().isEmpty())	//check if all fields are empty
		{
			GeneralMessageBoundary msg = new GeneralMessageBoundary();
			msg.showGeneralMessage("All fields are Empty.\nPlease fill all fields.");
		} 
		else if (this.usrNmTxtFld.getText().isEmpty() || this.psswrdTxtFld.getText().isEmpty())	//check if user's login info fields are empty
		{	
			GeneralMessageBoundary msg = new GeneralMessageBoundary();
			msg.showGeneralMessage("Please fill all user's login information fields");
		} 
		else if (this.srvrIPTxtFld.getText().isEmpty()) //check if server's ip field is empty
		{		
			GeneralMessageBoundary msg = new GeneralMessageBoundary();
			msg.showGeneralMessage("Server IP is missing.\nMust fill server's IP for connecting");
		} 
		else 
		{
			String username_password = "";
			username_password = username_password + this.usrNmTxtFld.getText()+'~'+this.psswrdTxtFld.getText();	//set the new data as string
			
			try {
			ArrayList<String> dataFromServer = null;
			Client chat = new Client(LoginController.getHost(), DEFAULT_PORT, username_password, 1);	//attempt to create a connection from client to server
			chat.accept();	//sends to server
			while(!chat.getConfirmationFromServer())	//wait until server replies
				Thread.sleep(100);
			dataFromServer = chat.getArrayListfromSrvr();	//get the returned ArrayList from the server
			if(dataFromServer.get(0).equals("success"))//if login info matches the data base
			{
				GeneralMessageBoundary msg = new GeneralMessageBoundary();
				msg.showGeneralMessage("u did it");
			}
			else	//check for reason of failure
			{
				if(dataFromServer.get(1).equals("user does not exists"))	//user does not exists
				{
					GeneralMessageBoundary msg = new GeneralMessageBoundary();
					msg.showGeneralMessage("User does not exists in the system.\nPlease check the entered username or contact a store for creating a new user.");
				}
				else if (dataFromServer.get(1).equals("user is blocked"))	//user is blocked
				{
					GeneralMessageBoundary msg = new GeneralMessageBoundary();
					msg.showGeneralMessage("The user is blocked from to many failed attempts to login.\nPlease contact a store.");
				}
				else if (dataFromServer.get(1).equals("password does not match"))	//password does not match
				{
					GeneralMessageBoundary msg = new GeneralMessageBoundary();
					String message = "";
					//show message of how many login attempts are left before blocking the user
					message = message +"Password entered does not match the username.\nPlease try again.\n" + (3-Integer.parseInt(dataFromServer.get(2))) + "/3 attempts left before blocking account!!!";
					msg.showGeneralMessage(message);
				}
			}
			}
			catch(IOException e){	//if there were a connection exception
				GeneralMessageBoundary msg = new GeneralMessageBoundary();
				msg.showGeneralMessage("Failed connecting to the server.\nCheck entered IP");
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

}
