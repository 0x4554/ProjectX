package gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.VerbalReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.MessageToSend;

/**
 * This class is the controller for the service worker for saving the verbal report from the service expert
 * 
 * CustomerServiceWorkerSaveVerbalReportsController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerServiceWorkerSaveVerbalReportsController implements Initializable {

	@FXML
    private ListView<String> rprtsLstVw;

    @FXML
    private Button svRprtBtn;
    
    private ArrayList<VerbalReportEntity> listOfReports;
    private ObservableList<String> obsListOfReports;
    private CustomerServiceWorkerMenuController cswmc;

    
    /**
     * method for connecting the screens together
     * 
     * @param cswMenu
     */
	public void setConnectionData(CustomerServiceWorkerMenuController cswMenu) {
		// TODO Auto-generated method stub
		this.cswmc = cswMenu;
	}
    
    
    /**
     * This method loads and show the reports from the DB
     * @throws InterruptedException	for the sleep
     */
	public void showReports() throws InterruptedException {

		MessageToSend message = new MessageToSend("", "getVerbalReports");
		Client.getClientConnection().setDataFromUI(message);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		
		message = Client.getClientConnection().getMessageFromServer();
		
		
		listOfReports = (ArrayList<VerbalReportEntity>)message.getMessage();
		
		this.rprtsLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		//unable multiple selection
		
		this.obsListOfReports = FXCollections.observableArrayList();		//the observable list to enter to the list  view
		
		for(VerbalReportEntity verbalReport : listOfReports)		//build list view to contain all orders
			this.obsListOfReports.add("Report date : "+verbalReport.getDate().toString());
		
		
		this.rprtsLstVw.setItems(this.obsListOfReports);		//set items to the list
	}
	
	
	/**
	 * This method saves the verbal report to the file system
	 * @param event presses the save report
	 * @throws IOException 
	 */
	public void saveVerbalReport(ActionEvent event) throws IOException
	{
		if(!rprtsLstVw.getSelectionModel().isEmpty()) {
			for (VerbalReportEntity verbalReport : listOfReports)
			{
				if(this.rprtsLstVw.getSelectionModel().getSelectedItem().substring(14).equals(verbalReport.getDate().toString())) 		//check which order was selected
				{
					Stage secondaryStage=new Stage();
		
					FileChooser fileChooser = new FileChooser();										//new file chooser
					fileChooser.setTitle("Save file");
					fileChooser.setInitialFileName(verbalReport.getDate().toString());					//default file name
					File dest = fileChooser.showSaveDialog(secondaryStage);								//get the file path from the file chooser
					
					if(dest != null)																	//check if a path was chosen
					{
						try (FileOutputStream fos = new FileOutputStream(dest.toString())) {			//create a new file using the path
						   fos.write(verbalReport.getFile());											//write the content of the file
						   fos.close();
						   GeneralMessageController.showMessage("File saved.");
							}
						catch(Exception e)
						{
							e.printStackTrace();
						} 
					}

				}
			}
		}
		else {
			GeneralMessageController.showMessage("Please choose report to save");
		}
	}


	
	/**
	 * method for sending back to the previous screen
	 * 
	 * @param event
	 * @throws IOException 
	 */
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.cswmc.showCostumerServiceWorkerMenu();
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}


}
