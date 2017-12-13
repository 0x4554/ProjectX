package GUI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import client.User;
//import gui.StudentFormController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import logic.Test;
import javafx.scene.Node;

import ocsf.client.*;

import javafx.*;

public class UserBoundary extends Application {
	
	final public static int DEFAULT_PORT = 5555;
	
	private String id="";
//	private User usr;
	
	@FXML private TextField srchIDfld;
	@FXML private AnchorPane root;
	@FXML private Label srchLbl;
	@FXML private Label prdLbl;
	@FXML private Button srchProd;
	
	@FXML private Label prdIDLbl;
	@FXML private Label prdNmLbl;
	@FXML private Label prdTpLbl;
	@FXML private Button srchagnBtn;
	
	@FXML
	public TextField txtfldID;
	@FXML
	public TextField txtfldNm;
	@FXML
	public TextField txtfldTp;
	
	@FXML private Button okerrBtn;
	@FXML private Label errMsgLbl;
	@FXML private Label errLbl;
//	@FXML private Ic errIcn;
	
	
	public void setID(String s) {
		this.id=s;
	}

	
	public void insertedID(ActionEvent event) throws IOException {	
		
		if(srchIDfld.getText().trim().isEmpty())  {
			//((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			Stage secondaryStage=new Stage();
			
			Parent root= FXMLLoader.load(getClass().getResource("/GUI/ErrorInputGUI.fxml"));
			
			Scene scene=new Scene(root);
			
			secondaryStage.setTitle("Error");
			secondaryStage.setScene(scene);
			secondaryStage.show();
			}
			
			
		else {
			this.setID(srchIDfld.getText());
			User chat = new User("localhost", DEFAULT_PORT,this.id);
			chat.accept(); 	 //Wait for console data
			ArrayList<String> details = null;
			details=chat.fromSrvr;
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			
			ProductFromDBGUI productView = new ProductFromDBGUI();
			productView.showProductDetails(details);
			
			
			/*this.txtfldID.setText(vals.get(0));
			this.txtfldNm.setText(vals.get(1));
			this.txtfldTp.setText(vals.get(2));*/
			
/*			Stage secondaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Pane root2 = loader.load(getClass().getResource("/GUI/ProductFromDBGUI.fxml").openStream());
			
			Scene scene2 = new Scene(root2);	
			secondaryStage.setScene(scene2);		
			secondaryStage.show();
			
			UserBoundary boundary = loader.getController();		
			boundary.txtfldID.setText(details.get(0));
			boundary.txtfldNm.setText(details.get(1));
			boundary.txtfldTp.setText(details.get(2));		*/
				
			//scene.getStylesheets().add(getClass().getResource("/gui/StudentForm.css").toExternalForm());
			
			
/*	     	prdIDLbl.setText(vals.get(0));
     		prdNmLbl.setText(vals.get(1));
     		prdTpLbl.setText(vals.get(2));	*/
/*			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			Stage primaryStage=new Stage();
			Parent root= FXMLLoader.load(getClass().getResource("ProductFromDBGUI.fxml"));
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Product");
			primaryStage.setScene(scene);
			primaryStage.show();	*/
		}
	}			
	
	public void hideError(ActionEvent event) {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	}
	
	
	public void searchAgain(ActionEvent event) throws IOException {
		 ((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
	      Stage primaryStage=new Stage();
	      Parent root= FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Product");
			primaryStage.setScene(scene);
			primaryStage.show();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Parent root= FXMLLoader.load(getClass().getResource("SearchProductGUI.fxml"));
		Scene scene=new Scene(root);
		
		primaryStage.setTitle("Search for Product");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) throws IOException {
		String host = "";
	    int port = 0;  //The port number		
	    
	    launch(args);
	    
	    try
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	    }
	  /* UserGUI chat= new UserGUI(host, DEFAULT_PORT);
	      chat.accept();  //Wait for console data	*/			
		
	}		

}
