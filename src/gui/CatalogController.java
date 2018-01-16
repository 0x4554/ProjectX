package gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import entities.ComplaintEntity;
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
	
	
	//*buttons of the customer menu*//
	  ListView<ProductEntity> List=new ListView<ProductEntity>();
	 private Button SearchItemButton;
	 private TextField EnterProductname;
	 private Button CheckOut;
	 private Button backFromcatalog;
	 private CustomerMenuController cstmc;
	 private ObservableList<ProductEntity> products=FXCollections.observableArrayList();
	 
	 
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
	public ListView<ProductEntity> getList() {
		return List;
	}
	public void setList(ListView<ProductEntity> list) {
		List = list;
	}
	public Button getSearchItemButton() {
		return SearchItemButton;
	}
	public void setSearchItemButton(Button searchItemButton) {
		SearchItemButton = searchItemButton;
	}
	public TextField getEnterProductname() {
		return EnterProductname;
	}
	public void setEnterProductname(TextField enterProductname) {
		EnterProductname = enterProductname;
	}
	public Button getCheckOut() {
		return CheckOut;
	}
	public void setCheckOut(Button checkOut) {
		CheckOut = checkOut;
	}
	public Button getBackFromcatalog() {
		return backFromcatalog;
	}
	public void setBackFromcatalog(Button backFromcatalog) {
		this.backFromcatalog = backFromcatalog;
	}
	public CustomerMenuController getCstmc() {
		return cstmc;
	}
	public void setCstmc(CustomerMenuController cstmc) {
		this.cstmc = cstmc;
	}
	public ObservableList<ProductEntity> getProducts() {
		return products;
	}
	public void setProducts(ObservableList<ProductEntity> products) {
		this.products = products;
	}
	/**
	 * A necessary constructor for the App
	 */
	public CatalogController()
	{
		
	}
	public void setConnectionData(CustomerMenuController cmc) {
		this.cstmc=cmc;
	}

	public void AddItemToCart(ActionEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("CustomerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
		
	public void bckToMainMenu() throws IOException{//Back to the customer main menu
		this.primaryStage.hide();
		this.cstmc.showCustomerMenu();
		return;
	}
	
	public void showProductcatalog() throws IOException//fix this shit//
, InterruptedException
	{		 
		products.setAll(getProduct());//get the product list
		
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
		
		List.setItems(products);//set the items
		/*set list view size*/
		List.setMinWidth(500);
		List.setMinHeight(300);
		
        /*set buttons*/
		backFromcatalog=new Button("Back");
		
		/*set an Hbox*/
		HBox hBox=new HBox();
		hBox.setPadding(new Insets(10,10,10,10));
		hBox.setSpacing(20);
		hBox.getChildren().addAll(backFromcatalog);
		
		/*set a Vbox*/
		VBox Vbox1=new VBox(10);
		Vbox1.setPadding(new Insets(30,30,30,30));
		Vbox1.getChildren().addAll(List,hBox);

		/*set scene*/
		Scene sc=new Scene(Vbox1);
		sc.getStylesheets().add("/gui/LoginStyle.css");
		this.primaryStage.setTitle("Zer-Li Catalog");
		this.primaryStage.setScene(sc);
		
		
        backFromcatalog.setOnAction(new EventHandler<ActionEvent>() {//set the back button with an action event
            	 @Override
            	  public void handle(ActionEvent event) {
            		    ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
            		    try {
							bckToMainMenu();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			
               }
            });
	   this.primaryStage.show();	
	}
	
	public ObservableList<ProductEntity> getProduct() throws InterruptedException //this method creat's a list of products
	{
		ObservableList<ProductEntity> prod=FXCollections.observableArrayList();
		ArrayList<ProductEntity> products=new ArrayList<ProductEntity>();
		products=getProductsFromDB();
		for(ProductEntity i: products)
		{
			prod.add(i);
		}
		System.out.println(prod);
		/*prod.add(new ProductEntity(123,"lian","boquet",(double) 20,"bridal","blue", new Image(getClass().getResourceAsStream("/images/pic1.jpg"),(double)100,(double)100,true,true)));
	    prod.add(new ProductEntity(124,"lili","boquet",(double) 15.60,"bridal","red",new Image(getClass().getResourceAsStream("/images/pic2.jpg"),(double)100,(double)100,true,true)));
	    prod.add(new ProductEntity(125,"magic","boquet",(double) 80,"bridal","red",new Image(getClass().getResourceAsStream("/images/pic3.jpg"),(double)100,(double)100,true,true)));
	    prod.add(new ProductEntity(126,"bird","boquet",(double) 96,"bridal","red",new Image(getClass().getResourceAsStream("/images/pic4.jpg"),(double)100,(double)100,true,true)));*/
		return prod;
	}
	
	
	/**
	 * This method gets the products from the DB (table catalog) and set's them in the catalog
	 * @return
	 * @throws InterruptedException
	 */
	public ArrayList<ProductEntity> getProductsFromDB() throws InterruptedException {
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
	
	public String addProductsToDB(ProductEntity product) throws InterruptedException {
		String msg;
		//ProductEntity product=new ProductEntity(123,"lian","boquet",(double) 20,"bridal","blue");
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
	
	public String DeleteProductsFromDB() throws InterruptedException {
		String msg;
		ProductEntity p=new ProductEntity(123,"lian","boquet",(double) 20,"bridal","blue");
		MessageToSend mts=new MessageToSend(p,"deleteProductFromCatalog");
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		 msg = (String)m.getMessage();
		return msg;
}
	public ProductEntity searchProductInCatalog(int productid) throws InterruptedException/******* lana**********************************/
	{
		ProductEntity p=new ProductEntity();
		MessageToSend mts=new MessageToSend(productid,"serachProductInCatalog");
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		p = (ProductEntity)m.getMessage();
		System.out.println(p.getProductID()+p.getProductDominantColor());
		return p;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
}
