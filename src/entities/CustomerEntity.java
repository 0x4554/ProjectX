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
	private Subscription subscriptionDiscount;
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
	 * Getter for the subscriptionDiscount
	 * @return the subscriptionDiscount
	 */
	public String getSubscriptionDiscount() {
		return this.subscriptionDiscount.name();
	}
	
	/**
	 * Setter for the subscriptionDiscount
	 * @param subscriptionDiscount the subscriptionDiscount to set
	 */
	public void setSubscriptionDiscount(String subscriptionDiscount) {
		if(subscriptionDiscount.equals("None"))
			this.subscriptionDiscount = Subscription.None;
		else if(subscriptionDiscount.equals("Monthly"))
			this.subscriptionDiscount=Subscription.Monthly;
		else if(subscriptionDiscount.equals("Yearly"))
			this.subscriptionDiscount=Subscription.Yearly;
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
	public void setCustomerID(long parseLong) {
		this.customerID=parseLong;
		// TODO Auto-generated method stub
		
	}
	public long getCustomerID() {
		// TODO Auto-generated method stub
		return this.customerID;
	}
	
	
	
	
		
}
