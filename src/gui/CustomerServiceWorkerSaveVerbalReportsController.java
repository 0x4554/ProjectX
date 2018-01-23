package gui;

import java.io.File;
import java.io.FileOutputStream;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.MessageToSend;

public class CustomerServiceWorkerSaveVerbalReportsController implements Initializable {

	@FXML
    private ListView<String> rprtsLstVw;

    @FXML
    private Button svRprtBtn;
    
    ArrayList<VerbalReportEntity> listOfReports;
    ObservableList<String> obsListOfReports;

    
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
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		
		
		listOfReports = (ArrayList<VerbalReportEntity>)m.getMessage();
		
		this.rprtsLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		//unable multiple selection
		
		this.obsListOfReports = FXCollections.observableArrayList();		//the observable list to enter to the list  view
		
		for(VerbalReportEntity verbalReport : listOfReports)		//build list view to contain all orders
		{
			this.obsListOfReports.add("Report date : "+verbalReport.getDate().toString());
		}
		
		this.rprtsLstVw.setItems(this.obsListOfReports);		//set items to the list
	}
	
	
	/**
	 * This method saves the verbal report to the file system
	 * @param event presses the save report
	 */
	public void saveVerbalReport(ActionEvent event)
	{
		for (VerbalReportEntity verbalReport : listOfReports)
		{
			if(this.rprtsLstVw.getSelectionModel().getSelectedItem().substring(14).equals(verbalReport.getDate().toString())) 		//check which order was selected
			{
				Stage secondaryStage=new Stage();
		//		InputStream is = FilesConverter.
				 FileChooser fileChooser = new FileChooser();
				   fileChooser.setTitle("Save file");
				   File dest = fileChooser.showSaveDialog(secondaryStage);
//				   if (dest != null) {
//				       try {
//				           Files.copy(file.toPath(), dest.toPath());
//				       } catch (IOException ex) {
//				           // handle exception...
//				       }
				try (FileOutputStream fos = new FileOutputStream(dest.toString())) {
					   fos.write(verbalReport.getFile());
					   fos.close();
					}
				catch(Exception e)
				{
					e.printStackTrace();
				}
//				File file = new File("Report date : "+verbalReport.getDate().toString());
				  
				//   fileChooser.showSaveDialog(secondaryStage);
				   

			}
		}
//		FileChooser fileChooser = new FileChooser();
//		Stage secondaryStage=new Stage();
//        //Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//        fileChooser.getExtensionFilters().add(extFilter);
//        
//        //Show save file dialog
//        File file = fileChooser.showSaveDialog(secondaryStage);
//        
//        if(file != null){
//            SaveFile(Santa_Claus_Is_Coming_To_Town, file);
	}

//	private void SaveFile(String content, File file) {
//		try
//		{
//			FileWriter fileWriter = null;
//
//			fileWriter = new FileWriter(file);
//			fileWriter.write(content);
//			fileWriter.close();
//		} catch (IOException ex)
//		{
//			Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, ex);
//		}
//
//	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try
		{
			showReports();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
