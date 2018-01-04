package entities;

import java.util.ArrayList;

public class OrderEntity {
	public enum SelfOrDelivery{selfPickup,delivery};
	public enum OrderStatus{active,cancelled};
	public enum CashOrCredit{cash,credit};
	
	private int orderID;
	private String orderDateAndTime;
	private String Description;
	private CardEntity card;
	private SelfOrDelivery orderPickup;
	private OrderStatus status;
	private boolean paid;
	private double totalPrice;
	private ArrayList<ProductEntity> productsInOrder;
	private CashOrCredit paymendMethod;
}
