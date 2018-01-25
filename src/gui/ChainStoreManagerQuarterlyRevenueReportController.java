package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.OrderEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * This class is the controller for the Quarterly revenue report boundary
 * 
 * ChainStoreManagerQuarterlyRevenueReportController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class ChainStoreManagerQuarterlyRevenueReportController implements Initializable {

	@FXML
	private ListView<String> ordrLstVw;

	@FXML
	private Label ttlLbl;

	@FXML
	private Button clsBtn;
	
	private ArrayList<OrderEntity> listOfOrders;
	private ObservableList<String> listOfOrderString;
	private Double totalEarnings;
	
	/**
	 * This method show the list of orders and total revenue
	 * @param orders
	 */
	public void showOrders(ArrayList<OrderEntity> orders) {
		this.listOfOrders = orders;
		this.ordrLstVw.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //unable multiple selection
		totalEarnings=0.0;
		this.listOfOrderString = FXCollections.observableArrayList(); //the observable list to enter to the list  view

		for (OrderEntity order : this.listOfOrders)
		{
			this.listOfOrderString.add("Order Number : " +order.getOrderID()+" total : "+ order.getTotalPrice());
			this.totalEarnings += order.getTotalPrice();		//sum  the earnings
		}

		this.ordrLstVw.setItems(this.listOfOrderString); //set items to the list of active complaints
		this.ttlLbl.setText(this.totalEarnings.toString()+" NIS");

	}

	/**
	 * This method handles the close operation
	 * @param event	pressed close
	 */
	public void close(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); //hide current window
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
