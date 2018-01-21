package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.ComplaintEntity;
import entities.OrderEntity;
import entities.StoreEntity;
import entities.SurveyEntity;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import logic.MessageToSend;

public class StoreManagerViewReportsConroller implements Initializable {

	private StoreEntity store;
	public ObservableList<String> list;
	private ArrayList<String> quarters;

	@FXML
	private ComboBox<String> rvnQrtrCmb;
	
	@FXML
	private ComboBox<String> cmplntQrtrCmb;
	@FXML
	private CheckBox qtrRvnChkBx;

	@FXML
	private CheckBox qtrlyCmplntChckBx;

//	@FXML
//	private CheckBox cstStsfctnChckBx;

	@FXML
	private CheckBox OrdrRprtChckBx;

	@FXML
	private Button gnrtRprtBtn;

	@FXML
	private Button bckBtn;

	@FXML
	private TreeView<String> storeDtlsTrVw;
	

	ArrayList<StoreEntity> listOfStoresFromDB;
	ArrayList<OrderEntity> listOfOrdersFromDB;
	ArrayList<ComplaintEntity> listOfComplaintsFromDB;
//	SurveyEntity survey;
	
	/**
	 * This method loads the store's details
	 * 
	 * @param event
	 *            store selected from the list
	 */
	public void showStoreDetails() {
	
		TreeItem<String> root;

		root = new TreeItem<>(); //set the root for the tree
		root.setExpanded(true); //set it to expanded by default  

		//**------Build a treeView that contains all the order's details-------**//
		
				TreeItem<String> BranchName = new TreeItem<>("Branch name : " + store.getBranchName()); //set the branch as the product's name to be the parent of it's details
				/* Set all the order's details to be leaves on the branch */
				TreeItem<String> BranchID = new TreeItem<>("Branch ID : " + store.getBranchID()); //create a new leaf
				BranchName.getChildren().add(BranchID); //set as a child 

				TreeItem<String> ManagerID = new TreeItem<>("Store manager ID : " + store.getStoreManagerWorkerID()); //create a new leaf
				BranchName.getChildren().add(ManagerID); //set as a child 

				TreeItem<String> workers = new TreeItem<>("Store workers ID");
				BranchName.getChildren().add(workers);

				if (store.getStoreWorkers() != null)
				{
					for (Integer worker : store.getStoreWorkers())
					{
						TreeItem<String> workerID = new TreeItem<>("Worker ID : " + worker);
						workers.getChildren().add(workerID);
					}
				}
				if (store.getStoreDiscoutsSales() != null)
				{

					TreeItem<String> discounts = new TreeItem<>("Store discounts");
					BranchName.getChildren().add(discounts);

					for (Integer productID : store.getStoreDiscoutsSales().keySet())
					{
						TreeItem<String> product = new TreeItem<>("Product ID : " + productID + "\nDiscount price : " + store.getStoreDiscoutsSales().get(productID));
						discounts.getChildren().add(product);
					}
				}

				//	showComplaintDetails(order.getOrderID());

				root.getChildren().add(BranchName);
				BranchName.setExpanded(true); //set the tree expanded by default
				Visible(true);
				
		this.storeDtlsTrVw.setRoot(root);
		this.storeDtlsTrVw.setShowRoot(false); //make root expanded every time it starts
	}
	
	/**
	 * This method sets visible true or false
	 * @param val
	 */
	private void Visible(boolean val)
	{
		this.qtrRvnChkBx.setVisible(val);
//		this.cstStsfctnChckBx.setVisible(val);
		this.OrdrRprtChckBx.setVisible(val);
		this.qtrlyCmplntChckBx.setVisible(val);
		this.cmplntQrtrCmb.setVisible(val);
		this.rvnQrtrCmb.setVisible(val);
		this.gnrtRprtBtn.setVisible(val);
	}

