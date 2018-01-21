package gui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import client.Client;
import entities.ComplaintEntity;
import entities.OrderEntity;
import entities.ProductEntity;
import entities.ComplaintEntity.Status;
import javafx.application.Application.Parameters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.FilesConverter;
import logic.MessageToSend;

/**
 * This program presents the catalog to the costumer and preform's Add, Delete, Edit products from the Data Base
 *   
 *CatalogController.java
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 * Project Name gitProjectX
 */

public class CatalogController implements Initializable{
	
	private Client clnt;
	Stage primaryStage=new Stage();
	
	
	@FXML private ListView<ProductEntity>listV;
	@FXML private Button backC;

	 private CustomerMenuController cstmc;
	 
	 /*Constructor*/
	 public CatalogController()
	 {
		 listV=new ListView<ProductEntity>();
	 }
	 
	 /*setters and getters for the variables*/
	public Client getClnt() {
		return clnt;
	}
	public void setClnt(Client clnt) {
		this.clnt = clnt;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	public CustomerMenuController getCstmc() {
		return cstmc;
	}
	public void setCstmc(CustomerMenuController cstmc) {
		this.cstmc = cstmc;
	}
	
	
	/**
	 * A necessary constructor for the App
	 */
	public void setConnectionData(CustomerMenuController cmc) {
		this.cstmc=cmc;
	}

	/**
	 * The method show's the catalog of the chain Zer-Li
	 * @throws InterruptedException
	 */
	public void showCatalog() throws InterruptedException
	{
	    ArrayList<ProductEntity>productsFromTable=new ArrayList<ProductEntity>();/*The array will contain the Products inserted to the catalog*/

	//	int storeid, i=0,temp_key=0;                 
	//	double newPrice=0;
	//    Iterator<Integer> itr ;
	    ObservableList<ProductEntity> prod=FXCollections.observableArrayList();
	    productsFromTable=getCatalog();
	   // newOrder=order;
	 
	   
	    /*Get Store discounts*/
	    //storeid=order.getStore().getBranchID();
		//HashMap<Integer, Double> discount=new HashMap<Integer,Double>();//Integer->product id-key, Double->product price is the value
		//discount=getDiscounts(storeid);                                                                 //get the discount for the specific store
	
		//i=0;
		/*
		if(discount!=null) //If there are discounts for this store
		{
			itr=discount.keySet().iterator();                                         //get the key's from the hash map of discounts
		/*Update store prices//
	    while (itr.hasNext())
	    {	
	    	temp_key=(int) itr.next();
	    		if(temp_key==productsFromTable.get(i).getProductID())                                 // If there is a discount for this product then update
	    		{
	    			newPrice=discount.get(temp_key);
	    			updatePrice(temp_key,newPrice);
		        }
	    		i++;
		   }
		}		
		*/
	    /*******************************************************Build the catalog view**********************************************/
	    
	    /*Add all products to observable List*/
		for(ProductEntity p: productsFromTable)
		{
			prod.add(p);
		}
		
		/*Insert data to ListView cell*/
		listV.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>(){
			 
            @Override
            public ListCell<ProductEntity> call(ListView<ProductEntity> p) {
            	
                ListCell<ProductEntity> cell = new ListCell<ProductEntity>(){
                    @Override
                    protected void updateItem(ProductEntity product, boolean status) {
                        super.updateItem(product, status);
                        if (product != null) {
                                                	
                        	//Button addToCart =new Button("Add To Cart");
                        	
                       // 	addToCart.setOnAction(new EventHandler<ActionEvent>() {//Set the back button with an action event
                           	 //@Override
                           	/*  public void handle(ActionEvent event) {
                           	/*	 
               							try {
											//AddProductToCart(product);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
                              }
                           });*/
                        	if(product.getProductImage()!=null)
                        	{
                        		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));
                        		ImageView v=new ImageView(j);
                        		v.setFitHeight(130);
                        		v.setFitWidth(130);
                        		VBox vb=new VBox(15);
                        		//addToCart.setMinWidth(130);
                            	vb.getChildren().addAll(v);
                                setGraphic(vb);
                        	}
                        	else {
                        		//setGraphic(addToCart);
                        	}
                            setText("              "+product.getProductName()+"   "+product.getProductDescription()+"  " + "\n              "+product.getProductPrice()+"¤");
                            setFont(Font.font(18));
                        }
                    }
                };
                return cell;
            }
        });
	listV.setItems(prod);//set the items to the ListView
	}
	/**
	 * The method return's to the customer's main menu
	 * @param event-button "Back" was clicked
	 * @throws IOException
	 */
	public void bckToMainMenu(ActionEvent event) throws IOException{//Back to the customer main menu
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Customer Main Menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	


	/**
	 * The method return's all the product's in the chain main catalog
	 * @return
	 * @throws InterruptedException
	 */
	 public ArrayList<ProductEntity> getCatalog() throws InterruptedException
	   {
				MessageToSend mts=new MessageToSend(null,"getCatalog");
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
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
}
