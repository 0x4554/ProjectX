package gui;
import java.io.IOException;
/**
 * 
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yaakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
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
import javafx.stage.Stage;

public class StoreWorkerMenuController implements Initializable{

    @FXML private Button ctlgBtn;
    @FXML private Button lgBtn;
    @FXML private Button srvBtn;
    private StoreWorkerMenuController swm;
    /**
     * Necessary constructor for the App
     */
	public StoreWorkerMenuController() {
		// TODO Auto-generated constructor stub
	}
	public void setConnectionData(StoreWorkerMenuController m) {
		this.swm=m;
	}

	
	public void showStoreWorkerMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreWorkerMenuBoundary.fxml").openStream());
		 
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		StoreWorkerMenuController swm = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window
		swm.setConnectionData(this);
		primaryStage.setTitle("Store worker main menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	
	/**
	 * when back button pressed
	 * go back to main menu
	 * @param event
	 * @throws IOException
	 */
	public void logOut(ActionEvent event) throws IOException	
	{
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
//		ConnectedClients.removeConnectedClient(Client.getClientConnection().getUsername());
		LoginController.signalLogOut();
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		GeneralMessageController.showMessage("Store worker "+Client.getClientConnection().getUsername()+" logged out");
		Client.getClientConnection().setClientUserName(null);
	}
	
	/**
	 * method to be called when store worker wishes to upload new survey
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void startNewSurvey(ActionEvent event) throws IOException {
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/SurveyBoundary.fxml").openStream());
		SurveyController sc=loader.getController();
		sc.setConnectionData(this);
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("New Survey");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
public void PromoteSale(ActionEvent event) throws IOException, InterruptedException {
		
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/StoreWorkerPromoteSale.fxml").openStream());
		StoreWorkerPromoteSaleController sc=loader.getController();
		//sc.setConnectionData(this);
		sc.ShowAllProduct();
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Promote Sale");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
