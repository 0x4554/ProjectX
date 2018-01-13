package entities;

/**
 * This class is the customer's entity class
 * CustomerEntity.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class CustomerEntity extends UserEntity {

	private long creditCardNumber;
	private String address;
	private long customerID;
	private double subscriptionDiscount;
	private AccountStatus accountStatus;
	
	/**
	 * 
	 * Constructor for the CustomerEntity.java class
	 * invokes the UserEntity constructor for the CustomerEntity
	 * @param userType
	 */
	public CustomerEntity() {
		super("C");
	}
	/**
	 * Getter for the creditCardNumber
	 * @return the creditCardNumber
	 */
	public long getCreditCardNumber() {
		return creditCardNumber;
	}
	/**
	 * Setter for the creditCardNumber
	 * @param creditCardNumber the creditCardNumber to set
	 */
	public void setCreditCardNumber(long creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	/**
	 * Getter for the address
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * Setter for the address
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * Getter for the customerID
	 * @return the customerID
	 */
	public long getCustomerID() {
		return customerID;
	}
	/**
	 * Setter for the customerID
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}
	/**
	 * Getter for the subscriptionDiscount
	 * @return the subscriptionDiscount
	 */
	public double getSubscriptionDiscount() {
		return subscriptionDiscount;
	}
	/**
	 * Setter for the subscriptionDiscount
	 * @param subscriptionDiscount the subscriptionDiscount to set
	 */
	public void setSubscriptionDiscount(String subscriptionDiscount) {
		if(subscriptionDiscount.equals("none"))
			this.subscriptionDiscount = 0;
		else if(subscriptionDiscount.equals("Monthly"))
			this.subscriptionDiscount=monthlySubscriptionDiscount;
		else if(subscriptionDiscount.equals("Yearly"))
			this.subscriptionDiscount=yearlySubscriptionDiscount;
	}
	/**
	 * Getter for the accountStatus
	 * @return the accountStatus
	 */
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	/**
	 * Setter for the accountStatus
	 * @param accountStatus the accountStatus to set
	 */
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	
	
	
		
}
