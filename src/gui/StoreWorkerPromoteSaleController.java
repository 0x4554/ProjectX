package gui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import client.Client;
import entities.ProductEntity;
import entities.StoreEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.MessageToSend;

/**
 * 
 * This class is the controller for the store worker promote sale controller
 * 
 * StoreWorkerPromoteSaleController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 *         Project Name gitProjectX
 *
 */
public class StoreWorkerPromoteSaleController {

	/* FXML */
	@FXML
	private Button BackBtn;
	@FXML
	private Button getDetailsBtn;
	@FXML
	private Button UpdateBtn;
	@FXML
	private Button clearBtn;
	@FXML
	private Button clearSelectionBtn;
	@FXML
	private Button sendMsgBtn;
	@FXML
	private Button deleteBtn;

	@FXML
	private TextArea msgTxt;
	@FXML
	private TextField NameTxt;
	@FXML
	private TextField TypeTxt;
	@FXML
	private TextField PriceTxt;
	@FXML
	private TextField DescriptionTxt;
	@FXML
	private TextField ImageTxt;
	@FXML
	private TextField ColorTxt;
	@FXML
	private TextField IDTxt;

	@FXML
	private Label warninglbl;
	@FXML
	private ListView<ProductEntity> list;

	public ObservableList<String> Type_list = FXCollections.observableArrayList(); 
	ArrayList<ProductEntity> productsFromTable = new ArrayList<ProductEntity>();
	private StoreEntity store;

	/**
	 * Constructor for the StoreWorkerPromoteSaleController.java class
	 */
	public StoreWorkerPromoteSaleController() {

	}

	/**
	 * This method sets the store
	 * 
	 * @param store
	 *  the store
	 */
	public void setStore(StoreEntity store) {
		this.store = store;
	}

	/**
	 * The method clear's the selections made in the list view
	 * 
	 * @param event
	 */
	public void ClearSelections(ActionEvent event) {
		list.getSelectionModel().clearSelection();
	}

	/**
	 * This method clears all the fields
	 * 
	 * @param event
	 *            pressed clear fields
	 */
	public void ClearFields(ActionEvent event) {
		IDTxt.clear();
		NameTxt.clear();
		TypeTxt.clear();
		PriceTxt.clear();
		DescriptionTxt.clear();
		ColorTxt.clear();
		msgTxt.clear();
	}

	/**
	 * This method sets the product's details to the fields
	 * 
	 * @param event pressed on a product from the list
	 * @throws IOException for the general message
	 */
	public void SetProductDetails(MouseEvent event) throws IOException {
		ProductEntity product=new ProductEntity();
		if(list.getSelectionModel().getSelectedItem() != null)//if there was a selection in the list view, set product details
		{
			product=list.getSelectionModel().getSelectedItem();
			IDTxt.setText(product.getProductID().toString());
			NameTxt.setText(product.getProductName());
			TypeTxt.setText(product.getProductType());
			PriceTxt.setText(product.getProductPrice().toString());
			DescriptionTxt.setText(product.getProductDescription());
			ColorTxt.setText(product.getProductDominantColor());
		}
		else
		{
			GeneralMessageController.showMessage("Please choose product from the list");
			return;
		}		
	}

