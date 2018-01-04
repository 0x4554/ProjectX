package gui;



import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.ConnectedClients;

public class CreateProductController implements Initializable{
	
	@FXML
	private Button bckBtn;
	@FXML
	private Button crtBtn;
	@FXML
	private TextField idFld;
	@FXML
	private TextField nmFld;
	@FXML
	private TextField typFld;
	
	private int port;
	private MainBoundary main;
	private String username;
	private Client clnt;
	
	public CreateProductController(int port,MainBoundary main,Client clnt)	
	{
		this.port=port;
		this.main = main;
		this.clnt = clnt;
	}
	
	public CreateProductController()	//necessary empty constructor
	{
		
	}

	public void showNewProductGUI() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("NewProductGUI.fxml").openStream());
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			CreateProductController cpb = loader.getController();	//get controller
			cpb.setConnectionData(this.port,this.main);	//set the host port and previous window for the controller
			primaryStage.setTitle("Search for Product");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	public void getNewData(ActionEvent event) throws IOException, InterruptedException {	//when pressed "Create" button
		
		if(!(this.idFld.getText().isEmpty()	||	this.nmFld.getText().isEmpty()	||	this.typFld.getText().isEmpty()))	//if all fields are properly filled
		{
			String newData="";
			newData=newData+idFld.getText()+"~"+nmFld.getText()+"~"+typFld.getText();	//set the new data as string
			this.clnt.setDataFromUI(newData, "createProduct!");				//set the data and the operation using the connected client
			this.clnt.accept();									//send and receive form server
			while(!this.clnt.getConfirmationFromServer())		//wait for confirmation from the server
				Thread.sleep(100);
			this.clnt.setConfirmationFromServer(); 				//set the confirmation back to false for next use
			String data=null;
			data=this.clnt.getStringFromServer();				//get the message returned from the DB via the server
			GeneralMessageController message = new GeneralMessageController();
			message.showGeneralMessage(data);					//show a message if succeeded or failed
				
			this.idFld.clear();									//clear all text fields after insert
			this.nmFld.clear();
			this.typFld.clear();
		}
		else	//if a field is empty show error msg
		{
			GeneralMessageController message = new GeneralMessageController();
			message.showGeneralMessage("One or more of the fields are missing.\nPlease fill all fields.");		//show a message 
			
		}
	}
	
	public void setConnectionData(int port,MainBoundary main)	//set the host port and the previous window for the controller
	{
		this.port=port;
		this.main = main;
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