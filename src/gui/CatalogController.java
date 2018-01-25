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
	 private MenuController previousController;
	 
	 /*Constructor*/
	 public CatalogController()
	 {
		 listV=new ListView<ProductEntity>();
	 }
	 
	 /**
	  * Setter for the previous controoler
	  * @param prev
	  */
	public void setPreviousController(MenuController prev)
	{
		this.previousController = prev;
	}
	 /**
	  * getter for the client
	  * @return
	  */
	public Client getClnt() {
		return clnt;
	}
	/**
	 * setter for the client
	 * @param clnt
	 */
	public void setClnt(Client clnt) {
		this.clnt = clnt;
	}
	/**
	 * getter for the stage
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	/**
	 * setter for the stage
	 * @param primaryStage
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	/**
	 * getter for the controller
	 * @return
	 */
	public CustomerMenuController getCstmc() {
		return cstmc;
	}
	/**
	 * setter for the controller
	 * @param cstmc
	 */
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

	    ObservableList<ProductEntity> prod=FXCollections.observableArrayList();
	    productsFromTable=getCatalog();
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
                        	else {
                         	}
                       	if(product.getProductDominantColor().equals("none"))
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
	listV.setItems(prod);//set the items to the ListView
	}
	/**
	 * The method return's to the customer's main menu
	 * @param event-button "Back" was clicked
	 * @throws IOException
	 */
	public void bckToMainMenu(ActionEvent event) throws IOException{//Back to the customer main menu
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
		
		this.previousController.showMenu();;
		
//		FXMLLoader loader = new FXMLLoader();
//		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
//		Stage primaryStage = new Stage();
//		Scene scene = new Scene(root);
//		primaryStage.setTitle("Customer Main Menu");
//		primaryStage.setScene(scene);
//		primaryStage.show();
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
