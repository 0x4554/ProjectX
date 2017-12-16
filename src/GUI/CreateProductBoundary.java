package GUI;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.User;

import client.User;
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

public class CreateProductBoundary implements Initializable{
	
	@FXML private Button bckBtn;
	@FXML private Button crtBtn;
	@FXML private TextField idFld;
	@FXML private TextField nmFld;
	@FXML private TextField typFld;
	
	private String host ="";
	private int port;
	private MainBoundary main;
	public CreateProductBoundary(String host,int port,MainBoundary main)	
	{
		this.host=host;
		this.port=port;
		this.main = main;
	}
	
	public CreateProductBoundary()	//necessary empty constructor
	{
		
	}

	public void showNewProductGUI() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("NewProductGUI.fxml").openStream());
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			CreateProductBoundary cpb = loader.getController();	//get controller
			cpb.setConnectionData(this.host,this.port,this.main);	//set the host port and previous window for the controller
			primaryStage.setTitle("Search for Product");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	public void getNewData(ActionEvent event) throws IOException, InterruptedException {	//when pressed "Create" button
		
		if(!(this.idFld.getText().isEmpty()	||	this.nmFld.getText().isEmpty()	||	this.typFld.getText().isEmpty()))	//if all fields are properly filled
		{
			String newData="";
			newData=newData+idFld.getText()+" "+nmFld.getText()+" "+typFld.getText();	//set the new data as string
			User chat = new User(this.host, port,newData,1);
			chat.accept();	//send and receive form server
			
			String data=null;
			Thread.sleep(2000);
			data=chat.getStringFromServer();		//get the message returned from the DB via the server
			GeneralMessageBoundary message = new GeneralMessageBoundary();
			message.showGeneralMessage(data);		//show a message if succeeded or failed
			
			this.idFld.clear();	//clear all text fields after insert
			this.nmFld.clear();
			this.typFld.clear();
		}
		else	//if a field is empty show error msg
		{
			GeneralMessageBoundary message = new GeneralMessageBoundary();
			message.showGeneralMessage("One or more of the fields are missing.\nPlease fill all fields.");		//show a message 
			/*
			Stage secondaryStage=new Stage();
			Parent root= FXMLLoader.load(getClass().getResource("ErrorInputGUI.fxml"));
			Scene scene=new Scene(root);
			secondaryStage.setTitle("Error");
			secondaryStage.setScene(scene);
			secondaryStage.show();	*/
		}
	}
	
	public void setConnectionData(String host,int port,MainBoundary main)	//set the host port and the previous window for the controller
	{
		this.host=host;
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



