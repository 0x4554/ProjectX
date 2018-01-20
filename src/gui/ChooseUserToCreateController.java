package gui;

import java.io.IOException;
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
import javafx.stage.Stage;

public class ChooseUserToCreateController implements Initializable{

	@FXML private ComboBox<String> prmCmb;
	@FXML private Button bckBtn;
	@FXML private Button okBtn;
	
	AdministratorMenuController am;
	
	private ObservableList<String> list;
	 
	 /**
	  * necessary empty constructor for the App
	  */
	 public ChooseUserToCreateController() {
	}
	 
	 public void setConnectionData(AdministratorMenuController m)
		{
			this.am=m;
		}
	 
	 /**
	  * Permissions varieties
	  */
		private void premissionsComboBox()
		{
			ArrayList<String> al = new ArrayList<String>();	
			al.add("Service Expert");
			al.add("Service Department");
			al.add("Store Worker");
			al.add("Store Manager");
			
			list = FXCollections.observableArrayList(al);
			prmCmb.setItems(list);
		}
		/**
		 * when OK button pressed
		 * @param event OK button pressed
		 * @throws InterruptedException
		 * @throws IOException
		 */
		public void enterOK(ActionEvent event) throws InterruptedException, IOException 
		{
			String antity = "";
			if(prmCmb.getSelectionModel().getSelectedItem().equals("Store Manager") || prmCmb.getSelectionModel().getSelectedItem().equals("Store Worker"))
			{
				((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
				antity=prmCmb.getSelectionModel().getSelectedItem();
				
				FXMLLoader loader = new FXMLLoader();
				Parent root = loader.load(getClass().getResource("/gui/CreateNewStoreManagerOrWorkerBoundary.fxml").openStream());
				
				
				CreateNewStoreManagerOrWorkerController cns = loader.getController();
				Stage primaryStage=new Stage();
				Scene scene=new Scene(root);
				cns.setAntity(antity);
				cns.setConnectionData(this);
				
				primaryStage.setTitle("Create");
				primaryStage.setScene(scene);
				primaryStage.show();
			}
				
		}
	
		/**
		 * when back button pressed
		 * @param event pressed back button
		 * @throws IOException
		 */	
		public void bckBtnHandler(ActionEvent event) throws IOException {
			((Node)event.getSource()).getScene().getWindow().hide();		//hide current window
			this.am.showAdministratorMenu();										//open previous menu
			return;
		}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		premissionsComboBox();
	}

}
