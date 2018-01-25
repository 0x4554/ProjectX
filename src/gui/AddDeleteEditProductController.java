package gui;

import java.io.ByteArrayInputStream;
import java.io.File;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.MessageToSend;

/**
 * This class is the controller for the add/delete/edit product from DB
 * 
 * AddDeleteEditProductController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class AddDeleteEditProductController implements Initializable {

	/*FXML*/
	@FXML private Label Namelbl;
	@FXML private Label Typelbl;
	@FXML private Label Pricelbl;
	@FXML private Label Descriptionbl;
	@FXML private Label Imagebl;
	@FXML private Label Colorlbl;

	@FXML private Button BackBtn;
	@FXML  private Button UploudBtn;
	@FXML  private Button addBtn;
	@FXML private Button deleteBtn;
	@FXML private Button getDetailsBtn;
	@FXML private Button UpdateBtn;
	@FXML private Button clearBtn;
	@FXML private Button clearSelectionBtn;
	
	@FXML private TextField NameTxt;
	@FXML private TextField TypeTxt;
	@FXML private TextField PriceTxt;
	@FXML private TextField DescriptionTxt;
	@FXML private TextField ImageTxt;
	@FXML private TextField IDTxt;
	
	@FXML private ListView<ProductEntity> list;
	@FXML private ComboBox<String> typeCmb=new ComboBox<String>();
	 
	@FXML private ComboBox<String> dmntClrCmb;
	
	
	public ObservableList<String> Type_list=FXCollections.observableArrayList();
    ArrayList<ProductEntity> productsFromTable = new ArrayList<ProductEntity>(); //array list of products from the data base
    public ObservableList<String> color_list=FXCollections.observableArrayList();
	private ArrayList<String> dominantColors; //array list of colors
	
	
	/*Constructor*/
	public AddDeleteEditProductController()
	{
		
	}
	/**
	 * The method clears the selections made in the list view
	 * @param event
	 */
	public void ClearSelections(ActionEvent event)
	   {
		   list.getSelectionModel().clearSelection();
	   }
	
	
	/**
	 * The method update's the chosen product
	 * @param event "Update Product" button clicked
	 * @throws IOException for the loader
	 * @throws InterruptedException for thread sleep
	 */
public 	void UpdateProduct(ActionEvent event) throws IOException, InterruptedException
{
	ProductEntity product=new ProductEntity();
	ProductEntity OldProduct=new ProductEntity();
	int flag=1;
	if(list.getSelectionModel().getSelectedItem()!=null) //if product was selected from the list view
	{
	    OldProduct=list.getSelectionModel().getSelectedItem();
	    
	    /*check the validation of the text fields that the user updated*/
		if(!NameTxt.getText().equals(""))//check name
		product.setProductName(NameTxt.getText());
		else {
			GeneralMessageController.showMessage("Please Enter All Fields");
			return;
			}
		
		if(!typeCmb.getSelectionModel().isEmpty())//check type
			product.setProductType(typeCmb.getSelectionModel().getSelectedItem());
		else {
			GeneralMessageController.showMessage("Please Enter All Fields");	
			return;
		}
		
		if(!(PriceTxt.getText().equals("")))//check price
		{	try//check if characters were not entered
			{
				 Double price=Double.parseDouble(PriceTxt.getText());
					if(price>0 && price <500)
					       product.setProductPrice(price);
					else
						{
						GeneralMessageController.showMessage("Product price is out of limits");
			            return;
						}
				 
		}
		catch(NumberFormatException e)
		{
			GeneralMessageController.showMessage("Incorrect price");
		}

		}
		else return;
		
		if(!(DescriptionTxt.getText().equals("")))//check description
		product.setProductDescription(DescriptionTxt.getText());
		else {
			product.setProductDescription("");
		}
		
		if(!(ImageTxt.getText().equals("")))//check image(if the user did not enter new image on update , keep the old image)
		{
			product.setProductImage(ImageTxt.getText());	
		}
		
		if(!this.dmntClrCmb.getSelectionModel().isEmpty())//check the color
			product.setProductDominantColor(dmntClrCmb.getSelectionModel().getSelectedItem());
		else {
			GeneralMessageController.showMessage("Please Enter All Fields");
			return;
		}
	}else
		{
		flag=0;
		GeneralMessageController.showMessage("Please choose product to update");
		return;
		}
	
	if(flag==1)//product to update was chosen
	{
	if(UpdateProductInDB(product,OldProduct).equals("Success"))//update product in data base
	{
		ShowAllProduct();//show all products
		GeneralMessageController.showMessage("Product was Updated successfully");
		/*clear fields*/
		NameTxt.clear();
		PriceTxt.clear();
		DescriptionTxt.clear();
		ImageTxt.clear();
		IDTxt.clear();
		typeCmb.setValue("");
		dmntClrCmb.setValue("");
	}
	else 
	{
		GeneralMessageController.showMessage("Update failed");
		/*clear fields*/
		NameTxt.clear();
		PriceTxt.clear();
		DescriptionTxt.clear();
		ImageTxt.clear();
		IDTxt.clear();
		typeCmb.setValue("");
		dmntClrCmb.setValue("");
		return;
	}
	
	}else 
		{
		GeneralMessageController.showMessage("Please choose product to update");
	    return;
	}
}
	/**
	 * The method sets the details of chosen product from the list view
	 * @param event click on a cell in the list view
	 * @throws IOException
	 */
