package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
///
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProductFromDBController implements Initializable{
	
	@FXML private Label rsltIDLbl;
	@FXML private Label rsltNmLbl;
	@FXML private Label rsltTpLbl;
	@FXML private Button srchagnBtn;
	//
	private MainBoundary main;

	public void setMainBoundary(MainBoundary main)	//set the main boundary to this controller (used for returning to the previous menu)
	{
		this.main=main;
	}
	
	public void showProductDetails(ArrayList<String> data,MainBoundary main) throws IOException {
		Stage secondaryStage=new Stage();
		FXMLLoader loader = new FXMLLoader();	//create an FXMLLoader
		Parent root= loader.load(getClass().getResource("ProductFromDBGUI.fxml").openStream());
		Scene scene=new Scene(root);
		
		ProductFromDBController pdb = (ProductFromDBController)loader.getController();	//get the FXMLLoader controller for use in the pdb (for using it's functions as a controller)
		
		pdb.setMainBoundary(main);	//set reference of (this) mainBoundary to the pdb controller
		pdb.setLabels(data);	//set the labels as returned from the DB
		
		secondaryStage.setTitle("Product Details");
		secondaryStage.setScene(scene);
		secondaryStage.show();	//show ProductFromDBBoundary
	}
	
	
	public void setLabels(ArrayList<String>res)	//set the labels to the details returned from the DB
	{
		this.rsltIDLbl.setText(res.get(0));
		this.rsltNmLbl.setText(res.get(1));
		this.rsltTpLbl.setText(res.get(2));
	}
	
	public void searchAgain(ActionEvent event) throws IOException {			//in case pressed "back"
		this.main.searchProduct(event);		//call method of main boundary for conducting another search
	}	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
	}


}
