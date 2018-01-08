// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;
import java.net.Socket;





import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entities.ProductEntity;
import entities.StoreEntity;
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

  /**
   * this method handles the creation of new order in the system
   * 
   * @param newOrderDetails
   * @return
   */

  //***////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public static void receiveFile(String fileLocation,String messagePath,int fileSize) throws IOException
	{
		int bytesRead=0;
		int current = 0;
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
	//	Socket socket = null;
		try {
			//creating connection.
	        //socket = new Socket(ipAddress,portNo);
			System.out.println("connected.");
	
			// receive file
			byte [] byteArray  = new byte [fileSize];					//I have hard coded size of byteArray, you can send file size from socket before creating this.
			System.out.println("Please wait downloading file");
			
			//reading file from socket
		//	InputStream inputStream = socket.getInputStream();
			fileInputStream = new FileInputStream(messagePath);
			fileOutputStream = new FileOutputStream("C:\\newpic.jpg");   //** change it to the name of the picture ok!**//
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bytesRead=fileInputStream.read(byteArray,0,fileSize);
			
		//	bytesRead = inputStream.read(byteArray,0,byteArray.length);					//copying file from socket to byteArray
			
			current = bytesRead;
			bufferedOutputStream.write(byteArray, 0 , current);			//writing byteArray to file
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
	}
  //***////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private ArrayList<String> createNewOrder(String newOrderDetails)
  {
	  ArrayList<String> returnMessage = new ArrayList<String>();
	  String[] data = newOrderDetails.split("~");	//split the userName and the password
	  
	  
	  return returnMessage;
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
  	 * This method gets the list of stores from the DB
  	 * @return	arrayList of StoreEntity
  	 * @throws SQLException		thrown if there was an SQL exception
  	 * @throws ClassNotFoundException	ClassNotFoundException	thrown if connecting to DB failed
  	 */
	private ArrayList<StoreEntity> getAllstoresFromDB() throws SQLException, ClassNotFoundException {
		ArrayList<StoreEntity> listOfStoresFromDB = new ArrayList<StoreEntity>();
		StoreEntity store;
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
		ResultSet rs1 = stmt.executeQuery("SELECT BranchID,BranchName,BranchManager FROM projectx.store"); //get all the stores (ID,Name,managerID) in the stores table from the data base

		while (rs1.next())
		{
			store = new StoreEntity(rs1.getInt(1), rs1.getString(2), rs1.getInt(3)); //create a new instance of a store
			listOfStoresFromDB.add(store); //add the product from the data base to the list
		}

		stmt = con.createStatement();
		ResultSet rs2;
		Map<String,Double> storeDiscoutsSales; 	//holds all the discounts of the store sale
		for (StoreEntity store2 : listOfStoresFromDB)
		{
			rs2 = stmt.executeQuery("SELECT ProductID,ProductPrice FROM projectx.discount WHERE BranchID = "+ store2.getBranchID()); //get all the discounts for each store
			if(!(rs2.next()))						//if the store has no discounts
			{
				store2.setStoreDiscoutsSales(null);
			} else
			{
				storeDiscoutsSales = new HashMap<String, Double>();			//create  a new hashMap for discounts
				while (rs2.next())
				{
					storeDiscoutsSales.put(rs2.getString(1), rs2.getDouble(2)); //insert each store's discounts to a hashMap
				}
				store2.setStoreDiscoutsSales(storeDiscoutsSales); //insert the hashMap of discounts to the storeEntity
			}
		}
		
		stmt = con.createStatement();
		ResultSet rs3;
		ArrayList<Integer> listOfStoreWorkers;			//holds the list of store workers for the store entity
		for (StoreEntity store2 : listOfStoresFromDB)
		{
			rs3 = stmt.executeQuery("SELECT WorkerID FROM projectx.storeemployee WHERE BranchID = "+ store2.getBranchID()); //get all the discounts for each store
			if(!(rs3.next()))						//if the store has no workers
			{
				store2.setStoreWorkers(null);;
			} else
			{
				listOfStoreWorkers = new ArrayList<Integer>();			//create  a new arrayList for workers
				while (rs3.next())
				{
					if(rs3.getInt(1) == store2.getStoreManagerWorkerID()) {		//if the worker is the manager 
						store2.setStoreManagerWorkerID(rs3.getInt(1));			//set manager to store
					}else													//if a simple store worker
						listOfStoreWorkers.add(rs3.getInt(1)); 				//insert each store worker's worker id to the list
				}
				store2.setStoreWorkers(listOfStoreWorkers);				//set the list of workers for the store
			}
		}

		return listOfStoresFromDB;
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
		 //product = new ProductEntity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getString(6));	//create a new instance of a product
		 //listOfProducts.add(product);	//add the product from the data base to the list
	  }
	  return listOfProducts;
  }
  
  /**
   * this method handles the creation of new customers complaint
   * 
   * 
   * @param details	- String of the complaint info ("complaint!"complaintnumber|complaintDescription)
   * @return
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public String complaint(String details) throws SQLException, ClassNotFoundException {
	  String num=details.substring(details.indexOf("!")+1, details.indexOf("|"));
	  int orderNum = Integer.parseInt(num);
	  String desc=details.substring(details.indexOf("|")+1,details.length());
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
	  
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.orders WHERE Ordernum = '" +orderNum+"'");	//prepare a statement
	    if((rs.next()))																						//if such ID exists in the DB, Insert the new data
	    {
		    PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.complaints (OrderNum,Description) VALUES (?,?)");	//prepare a statement
		    ps.setInt(1, orderNum);																			//insert parameters into the statement
		    ps.setString(2, desc);
		    ps.executeUpdate();
		    
		    return "Success";
	    }
	  return "Order does not exist";   
  }
  
  /**this method handles file received from user
   * 
   * 
   * @param ipAddress - ip of the client
   * @param portNo - open port for sending/receiving the file
   * @param fileLocation - Path to where file would be saved on server side
   * @throws IOException - IOException might be thrown during the process
   */
  public void receiveFileFromClient(String ipAddress,int portNo) throws IOException
  {
	  	String fileLocation="/home/mdhttr/Documents/complaints/";
	  	fileLocation+="file.jpg";
		int bytesRead=0;
		int current = 0;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		Socket socket = null;
		try {
			//creating connection.
			//this.setPort(5556);
			
			
			socket = new Socket(ipAddress,portNo);
			System.out.println("connected.");
			
			// receive file
			byte [] byteArray  = new byte [6022386];					//I have hard coded size of byteArray, you can send file size from socket before creating this.
			System.out.println("Please wait downloading file");
			
			//reading file from socket
			InputStream inputStream = socket.getInputStream();
			fileOutputStream = new FileOutputStream(fileLocation);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bytesRead = inputStream.read(byteArray,0,byteArray.length);					//copying file from socket to byteArray

			current = bytesRead;
			do {
				bytesRead =inputStream.read(byteArray, current, (byteArray.length-current));
				if(bytesRead >= 0) current += bytesRead;
			} while(bytesRead > -1);
			bufferedOutputStream.write(byteArray, 0 , current);							//writing byteArray to file
			bufferedOutputStream.flush();												//flushing buffers
			
			System.out.println("File " + fileLocation  + " downloaded ( size: " + current + " bytes read)");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if (fileOutputStream != null) fileOutputStream.close();
			if (bufferedOutputStream != null) bufferedOutputStream.close();
			if (socket != null) socket.close();
		}
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
			sendToAllClients(listOfProducts);
		}

		if(operation.equals("createNewOrder"))
		{
			retval = createNewOrder(messageFromClient);
			sendToAllClients(retval);
		}
//		if(operation.equals("addProductToCatalog"))
//		{
//			
//		}

			
		
		////////////////Need to split it to store names, store details ,store workers.....////////
		if(operation.equals("getAllStores"))
		{
			ArrayList<StoreEntity> listOfAllStores = new ArrayList<StoreEntity>();		//an arrayList that holds all the stores in the DB
			listOfAllStores = getAllstoresFromDB();
			sendToAllClients(listOfAllStores);
	//		sendToAllClients(new ArrayList<StoreEntity>);
		}
//		if(operation.equals("addProductToCatalog"))
//		{
//			
//		}


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
		
		if(operation.equals("complaint")) {
			if(this.complaint((String)msg).equals("Success")) {
				System.out.println("complaint added");
				generalMessage = new String("Added");
				sendToAllClients(generalMessage);
			}
			else {
				System.out.println("complaint failed");
				generalMessage = new String("failed");
				sendToAllClients(generalMessage);
			}
		}
		
		if(operation.equals("downloadFile")) {
			System.out.println("Server downloading file sent from client");
			String filePath=(String)msg;
			filePath=filePath.substring(filePath.indexOf("!")+1,filePath.length());
			this.stopListening();
			sendToAllClients("downloading");
			this.receiveFileFromClient("localhost", 5556);
			}
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
			}
		
	
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