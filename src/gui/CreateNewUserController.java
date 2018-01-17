package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.UserEntity;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 * this class shows user account
 * and allows to change user details
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CreateNewUserController implements Initializable{

	@FXML private TextField nmTxt;
    @FXML private TextField idTxt;
    @FXML private TextField prmTxt;
    @FXML private TextField phnNumTxt;
    @FXML private TextField emlTxt;
    @FXML private TextField wnumTxt;
    @FXML private TextField slrTxt;
    @FXML private Button bckBtn;
    @FXML private Button crtBtn;
    @FXML private PasswordField pswrdTxt;
	
    
	AdministratorMenuController am;
	
	/**
	 * Necessary constructor for the App
	 */
	public CreateNewUserController() {
		
	}
	
	public void setConnectionData(AdministratorMenuController m)
	{
		this.am=m;
	}
	
    public void bckBtnHandler(ActionEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();				//hide current window
		this.am.showAdministratorMenu();										//open previous menu
		return;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
