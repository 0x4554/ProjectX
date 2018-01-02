package GUI;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Node;

public class AccountController {

	private int port;
	private CustomerMenuController main;
	
	/*lmlklj*/
	/*public void setConnectionData(int port,MainBoundary main)	//set the host port and the previous window for the controller
	{
		this.port=port;
		this.main = main;
	}
	
	public void backToMainMenu(ActionEvent event) throws IOException	//when click "Back" return to main menu
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.main.showMainMenu(event);
	}*/
}