public void SetProductDetails(MouseEvent event) throws IOException
{
	ProductEntity product=new ProductEntity();
	if(list.getSelectionModel().getSelectedItem() != null)
	{
		/*set details in the text fields*/
		product=list.getSelectionModel().getSelectedItem();
		IDTxt.setText(product.getProductID().toString());
		NameTxt.setText(product.getProductName());
		typeCmb.setValue(product.getProductType());
		PriceTxt.setText(product.getProductPrice().toString());
		DescriptionTxt.setText(product.getProductDescription());
		dmntClrCmb.setValue(product.getProductDominantColor());
	}
	else
	{
		GeneralMessageController.showMessage("Please choose product from the list");
		return;
	}
}
	/**
	 * The method deletes a product from the products list 
	 * @param event click on "Delete" button
	 * @throws InterruptedException for thread sleep
	 * @throws IOException for the loader
	 */
public void DeleteProduct(ActionEvent event) throws InterruptedException, IOException 
{
	 ObservableList<ProductEntity> Products=FXCollections.observableArrayList();
	 ArrayList<ProductEntity> ProductsToDelete=new ArrayList<ProductEntity>(); //array list of products to delete
	 
	if(!(list.getSelectionModel().getSelectedItems().isEmpty()))//if user choose products to delete from the list view ,get the products
	{
		Products=list.getSelectionModel().getSelectedItems();
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
		if(DeleteProductFromDB(ProductsToDelete.get(i)).equals("Success"))
		{
			if(DeleteProductFromCatalogDB(ProductsToDelete.get(i)).equals("Deleted"))
				GeneralMessageController.showMessage("Product : "+ProductsToDelete.get(i).getProductName()+"\nwas deleted succsessfuly");
		}
		else 
		{
			   GeneralMessageController.showMessage("Product : "+ProductsToDelete.get(i).getProductName()+"\nwas not deleted");
		}
	}
	ShowAllProduct();
}

public void AddProduct(ActionEvent event) throws IOException, InterruptedException
{
	ProductEntity product=new ProductEntity();

			/*****************set product name****************/
	if(!(NameTxt.getText().equals("")) )//text is not empty
	{
			product.setProductName(NameTxt.getText());
	}
	else 
		{
		GeneralMessageController.showMessage("Please Enter All Fields");
		return;
		}		
		if(!(typeCmb.getSelectionModel().isEmpty()))//selection has been made
		{
			product.setProductType((String)(typeCmb.getSelectionModel().getSelectedItem()));
		}
		else {
			GeneralMessageController.showMessage("Please Enter All Fields");
			return;
		}
		if (!(PriceTxt.getText().equals("")))//price fiel is not empty
		{
			try
			{
				Double price = Double.parseDouble(PriceTxt.getText());
				if (price > 0 && price < 500)
					product.setProductPrice(price);
				else
				{
					GeneralMessageController.showMessage("Product price is out of limits");
					return;
				}
			} catch (NumberFormatException e)
			{
				GeneralMessageController.showMessage("Incorrect price");
				return;
			}
		}
		else
		{
			GeneralMessageController.showMessage("Please Enter All Fields");
			return;
		}
		
		if(!(DescriptionTxt.getText().equals(""))) {// description may be an optional so i will think about it
			product.setProductDescription(DescriptionTxt.getText());
		}
		else product.setProductDescription("");

		if(!dmntClrCmb.getSelectionModel().isEmpty())
		{
			product.setProductDominantColor(dmntClrCmb.getSelectionModel().getSelectedItem());
		}
		
		else 
		{
			GeneralMessageController.showMessage("Please Enter All Fields");
			return;
		}
		
		if(!(ImageTxt.getText().equals("")))
		{
	    product.setProductImage(ImageTxt.getText());
		}
		
		else {
			GeneralMessageController.showMessage("Please Enter All Fields");
			return;
		}
		
		//Insert to data base
		if(addProductsToDB(product).equals("Failed"))
		{
			GeneralMessageController.showMessage("Inserting product failed");
		}
		
		else
			{
			ShowAllProduct();
			NameTxt.clear();
			PriceTxt.clear();
			typeCmb.setValue("");
			DescriptionTxt.clear();
			ImageTxt.clear();
			dmntClrCmb.setValue("");
			GeneralMessageController.showMessage("Product :"+product.getProductName()+"  ID:  "+product.getProductID()+"\nwas added successfully");
			}
}

