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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.MessageToSend;
/**
 * This class is the controller for the self defined product window
 * 
 * CustomerSelfDefinedProductController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerSelfDefinedProductController implements Initializable {

	/*FXML*/
	@FXML private ComboBox<String> dmnntClrCmb;
	@FXML private ComboBox<String> prdctTypeCmb;
	@FXML private Button fndPrdctBtn;
	@FXML private ComboBox<String> MinPrcCmb;
	@FXML private ComboBox<String> maxPrcCmb;
	@FXML private Button bckBtn;
	@FXML private ListView<ProductEntity> list;
	@FXML private Label resultlbl;
	@FXML private Button BackResultBtn;
	

	
	private OrderEntity newOrder;//contains the current order
	public ObservableList<String> listMinPrices;
	public ObservableList<String> listMaxPrices;
	public ObservableList<String> listDominantColors;
	public ObservableList<String> listProductTypes;

	private ArrayList<String> productTypes;
	private ArrayList<String> dominantColors;
	private ArrayList<String> minPrices;
	private ArrayList<String> maxPrices;
	
	private ArrayList<ProductEntity> ResultList;
    private ArrayList<ProductEntity> productsInOrder;

    /**
     * 
     * Constructor for the CustomerSelfDefinedProductController.java class
     */
    public CustomerSelfDefinedProductController(){
    	list=new ListView<ProductEntity>();
		productsInOrder=new ArrayList<ProductEntity>();
    }
    
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
		if(!this.dmnntClrCmb.getSelectionModel().isEmpty() && !this.dmnntClrCmb.getSelectionModel().getSelectedItem().equals("none"))
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
		resultlbl.setText(String.valueOf(matchingProducts.size()));
		UpdateResultList(matchingProducts);
	}

	/**
	 * This method builds and displays the list of products 
	 * @param ResultList of products
	 * @throws InterruptedException for thread sleep
	 */
	public void UpdateResultList(ArrayList<ProductEntity> ResultList) throws InterruptedException
	{
		int storeid, temp_key=0;                 
		double newPrice=0;
	    Iterator<Integer> itr ;
	    ObservableList<ProductEntity> prod=FXCollections.observableArrayList();
	    this.ResultList=ResultList;
	    storeid=newOrder.getStore().getBranchID();
		HashMap<Integer, Double> discount=new HashMap<Integer,Double>();//Integer->product id-key, Double->product price is the value
		discount=getDiscounts(storeid);                                                                 //get the discount for the specific store
		
		if(discount!=null) //If there are discounts for this store
		{
			itr=discount.keySet().iterator();                                                               //get the key's from the hash map of discounts
			
		/*Update store prices*/
	    while (itr.hasNext())
	    {	
	    	temp_key=(int) itr.next();
	    	for(int j=0;j<ResultList.size();j++)
	    	{
	    		if(temp_key==ResultList.get(j).getProductID())                                 // If there is a discount for this product then update, product price
	    		{
	    			newPrice=discount.get(temp_key);
	    			updatePrice(temp_key,newPrice);
		        }
	    	}
		   }
		}		
		
	    /*******************************************************Build the catalog view**********************************************/
	    
	    /*Add all products to observable List*/
		for(ProductEntity p: ResultList)
		{
			prod.add(p);
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
                                                	
                        	Button addToCart =new Button("Add To Cart");//add button "Add to cart" to each cell in the list view
                        	
                        	addToCart.setOnAction(new EventHandler<ActionEvent>() {//set the back button with an action event
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
                        
                        	if(product.getProductImage()!=null) //if there is an image to product, set image
                        	{
                        		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));
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
                        	if(!product.getProductDominantColor().equals("none"))//if there is dominant color to the product
                        		setText("              "+product.getProductName()+"  is a  "+product.getProductType()+",  \n          "+product.getProductDescription()+", in  "+product.getProductDominantColor()+"  colors  "+"\n              price  "+product.getSale()+product.getProductPrice()+"¤");
                        	else
                                setText("              "+product.getProductName()+"  is a  "+product.getProductType()+",  \n          "+product.getProductDescription()+"  \n              price:  "+product.getSale()+product.getProductPrice()+"¤");

                            setFont(Font.font(18));
                        }
                    }
                };
                return cell;
            }
        });
	list.setItems(prod);//set the items to the ListView
	}
	
	/**
	 * This method adds a product chosen by the customer to the cart
	 * @param product to add to cart
	 * @throws IOException for general message
	 */
	public void AddProductToCart(ProductEntity product) throws IOException
	{
		if(product.getSalePrice()!=null)//if there is a sale on this product update sale price
	    	product.setProductPrice(product.getSalePrice());
		productsInOrder.add(product);//add product to order
		GeneralMessageController.showMessage("Product : "+product.getProductName()+"  ,ID:  "+product.getProductID()+"\nAdded to cart");
	}
	/**
	 * This method updates the price of the product                                                                    
	 * @param key is the product id we pulled from the discount table
	 * @param price is the new price after sale on the product
	 */
	public void updatePrice(int key,double price)
	{
		for(int i=0;i<this.ResultList.size();i++)
		{
			if(this.ResultList.get(i)!=null)//id there are discounts for this store
			{
			if(this.ResultList.get(i).getProductID()==key)//if there is a sale on the product update his price
			{
				this.ResultList.get(i).setSalePrice(price);
				this.ResultList.get(i).setSale("ON SALE-> New Price :  ");//set sale message to product
				this.ResultList.get(i).setSale(this.ResultList.get(i).getSalePrice()+"¤"+"\n              Old price:  ");
			}
			}
		}
	}
	
	/**
	 * This method handles the back to order menu
	 * @param event pressed back
	 * @throws IOException for loader
	 */
	public void backToNewOrderMenu(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window

		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
		CreateNewOrderController cnoc = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		for(int i=0;i<this.productsInOrder.size();i++) //add products to the array list of the products in the order
	       {
	    	   newOrder.setProductsInOrder(productsInOrder.get(i));
	       }
		cnoc.setOrderDetails(newOrder);
		primaryStage.setTitle("New order from " + newOrder.getStore().getBranchName());
		primaryStage.setScene(scene);
		primaryStage.show();
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
		return discounts;                                                                     //return array list of discounts
	}
	
	
	/**
	 * This method sets the new orderEntitiy
	 * @param order  the new OrderEntity
	 */
	public void setOrder(OrderEntity order) {
		this.newOrder = order;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
		this.productTypes.add("Bridal");
		this.productTypes.add("Special");
		this.productTypes.add("Flower Boquet");
		
		this.listProductTypes = FXCollections.observableArrayList(this.productTypes);
		this.prdctTypeCmb.setItems(this.listProductTypes);
		
		this.dominantColors = new ArrayList<String>();		//set  the dominant colors
		this.dominantColors.add("none");
		this.dominantColors.add("Blue");
		this.dominantColors.add("Red");
		this.dominantColors.add("White");
		this.dominantColors.add("Yellow");
		this.dominantColors.add("Purple");
		this.dominantColors.add("Green");
		
		this.listDominantColors = FXCollections.observableArrayList(this.dominantColors);
		this.dmnntClrCmb.setItems(this.listDominantColors);
		
	}

}
