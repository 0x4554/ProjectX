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
import java.io.*;
//import java.awt.image.BufferedImage;
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

import client.Client;

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
   * This method is for handling a complaint as a customer service worker
   * @param complaint the complaint
   * @return
 * @throws SQLException 
 * @throws ClassNotFoundException 
   */
  private String handleComplaint(ComplaintEntity complaint) throws SQLException, ClassNotFoundException
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
	  stmt.executeUpdate("UPDATE projectx.complaints SET Status = 'handled', Reply = '"+complaint.getStoreReply()+"',Compensation = "+complaint.getCompensation()+" WHERE Ordernum = " + complaint.getOrderID()+ "");
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  return retMsg = "faild to handle the complaint.";
	  }
	  /// **** compensate the customer ****/////
	  return retMsg = "complaint has been handled";
  }
  /**
   * This method gets all the orders with complaints on them
   * @param listOfcomplaints
   * @return	arrayList of orders
 * @throws ClassNotFoundException connection to DB
 * @throws SQLException for the SQL 
 * @throws IOException for the file converting
   */
  private ArrayList<OrderEntity> getAllComplaintOrders(ArrayList<ComplaintEntity> listOfcomplaints) throws ClassNotFoundException, SQLException, IOException
  {
	  Statement stmt,stmt2,stmt3,stmt4,stmt5;
	  ResultSet rs,rs2,rs3,rs4,rs5;
	  OrderEntity order;
	  DeliveryEntity delivery = null;
	  StoreEntity store;
	  ProductEntity product;
	  ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
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
	  stmt3=con.createStatement();
	  stmt4=con.createStatement();
	  stmt5=con.createStatement();
	 
	 for(ComplaintEntity complaint : listOfcomplaints)
	 {
		
		 rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE Ordernum = "+complaint.getOrderID()+"");	//get all orders matching the complaint's order ID
		 while(rs.next())
		  {
			  order = new OrderEntity();
				
				//** get the delivery details **//
		rs2 = stmt2.executeQuery("SELECT * FROM projectx.delivery WHERE OrderID ="+rs.getInt(1)+"" );	//get the delivery data for the order
		while(rs2.next())
			delivery = new DeliveryEntity(rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getTimestamp(6));		//create a new delivery using the data
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
		
		if(rs.getString(7).equals(OrderEntity.CashOrCredit.cash.toString()))
			order.setPaymendMethod(OrderEntity.CashOrCredit.cash);
		else
			order.setPaymendMethod(OrderEntity.CashOrCredit.credit);

		listOfOrders.add(order); //add the product from the data base to the list
		
//		stmt.close();				//close all statements for reuse
//		stmt2.close();
//		stmt3.close();
//		stmt4.close();
//		stmt5.close();
		  }
	 }
	 return listOfOrders;
	 
	  
  }

  /**
   * This method gets the complaints from the DB
   * @return	arrayList of complaints
   * @throws SQLException	for SQL 
   * @throws ClassNotFoundException DB connection
   * @throws IOException	file converting
   */
  private ArrayList<ComplaintEntity> getComplaints() throws SQLException, IOException, ClassNotFoundException
  {
	  ArrayList<ComplaintEntity> listOfComplaints = new ArrayList<ComplaintEntity>();
	  ComplaintEntity complaint;
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
		 
		 stmt=con.createStatement();
		 rs = stmt.executeQuery("Select * FROM projectx.complaints");
		 while(rs.next())
		 {
			 complaint = new ComplaintEntity();									//create new product
			 complaint.setOrderID(rs.getInt(1));
			 complaint.setDescription(rs.getString(2));
			 if(rs.getString(3).equals("processing"))
					 complaint.setStatus(ComplaintEntity.Status.processing);
			 else 
				 complaint.setStatus(ComplaintEntity.Status.handled);
			 
			 					////***handle file***////
			 
				Blob b = con.createBlob(); 					//create blob
				byte[] image = null;
				if(rs.getBlob(4) != null)					//check if file is added to the complaint
				{
				b=rs.getBlob(4);							////get blob from DB
		  		InputStream is=b.getBinaryStream();	  		//get binary Stream for blob and than use FilesConverter.convertInputStreamToByteArray(InputStream)
		  		image = FilesConverter.convertInputStreamToByteArray(is);
		  		complaint.setFile(image);					//set the file
				}
				else 
					complaint.setFile(image);
		  		
		  		complaint.setStoreReply(rs.getString(5));
		  		complaint.setCompensation(rs.getDouble(6));
		  		

			 
			 listOfComplaints.add(complaint);
		 }
		 return listOfComplaints;				
  }
