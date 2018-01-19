package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.ComplaintEntity;
import entities.OrderEntity;
import entities.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.FilesConverter;
import logic.MessageToSend;

public class CustomerServiceWorkerComplaintController implements Initializable {

	@FXML
	private ListView<String> ActvCmplntLstVw;
	
	@FXML
	private ListView<String> InActvCmplntLstVw;
	
	private ListView<String> listToDisplayDetails;  	//to use each list apart

	@FXML
	private Button bckBtn;

	@FXML
	private Button shwImgBtn;

	@FXML
	private Button hndlcmplntBtn;

	@FXML
	private TreeView<String> dtlsTrVw;
	
	@FXML
    private TextArea cmplntDtlsTxtArea;
	
	private ArrayList<ComplaintEntity> listOfComplaints;
	private ArrayList<OrderEntity> listOfOrders;
	private Image complaintImage;
	private ComplaintEntity complaint;

	private ObservableList<String> listOfActiveComplaintString;
	private ObservableList<String> listOfInActiveComplaintSting;

	/**
	 * This method shows the list of complaints
	 * @throws InterruptedException 
	 */
	public void showComplaints() throws InterruptedException {
		
			//****Get all the complaints from the server****////
		MessageToSend message = new MessageToSend("", "getComplaints");
		Client.getClientConnection().setDataFromUI(message);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		listOfComplaints = (ArrayList<ComplaintEntity>)m.getMessage();
		
		 	//****Get all the complaint's orders***////
		message = new MessageToSend(listOfComplaints, "getComplaintOrders");
		Client.getClientConnection().setDataFromUI(message);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		m = Client.getClientConnection().getMessageFromServer();
		this.listOfOrders = (ArrayList<OrderEntity>)m.getMessage();
		
		this.ActvCmplntLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		//unable multiple selection
		
		this.listOfActiveComplaintString = FXCollections.observableArrayList();		//the observable list to enter to the list  view
		this.listOfInActiveComplaintSting = FXCollections.observableArrayList();
		
		for(ComplaintEntity complaint : this.listOfComplaints)
		{
			if(complaint.getStatus().toString().equals("processing"))
				this.listOfActiveComplaintString.add("Complaint for order number "+complaint.getOrderID());
			else
				this.listOfInActiveComplaintSting.add("Complaint for order number "+complaint.getOrderID());
		}
//		for(OrderEntity order : listOfOrders)		//build list view to contain all orders
//		{
//			if()
//			this.listOfActiveComplaintString.add("Complaint for order number "+order.getOrderID());
//		}
		
//		this listOfActiveComplaintString.add("I)
		this.ActvCmplntLstVw.setItems(this.listOfActiveComplaintString);		//set items to the list of active complaints

		this.InActvCmplntLstVw.setItems(this.listOfInActiveComplaintSting); 	//set items to the list of Closed complaints
		

		EventHandler<MouseEvent> ActiveComplaintMouseEventHandle = (MouseEvent event) -> {
			selectedActiveComplaint(event);
		};

		this.ActvCmplntLstVw.addEventHandler(MouseEvent.MOUSE_CLICKED, ActiveComplaintMouseEventHandle); 
		
		EventHandler<MouseEvent> InActiveComplaintMouseEventHandle = (MouseEvent event) -> {
			selectedInActiveComplaint(event);
		};

		this.InActvCmplntLstVw.addEventHandler(MouseEvent.MOUSE_CLICKED, InActiveComplaintMouseEventHandle);
		this.shwImgBtn.setVisible(false);
	}
	
	/**
	 * This method handles if pressed on an active complaint
	 * @param event	selected active complaint
	 */
	public void selectedActiveComplaint(MouseEvent event)
	{
		this.listToDisplayDetails = this.ActvCmplntLstVw;		//set the selected listView for showing details
		showOrderDetails(event);
	}
	
