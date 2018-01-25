package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import client.Client;
import entities.ComplaintEntity;
import entities.OrderEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.MessageToSend;
import logic.TimeCalculation;

/**
 * This class is the controller for the customer service worker handle complaint boundary
 * 
 * CustomerServiceWorkerHandleComplaintController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerServiceWorkerHandleComplaintController implements Initializable {

	private final long Hours_To_Reply = 24;
	@FXML
	private TextArea rplyTxtArea;

	@FXML
	private CheckBox rfndChkBx;

	@FXML
	private TextField amntRfdTxtBx;

	@FXML
	private Button bckBtn;

	@FXML
	private Button rplyTCsmrBtn;
	
	@FXML 
	private Label cmplntTmToRplyLbl;
	private ComplaintEntity complaint;
	private OrderEntity order;

	
	/**
	 * This method handles the select refund check box
	 * @param event	pressed the check box
	 */
	public void selectedRefund(ActionEvent event) {
		if(this.rfndChkBx.isSelected())					//if refund check box is selected 
			this.amntRfdTxtBx.setVisible(true);			//show refund text field
		else
			this.amntRfdTxtBx.setVisible(false);

	}
	
	/**
	 * Setter for the complaint entity
	 * @param complaint
	 */
	public void setComplaint(ComplaintEntity complaint,OrderEntity order)
	{
		this.complaint = complaint;
		this.order=order;
	}
	
	/**
	 * This method checks if the complaint was not handled within 24 hours
	 * If not, forces a full refund as compensation
	 */
	public void checkIfLaterResponse()
	{
		Long timeDiff = TimeCalculation.calculateTimeDifference(new Timestamp(System.currentTimeMillis()), complaint.getFiledOn());
		if((timeDiff = Hours_To_Reply - TimeUnit.MILLISECONDS.toHours(timeDiff)) < 0)		//check if over 48 hours
		{
			this.cmplntTmToRplyLbl.setText("Over "+Hours_To_Reply+" hours has passed, automatic full refund.");
			this.rfndChkBx.setSelected(true);
			this.rfndChkBx.setDisable(true);
		}
		
	}
	
	/**
	 * This method handles the reply action
	 * @param event	pressed reply
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void replyToCustomer(ActionEvent event) throws IOException, InterruptedException {
		ComplaintEntity complaint = new ComplaintEntity();
		if(this.rplyTxtArea.getText().isEmpty())
		{
			GeneralMessageController.showMessage("Please write a reply");
			return;
		}
		
		if(this.rfndChkBx.isSelected() && !this.rfndChkBx.isDisabled() && (this.amntRfdTxtBx.getText().isEmpty() || Double.parseDouble(this.amntRfdTxtBx.getText()) <= 0))	//check if filled compensation
		{
			GeneralMessageController.showMessage("Enter valid amount for compensation, or uncheck the check box");
			return;
		}
		
		else if(this.rfndChkBx.isSelected())			//if selected a compensation
		{
			if(this.rfndChkBx.isDisabled())								//if response is late and requires a full refund
				complaint.setCompensation(order.getTotalPrice());
			else															//else set compensation amount
				complaint.setCompensation(Double.parseDouble(this.amntRfdTxtBx.getText()));
		}
		complaint.setStoreReply(this.rplyTxtArea.getText());
		complaint.setOrderID(this.complaint.getOrderID()); 			//set the order id for the complaint
		
		MessageToSend message = new MessageToSend(complaint, "handleComplaint");
		Client.getClientConnection().setDataFromUI(message);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		
		String messageFromServer = (String)m.getMessage();
		backToComplaints(event);
		GeneralMessageController.showMessage(messageFromServer);

	}
	
	/**
	 * This method handles the back to complaint window
	 * @param event	pressed back
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void backToComplaints(ActionEvent event) throws IOException, InterruptedException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerComplaintsBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerServiceWorkerComplaintController cswmcc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		cswmcc.showComplaints();
		
		primaryStage.setTitle("Complaints");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Initializer
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.amntRfdTxtBx.setVisible(false);
	}

}
