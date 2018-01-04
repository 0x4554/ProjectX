package entities;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;

public class OrderEntity {
	public enum SelfOrDelivery{selfPickup,delivery};
	public enum OrderStatus{active,cancelled};
	public enum CashOrCredit{cash,credit};
	
	private int orderID;
	private String orderDateAndTime;
	private Time orderTime;	//theses represents the sql type time and date 
	private Date orderDate;
	private String Description;
	private CardEntity card;
	private SelfOrDelivery orderPickup;
	private OrderStatus status;
	private boolean paid;
	private double totalPrice;
	private ArrayList<ProductEntity> productsInOrder;
	private CashOrCredit paymendMethod;
	private DeliveryEntity deliveryDetails;
}
