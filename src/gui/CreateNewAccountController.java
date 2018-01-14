package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.ConnectedClients;

/**
 * 
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yaakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */

public class CreateNewAccountController implements Initializable {

	@FXML private Label usrLbl;
    @FXML private Label idLbl;
    @FXML private Label pswrdLbl;
    @FXML private Label pswrd2Lbl;
    @FXML private Label subscrptLbl;
    @FXML private Label crdLbl;
    @FXML private TextField usrFld;
    @FXML private TextField idFld;
    @FXML private PasswordField pswrdFld;
    @FXML private PasswordField pswrd2Fld;
    @FXML private TextField crdFld;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;
    @FXML private ComboBox subscrptCmb;

    private Client clnt;
    ObservableList<String> list;

    /**
     * This method is the constructor for this class
     * @param clnt the connected client
     */
    public CreateNewAccountController(Client clnt)
	{
		this.clnt=clnt;
	}
    
    
	/**
	 * A necessary constructor for the App
	 */
	public CreateNewAccountController() {			//Necessary empty constructor 
		
	}
	
	/**
	 * This method is the constructor for this class
	 * @param clnt
	 */
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	
	
	private void subscriptionComboBox()
	{
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Mounthly");
		al.add("Yearly");
		al.add("None");
				
		list = FXCollections.observableArrayList(al);
		subscrptCmb.setItems(list);
	
	}
	
	
	public void ShowNewUserAcount() throws IOException {
			
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("NewAccountBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CreateNewAccountController nac = loader.getController();	//set the controller to the NewAccountController
		nac.setConnectionData(this.clnt);
		primaryStage.setTitle("New Customer");
		primaryStage.setScene(scene);
		primaryStage.show();
			
			
	}
	
	/**
	 * when "create" button pressed
	 * checks if all data is correct
	 * checks if all required fields are filled in
	 * @param event
	 * @throws IOException
	 */

	public void pressedCreateAccount(ActionEvent event) throws IOException  {
		if(this.usrFld.getText().isEmpty() && this.idFld.getText().isEmpty() && this.pswrdFld.getText().isEmpty() && this.pswrd2Fld.getText().isEmpty() && this.crdFld.getText().isEmpty())			//if all fields are empty
			GeneralMessageController.showMessage("All fields are empty!\nPlease fill the fields");
		
		else if(this.usrFld.getText().isEmpty())
			GeneralMessageController.showMessage("Please enter user name");
		
		else if(this.idFld.getText().isEmpty())		
			GeneralMessageController.showMessage("Please enter user ID");
		else if(this.pswrdFld.getText().isEmpty() && this.pswrd2Fld.getText().isEmpty())
			GeneralMessageController.showMessage("Please enter password");
		else if(!(this.pswrdFld.getText().equals(this.pswrd2Fld.getText())))					//check if password in field1 = password in field2;
		{
			GeneralMessageController.showMessage("Password doeas not match!\nPlease enter again");
			this.pswrdFld.clear();
			this.pswrd2Fld.clear();
		}
		else if(this.subscrptCmb.getSelectionModel().isEmpty())
			GeneralMessageController.showMessage("Please enter subscription");
	/*	else
			GeneralMessageController.showMessage("succeed");*/
		
//		this.clnt.setDataFromUI(username_password, "login!");	//set the data and the operation to send from the client to the server
//		this.clnt.accept();	//sends to server
//		while(!this.clnt.getConfirmationFromServer())	//wait until server replies
//			Thread.sleep(100);
//		this.clnt.setConfirmationFromServer();		//reset confirmation to false
		}
	
	
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		subscriptionComboBox();
	}
	
	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */
	public void back(ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ManagerMenuBoundary.fxml").openStream());
		
		Stage primaryS=new Stage();
		Scene scene=new Scene(root);
		
		primaryS.setTitle("Generate Report");
		primaryS.setScene(scene);
		primaryS.show();
	}
	
}
