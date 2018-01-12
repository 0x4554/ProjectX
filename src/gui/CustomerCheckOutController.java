package gui;

import java.io.IOException;


import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.DeliveryEntity;
import entities.OrderEntity;
import entities.OrderEntity.CashOrCredit;
import entities.OrderEntity.OrderStatus;
import entities.OrderEntity.SelfOrDelivery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.MessageToSend;

public class CustomerCheckOutController implements Initializable {

	@FXML
	private Button bckToOrdr;
	@FXML 
	private Button plcOrdrBtn;
	@FXML
	private ImageView image;
	@FXML
	private CheckBox slfPckpCkBx;
	@FXML 
	private CheckBox dlvrCkBx;
	@FXML
	private TextField dlvrAddrs;
	@FXML
	private TextField dlvrRcptNm;
	@FXML
	private TextField dlvrPhoneNmbr;
	@FXML
    private CheckBox cshCkBx;
    @FXML
    private CheckBox crdCrdCkBx;
    @FXML
    private TextField crdCrdNmbrTxtFld;
    @FXML
    private TextField crdCrdCCVTxtFld;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField hrsTxtFld;
    @FXML
    private TextField mntsTxtFld;
    @FXML
    private Label totalPriceLabel;
	
	private OrderEntity newOrder;
	/**
	 * This method handles the selected Cash check box
	 * @param event
	 */
	public void selectedCash(ActionEvent event)
	{
		if(this.crdCrdCkBx.isSelected())			//check if the other check box is selected, if true setSelected to false
			this.crdCrdCkBx.setSelected(false);
		
		if(!this.cshCkBx.isSelected())				//make sure at least one check box is selected
			this.cshCkBx.setSelected(true);
		
		this.crdCrdNmbrTxtFld.setVisible(false);			//set the text fields visible
		this.crdCrdCCVTxtFld.setVisible(false);
	}
	/**
	 * This method handles the selected credit card check box
	 * @param event
	 */
	public void selectedCreditCard(ActionEvent event)
	{
		if(this.cshCkBx.isSelected())				//check if the other check box is selected, if true setSelected to false
			this.cshCkBx.setSelected(false);
		
		if(!this.crdCrdCkBx.isSelected())			//make sure at least one check box is selected
			this.crdCrdCkBx.setSelected(true);
		
		this.crdCrdNmbrTxtFld.setVisible(true);
		this.crdCrdCCVTxtFld.setVisible(true);
			}
	/**
	 * This method handles the selected delivery check box
	 * @param event
	 */
	public void selectedDelivery(ActionEvent event)
	{
		if(this.slfPckpCkBx.isSelected())			//check if the other check box is selected, if true setSelected to false
			this.slfPckpCkBx.setSelected(false);
		
		if(!this.dlvrCkBx.isSelected())				//make sure at least one check box is selected
			this.dlvrCkBx.setSelected(true);
		
		Double price = this.newOrder.getTotalPrice()+DeliveryEntity.getDeliveryPrice();		//update price to include delivery
		this.totalPriceLabel.setText(price.toString());
		
		this.dlvrAddrs.setVisible(true);			//set the text fields visible
		this.dlvrPhoneNmbr.setVisible(true);
		this.dlvrRcptNm.setVisible(true);
	}
	/**
	 * This method handles the selected self pickup check box
	 * @param event
	 */
	public void selectedSelfPickup(ActionEvent event)
	{
		if(this.dlvrCkBx.isSelected())				//check if the other check box is selected, if true setSelected to false
			this.dlvrCkBx.setSelected(false);
		
		if(!this.slfPckpCkBx.isSelected())			//make sure at least one check box is selected
			this.slfPckpCkBx.setSelected(true);
		
		this.totalPriceLabel.setText(this.newOrder.getTotalPrice().toString());		//update price 
	
		this.dlvrAddrs.setVisible(false);
		this.dlvrPhoneNmbr.setVisible(false);
		this.dlvrRcptNm.setVisible(false);
	}
	
