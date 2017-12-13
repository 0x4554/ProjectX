package GUI;



import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateProductBoundary {
	
	@FXML private Button crtBtn;
	@FXML private TextField idFld;
	@FXML private TextField nmFld;
	@FXML private TextField typFld;
	
	ArrayList <String>newDetails=new ArrayList<String>();
	
	
	public void getProductDetails() {
		if(idFld.getText().trim().isEmpty() || nmFld.getText().trim().isEmpty() || typFld.getText().trim().isEmpty()) {
			System.out.print("please enter ");
		}
		
		else {
			
		}
	}


	public void showNewProductGUI() {
		
	}
}



