package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.FilesConverter;

public class CartController implements Initializable {
	private OrderEntity newOrder;
	private List<String> listOfProductsNames;
	ObservableList observableList = FXCollections.observableArrayList();

//	@FXML
//	private ListView<String> prdctDtlLst;
//	@FXML
//	private ListView<String> prdLst;
	@FXML
	private ImageView prdctImg;
	@FXML
	private TreeView<String> prdctTrVw;
	@FXML
	private Label crtEmptLbl;
	@FXML
	private Button crtBckBtn;
	@FXML
	private Button chkOutBtn;
	@FXML
	private Label totalPriceLable;

	public void showCart() throws IOException {
		String lbl = "";
		this.listOfProductsNames = new ArrayList<String>();
//		byte[] b = FilesConverter.convertFileToByteArray(new File("C:\\Users\\pic1.jpg"));
		TreeItem<String> root;

		root = new TreeItem<>(); //set the root for the prodcuts in cart tree
		root.setExpanded(true); //set it to expanded by default  

		if (this.newOrder.getProductsInOrder() != null)
		{

			lbl = "Your cart";
			for (ProductEntity product : this.newOrder.getProductsInOrder())
			{
				this.listOfProductsNames.add(product.getProductName());
				TreeItem<String> productName = new TreeItem<>(product.getProductName()); //set the branch as the product's name to be the parent of it's details
							/* Set all the product's details to be leaves on the branch */
				TreeItem<String> productID = new TreeItem<>(product.getProductID().toString()); 		//create a new leaf
				productName.getChildren().add(productID); 									//set as a child 
				TreeItem<String> productType = new TreeItem<>(product.getProductType());
				productName.getChildren().add(productType);
				TreeItem<String> productPrice = new TreeItem<>(product.getProductPrice().toString());
				productName.getChildren().add(productPrice);
				TreeItem<String> productDescription = new TreeItem<>(product.getProductDescription());
				productName.getChildren().add(productDescription);
				if (product.getProductDominantColor() != null)					//if domenant color exists
				{
					TreeItem<String> productDominantColor = new TreeItem<>(product.getProductDominantColor());
					productName.getChildren().add(productDominantColor);
				}
				root.getChildren().add(productName);
			}
//			observableList.setAll(stringSet);
//			this.prdLst.setItems(observableList);
			
		} else
			lbl = "Your Cart is empty";
		this.prdctTrVw.setRoot(root);
		this.prdctTrVw.setShowRoot(false); //make root expanded every time it starts
		this.crtEmptLbl.setText(lbl);
		
		calculatePrice(); 	//call method to calculate the order price
		this.totalPriceLable.setText(this.newOrder.getTotalPrice().toString());
						///This EventHanlder is an mouse event handler which listens to a product select in the products treeview
		EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
			showProductImage(event);
		};

		this.prdctTrVw.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 


	}

	/**
	 * This method handles the output of a product's image to the cart window
	 * @param event	the mouseEvent observed by the Event handler
	 */
	public void showProductImage(MouseEvent event) {
		for(ProductEntity product : this.newOrder.getProductsInOrder())
		{
		if(product.getProductName().equals(this.prdctTrVw.getSelectionModel().getSelectedItem().getValue()))
				this.prdctImg.setImage(FilesConverter.convertByteArrayToImage(product.getProductImage()));
		}
	} 
	
	/**
	 * This method calculates the order's price based on products prices
	 */
	private void calculatePrice()
	{
		this.newOrder.setTotalPrice(0.0);
		for(ProductEntity product: this.newOrder.getProductsInOrder())
		{
			
			this.newOrder.setTotalPrice(this.newOrder.getTotalPrice()+product.getProductPrice()); 		//sum all of the product prices
		}
	}
	
	/**
	 * This method loads the check out window
	 * @param event	clicked on go to checkout
	 * @throws IOException
	 */
	public void goToCheckOut(ActionEvent event) throws IOException
	{
		if(!this.newOrder.getProductsInOrder().isEmpty())		//if cart contains products
		{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/gui/CustomerCheckOutBoundary.fxml").openStream());
		
		CustomerCheckOutController ccoc = loader.getController();	
		ccoc.setOrder(this.newOrder);
//		ccoc.showCart();
		
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Your cart");
		primaryStage.setScene(scene);
		primaryStage.show();
		}
		
		else
		{
			GeneralMessageController.showMessage("Cart is empty.");
		}
	}

	/**
	 * This method sets the new orderEntitiy
	 * 
	 * @param order the new OrderEntity
	 */
	public void setOrder(OrderEntity order) {
		this.newOrder = order;
	}

	/**
	 * This method is used to return to the new order menu
	 * 
	 * @param event
	 *            button back
	 * @throws IOException 
	 */
	public void backToNewOrderMenu(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
		//	selectedStoreName = (String) this.strCmb.getSelectionModel().getSelectedItem(); //get the selected store name from the comboBox

			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
			CreateNewOrderController cnoc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window

			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			cnoc.setOrderDetails(newOrder);
//			nom.setConnectionData(this.listOfStoresEntities.get(selectedStoreName)); //send the connection and the StoreEntity selected by the user
			primaryStage.setTitle("New order from " + newOrder.getStore().getBranchName());
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	/**
	 * This method removed a product from the order
	 * @param event	click on remove from cart
	 * @throws IOException 
	 */
	public void removeFromCart(ActionEvent event) throws IOException
	{
		if(this.listOfProductsNames.contains(this.prdctTrVw.getSelectionModel().getSelectedItem().getValue()))		//if a product was selected
		{
			 										//remove the product from the treeView
	           this.newOrder.removeProductFromCart(this.prdctTrVw.getSelectionModel().getSelectedItem().getValue());		//remove the product from the Order's products in cart list
	           calculatePrice(); 	//recalculate the price
	           this.totalPriceLable.setText(this.newOrder.getTotalPrice().toString()); 			//update the price label 
	           this.listOfProductsNames.remove(this.prdctTrVw.getSelectionModel().getSelectedItem().getValue());
	           TreeItem c = (TreeItem)this.prdctTrVw.getSelectionModel().getSelectedItem();
	           c.getParent().getChildren().remove(c);	
		}
		
		else
		{
			GeneralMessageController.showMessage("No product selected to remove.");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//this.prdctTrVw = new TreeView<String>();
		//	showCart();

	}

}
