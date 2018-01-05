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
	 * This method inserts all the stores into the combobox
	 * @throws InterruptedException
	 */
	public void showStores() throws InterruptedException
	{
//		this.clnt.setDataFromUI("", "getAllStores!");
//		this.clnt.accept();
//		while(!(this.clnt.getConfirmationFromServer()))
//			Thread.sleep(100);
		ArrayList<String> listOfStores = new ArrayList<String>();
		listOfStores.add("1");
		listOfStores.add("koko");
		listOfStores.add("1");
//		listOfStores=this.clnt.getArrayListfromSrvr();
		ArrayList<String> storeNames = new ArrayList<String>();		//an arrayList of the store names

		for(int i=0;i<listOfStores.size();i+=3)
		{
			StoreEntity store = new StoreEntity(Integer.parseInt(listOfStores.get(i)), listOfStores.get(i+1),Integer.parseInt(listOfStores.get(i+2)));		//get a store
			storeNames.add(store.getBranchName());	//add the store name to the list of names
			this.listOfStoresEntities.put(store.getBranchName(),store);	//add the store to the storeEntity list
		}
//		for(int i=1;i<listOfStores.size();i+=3)
//			storeNames.add(listOfStores.get(i));	//add all the store names to the list
		list = FXCollections.observableArrayList(storeNames);	//set to an observableArrayList
		this.strCmb.setItems(list);		//set the list to the comboBox

		
	}
	/**
	 * This method handles the when pressed the selected store
	 * @param event
	 * @throws IOException 
	 */
	public void storeSelected(ActionEvent event) throws IOException
	{
		((Node)event.getSource()).getScene().getWindow().hide();	//hide last window
		
		String selectedStoreName="";
		selectedStoreName = this.strCmb.getSelectionModel().toString();
		
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("/gui/CreateNewOrderBoundary.fxml").openStream());
		CreateNewOrderController nom = loader.getController();	//set the controller to the FindProductBoundary to control the SearchProductGUI window

		Stage primaryStage=new Stage();
		Scene scene=new Scene(root);
		nom.setConnectionData(this.clnt,this.listOfStoresEntities.get(selectedStoreName));	//send the connection and the StoreEntity selected by the user
		primaryStage.setTitle("New order from "+selectedStoreName);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		listOfStoresEntities = new HashMap<String,StoreEntity>();		
	}

}
