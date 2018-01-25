package gui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.ProductEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.MessageToSend;

/**
 * This class handles with the operations add and delete from the catalog 
 *AddDeleteCatalogController.java
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 * Project Name gitProjectX
 */
public class AddDeleteCatalogController implements Initializable{
	
	/*FXML*/
	@FXML private Label addInstrulbl;
	@FXML private Label deleteInstrulbl;
	@FXML private Label warninglbl;
	@FXML private Label Descriptionbl;
		
	@FXML private Button BackBtn;
	@FXML  private Button addBtn;
	@FXML  private Button ClearBtn;
	@FXML private Button deleteBtn;
	@FXML private Button searchBtn;
	
	@FXML private ListView<ProductEntity> listProducts; //list view of the products
	@FXML private ListView<ProductEntity> listCatalog;  //list view of the products in the catalog
	
    ArrayList<ProductEntity> productsFromTable = new ArrayList<ProductEntity>(); //contains the product entities returned from the data base

    /**
     * The method clear's the selections in the list view
     * @param event
     */
   public void ClearSelections(ActionEvent event)
   {
	   listCatalog.getSelectionModel().clearSelection();
	   listProducts.getSelectionModel().clearSelection();
   }
    
   /**
    * The method adds product to the catalog
    * @param event when "Add" button is clicked
    * @throws IOException for the loader
    * @throws InterruptedException for the thread sleep
    */
  public void AddToCatalog(ActionEvent event) throws IOException, InterruptedException {
    	
    	 ObservableList<ProductEntity> Products=FXCollections.observableArrayList(); //observable list to get products from list view
    	 ArrayList<ProductEntity> ProductsToAdd=new ArrayList<ProductEntity>(); //the products we want to add to the catalog

    	if(!(listProducts.getSelectionModel().getSelectedItems().isEmpty())) //if the user choose products
             {
    		Products=listProducts.getSelectionModel().getSelectedItems();
             }
    	  else 
        	{
    		GeneralMessageController.showMessage("Please choose product to delete");
    		return;
        	}
    	for (ProductEntity prd:Products)//add products to add to array list
    	{
    		ProductsToAdd.add((ProductEntity)prd);
    	}
    	
    	for(int i=0;i<ProductsToAdd.size();i++) //add products to data base
    	{
    		if(AddProductsToCatalogDB(ProductsToAdd.get(i)).equals("Added"))//if inserting succeeded
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToAdd.get(i).getProductName()+",  ID:  "+ProductsToAdd.get(i).getProductID()+" \n was added succsessfuly");
    		}
    		else 
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToAdd.get(i).getProductName()+",   ID:  "+ProductsToAdd.get(i).getProductID()+" \n  was not added");
    		}
    	}
    	ShowCatalog(); //show the catalog after the insertion
    }
    
  /**
   * The method shows the catalog, inserts the products to listView
   * @throws InterruptedException
   */
    public void ShowCatalog() throws InterruptedException {
    	
    	    ObservableList<ProductEntity> Products=FXCollections.observableArrayList(); //list to present the products in list view
    		listCatalog.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	//set the selection mode to multiple
    	    ArrayList<ProductEntity> productsFromCatalog = new ArrayList<ProductEntity>(); //contains the product entitys in the catalog

    	    productsFromCatalog=getCatalog(); //get the products in the catalog from the data base
    		
    	    /*******************************************************Build the catalog view**********************************************/
    		
    	    /*Add all products to observable List*/
    		for(ProductEntity p: productsFromCatalog)
    		{
    			Products.add(p);
    		}
    		
    		/*Insert data to ListView cell*/
    		listCatalog.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>(){
    	        @Override
    	        public ListCell<ProductEntity> call(ListView<ProductEntity> p) {
    	        	
    	            ListCell<ProductEntity> cell = new ListCell<ProductEntity>(){
    	                @Override
    	                protected void updateItem(ProductEntity product, boolean status) {
    	                    super.updateItem(product, status);
    	                    if (product != null) {
    	       
    	                    	if(product.getProductImage()!=null) //If there is an image to the product entity, set image
    	                    	{
    	                    		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));//set the image 
    	                    		ImageView v=new ImageView(j);//insert the image to image view
    	                    		v.setFitHeight(130);
    	                    		v.setFitWidth(130);
    	                    		VBox vb=new VBox(15);
    	                        	vb.getChildren().addAll(v);
    	                            setGraphic(vb);
    	                    	}
    	                    	/*Set text and font in cell*/
    	                        setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+" in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");//set the text in the list cell
    	                        setFont(Font.font(18));
    	                    }
    	                }
    	            };
    	            return cell;
    	        }
    	    });
    	listCatalog.setItems(Products);													//set the items to the ListView
    }

    /**
     * The method deletes product from the catalog
     * @param event when "Delete" button is clicked
     * @throws InterruptedException for thread sleep
     * @throws IOException for general message 
     */
    public void DeleteProductFromCatalog(ActionEvent event) throws InterruptedException, IOException 
    {
    	 ObservableList<ProductEntity> Products=FXCollections.observableArrayList();//products to delete from the list view
    	 ArrayList<ProductEntity> ProductsToDelete=new ArrayList<ProductEntity>();  //products to delete from data base
    	 
    	if(!(listCatalog.getSelectionModel().getSelectedItems().isEmpty()))				//if there are items to delete
    	{
    		Products=listCatalog.getSelectionModel().getSelectedItems();				//get the selected products
    	}
    	else 
    	{
    		GeneralMessageController.showMessage("Please choose product to delete");
    		return;
    	}
    	for (ProductEntity prd:Products)												                          //add products to delete to the array list
    	{
    		ProductsToDelete.add((ProductEntity)prd);
    	}
    	for(int i=0;i<ProductsToDelete.size();i++)//iterating the list of products to delete
    	{
    		if(DeleteProductFromCatalogDB(ProductsToDelete.get(i)).equals("Deleted")) //delete product from the catalog in the data base
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToDelete.get(i).getProductName()+",  ID:  "+ProductsToDelete.get(i).getProductID()+" \n was deleted succsessfuly");
    		}
    		else 
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToDelete.get(i).getProductName()+",   ID:  "+ProductsToDelete.get(i).getProductID()+" \n  was not deleted");
    		}
    	}
    	ShowCatalog(); 												//Show the catalog after the deletion operation
    }
    
    /**
     * The method show's the product's list 
     * @throws InterruptedException
     */
    public void ShowAllProduct() throws InterruptedException
    {               
        ObservableList<ProductEntity> Products=FXCollections.observableArrayList(); //list of products in the list view
    	listProducts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);//Set the selection mode to multiple	
    	productsFromTable=getAllProducts();//get all the products from the data base
    	
        /*******************************************************Build the catalog view**********************************************/
    	
        /*Add all products to observable List*/
    	for(ProductEntity p: productsFromTable)
    	{
    		Products.add(p);
    	}
    	
    	/*Insert data to ListView cell*/
    	listProducts.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>(){
            @Override
            public ListCell<ProductEntity> call(ListView<ProductEntity> p) {
            	
                ListCell<ProductEntity> cell = new ListCell<ProductEntity>(){
                    @Override
                    protected void updateItem(ProductEntity product, boolean status) {
                        super.updateItem(product, status);
                        if (product != null) {
           
                        	if(product.getProductImage()!=null) 				//If there is an image to the product
                        	{
                        		Image j=new Image(new ByteArrayInputStream(product.getProductImage()));//set the image
                        		ImageView v=new ImageView(j);
                        		v.setFitHeight(130);
                        		v.setFitWidth(130);
                        		VBox vb=new VBox(15);
                            	vb.getChildren().addAll(v);
                                setGraphic(vb);
                        	}
                        	if(product.getProductDominantColor().equals("none"))//if there is a dominant color to the product 
                        		setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+",\n        price:  "+product.getProductPrice()+"¤");//set text in list cell
                        	else 
                        		setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+", in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");//set text in list cell

                            setFont(Font.font(18));//set font to text
                        }
                    }
                };
                return cell;
            }
        });
    listProducts.setItems(Products);					//set the items to the ListView
    }

    /**
     * The method calls to show: the product's list and the catalog list
     * @throws InterruptedException
     */
    public void Show() throws InterruptedException
    {
    	ShowAllProduct();
    	ShowCatalog();
    }
    
    /***************************************************************Data Base******************************************************/
    
    /**
     * The method adds a product to the data base
     * @param product is the product we want to add to the products table
     * @return String the message from the data base (success or failed)
     * @throws InterruptedException for thread sleep 
     * @throws IOException for loader
     */
    public String AddProductsToCatalogDB(ProductEntity product) throws InterruptedException, IOException {
    	String msg;	
    	
    	MessageToSend mts=new MessageToSend(product,"addProductToCatalog");   //set the parameters to the server method
    	Client.getClientConnection().setDataFromUI(mts);					                             //set the data and the operation to send from the client to the server
    	Client.getClientConnection().accept();									                               	//sends to server
    	while(!Client.getClientConnection().getConfirmationFromServer())		            	//wait until server replies
    		Thread.sleep(100);
    	Client.getClientConnection().setConfirmationFromServer();		                            //reset confirmation to false
    	MessageToSend m = Client.getClientConnection().getMessageFromServer();
    	 msg = (String)m.getMessage();
    	return msg;                                                                                                         //return the message from the data base
    }	
    
    
    /**
     * The method get's all the products from the product table in the data base
     * @return an ArrayList of product  entities
     * @throws InterruptedException
     */
    public ArrayList<ProductEntity> getAllProducts() throws InterruptedException
    {
    	MessageToSend mts=new MessageToSend(null,"getAllProducts");                      //set parameters for the server's method
    	ArrayList<ProductEntity> dataFromServer = null;
    	Client.getClientConnection().setDataFromUI(mts);					                              //set the data and the operation to send from the client to the server
    	Client.getClientConnection().accept();								                                      //sends to server
    	while(!Client.getClientConnection().getConfirmationFromServer())	                      //wait until server replies
    		Thread.sleep(100);
    	Client.getClientConnection().setConfirmationFromServer();		                              //reset confirmation to false
    	MessageToSend m = Client.getClientConnection().getMessageFromServer();
    	dataFromServer = (ArrayList<ProductEntity>)m.getMessage();
    	return dataFromServer;                                                                                         //return the data from  the data base
    }
    
    
    /**
     * The method delet's a product from the products table in the data base
     * @param productToDelete the product we want to delete
     * @return String String the message from the data base (success or failed)
     * @throws InterruptedException
     */
   public String DeleteProductFromCatalogDB(ProductEntity productToDelete) throws InterruptedException
   {
	   		String msg;
	   		MessageToSend mts=new MessageToSend(productToDelete,"deleteProductFromCatalog");//set parameters for the server method
	   		Client.getClientConnection().setDataFromUI(mts);				                                    //set the data and the operation to send from the client to the server
	   		Client.getClientConnection().accept();										                                //sends to server
	   		while(!Client.getClientConnection().getConfirmationFromServer())		                    //wait until server replies
	   			Thread.sleep(100);
	   		Client.getClientConnection().setConfirmationFromServer();		                                //reset confirmation to false
	   		MessageToSend m = Client.getClientConnection().getMessageFromServer();
	   		 msg = (String)m.getMessage();
	   		return msg;                                                                                                             //return the message from the data base
   }
   
   
   /**
    * The method get's all the product's in the catalog
    * @return an ArrayList of product entities
    * @throws InterruptedException
    */
   public ArrayList<ProductEntity> getCatalog() throws InterruptedException
   {
			MessageToSend mts=new MessageToSend(null,"getCatalog");   		//set parameters for the server method
			ArrayList<ProductEntity> dataFromServer = null;
			Client.getClientConnection().setDataFromUI(mts);				            	//set the data and the operation to send from the client to the server
			Client.getClientConnection().accept();							                		//sends to server
			while(!Client.getClientConnection().getConfirmationFromServer())		//wait until server replies
				Thread.sleep(100);
			Client.getClientConnection().setConfirmationFromServer();		   		    //reset confirmation to false
			MessageToSend m = Client.getClientConnection().getMessageFromServer();
			dataFromServer = (ArrayList<ProductEntity>)m.getMessage();
			return dataFromServer;                                                                     //return data from data base
   }
    
   /**
    * This  method loads the previous window
    * @param event pressed back
    * @throws IOException for the loader
    */
    public void back(ActionEvent event) throws IOException
    {
    		((Node) event.getSource()).getScene().getWindow().hide(); 				                    //hide Current window
    		FXMLLoader loader = new FXMLLoader();
    		Parent root = loader.load(getClass().getResource("/gui/ChainWorkerMenuBoudary.fxml").openStream()); // load the fxml class
    		Stage primaryStage = new Stage();
    		Scene scene = new Scene(root);                                                                         //set scene
    		primaryStage.setTitle("Chain worker's main menu");                                           //set title
    		primaryStage.setScene(scene); 
    		primaryStage.show();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
