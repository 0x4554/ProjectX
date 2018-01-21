package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.ChainWorkerEntity;
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

public class ChainWorkerMenuController implements Initializable{

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
	
	Stage primaryStage=new Stage();
	private Object clnt;
	
	
	public void showChainWorkerMenu() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/ChainWorkerMenuBoudary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
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
			((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
			//CreateNewOrderController cnoc = loader.getController(); 
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	public void addProduct()
	{
		
	}
	public void deleteProduct()
	{
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