	/**
	 * This method loads the new order menu
	 * @param event	pressed back to order
	 * @throws IOException
	 */
	public void backToOrder(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
//		selectedStoreName = (String) this.strCmb.getSelectionModel().getSelectedItem(); //get the selected store name from the comboBox

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
		CreateNewOrderController cnoc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		cnoc.setOrderDetails(newOrder);
//		nom.setConnectionData(this.listOfStoresEntities.get(selectedStoreName)); //send the connection and the StoreEntity selected by the user
		primaryStage.setTitle("New order from " + newOrder.getStore().getBranchName());

		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method handles the place order button
	 * @param event	pressed place order
	 * @throws IOException
	 * @throws ParseException
	 * @throws InterruptedException 
	 */
	public void pressedPlaceOrder(ActionEvent event) throws IOException, ParseException, InterruptedException
	{
		if(this.dlvrCkBx.isSelected() && (this.dlvrAddrs.getText().isEmpty() || this.dlvrPhoneNmbr.getText().isEmpty() || this.dlvrRcptNm.getText().isEmpty()))
		{			//if the delivery check box is selected and not all fields are filled 
			GeneralMessageController.showMessage("Please fill all delivery fields");
			return;
		}
		
		if(this.crdCrdCkBx.isSelected() && (this.crdCrdNmbrTxtFld.getText().isEmpty() || this.crdCrdCCVTxtFld.getText().isEmpty())) 
		{		//if the credit card check box is selected and not all fields are filled
			GeneralMessageController.showMessage("Please fill all credit card fields");
			return;
		}
		
		if(this.hrsTxtFld.getText().isEmpty() || Integer.parseInt(this.hrsTxtFld.getText())>23 || Integer.parseInt(this.hrsTxtFld.getText())<00 )
		{		//if invalid hour entered
			GeneralMessageController.showMessage("Invalid hours field");
			return;
		}
		
		if( this.mntsTxtFld.getText().isEmpty() || Integer.parseInt(this.mntsTxtFld.getText())>59 || Integer.parseInt(this.mntsTxtFld.getText())<00 )
		{		//if invalid minutes entered
			GeneralMessageController.showMessage("Invalid minutes field");
			return;
		}
		
		setTimeAndDateOfOrder();		//call method to set time and date		
		
		if(this.dlvrCkBx.isSelected())		// if selected delivery
		{
			//GeneralMessageController.showMessage(receivingDate.toString() + receivingTime.toString());
			this.newOrder.setOrderPickup(SelfOrDelivery.delivery);		//set to delivery
			this.newOrder.setTotalPrice(this.newOrder.getTotalPrice()+DeliveryEntity.getDeliveryPrice());			//add the delivery fee to the order's price
					//create a new delivery entity and set it's details
			this.newOrder.setDeliveryDetails(new DeliveryEntity( this.dlvrAddrs.getText(), this.dlvrRcptNm.getText(), this.dlvrPhoneNmbr.getText(), this.newOrder.getReceivingDate(), this.newOrder.getReceivingTime()));
		}
		
		else
		{
			this.newOrder.setOrderPickup(SelfOrDelivery.selfPickup); 		//set to self pickup
		}
		
		if(this.crdCrdCkBx.isSelected())		//if selected credit
		{
			this.newOrder.setPaymendMethod(CashOrCredit.credit);
			this.newOrder.setPaid(true);
		}
		
		else									//if selected cash
		{
			this.newOrder.setPaymendMethod(CashOrCredit.cash);
			this.newOrder.setPaid(false);
		}
		
		this.newOrder.setStatus(OrderStatus.active);	//set order status to active
		

		MessageToSend mts = new MessageToSend(this.newOrder, "createNewOrder");
		Client.getClientConnection().setDataFromUI(mts);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		
		ArrayList<String> dataFromServer = (ArrayList<String>)m.getMessage();
		GeneralMessageController.showMessage(dataFromServer.get(0));
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

		showCustomerMenu();
		
		
	}
	
	/**
	 * This method loads the customer's main menu after an order was placed
	 * @throws IOException
	 */
	public void showCustomerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	/**
	 * This method sets the time and date of the order for receiving the order
	 * @throws ParseException
	 */
	private void setTimeAndDateOfOrder() throws ParseException
	{
		LocalDate date =this.datePicker.getValue();		//get the localDate from the date picker
		Date receivingDate = Date.valueOf( date );		//convert the LocalDate type to a SQL Date type
		Time receivingTime;
		DateFormat formatter = new SimpleDateFormat("HH:mm");		//format the time
		receivingTime = new Time(formatter.parse(this.hrsTxtFld.getText()+":"+this.mntsTxtFld.getText()).getTime());		//set the time to a SQL time type
		
		this.newOrder.setReceivingDate(receivingDate);			//set the time and date to the new order 
		this.newOrder.setReceivingTime(receivingTime);
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
//		this.newOrder=newOrder;
		
		this.slfPckpCkBx.isSelected();
		this.dlvrAddrs.setVisible(false);
		this.dlvrPhoneNmbr.setVisible(false);
		this.dlvrRcptNm.setVisible(false);
		
		this.crdCrdNmbrTxtFld.setVisible(false);
		this.crdCrdCCVTxtFld.setVisible(false);
		
		this.slfPckpCkBx.setSelected(true);
		this.cshCkBx.setSelected(true);
		
	}


	public void setOrder(OrderEntity newOrder) {
		// TODO Auto-generated method stub
		this.newOrder=newOrder;
		this.totalPriceLabel.setText(this.newOrder.getTotalPrice().toString());
	}
	
}
