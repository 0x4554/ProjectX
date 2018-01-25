package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import client.Client;
import entities.VerbalReportEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.MessageToSend;
/**
 * This class is the controller for the upload verbal report controller
 * 
 * UploadVerbalReportController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class UploadVerbalReportController implements Initializable {

	
    @FXML
    private TextField pathTxtFld;

    @FXML
    private Button brwsBtn;

    @FXML
    private Button upldBtn;

    @FXML
    private Button bckBtn;
    
    private CustomerServiceExpertMenuController csemc;
    
	/**
	 * method for connecting the previous screen to here
	 * 
	 */
	public void setConnectionData(CustomerServiceExpertMenuController admin) {
		csemc=admin;
	}
	
	
	/**
	 * This method searches for a file to add
	 * 	 
	 * @param event	pressed add file
	 * @throws IOException
	 */
	public void searchForFile(ActionEvent event) throws IOException{
		
		Stage secondaryStage=new Stage();
		Node node = (Node) event.getSource();
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose File");
	    try {
		    File f=chooser.showOpenDialog(secondaryStage);
		    String filepath = f.getAbsolutePath();
		    pathTxtFld.setText(filepath);
	    }
	    catch(Exception e) {
		    return;
		    }
	}
	
	
	/**
	 * method for handling the sending of the chosen file of the verbal report
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 */
	public void sendReport(ActionEvent event) throws IOException, InterruptedException {
		if(pathTxtFld.getText().isEmpty())
			GeneralMessageController.showMessage("Please choose file to upload");
		else {
			try {
				File f=new File(pathTxtFld.getText());
			}
			catch(Exception e) {
				GeneralMessageController.showMessage("File does not exist\nPlease try again");
			}
			
		VerbalReportEntity verbalRep = new VerbalReportEntity();
		verbalRep.setFile(pathTxtFld.getText());
		verbalRep.setDate(new Timestamp(System.currentTimeMillis()));
		
		MessageToSend toServer = new MessageToSend(verbalRep,"verbalReport");
		Client.getClientConnection().setDataFromUI(toServer);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		if(Client.getClientConnection().getMessageFromServer().getMessage().equals("uploaded")) {
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			this.csemc.showMenu();
			GeneralMessageController.showMessage("Verbal report uploaded successfully");
		}
		else {
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			this.csemc.showMenu();
			GeneralMessageController.showMessage("There was a problem uploading the report\nPlease contact technical support and try again later");
			}
			
		}
	}
	
	
	/**
	 * method for going back to the previous screen
	 * 
	 * @param event - event for hiding the current screen and starting the previous one
	 * @throws IOException 
	 */
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.csemc.showMenu();
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
