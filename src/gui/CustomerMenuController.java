package gui;
import  entities.ProductEntity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import client.Client;
import javafx.application.Application.Parameters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.ConnectedClients;
import sun.tools.jar.Main;
import java.awt.image.BufferedImage;


public class CustomerMenuController implements Initializable{
	
	final public static int DEFAULT_PORT = 5555;
	//@FXML private AnchorPane root;
	
	private Parameters params;
	
	
	private Client clnt;
	private LoginController logcon;
	
	//*buttons of the customer menu*//
	@FXML private Label mmlbl;
	@FXML private Button ordBtn;
	@FXML private Button updeatilsBtn;
	@FXML private Button viewcatBtn;
	@FXML private Button watchaccBtn;
	@FXML private Button cmplntBtn;
	@FXML private Button logoutBtn;
	
	/**
	 * This variables are connected to the catalog class
	 */
	TableView<ProductEntity> table;
	private TextField productID;
	private TextField productName;
	private TextField productType;
	private TextField productPrice;
	private TextField productDescription;
	private TextField productColor;
	private Button addButton;
	private Button deleteButton;
	private Button editButton;
	
	/**
	 * This method is the constructor for this class
	 * @param clnt	the connected client
	 */
	public CustomerMenuController(Client clnt,LoginController lc)
	{
		this.clnt=clnt;
		this.logcon=lc;		
	}
	
