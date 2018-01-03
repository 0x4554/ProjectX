package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import javafx.fxml.Initializable;

public class CustomerServiceWorkerMenuController implements Initializable {

	private Client clnt;

	/**
	 * This method is the constructor for this class
	 * @param clnt	the connected client
	 */
	public CustomerServiceWorkerMenuController(Client clnt)
	{
		this.clnt=clnt;
	}
	/**
	 * A necessary constructor for the App
	 */
	public CustomerServiceWorkerMenuController() {}
	
	/**
	 * This method saves the client connection to the controller
	 * @param clnt	the connection client
	 */
	
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	public void showCostumerServiceWorkerMenu()
	{
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
