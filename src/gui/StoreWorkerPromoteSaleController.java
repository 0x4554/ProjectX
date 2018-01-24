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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class StoreWorkerPromoteSaleController {
	
	/*FXML*/
	@FXML private Button BackBtn;
	@FXML private Button getDetailsBtn;
	@FXML private Button UpdateBtn;
	@FXML private Button clearBtn;
	@FXML private Button clearSelectionBtn;
	@FXML private Button sendMsgBtn;
	@FXML private Button deleteBtn;
	
	@FXML private TextArea msgTxt;
	@FXML private TextField NameTxt;
	@FXML private TextField TypeTxt;
	@FXML private TextField PriceTxt;
	@FXML private TextField DescriptionTxt;
	@FXML private TextField ImageTxt;
	@FXML private TextField ColorTxt;
	@FXML private TextField IDTxt;
	
	@FXML private Label warninglbl;
	@FXML private ListView<ProductEntity> list;
	
	public ObservableList<String> Type_list=FXCollections.observableArrayList();
    ArrayList<ProductEntity> productsFromTable = new ArrayList<ProductEntity>();
	private StoreEntity store;
	
	public StoreWorkerPromoteSaleController()
	{
		
	}
	
	/**
	 * This method sets the store
	 * @param store	the store
	 */
	public void setStore(StoreEntity store)
	{
		this.store=store;
	}
	
	/**
	 * The method clear's the selection's made in the list view
	 * @param event
	 */
public void ClearSelections(ActionEvent event)
	   {
		   list.getSelectionModel().clearSelection();
	   }
	
public void ClearFields(ActionEvent event)
	{
		IDTxt.clear();
		NameTxt.clear();
		TypeTxt.clear();
		PriceTxt.clear();
		DescriptionTxt.clear();
		ColorTxt.clear();
		msgTxt.clear();
	}
	
public void SetProductDetails(MouseEvent event) throws IOException
	{
		ProductEntity product=new ProductEntity();
		if(list.getSelectionModel().getSelectedItem() != null)
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
	
public void ShowAllProduct() throws InterruptedException
	{               
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
			itr=discount.keySet().iterator();                                                                             //get the key's from the hash map of discounts
			int temp_key=0;
			double newPrice=0;
			
		   /*Update store prices*/
	    while (itr.hasNext())
	    {	
	    	temp_key=(int) itr.next();
	    	for(int j=0;j<productsFromTable.size();j++)
	    	{
	    		if(temp_key==productsFromTable.get(j).getProductID())                                 // If there is a discount for this product then update
	    		{
	    			newPrice=discount.get(temp_key);
	    			updatePrice(temp_key,newPrice);
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
	       
	                    	if(product.getProductImage()!=null)
	                    	{
	                    		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));
	                    		ImageView v=new ImageView(j);
	                    		v.setFitHeight(130);
	                    		v.setFitWidth(130);
	                    		VBox vb=new VBox(15);
	                        	vb.getChildren().addAll(v);
	                            setGraphic(vb);
	                    	}
	                        setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+" in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        "+product.getSale()+product.getProductPrice()+"�");
	                        setFont(Font.font(18));
	                    }
	                }
	            };
	            return cell;
	        }
	    });
	list.setItems(Products);//set the items to the ListView
	}

public void back(ActionEvent event) throws IOException
	{
			((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/StoreWorkerMenuBoundary.fxml").openStream());
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			primaryStage.setTitle("Store worker main menu");
			primaryStage.setScene(scene);
			primaryStage.show();
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
			this.productsFromTable.get(i).setSale("ON SALE-> New Price :  ");
			this.productsFromTable.get(i).setSale(this.productsFromTable.get(i).getSalePrice()+"�"+"\n         Old price:  ");
		}
		}
	}
}

public void sendMessageToCustomers(ActionEvent event) throws IOException
{
	if(!(msgTxt.getText().equals("")))
	{
      GeneralMessageController.showMessage("Message : "+msgTxt.getText()+"  has been sent to all customers");
	}
   else 
   {
    GeneralMessageController.showMessage("Please enter message to send");
    return;
   }
}

public void deletDiscount() throws IOException, InterruptedException
{
	ProductEntity product=new ProductEntity();
	if(list.getSelectionModel().getSelectedItem()!=null)
	{
		product=list.getSelectionModel().getSelectedItem();
		
	if(deleteDiscountInDB(product,store.getBranchID()).equals("Success"))
	{
		ShowAllProduct();
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
 * The method update's the chosen product
 * @param event "Update Product" button clicked
 * @throws IOException
 * @throws InterruptedException
 */
public 	void AddUpdateDiscount(ActionEvent event) throws IOException, InterruptedException
{
ProductEntity product=new ProductEntity();
double pr;
if(list.getSelectionModel().getSelectedItem()!=null)
{
	product=list.getSelectionModel().getSelectedItem();

	if(!(PriceTxt.getText().equals(""))) 
	{
		pr=Double.parseDouble(PriceTxt.getText());
		if(pr>=product.getProductPrice() || pr<=0)//If the product price is not valid
		{
			GeneralMessageController.showMessage("The price is not vaild for the sale");
			return;
		}
		else product.setSalePrice(Double.parseDouble(PriceTxt.getText()));
	}
	else
		{
		GeneralMessageController.showMessage("Please enter product new price");
		return;
		}
if(AddDiscountToProductInDB(product,store.getBranchID()).equals("Success"))
{
	ShowAllProduct();
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

public String deleteDiscountInDB(ProductEntity Product, int storeid)
{
	String msg;	
	HashMap<ProductEntity,Integer> map=new HashMap<ProductEntity,Integer>();
	map.put(Product, storeid);
	MessageToSend mts=new MessageToSend(map,"DeleteDiscount");
	Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
	Client.getClientConnection().accept();										//sends to server
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
	return msg;
}

public String AddDiscountToProductInDB( ProductEntity Product,int storeid) {
	String msg;	
	HashMap<ProductEntity,Integer> map=new HashMap<ProductEntity,Integer>();
	map.put(Product, storeid);
	MessageToSend mts=new MessageToSend(map,"AddDiscount");
	Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
	Client.getClientConnection().accept();										//sends to server
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
	return msg;
}

public ArrayList<ProductEntity> getAllProducts() throws InterruptedException
	{
		MessageToSend mts=new MessageToSend(null,"getAllProducts");
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
}
