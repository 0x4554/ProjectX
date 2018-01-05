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
	
	/**
	 * 
	 * Constructor for the OrderEntity.java class
	 */
	public OrderEntity()
	{
		this.productsInOrder = new ArrayList<ProductEntity>();
	}
	/**
	 * This method adds a product to the cart
	 * @param product	the new product
	 */
	public void addProductToCart(ProductEntity product)
	{
		this.productsInOrder.add(product);
	}
	/**
	 * Getter for the productsInOrder
	 * @return the productsInOrder
	 */
	public ArrayList<ProductEntity> getProductsInOrder() {
		return productsInOrder;
	}
	/**
	 * Setter for the productsInOrder
	 * @param productsInOrder the productsInOrder to set
	 */
	public void setProductsInOrder(ArrayList<ProductEntity> productsInOrder) {
		this.productsInOrder = productsInOrder;
	}
	private CashOrCredit paymendMethod;
	private DeliveryEntity deliveryDetails;
	
	/**
	 * Getter for the card
	 * @return the card
	 */
	public CardEntity getCard() {
		return card;
	}
	/**
	 * Setter for the card
	 * @param card the card to set
	 */
	public void setCard(CardEntity card) {
		this.card = card;
	}
}
