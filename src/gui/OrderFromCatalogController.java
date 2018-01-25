package gui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.MessageToSend;
/**
 * This class is the controller for the order from catalog boundary
 * 
 *OrderFromCatalogController.java
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 * Project Name gitProjectX
 */
public class OrderFromCatalogController implements Initializable{

	@FXML
	private Button backBtn;
	@FXML
	private ListView<ProductEntity> List;
	@FXML
	private Label catalogLbl;
	@FXML
	private HBox hb;
	
    private ArrayList<ProductEntity>productsFromTable;/*The array will contain the Products inserted to the catalog*/
    private ArrayList<ProductEntity> productsInOrder;   /*The array will contain the products in the order*/
    Stage primaryStage=new Stage();
    private OrderEntity newOrder;
	CreateNewOrderController ordCon =new CreateNewOrderController();
    private int flag=0;
	/**
	 * Constructor 
	 */
	public OrderFromCatalogController()
	{
		List=new ListView<ProductEntity>();/*Initialize the list's*/
		productsInOrder=new ArrayList<ProductEntity>();
	}
	
	/*************************************************************Method's****************************************/
	/**
	 * The method show's the catalog customized to the store that the customer choose to shop from
	 * @param order is the instance of the order that the customer created
	 * @throws InterruptedException  for thread sleep
	 */
	public void showCatalog(OrderEntity order) throws InterruptedException
	{
		int storeid,temp_key=0;               
		double newPrice=0;
	    Iterator<Integer> itr ;                                                                           //to iterate over the discounts hash map
	    productsFromTable=new ArrayList<ProductEntity>();                         //holds the products from the products list 
	    ObservableList<ProductEntity> prod=FXCollections.observableArrayList();
	    productsFromTable=getCatalog();                                                      //insert products from the products list
	    newOrder=order;
	   
	    /*Get Store discounts*/
	    storeid=order.getStore().getBranchID();
		HashMap<Integer, Double> discount=new HashMap<Integer,Double>();//Integer->product id-key, Double->product price is the value
		discount=getDiscounts(storeid);                                                                 //get the discount for the specific store
	
		
		if(discount!=null) //If there are discounts for this store
		{
		itr=discount.keySet().iterator();                                                                             //get the key's from the hash map of discounts
		/*Update store prices*/
	    while (itr.hasNext())
	    {	
	    	temp_key=(int) itr.next();
	    	for(int j=0;j<productsFromTable.size();j++)
	    	{
	    		if(temp_key==productsFromTable.get(j).getProductID())                                 // If there is a discount for this product then update
	    		{
	    			newPrice=discount.get(temp_key);
	    			updatePrice(temp_key,newPrice);                                                                   //update the prices for the products
		        }
	    	}
		   }
		}		
		
	    /*******************************************************Build the catalog view**********************************************/
	  
	    
	    /*Add all products to observable List*/
		for(ProductEntity p: productsFromTable)
		{
			prod.add(p);
		}
		
		/*Insert data to ListView cell*/
		List.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>(){
			 
            @Override
            public ListCell<ProductEntity> call(ListView<ProductEntity> p) {
            	
                ListCell<ProductEntity> cell = new ListCell<ProductEntity>(){
                    @Override
                    protected void updateItem(ProductEntity product, boolean status) {
                        super.updateItem(product, status);
                        if (product != null) {
                                                	
                        	Button addToCart =new Button("Add To Cart");                       //add button "Add to cart" to every cell in the list view
                        	addToCart.setOnAction(new EventHandler<ActionEvent>() {   //Set the "Add to cart" button with an action event add to cart
                           	 @Override
                           	  public void handle(ActionEvent event) {
                           		 
               							try {
											AddProductToCart(product);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
                              }
                           });
                        	if(product.getProductImage()!=null)//if there is an image to the product, set the image
                        	{
                        		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));//convert byte array to image
                        		ImageView v=new ImageView(j);
                        		v.setFitHeight(130);
                        		v.setFitWidth(130);
                        		VBox vb=new VBox(15);
                        		addToCart.setMinWidth(130);
                            	vb.getChildren().addAll(v,addToCart);
                                setGraphic(vb);
                        	}
                        	else {
                        		setGraphic(addToCart);
                        	}
                        	if(product.getProductDominantColor().equals("none"))//if there is a color to the product
                        		setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+",\n        price:  "+product.getProductPrice()+"¤");//set text in list cell
                        	else 
                        		setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+", in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");//set text in list cell
                            setFont(Font.font(18));

                        }
                    }
                };
                return cell;
            }
        });
	List.setItems(prod);//set the items to the ListView
	}
	
	/**
	 * This method return's to the previous menu
	 * @param event when "Back" button clicked
	 * @throws IOException for the loader
	 */
	  public void backToOrder(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
		CreateNewOrderController cnoc = loader.getController();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

       for(int i=0;i<this.productsInOrder.size();i++)//transfer the products to products list in the order 
       {
    	   newOrder.setProductsInOrder(productsInOrder.get(i));
       }
		cnoc.setOrderDetails(newOrder);
		primaryStage.setTitle("New order from " + newOrder.getStore().getBranchName());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method add's a product chosen by the customer to the cart
	 * @param product that we want to add to the cart
	 * @throws IOException for the loader
	 */
	public void AddProductToCart(ProductEntity product) throws IOException
	{
		if(product.getSalePrice()!=null)    //if there is a sale on this product, set sale
	    	product.setProductPrice(product.getSalePrice());
		productsInOrder.add(product);//add product to the order
		GeneralMessageController.showMessage("Product : "+product.getProductName()+"  ,ID:  "+product.getProductID()+"\nAdded to cart");
	}
	
	/**
	 * This method updates the price of the product according to the discount's promoted at the store                                                            
	 * @param key is the product id we pulled from the discount table
	 * @param price is the new price after sale on the product
	 */
	public void updatePrice(int key,double price)
	{
		for(int i=0;i<this.productsFromTable.size();i++)
		{
			if(this.productsFromTable.get(i)!=null)
			{
			if(this.productsFromTable.get(i).getProductID()==key)//if there is a sale on the product update his price
			{
				this.productsFromTable.get(i).setSalePrice(price);
				this.productsFromTable.get(i).setSale("ON SALE-> New Price :  ");  //set sale message to the product
				this.productsFromTable.get(i).setSale(this.productsFromTable.get(i).getSalePrice()+"¤"+"\n              Old price:  ");
			}
			}
		}
	}
	
	/************************************************Data Base***********************************************************/
	
	/**
	 * This method gets the list of products in the catalog from the server
	 * @return arrayList of product entity
	 * @throws InterruptedException for thread sleep
	 */
	 public ArrayList<ProductEntity> getCatalog() throws InterruptedException
	   {
				MessageToSend mts=new MessageToSend(null,"getCatalog"); //send message to server
				ArrayList<ProductEntity> dataFromServer = null;
				Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
				Client.getClientConnection().accept();										//sends to server
				while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
					Thread.sleep(100);
				Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
				MessageToSend m = Client.getClientConnection().getMessageFromServer();
				dataFromServer = (ArrayList<ProductEntity>)m.getMessage();
				return dataFromServer;
	   }

	
	/**
	 * This method return the relevant discounts for the specific store in order to attach the sale's to the catalog
	 * @param storeID is the store we want to shop from
	 * @return hash map of the product id and their new price
	 * @throws InterruptedException for thread sleep
	 */
	public HashMap<Integer,Double> getDiscounts(int storeID) throws InterruptedException
	{
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	
}
