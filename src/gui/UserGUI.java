package gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserGUI extends Application {
	@FXML
	private TextField srchIDfld;
	private AnchorPane anc;
	
	/**
	 * Class empty constructor
	 */
	public UserGUI() {}
	
	
	public String idToSearch() {
		String str=srchIDfld.getText();
		System.out.println("Tal the.... ha! gayyyyy" + str);
		
		return str;
	}


	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		anc.setVisible(true);
	}
	
	
}