	/**
	 * This method handles if pressed on an active complaint
	 * @param event	selected active complaint
	 */
	public void selectedInActiveComplaint(MouseEvent event)
	{
		this.listToDisplayDetails = this.InActvCmplntLstVw;				//set the selected listView for showing details
		showOrderDetails(event);
	}
	
	/**
	 * This method builds the treeView of a specific order chosen
	 * @param event order chosen from the list
	 */
	public void showOrderDetails(MouseEvent event)
	{
		if(this.listToDisplayDetails.getSelectionModel().isEmpty())	//if nothing was selected
			return;
		TreeItem<String> root;
		
		root = new TreeItem<>(); //set the root for the tree
		root.setExpanded(true); //set it to expanded by default  

	
					//**------Build a treeView that contains all the order's details-------**//
			for (OrderEntity order : listOfOrders)
			{
				if(this.listToDisplayDetails.getSelectionModel().getSelectedItem().substring(27).equals(order.getOrderID().toString())) 		//check which order was selected
				{
				TreeItem<String> OrderID = new TreeItem<>("Order number : "+order.getOrderID().toString()); //set the branch as the product's name to be the parent of it's details
							/* Set all the order's details to be leaves on the branch */
				TreeItem<String> totalprice = new TreeItem<>("Total price : "+order.getTotalPrice().toString()); 		//create a new leaf
				OrderID.getChildren().add(totalprice); 									//set as a child 
				
				TreeItem<String> orderTime = new TreeItem<>("Order Time : "+order.getOrderTime().toString()); 		//create a new leaf
				OrderID.getChildren().add(orderTime); 									//set as a child 
			TreeItem<String> store = new TreeItem<>("From Store named : "+order.getStore().getBranchName());
				OrderID.getChildren().add(store);
				
				if(order.getCard() != null)		//if there is a card
				{
					TreeItem<String> card = new TreeItem<>("Added card : "+order.getCard().getText());
					OrderID.getChildren().add(card);
				}
				
				TreeItem<String> orderPickup = new TreeItem<>(order.getOrderPickup().toString());
				OrderID.getChildren().add(orderPickup);
				TreeItem<String> receivingTime = new TreeItem<>("Receiving time : "+order.getReceivingTimestamp().toString());
				OrderID.getChildren().add(receivingTime);
				TreeItem<String> status = new TreeItem<>("Order Status : "+order.getStatus().toString());
				OrderID.getChildren().add(status);
				TreeItem<String> paid = new TreeItem<>("Order paid ? : "+((order.getPaid())? "Yes":"No"));
				OrderID.getChildren().add(paid);
				TreeItem<String> paymendMethod = new TreeItem<>("Payment method : "+order.getPaymendMethod().toString());
				OrderID.getChildren().add(paymendMethod);
				
				if(order.getDeliveryDetails() != null)		//if there is a delivery
				{
				TreeItem<String> deliveryDetails = new TreeItem<>("Delivery Details");
				OrderID.getChildren().add(deliveryDetails);
				TreeItem<String> recipientName = new TreeItem<>("Recipient name : "+order.getDeliveryDetails().getRecipientName());
				deliveryDetails.getChildren().add(recipientName);
				TreeItem<String> address = new TreeItem<>("Recipient address : "+order.getDeliveryDetails().getDeliveryAddress());
				deliveryDetails.getChildren().add(address);
				TreeItem<String> phoneNumber = new TreeItem<>("Recipient phone number : "+order.getDeliveryDetails().getPhoneNumber());
				deliveryDetails.getChildren().add(phoneNumber);
				TreeItem<String> deliveryprice = new TreeItem<>("Delivery fee : "+order.getDeliveryDetails().getDeliveryPrice().toString());
				deliveryDetails.getChildren().add(deliveryprice);
				}
							//** the lists of products in the order **//
				TreeItem<String> products = new TreeItem<>("Products in the order");
				OrderID.getChildren().add(products);
				
				for(ProductEntity product : order.getProductsInOrder())
				{
					TreeItem<String> productName = new TreeItem<>("Product name : "+product.getProductName());
					products.getChildren().add(productName);
					TreeItem<String> productID = new TreeItem<>("Product ID : "+product.getProductID().toString());
					productName.getChildren().add(productID);
					TreeItem<String> ProductType = new TreeItem<>("Product Type : "+product.getProductType());
					productName.getChildren().add(ProductType);
					TreeItem<String> productDescription = new TreeItem<>("Product description : "+product.getProductDescription());
					productName.getChildren().add(productDescription);
//					if(product.getProductDominantColor() != null)
//					{
//						TreeItem<String> productDominantColor = new TreeItem<>("Dominent color : "+product.getProductDominantColor());
//						productName.getChildren().add(productDominantColor);
//			
//					}
					Double price;
					TreeItem<String> productPrice;
					if((price = order.getStore().getStoreDiscoutsSales().get(product.getProductID())) != null)	//check for disocunt
						productPrice = new TreeItem<>("Product price : "+price.toString());
					else
						productPrice = new TreeItem<>("Product price : "+product.getProductPrice().toString());
					productName.getChildren().add(productPrice);
					
				}
				
				showComplaintDetails(order.getOrderID());
				
				root.getChildren().add(OrderID);
				OrderID.setExpanded(true); 				//set the tree expanded by default
				}
			}			
		this.dtlsTrVw.setRoot(root);
		this.dtlsTrVw.setShowRoot(false); //make root expanded every time it starts
	}
	
