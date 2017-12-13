package GUI;



import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateProductBoundary {
	
	@FXML private Button crtBtn;
	@FXML private TextField idFld;
	@FXML private TextField nmFld;
	@FXML private TextField typFld;
	

	public void showNewProductGUI() throws IOException {
		
		 Parent root= FXMLLoader.load(getClass().getResource("NewProductGUI.fxml"));
			Stage primaryStage=new Stage();
			Scene scene=new Scene(root);
			
			primaryStage.setTitle("Search for Product");
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
}



