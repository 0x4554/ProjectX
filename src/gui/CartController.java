package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import entities.OrderEntity;
import entities.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class CartController implements Initializable {
	private OrderEntity newOrder;
	private List<String> stringSet;
	ObservableList observableList = FXCollections.observableArrayList();

//	@FXML
//	private ListView<String> prdctDtlLst;
//	@FXML
//	private ListView<String> prdLst;
	@FXML
	private TreeView<String> prdctTrVw;
	@FXML
	private Label crtEmptLbl;
	@FXML
	private Button crtBckBtn;
	@FXML
	private Button chkOutBtn;

	public void showCart() {
		String lbl = "";
		this.stringSet = new ArrayList<String>();
		this.newOrder = new OrderEntity();

		//////////////////a made up list of products for testing ///////////////////
		for (int i = 0; i < 5; i++)
		{
			this.newOrder.addProductToCart(new ProductEntity("a" + i, "bbbbbbbb" + i, "c" + i, 1.1 + i, "e" + i, "f" + i));
		}
		////////////////////////////////////////////////////////////////////////////
		TreeItem<String> root;

		root = new TreeItem<>(); //set the root for the prodcuts in cart tree
		root.setExpanded(true); //set it to expanded by default  

		if (this.newOrder.getProductsInOrder() != null)
		{

			lbl = "Your cart";
			for (ProductEntity product : this.newOrder.getProductsInOrder())
			{
				this.stringSet.add(product.getProductName());
				TreeItem<String> productName = new TreeItem<>(product.getProductName()); //set the branch as the product's name to be the parent of it's details
							/* Set all the product's details to be leaves on the branch */
				TreeItem<String> productID = new TreeItem<>(product.getProductID()); 		//create a new leaf
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

	}
/*
	public void showProductDetails(ActionEvent event) {
		for (ProductEntity product : this.newOrder.getProductsInOrder())
		{

		}
	} */

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
	 */
	public void backToNewOrderMenu(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//this.prdctTrVw = new TreeView<String>();
		//	showCart();

	}

}