	/**
	 * This method builds the list of products with updated prices and details
	 * 
	 * @throws InterruptedException for thread sleep
	 */
	public void ShowAllProduct() throws InterruptedException {
		 ObservableList<ProductEntity> Products=FXCollections.observableArrayList();
			list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	
			productsFromTable=getAllProducts();
			int storeid=0;
			
			/*Get Store discounts*/
		    storeid=store.getBranchID();
			HashMap<Integer, Double> discount=new HashMap<Integer,Double>();//Integer->product id-key, Double->product price is the value
			discount=getDiscounts(storeid);                                                                 //get the discount for the specific store
			
			if(discount!=null) //If there are discounts for this store
			{
				Iterator<Integer> itr=discount.keySet().iterator();
				itr=discount.keySet().iterator();                                                                 //get the key's from the hash map of discounts
				int temp_key=0;
				double newPrice=0;
				
			   /*Update store prices*/
		    while (itr.hasNext()) 
		    {	
		    	temp_key=(int) itr.next();
		    	for(int j=0;j<productsFromTable.size();j++)
		    	{
		    		if(temp_key==productsFromTable.get(j).getProductID())                      // If there is a discount for this product then update
		    		{
		    			newPrice=discount.get(temp_key);
		    			updatePrice(temp_key,newPrice);                                                   //update products price sending the new price and the product id
			        }
		    	}
			   }
			}		
		    /*******************************************************Build the catalog view**********************************************/
			
		    /*Add all products to observable List*/
			for(ProductEntity p: productsFromTable)
			{
				Products.add(p);
			}
			
			/*Insert data to ListView cell*/
			list.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>(){
		        @Override
		        public ListCell<ProductEntity> call(ListView<ProductEntity> p) {
		        	
		            ListCell<ProductEntity> cell = new ListCell<ProductEntity>(){
		                @Override
		                protected void updateItem(ProductEntity product, boolean status) {
		                    super.updateItem(product, status);
		                    if (product != null) {
		       
		                    	if(product.getProductImage()!=null)//if there is an image to the product, set image
		                    	{
		                    		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));
		                    		ImageView v=new ImageView(j);//set image in image view
		                    		v.setFitHeight(130);
		                    		v.setFitWidth(130);
		                    		VBox vb=new VBox(15);
		                        	vb.getChildren().addAll(v);
		                            setGraphic(vb);
		                    	}
		                    	if(product.getProductDominantColor().equals("none"))//if there is a dominant color to the product set it on text
		                        setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+",\n        price:  "+product.getSale()+product.getProductPrice()+"¤");
		                    	else
			                        setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+" ,in  "+product.getProductDominantColor()+"  color's  "+"\n        price:  "+product.getSale()+product.getProductPrice()+"¤");
		                        setFont(Font.font(18));
		                    }
		                }
		            };
		            return cell;
		        }
		    });
		list.setItems(Products);//set the items to the ListView
	}

	/**
	 * This method loads the previous window
	 * @param event pressed "Back"
	 * @throws IOException for the loader
	 */
	public void back(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreWorkerMenuBoundary.fxml").openStream());//load new window
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);                                         //set scene
		primaryStage.setTitle("Store worker main menu");              //set title
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method updates the price of the product according to the discounts promoted at the store
	 * @param key  is the product id we pulled from the discount table
	 * @param price    is the new price after sale on the product
	 */
	public void updatePrice(int key, double price) {
		for(int i=0;i<this.productsFromTable.size();i++)
		{
			if(this.productsFromTable.get(i)!=null)
			{
			if(this.productsFromTable.get(i).getProductID()==key)//if there is a sale on the product update his price
			{
				this.productsFromTable.get(i).setSalePrice(price);
				this.productsFromTable.get(i).setSale("ON SALE-> New Price :  ");//set sale message
				this.productsFromTable.get(i).setSale(this.productsFromTable.get(i).getSalePrice()+"¤"+"\n         Old price:  ");
			}
			}
		}
	}

	/**
	 * This method handles the send message for promoting the sale
	 * @param event  pressed send message
	 * @throws IOException for general message
	 */
	public void sendMessageToCustomers(ActionEvent event) throws IOException {
		if(!(msgTxt.getText().equals(""))) //if there is a message to send, send it
		{
	      GeneralMessageController.showMessage("Message : "+msgTxt.getText()+"  has been sent to all customers");
		}
	   else 
	   {
	    GeneralMessageController.showMessage("Please enter message to send");
	    return;
	   }
	}

	/**
	 * This method is for deleting a discount and clearing the fields
	 * @throws IOException for general message
	 * @throws InterruptedException for thread sleep
	 */ 
	public void deletDiscount() throws IOException, InterruptedException {
		ProductEntity product=new ProductEntity();
		if(list.getSelectionModel().getSelectedItem()!=null)//if selection was made by the user
		{
			product=list.getSelectionModel().getSelectedItem();//get the selected product
			
		if(deleteDiscountInDB(product,store.getBranchID()).equals("Success"))//delete the product from the discounts table in the data base
		{
			ShowAllProduct();//show products after operation, and clear all fields
			GeneralMessageController.showMessage("Product discount was deleted successfully");
			NameTxt.clear();
			PriceTxt.clear();
			DescriptionTxt.clear();
			ColorTxt.clear();
			IDTxt.clear();
			TypeTxt.clear();
		}
		else 
		{
			//clear all fields
			GeneralMessageController.showMessage("Discount deletion failed");
			NameTxt.clear();
			PriceTxt.clear();
			DescriptionTxt.clear();
			ColorTxt.clear();
			IDTxt.clear();
			TypeTxt.clear();
			return;
		}

		}else 
			{
			GeneralMessageController.showMessage("Please choose product In order to delete the discount");
		    return;
		}
	}

	/**
	 * The method adds a product or updates the chosen product by the user
	 * @param event  "Update Product" button clicked
	 * @throws IOException for general message
	 * @throws InterruptedException for thread sleep
	 */
	public void AddUpdateDiscount(ActionEvent event) throws IOException, InterruptedException {
		ProductEntity product=new ProductEntity();
		double pr;                 //the price entered by the user
		if(list.getSelectionModel().getSelectedItem()!=null)//if selection was made
		{
			product=list.getSelectionModel().getSelectedItem();

			if(!(PriceTxt.getText().equals(""))) //validate the price entered
			{
				try {
					pr=Double.parseDouble(PriceTxt.getText());
					if(pr>=product.getProductPrice() || pr<=0)//If the product price is not valid
					{
						GeneralMessageController.showMessage("The price is not vaild for the sale");
						return;
					}
					else product.setSalePrice(pr);
			}catch(NumberFormatException e)
				{
				GeneralMessageController.showMessage("Incorrect price");
				return;
				}
				
			}
			else
				{
				GeneralMessageController.showMessage("Please enter product new price");
				return;
				}
		if(AddDiscountToProductInDB(product,store.getBranchID()).equals("Success"))//add discount to the data base
		{
			ShowAllProduct();//show product list after operation, and clear all fields
			GeneralMessageController.showMessage("Product discount was Updated successfully");
			NameTxt.clear();
			PriceTxt.clear();
			DescriptionTxt.clear();
			ColorTxt.clear();
			IDTxt.clear();
			TypeTxt.clear();
		}
		else 
		{
			GeneralMessageController.showMessage("Discount Update failed");
			//clear all fields
			NameTxt.clear();
			PriceTxt.clear();
			DescriptionTxt.clear();
			ColorTxt.clear();
			IDTxt.clear();
			TypeTxt.clear();
			return;
		}

		}else 
			{
			GeneralMessageController.showMessage("Please choose product In order to attach discount");
		    return;
		}
	}

	/****************************************************************data base **********************************************************/
	/**
	 * This method is for deleting a discount from the DB
	 * @param Product to delete
	 * @param storeid the id of the store we are deleting the discount
	 * @return message from the server
	 */
	public String deleteDiscountInDB(ProductEntity Product, int storeid) {
		String msg;	
		HashMap<ProductEntity,Integer> map=new HashMap<ProductEntity,Integer>();
		map.put(Product, storeid);                                                                 //insert the store id and the product to hash map in order to send it to the server
		MessageToSend mts=new MessageToSend(map,"DeleteDiscount");//send message to server
		Client.getClientConnection().setDataFromUI(mts);					          //set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();									            //sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		 msg = (String)m.getMessage();
		return msg;//return message from server
	}

	/**
	 * This method adds the discount to the DB
	 * @param Product  the product to update           
	 * @param storeid       the store's id
	 * @return message from the server
	 */
	public String AddDiscountToProductInDB(ProductEntity Product, int storeid) {
		String msg;	
		HashMap<ProductEntity,Integer> map=new HashMap<ProductEntity,Integer>();
		map.put(Product, storeid);                                                        //insert the store id and the product to hash map in order to send it to the server
		MessageToSend mts=new MessageToSend(map,"AddDiscount");//send operation to server
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Client.getClientConnection().setConfirmationFromServer();		 //reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		 msg = (String)m.getMessage();
		return msg;//return the message from server
	}

	/**
	 * This method gets all the products from the DB
	 * @return an array list of the products from the table
	 * @throws InterruptedException
	 */
	public ArrayList<ProductEntity> getAllProducts() throws InterruptedException {
		MessageToSend mts=new MessageToSend(null,"getAllProducts"); //send message to server
		ArrayList<ProductEntity> dataFromServer = null;
		Client.getClientConnection().setDataFromUI(mts);				        	//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();									        	//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer()) //wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		       //reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		dataFromServer = (ArrayList<ProductEntity>)m.getMessage();
		return dataFromServer;   
	}

	/**
	 * This method return the relevant discounts for the specific store in order to attach the sale's to the catalog
	 * @param storeID    is the store we want to shop from       
	 * @return hash map of the product id and their new price
	 * @throws InterruptedException for thread sleep
	 */
	public HashMap<Integer, Double> getDiscounts(int storeID) throws InterruptedException {
		MessageToSend mts=new MessageToSend(storeID,"getDiscounts");//send message to server
		HashMap<Integer,Double> discounts=null;
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		discounts = (HashMap<Integer,Double>)m.getMessage();
		return discounts;
	}
}
