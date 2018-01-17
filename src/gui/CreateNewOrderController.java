package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.Client;
import entities.CardEntity;
import entities.OrderEntity;
import entities.ProductEntity;
import entities.StoreEntity;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.MessageToSend;
import sun.reflect.generics.tree.Tree;

public class CreateNewOrderController implements Initializable {

	private StoreEntity store;
	private OrderEntity newOrder;
	@FXML
	private TreeView<String> PrdctsTrVw;
	@FXML 
	private Label crtEmptLbl;
	@FXML
	private Button crtBckBtn;

	@FXML
	private Button bckTorderMn;

	@FXML
	private ListView<?> dtlsLstVw;
	
	@FXML
	private Button itmFrmCtlgBtn;

	@FXML
    private Button AddSlfDfIPrdctBtn;

	@FXML
	private Button VwCrtBtn;

	@FXML
	private Button PrcdToChkOutBtn;

	@FXML
	private Button addCrdBtn;
	@FXML
	private ComboBox chsStrCmb;
	
	/**
	 * This method saves the client connection to the controller
	 * And saves the selected store to make the order from
	 * @param store	the store
	 */
	public void setConnectionData(StoreEntity store)
	{
		this.store=store;
		this.newOrder.setStore(store);

	}
	
	/**
	 * This methods sets the selected store to the order
	 * @param store
	 */
	public void setStore(StoreEntity store)
	{
		this.store=store;
	}
	
	/**
	 * This method sets the order
	 * @param order
	 */
	public void setOrderDetails(OrderEntity order) {
		this.newOrder=order;
	}
	
	/**
	 * This method handles the self defined product option
	 * @param event	pressed self defined product button
	 * @throws IOException
	 */
	public void addSelfDefinedProduct(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerSelfDefinedProductBoundary.fxml").openStream());
		CustomerSelfDefinedProducyController csdpc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		csdpc.setOrder(newOrder);
		primaryStage.setTitle("Search for self defined product");

		primaryStage.setScene(scene);
		primaryStage.show();
    }

	
	/**
	 * This method shows the add card window when pressed "add new card" button
	 * @param event add a card pressed
	 * @throws IOException
	 */
	public void addCard(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CardBoundary.fxml").openStream());
		AddCardController acc = loader.getController();
		acc.setOrder(this.newOrder);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Add a card");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method opens the cart 
	 * @param event	pressed view cart
	 * @throws IOException
	 */
	public void viewCart(ActionEvent event) throws IOException {
<<<<<<< HEAD
	
		
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

=======
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
>>>>>>> branch 'master' of https://github.com/1elirantoledano/ProjectX.git
		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/CartBoundary.fxml").openStream());
		CartController cc = loader.getController();
		cc.setOrder(this.newOrder);
		cc.showCart();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(pRoot);
		primaryStage.setTitle("Your cart");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method show's the catalog in order to choose item 
	 * @param event
	 * @throws IOException
	 * @throws InterruptedException
	 */
public void addItemFromCatalog(ActionEvent event) throws IOException, InterruptedException {
		((Node) event.getSource()).getScene().getWindow().hide(); //Hide last window
		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/OrderFromCatalogBoundary.fxml").openStream());
		OrderFromCatalogController ord=loader.getController();
		ord.showCatalog(this.newOrder);                                              //Call the method show catalog
		Stage primaryStage=new Stage();
		Scene scene=new Scene(pRoot);
		primaryStage.setTitle("Zer-Li Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * This method is used to return to the  order menu
	 * @param event	button back 
	 * @throws IOException 
	 */
	public void backToOrderMenu(ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/CustomerOrderMenuBoundary.fxml").openStream());
		 CustomerOrderController ord = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.newOrder = new OrderEntity();
		this.newOrder.setUserName(Client.getClientConnection().getUsername()); 	//set the customer user name to the order
		//this.newOrder.setStore(store);
		this.PrdctsTrVw = new TreeView<>();
	}
}