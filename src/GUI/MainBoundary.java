package GUI;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainBoundary extends Application {
	
	final public static int DEFAULT_PORT = 5555;
	
	private Parameters params;
	private Stage primaryStage;

	
	@FXML private TextField srchIDfld;
	@FXML private AnchorPane root;
	@FXML private Label srchLbl;
	@FXML private Label prdLbl;
	@FXML private Button srchProdBtn;
	
	
	@FXML private Button okerrBtn;
	@FXML private Label errMsgLbl;
	@FXML private Label errLbl;
	
//	
	private static String host;
	private CreateProductBoundary cpd;
	
	public void setHost(String parameters) {	//set the host's IP to static String
		MainBoundary.host=parameters;
	}
	
	public static String getHost()	//static method for holding the host's IP (accessible to all classes using static String)
	{
		return MainBoundary.host;
	}

	
	public void hideError(ActionEvent event) {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	}
	
	
	
	public void searchProduct(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		 FXMLLoader loader = new FXMLLoader();
		 Parent root = loader.load(getClass().getResource("SearchProductGUI.fxml").openStream());
		 FindProductBoundary fnd = loader.getController();
		 fnd.setConnectionData(DEFAULT_PORT, this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}			
	
	

	public void showNewProductGUI(ActionEvent event) throws IOException {	//when click the add new product in main menu
		
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		cpd=new CreateProductBoundary(DEFAULT_PORT,this);
		cpd.showNewProductGUI();	//call method to show the add new product menu
	}

	
	public void showMainMenu(ActionEvent event) throws IOException	//show the main menu
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		Stage primaryStage=new Stage();
		Parent root= FXMLLoader.load(getClass().getResource("MenuGUI.fxml"));
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		params=getParameters();
		List<String> lst=params.getRaw();
		 try
		    {
			 this.setHost(lst.get(0));		//set the host IP from command prompt input  
			 }
		    catch(IndexOutOfBoundsException e)
		    {
		      MainBoundary.host = "localhost";						//set the host IP to localhost
		    }			
		
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("MenuGUI.fxml").openStream());
//		MainBoundary mainB = loader.getController();
		
		Scene scene=new Scene(root);
		
		this.primaryStage.setTitle("Main Menu");
		this.primaryStage.setScene(scene);
		this.primaryStage.show();
	}
	
	
	
	/**
	 * Main method, getting host from command prompt or choosing the default host
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
	    launch(args);
	}		

}
