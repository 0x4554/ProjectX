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
	private Button SearchItemButton;
	private TextField EnterProductname;
	private Button backFromcatalog;
	private Button CheckOut;
	private Client clnt;
	
	/**
	 * This method is the constructor for this class
	 * @param lc	the login controller
	 */
	public CustomerMenuController(LoginController lc)
	{
		this.logcon=lc;		
	}
	/**
	 * A necessary constructor for the App
	 */
	public CustomerMenuController()
	{
		
	}
	
	public Client getClient() {
		return Client.getClientConnection();
	}
	
	public void showCustomerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CustomerMenuBoundary.fxml").openStream());
		//CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		CustomerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		cmc.setConnectionData(this.clnt);
		primaryStage.setTitle("Customer's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	private void setConnectionData(Client clnt2) {
		// TODO Auto-generated method stub
		this.clnt=clnt2;
	}////////////////////////////////////////////////////////
	
	//*Open order menu from customer main menu*//
	public void enterToOrder(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("/gui/CustomerOrderMenuBoundary.fxml").openStream());
		 CustomerOrderController ord = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		ord.setConnectionData(this);
		 Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	//Enter to catalog
	public void enterCatalog(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		CatalogController catlg=new CatalogController();
		catlg.setConnectionData(this);
		try {
			catlg.addProductsToDB(null);
			//catlg.showProductcatalog();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    //*Open  Account details  menu from customer main menu*//
		public void enterToAccount(ActionEvent event) throws IOException {
			 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			 FXMLLoader loader = new FXMLLoader();
			 Parent root = loader.load(getClass().getResource("/gui/AccountDetailsBoundary.fxml").openStream());
			 AccountDetailsController adc= loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
			 adc.setConnectionData(this);
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
				//	 UpdateAccountController upac= loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
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
					/*((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					ConnectedClients.removeConnectedClient(Client.getClientConnection().getUsername());
					GeneralMessageController.showMessage("Bye Bye "+Client.getClientConnection().getUsername()+" we hope to see you soon");
					//////////////////Check returning to login page//////////////////////////*/
					
					((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
					LoginController.signalAppClose();
					
					FXMLLoader loader = new FXMLLoader();
					Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
					Stage primaryStage=new Stage();
					Scene scene=new Scene(root);
					
					primaryStage.setTitle("Login");
					primaryStage.setScene(scene);
					primaryStage.show();
					
					GeneralMessageController.showMessage("Bye Bye "+Client.getClientConnection().getUsername()+" we hope to see you soon");
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
