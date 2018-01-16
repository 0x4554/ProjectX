package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import client.Client;
import entities.ProductEntity;
import entities.StoreEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import logic.MessageToSend;
/**
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
	private Button checkOutBtn;
	@FXML
	private Button viewCartBtn;
	@FXML
	private ListView<ProductEntity> List;
	@FXML
	private Label catalogLbl;
	@FXML
	private HBox hb;
	
    private ArrayList<ProductEntity>productsFromTable;/*The array will contain the Products neede to be inserted to the catalog*/

   /*I added getProductsFromDB_ByID(store id method), i added updatePrice method, i added serial number in the the discount table,
     *I added discountnumber in the counters table in the DB, i changed the method get product in the server, i have changed this class ,
     *I added getItemFrom Catalog method in create new order class*/
    
	/**
	 * Constructor for the class
	 */
	public OrderFromCatalogController()
	{
		List=new ListView<ProductEntity>();
	}
	/**
	 * The method show's the catalog customized to the store that the customer choose to shop
	 * @param store is the instance of the store that the customer choose
	 * @throws InterruptedException
	 */
	public void showCatalog(StoreEntity store) throws InterruptedException
	{
		int storeid, i=0,temp_key=0;                 
		double newPrice=0;
	    Iterator<Integer> itr ;
	    productsFromTable=new ArrayList<ProductEntity>();
	    ObservableList<ProductEntity> prod=FXCollections.observableArrayList();
	    
	    storeid=store.getBranchID();                                           //get the branch id
		HashMap<Integer, Double> discount=new HashMap<Integer,Double>();//Integer->product id-key, Double->product price is the value
		discount=getDiscounts(storeid);                                     //get the discount for the specific store
		itr=discount.keySet().iterator();                                         //get the key's from the hash map of discounts
		
		ArrayList<Integer> products =new ArrayList<Integer>();//product id's from the Catalog 
		products=getProductsFromDB_ByID();                            //get the product id's from the catalog table in the data base
		
		/*Here we insert  the product's that need to be in the catalog */
		for(i=0;i<products.size();i++)
		{
			productsFromTable.add(getProduct(products.get(i)));//get product using product id from the product table
		}		
		i=0;
		
		/*Update store prices*/
	    while (itr.hasNext())
	    {	
	    	temp_key=(int) itr.next();
	    		if(temp_key==products.get(i))                                 // if there is a discount for this product then update
	    		{
	    			newPrice=discount.get(temp_key);
	    			updatePrice(temp_key,newPrice);
		        }
	    		i++;
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
                        	ImageView imgv=new ImageView(product.getProductImage());
                        	
                            setText("              "+product.getProductName()+"   "+product.getProductDescription()+"  " + "\n              "+product.getProductPrice()+"¤");
                            setFont(Font.font(18));
                            setGraphic(imgv);
                        }
                    }
                };
                return cell;
            }
        });
	List.setItems(prod);//set the items to the ListView
	}
	
	/*************************************************************Update Price**********************************************/
	/**
	 * This method updates the price of the product                                                                    
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
				this.productsFromTable.get(i).setProductPrice(price);
			}
			}
		}
	}
	/**
	 * This method returns the id of the products in the catalog table
	 * @return
	 * @throws InterruptedException
	 */
	public ArrayList<Integer> getProductsFromDB_ByID() throws InterruptedException {
		MessageToSend mts=new MessageToSend(null,"getCatalogByID");
		ArrayList<Integer> dataFromServer = null;
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		dataFromServer = (ArrayList<Integer>)m.getMessage();
		return dataFromServer;
}

	/**
	 * This method return's the product entity requested by product ID 
	 * @param productID-the product we want to get from the data base
	 * @return
	 * @throws InterruptedException 
	 */
	public ProductEntity getProduct(int productID) throws InterruptedException
	{	
		ProductEntity p;
		MessageToSend mts=new MessageToSend(productID,"getProduct");
		//ArrayList<ProductEntity> dataFromServer = null;
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		p = (ProductEntity)m.getMessage();
		//p=dataFromServer.get(0);
		return p;
	}
	
	/**
	 * This method return the relevant discounts for the specific store in order to attach the sale's to the catalog
	 * @param storeID is the store we want to shop from
	 * @return hash map of the product id and their new price
	 * @throws InterruptedException
	 */
	public HashMap<Integer,Double> getDiscounts(int storeID) throws InterruptedException
	{
		MessageToSend mts=new MessageToSend(storeID,"getDiscounts");
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
