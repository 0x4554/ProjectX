// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.security.auth.callback.ConfirmationCallback;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;

import java.util.Date;
import entities.CardEntity;
import entities.ComplaintEntity;
import entities.CustomerEntity;
import entities.DeliveryEntity;
import entities.OrderEntity;
import entities.ProductEntity;
import entities.StoreEntity;
import javafx.scene.image.Image;
import logic.ConnectedClients;
import logic.FilesConverter;
import logic.MessageToSend;
import ocsf.server.*;
import ocsf.*;
import gui.GeneralMessageController;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 */
public class ProjectServer extends AbstractServer 
{
	//do not touch//
	private static Connection con;
	private static String driver="com.mysql.jdbc.Driver";
	private int incomingFileName;
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
   System.out.println(username + " logged out");
	  ConnectedClients.removeConnectedClient(username);
  }


  /**
   * This method get the products matching the self defined products from the product table in the DB
 * @throws ClassNotFoundException  problem connecting
 * @throws SQLException for the sql query
 * @throws IOException for the files converter
   */
    private ArrayList<ProductEntity> getSelfDefinedProducts(ArrayList<String> requests) throws ClassNotFoundException, SQLException, IOException
    {
  	  			//the arrayList of String in form of {minPrice,maxPrice,type,dominantColor(if chosen)}
  	 ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();
  	 ProductEntity product;
  	 Blob productImage;
  	 Statement stmt;
  	 ResultSet rs;
  	 try
  		{
  			con = connectToDB(); //call method to connect to DB
  			if (con != null)
  				System.out.println("Connection to Data Base succeeded");
  		} catch (SQLException e) //catch exception
  		{
  			System.out.println("SQLException: " + e.getMessage());
  		}
  	 Double minPrice,maxPrice;
  	 minPrice= Double.parseDouble(requests.get(0)); 				//parse the minimum price
  	 maxPrice= Double.parseDouble(requests.get(1)); 				//parse the maximum price
  	 
  	 String type = "";
  	 type=requests.get(2); 											//get the product type
  	 
  	 String dominantColor ="";
  	 if(!(requests.size()<4)) 								//if dominant color was chosen
  	 {
  		dominantColor = "AND ProductDominentColor = '"+ requests.get(3)+"'";
  	 }
  	 stmt=con.createStatement();
  	 rs = stmt.executeQuery("Select * FROM projectx.product WHERE ProductPrice BETWEEN "+minPrice+" AND "+maxPrice+" AND ProductType = '"+type+"'"+dominantColor+"");
  	 while(rs.next())
  	 {
  		 product = new ProductEntity();									//create new product
  		 product.setProductID(rs.getInt(1));
  		 product.setProductName(rs.getString(2));
  		 product.setProductType(rs.getString(3));
  		 product.setProductPrice(rs.getDouble(4));
  		 product.setProductDescription(rs.getString(5));
  		 				//**get the blob for the image from the DB**//
  		 productImage =con.createBlob();
  		 productImage = rs.getBlob(6);
		 InputStream is = productImage.getBinaryStream();
  		 
 		 product.setProductImage(FilesConverter.convertInputStreamToByteArray(is)); 		//set the input stream to a byte array
  		 product.setProductDominantColor(rs.getString(7));
  		 listOfProducts.add(product);									//add the product to the list
  	 }
  	 return listOfProducts;												//return the found products
  	 
    }
 /**
   * This method cancel an order and sets its status to canceled in the DB
   * @param OrderID	the order to cancel
   * @return
 * @throws ClassNotFoundException 
 * @throws SQLException 
   */
  private String cancelOrder(Integer OrderID) throws ClassNotFoundException, SQLException
  {
	  Statement stmt;
	  String retMsg="";
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
	  try {
	  stmt.executeUpdate("UPDATE projectx.order SET OrderStatus = 'cancelled' WHERE OrderID = " + OrderID+ "");
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  return retMsg = "faild to cancel the order.";
	  }
	  return retMsg = "Order cancelled";
  }
   /**
   * This method calculates time difference between two Timestamp objects
   * @param t1	Later Timestamp
   * @param t2	sooner Timestamp
   * @return the time difference in miliseconds
   */
  private Long calculateTimeDifference(Timestamp t1, Timestamp t2)
  {
		Long t3 = t1.getTime()-t2.getTime();
		return t3;
  }
  
  /**
   * This method sets the customer's order status to "cancel_request" 
   * @param UserID the customer's id
   * @throws SQLException
 * @throws ClassNotFoundException 
   */
  private String cancelRequest(Integer OrderID) throws SQLException, ClassNotFoundException
  {
	  Statement stmt;
	  String retMsg = "";
	  try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
				System.out.println("Connection to Data Base succeeded");
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
		}
	  try {
	  stmt = con.createStatement();
	  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//	   Date date = new Date(System.currentTimeMillis());
//	    Time time = new Time(System.currentTimeMillis());
//	    time.setTime(System.currentTimeMillis());
//		DateFormat formatter = new SimpleDateFormat("HH:mm");		//format the time
//		DateFormat formatter2 = new SimpleDateFormat("dd/MM/yy");
	//  stmt.executeUpdate("UPDATE projectx.order SET OrderStatus = 'cancel_requested' ,CancelRequestDate = '"+ formatter.format(date.toString()) + "',CancelRequestTime = '"+ formatter2.format(date.toString())+"' WHERE Ordernum = " + OrderID + "");
		PreparedStatement ps = con.prepareStatement("UPDATE projectx.order SET OrderStatus = ? , CancelRequestTime = ? WHERE Ordernum = ?"); //prepare a statement
		ps.setString(1, OrderEntity.OrderStatus.cancel_requested.toString());
		ps.setTimestamp(2, timestamp);
		ps.setInt(3, OrderID);
		//ps.setDate(2, new Date(System.currentTimeMillis()));
		//ps.setTime(3, time);
		ps.executeUpdate();
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  return retMsg="cancel request failed.";
	  }
	  
	  return retMsg = "cancel request sent.";
  }
  
  
  /**
   * This method gets all the cancel requests from the DB
   * @return	an arrayList of Orders
 * @throws SQLException 
 * @throws ClassNotFoundException 
 * @throws IOException 
   */
  private ArrayList<OrderEntity> getCancelRequests() throws SQLException, ClassNotFoundException, IOException
  {
	  ArrayList<OrderEntity> listOfOrdersFromDB = new ArrayList<OrderEntity>();
		OrderEntity order;
		StoreEntity store;
		DeliveryEntity delivery = null;
		ProductEntity product;
		Statement stmt,stmt2,stmt3,stmt4,stmt5;
		ResultSet rs,rs2,rs3,rs4,rs5; 				//for the (order,delivery,list of products,store)
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
		stmt2 = con.createStatement();
		stmt3 = con.createStatement();
		stmt4 = con.createStatement();
		stmt5 = con.createStatement();
	    rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE OrderStatus = 'cancel_requested'"); //get all the stores (ID,Name,managerID) in the stores table from the data base
		
		while (rs.next())
		{
			order = new OrderEntity();
			
					//** get the delivery details **//
			rs2 = stmt2.executeQuery("SELECT * FROM projectx.delivery WHERE OrderID ="+rs.getInt(1)+"" );	//get the delivery data for the order
			while(rs2.next())
				delivery = new DeliveryEntity(rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getTimestamp(5));		//create a new delivery using the data
			order.setDeliveryDetails(delivery);
			
					//**get the products in the order **//
			rs3 = stmt3.executeQuery("SELECT ProductID FROM projectx.productsinorder WHERE Ordernum = " +rs.getInt(1)+ "");
			while(rs3.next())
			{
				rs4 = stmt4.executeQuery("SELECT * FROM projectx.product WHERE ProductID = " +rs3.getInt(1)+""); 	//get each product's details from DB
				while(rs4.next())
				{
					Blob b = con.createBlob(); 					//create blob
					b=rs.getBlob(8);							////get blob from DB
			  		InputStream is=b.getBinaryStream();	  		//get binary Stream for blob and than use FilesConverter.convertInputStreamToByteArray(InputStream)
			  		byte[] image = FilesConverter.convertInputStreamToByteArray(is);

			  		product = new ProductEntity(rs4.getInt(1), rs4.getString(2), rs4.getString(3), rs4.getDouble(4), rs4.getString(5), rs4.getString(7), image); //**NO IMAGE YET//
					order.addProductToCart(product); 			//add the product to the order
				}
			}
			order.setOrderID(rs.getInt(1));
			order.setUserName(rs.getString(2));
			order.setOrderTime(rs.getTimestamp(3));
			if(rs.getString(4) != null)
				order.setCard(new CardEntity(rs.getString(4)));
			if(rs.getString(5).equals(OrderEntity.SelfOrDelivery.selfPickup.toString()))  //set self pickup or delivery
				order.setOrderPickup(OrderEntity.SelfOrDelivery.selfPickup);
			else
				order.setOrderPickup(OrderEntity.SelfOrDelivery.delivery);
			
			if(rs.getString(6).equals(OrderEntity.OrderStatus.active.toString()))		//check order status
				order.setStatus(OrderEntity.OrderStatus.active);
			else if(rs.getString(6).equals(OrderEntity.OrderStatus.cancel_requested.toString()))
				order.setStatus(OrderEntity.OrderStatus.cancel_requested);
			else
				order.setStatus(OrderEntity.OrderStatus.cancelled);
			
			if(rs.getInt(7) == 0)				//check if order was paid for
				order.setPaid(false);
			else
				order.setPaid(true);
			
			order.setTotalPrice(rs.getDouble(8));
			order.setReceivingTimestamp(rs.getTimestamp(9));
						//** get the store **//
			rs5 = stmt5.executeQuery("SELECT * FROM projectx.store WHERE BranchID = "+rs.getInt(10)+"");
			while(rs5.next())
			{
				store = new StoreEntity(rs5.getInt(1), rs5.getString(2), rs5.getInt(3));
				order.setStore(store);
			}
			
			if(rs.getString(11).equals(OrderEntity.CashOrCredit.cash.toString()))
				order.setPaymendMethod(OrderEntity.CashOrCredit.cash);
			else
				order.setPaymendMethod(OrderEntity.CashOrCredit.credit);
			if(rs.getTimestamp(12) != null)
			{
				order.setCancelRequestTime(rs.getTimestamp(12));
			}
	
			listOfOrdersFromDB.add(order); //add the product from the data base to the list
		}
		
		return listOfOrdersFromDB;
  }
  
  
  /**
   * This method returns an ArrayList of the customer's orders
   * @param userID	the user id
   * @return ArrayList  of orders
   * @throws SQLException
   * @throws ClassNotFoundException
 * @throws IOException for file converting
   */
  private ArrayList<OrderEntity> getCustomerOrders(String userID) throws SQLException, ClassNotFoundException, IOException
  {
	  ArrayList<OrderEntity> listOfOrdersFromDB = new ArrayList<OrderEntity>();
		OrderEntity order;
		StoreEntity store;
		DeliveryEntity delivery = null;
		ProductEntity product;
		Statement stmt,stmt2,stmt3,stmt4,stmt5;
		ResultSet rs,rs2,rs3,rs4,rs5; 				//for the (order,delivery,list of products,store)
		
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
		stmt2 = con.createStatement();
		stmt3 = con.createStatement();
		stmt4 = con.createStatement();
		stmt5 = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE UserID ='"+userID+"'"); //get all the stores (ID,Name,managerID) in the stores table from the data base
	
		while (rs.next())
		{
			order = new OrderEntity();
			
					//** get the delivery details **//
			rs2 = stmt2.executeQuery("SELECT * FROM projectx.delivery WHERE OrderID ="+rs.getInt(1)+"" );	//get the delivery data for the order
			while(rs2.next())
				delivery = new DeliveryEntity(rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getTimestamp(5));		//create a new delivery using the data
			order.setDeliveryDetails(delivery);
			
					//**get the products in the order **//
			rs3 = stmt3.executeQuery("SELECT ProductID FROM projectx.productsinorder WHERE Ordernum = " +rs.getInt(1)+ "");
			while(rs3.next())
			{
				rs4 = stmt4.executeQuery("SELECT * FROM projectx.product WHERE ProductID = " +rs3.getInt(1)+""); 	//get each product's details from DB
				while(rs4.next())
				{
					Blob b = con.createBlob(); 					//create blob
					b=rs.getBlob(8);							////get blob from DB
			  		InputStream is=b.getBinaryStream();	  		//get binary Stream for blob and than use FilesConverter.convertInputStreamToByteArray(InputStream)
			  		byte[] image = FilesConverter.convertInputStreamToByteArray(is);
					product = new ProductEntity(rs4.getInt(1), rs4.getString(2), rs4.getString(3), rs4.getDouble(4), rs4.getString(5), rs4.getString(7),image); //**NO IMAGE YET//
					order.addProductToCart(product); 			//add the product to the order
				}
			}
			order.setOrderID(rs.getInt(1));
			order.setUserName(userID);
			order.setOrderTime(rs.getTimestamp(3));
			//order.setOrderDate(rs.getDate(4));
			if(rs.getString(4) != null)
				order.setCard(new CardEntity(rs.getString(4)));
			if(rs.getString(5).equals(OrderEntity.SelfOrDelivery.selfPickup.toString()))  //set self pickup or delivery
				order.setOrderPickup(OrderEntity.SelfOrDelivery.selfPickup);
			else
				order.setOrderPickup(OrderEntity.SelfOrDelivery.delivery);
			
			if(rs.getString(6).equals(OrderEntity.OrderStatus.active.toString()))
				order.setStatus(OrderEntity.OrderStatus.active);
			else if(rs.getString(6).equals(OrderEntity.OrderStatus.cancel_requested.toString()))
				order.setStatus(OrderEntity.OrderStatus.cancel_requested);
			else
				order.setStatus(OrderEntity.OrderStatus.cancelled);
			
			if(rs.getInt(7) == 0)
				order.setPaid(false);
			else
				order.setPaid(true);
			
			order.setTotalPrice(rs.getDouble(8));
			order.setReceivingTimestamp(rs.getTimestamp(9));
			//order.setReceivingTime(rs.getTime(11));
						//** get the store **//
			rs5 = stmt5.executeQuery("SELECT * FROM projectx.store WHERE BranchID = "+rs.getInt(10)+"");
			while(rs5.next())
			{
				store = new StoreEntity(rs5.getInt(1), rs5.getString(2), rs5.getInt(3));
				order.setStore(store);
			}
			
			if(rs.getString(7).equals(OrderEntity.CashOrCredit.cash.toString()))
				order.setPaymendMethod(OrderEntity.CashOrCredit.cash);
			else
				order.setPaymendMethod(OrderEntity.CashOrCredit.credit);
	
			listOfOrdersFromDB.add(order); //add the product from the data base to the list
		}
		
		return listOfOrdersFromDB;
	  
  }

 /**
   * This method add an order to the data base
   * @param newOrder	the new order
   * @return	messages
   * @throws SQLException
   * @throws ClassNotFoundException
   */
	private ArrayList<String> createNewOrder(OrderEntity newOrder) throws SQLException, ClassNotFoundException {
		ArrayList<String> returnMessage = new ArrayList<String>();
		Statement stmt;
		int orderCounter = 0;
		try
		{
			con = connectToDB(); //call method to connect to DB

			if (con != null)
				System.out.println("Connection to Data Base succeeded");
		}

		catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			e.printStackTrace();
		}

		stmt = con.createStatement();
		//ResultSet rs= stmt.executeQuery("SELECT OrdersID FROM projectx.counters");			//get the current latest order id
		
	//	while(rs.next())
		//	orderCounter = rs.getInt(1);
		
			PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.order (UserID,OrderTime,OrderCard,PickupMethod,OrderStatus,OrderPaid,TotalPrice,ReceiveTimestamp,BranchID,PaymentMethod) VALUES (?,?,?,?,?,?,?,?,?,?)"); //prepare a statement
			//ps.setInt(1, orderCounter+1);  				//insert new order id into the statement
			ps.setString(1, newOrder.getUserName());
			
			Timestamp time = new Timestamp(System.currentTimeMillis()); 	//get the time and date the order was placed on
			ps.setTimestamp(2, time);
			
			if(newOrder.getCard()!=null)						//if there is a card
				ps.setString(3,newOrder.getCard().getText());
			else
				ps.setString(3, null);
			ps.setString(4,newOrder.getOrderPickup().toString());
			ps.setString(5,newOrder.getStatus().toString());
			
			if(newOrder.getPaid())			//if paid
				ps.setInt(6, 1);
			else
				ps.setInt(6, 0);
			
			ps.setDouble(7, newOrder.getTotalPrice());
			ps.setTimestamp(8, newOrder.getReceivingTimestamp());
		//	ps.setTime(11, newOrder.getReceivingTime());
		//ps.setInt(12, newOrder.getStore().getBranchID());
	    	ps.setInt(9, newOrder.getStore().getBranchID());
	    	if(newOrder.getPaymendMethod().toString().equals(OrderEntity.CashOrCredit.cash.toString()))
	    		ps.setString(10, OrderEntity.CashOrCredit.cash.toString());
	    	else
	    		ps.setString(10, OrderEntity.CashOrCredit.credit.toString());
			ps.executeUpdate();

		//	ps =con.prepareStatement("UPDATE projectx.counters SET OrdersID= ? + 1");	//increment the orders ID counter
		//	ps.setInt(1, orderCounter);
		//	ps.executeUpdate();
			
					//** now insert all the products in order to the productsInOrder table **//
			PreparedStatement ps3;
			for(ProductEntity product : newOrder.getProductsInOrder())
			{
				ps3 = con.prepareStatement("INSERT INTO projectx.productsinorder (OrderNum,ProductID) VALUES (?,?)"); //prepare a statement
				ps3.setInt(1, orderCounter+1);
				ps3.setInt(2, product.getProductID());
				ps3.executeUpdate();
			}
			
			if(newOrder.getOrderPickup().equals(OrderEntity.SelfOrDelivery.delivery))		//if the order has a delivery
			{
						//** insert all the order's delivery details in to the delivery table **//
				newOrder.getDeliveryDetails().setOrderID(orderCounter+1);	//set the orderID for the delivery
				PreparedStatement ps2 = con.prepareStatement("INSERT INTO projectx.delivery (OrderID,DeliveryAddress,RecipientName,PhoneNumber,DeliveryTimestamp) VALUES (?,?,?,?,?)"); //prepare a statement
				ps2.setInt(1, newOrder.getDeliveryDetails().getOrderID());
				ps2.setString(2, newOrder.getDeliveryDetails().getDeliveryAddress());
				ps2.setString(3, newOrder.getDeliveryDetails().getRecipientName());
				ps2.setString(4,	 newOrder.getDeliveryDetails().getPhoneNumber());
				ps2.setTimestamp(5, newOrder.getReceivingTimestamp());/**changed it lanaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa*/
			//	ps2.setTime(6, newOrder.getDeliveryDetails().getDeliveryTime());
				ps2.executeUpdate();
			}
	  
	  
	  returnMessage.add("Your order was placed successfully.\nWe hope to see you again.");
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
			e.printStackTrace();
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
				System.out.println("connected user tried to login again - blocked");
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
  public String insertProduct(ProductEntity product, ConnectionToClient client) throws SQLException, ClassNotFoundException
  {
	  Statement stmt;
	  try
	    {
	    con = connectToDB();	//Call method to connect to DB
	      
	    }
	    catch( SQLException e)	//Catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	     
	    }
	    //First check if already exists in the DB
	    stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE ProductName = '" +product.getProductName()+"'");	//Prepare a statement
	    if(!(rs.next()))	//If no such ID exists in the DB, Insert the new data
	    {
		    PreparedStatement ps = con.prepareStatement("INSERT INTO product (ProductName,ProductType,ProductPrice,ProductDescription,ProductImage,ProductDominentColor) VALUES (?,?,?,?,?,?)");	//prepare a statement
		    ps.setString(1, product.getProductName());
		    ps.setString(2, product.getProductType());
		    ps.setDouble(3, product.getProductPrice());
		    ps.setString(4,product.getProductDescription());
		    ps.setBlob(5,FilesConverter.convertByteArrayToInputStream(product.getProductImage()));
		    ps.setString(6, product.getProductDominantColor());
		    ps.executeUpdate();
		    return "Success";
	    }  
	    else	//If such name already exists return failed String
	    	return "Failed";
	    
	  }

  /**
   * The method delet's product from the the product table in Data base 
   * @param product
   * @return
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  public String DeleteProductsFromDB(ProductEntity product) throws ClassNotFoundException, SQLException
  {
	  Statement stmt;
	  try
	    {
	    con = connectToDB();	//Call method to connect to DB
	      
	    }
	    catch( SQLException e)	//Catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	     
	    }
	    //First check if the name already exists in the DB
	    stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE ProductName = '" +product.getProductName()+"'");	//prepare a statement
	    if(rs.next()) {
	    	PreparedStatement ps = con.prepareStatement("DELETE FROM projectx.product WHERE ProductName = ?");	//prepare a statement
		    ps.setString(1, product.getProductName());																			//insert parameters into the statement
		    ps.executeUpdate();
		    return "Success";    
  }
	    else return "Failed to delete product does not exist";
  }
  	/**
  	 * converts array of bytes (byte[]) into a InputStream in order to enter it to the database as blob
  	 * 
  	 * 
  	 * @param byteArray - the byte[] to convert
  	 */
  	public InputStream convertByteArrayToInputStream(byte [] byteArray) {
  		
  		InputStream retInputStream = new ByteArrayInputStream(byteArray);
  		
  		return retInputStream;
  	}
  
  	/**
  	 * this method allows the insertion of a photo into the data base
  	 * 
  	 * @param instrm
  	 * @throws ClassNotFoundException
  	 * @throws SQLException
  	 */
  	public void insertPhotoToDB(InputStream instrm) throws ClassNotFoundException, SQLException {
  		Statement stmt;
  		
  		 try
 	    {
 	    con = connectToDB();	//call method to connect to DB
 	      
 	    }
 	    catch( SQLException e)	//catch exception
 	    {
 	      System.out.println("SQLException: " + e.getMessage() );
 	    }
  		 System.out.println("uploadind file to Data Base");
   		stmt=con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.complaints WHERE Ordernum = '" +incomingFileName+"'");	//prepare a statement
	    if((rs.next()))	//if no such ID exists in the DB, Insert the new data
	    {
	   		PreparedStatement pstmt =con.prepareStatement("UPDATE projectx.complaints set File = ? where Ordernum = ?");
	   		pstmt.setBlob(1, instrm);
	   		pstmt.setInt(2, incomingFileName);
	   		
	   		pstmt.executeUpdate();
	    }
	    
	    /*checking the returned value for cases it doesnt work(Should return string or int or boolean)*/
  	}
  	
  	/**
  	 * this method gets a Blob item from the data base and converts it into InputStream 
  	 * 
  	 * @param orderNum - primary key of the table we want to get the data from
  	 * @return
  	 * @throws SQLException
  	 */
  	public InputStream getPhotoFromDB(String orderNum) throws SQLException{
  		InputStream is = null;
  		Statement stmt;
  		Blob b = con.createBlob();							//Object to contain the data from the data base
  		
  		try {
  		con=connectToDB();									//Achieve connection to the data base
  		}
  		catch(Exception e) {
  			System.out.println("failed connecting to db");	
  		}
  		
  		try {
  			stmt=con.createStatement();
  		    ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.complaints WHERE Ordernum = '" +orderNum+"'");	//Statement to execute
  		    if (rs.next())						//if such ID exists in the DB, get the new data
  		    	b = rs.getBlob("File");			//getting the wanted blob form the column File
  		    is=b.getBinaryStream();				//convert blob to InputStream
  		    
  		}catch(Exception e) {
  			e.printStackTrace();
  		}
  		
  		return is;							//returned value
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
		Map<Integer,Double> storeDiscoutsSales; 	//holds all the discounts of the store sale
		for (StoreEntity store2 : listOfStoresFromDB)
		{
			rs2 = stmt.executeQuery("SELECT ProductID,ProductPrice FROM projectx.discount WHERE BranchID = "+ store2.getBranchID()); //get all the discounts for each store
			if(!(rs2.next()))						//if the store has no discounts
			{
				store2.setStoreDiscoutsSales(null);
			} else
			{
				storeDiscoutsSales = new HashMap<Integer, Double>();			//create  a new hashMap for discounts
				while (rs2.next())
				{
					storeDiscoutsSales.put(rs2.getInt(1), rs2.getDouble(2)); //insert each store's discounts to a hashMap
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
 * @throws IOException  for the file convention
   */
  private ArrayList<ProductEntity> getCatalog() throws ClassNotFoundException, SQLException, IOException
  {
	  ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();
	  Statement stmt;
	  Blob b = con.createBlob();							//Object to contain the data from the data base
	  InputStream is = null;
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
	  
	  ResultSet rs = stmt.executeQuery("SELECT *  FROM projectx.product INNER JOIN projectx.catalog ON  projectx.product.ProductID=projectx.catalog.ProductID" );	//get all the products in the catalog table from the data base

	  
	  while(rs.next())
	  {
		  
		  ProductEntity product = new ProductEntity();	//create a new instance of a product
		  product.setProductID(rs.getInt(1));                   //insert data
		  product.setProductName(rs.getString(2));
		  product.setProductType(rs.getString(3));
		  product.setProductPrice(rs.getDouble(4));
		  product.setProductDescription(rs.getString(5));

		  if(rs.getBlob(6)!=null)//if there is a picture for this product
		  {
		  b=rs.getBlob(6);
		   is=b.getBinaryStream();	
		  byte [] byte_Image;
		  byte_Image=FilesConverter.convertInputStreamToByteArray(is);
		  product.setProductImage(byte_Image);
		  }
		  product.setProductDominantColor(rs.getString(7));
		  listOfProducts.add(product);	//add the product from the data base to the list
	  }
	  return listOfProducts;
  }

  
  /**
   * The method return all the product's from the data base table
   * @return an ArrayList of product entities 
   * @throws SQLException
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public ArrayList<ProductEntity>getAllProductsFromDB() throws SQLException, IOException, ClassNotFoundException
  {
	  ArrayList<ProductEntity> AllProducts=new ArrayList<ProductEntity>();
	  Statement stmt;
	  Blob b = con.createBlob();							//Object to contain the data from the data base
	  InputStream is = null;
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
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.product");	//get all the products in the catalog table from the data base
	  while(rs.next())
	  {
		  ProductEntity product = new ProductEntity();	//create a new instance of a product
		  product.setProductID(rs.getInt(1));
		  product.setProductName(rs.getString(2));
		  product.setProductType(rs.getString(3));
		  product.setProductPrice(rs.getDouble(4));
		  product.setProductDescription(rs.getString(5));

		  if(rs.getBlob(6)!=null)
		  {
		  b=rs.getBlob(6);
		   is=b.getBinaryStream();	
		  byte [] byte_Image;
		  byte_Image=FilesConverter.convertInputStreamToByteArray(is);
		  product.setProductImage(byte_Image);
		  }
		  product.setProductDominantColor(rs.getString(7));
		  AllProducts.add(product);	//add the product from the data base to the list
	  }
	  return AllProducts;
  }
  /**
   *This method  Insert's product to catalog
   * @param prd the product we want to add to the catalog
   * @return String message Success, or Failed
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public String addToCatalog(ProductEntity prd) throws SQLException, ClassNotFoundException
  {
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
	  ResultSet rs = stmt.executeQuery("SELECT * FROM catalog WHERE ProductID = '" +prd.getProductID()+"'");	//prepare a statement
	    if(!(rs.next()))																						//if such ID does not exists in the DB, Insert the new data
	    {
		    PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.catalog (ProductID,ProductName) VALUES (?,?)");	//prepare a statement
		    ps.setInt(1, prd.getProductID());																			//insert parameters into the statement
		    ps.setString(2, prd.getProductName());
		    ps.executeUpdate();
		    return "Success";
	    }
	  return "Failed";   
  }
  /**
   * This method delet's product from the catalog 
   * @param prd the product we want to delete from catalog
   * @return String "Success" or "Failure"
   * @throws SQLException
   * @throws ClassNotFoundException
   */
 public String deleteProductFromCatalog(ProductEntity prd) throws SQLException, ClassNotFoundException/**lanaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa*/

  {
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
	  ResultSet rs = stmt.executeQuery("SELECT * FROM catalog WHERE ProductID = '" +prd.getProductID()+"'");	//see if the product exists in the catalog
	    if((rs.next()))																						//if such ID exists in the DB, delete .
	    {
		    PreparedStatement ps = con.prepareStatement("DELETE FROM projectx.catalog WHERE ProductID = ?");	//prepare a statement
		    ps.setInt(1, prd.getProductID());																			//insert parameters into the statement
		    ps.executeUpdate();
		    return "Success";                                                                            //deleted successesfuly
	    }
	  return "Product was not found";   
  }

 /**
  * The method Update's a product in the product's table in the data base
  * @param product , the product after editing
  * @param OldProduct , the product before editing
  * @return String "Success"
  * @throws SQLException
  * @throws ClassNotFoundException
  */
 public String UpdateProductInDB(ProductEntity product,ProductEntity OldProduct) throws SQLException, ClassNotFoundException
{
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
	//  ResultSet rs = stmt.executeQuery("SELECT * FROM catalog WHERE ProductID = '" +prd.getProductID()+"'");	//see if the product exists in the catalog
	   // if((rs.next()))																						//if such ID exists in the DB, delete .
	 //   {
		    PreparedStatement ps = con.prepareStatement("UPDATE  projectx.product SET ProductName = ?,ProductType=?,ProductPrice=?,ProductDescription=?,ProductDominentColor=? WHERE ProductName=?");	//prepare a statement
		    ps.setString(1, product.getProductName());
		    ps.setString(2, product.getProductType());
		    ps.setDouble(3, product.getProductPrice());
		    ps.setString(4, product.getProductDescription());
		    
		   /* if(product.getProductImage()!=null)
		    {
		    	ps.setBlob(5,FilesConverter.convertByteArrayToInputStream(product.getProductImage()));
		    }*/
		    ps.setString(5, product.getProductDominantColor());
		    ps.setString(6, OldProduct.getProductName()); 
		    ps.executeUpdate();
		    return "Success";                                                                           
	   // }
}
  /**
   * This method handles the creation of new customers complaint
   * 
   * @param details	- String of the complaint info ("complaint!"complaintnumber|complaintDescription)
   * @return
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  	public String complaint(ComplaintEntity details) throws SQLException, ClassNotFoundException {
	
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
	  
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE Ordernum = '" +details.getOrderID()+"'");	//prepare a statement
	    if((rs.next()))																						//if such ID exists in the DB, Insert the new data
	    {
	    	InputStream inStrm=FilesConverter.convertByteArrayToInputStream(details.getFile());
		    PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.complaints (OrderNum,Description,Status) VALUES (?,?,?)");	//prepare a statement
		    ps.setInt(1,details.getOrderID());																			//insert parameters into the statement
		    ps.setString(2, details.getDescription());
		    ps.setString(3, details.getStatus().toString());
		    ps.setBlob(4, inStrm);
		    ps.executeUpdate();
		    
		    return "Success";
	    }
	  return "Order does not exist";   
  }

 
  /** This method handles product search messages received from the client.
  *    Search is with ID
  * @param msg The message received from the client.
  * @param client The connection from which the message originated.
 * @throws IOException 
  */
  public ProductEntity getProduct(ConnectionToClient clnt,int asked) throws SQLException, InterruptedException, ClassNotFoundException, IOException
  {
	  ProductEntity prd=new ProductEntity();
	  //ArrayList<ProductEntity> product = new ArrayList<ProductEntity>();
	  Statement stmt;
	  int productID=asked;     /*The id of the product we want to get from the data base*/
	  Blob b = con.createBlob();							//Object to contain the data from the data base
      InputStream is = null;
		
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
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.product WHERE ProductID ='" +productID+"'");	//query for extracting a prodcut's details
	  
	  if(rs.next())
	  {
		  prd.setProductID(rs.getInt(1));
		  prd.setProductName(rs.getString(2));
		  prd.setProductType(rs.getString(3));
		  prd.setProductPrice(rs.getDouble(4));
		  prd.setProductDescription(rs.getString(5));
		  /**************************************************************added*******************************************************/
		  if(rs.getBlob(6)!=null)
		  {
		  b=rs.getBlob(6);
		   is=b.getBinaryStream();	
		  byte [] byte_Image;
		  byte_Image=FilesConverter.convertInputStreamToByteArray(is);
		  prd.setProductImage(byte_Image);
		  }
		  /***************************************************************************************************************************/
  		  prd.setProductDominantColor(rs.getString(7));  
          return prd;
	  }
	  else 
	         return null;  
  }
/**
 * This method return's the discount's from the table in the data base 
 * @param storeID, the store that the customer shop's in
 * @return hash map of the discounts 
 * @throws SQLException 
 * @throws ClassNotFoundException 
 */
  public HashMap<Integer,Double> getDiscounts(int storeID) throws SQLException, ClassNotFoundException
  {
	  HashMap<Integer,Double> product = new HashMap<Integer,Double>();
	 int key_product_id=0;
	 double price=0;
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
	  
	  /*Query -get all the discounts for the storeID*/
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.discount WHERE BranchID ='" +storeID+"'");	
	  if(rs.next())
	  {
		  key_product_id=rs.getInt(2); //set value's
		  price=rs.getDouble(3);
          product.put(key_product_id, price); //add values to hash map
          return product;
	  }
	  else 
	         return null; 
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
	protected synchronized void handleMessageFromClient(Object msg, ConnectionToClient client) {
	// TODO Auto-generated method stub
	  String generalMessage;
	  String operation;
	  MessageToSend messageToSend=(MessageToSend)msg;
	  Object messageFromClient=messageToSend.getMessage();		//the sent data from the client
	  
	  
	  operation=messageToSend.getOperation();					//the operation required
	//  messageFromClient=m.getMessage();
	  
	//  messageFromClient=messageFromClient.substring(messageFromClient.indexOf("!")+1,messageFromClient.length());	/**set apart the operation from the message from the client**/
	  
/////	  	operation=operation.substring(0,operation.indexOf("!"));
	  	
		ArrayList<String>retval=new ArrayList<String>();
		
		try {
		System.out.println("<user>"+operation);
		
		if(operation.equals("getCatalog"))
		{
			ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();	//an arrayList that holds all the products in the catalog
			listOfProducts = getCatalog();
			messageToSend.setMessage(listOfProducts);		//set the message for sending back to the client
			sendToAllClients(messageToSend);
			
		}
		if(operation.equals("getSelfDefinedProduct"))		//get an arratList of products who fit the customer's paramaters
		{
			ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();	//an arrayList that holds all the products in the catalog
			listOfProducts = getSelfDefinedProducts((ArrayList<String>)messageFromClient);
			messageToSend.setMessage(listOfProducts);
			sendToAllClients(messageToSend);
		}
		
		if(operation.equals("getDiscounts"))/*get the discounts Hash Map for a specific store*/
		{
			int store= (int)messageToSend.getMessage();
			HashMap<Integer,Double> listOfDiscounts = new HashMap<Integer,Double>();	//an arrayList that holds all the products in the catalog
			listOfDiscounts= getDiscounts(store);                                                                     //get discount from discount table in the data base
			if(listOfDiscounts!=null)
			{
			messageToSend.setMessage(listOfDiscounts);		                                                     //set the message for sending back to the client
			sendToAllClients(messageToSend);
			}
			else {
				System.out.println("Operation failed");
				messageToSend.setMessage(null); //set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
		}
			if(operation.equals("cancelRequest"))
		{
			String retMsg = cancelRequest((Integer)messageFromClient);
			messageToSend.setMessage(retMsg);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("getCancelRequests"))
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getCancelRequests();
			messageToSend.setMessage(listOfOrders);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("cancelOrder"))
		{
			String retMsg = cancelOrder((Integer)messageFromClient);
			messageToSend.setMessage(retMsg);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("addProductToCatalog"))
		{
			ProductEntity product = (ProductEntity)messageToSend.getMessage();
			
			if(this.addToCatalog(product).equals("Success")) {
				System.out.println("product added sucessesfuly to the catalog DB");
				messageToSend.setMessage("Added"); 		//set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
			else {
				System.out.println("Inserting failed");
				messageToSend.setMessage("failed"); //set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
		}
		if(operation.equals("deleteProductFromCatalog"))
		{
			ProductEntity product;
			product =  (ProductEntity)messageToSend.getMessage();
			
			if(this.deleteProductFromCatalog(product).equals("Success")) {
				messageToSend.setMessage("Deleted"); 		//set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
			else {
				messageToSend.setMessage("failed"); //set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
		}
		
		if(operation.equals("createNewOrder"))
		{
			try
			{
			retval = createNewOrder((OrderEntity)messageFromClient);
			}
			catch(Exception e)
			{
				retval.add("We are sorry,Something went wrong, Please try again later.");
				e.printStackTrace();
			}
			messageToSend.setMessage(retval);
			sendToAllClients(messageToSend);
		}
		
		if(operation.equals("getCustomerOrders"))		//for getting a specific customer's list of orders
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getCustomerOrders((String)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			sendToAllClients(messageToSend);
		}
		
		if(operation.equals("getAllOrders"))			//for getting ALL of the orders in the DB
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			//listOfOrders = getAllOrders((String)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("createAccount")) {
			CustomerEntity custen=(CustomerEntity)messageFromClient;	
			this.insertNewCustomer(custen);
		}
		if(operation.equals("getAllStores"))				//gets all the stores in the DB
		{
			ArrayList<StoreEntity> listOfAllStores = new ArrayList<StoreEntity>();		//an arrayList that holds all the stores in the DB
			listOfAllStores = getAllstoresFromDB();
			messageToSend.setMessage(listOfAllStores);		//set the message for sending back to the client
			sendToAllClients(messageToSend);
		}
		if(operation.equals("exitApp"))					//for when a user exits the app
		{
			this.terminateConnection((String)messageFromClient);	//calls a method to remove the user from the connected list
		}
		
		if(operation.equals("login"))					//for when a user tries to log in
		{
			retval = this.login(client,(String)messageFromClient);
			messageToSend.setMessage(retval);	//set the message for sending back to the client
			sendToAllClients(messageToSend);	//send arraylist back to client
		}
		
		if(operation.equals("getProduct"))	//check***********fix*******to***********lana*******object***********************//
		{

			ProductEntity produ;
			int productid = (int)messageToSend.getMessage();
			produ= this.getProduct(client,productid);	//get the product
			messageToSend.setMessage(produ);	        //set the message for sending back to the client
			sendToAllClients(messageToSend);	            //send arrayList back to client
		}
		
		if(operation.equals("getAllProducts"))	
		{
			ArrayList<ProductEntity> listOfAllProducts = new ArrayList<ProductEntity>();		//an arrayList that holds all the stores in the DB
			listOfAllProducts = this.getAllProductsFromDB();
			messageToSend.setMessage(listOfAllProducts);	        //set the message for sending back to the client
			sendToAllClients(messageToSend);	            //send arrayList back to client
		}
		if(operation.equals("deleteProduct"))	
		{
			//ArrayList<ProductEntity> listOfAllProducts = new ArrayList<ProductEntity>();		//an arrayList that holds all the stores in the DB
			String Reply;
			ProductEntity prd = (ProductEntity)messageToSend.getMessage();
			Reply = this.DeleteProductsFromDB(prd);
			messageToSend.setMessage(Reply);	        //set the message for sending back to the client
			sendToAllClients(messageToSend);	            //send arrayList back to client
		}
		if(operation.equals("UpdateProduct"))	
		{
			String Reply;
			ArrayList<ProductEntity> p=(ArrayList<ProductEntity>)messageToSend.getMessage();
		//	ProductEntity prd = (ProductEntity)messageToSend.getMessage();
			Reply = this.UpdateProductInDB(p.get(0),p.get(1));
			messageToSend.setMessage(Reply);	        //set the message for sending back to the client
			sendToAllClients(messageToSend);	            //send arrayList back to client
		}
		if(operation.equals("createProduct"))
		{
			
			ProductEntity product = (ProductEntity)messageToSend.getMessage();
			if((this.insertProduct(product, client)).equals("Success"))	//check if asked to create a new product and check if it was create successfully
			{
				System.out.println("product added sucessesfuly to DB");
				messageToSend.setMessage("Added");
				sendToAllClients(messageToSend);
			}
			else
			{
				System.out.println("Failed to insert the product to DB");
				messageToSend.setMessage("Failed");
				sendToAllClients(messageToSend);
			}
			
		}
		
		if(operation.equals("complaint")) {
			ComplaintEntity complaint = (ComplaintEntity)messageToSend.getMessage();
			this.incomingFileName=complaint.getOrderID();
			if(this.complaint(complaint).equals("Success")) {
				System.out.println("complaint added");
			//	generalMessage = (String)("Added");
				messageToSend.setMessage("Added"); 		//set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
			else {
				System.out.println("complaint failed");
			//	generalMessage = (String)("failed");
				messageToSend.setMessage("failed"); //set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
		}
		
		if(operation.equals("downloadFile")) {
			
			System.out.println("Server downloading file sent from client");
			String filePath=(String)messageToSend.getMessage();
			filePath=filePath.substring(filePath.indexOf("!")+1,filePath.length());
			
		//	this.receiveFileFromClient("localhost", 5556);
			}
		}
		
		catch(Exception ex) {
			ex.printStackTrace();
			}
		
	
//		sendToAllClients(messageToSend);
		
	}
  
  
  public void insertNewCustomer(CustomerEntity ce) throws SQLException {
	  
	  try {
		  con=connectToDB();
		  System.out.println("Connection to Database succeeded");
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  System.out.println("Connection to Database failed");
	  }
	  
	  PreparedStatement ps=con.prepareStatement("INSERT INTO projectx.customers (Username,Password,UserID,SubscriptionDiscount,JoinTime,Credit) VALUES (?,?,?,?,?,?)");
	  ps.setString(1, ce.getUserName());
	  ps.setString(2, ce.getPassword());
	  ps.setLong(3, ce.getCustomerID());
	  ps.setString(4,ce.getSubscriptionDiscount().toString());
	  
	  Timestamp timestamp = new Timestamp(System.currentTimeMillis());		//get current time
//	  DateFormat df = new SimpleDateFormat("dd/MM/yy");
//      Date dateobj = new Date();
      ps.setTimestamp(5, timestamp);
//	  ps.setString(5, df.format(dateobj).toString());
	  ps.setLong(6, ce.getCreditCardNumber());
	  ps.executeUpdate();										//add new customer to Database
	
	  System.out.println("new customer added to Database");
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