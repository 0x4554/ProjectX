package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

///
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
///
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProductFromDBBoundary implements Initializable{
	
	@FXML private Label rsltIDLbl;
	@FXML private Label rsltNmLbl;
	@FXML private Label rsltTpLbl;
	@FXML private Button srchagnBtn;
	
	private ArrayList<String>results=new ArrayList<String>();
	
	public void setData(ArrayList<String>str) {
		for(String s:str)
			results.add(s);
	}
	
	public void showProductDetails() throws IOException {
			
		Stage primaryStage=new Stage();
		Parent root= FXMLLoader.load(getClass().getResource("ProductFromDBGUI.fxml"));
		Scene scene=new Scene(root);
		primaryStage.setTitle("Product Details");
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	void setLabels(ArrayList<String>res)
	{
		this.rsltIDLbl.setText(res.get(0));
		this.rsltNmLbl.setText(res.get(1));
		this.rsltTpLbl.setText(res.get(2));
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


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		setLabels(this.results);
	}


}