	/**
	 * A necessary constructor for the App
	 */
	public CustomerMenuController()
	{
		
	}
	/**
	 * This method saves the client connection to the controller
	 * @param clnt	the connection client
	 */
	
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	
	
	
	public void showCustomerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		cmc.setConnectionData(this.clnt);
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	//*Open order menu from customer main menu*//
	public void enterToOrder(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/OrderMenuBoundary.fxml").openStream());
		 OrderController ord = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
	//	 ord.setConnectionData(DEFAULT_PORT, this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	//*Open  catalog  menu from customer main menu*//
	/*public void enterCatalog(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/CatalogBoundary.fxml").openStream());
		CatalogController catlg = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window//i changed this line -lana
		catlg.setConnectionData(this);
	//	 ord.setConnectionData(DEFAULT_PORT, this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();
	}	*/		
	
	
	//*Open  catalog  menu from customer main menu*//
		public void enterCatalog(ActionEvent event) throws IOException {
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			//TableView<ProductEntity> table;
			
			//**adding the columns to the table**//
			TableColumn<ProductEntity,String> IDcolumn=new TableColumn<>("ProductID");
			IDcolumn.setMaxWidth(200);
			IDcolumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
			
			TableColumn<ProductEntity,String> namecolumn=new TableColumn<>("ProductName");
			namecolumn.setMaxWidth(200);
			namecolumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
			
			TableColumn<ProductEntity,String> typecolumn=new TableColumn<>("productType");
			typecolumn.setMaxWidth(200);
			typecolumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
			
			TableColumn<ProductEntity,Double> pricecolumn=new TableColumn<>("productPrice");
			pricecolumn.setMaxWidth(200);
			pricecolumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
			
			TableColumn<ProductEntity,String> descriptioncolumn1=new TableColumn<>("productDescription");
			descriptioncolumn1.setMaxWidth(200);
			descriptioncolumn1.setCellValueFactory(new PropertyValueFactory<ProductEntity,String>("productDescription"));
			
			TableColumn<ProductEntity,String> colorcolumn=new TableColumn<>("productDominantColor");
			colorcolumn.setMaxWidth(200);
			colorcolumn.setCellValueFactory(new PropertyValueFactory<>("productDominantColor"));
			
			//TableColumn<ProductEntity,ImageView> imagecolumn=new TableColumn<>("productImageView");
			TableColumn<ProductEntity,String> imagecolumn=new TableColumn<>("productImageView");
			imagecolumn.setMaxWidth(500);
			imagecolumn.setCellValueFactory(new PropertyValueFactory<>("productImageView"));
			
			TableCell<ProductEntity, Image> cell = new TableCell<ProductEntity,Image>(); 

			
			
			productID=new TextField();
			productID.setPromptText("Enter Id");
			productID.setMinWidth(110);
			
			productName=new TextField();
			productName.setPromptText("Enter Name");
			productName.setMinWidth(110);
			
			productType=new TextField();
			productType.setPromptText("Enter Type");
			productType.setMinWidth(250);
			
			productPrice=new TextField();
			productPrice.setPromptText("Enter Price");
			productPrice.setMinWidth(250);
			
			productDescription=new TextField();
			productDescription.setPromptText("Enter Description");
			productDescription.setMinWidth(250);
			
			productColor=new TextField();
			productColor.setPromptText("Enter dominant color");
			productColor.setMinWidth(150);
			
			Button addButton=new Button("Add");
			addButton.setOnAction(e->AddToList());
			Button deleteButton=new Button("Delete");
			deleteButton.setOnAction(e->DeleteFromList());
			Button EditButton=new Button("Edit");
		//	EditButton.setOnAction(e->EditList());
			
			HBox hBox=new HBox();
			hBox.setPadding(new Insets(10,10,10,10));
			hBox.setSpacing(10);
			hBox.getChildren().addAll(productID,productName,productType,productPrice,productDescription,productColor,addButton,deleteButton,EditButton);
			
			table=new TableView<>();
			//table.setItems(getProduct());
	    	table.getColumns().addAll(IDcolumn,namecolumn, typecolumn, pricecolumn,descriptioncolumn1,colorcolumn, imagecolumn);
			table.setItems(getProduct());

			
			VBox Vbox1=new VBox();
			Vbox1.getChildren().addAll(table,hBox);
			
			Stage primaryStage=new Stage();
			Scene sc=new Scene(Vbox1);
			primaryStage.setTitle("Zer-Li Catalog");
			primaryStage.setScene(sc);
		    primaryStage.show();
			
			 
			/* CatalogController catlg=new CatalogController();
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CatalogBoundary.fxml"));
			loader.setController(catlg);
			catlg.setConnectionData(this);
			Parent root = loader.load();
		
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			primaryStage.setTitle("Catalog");
			primaryStage.setScene(scene);
			primaryStage.show();
			*/
			
		}			
		public void AddToList()
		{
			ProductEntity product=new ProductEntity();
			product.setProductID(productID.getText());
			product.setProductName(productName.getText());
			product.setProductType(productType.getText());
			product.setProductPrice(Double.parseDouble(productPrice.getText()));
			product.setProductDescription(productDescription.getText());
			product.setProductDominantColor(productColor.getText());
			this.table.getItems().add(product);
			productID.clear();
			productName.clear();
			productType.clear();
			productPrice.clear();
			productDescription.clear();
			productColor.clear();
		}
		
		public void DeleteFromList()
		{
			ObservableList<ProductEntity> ProductSelected,AllProducts;
			AllProducts=table.getItems();
			ProductSelected=table.getSelectionModel().getSelectedItems();
			 ProductSelected.forEach(AllProducts::remove);
		}

	public ObservableList<ProductEntity> getProduct()
	{   
		/*public void initialize(URL url, ResourceBundle rb) {
		    KiwiImageCol.setCellFactory(param -> {
		       //Set up the ImageView
		       final ImageView imageview = new ImageView();
		       imageview.setFitHeight(50);
		       imageview.setFitWidth(50);

		       //Set up the Table
		       TableCell<NewBeautifulKiwi, Image> cell = new TableCell<NewBeautifulKiwi, Image>() {
		           public void updateItem(Image item, boolean empty) {
		             if (item != null) {
		                  imageview.setImage(item);
		             }
		           }
		        };
		        // Attach the imageview to the cell
		        cell.setGraphic(imageview);
		        return cell;
		   });
		   KiwiImageCol.setCellValueFactory(new PropertyValueFactory<NewBeautifulKiwi, Image>("kiwiImage"));
		
		*/
		
		
		ObservableList<ProductEntity> products=FXCollections.observableArrayList();
		ImageView im=new ImageView((new Image(getClass().getResourceAsStream("/gui/pic1.jpg"),(double)100,(double)100,true,true)));
	//	if(im==null)System.out.println("its null");
		products.add(new ProductEntity("123","lian","boquet",(double) 20,"bridal","blue",im));
	//	products.add(new ProductEntity("124","lili","boquet",(double) 15,"bridal","red"));
		return products;
	}
	
	
	//*Open  Account details  menu from customer main menu*//
		public void enterToAccount(ActionEvent event) throws IOException {
			 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			 FXMLLoader loader = new FXMLLoader();
			 Parent root = loader.load(getClass().getResource("/gui/AccountDetailsBoundary.fxml").openStream());
			 AccountController acc= loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		//	 ord.setConnectionData(DEFAULT_PORT, this);
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			primaryStage.setTitle("Account details");
			primaryStage.setScene(scene);
			primaryStage.show();
		}			
		
		//*Open  Update details Window from customer main menu*//
				public void enterToUpdateDetails(ActionEvent event) throws IOException {
					 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					 FXMLLoader loader = new FXMLLoader();
					 Parent root = loader.load(getClass().getResource("/gui/UpdateAccountBoundary.fxml").openStream());
					 UpdateAccountController upac= loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
				//	 ord.setConnectionData(DEFAULT_PORT, this);
					Stage primaryStage=new Stage();
					Scene scene=new Scene(root);
					primaryStage.setTitle("Details");
					primaryStage.setScene(scene);
					primaryStage.show();
				}			
		
				/**
				 * this method sends you back to the previous window
				 * 
				 * 
				 * @param event
				 * @throws IOException
				 */
				public void logOutCustomer(ActionEvent event) throws IOException	//when click "Back" return to main menu
				{
					((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					ConnectedClients.removeConnectedClient(this.clnt.getUsername());
					GeneralMessageController.showMessage("Bye Bye "+this.clnt.getUsername()+" we hope to see you soon");
					//////////////////Check returning to login page//////////////////////////
				}
				
				
				public void startComplaint(ActionEvent event) throws IOException {
					((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					 FXMLLoader loader = new FXMLLoader();
					 Parent root = loader.load(getClass().getResource("/gui/ComplaintBoundary.fxml").openStream());
					 ComplaintController cmpc= loader.getController();	//set the controller to the ComplaintBoundary to control the SearchProductGUI window
					 cmpc.setConnectionData(this);
					Stage primaryStage=new Stage();
					Scene scene=new Scene(root);
					primaryStage.setTitle("Complaint");
					primaryStage.setScene(scene);
					primaryStage.show();
				}

				
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
		/*	products=FXCollections.observableArrayList(new ProductEntity("123","lian","boquet",(double) 20,"bridal","blue",new ImageView("images/pic1.jpg")));
			
			IDcolumn.setCellValueFactory(new PropertyValueFactory<ProductEntity,String>("productID"));
			namecolumn.setCellValueFactory(new PropertyValueFactory<ProductEntity,String>("productName"));
			typecolumn.setCellValueFactory(new PropertyValueFactory<ProductEntity,String>("productType"));
			pricecolumn.setCellValueFactory(new PropertyValueFactory<ProductEntity,Double>("productPrice"));
			descriptioncolumn1.setCellValueFactory(new PropertyValueFactory<ProductEntity,String>("productDescription"));
			colorcolumn.setCellValueFactory(new PropertyValueFactory<ProductEntity,String>("productDominantColor"));
			imagecolumn.setCellValueFactory(new PropertyValueFactory<ProductEntity,ImageView>("productImageView"));
			tbl.setItems(products);*/
			
		}

		
}