	/**
	 * This method shows the complaint details
	 * @param oderID	the order id
	 */
	private void showComplaintDetails(Integer orderID)
	{
		for(ComplaintEntity complaint : this.listOfComplaints)
		{
			if(complaint.getOrderID() == orderID)
			{
				this.cmplntDtlsTxtArea.setText(complaint.getDescription());		//set the complaint details to the text area
				if(complaint.getFile() != null)									//if there is an image 
				{
					this.shwImgBtn.setVisible(true);
					this.complaintImage = FilesConverter.convertByteArrayToImage(complaint.getFile());		//convert and save the image
				}
				else
					this.shwImgBtn.setVisible(false);
				this.complaint = complaint;
			}
		}
	}

	
	/**
	 * This method loads the handle complaint window
	 * @param event	pressed handle complaint
	 * @throws IOException 
	 */
	public void handlecomplaint(ActionEvent event) throws IOException {

		
		if(!this.ActvCmplntLstVw.getSelectionModel().isEmpty())
		{
			if(this.complaint.getStatus().toString().equals("handled"))
			{
				GeneralMessageController.showMessage("This complaint has already been handled");
				return;
			}
			else
			{
				((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerHandleComplaintBoundary.fxml").openStream());
				CustomerServiceWorkerHandleComplaintController cswscic = new CustomerServiceWorkerHandleComplaintController();
				cswscic = loader.getController();
				cswscic.setComplaint(this.complaint);
				Stage primaryStage = new Stage();
				Scene scene = new Scene(root);
				primaryStage.setTitle("Customer Service worker main menu");
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		}
		else
		{
			GeneralMessageController.showMessage("No complaint was chosen");
			return;
		}
	}

	/**
	 * This method loads the complaint image
	 * @param event	pressed show complaint image
	 * @throws IOException 
	 */
	public void showImage(ActionEvent event) throws IOException {
		
//		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerShowComplaintImageBoundary.fxml").openStream());
		CustomerServiceWorkerShowComplaintImageController cswscic = new CustomerServiceWorkerShowComplaintImageController();
		cswscic.showImage(this.complaintImage);
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Complaint image");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method loads the main menu
	 */
	public void backToMainMenu(ActionEvent event) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerServiceWorkerMenuBoundary.fxml").openStream());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Customer Service worker main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
