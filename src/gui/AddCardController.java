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


public class AddCardController implements Initializable{
	
	@FXML
	private TextArea crdTxtArea;
	
	@FXML
	private Button crdCnfrmBtn;
	
	private OrderEntity newOrder;
	
	public void setOrder(OrderEntity newOrder)
	{
		this.newOrder=newOrder;
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
