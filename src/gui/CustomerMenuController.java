package gui;
import  entities.ProductEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
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
	
	ListView<ProductEntity> List;
	/*private Label ProductNameLabel;
	private Label ProductIDLabel;
	private Label ProductPriceLabel;
	private Label ProductDescriptionLabel;
	private Label ProductTypeLabel;
	private Label ProductColorLabel;
	private Button AddToCartButton;*/
	private Button SearchItemButton;
	private TextField EnterProductname;
	private Button backFromcatalog;
	private Button CheckOut;
	
	
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
	
	public Client getClient() {
		return this.clnt;
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
		 Parent root = loader.load(getClass().getResource("/gui/CustomerOrderMenuBoundary.fxml").openStream());
		 CustomerOrderController ord = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		 ord.setConnectionData(this.clnt);
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
	
	//enter to catalog try 2
	public void enterCatalog(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		Stage primaryStage=new Stage();

		List=new ListView<ProductEntity>();
		List.setCellFactory(new Callback<ListView<ProductEntity>, ListCell<ProductEntity>>(){
			 
	            @Override
	            public ListCell<ProductEntity> call(ListView<ProductEntity> p) {
	                 
	                ListCell<ProductEntity> cell = new ListCell<ProductEntity>(){
	 
	                    @Override
	                    protected void updateItem(ProductEntity product, boolean status) {
	                        super.updateItem(product, status);
	                        if (product != null) {
	                            setText(product.getProductName()+"  "+product.getProductDescription()+"  " + "\n"+product.getProductPrice()+"¤");
	                            setGraphic(new ImageView(product.getProductImage()));
	                        }
	                    }
	                };
	                return cell;
	            }
	        });
	
		List.setMinWidth(500);
		List.setMinHeight(800);
		
		List.getItems().addAll(getProduct());
		backFromcatalog=new Button("Back");
		
		EnterProductname=new TextField();
		EnterProductname.setPromptText("Enter product name");
		EnterProductname.setOnAction(e->handelThesearch(EnterProductname));
		SearchItemButton=new Button("Search");
		SearchItemButton.setOnAction(e->searchButton());
		
		HBox hBox=new HBox();
		hBox.setPadding(new Insets(10,10,10,10));
		hBox.setSpacing(20);
		hBox.getChildren().addAll(backFromcatalog,EnterProductname,SearchItemButton);
		
		
		VBox Vbox1=new VBox();
		Vbox1.getChildren().addAll(List,hBox);
			
		// FXMLLoader loader = new FXMLLoader();
		 //Parent root = loader.load(getClass().getResource("/gui/CatalogBoundary.fxml").openStream());

		Scene sc=new Scene(Vbox1);
		sc.getStylesheets().add("/gui/LoginStyle.css");
		primaryStage.setTitle("Zer-Li Catalog");
		primaryStage.setScene(sc);
		
		backFromcatalog.setOnAction(e ->{
			try {
				back(primaryStage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	    primaryStage.show();	
	}
	
	public void back(Stage TheCurrentScene) throws IOException //Method that return to the customer menu from the catalog
	{
		TheCurrentScene.getScene().getWindow().hide();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
		
		Stage primaryS=new Stage();
		Scene scene=new Scene(root);
		
		primaryS.setTitle("Custumer manu");
		primaryS.setScene(scene);
		primaryS.show();
	}
	
	public void handelThesearch(TextField f)
	{
		System.out.println("Because im happy "+f.getText());
	}
	public void searchButton( )
	{
		System.out.println("Searching ");
	}
	
		/*public void AddToList()//Method that adds product to the catalog
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
		
		public void DeleteFromList()//Method that delet's product from catalog
		{
			ObservableList<ProductEntity> ProductSelected,AllProducts;
			AllProducts=table.getItems();
			ProductSelected=table.getSelectionModel().getSelectedItems();
			 ProductSelected.forEach(AllProducts::remove);
		}
*/
	    public ObservableList<ProductEntity> getProduct() throws FileNotFoundException//Method that creat's a list of products
	{   
		ObservableList<ProductEntity> products=FXCollections.observableArrayList();
		
//		products.add(new ProductEntity("123","lian","boquet",(double) 20,"bridal","blue", new ImageView(new Image(getClass().getResourceAsStream("/images/pic1.jpg"),(double)100,(double)100,true,true))));
//	    products.add(new ProductEntity("124","lili","boquet",(double) 15.60,"bridal","red",new ImageView(new Image(getClass().getResourceAsStream("/images/pic2.jpg"),(double)100,(double)100,true,true))));
//	    products.add(new ProductEntity("124","magic","boquet",(double) 80,"bridal","red",new ImageView(new Image(getClass().getResourceAsStream("/images/pic3.jpg"),(double)100,(double)100,true,true))));
//	    products.add(new ProductEntity("124","bird","boquet",(double) 96,"bridal","red",new ImageView(new Image(getClass().getResourceAsStream("/images/pic4.jpg"),(double)100,(double)100,true,true))));

	    
		products.add(new ProductEntity("123","lian","boquet",(double) 20,"bridal","blue", new Image(getClass().getResourceAsStream("/images/pic1.jpg"),(double)100,(double)100,true,true)));
	    products.add(new ProductEntity("124","lili","boquet",(double) 15.60,"bridal","red",new Image(getClass().getResourceAsStream("/images/pic2.jpg"),(double)100,(double)100,true,true)));
	    products.add(new ProductEntity("124","magic","boquet",(double) 80,"bridal","red",new Image(getClass().getResourceAsStream("/images/pic3.jpg"),(double)100,(double)100,true,true)));
	    products.add(new ProductEntity("124","bird","boquet",(double) 96,"bridal","red",new Image(getClass().getResourceAsStream("/images/pic4.jpg"),(double)100,(double)100,true,true)));

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
		}

		
}
