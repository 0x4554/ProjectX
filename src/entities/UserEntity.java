package entities;

/**
 * This is an abstract class for the users
 * This class implements UserInterface
 * 
 *@author Eliran Toledano
 *@author Lana Krikheli
 *@author Katya Yakovlev
 *@author Tal Gross
 *
 * This project was made by the ProjectX team
 */
public abstract class  UserEntity implements UserInterface{
	private String UserName;
	private String Password;
	private String UserType;
	private long PhoneNumber;
	private String EmailAddress;
	/**
	 * Getter for the phone number
	 * @return	the phone number
	 */
	public long getPhoneNumber() {
		return PhoneNumber;
	}
	/**
	 * Setter for the phone number
	 * @param phoneNumber the new phone number
	 */
	public void setPhoneNumber(long phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	/**
	 * Getter for the Email address
	 * @return	the Email Address
	 */
	public String getEmailAddress() {
		return EmailAddress;
	}
	/**
	 * Setter for the Email address
	 * @param emailAddress the new Email address
	 */
	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}
	/**
	 * Getter for the userName
	 * @return the userName
	 */
	public String getUserName() {
		return UserName;
	}
	/**
	 * Setter for the userName
	 * @param userName the userName
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}
	/**
	 * Getter for the password
	 * @return	the password
	 */
	public String getPassword() {
		return Password;
	}
	/**
	 * Setter for the password
	 * @param password the password
	 */
	public void setPassword(String password) {
		Password = password;
	}
	/**
	 * Getter for the user type
	 * @return	the user type
	 */
	public String getUserType() {
		return UserType;
	}
	/**
	 * Setter for the user type
	 * @param userType	the user type
	 */
	public void setUserType(String userType) {
		UserType = userType;
	}
	
	
	
}
