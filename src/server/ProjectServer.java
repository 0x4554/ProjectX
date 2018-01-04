// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.ProductEntity;
import logic.ConnectedClients;
import ocsf.server.*;
import ocsf.*;
/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 */
public class ProjectServer extends AbstractServer 
{
	//do not touch//
	private static Connection con;
	private static String driver="com.mysql.jdbc.Driver";
  final public static int DEFAULT_PORT = 5555;
  
  
  /// * Constructs an instance of the echo server.
  /**
   * Constructor for the server
   * @param port the port of the server
   */
  public ProjectServer(int port) 
  {
    super(port);
  }
  
  /**
   * This method receives the file from the client?????????????????????????
   * @param fileLocation
   * @param messagePath
   * @param fileSize
   * @throws IOException
   */
  /*public static void receiveFile(String fileLocation,String messagePath,int fileSize) throws IOException
	{

		int bytesRead=0;
		int current = 0;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
	//	Socket socket = null;
		try {

			//creating connection.
	//		socket = new Socket(ipAddress,portNo);
			System.out.println("connected.");
			
			// receive file
			byte [] byteArray  = new byte [fileSize];					//I have hard coded size of byteArray, you can send file size from socket before creating this.
			System.out.println("Please wait downloading file");
			
			//reading file from socket
		//	InputStream inputStream = socket.getInputStream();
			fileInputStream = new FileInputStream(messagePath);
			fileOutputStream = new FileOutputStream("C:\\Users\\M207user\\Downloads\\new.pdf");
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
		
			bytesRead=fileInputStream.read(byteArray,0,fileSize);
			
		//	bytesRead = inputStream.read(byteArray,0,byteArray.length);					//copying file from socket to byteArray
			

			current = bytesRead;
			bufferedOutputStream.write(byteArray, 0 , current);							//writing byteArray to file
			bufferedOutputStream.flush();												//flushing buffers
			
			System.out.println("File " + fileLocation  + " downloaded ( size: " + current + " bytes read)");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (fileInputStream != null) fileInputStream.close();
			if (fileOutputStream != null) fileOutputStream.close();
			if (bufferedOutputStream != null) bufferedOutputStream.close();
		//	if (socket != null) socket.close();
		}
	}*/
  
  /**
   * method for connecting to DB
   * @return	the Connection
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  protected static  Connection connectToDB() throws SQLException, ClassNotFoundException
  {
	// Class.forName("com.mysql.jdbc.Driver");
 	 return DriverManager.getConnection("jdbc:mysql://localhost/projectx","root","Braude");	//connect to the sql database
  }
  
  /**
   * This method removes the connection of the user from the connected client list
   * @param username
   */
  private void terminateConnection(String username)
  {
	  ConnectedClients.removeConnectedClient(username);
  }
  
