package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import client.Client;
import entities.StoreEntity;
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

public class SelectStoreController implements Initializable{
	private Client clnt;
	private Integer branchID;
	public ObservableList<String> list;
	private Map<String,StoreEntity> listOfStoresEntities;
	private ArrayList<String> storeNames;

	@FXML
	private ComboBox strCmb;
	@FXML
	private Button okBtn;
	/**
	 * This method saves the client connection to the controller
	 * @param clnt	the connection client
	 */
	public void setConnectionData(Client clnt)
	{
		this.clnt=clnt;
	}
	/**
	 * This method inserts all the store names into the combobox
	 * @throws InterruptedException
	 */
	public void loadStoresIntoComboBox()
	{
		this.list = FXCollections.observableArrayList(storeNames);	//set to an observableArrayList
		this.strCmb.setItems(this.list);							//set the list to the comboBox
	}
	
	/**
	 * This method gets the list of stores Entities from the server into ListOfStoresEntities
	 * and creates a list of store names
	 * @throws InterruptedException
	 */
	public void showStores() throws InterruptedException
	{
		this.clnt.setDataFromUI("", "getAllStores!");	//set operation to get all stores from DB
		this.clnt.accept();
		while(!(this.clnt.getConfirmationFromServer()))		//wait for server response
			Thread.sleep(100);
		this.clnt.setConfirmationFromServer(); 				//reset to false
		ArrayList<StoreEntity> listOfStoresFromDB = new ArrayList<StoreEntity>();
		listOfStoresFromDB=this.clnt.getArrayListOfStoreEntityFromServer();		//get the list from the server's response
		
		this.storeNames = new ArrayList<String>();		//an arrayList of the store names
		this.listOfStoresEntities = new HashMap<String,StoreEntity>();		//a hashMap to hold the stores 

		for(StoreEntity store : listOfStoresFromDB)
		{
			this.listOfStoresEntities.put(store.getBranchName(), store);		//add the store to the storeEntity list
			storeNames.add(store.getBranchName());		//add the store name to the list of names
		}
	}
	/**
	 * This method handles the when pressed the selected store
	 * @param event
	 * @throws IOException 
	 */
	public void storeSelected(ActionEvent event) throws IOException {
		

		String selectedStoreName = "";
		if (!this.strCmb.getSelectionModel().isEmpty())				//if selected a store
		{
			((Node) event.getSource()).getScene().getWindow().hide(); //hide last window
			selectedStoreName = (String) this.strCmb.getSelectionModel().getSelectedItem(); //get the selected store name from the comboBox

			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
			CreateNewOrderController nom = loader.getController(); //set the controller to the FindProductBoundary to control the SearchProductGUI window

			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			nom.setConnectionData(this.clnt, this.listOfStoresEntities.get(selectedStoreName)); //send the connection and the StoreEntity selected by the user
			primaryStage.setTitle("New order from " + selectedStoreName);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		
		else {				//if no store was selected
			GeneralMessageController.showMessage("Please select a store");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loadStoresIntoComboBox();			//call method to load the store name into the comboBox
	}

}
