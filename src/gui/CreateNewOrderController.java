package gui;

import java.io.IOException;
import java.net.URL;
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
import sun.reflect.generics.tree.Tree;

public class CreateNewOrderController implements Initializable {

	private StoreEntity store;
	private Client clnt;
	private OrderEntity newOrder;
	@FXML
	private TreeView<String> PrdctsTrVw;
	@FXML 
	private Label crtEmptLbl;
	@FXML
	private Button crtBckBtn;

	@FXML
	private Button chkOutBtn;

	@FXML
	private ListView<?> dtlsLstVw;
	@FXML
	private TextArea crdTxtArea;
	
	@FXML
	private Button crdCnfrmBtn;
	@FXML
	private Button itmFrmCtlgBtn;

	@FXML
	private Button AddSlfDfItmBtm;

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
	 * @param clnt	the connection client
	 */
	public void setConnectionData(Client clnt,StoreEntity store)
	{
		this.store=store;
		this.clnt=clnt;
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
	 * This method shows the add card window when pressed "add new card" button
	 * @param event add a card pressed
	 * @throws IOException
	 */
	public void addCard(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CardBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Add a card");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * This method handles the card confirmation and add it to the order
	 * @param event	confirm pressed
	 */
	public void cardConfirm(ActionEvent event)
	{
		CardEntity newCard = new CardEntity(this.crdTxtArea.getText());		//get the text from the text area
		this.newOrder.setCard(newCard);										//save the new card to the new order
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	
	public void viewCart(ActionEvent event) throws IOException {
	
		String lbl ="";
		TreeItem<String> root;
		root = new TreeItem<>();	//set the root for the prodcuts in cart tree
		root.setExpanded(true);		//set it to expanded by default  
		

		if (this.newOrder.getProductsInOrder() != null)
		{
			////////////////// a made up list of products for testing ///////////////////
			for(int i=0;i<5;i++)
			{
				//this.newOrder.addProductToCart(new ProductEntity("a"+i,"b"+i,"c"+i,1.1+i,"e"+i,"f"+i));
			}
			////////////////////////////////////////////////////////////////////////////
			lbl="Your cart";
			for (ProductEntity product : this.newOrder.getProductsInOrder())
			{
				TreeItem<String> productName = new TreeItem<>(product.getProductName()); //set the branch as the product's name to be the parent of it's details
				/* Set all the product's details to be leaves on the branch */
				TreeItem<String> productID = new TreeItem<>(product.getProductID()); //create a new leaf
				productName.getChildren().add(productID); //set as a child 
				TreeItem<String> productType = new TreeItem<>(product.getProductType());
				productName.getChildren().add(productType);
				TreeItem<String> productPrice = new TreeItem<>(product.getProductPrice().toString());
				productName.getChildren().add(productPrice);
				TreeItem<String> productDescription = new TreeItem<>(product.getProductDescription());
				productName.getChildren().add(productDescription);
				if (product.getProductDominantColor() != null)
				{
					TreeItem<String> productDominantColor = new TreeItem<>(product.getProductDominantColor());
					productName.getChildren().add(productDominantColor);
				}
				root.getChildren().add(productName);
			}
			
		}else lbl = "Your Cart is empty";

		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/CartBoundary.fxml").openStream());
		CreateNewOrderController cnoc = loader.getController();
		this.PrdctsTrVw = new TreeView<>(root);
//		cnoc.PrdctsTrVw = new TreeView<>(root);
//		cnoc.PrdctsTrVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		cnoc.PrdctsTrVw.setRoot(root);
		cnoc.PrdctsTrVw.setShowRoot(false);	//make root expanded every time it starts
		cnoc.crtEmptLbl.setText(lbl);
		
		Stage primaryStage=new Stage();
		Scene scene=new Scene(pRoot);
		primaryStage.setTitle("Your cart");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	/**
	 * This method is used to return to the new order menu
	 * @param event	button back 
	 */
	public void backToNewOrderMenu(ActionEvent event)
	{
		((Node)event.getSource()).getScene().getWindow().hide();	//hide last window
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		this.newOrder = new OrderEntity();
		this.PrdctsTrVw = new TreeView<>();
	}
}