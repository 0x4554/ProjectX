package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.SurveyEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import logic.MessageToSend;

public class ChooseSurveyNumberController implements Initializable{
	
    @FXML
    private ComboBox<Integer> srvCmbBox;

    @FXML
    private Button bckBtn;

    @FXML
    private Button cntBtn;
    
    private StoreWorkerMenuController swmc;
    
    /**
     * method for connecting the screens
     * 
     * @param storeWorkerMenu
     */
	public void setConnectionData(StoreWorkerMenuController storeWorkerMenu) {
		swmc = storeWorkerMenu;
	}
	
	/**
	 * handler method if the continue button was pressed
	 * 
	 * 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void continueToSurvey(ActionEvent event) throws IOException, InterruptedException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/SurveyBoundary.fxml").openStream());
		SurveyController sc=loader.getController();
		sc.setConnectionData(this);
		
		MessageToSend toServer = new MessageToSend(null, "getSurveyQs");
		Client.getClientConnection().setDataFromUI(toServer);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		String []qs=(String [])Client.getClientConnection().getMessageFromServer().getMessage();
		
		sc.setLabels(qs);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("New Survey");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
    
    
    
    
    
    
    
    
	/**
	 * loads the previous window
	 * @param event
	 * @throws IOException
	 */
	public void bckToPrevMnu(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.swmc.showMenu();
	}
	
	
	/**
	 * This method loads the store worker menu boundary
	 * @throws IOException
	 */
	public void showMenu() throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChooseSurveyNumberBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		ChooseSurveyNumberController chs = loader.getController();					//set the controller to the FindProductBoundary to control the SearchProductGUI window
		chs.setConnectionData(this.swmc);
		primaryStage.setTitle("Store worker main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
