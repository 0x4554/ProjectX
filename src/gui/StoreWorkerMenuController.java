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

	@FXML private Button cmplnBtn;
    @FXML private Button cmplnDetBtn;
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
		primaryStage.setTitle("Manager's main menu");
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
		LoginController.signalAppClose();
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/LoginBoundary.fxml").openStream());
		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
		GeneralMessageController.showMessage("Manager "+Client.getClientConnection().getUsername()+" logged out");
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
