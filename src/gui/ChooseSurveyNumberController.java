package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.SurveyEntity;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.MessageToSend;

public class ChooseSurveyNumberController implements Initializable{
	
    @FXML
    private ComboBox<Integer> srvCmbBox;

    @FXML
    private Button bckBtn;

    @FXML
    private Button cntBtn;
    
    @FXML
    private Label hidenLbl;
    
    private StoreWorkerMenuController swmc;
    private ObservableList<Integer> list;
	private CustomerServiceWorkerMenuController cswmc;
    
    /**
     * method for connecting the screens
     * 
     * @param storeWorkerMenu
     */
	public void setConnectionData(StoreWorkerMenuController storeWorkerMenu) {
		this.swmc = storeWorkerMenu;
	}
	
	public void setConnectionData(CustomerServiceWorkerMenuController cswMenu) {
		this.cswmc = cswMenu;
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
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		if(srvCmbBox.getSelectionModel().getSelectedItem()!=null) {
		
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/SurveyBoundary.fxml").openStream());
			SurveyController sc=loader.getController();
			sc.setConnectionData(this);
		
			MessageToSend toServer = new MessageToSend(srvCmbBox.getSelectionModel().getSelectedItem(), "getSurveyQs");
			Client.getClientConnection().setDataFromUI(toServer);
			Client.getClientConnection().accept();
		
			while(!Client.getClientConnection().getConfirmationFromServer())
				Thread.sleep(100);
			Client.getClientConnection().setConfirmationFromServer();
		
			String []qs=(String [])Client.getClientConnection().getMessageFromServer().getMessage();
			
			sc.setSurveyNum(srvCmbBox.getSelectionModel().getSelectedItem());
			sc.setLabels(qs);
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
		
			primaryStage.setTitle("New Survey");
			primaryStage.setScene(scene);
			primaryStage.show();
			}
		else {
			GeneralMessageController.showMessage("Pleae choose survey");
		}
	}
    
    
    
	/**
	 * This method loads the update survey window
	 * @param event	pressed update survey
	 * @throws IOException	for the loader
	 * @throws InterruptedException	for the sleep
	 */
	public void updateQuestionsText(ActionEvent event) throws IOException, InterruptedException {
		
		int cnt=0;
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/UpdateSurveyBoundary.fxml").openStream());
		UpdateSurveyController usc = loader.getController();
		usc.setConnectionData(this);
		
		MessageToSend msg=new MessageToSend(srvCmbBox.getSelectionModel().getSelectedItem(),"getSurveyQs");
		Client.getClientConnection().setDataFromUI(msg);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		String[] questions = (String[])Client.getClientConnection().getMessageFromServer().getMessage();
		for(String s:questions)
			if(s==null)
				cnt++;
		if(cnt==0)
			usc.setTextFields(questions);
		
		SurveyEntity se = usc.getNewSurvey();
		se.setSurveyNum(srvCmbBox.getSelectionModel().getSelectedItem());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Update Survey");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
    
	
    /**
     * method to decide which screen will be next
     * 
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void updateOrAnswer(ActionEvent event) throws IOException, InterruptedException {
    	if(srvCmbBox.getSelectionModel().getSelectedItem()!=null) {
    		if(this.swmc!=null)
    			continueToSurvey(event);
    		else
    			updateQuestionsText(event);
    		}
    	else
    		GeneralMessageController.showMessage("Please choose survey to update");
    }
    
    
	/**
	 * loads the previous window
	 * @param event
	 * @throws IOException
	 */
	public void bckToPrevMnu(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		if(this.swmc!=null)
			this.swmc.showMenu();
		else
			this.cswmc.showMenu();
	}
	
	
	/**
	 * method to initialize screens comboBox
	 * 
	 * @param surveys
	 */
	public void initComboBox(ArrayList<Integer> surveys) {
		this.list=FXCollections.observableArrayList(surveys);
		this.srvCmbBox.setItems(this.list);
		
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
		chs.setConnectionData(this.cswmc);
		primaryStage.setTitle("Store worker main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	

	/**
	 *Getter for the cswmc
	 * @return the cswmc
	 */
	public CustomerServiceWorkerMenuController getCswmc() {
		return cswmc;
	}
	
	
	/**
	 * method for loading the combobox data
	 * 
	 * @throws InterruptedException
	 */
	public void loadComboBox() throws InterruptedException {
		MessageToSend toServer = new MessageToSend(null, "getNumberOfSurveys");
		Client.getClientConnection().setDataFromUI(toServer);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();
		
		ArrayList<Integer> allSurveys=(ArrayList<Integer>)Client.getClientConnection().getMessageFromServer().getMessage();

		this.initComboBox(allSurveys);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		hidenLbl.setVisible(false);
		try {
			this.loadComboBox();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
