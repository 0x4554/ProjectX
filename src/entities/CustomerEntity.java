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

	private static final Double monthlyDiscount = 0.10;
	private static final Double yearlyDiscount = 0.35;
	private long creditCardNumber;
	private String address;
	private long customerID;
	private Subscription subscriptionDiscount;
	private AccountStatus accountStatus;
	
	/**
	 * 
	 * Constructor for the CustomerEntity.java class
	 * invokes the UserEntity constructor for the CustomerEntity
	 * 
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
	 * @return the subscriptionDiscount as a string
	 */
	public String getSubscriptionDiscount() {
		return this.subscriptionDiscount.toString();
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
	/**
	 * Getter for the monthlyDiscount
	 * @return the monthlyDiscount
	 */
	public static Double getMonthlyDiscount() {
		return monthlyDiscount;
	}
	/**
	 * Getter for the yearlyDiscount
	 * @return the yearlyDiscount
	 */
	public static Double getYearlyDiscount() {
		return yearlyDiscount;
	}
	/**
	 * Setter for the subscriptionDiscount
	 * @param subscriptionDiscount the subscriptionDiscount to set
	 */
	public void setSubscriptionDiscount(Subscription subscriptionDiscount) {
		this.subscriptionDiscount = subscriptionDiscount;
	}
	
	
	
	
	
		
}
