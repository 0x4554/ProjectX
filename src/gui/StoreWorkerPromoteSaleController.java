package gui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import client.Client;
import entities.ProductEntity;
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
	
	public StoreWorkerPromoteSaleController()
	{
		
	}
	
	/*EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
		SetProductDetails(event);
	};

	list.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle); 
	*/
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
	                        setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+" in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");
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
 * The method update's the chosen product
 * @param event "Update Product" button clicked
 * @throws IOException
 * @throws InterruptedException
 */
public 	void UpdateProduct(ActionEvent event) throws IOException, InterruptedException
{
ProductEntity product=new ProductEntity();
ProductEntity OldProduct=new ProductEntity();
double pr;
if(list.getSelectionModel().getSelectedItem()!=null)
{
    OldProduct=list.getSelectionModel().getSelectedItem();

	if(!(PriceTxt.getText().equals(""))) {
		pr=Double.parseDouble(PriceTxt.getText());
		if(pr>=product.getProductPrice() || pr<=0)//If the product price is not valid
		{
			GeneralMessageController.showMessage("The price is not vaild for a sale");
		}
		else product.setProductPrice(Double.parseDouble(PriceTxt.getText()));
	}
	else
		{
		GeneralMessageController.showMessage("Please enter product new price");
		return;
		}
	
if(UpdateProductInDB(product,OldProduct).equals("Success"))
{
	ShowAllProduct();
	GeneralMessageController.showMessage("Product was Updated successfully");
	NameTxt.clear();
	PriceTxt.clear();
	DescriptionTxt.clear();
	ImageTxt.clear();
	ColorTxt.clear();
	IDTxt.clear();
	TypeTxt.clear();
}
else 
{
	GeneralMessageController.showMessage("Update failed");
	NameTxt.clear();
	PriceTxt.clear();
	DescriptionTxt.clear();
	ImageTxt.clear();
	ColorTxt.clear();
	IDTxt.clear();
	TypeTxt.clear();
	return;
}

}else 
	{
	GeneralMessageController.showMessage("Please choose product to update");
    return;
}
}

/***********************************************Data Base***********************************************/
	public String UpdateProductInDB(ProductEntity productToUpdate,ProductEntity oldProduct) throws InterruptedException
	{
	    String msg;	
	    ArrayList<ProductEntity> productToUpdateAr=new ArrayList<ProductEntity>();
	    productToUpdateAr.add(productToUpdate);
	    productToUpdateAr.add(oldProduct);
		MessageToSend mts=new MessageToSend(productToUpdateAr,"UpdateProduct");
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
}
