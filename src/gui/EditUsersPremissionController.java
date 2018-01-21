package gui;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * this class allows to change users permission
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */


public class EditUsersPremissionController implements Initializable{
	
	@FXML private Label usrLbl;
	@FXML private TextField usrFld;
	@FXML private Button bckBtn;
	@FXML private Button okBtn;
	@FXML private ComboBox<String> prmsCmb;
	
	private StoreManagerMenuController mmc;
	private ObservableList<String> list;
	
	/**
	 * Necessary constructor for the APP
	 */
	public EditUsersPremissionController() {
	}
	
	
	public void setConnectionData(StoreManagerMenuController m)
	{
		this.mmc=m;
	}
	
	
	/**
	 * Permissions varieties 
	 */
	private void premissionsComboBox()
	{
		ArrayList<String> al = new ArrayList<String>();	
		al.add("Customer");
		al.add("Store Worker");
		al.add("Store Manager");
		
		list = FXCollections.observableArrayList(al);
		prmsCmb.setItems(list);
	}
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		premissionsComboBox();
	}
	
	/**
	 * when back button pressed
	 * @param event pressed back button
	 * @throws IOException
	 */	
	public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
		this.mmc.showManagerMenu();										//open previous menu
		return;
	}

	
	
}
