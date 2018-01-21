package entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderEntity implements Serializable{
	public static enum SelfOrDelivery {selfPickup,delivery};
	public  static enum OrderStatus	  {active,cancelled,cancel_requested};
	public  static enum CashOrCredit   {cash,credit};
	
	private Integer orderID;
//	private String orderDateAndTime;
//	private Time orderTime;	//theses represents the sql type time and date 
//	private Date orderDate;
	private Timestamp orderTime;
	
	private String userName;
	private StoreEntity store;
//	private String Description;
	private CardEntity card;
	private SelfOrDelivery orderPickup;
	private Timestamp receivingTimestamp;
//	private Date receivingDate;
//	private Time receivingTime;
	private OrderStatus status;
	private Boolean paid;
	private Double totalPrice;
	private ArrayList<ProductEntity> productsInOrder;
	private CashOrCredit paymendMethod;
	private DeliveryEntity deliveryDetails;
	
	private Timestamp cancelRequestTime;
	
	
	/**
	 * 
	 * Constructor for the OrderEntity.java class
	 */
	public OrderEntity()
	{
		this.productsInOrder = new ArrayList<ProductEntity>();
		this.totalPrice=0.0;
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
	
	public void setProductsInOrder(ProductEntity p) {
		this.productsInOrder.add(p);
	}
	/**
	 * This method uses for removing a product from the cart
	 * @param productName
	 */
	public void removeProductFromCart(String productName)
	{
		for(ProductEntity product : this.productsInOrder)
			if(product.getProductName().equals(productName))
			{
				this.getProductsInOrder().remove(product);
				return;
			}
	}
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
	/**
	 * Getter for the store
	 * @return the store
	 */
	public StoreEntity getStore() {
		return store;
	}
	/**
	 * Setter for the store
	 * @param store the store to set
	 */
	public void setStore(StoreEntity store) {
		this.store = store;
	}
	/**
	 * Getter for the orderPickup
	 * @return the orderPickup
	 */
	public SelfOrDelivery getOrderPickup() {
		return orderPickup;
	}
	/**
	 * Setter for the orderPickup
	 * @param orderPickup the orderPickup to set
	 */
	public void setOrderPickup(SelfOrDelivery orderPickup) {
		this.orderPickup = orderPickup;
	}
	/**
	 * Getter for the paid
	 * @return the paid
	 */
	public boolean isPaid() {
		return paid;
	}
	/**
	 * Setter for the paid
	 * @param paid the paid to set
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
	/**
	 * Getter for the totalPrice
	 * @return the totalPrice
	 */
	public Double getTotalPrice() {
		return totalPrice;
	}
	/**
	 * Setter for the totalPrice
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	/**
	 * Getter for the paymendMethod
	 * @return the paymendMethod
	 */
	public CashOrCredit getPaymendMethod() {
		return paymendMethod;
	}
	/**
	 * Setter for the paymendMethod
	 * @param paymendMethod the paymendMethod to set
	 */
	public void setPaymendMethod(CashOrCredit paymendMethod) {
		this.paymendMethod = paymendMethod;
	}
	/**
	 * Getter for the deliveryDetails
	 * @return the deliveryDetails
	 */
	public DeliveryEntity getDeliveryDetails() {
		return deliveryDetails;
	}
	/**
	 * Setter for the deliveryDetails
	 * @param deliveryDetails the deliveryDetails to set
	 */
	public void setDeliveryDetails(DeliveryEntity deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
	/**
	 * Getter for the orderID
	 * @return the orderID
	 */
	public Integer getOrderID() {
		return orderID;
	}
	/**
	 * Setter for the orderID
	 * @param orderID the orderID to set
	 */
	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}
//	/**
//	 * Getter for the receivingDate
//	 * @return the receivingDate
//	 */
//	public Date getReceivingDate() {
//		return receivingDate;
//	}
//	/**
//	 * Setter for the receivingDate
//	 * @param receivingDate the receivingDate to set
//	 */
//	public void setReceivingDate(Date receivingDate) {
//		this.receivingDate = receivingDate;
//	}
//	/**
//	 * Getter for the receivingTime
//	 * @return the receivingTime
//	 */
//	public Time getReceivingTime() {
//		return receivingTime;
//	}
//	/**
//	 * Setter for the receivingTime
//	 * @param receivingTime the receivingTime to set
//	 */
//	public void setReceivingTime(Time receivingTime) {
//		this.receivingTime = receivingTime;
//	}
	/**
	 * Getter for the status
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}
	/**
	 * Setter for the status
	 * @param status the status to set
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	/**
	 * Getter for the userName
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Setter for the userName
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * Getter for the paid
	 * @return the paid
	 */
	public Boolean getPaid() {
		return paid;
	}
	/**
	 * Setter for the paid
	 * @param paid the paid to set
	 */
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
//	/**
//	 * Getter for the orderTime
//	 * @return the orderTime
//	 */
//	public Time getOrderTime() {
//		return orderTime;
//	}
//	/**
//	 * Setter for the orderTime
//	 * @param orderTime the orderTime to set
//	 */
//	public void setOrderTime(Time orderTime) {
//		this.orderTime = orderTime;
//	}
//	/**
//	 * Getter for the orderDate
//	 * @return the orderDate
//	 */
//	public Date getOrderDate() {
//		return orderDate;
//	}
//	/**
//	 * Setter for the orderDate
//	 * @param orderDate the orderDate to set
//	 */
//	public void setOrderDate(Date orderDate) {
//		this.orderDate = orderDate;
//	}
	/**
	 * Getter for the receivingTimestamp
	 * @return the receivingTimestamp
	 */
	public Timestamp getReceivingTimestamp() {
		return receivingTimestamp;
	}
	/**
	 * Setter for the receivingTimestamp
	 * @param receivingTimestamp the receivingTimestamp to set
	 */
	public void setReceivingTimestamp(Timestamp receivingTimestamp) {
		this.receivingTimestamp = receivingTimestamp;
	}
	/**
	 * Getter for the orderTime
	 * @return the orderTime
	 */
	public Timestamp getOrderTime() {
		return orderTime;
	}
	/**
	 * Setter for the orderTime
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	/**
	 * Getter for the cancelRequestTime
	 * @return the cancelRequestTime
	 */
	public Timestamp getCancelRequestTime() {
		return cancelRequestTime;
	}
	/**
	 * Setter for the cancelRequestTime
	 * @param cancelRequestTime the cancelRequestTime to set
	 */
	public void setCancelRequestTime(Timestamp cancelRequestTime) {
		this.cancelRequestTime = cancelRequestTime;
	}
	
	
	
	
}