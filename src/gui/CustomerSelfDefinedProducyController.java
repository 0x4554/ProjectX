package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.OrderEntity;
import entities.ProductEntity;
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
import javafx.stage.Stage;
import logic.MessageToSend;

public class CustomerSelfDefinedProducyController implements Initializable {

	@FXML
	private ComboBox<String> dmnntClrCmb;

	@FXML
	private ComboBox<String> prdctTypeCmb;

	@FXML
	private Button fndPrdctBtn;

	@FXML
	private ComboBox<String> MinPrcCmb;

	@FXML
	private ComboBox<String> maxPrcCmb;

	@FXML
	private Button bckBtn;

	private OrderEntity newOrder;
	
	public ObservableList<String> listMinPrices;
	public ObservableList<String> listMaxPrices;
	public ObservableList<String> listDominantColors;
	public ObservableList<String> listProductTypes;

	private ArrayList<String> productTypes;
	private ArrayList<String> dominantColors;
	private ArrayList<String> minPrices;
	private ArrayList<String> maxPrices;

	/**
	 * This method collects the data from the fields and send it to the server
	 * 
	 * @param event	pressed find products
	 * @throws IOException 	for the loader
	 * @throws InterruptedException for the sleep
	 */
	public void FindMatchingProducts(ActionEvent event) throws IOException, InterruptedException {
		if(this.MinPrcCmb.getSelectionModel().isEmpty() || this.maxPrcCmb.getSelectionModel().isEmpty())	//if price wasn't chosen
		{
			GeneralMessageController.showMessage("Please enter price range");
			return;
		}
		
		if(this.prdctTypeCmb.getSelectionModel().isEmpty())		//if type was not chosen
		{
			GeneralMessageController.showMessage("Please choose product type");
			return;
		}
		
		ArrayList<String> data = new ArrayList<String>();
		
		data.add(this.MinPrcCmb.getSelectionModel().getSelectedItem());		//add the minimum price to the list
		data.add(this.maxPrcCmb.getSelectionModel().getSelectedItem());		//add the maximum price to the list
		data.add(this.prdctTypeCmb.getSelectionModel().getSelectedItem());	//add type to list
		if(!this.dmnntClrCmb.getSelectionModel().isEmpty())
			data.add(this.dmnntClrCmb.getSelectionModel().getSelectedItem());		//if exists add dominant color
		
		MessageToSend messageToSend = new MessageToSend(data, "getSelfDefinedProduct");
		Client.getClientConnection().setDataFromUI(messageToSend);							//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend message = Client.getClientConnection().getMessageFromServer();
		
		ArrayList<ProductEntity> matchingProducts = (ArrayList<ProductEntity>) message.getMessage();
		
		if(matchingProducts.isEmpty())											//if no products match were found
		{
			GeneralMessageController.showMessage("No products matching your requirments found.");
			return;
		}
		
		///////////////////////show as catalog////////////////////////

	}

	/**
	 * This emthod hanldes the back to order menu
	 * 
	 * @param event
	 *            pressed back
	 * @throws IOException
	 */
	public void backToNewOrderMenu(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
		CreateNewOrderController cnoc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		cnoc.setOrderDetails(newOrder);
		primaryStage.setTitle("New order from " + newOrder.getStore().getBranchName());

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method sets the new orderEntitiy
	 * 
	 * @param order
	 *            the new OrderEntity
	 */
	public void setOrder(OrderEntity order) {
		this.newOrder = order;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//this.newOrder=newOrder;
		setComboxes();
	}

	/**
	 * This method sets the content of the combo boxes 
	 */
	private void setComboxes() {
		this.minPrices = new ArrayList<String>();
		this.maxPrices = new ArrayList<String>();
		for(Integer i=0;i<500;i+=10)				//set the prices range
			this.minPrices.add(i.toString());
		for(Integer i=0;i<500;i+=10)
			this.maxPrices.add(i.toString());
		this.listMinPrices = FXCollections.observableArrayList(this.minPrices);
		this.listMaxPrices = FXCollections.observableArrayList(this.maxPrices);
		this.MinPrcCmb.setItems(this.listMinPrices);
		this.maxPrcCmb.setItems(this.listMaxPrices);
		
		this.productTypes = new ArrayList<String>();		//set the products types
		this.productTypes.add("bridal");
		this.productTypes.add("flower");
		this.productTypes.add("birthday");
		
		this.listProductTypes = FXCollections.observableArrayList(this.productTypes);
		this.prdctTypeCmb.setItems(this.listProductTypes);
		
		this.dominantColors = new ArrayList<String>();		//set  the dominant colors
		this.dominantColors.add("blue");
		this.dominantColors.add("red");
		this.dominantColors.add("green");
		this.dominantColors.add("yellow");
		
		this.listDominantColors = FXCollections.observableArrayList(this.dominantColors);
		this.dmnntClrCmb.setItems(this.listDominantColors);
		
	}

}
