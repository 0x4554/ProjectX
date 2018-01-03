package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class GeneralMessageController implements Initializable {

	///general message
	@FXML private Label generaMsglLabel;
	@FXML private Button generalMsgButton;
		
	public void showGeneralMessage(String msg) throws IOException
	{
		Stage secondaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/GeneralMessageBoundary.fxml").openStream());	//load the general message page
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/gui/GeneralMessageStyle.css");
		GeneralMessageController gmb = loader.getController();
		gmb.setLabel(msg);	//set the label for specific message
			
		secondaryStage.setTitle("Message");
		secondaryStage.setScene(scene);
		secondaryStage.show();
	}
	
	/**
	 * This method creates a general message to display to the UI
	 * @param message the message to be displayed to the UI
	 * @throws IOException	for the FXMLLoader in GeneralMessageController
	 */
	public static void showMessage(String message) throws IOException
	{
		GeneralMessageController msg = new GeneralMessageController();
		msg.showGeneralMessage(message);
	}
	
	public void setLabel(String msg)	//set the label for the specific message
	{
		this.generaMsglLabel.setText(msg);
	}
	public void hideGeneralMessage(ActionEvent event)	//for hiding a general message
	{
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
