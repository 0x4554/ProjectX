package gui;
/**
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This class was made by the ProjectX team
 * the class handles the creation of new complaints in the system
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComplaintController implements Initializable{
	
	@FXML
	private TextField ordNumTxtFld;
	
    @FXML
    private TextArea cmpDtsTxtArea;

    @FXML
    private Button upldBtn;

    @FXML
    private Button sndBtn;

    @FXML
    private Button cnclBtn;

    @FXML
    private TextField picPathTxtFld;
	
	
	private CustomerMenuController cstmc;
	

	public ComplaintController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setConnectionData(CustomerMenuController cmc) {
		this.cstmc=cmc;
	}
	
	
	public void getComplaintData(ActionEvent event) throws IOException, InterruptedException{					//////////////////////////
		String complaintDetails="";
		String pctr;
		String details;
		
		if(!ordNumTxtFld.getText().isEmpty()) {
			complaintDetails=ordNumTxtFld.getText();
			complaintDetails+="|";
		}
		
		if(!cmpDtsTxtArea.getText().isEmpty()) {					//if complaint inserted
				details=cmpDtsTxtArea.getText();					
		details=details.replaceAll(" ", "~");						//for handling the message later
		complaintDetails+=details;
		
		if(!picPathTxtFld.getText().isEmpty()) 						//if path to picture uploaded for sending it as avidence
				pctr=picPathTxtFld.getText();
				
				Client c=this.cstmc.getClient();
				c.setDataFromUI(complaintDetails, "complaint!");
				c.accept();
				if(c.getStringFromServer().equals("failed"))				
					GeneralMessageController.showMessage("Order does not exist");
			
				else if(c.getStringFromServer().equals("Added")){
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cstmc.showCustomerMenu();								//back to main menu
		GeneralMessageController.showMessage("Dear customer, we got your complaint\nand we are doing everything we can\nto make it up to you");			//message to present when complaint succeeded
		return;
				}
		}
		else {
			GeneralMessageController.showMessage("Please fill in your complaint");		//if nothing was inserted show general message
		}
	}
	
	/**
	 * this method handles the complaint cancellation, and sends to the previous screen
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void cancelComplaint(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cstmc.showCustomerMenu();
		return;
	}
	
	
	/**
	 * this method uploads a picture of complaint
	 * 
	 * @param path - client side file location in file-system
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void uploadPhoto(String path) throws IOException, InterruptedException {
		String res;
		Client c = this.cstmc.getClient();
		c.setDataFromUI(path, "downloadFile!");
		c.accept();
		while(!c.getConfirmationFromServer())
			Thread.sleep(100);
		c.uploadFileToServer(5556, path);
		c.setConfirmationFromServer();
	}
	
	
	public void bckToMainMenu(ActionEvent event)throws IOException{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cstmc.showCustomerMenu();									//open previous menu
		return;
	}
	


	public void searchForPhoto(ActionEvent event) throws IOException{
		Node node = (Node) event.getSource();
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Choose File");
	    chooser.showOpenDialog(node.getScene().getWindow());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	
}
