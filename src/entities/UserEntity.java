package entities;

import java.io.Serializable;

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
public abstract class  UserEntity implements UserInterface,Serializable{
	private String userName;
	private String password;
	private String userType;
	private String phoneNumber;
	private String EmailAddress;
	/**
	 * 
	 * Constructor for the UserEntity.java class
	 * This constructor sets the user's type 
	 * @param userType the type of the user
	 */
	public UserEntity(String userType)
	{
		this.userType = userType;
	}
	/**
	 * Getter for the phone number
	 * @return	the phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * Setter for the phone number
	 * @param phoneNumber the new phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
		this.EmailAddress = emailAddress;
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
	 * @param userName the userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * Getter for the password
	 * @return	the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Setter for the password
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Getter for the user type
	 * @return	the user type
	 */
	public String getUserType() {
		return userType;
	}
	/**
	 * Setter for the user type
	 * @param userType	the user type
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	
}
