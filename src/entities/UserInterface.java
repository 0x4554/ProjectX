package entities;

/**
 * This Interface is for all the users in the system
 * 
 * UserInterface.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public interface UserInterface {

//	final double monthlySubscriptionDiscount = 0.05 ;
//	final double yearlySubscriptionDiscount = 0.1;
	
	public enum AccountStatus{confirmed,onHold,unconfirmed};
	public enum Subscription{Monthly,Yearly,None};
	/**
	 * Getter for the userName
	 * @return the userName
	 */
	public String getUserName();
	/**
	 * Setter for the userName
	 * @param userName the userName
	 */
	public void setUserName(String userName);
	/**
	 * Getter for the password
	 * @return	the password
	 */
	public String getPassword();
	/**
	 * Setter for the password
	 * @param password the password
	 */
	public void setPassword(String password);
	/**
	 * Getter for the user type
	 * @return	the user type
	 */
	public String getUserType();
	/**
	 * Setter for the user type
	 * @param userType	the user type
	 */
	public void setUserType(String userType);
	/**
	 * Getter for the phone number
	 * @return	the phone number
	 */
	public String getPhoneNumber();
	/**
	 * Setter for the phone number
	 * @param phoneNumber the new phone number
	 */
	public void setPhoneNumber(String phoneNumber);
	/**
	 * Getter for the Email address
	 * @return	the Email Address
	 */
	public String getEmailAddress();
	/**
	 * Setter for the Email address
	 * @param emailAddress the new Email address
	 */
	public void setEmailAddress(String emailAddress);
	
}