	/**
	 * This method generates the asked reports
	 * @param event	pressed generate reports
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void generateReports(ActionEvent event) throws IOException, InterruptedException {
		if(!this.qtrRvnChkBx.isSelected() && !this.OrdrRprtChckBx.isSelected() && !this.qtrlyCmplntChckBx.isSelected())		//check if no report was chosen
		{
			GeneralMessageController.showMessage("No report was chosen");
			return;
		}
		
		if (this.qtrRvnChkBx.isSelected())			//if selected a quarterly revenue report
		{
			if (this.rvnQrtrCmb.getSelectionModel().isEmpty())		//if didn't select a quarter
			{
				GeneralMessageController.showMessage("For a quarterly revenue report, must choose a quarter");
				return;
			}
			else
			{
				quarterlyRevenueReport();		//call method for revenue report
			}
		}
		
		if(this.qtrlyCmplntChckBx.isSelected())						//if selected a quarterly complaint report
		{
			if (this.cmplntQrtrCmb.getSelectionModel().isEmpty())		//if didn't select a quarter
			{
				GeneralMessageController.showMessage("For a quarterly Complaint report, must choose a quarter");
				return;
			}
			else
			{
			quarterlyComplaintsReport();			//call method for complaint report
			}
		}
		
									//the Orders report//
		if (this.OrdrRprtChckBx.isSelected())
		{
			OrderReport();							//call merthod for order report
		}
		this.qtrRvnChkBx.setSelected(false);
		this.OrdrRprtChckBx.setSelected(false);
		this.qtrlyCmplntChckBx.setSelected(false);
											//The survey report//
//		if (this.cstStsfctnChckBx.isSelected())
//		{
//			satisfactionReport(); 						//method for satisfaction report
//		}
	}
	
	/**
	 * This method loads the revenue report
	 */
	private void quarterlyRevenueReport() throws InterruptedException, IOException
	{
		// **** get all order from DB for the selected quarter ***//
		ArrayList<String> message = new ArrayList<String>();
		message.add(this.store.getBranchName());
		message.add(this.rvnQrtrCmb.getSelectionModel().getSelectedItem());
		MessageToSend messageToSend = new MessageToSend(message, "getAllOrders");
		Client.getClientConnection().setDataFromUI(messageToSend); //set operation to get all stores from DB
		Client.getClientConnection().accept();
		while (!(Client.getClientConnection().getConfirmationFromServer())) //wait for server response
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer(); //reset to false
		messageToSend = Client.getClientConnection().getMessageFromServer();
		listOfOrdersFromDB = (ArrayList<OrderEntity>) messageToSend.getMessage(); //get the list of stores from the message class

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainStoreManagerQuarterlyRevenueReportBoundary.fxml").openStream());
		ChainStoreManagerQuarterlyRevenueReportController c = new ChainStoreManagerQuarterlyRevenueReportController();
		c = loader.getController();
		c.showOrders(listOfOrdersFromDB);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Quarterly revenue report for "+this.store.getBranchName()+", quarter No."+this.rvnQrtrCmb.getSelectionModel().getSelectedItem());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method loads the complaint report
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private void quarterlyComplaintsReport() throws InterruptedException, IOException {
		// **** get all Complaints from DB for the selected quarter ***//
		ArrayList<String> message = new ArrayList<String>();
		message.add(this.store.getBranchName());
		message.add(this.cmplntQrtrCmb.getSelectionModel().getSelectedItem());

		MessageToSend messageToSend = new MessageToSend(message, "getComplaints");
		Client.getClientConnection().setDataFromUI(messageToSend); //set operation to get all stores from DB
		Client.getClientConnection().accept();
		while (!(Client.getClientConnection().getConfirmationFromServer())) //wait for server response
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer(); //reset to false
		messageToSend = Client.getClientConnection().getMessageFromServer();
		this.listOfComplaintsFromDB = (ArrayList<ComplaintEntity>) messageToSend.getMessage(); //get the list of stores from the message class


		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainStoreManagerComplaintReportBoundary.fxml").openStream());
		ChainStoreManagerComplaintReportController c = new ChainStoreManagerComplaintReportController();
		c = loader.getController();
		c.showComplaints(this.cmplntQrtrCmb.getSelectionModel().getSelectedItem(),listOfComplaintsFromDB);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Quarterly Complaint report for " + this.store.getBranchName() + ", quarter No." + this.cmplntQrtrCmb.getSelectionModel().getSelectedItem());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method loads the order report
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private void OrderReport() throws InterruptedException, IOException
	{
	
		ArrayList<String> message = new ArrayList<String>();
		message.add("all");
		message.add("all");
		MessageToSend messageToSend = new MessageToSend(message, "getAllOrders");
		Client.getClientConnection().setDataFromUI(messageToSend); //set operation to get all stores from DB
		Client.getClientConnection().accept();
		while (!(Client.getClientConnection().getConfirmationFromServer())) //wait for server response
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer(); //reset to false
		messageToSend = Client.getClientConnection().getMessageFromServer();
		this.listOfOrdersFromDB = (ArrayList<OrderEntity>) messageToSend.getMessage(); //get the list of stores from the message class

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainStoreManagerOrderReportBoundary.fxml").openStream());
		ChainStoreManagerOrderReportContorller c = new ChainStoreManagerOrderReportContorller();
		c = loader.getController();
		c.showOrders(listOfOrdersFromDB);
		
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Orders report for " + this.store.getBranchName());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
//	/**
//	 * This method loads the satisfaction report;
//	 * @throws InterruptedException 
//	 * @throws IOException 
//	 */
//	private void satisfactionReport() throws InterruptedException, IOException
//	{
//		// **** get  survey from DB for the selected quarter ***//
////		MessageToSend messageToSend = new MessageToSend("", "getSurvey");
////		Client.getClientConnection().setDataFromUI(messageToSend); //set operation to get all stores from DB
////		Client.getClientConnection().accept();
////		while (!(Client.getClientConnection().getConfirmationFromServer())) //wait for server response
////			Thread.sleep(100);
////		Client.getClientConnection().setConfirmationFromServer(); //reset to false
////		messageToSend = Client.getClientConnection().getMessageFromServer();
////		this.survey = (SurveyEntity) messageToSend.getMessage(); //get the list of stores from the message class
////
//		FXMLLoader loader = new FXMLLoader();
//		Parent root = loader.load(getClass().getResource("/gui/ChainStoreManagerSatisfactionReportBoundary.fxml").openStream());
//		//CustomerServiceWorkerHandleComplaintController cswscic = new CustomerServiceWorkerHandleComplaintController();
//		//cswscic = loader.getController();
//		//cswscic.setComplaint(this.complaint);
//		Stage primaryStage = new Stage();
//		Scene scene = new Scene(root);
//		primaryStage.setTitle("Satisfactory report for " + this.store.getBranchName());
//		primaryStage.setScene(scene);
//		primaryStage.show();
//
//	}
	
	/**
	 * This method loads the comboBox
	 */
	private void loadComboBox()
	{
		quarters = new ArrayList<String>();
		quarters.add("1");
		quarters.add("2");
		quarters.add("3");
		quarters.add("4");
		this.list = FXCollections.observableArrayList(quarters);	//set to an observableArrayList
		this.cmplntQrtrCmb.setItems(this.list);							//set the list to the comboBox
		this.rvnQrtrCmb.setItems(this.list);
	}

	/**
	 * This method is used to return to the order menu
	 * 
	 * @param event
	 *            button back
	 * @throws IOException
	 */
	public void backToMenu(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreManagerMenuBoundary.fxml").openStream());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Store manager main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * This method sets the store
	 * @param store	the store
	 */
	public void setStore(StoreEntity store)
	{
		this.store=store;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadComboBox();
	}

}
