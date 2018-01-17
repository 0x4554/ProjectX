package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logic.MessageToSend;
import logic.TimeCalculation;

public class StoreManagerCancellationRequestsController implements Initializable {

	@FXML
    private Label rfndLbl;
	@FXML
    private ListView<String> ordrLstVw;

    @FXML
    private Button bckBtn;

    @FXML
    private Button dclnBtn;

    @FXML
    private Button apprvBtn;

    @FXML
    private TreeView<String> dtlsTrVw;

	ObservableList<String> listOfOrders;
	ArrayList<OrderEntity> arraylistOfOrders;

    
	/**
	 * This method builds the ListView that contains the customer's orders
	 * @throws InterruptedException
	 */
	public void showOrders() throws InterruptedException
	{
		MessageToSend message = new MessageToSend(null, "getCancelRequests");
		Client.getClientConnection().setDataFromUI(message);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		
		arraylistOfOrders = (ArrayList<OrderEntity>)m.getMessage();
		
		this.ordrLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);		//unable multiple selection
		
		this.listOfOrders = FXCollections.observableArrayList();		//the observable list to enter to the list  view
		
		for(OrderEntity order : arraylistOfOrders)		//build list view to contain all orders
		{
			this.listOfOrders.add("Order number "+order.getOrderID());
		}
		
		this.ordrLstVw.setItems(this.listOfOrders);		//set items to the list

		EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
			showOrderDetails(event);
		};

		this.ordrLstVw.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 

	}
	
	/**
	 * This method builds the treeView of a specific order chosen
	 * @param event order chosen from the list
	 */
	public void showOrderDetails(MouseEvent event)
	{
		if(this.ordrLstVw.getSelectionModel().isEmpty())	//if nothing was selected
			return;
		TreeItem<String> root;

		root = new TreeItem<>(); //set the root for the tree
		root.setExpanded(true); //set it to expanded by default  

	
					//**------Build a treeView that contains all the order's details-------**//
			for (OrderEntity order : arraylistOfOrders)
			{
				if(this.ordrLstVw.getSelectionModel().getSelectedItem().substring(13).equals(order.getOrderID().toString())) 		//check which order was selected
				{
				TreeItem<String> OrderID = new TreeItem<>("Order number : "+order.getOrderID().toString()); //set the branch as the product's name to be the parent of it's details
							/* Set all the order's details to be leaves on the branch */
				TreeItem<String> totalprice = new TreeItem<>("Total price : "+order.getTotalPrice().toString()); 		//create a new leaf
				OrderID.getChildren().add(totalprice); 									//set as a child 
				
//**NEED TO FIX 				TreeItem<String> orderTime = new TreeItem<>("Order Time : "+order.getOrderTime().toString()); 		//create a new leaf
//		IMAGE STUFF		OrderID.getChildren().add(orderTime); 									//set as a child 
//				TreeItem<String> orderDate = new TreeItem<>("Order Date : "+order.getOrderDate().toString());
//				OrderID.getChildren().add(orderDate);
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
					TreeItem<String> productPrice = new TreeItem<>("Product price : "+product.getProductPrice().toString());
					productName.getChildren().add(productPrice);
					
				}
				
				root.getChildren().add(OrderID);
				OrderID.setExpanded(true); 				//set the tree expanded by default
				}
			}
			
		this.dtlsTrVw.setRoot(root);
		this.dtlsTrVw.setShowRoot(false); //make root expanded every time it starts
		
	////////////////////////////////////	GeneralMessageController.showMessage(TimeCalculation.);
	}
	
   public void backToMainMenu(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreManagerMenuBoundary.fxml").openStream());
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Store manager's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
    }

    /**
     * This method handles the approved cancellation
     * @param event	pressed approve
     * @throws IOException for loader
     * @throws InterruptedException for sleep
     */
   public void cancelApproved(ActionEvent event) throws IOException, InterruptedException {
	   
	   OrderEntity orderToCancel = null;
		
		if(this.ordrLstVw.getSelectionModel().isEmpty())	//if nothing was selected
			GeneralMessageController.showMessage("No order was selected.");
		else
		{
			for(OrderEntity order : this.arraylistOfOrders)
			{
				if(order.getOrderID() == Integer.parseInt(this.ordrLstVw.getSelectionModel().getSelectedItem().substring(13)))	//get the selected order
					orderToCancel=order;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);		//set new alert for confirmation
			alert.setTitle("Confirmation");
			alert.setHeaderText("Cancel order");
			alert.setContentText("Are you sure you want to approve this order cancellation?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){						//if pressed OK
				MessageToSend message = new MessageToSend(Integer.parseInt(this.ordrLstVw.getSelectionModel().getSelectedItem().substring(13)), "cancelRequest");	//get the order ID

				Client.getClientConnection().setDataFromUI(message);							//set the data and the operation to send from the client to the server
				Client.getClientConnection().accept();										//sends to server
				while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
					Thread.sleep(100);
				Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
				MessageToSend m = Client.getClientConnection().getMessageFromServer();
				GeneralMessageController.showMessage((String)m.getMessage());
				showOrders();
			
			}
			else
				GeneralMessageController.showMessage("Cancellation was already requested for the order");
		}
			
    }

    
	/**
	 * This method handles the decline cancellation
	 * @param event	pressed decline
	 */
   public void cancelDeclined(ActionEvent event) {

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try
		{
			showOrders();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}