package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import entities.SurveyEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import logic.MessageToSend;

public class SurveyController implements Initializable {
	
	@FXML
	private ToggleGroup q1;
	@FXML
	private ToggleGroup q2;
	@FXML
	private ToggleGroup q3;
	@FXML
	private ToggleGroup q4;
	@FXML
	private ToggleGroup q5;
	@FXML
	private ToggleGroup q6;
	
	@FXML
	private Button sbmtBtn;
	@FXML
	private Button bckBtn;
	
	@FXML
	private Label missingLbl;
	
    @FXML
    private Label q1Lbl;
	
	@FXML
    private Label q2Lbl;

    @FXML
    private Label q3Lbl;

    @FXML
    private Label q4Lbl;
	
    @FXML
    private Label q5Lbl;

    @FXML
    private Label q6Lbl;
	
	private SurveyEntity srvy;
	private StoreWorkerMenuController swmc;
	
	public void setConnectionData(StoreWorkerMenuController storeWorkerMenu) {
		swmc = storeWorkerMenu;
		srvy=new SurveyEntity();
	}
	
	
	public boolean validateAnswers() {
		if(q1.getSelectedToggle()==null || q2.getSelectedToggle()==null || q3.getSelectedToggle()==null || q4.getSelectedToggle()==null || q5.getSelectedToggle()==null || q6.getSelectedToggle()==null)
			return false;
		
		return true;
	}


	public void getSurveyAnswers(ActionEvent event) throws IOException, InterruptedException {
		if(!validateAnswers())
			missingLbl.setVisible(true);
		
		else {
			RadioButton r1=(RadioButton)q1.getSelectedToggle();
			RadioButton r2=(RadioButton)q2.getSelectedToggle();
			RadioButton r3=(RadioButton)q3.getSelectedToggle();
			RadioButton r4=(RadioButton)q4.getSelectedToggle();
			RadioButton r5=(RadioButton)q5.getSelectedToggle();
			RadioButton r6=(RadioButton)q6.getSelectedToggle();
			srvy.setAnswers(Integer.parseInt(r1.getText()), Integer.parseInt(r2.getText()), Integer.parseInt(r3.getText()), Integer.parseInt(r4.getText()), Integer.parseInt(r5.getText()), Integer.parseInt(r6.getText()));
			//this.srvy.setAnswers(1, 1, 2, 2, 3, 3);
			this.submitSurveyAnswers(event,this.srvy);
		}
	}
	
	
	/**
	 * Saves the survey answers in the database
	 * 
	 * @param event - event to hide window when done
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void submitSurveyAnswers(ActionEvent event,SurveyEntity se) throws InterruptedException, IOException {
		MessageToSend toServer = new MessageToSend(srvy,"SurveyAnswers");
		Client.getClientConnection().setDataFromUI(toServer);
		Client.getClientConnection().accept();
		
		while(!Client.getClientConnection().getConfirmationFromServer())
			Thread.sleep(100);
		
		Client.getClientConnection().setConfirmationFromServer();
		
		if(Client.getClientConnection().getMessageFromServer().getMessage().equals("Problem"))
			GeneralMessageController.showMessage("There was a problem\nPlease inform the technical support and try again later");
		else{
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			this.swmc.showStoreWorkerMenu();
			GeneralMessageController.showMessage("Survey was sent successfully");
		}
		
		
		
	}
	
	
	public void setLabels(String[] se) {
		q1Lbl.setText(se[0]);
		q2Lbl.setText(se[1]);
		q3Lbl.setText(se[2]);
		q4Lbl.setText(se[3]);
		q5Lbl.setText(se[4]);
		q6Lbl.setText(se[5]);
	}
	
	
	public void bckToPrevMnu(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.swmc.showStoreWorkerMenu();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		missingLbl.setVisible(false);
	}
	
	
	
	
}