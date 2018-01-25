package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ChainWorkerMenuController extends MenuController implements Initializable{

	@FXML private Button backBtn;
	@FXML private Button addToCatalogBtn;
	@FXML private Button deleteFromCatalogBtn;
	@FXML private Button promoteSaleBtn;
	@FXML private Button addProductBtn;
	@FXML private Button deleteProductBtn;
	@FXML private Button editProductBtn;
	@FXML private Label ChainWorkMenulbl;
	@FXML private Label cataloglbl;
	@FXML private Label productlbl;
	@FXML private Button vwCtlgBtn;
	
	Stage primaryStage=new Stage();
	private Object clnt;
	
	/**
	 * This method loads the chain store worker menu
	 * @throws IOException for the loader
	 */
	public void showMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainWorkerMenuBoudary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		ChainWorkerMenuController cmc = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		primaryStage.setTitle("Chain worker's main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method return to login window
	 * @param event
	 * @throws IOException 
	 */
	public void back(ActionEvent event) throws IOException
	{
		    LoginController.signalAppClose();
			((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/gui/LoginStyle.css");

			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.show();
			GeneralMessageController.showMessage("Bye Bye "+Client.getClientConnection().getUsername()+" we hope to see you soon");

	}
	
	/**
	 * This method loads the add/delete/edit product boundary
	 * @param event	pressed add delete edit product
	 * @throws IOException	for the loader
	 * @throws InterruptedException
	 */
	public void AddDeletEditProduct(ActionEvent event) throws IOException, InterruptedException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/AddDeleteEditProductBoundary.fxml").openStream());
		AddDeleteEditProductController edit=loader.getController();
		edit.ShowAllProduct();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Edit Product's");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * This method loads the edit catalog boundary
	 * @param event	pressed edit catalog
	 * @throws IOException	for the loader
	 * @throws InterruptedException
	 */
	public void AddDeletCatalog(ActionEvent event) throws IOException, InterruptedException
	{
		((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/AddDeleteCatalogBoundary.fxml").openStream());
		AddDeleteCatalogController edit=loader.getController();
		edit.Show();
		Stage primaryStage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Edit Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * The method open's the Catalog Window
	 * 
	 * @param event
	 *            on clicking "View Catalog" Button
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void enterCatalog(ActionEvent event) throws IOException, InterruptedException {
		((Node) event.getSource()).getScene().getWindow().hide(); //Hide Current window
		FXMLLoader loader = new FXMLLoader();
		Parent pRoot = loader.load(getClass().getResource("/gui/ViewCatalogBoundary.fxml").openStream()); //Load the fxml class
		CatalogController catl = loader.getController();
		catl.setPreviousController(this);
		catl.showCatalog(); //Call the method show catalog
		Stage primaryStage = new Stage(); //Set Stage->Show()
		Scene scene = new Scene(pRoot);
		scene.getStylesheets().add("/gui/LoginStyle.css");

		primaryStage.setTitle("Zer-Li Catalog");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