/**
   * This method gets all of the orders in the DB OR  a specific store's orders
   * @return	arrayList of orders in the DB
   * @param the store name OR null if for all stores
 * @throws ClassNotFoundException 
 * @throws SQLException 
 * @throws IOException for file conversion
   */
  private ArrayList<OrderEntity> getAllOrders(ArrayList<String> storeNameQuarter) throws ClassNotFoundException, SQLException, IOException
  {
	///Arraylist recieved in the form of ("all" if the all store OR "<the store name>" for a specific store,<"number"> for the wanted quarter ////
	  String[] firstQuarter = {"January","February","March"};
	  String[] secondQuarter = {"April","May","June"};
	  String[] thirdQuarter = {"July","August","September"};
	  String[] forthQuarter = {"October","November","December"};
	  String[] askedQuarter = null;
	  
	  
	  Statement stmt,stmt2,stmt3,stmt4,stmt5;
	  OrderEntity order;
	  DeliveryEntity delivery = null;
	  ProductEntity product;
	  StoreEntity store;
	  ResultSet rs,rs2,rs3,rs4,rs5;
	  ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
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
	  stmt3=con.createStatement();
	  stmt4=con.createStatement();
	  stmt5=con.createStatement();
	  
	  //determine which quarter is asked by the client
	  if(storeNameQuarter.get(1).equals("1"))
		  askedQuarter=firstQuarter;
	  else if (storeNameQuarter.get(1).equals("2"))
		  askedQuarter=secondQuarter;
	  else if (storeNameQuarter.get(1).equals("3"))
		  askedQuarter=thirdQuarter;
	  else if (storeNameQuarter.get(1).equals("4"))
		  askedQuarter=forthQuarter;
	  String storeOrders = "";
	  if(!storeNameQuarter.get(0).equals("all"))							//check if asked for all orders OR a specific store orders
	  {
		  storeOrders = "AND A.BranchID = (SELECT BranchID FROM projectx.store WHERE BranchName = '"+storeNameQuarter.get(0)+"') ";
	  }
	  rs = stmt.executeQuery("SELECT * FROM projectx.order A "
	  		+ "WHERE"
	  		+ "( monthname(A.OrderTime)= '"+askedQuarter[0]+"' OR"
	  		+ " monthname(A.OrderTime)= '"+askedQuarter[1]+"' OR"
	  		+ " monthname(A.OrderTime)= '"+askedQuarter[2]+"' )"+storeOrders);
	  while(rs.next())
	  {
		  order = new OrderEntity();
			
			//** get the delivery details **//
	rs2 = stmt2.executeQuery("SELECT * FROM projectx.delivery WHERE OrderID ="+rs.getInt(1)+"" );	//get the delivery data for the order
	while(rs2.next())
		delivery = new DeliveryEntity(rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getTimestamp(6));		//create a new delivery using the data
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
	
	if(rs.getString(7).equals(OrderEntity.CashOrCredit.cash.toString()))
		order.setPaymendMethod(OrderEntity.CashOrCredit.cash);
	else
		order.setPaymendMethod(OrderEntity.CashOrCredit.credit);

	listOfOrders.add(order); //add the product from the data base to the list
	  }

	  return listOfOrders;
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
		dominantColor = "AND ProductDominantColor = '"+ requests.get(3)+"'";
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
////////********////////		 InputStream is = productImage.getBinaryStream();
		 
///////*********////////		 product.setProductImage(FilesConverter.convertInputStreamToByteArray(is)); 		//set the input stream to a byte array
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
	  stmt.executeUpdate("UPDATE projectx.order SET OrderStatus = 'cancelled' WHERE Ordernum = " + OrderID+ "");
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
 * @throws SQLException 	sql error
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
				delivery = new DeliveryEntity(rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getTimestamp(6));		//create a new delivery using the data
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
		Statement stmt,stmt2,stmt3,stmt4,stmt5,stmt6;
		ResultSet rs,rs2,rs3,rs4,rs5,rs6; 				//for the (order,delivery,list of products,store)
		
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
		stmt6 = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE UserID ='"+userID+"'"); //get all the stores (ID,Name,managerID) in the stores table from the data base
	
		while (rs.next())
		{
			order = new OrderEntity();
			
					//** get the delivery details **//
			rs2 = stmt2.executeQuery("SELECT * FROM projectx.delivery WHERE OrderID ="+rs.getInt(1)+"" );	//get the delivery data for the order
			while(rs2.next())
				delivery = new DeliveryEntity(rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getTimestamp(6));		//create a new delivery using the data
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
			Map<Integer,Double> storeDiscoutsSales;
			rs5 = stmt5.executeQuery("SELECT * FROM projectx.store WHERE BranchID = "+rs.getInt(10)+"");
			while(rs5.next())
			{
				store = new StoreEntity(rs5.getInt(1), rs5.getString(2), rs5.getInt(3));
				storeDiscoutsSales = new HashMap<Integer,Double>();
				rs6 = stmt6.executeQuery("SELECT ProductID,ProductPrice FROM projectx.discount WHERE BranchID = "+rs.getInt(10)+""); 		//get store's discounts
				while(rs6.next())
				{
					storeDiscoutsSales.put(rs6.getInt(1), rs6.getDouble(2));
				}
				store.setStoreDiscoutsSales(storeDiscoutsSales);
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
//		int orderCounter = 0;
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
			
					//*** A query for getting the next  Auto_Icrement key (the inserted order ID  + 1 )	****///
			ResultSet rs = stmt.executeQuery("SELECT `AUTO_INCREMENT`\r\n" + 
					"FROM  INFORMATION_SCHEMA.TABLES\r\n" + 
					"WHERE TABLE_SCHEMA = 'projectx'\r\n" + 
					"AND   TABLE_NAME   = 'order';");
			if(rs.next());
			
					//** now insert all the products in order to the productsInOrder table **//
			PreparedStatement ps3;
			for(ProductEntity product : newOrder.getProductsInOrder())
			{
				ps3 = con.prepareStatement("INSERT INTO projectx.productsinorder (OrderNum,ProductID) VALUES (?,?)"); //prepare a statement
				ps3.setInt(1, rs.getInt(1)-1);
				ps3.setInt(2, product.getProductID());
				ps3.executeUpdate();
			}
			
			if(newOrder.getOrderPickup().equals(OrderEntity.SelfOrDelivery.delivery))		//if the order has a delivery
			{
						//** insert all the order's delivery details in to the delivery table **//
				newOrder.getDeliveryDetails().setOrderID(rs.getInt(1)-1);	//set the orderID for the delivery
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

		ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.user WHERE Username = '" + data[0] + "'"); //query to check if such a user exists
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
					PreparedStatement ps = con.prepareStatement("UPDATE user SET LoginAttempts = 0  WHERE Username = ?"); //prepare a statement
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
					PreparedStatement ps = con.prepareStatement("UPDATE user SET LoginAttempts = ? WHERE Username = ?"); //prepare a statement
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
  public String createNewProduct(ProductEntity newProduct) throws SQLException, ClassNotFoundException
  {
	//  String [] data= msg.split("~");	//split the data
	  Statement stmt;
	  String retval = "";
//	  ArrayList<String> retval = new ArrayList<String>();
	  try
	    {
	    con = connectToDB();	//call method to connect to DB
	      
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	     
	    }
//	    System.out.println("Message received: " + msg + " from " + client);
	    try
	    {//first check if the ID already exists in the DB
	    stmt = con.createStatement();
	    PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.product (ProductName,ProductType,ProductPrice,ProductDescription,ProductImage,ProductDominantColor) VALUES (?,?,?,?,?,?)");
	    ps.setString(1, newProduct.getProductName());
	    ps.setString(2, newProduct.getProductType());
	    ps.setDouble(3, newProduct.getProductPrice());
	    ps.setString(4, newProduct.getProductDescription());
	    
	  
	    InputStream is =FilesConverter.convertByteArrayToInputStream(newProduct.getProductImage());		//convert byte array to input stream
	    ps.setBlob(5, is);							//set blob for image 
	    
	    ps.setString(6, newProduct.getProductDominantColor());
	    ps.executeUpdate();
	    retval="Product was added successfully";
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    	retval="Something went wrong";
	    }
	    return retval;
//	    ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE ProductID = '" +data[0]+"'");	//prepare a statement
//	    if(!(rs.next()))	//if no such ID exists in the DB, Insert the new data
//	    {
//		    PreparedStatement ps = con.prepareStatement("INSERT INTO product (ProductID,ProductName,ProductType) VALUES (?,?,?)");	//prepare a statement
//		    ps.setString(1,data[0]);	//insert parameters into the statement
//		    ps.setString(2, data[1]);
//		    ps.setString(3, data[2]);
//		    ps.executeUpdate();
//		    
//		    return "Success";
//	    }
	    
//	    else	//if such Id already exists return failed String
//	    	return "Failed";
	    
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
  	public InputStream getInputStreamFromDB(String orderNum) throws SQLException{
  		InputStream is = null;
  		Statement stmt;
  		
  		try {
  		con=connectToDB();									//Achieve connection to the data base
  		}
  		catch(Exception e) {
  			System.out.println("failed connecting to db");	
  		}
  		
  		try {
  	  		Blob b = con.createBlob();							//Object to contain the data from the data base

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
  	 * this method converts InputStream to a File
  	 * 
  	 * @param is - InputStream to convert
  	 * @throws IOException
  	 */
/*  	public void convertInputStreamToFile(InputStream is) throws IOException {
  		
  	OutputStream outputStream = new FileOutputStream(new File("/home/mdhttr/Documents/converted/img.jpg"));		//new file's output 

	int read = 0;
	byte[] bytes = new byte[1024];

	while ((read = is.read(bytes)) != -1) {				//convertion proccess
		outputStream.write(bytes, 0, read);
		}
  	}				*/
  	
  	
  	/**
  	 * this method converts InputStream object into array of bytes(byte[])
  	 * 
  	 * 
  	 * @param inStrm - InputStream to convert
  	 * @return  - array of bytes
  	 * @throws IOException 
  	 */
  /*	public byte[] convertInputStreamToByteArray(InputStream inStrm) throws IOException {
  		
  		byte [] retByteArray=null;
  		byte[] buff = new byte[4096];
  		int bytesRead = 0;

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        
        while((bytesRead = inStrm.read(buff)) != -1) {							//read the entire stream
            bao.write(buff, 0, bytesRead);
         }

         retByteArray = bao.toByteArray();
  
  		return retByteArray;
  	}								*/
    	
  	
  	
  	
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
 * @throws IOException  for the file convertion
   */
  private ArrayList<ProductEntity> getCatalog() throws ClassNotFoundException, SQLException, IOException
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
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.catalog A , projectx.product B WHERE A.ProductID = B.ProductID");	//get all the products in the catalog table from the data base
	  
	  while(rs.next())
	  {
		  Blob b = con.createBlob();
		  b=rs.getBlob(6);
		  InputStream is = b.getBinaryStream();
		  byte[] image = FilesConverter.convertInputStreamToByteArray(is);
		 product = new ProductEntity(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getString(7),image);	//create a new instance of a product
		 listOfProducts.add(product);	//add the product from the data base to the list
	  }
	  return listOfProducts;
  }
  
  /**
   * Insert product to catalog
   * @param prd
   * @return
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public String addToCatalog(ProductEntity prd) throws SQLException, ClassNotFoundException/**lanaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa*/
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
	    if(!(rs.next()))																						//if such ID exists in the DB, Insert the new data
	    {
		    PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.catalog (ProductID,ProductName,ProductType,ProductPrice,ProductDescription,ProductColor,ProductImage) VALUES (?,?,?,?,?,?,?)");	//prepare a statement
		    ps.setInt(1, prd.getProductID());																			//insert parameters into the statement
		    ps.setString(2, prd.getProductName());
		    ps.setString(3, prd.getProductType());
		    ps.setDouble(4,prd.getProductPrice());
		    ps.setString(5,prd.getProductDescription());
		    
//		    ps.setBlob(7, FilesConverter.convertByteArrayToInputStream(prd.getImage1()));
		    
		   // FilesConverter.convertByteArrayToInputStream(prd.getImage1());
		    /*
	  		InputStream is=b.getBinaryStream();
	  		  prd.setProductImage(convertInputStreamToByteArray(is));
	  		  prd.setProductImage(FilesConverter.convertInputStreamToByteArray(is));
		      ps.setBlob(6, prd.getImage1());
		    */
		    ps.setString(6, prd.getProductDominantColor());
		    ps.executeUpdate();
		    return "Success";
	    }
	  return "Inserting product to catalog failed";   
  }
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
	  ResultSet rs = stmt.executeQuery("SELECT * FROM catalog WHERE ProductID = '" +prd.getProductID()+"'");	//prepare a statement
	    if((rs.next()))																						//if such ID exists in the DB, Insert the new data
	    {
		    PreparedStatement ps = con.prepareStatement("DELETE FROM projectx.catalog WHERE ProductID = ?");	//prepare a statement
		    ps.setInt(1, prd.getProductID());																			//insert parameters into the statement
		    ps.executeUpdate();
		    return "Success";
	    }
	  return "Product was not found";   
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
  
  
	public String complaint(ComplaintEntity details) throws SQLException, ClassNotFoundException {

		Statement stmt, stmt2;
		ResultSet rs, rs2;
		try
		{
			con = connectToDB(); //call method to connect to DB

			if (con != null)
				System.out.println("Connection to Data Base succeeded");
		}

		catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
		}

		stmt = con.createStatement();
		stmt2 = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE Ordernum = '" + details.getOrderID() + "'"); //prepare a statement
	
		rs2 = stmt2.executeQuery("SELECT count(1) FROM projectx.complaints WHERE OrderNum = " + details.getOrderID() + "");
		if(rs2.next())
			if (rs2.getInt(1) == 1)			//check if a complaint for this order was already filed
			{
				return "Complaint was already filed";
			} 
		
		else if ((rs.next())) //if such ID exists in the DB, Insert the new data
		{
			PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.complaints (OrderNum,Description,Status,File,Compensation) VALUES (?,?,?,?,?)"); //prepare a statement
			ps.setInt(1, details.getOrderID()); //insert parameters into the statement
			ps.setString(2, details.getDescription());
			ps.setString(3, details.getStatus().toString());
						if (details.getFile() != null) //if a file was addded
			{
				InputStream inStrm = FilesConverter.convertByteArrayToInputStream(details.getFile());
				ps.setBlob(4, inStrm);
			} else
				ps.setNull(4, java.sql.Types.NULL);
			ps.setDouble(5, 0);

			ps.executeUpdate();

			return "Success";
		}
		return "Order does not exist";
	}

  
  public ArrayList<Integer> getProductsIDS() throws SQLException, ClassNotFoundException
  {
	  ArrayList<Integer> listOfProducts=new ArrayList<Integer>(); 
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
	  ResultSet rs = stmt.executeQuery("SELECT productID FROM projectx.catalog");	//get all the products in the catalog table from the data base
	  while(rs.next())
	  {
		 listOfProducts.add(rs.getInt(1));	//add the product from the data base to the list
	  }
	  return listOfProducts;
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
	  
	  Blob b=con.createBlob();

	  if(rs.next())
	  {
			// prd = new ProductEntity(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getBlob(6));	//create a new instance of a product
             // product.add(prd);
		  prd.setProductID(rs.getInt(1));
		  prd.setProductName(rs.getString(2));
		  prd.setProductType(rs.getString(3));
		  prd.setProductPrice(rs.getDouble(4));
		  prd.setProductDescription(rs.getString(5));
		
		 /* 
		  b=rs.getBlob(6);
  		  InputStream is=b.getBinaryStream();
  		  prd.setProductImage(convertInputStreamToByteArray(is));
  		  prd.setProductImage(FilesConverter.convertInputStreamToByteArray(is));
		  */
		  
		  
  		  prd.setProductDominantColor(rs.getString(7));  
          return prd;
	  }
	  else 
	         return null;  
  }
  

  
/**
 * This method return's the discount's from the table in the data base 
 * @param storeID
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
		//prd = new ProductEntity(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getString(5),rs.getString(6));	//create a new instance of a product
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
		
		if(operation.equals("getUserDetails")) {
			
			MessageToSend reply;

			CustomerEntity cust=this.getCustomerDetails((String)messageFromClient);
			if(cust!=null) {
			reply=new MessageToSend(cust, "customerExist");
			client.sendToClient(reply);
			}
			else {
				reply=new MessageToSend(null,"noCustomer");
				client.sendToClient(reply);
			}
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
		
		if(operation.equals("getCatalogByID"))/*get the discounts Hash Map for a specific store*/
		{
			ArrayList<Integer> listOfProducts = new ArrayList<Integer>();	//an arrayList that holds all the products in the catalog
			listOfProducts= getProductsIDS();                                               //get products from the catalog table in the data base
			if(listOfProducts!=null)                                                               //Success
			{
			messageToSend.setMessage(listOfProducts);		                        //set the message for sending back to the client
			sendToAllClients(messageToSend);
			}
			else {
				System.out.println("Operation failed");                                    //fail to fulfill the operation
				messageToSend.setMessage(null);                                          //set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
		}
		
		
		if(operation.equals("cancelRequest"))
		{
			String retMsg = cancelRequest((Integer)messageFromClient);
			messageToSend.setMessage(retMsg);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("getCancelRequests"))		//get all the orders which have a cancel request
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getCancelRequests();
			messageToSend.setMessage(listOfOrders);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("cancelOrder"))				//for store manger canceling an order
		{
			String retMsg = cancelOrder((Integer)messageFromClient);
			messageToSend.setMessage(retMsg);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("addProductToCatalog"))/****************************works**********************************lana*/
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
		if(operation.equals("deleteProductFromCatalog"))/**************************works*********************************lana*/
		{
			ProductEntity product;
			product =  (ProductEntity)messageToSend.getMessage();
			
			if(this.deleteProductFromCatalog(product).equals("Success")) {
				
				System.out.println("product deleted sucessesfuly from the catalog DB");
				messageToSend.setMessage("Deleted"); 		//set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
			else {
				System.out.println("Deletion failed");
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
			
							///Arraylist recieved in the form of ("all" if the all store OR "<the store name>" for a specific store,<int> for the wanted quarter ////
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getAllOrders((ArrayList<String>)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			sendToAllClients(messageToSend);
		}
	
//		if(operation.equals("addProductToCatalog"))
//		{
//			
//		}
		
		if(operation.equals("createAccount")) {	
			CustomerEntity custen=(CustomerEntity)messageFromClient;
			try {
			this.insertNewCustomer(custen);
			MessageToSend toClient=new MessageToSend("added","retval");
			client.sendToClient(toClient);
			
			}
			catch(Exception e) {
				MessageToSend toClient = new MessageToSend("failed","retval");
				client.sendToClient(toClient);
			}
			
		}
		
		
		if(operation.equals("getAllStores"))				//gets all the stores in the DB
		{
			ArrayList<StoreEntity> listOfAllStores = new ArrayList<StoreEntity>();		//an arrayList that holds all the stores in the DB
			listOfAllStores = getAllstoresFromDB();
			messageToSend.setMessage(listOfAllStores);		//set the message for sending back to the client
			sendToAllClients(messageToSend);
		}
//		if(operation.equals("addProductToCatalog"))
//		{
//			
//		}
		
		if(operation.equals("exitApp"))					//for when a user exits the app
		{
			if((String)messageFromClient!=null) {
				this.terminateConnection((String)messageFromClient);	//calls a method to remove the user from the connected list
				System.out.println("client "+(String)messageFromClient+" logged out (App closed)");
		
			}
		}
		
		if(operation.equals("logOut")) {
			this.terminateConnection((String)messageFromClient);
		}
		
		if(operation.equals("login"))					//for when a user tries to log in
		{
			retval = this.login(client,(String)messageFromClient);
			messageToSend.setMessage(retval);	//set the message for sending back to the client
			sendToAllClients(messageToSend);	//send arraylist back to client
		}
		
		if(operation.equals("getProduct"))	//check***********fix*******to***********lana*******object***********************//
		{
		//getProductFromDB
			ProductEntity produ;
			int productid = (int)messageToSend.getMessage();
			produ= this.getProduct(client,productid);	//get the product
			messageToSend.setMessage(produ);	        //set the message for sending back to the client
			sendToAllClients(messageToSend);	            //send arrayList back to client
		}
		
		if(operation.equals("createProduct"))
		{
			String retmsg = "";
			retmsg = createNewProduct((ProductEntity)messageFromClient);
			messageToSend.setMessage(retmsg);
			sendToAllClients(messageToSend);
		//	String str =(String)messageFromClient;
		//	str = str.substring(1,str.length());
//			//String str=messageFromClient
//			if((this.insertProduct((String)messageFromClient, client)).equals("Success"))	//check if asked to create a new product and check if it was create successfully
//			{
//				generalMessage = new String("Product was successfully added to the DataBase");
//			}
//			
//			else
//			{
//				generalMessage = new String("Product was not added to the DataBase.\n(Product ID already exists)");
//			}
//			
//			sendToAllClients(generalMessage);	//send string back to client
		}
		if(operation.equals("handleComplaint"))
		{
			String retmsg = "";
			try
			{
				retmsg = handleComplaint((ComplaintEntity)messageFromClient);
			}
			catch(Exception e)
			{
				retmsg="We are sorry,Something went wrong, Please try again later.";
				e.printStackTrace();
			}
			messageToSend.setMessage(retmsg);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("getComplaintOrders"))	//for getting all the orders with comlpaints
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getAllComplaintOrders((ArrayList<ComplaintEntity>)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			sendToAllClients(messageToSend);
		}
		if(operation.equals("getComplaints"))		//for getting all complaints
		{
			ArrayList<ComplaintEntity> listOfComplaints = new ArrayList<ComplaintEntity>();		//an arrayList that holds all the stores in the DB
			listOfComplaints = getComplaints();
			messageToSend.setMessage(listOfComplaints);		//set the message for sending back to the client
			sendToAllClients(messageToSend);
		}
		if(operation.equals("complaint")) {
			ComplaintEntity complaint = (ComplaintEntity)messageToSend.getMessage();
			this.incomingFileName=complaint.getOrderID();
			String ret="";
			ret =	this.complaint(complaint);
			if(ret.equals("Success")) {
				System.out.println("complaint added by customer");
			//	generalMessage = (String)("Added");
				messageToSend.setMessage("Success"); 		//set the message for sending back to the client
				sendToAllClients(messageToSend);
			}
			else if(ret.equals("Complaint was already filed"))
			{
				System.out.println("complaint failed");
				messageToSend.setMessage("Complaint was already filed");
				sendToAllClients(messageToSend);
			}
			else if(ret.equals("Order does not exist")){
				System.out.println("failed to add complaint");
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
  
  
  private CustomerEntity getCustomerDetails(String custName) throws SQLException {
	// TODO Auto-generated method stub
	  try {
		  con=connectToDB();
		  System.out.println("Connection to Database succeeded");
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  System.out.println("Connection to Database failed");
	  }
	  
	  Statement s=con.createStatement();
	  ResultSet rs = s.executeQuery("SELECT * FROM projectx.customers WHERE Username='"+custName+"'");
	  if(rs.next()) {
		  CustomerEntity ce=new CustomerEntity();
		  ce.setUserName(rs.getString(1));
		  ce.setID(rs.getLong(3));
		  ce.setSubscriptionDiscount(rs.getString(4));
		  ce.setAddress(rs.getString(5));
		  ce.setEmailAddress(rs.getString(6));
		  ce.setPhoneNumber(rs.getString(7));
		  ce.setCreditCardNumber(Long.parseLong(rs.getString(9)));
		 
	      System.out.println("customer was pulled from database");
	      return ce;
	  }
	  
	  else {
		  System.out.println("failed to fine customer "+custName);
	  }
	  
	return null;
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
	  
	  PreparedStatement ps=con.prepareStatement("INSERT INTO projectx.customers (Username,Password,UserID,Subscription,Address,Email,PhoneNumber,JoinTime,CreditCard) VALUES (?,?,?,?,?,?,?,?,?)");
	  ps.setString(1, ce.getUserName());
	  ps.setString(2, ce.getPassword());
	  ps.setLong(3, ce.getID());
	  ps.setString(4,ce.getSubscriptionDiscount().toString());
	  ps.setString(5, ce.getAddress());
	  ps.setString(6, ce.getEmailAddress());
	  ps.setString(7, ce.getPhoneNumber());
	  
	  DateFormat df = new SimpleDateFormat("dd/MM/yy");
      Date dateobj = new Date();
      
	  ps.setString(8, df.format(dateobj).toString());
	  ps.setLong(9, ce.getCreditCardNumber());
	  ps.executeUpdate();										//add new customer to Database
	  
	  ps=con.prepareStatement("INSERT INTO projectx.user (Username,Password,UserType,LoginAttempts) VALUES (?,?,?,?)");
	  ps.setString(1, ce.getUserName());
	  ps.setString(2, ce.getPassword());
	  ps.setString(3, "C");
	  ps.setInt(4, 0);
	  
	  ps.executeUpdate();
	  
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