/**
 * This method clears all the fields 
 * @param event pressed clear all fields
 */
public void ClearFields(ActionEvent event)
{
	IDTxt.clear();
	NameTxt.clear();
	typeCmb.setValue("");
	PriceTxt.clear();
	DescriptionTxt.clear();
	dmntClrCmb.setValue("");
}

/**
 * This method is used for searching for a file to add 
 * @param event	pressed add image
 * @throws IOException	for the file chooser
 */
public void searchForPhoto(ActionEvent event) throws IOException{
		
		Stage secondaryStage=new Stage();
		Node node = (Node) event.getSource();
		FileChooser chooser = new FileChooser();
	    chooser.setTitle("Choose File");
	    try {
	    File f=chooser.showOpenDialog(secondaryStage);
	    String filepath = f.getAbsolutePath();
	    ImageTxt.setText(filepath);
	    }
	    catch(Exception e) {
	    return;
	    }
	}

/**
 * This method loads and shows all the products in the DB
 * @throws InterruptedException for the thread
 */
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
                    	if(product.getProductDominantColor().equals("none"))
                    	{
                            setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+",\n        price:  "+product.getProductPrice()+"¤");
                    	}
                    	else
                    		setText("        "+product.getProductName()+"  is a  "+product.getProductType()+",  \n        "+product.getProductDescription()+", in  "+product.getProductDominantColor()+"  color's  "+"  " + "\n        price:  "+product.getProductPrice()+"¤");
                        setFont(Font.font(18));
                    }
                }
            };
            return cell;
        }
    });
list.setItems(Products);//set the items to the ListView
}

/**
 * This method loads the previous window
 * @param event pressed back
 * @throws IOException	for the loader
 */
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



/**************************Data Base Method's ***************************************************************/

/**
 * This method sends a message to the server for updating a product
 * @param productToUpdate	The newly updated product
 * @param oldProduct		The old product
 * @return	the message from the server
 * @throws InterruptedException for the Thread sleep
 */
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

/**
 * This method sends a message to the server to delete a product from the Catalog table
 * @param productToDelete	the product to delete
 * @return	the message for the server
 * @throws InterruptedException	for the Thread.sleep
 */
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

/**
 * This method sends a message to the server to add a new product
 * @param product the new product to add
 * @return	the message from the server
 * @throws InterruptedException	for the Thread sleep
 * @throws IOException	
 */
public String addProductsToDB(ProductEntity product) throws InterruptedException, IOException {
	String msg;	
	
	MessageToSend mts=new MessageToSend(product,"createProduct");
	Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
	Client.getClientConnection().accept();										//sends to server
	while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
		Thread.sleep(100);
	Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
	MessageToSend m = Client.getClientConnection().getMessageFromServer();
	 msg = (String)m.getMessage();
	return msg;
}	

/**
 * This method gets all the products from the DB
 * @return	arrayList of Products
 * @throws InterruptedException
 */
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
 * This method sends a message to the server to delete a product from the Product table
 * @param productToDelete	the product to delete
 * @return	the message from the server
 * @throws InterruptedException
 */
public String DeleteProductFromDB(ProductEntity productToDelete) throws InterruptedException
{
		String msg;
		MessageToSend mts=new MessageToSend(productToDelete,"deleteProduct");
		Client.getClientConnection().setDataFromUI(mts);					//set the data and the operation to send from the client to the server
		Client.getClientConnection().accept();										//sends to server
		while(!Client.getClientConnection().getConfirmationFromServer())			//wait until server replies
			Thread.sleep(100);
		Client.getClientConnection().setConfirmationFromServer();		//reset confirmation to false
		MessageToSend m = Client.getClientConnection().getMessageFromServer();
		 msg = (String)m.getMessage();
		return msg;
}

/**
 * For initializing the FXML window
 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Type_list.add("Bridal");
		Type_list.add("Flower Boquet");
		Type_list.add("Special");
		typeCmb.setItems(Type_list);
		
		this.dominantColors = new ArrayList<String>();		//set  the dominant colors
		this.dominantColors.add("none");
		this.dominantColors.add("Blue");
		this.dominantColors.add("Red");
		this.dominantColors.add("White");
		this.dominantColors.add("Yellow");
		this.dominantColors.add("Purple");
		this.dominantColors.add("Green");
		this.dominantColors.add("Orange");
		this.dominantColors.add("Pink");
		this.dominantColors.add("Gold");
		
		this.color_list.setAll(this.dominantColors);
		this.dmntClrCmb.setItems(color_list);
	}

}