  /** This method handles any login attempt messages received from the client.
  *
  * @param msg The message received from the client (the userName and the password)
  * @param client The connection from which the message originated.
 * @throws ClassNotFoundException 
 * @throws SQLException 
  */
	public ArrayList<String> login(ConnectionToClient client, String loginInfo)
			throws ClassNotFoundException, SQLException {
		ArrayList<String> returnMessage = new ArrayList<String>();
		String[] data = loginInfo.split("~");	//split the userName and the password
		int attempts;
		Statement stmt;
		try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
				System.out.println("Connection to Data Base succeeded");
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
		}
		stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.users WHERE Username = '" + data[0] + "'"); //query to check if such a user exists
		if (!(rs.next())) //if user does not exists
		{
			//failed - ArrayList<String> to return in form of ["failed",reason of failure]
			returnMessage.add("failed"); //state failed to log in
			returnMessage.add("user does not exists"); //reason for failure
			return returnMessage;
		} else //if user name was found
		{
			if (ConnectedClients.isConnected(data[0]))	//if the user is already logged in to the system
			{
					//failed - ArrayList<String> to return in form of ["failed",reason of failure]
				returnMessage.add("failed"); 							//state failed to log in
				returnMessage.add("user is already logged in"); 		//reason for failure
				return returnMessage;
			} else if (data[1].equals(rs.getString(2))) 				//if password received matches the data base 
			{
				if (rs.getInt(4) == 3)	//if user is already blocked from too many login attempts
				{ 
						//failed - ArrayList<String> to return in form of ["failed",reason of failure]
					returnMessage.add("failed");						//state failed to log in
					returnMessage.add("user is blocked"); 				//reason for failure
					return returnMessage;
				} else
				{
										//----------success - ArrayList<String> to return in form of ["success",user's type]----------//
					returnMessage.add("success"); //state succeeded to login
					returnMessage.add(rs.getString(3)); //add the type of user (customer,worker...)
					PreparedStatement ps = con.prepareStatement("UPDATE users SET LoginAttempts = 0  WHERE Username = ?"); //prepare a statement
					ps.setString(1, data[0]); //reset the user's login attempts to 0
					ps.executeUpdate();

					ConnectedClients.insertNewConnection(data[0]);	//insert the userName to the connected list of users
					return returnMessage;
				}
			} else if (!(data[1].equals(rs.getString(2)))) //if password received does not match the data base 
			{
				if (rs.getInt(4) == 3)
				{ //if user is already blocked from too many login attempts
					//failed - ArrayList<String> to return in form of ["failed",reason of failure]
					returnMessage.add("failed"); //state failed to log in
					returnMessage.add("user is blocked"); //reason for failure
					return returnMessage;
				} else
				{
						//failed - ArrayList<String> to return in form of ["failed",reason of failure,number of attempts made]
					returnMessage.add("failed"); 							//state failed to log in
					returnMessage.add("password does not match"); 			//reason for failure
					attempts = rs.getInt(4) + 1; 							//increment number of attempts made
					PreparedStatement ps = con.prepareStatement("UPDATE users SET LoginAttempts = ? WHERE Username = ?"); //prepare a statement
					ps.setString(2, data[0]);
					ps.setInt(1, attempts); 								//update the number of attempts made to log in 
					ps.executeUpdate();
					returnMessage.add(Integer.toString(attempts));			//add the number of attempts left
					return returnMessage;
				}
			}
		}
		return returnMessage;

	}
  
  
   /** This method handles any product creation messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public String insertProduct(String msg, ConnectionToClient client) throws SQLException, ClassNotFoundException
  {
	  String [] data= msg.split("~");	//split the data
	  Statement stmt;
	  try
	    {
	    con = connectToDB();	//call method to connect to DB
	      
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	    }
	    System.out.println("Message received: " + msg + " from " + client);
	    								//first check if the ID already exists in the DB
	    stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE ProductID = '" +data[0]+"'");	//prepare a statement
	    if(!(rs.next()))	//if no such ID exists in the DB, Insert the new data
	    {
		    PreparedStatement ps = con.prepareStatement("INSERT INTO product (ProductID,ProductName,ProductType) VALUES (?,?,?)");	//prepare a statement
		    ps.setString(1,data[0]);	//insert parameters into the statement
		    ps.setString(2, data[1]);
		    ps.setString(3, data[2]);
		    ps.executeUpdate();
		    
		    return "Success";
	    }
	    
	    else	//if such Id already exists return failed String
	    	return "Failed";
	    
	  }
  
  /**
   * This method gets all of the products from the catalog
   * @return returns an ArrayList of all of the products in the catalog
   * @throws ClassNotFoundException	thrown if connecting to DB failed
   * @throws SQLException	thrown if there was an SQL exception
   */
  private ArrayList<ProductEntity> getCatalog() throws ClassNotFoundException, SQLException
  {
	  ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();
	  ProductEntity product;
	  Statement stmt;
	  try
	    {
	    con = connectToDB();	//call method to connect to DB
	    if(con!=null)
	    System.out.println("Connection to Data Base succeeded");  
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	    }
	  stmt = con.createStatement();
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.catalog");	//get all the products in the catalog table from the data base
	  
	  while(rs.next())
	  {
		 product = new ProductEntity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getString(6));	//create a new instance of a product
		 listOfProducts.add(product);	//add the product from the data base to the list
	  }
	  return listOfProducts;
  }
  
  /** This method handles product search messages received from the client.
  *
  * @param msg The message received from the client.
  * @param client The connection from which the message originated.
  */
  public ArrayList<String> getProduct(ConnectionToClient clnt,Object asked) throws SQLException, InterruptedException, ClassNotFoundException
  {
	  ArrayList<String> msg1 = new ArrayList<String>();
	  Statement stmt;
	  String str = (String) asked;	/**The asked Product**/
	  try
	    {
	    con = connectToDB();	//call method to connect to DB
	    if(con!=null)
	    System.out.println("Connection to Data Base succeeded");  
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	    }
	  stmt = con.createStatement();
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.product WHERE ProductID = "+str);	//query for extracting a prodcut's details
	  
	  
	  while(rs.next()) {	//run for the extracted data and add it to an arrayList of strings  
		  msg1.add(rs.getString(1));
		  msg1.add(rs.getString(2));
		  msg1.add(rs.getString(3));
	  }
	  
		  
	  
	 if(msg1.isEmpty())
		 System.out.println("No data found");
	  for(String s:msg1)
		System.out.println(s.toString()+" ");
	  return msg1; 
  }

    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  /**
   * This method handles the message received from the client
   */
  @Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
	// TODO Auto-generated method stub
	  String generalMessage;
	  String operation=(String)msg;
	  String messageFromClient=(String)msg;
	  
	  messageFromClient=messageFromClient.substring(messageFromClient.indexOf("!")+1,messageFromClient.length());	/**set apart the operation from the message from the client**/
	  
	  	operation=operation.substring(0,operation.indexOf("!"));
	  	
		ArrayList<String>retval=new ArrayList<String>();
		try {
		System.out.println("<user>"+(String)msg);
		if(operation.equals("getCatalog"))
		{
			ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();	//an arrayList that holds all the products in the catalog
			listOfProducts = getCatalog();
		}
//		if(operation.equals("addNewProdcutToCatalog"))
//		{
//			retval = this.addNewProductToCatalog(messageFromClient);
//		}
		if(operation.equals("exitApp"))
		{
			this.terminateConnection(messageFromClient);	//calls a method to remove the user from the connected list
		}
		if(operation.equals("login"))
		{
			retval = this.login(client,messageFromClient);
			sendToAllClients(retval);	//send arraylist back to client
		}
		if(operation.equals("getProduct"))	//check if asked to find an existing product
		{
			retval = this.getProduct(client,messageFromClient);	//get the product's details
			sendToAllClients(retval);	//send arraylist back to client
		}
		if(operation.equals("createProduct"))
		{
			messageFromClient=messageFromClient.substring(1,messageFromClient.length());
			if((this.insertProduct((String)messageFromClient, client)).equals("Success"))	//check if asked to create a new product and check if it was create successfully
			{
				generalMessage = new String("Product was successfully added to the DataBase");
			}
			else
			{
				generalMessage = new String("Product was not added to the DataBase.\n(Product ID already exists)");
			}
			sendToAllClients(generalMessage);	//send string back to client
		}
		}
		catch(Exception ex) {ex.printStackTrace();}
		
	
//		sendToAllClients(retval);
		
	}
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
	 public static void main(String[] args) 
  {
    int port = 0; //Port to listen on
    
    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ProjectServer sv = new ProjectServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }			


}