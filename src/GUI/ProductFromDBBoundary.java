package GUI;

import java.io.IOException;
import java.util.ArrayList;

///
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
///
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProductFromDBBoundary {
	
	@FXML private Label rsltIDLbl;
	@FXML private Label rsltNmLbl;
	@FXML private Label rsltTpLbl;
	@FXML private Button srchagnBtn;
	
	public void showProductDetails(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window		
		Stage primaryStage=new Stage();
		Parent root= FXMLLoader.load(getClass().getResource("ProductFromDBGUI.fxml"));
		Scene scene=new Scene(root);
		primaryStage.setTitle("Product Details");
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	
	public void searchAgain(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		Stage primaryStage=new Stage();
		Parent root= FXMLLoader.load(getClass().getResource("SeaerchProductGUI.fxml"));
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search Product");
		primaryStage.setScene(scene);
		primaryStage.show();	
	}


}
