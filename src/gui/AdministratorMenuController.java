package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import javafx.fxml.Initializable;

public class AdministratorMenuController implements Initializable {

	private Client clnt;

	/**
	 * This method is the constructor for this class
	 * @param clnt	the connected client
	 */
	public AdministratorMenuController(Client clnt)
	{
		this.clnt=clnt;
	}
	/**
	 * A necessary constructor for the App
	 */
	public AdministratorMenuController() {}
	/**
	 * This method saves the client connection to the controller
	 * @param clnt	the connection client
	 */
	
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	public void showAdministratorMenu()
	{
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
