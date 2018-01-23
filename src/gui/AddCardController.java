package gui;

import java.net.URL;
import java.util.ResourceBundle;

import entities.CardEntity;
import entities.OrderEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * This class is a controller for adding a card to an order
 * AddCardController.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class AddCardController implements Initializable{
	
	@FXML
	private TextArea crdTxtArea;
	
	@FXML
	private Button crdCnfrmBtn;
	
	private OrderEntity newOrder;
	
	/**
	 * Setter for the new order
	 * @param newOrder
	 */
	public void setOrder(OrderEntity newOrder)
	{
		this.newOrder=newOrder;
	}
	
	/**
	 * This method sets the text for the card if already added before
	 * @param text	the existing card text
	 */
	public void setText(String text)
	{
		this.crdTxtArea.setText(text);
	}
	/**
	 * This method handles the card confirmation and add it to the order
	 * @param event	confirm pressed
	 */
	public void cardConfirm(ActionEvent event)
	{
		CardEntity newCard = new CardEntity(this.crdTxtArea.getText());		//get the text from the text area
		this.newOrder.setCard(newCard);										//save the new card to the new order
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
