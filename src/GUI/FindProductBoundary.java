package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class FindProductBoundary implements Initializable{
	@FXML private Button srchProdBtn;
	@FXML private TextField srchIDfld;
	@FXML private Button bckMnuBtn;
	//
	private int port;
	private MainBoundary main;
	
	private String id="";
	private ProductFromDBBoundary pfdb;
	
	public void setConnectionData(int port,MainBoundary main)	//set the host port and the previous window for the controller
	{
		this.port=port;
		this.main = main;
	}
	
	public void setID(String s) {
		this.id=s;
	}
public void searchProductID(ActionEvent event) throws IOException, InterruptedException {		//if pressed "Search"
		
		if(srchIDfld.getText().trim().isEmpty())  {	//check if the search field is empty
			
			GeneralMessageBoundary message = new GeneralMessageBoundary();
			message.showGeneralMessage("Search field is empty.\nPlease insert ID to search.");		//show a message 
			

			}
			
			
		else {
			this.setID(srchIDfld.getText());	//collect the ID entered
			User chat = new User(MainBoundary.getHost(), this.port,this.id,2);		//last parameter (2) is for telling if we inserting product or searching product (1-insert ; 2-search)
			chat.accept(); 	 //Wait for console data
			ArrayList<String> data=null;
			while(!chat.getConfirmationFromServer())
				Thread.sleep(100);			//wait for server's message
			data=chat.getArrayListfromSrvr();	//get the message returned from the server
				
			
			if(!(data.isEmpty()))	//check if product ID is found in the Data Base
					{
						((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
						
						this.pfdb=new ProductFromDBBoundary();
						this.pfdb.showProductDetails(data, this.main);	//send the data and main instance to the product view window
						
					}
			else	//if no such ID found show error
			{
				
				GeneralMessageBoundary message = new GeneralMessageBoundary();
				message.showGeneralMessage("There is no such ID in the DataBase.\nPlease try again.");		//show a message 
				
			}

		}
	}	
	public void backToMainMenu(ActionEvent event) throws IOException	//when click "Back" return to main menu
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.main.showMainMenu(event);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
