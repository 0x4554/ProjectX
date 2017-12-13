package GUI;

import java.io.IOException;
import java.util.ArrayList;

///
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
///
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ProductFromDBGUI {
	
	@FXML
	private TextField txtfldID;
	@FXML
	private TextField txtfldNm;
	@FXML
	private TextField txtfldTp;
	
	ArrayList<String> productDetails;
	
	public void showProductDetails(ArrayList<String> details) throws IOException
	{
		this.productDetails = details;
		
		Stage secondaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root2 = loader.load(getClass().getResource("/GUI/WTFproduct.fxml").openStream());
		
		
		
		Scene scene2 = new Scene(root2);
		secondaryStage.setScene(scene2);		
		secondaryStage.show();
		
		this.txtfldID.setText(details.get(0));
		this.txtfldNm.setText(details.get(1));
		this.txtfldTp.setText(details.get(2));
		
	//	UserBoundary boundary = loader.getController();		
		
	}

}
