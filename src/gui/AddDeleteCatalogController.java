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

public class AddDeleteCatalogController implements Initializable{
	
	@FXML private Label addInstrulbl;
	@FXML private Label deleteInstrulbl;
	@FXML private Label warninglbl;
	@FXML private Label Descriptionbl;
		
	@FXML private Button BackBtn;
	@FXML  private Button addBtn;
	@FXML  private Button ClearBtn;
	@FXML private Button deleteBtn;
	@FXML private Button searchBtn;
	
	@FXML private ListView<ProductEntity> listProducts;
	@FXML private ListView<ProductEntity> listCatalog;
	
    ArrayList<ProductEntity> productsFromTable = new ArrayList<ProductEntity>();

   public void ClearSelections(ActionEvent event)
   {
	   listCatalog.getSelectionModel().clearSelection();
	   listProducts.getSelectionModel().clearSelection();
   }
    
  public void AddToCatalog(ActionEvent event) throws IOException, InterruptedException {
    	
    	 ObservableList<ProductEntity> Products=FXCollections.observableArrayList();
    	 ArrayList<ProductEntity> ProductsToAdd=new ArrayList<ProductEntity>();

    	if(!(listProducts.getSelectionModel().getSelectedItems().isEmpty()))
             {
    		Products=listProducts.getSelectionModel().getSelectedItems();
             }
    	  else 
        	{
    		GeneralMessageController.showMessage("Please choose product to delete");
    		return;
        	}
    	for (ProductEntity prd:Products)//add products to delete
    	{
    		ProductsToAdd.add((ProductEntity)prd);
    	}
    	
    	for(int i=0;i<ProductsToAdd.size();i++)
    	{
    		if(AddProductsToCatalogDB(ProductsToAdd.get(i)).equals("Added"))
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToAdd.get(i).getProductName()+",  ID:  "+ProductsToAdd.get(i).getProductID()+" \n was added succsessfuly");
    		}
    		else 
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToAdd.get(i).getProductName()+",   ID:  "+ProductsToAdd.get(i).getProductID()+" \n  was not added");
    		}
    	}
    	ShowCatalog();
    }
    
    public void ShowCatalog() throws InterruptedException {
    	   ObservableList<ProductEntity> Products=FXCollections.observableArrayList();
    		listCatalog.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	
    	    ArrayList<ProductEntity> productsFromCatalog = new ArrayList<ProductEntity>();

    	    productsFromCatalog=getCatalog();
    		
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
    	                        setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+" in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");
    	                        setFont(Font.font(18));
    	                    }
    	                }
    	            };
    	            return cell;
    	        }
    	    });
    	listCatalog.setItems(Products);//set the items to the ListView
    }
    
    public void DeleteProductFromCatalog(ActionEvent event) throws InterruptedException, IOException 
    {
    	 ObservableList<ProductEntity> Products=FXCollections.observableArrayList();
    	 ArrayList<ProductEntity> ProductsToDelete=new ArrayList<ProductEntity>();
    	if(!(listCatalog.getSelectionModel().getSelectedItems().isEmpty()))//get the items to delete
    	{
    		Products=listCatalog.getSelectionModel().getSelectedItems();
    	}
    	else 
    	{
    		GeneralMessageController.showMessage("Please choose product to delete");
    		return;
    	}
    	for (ProductEntity prd:Products)//add products to delete
    	{
    		ProductsToDelete.add((ProductEntity)prd);
    	}
    	for(int i=0;i<ProductsToDelete.size();i++)
    	{
    		if(DeleteProductFromCatalogDB(ProductsToDelete.get(i)).equals("Deleted"))
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToDelete.get(i).getProductName()+",  ID:  "+ProductsToDelete.get(i).getProductID()+" \n was deleted succsessfuly");
    		}
    		else 
    		{
    			GeneralMessageController.showMessage("Product : "+ProductsToDelete.get(i).getProductName()+",   ID:  "+ProductsToDelete.get(i).getProductID()+" \n  was not deleted");
    		}
    	}
    	ShowCatalog();
    }
    
    public void ShowAllProduct() throws InterruptedException
    {               
        ObservableList<ProductEntity> Products=FXCollections.observableArrayList();
    	listProducts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	
    	productsFromTable=getAllProducts();
    	
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
                            setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+" in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");
                            setFont(Font.font(18));
                        }
                    }
                };
                return cell;
            }
        });
    listProducts.setItems(Products);//set the items to the ListView
    }

    public void Show() throws InterruptedException
    {
    	ShowAllProduct();
    	ShowCatalog();
    }
    
    /************************Data Base***********************/
    
    public String AddProductsToCatalogDB(ProductEntity product) throws InterruptedException, IOException {
    	String msg;	
    	
    	MessageToSend mts=new MessageToSend(product,"addProductToCatalog");
    	Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
    	Client.getClientConnection().accept();										//sends to server
    	while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
    		Thread.sleep(100);
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
    
   public String DeleteProductFromCatalogDB(ProductEntity productToDelete) throws InterruptedException
   {
	   		String msg;
	   		MessageToSend mts=new MessageToSend(productToDelete,"deleteProductFromCatalog");
	   		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
	   		Client.getClientConnection().accept();										//sends to server
	   		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
	   			Thread.sleep(100);
	   		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
	   		MessageToSend m = Client.getClientConnection().getMessageFromServer();
	   		 msg = (String)m.getMessage();
	   		return msg;   
   }
   
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
    
    public void back(ActionEvent event) throws IOException
    {
    		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
    		FXMLLoader loader = new FXMLLoader();
    		Parent root = loader.load(getClass().getResource("/gui/ChainWorkerMenuBoudary.fxml").openStream());
    		Stage primaryStage = new Stage();
    		Scene scene = new Scene(root);
    		primaryStage.setTitle("Chain worker's main menu");
    		primaryStage.setScene(scene);
    		primaryStage.show();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
