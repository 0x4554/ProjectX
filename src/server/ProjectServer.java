// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import entities.CardEntity;
import entities.ComplaintEntity;
import entities.CustomerEntity;
import entities.DeliveryEntity;
import entities.OrderEntity;
import entities.ProductEntity;
import entities.StoreEntity;
import entities.StoreManagerEntity;
import entities.StoreWorkerEntity;
import entities.SurveyEntity;
import entities.UserInterface;
import entities.VerbalReportEntity;
import logic.ConnectedClients;
import logic.FilesConverter;
import logic.MessageToSend;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

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
	ServerMain.serverController.showMessageToUI(username + " logged out");
  }

  /**
   * This method get the verbal reports from the DB
   * @return arrayList of VerbalReportEntity
 * @throws SQLException create statement
 * @throws ClassNotFoundException  DB connection
 * @throws IOException  for the conversion
   */
  private ArrayList<VerbalReportEntity> getVerbalReports() throws SQLException, ClassNotFoundException, IOException
  {
	  Statement stmt;
	  ResultSet rs;
	  VerbalReportEntity report;
	  ArrayList<VerbalReportEntity> listOfReports = new ArrayList<VerbalReportEntity>();
	  try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
	  stmt = con.createStatement();
	  
	  rs = stmt.executeQuery("Select Report,Date FROM projectx.verbalreports");
	  while(rs.next())
	  {
		    report = new VerbalReportEntity();
			Blob b = con.createBlob(); 					//create blob
			b=rs.getBlob(1);							////get blob from DB
	  		InputStream is=b.getBinaryStream();	  		//get binary Stream for blob and than use FilesConverter.convertInputStreamToByteArray(InputStream)
	  		byte[] file = FilesConverter.convertInputStreamToByteArray(is);
		    report.setFile(file);
		    report.setDate(rs.getTimestamp(2));
		    listOfReports.add(report);
	  }
	  
	  return listOfReports;
	  
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
	  Statement stmt,stmt2;
	  String retMsg="";
	
	  try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
	  stmt = con.createStatement();
	  
	  try {
	  stmt.executeUpdate("UPDATE projectx.complaints SET Status = 'handled', Reply = '"+complaint.getStoreReply()+"',Compensation = "+complaint.getCompensation()+" WHERE Ordernum = " + complaint.getOrderID()+ "");
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  ServerMain.serverController.showMessageToUI(e.getMessage());
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
	  Statement stmt,stmt2,stmt3,stmt4,stmt5,stmt6;
	  ResultSet rs,rs2,rs3,rs4,rs5,rs6;
	  OrderEntity order;
	  DeliveryEntity delivery = null;
	  StoreEntity store;
	  ProductEntity product;
	  ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
	  try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
	 
	  stmt = con.createStatement();
	  stmt2 = con.createStatement();
	  stmt3=con.createStatement();
	  stmt4=con.createStatement();
	  stmt5=con.createStatement();
	  stmt6=con.createStatement();
	 
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
		Map<Integer,Double> storeDiscoutsSales;
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

		listOfOrders.add(order); //add the product from the data base to the list
		
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
  private ArrayList<ComplaintEntity> getComplaints(ArrayList<String> storeNameQuarter) throws SQLException, IOException, ClassNotFoundException
  {
	  ArrayList<ComplaintEntity> listOfComplaints = new ArrayList<ComplaintEntity>();
	///Arraylist recieved in the form of ("all" if the all store OR "<the store name>" for a specific store,<"number"> for the wanted quarter of "all" ////
	  String[] firstQuarter = {"January","February","March"};
	  String[] secondQuarter = {"April","May","June"};
	  String[] thirdQuarter = {"July","August","September"};
	  String[] forthQuarter = {"October","November","December"};
	  String[] askedQuarter = null;
	  String storeOrders="",where="",store="";
	  ComplaintEntity complaint;
		 Statement stmt;
		 ResultSet rs;
		 try
			{
				con = connectToDB(); //call method to connect to DB
				if (con != null)
				{
					System.out.println("Connection to Data Base succeeded");
					ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
				}
			} catch (SQLException e) //catch exception
			{
				System.out.println("SQLException: " + e.getMessage());
				ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
			}
		  //determine which quarter is asked by the client
		  if(storeNameQuarter.get(1).equals("1"))
			  askedQuarter=firstQuarter;
		  else if (storeNameQuarter.get(1).equals("2"))
			  askedQuarter=secondQuarter;
		  else if (storeNameQuarter.get(1).equals("3"))
			  askedQuarter=thirdQuarter;
		  else if (storeNameQuarter.get(1).equals("4"))
			  askedQuarter=forthQuarter;
		 stmt=con.createStatement();
		  if(!storeNameQuarter.get(0).equals("all"))							//check if asked for all orders OR a specific store orders
		  {
			  storeOrders = "AND A.BranchID = (SELECT BranchID FROM projectx.store WHERE BranchName = '"+storeNameQuarter.get(0)+"') ";
			  where="WHERE";
			  store = storeOrders.substring(3);
		  }
		  if(storeNameQuarter.get(1).equals("all"))
		  {
			  rs = stmt.executeQuery("Select * FROM projectx.complaints "+where+" "+store);
		  }
		  else
		  {
		  rs = stmt.executeQuery("SELECT B.Ordernum,B.Description,B.Status,B.File,B.Reply,B.Compensation,B.FiledOn"
		  		+ " FROM"
		  		+ " projectx.complaints B ,projectx.order A "
		  		+ "WHERE"
		  		+ " ( monthname(B.FiledOn)= '"+askedQuarter[0]+"' OR"
		  		+ " monthname(B.FiledOn)= '"+askedQuarter[1]+"' OR"
		  		+ " monthname(B.FiledOn)= '"+askedQuarter[2]+"' )"
		  				+ " AND A.Ordernum = B.Ordernum "+storeOrders);
		  }
		
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
		  		complaint.setFiledOn(rs.getTimestamp(7));
		  		

			 
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
	///Arraylist recieved in the form of ("all" if the all store OR "<the store name>" for a specific store,<"number"> for the wanted quarter of "all" ////
	  String[] firstQuarter = {"January","February","March"};
	  String[] secondQuarter = {"April","May","June"};
	  String[] thirdQuarter = {"July","August","September"};
	  String[] forthQuarter = {"October","November","December"};
	  String[] askedQuarter = null;
	  
	  
	  Statement stmt,stmt2,stmt3,stmt4,stmt5,stmt6;
	  OrderEntity order;
	  DeliveryEntity delivery = null;
	  ProductEntity product;
	  StoreEntity store;
	  ResultSet rs,rs2,rs3,rs4,rs5,rs6;
	  ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
	  try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
	  stmt = con.createStatement();
	  stmt2 = con.createStatement();
	  stmt3=con.createStatement();
	  stmt4=con.createStatement();
	  stmt5=con.createStatement();
	  stmt6=con.createStatement();
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
	  String AND = "AND";
	  String WHERE = "";
	  if(!storeNameQuarter.get(0).equals("all"))							//check if asked for all orders OR a specific store orders
	  {
		  storeOrders = " A.BranchID = (SELECT BranchID FROM projectx.store WHERE BranchName = '"+storeNameQuarter.get(0)+"') ";
	  }
	  if(storeNameQuarter.get(1).equals("all") )
	  {
		  if(!storeNameQuarter.get(0).equals("all"))
		  {
			  WHERE = "WHERE";
		  }
		  rs = stmt.executeQuery("SELECT * FROM projectx.order A "+WHERE+storeOrders);
	  }
	  else
	  {
	  rs = stmt.executeQuery("SELECT * FROM projectx.order A "
	  		+ "WHERE"
	  		+ "( monthname(A.OrderTime)= '"+askedQuarter[0]+"' OR"
	  		+ " monthname(A.OrderTime)= '"+askedQuarter[1]+"' OR"
	  		+ " monthname(A.OrderTime)= '"+askedQuarter[2]+"' )"+ AND +storeOrders);
	  }
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
	Map<Integer,Double> storeDiscoutsSales;
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
				{
					System.out.println("Connection to Data Base succeeded");
					ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
				}
			} catch (SQLException e) //catch exception
			{
				System.out.println("SQLException: " + e.getMessage());
				ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
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
			 if(rs.getBlob(6) != null)
			 {
			 productImage = rs.getBlob(6);
			 
			 InputStream is = productImage.getBinaryStream();
	  		 
	 		 product.setProductImage(FilesConverter.convertInputStreamToByteArray(is)); 		//set the input stream to a byte array
			 }
	 		 product.setProductDominantColor(rs.getString(7));
	  		 listOfProducts.add(product);									//add the product to the list
	  	 }
	  	 return listOfProducts;	
    }
 /**
   * This method cancel an order and sets its status to canceled in the DB
   * @param OrderID	the order to cancel
   * @return
 * @throws ClassNotFoundException 
 * @throws SQLException 
   */
  private String cancelOrder(ArrayList<String> OrderID_refund) throws ClassNotFoundException, SQLException
  {			//receives an arrayList {order ID , refund amount }
	  Statement stmt;
	  String retMsg="";
	  try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
	  stmt = con.createStatement();
	  try {
	  stmt.executeUpdate("UPDATE projectx.order SET OrderStatus = 'cancelled', TotalPrice = TotalPrice - "+Double.parseDouble(OrderID_refund.get(1))+", Refund = "+Double.parseDouble(OrderID_refund.get(1))  +" WHERE Ordernum = " + Integer.parseInt(OrderID_refund.get(0))+ "");
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  ServerMain.serverController.showMessageToUI( e.getMessage());
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
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
	  try {
	  stmt = con.createStatement();
	  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		PreparedStatement ps = con.prepareStatement("UPDATE projectx.order SET OrderStatus = ? , CancelRequestTime = ? WHERE Ordernum = ?"); //prepare a statement
		ps.setString(1, OrderEntity.OrderStatus.cancel_requested.toString());
		ps.setTimestamp(2, timestamp);
		ps.setInt(3, OrderID);
		ps.executeUpdate();
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
		  ServerMain.serverController.showMessageToUI( e.getMessage());
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
  private ArrayList<OrderEntity> getCancelRequests(String BranchID) throws SQLException, ClassNotFoundException, IOException
  {
	  ArrayList<OrderEntity> listOfOrdersFromDB = new ArrayList<OrderEntity>();
		OrderEntity order;
		StoreEntity store;
		DeliveryEntity delivery = null;
		ProductEntity product;
		String specificStore="";
		Statement stmt,stmt2,stmt3,stmt4,stmt5;
		ResultSet rs,rs2,rs3,rs4,rs5; 				//for the (order,delivery,list of products,store)
		try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
		stmt = con.createStatement();
		stmt2 = con.createStatement();
		stmt3 = con.createStatement();
		stmt4 = con.createStatement();
		stmt5 = con.createStatement();
		if(!BranchID.equals("all"))		//if a specific store is asked
		{
			specificStore = "AND BranchID = "+Integer.parseInt(BranchID);
		}
	    rs = stmt.executeQuery("SELECT * FROM projectx.order WHERE OrderStatus = 'cancel_requested'"+specificStore); //get all the stores (ID,Name,managerID) in the stores table from the data base
		
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
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
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
	 * 
	 * @param newOrder
	 *            the new order
	 * @return messages
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private ArrayList<String> createNewOrder(OrderEntity newOrder) throws SQLException, ClassNotFoundException {
		ArrayList<String> returnMessage = new ArrayList<String>();
		Statement stmt;
		try
		{
			con = connectToDB(); //call method to connect to DB

			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		}

		catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
			e.printStackTrace();
		}

		stmt = con.createStatement();
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO projectx.order (UserID,OrderTime,OrderCard,PickupMethod,OrderStatus,OrderPaid,TotalPrice,ReceiveTimestamp,BranchID,PaymentMethod) VALUES (?,?,?,?,?,?,?,?,?,?)"); //prepare a statement
		ps.setString(1, newOrder.getUserName());

		Timestamp time = new Timestamp(System.currentTimeMillis()); //get the time and date the order was placed on
		ps.setTimestamp(2, time);

		if (newOrder.getCard() != null) //if there is a card
			ps.setString(3, newOrder.getCard().getText());
		else
			ps.setString(3, null);
		ps.setString(4, newOrder.getOrderPickup().toString());
		ps.setString(5, newOrder.getStatus().toString());

		if (newOrder.getPaid()) //if paid
			ps.setInt(6, 1);
		else
			ps.setInt(6, 0);

		ps.setDouble(7, newOrder.getTotalPrice());
		ps.setTimestamp(8, newOrder.getReceivingTimestamp());
		ps.setInt(9, newOrder.getStore().getBranchID());
		if (newOrder.getPaymendMethod().toString().equals(OrderEntity.CashOrCredit.cash.toString()))
			ps.setString(10, OrderEntity.CashOrCredit.cash.toString());
		else
			ps.setString(10, OrderEntity.CashOrCredit.credit.toString());
		ps.executeUpdate();

		//*** A query for getting the next  Auto_Icrement key (the inserted order ID  + 1 )	****///
		ResultSet rs = stmt.executeQuery("SELECT `AUTO_INCREMENT`\r\n" + "FROM  INFORMATION_SCHEMA.TABLES\r\n" + "WHERE TABLE_SCHEMA = 'projectx'\r\n" + "AND   TABLE_NAME   = 'order';");
		if (rs.next())
			;

		//** now insert all the products in order to the productsInOrder table **//
		PreparedStatement ps3;
		for (ProductEntity product : newOrder.getProductsInOrder())
		{
			ps3 = con.prepareStatement("INSERT INTO projectx.productsinorder (OrderNum,ProductID) VALUES (?,?)"); //prepare a statement
			ps3.setInt(1, rs.getInt(1) - 1);
			ps3.setInt(2, product.getProductID());
			ps3.executeUpdate();
		}

		if (newOrder.getOrderPickup().equals(OrderEntity.SelfOrDelivery.delivery)) //if the order has a delivery
		{
			//** insert all the order's delivery details in to the delivery table **//
			newOrder.getDeliveryDetails().setOrderID(rs.getInt(1) - 1); //set the orderID for the delivery
			PreparedStatement ps2 = con.prepareStatement("INSERT INTO projectx.delivery (OrderID,DeliveryAddress,RecipientName,PhoneNumber,DeliveryTimestamp) VALUES (?,?,?,?,?)"); //prepare a statement
			ps2.setInt(1, newOrder.getDeliveryDetails().getOrderID());
			ps2.setString(2, newOrder.getDeliveryDetails().getDeliveryAddress());
			ps2.setString(3, newOrder.getDeliveryDetails().getRecipientName());
			ps2.setString(4, newOrder.getDeliveryDetails().getPhoneNumber());
			ps2.setTimestamp(5, newOrder.getReceivingTimestamp());
			ps2.executeUpdate();
		}

		returnMessage.add("Your order was placed successfully.\nWe hope to see you again.");
		return returnMessage;
	}
  
  /** This method handles any login attempt messages received from the client.
  *
  * @param loginInfo The message received from the client (the userName and the password)
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
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("Connection to Database failed");
			ServerMain.serverController.showMessageToUI("Connection to Database failed");
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
				System.out.println("connected user"+data[0]+" tried to login again - blocked");
				ServerMain.serverController.showMessageToUI("connected user"+data[0]+" tried to login again - blocked");
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
					System.out.println(data[0]+" logged into the system");
					ServerMain.serverController.showMessageToUI(data[0]+" logged into the system");
					return returnMessage;
				}
			} else if (!(data[1].equals(rs.getString(2)))) //if password received does not match the data base 
			{
				if (rs.getInt(4) == 3)
				{ //if user is already blocked from too many login attempts
					//failed - ArrayList<String> to return in form of ["failed",reason of failure]
					returnMessage.add("failed"); //state failed to log in
					returnMessage.add("user is blocked"); //reason for failure
					System.out.println(data[0]+" is blocked");
					ServerMain.serverController.showMessageToUI(data[0]+" is blocked");
					return returnMessage;
				} else
				{
						//failed - ArrayList<String> to return in form of ["failed",reason of failure,number of attempts made]
					returnMessage.add("failed"); 							//state failed to log in
					returnMessage.add("password does not match"); 			//reason for failure
					System.out.println("wrong password "+data[0]);
					ServerMain.serverController.showMessageToUI("wrong password "+data[0]);
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
		    PreparedStatement ps = con.prepareStatement("INSERT INTO product (ProductName,ProductType,ProductPrice,ProductDescription,ProductImage,ProductDominantColor) VALUES (?,?,?,?,?,?)");	//prepare a statement
		    ps.setString(1, product.getProductName());
		    ps.setString(2, product.getProductType());
		    ps.setDouble(3, product.getProductPrice());
		    ps.setString(4,product.getProductDescription());
		    if(product.getProductImage() != null)
		    	ps.setBlob(5,FilesConverter.convertByteArrayToInputStream(product.getProductImage()));
		    else
		    	ps.setNull(5, java.sql.Types.NULL);
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
 	   if (con != null)
		{
			System.out.println("Connection to Data Base succeeded");
			ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
		}
 	    }
 	    catch( SQLException e)	//catch exception
 	    {
 	      System.out.println("SQLException: " + e.getMessage() );
 	     ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
 	    }
  		 System.out.println("uploadind file to Data Base");
  		ServerMain.serverController.showMessageToUI(  "uploadind file to Data Base");
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
  		con=connectToDB();	
  		if (con != null)
		{
			System.out.println("Connection to Data Base succeeded");
			ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
		}
  		//Achieve connection to the data base
  		}
  		catch(Exception e) {
  			System.out.println("failed connecting to db");	
  			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
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
  			ServerMain.serverController.showMessageToUI(  e.getMessage());
  		}
  		
  		return is;							//returned value
  	}
  	  		
  	
  	/**
  	 * This method returns a specific store based on the userName
  	 * @param Username the username of the store employee
  	 * @return	the store
  	 * @throws ClassNotFoundException	DB connection
  	 * @throws SQLException	for SQL
  	 */
  	private StoreEntity getSpecificStore(String Username) throws ClassNotFoundException, SQLException
  	{
  		StoreEntity store = null;
		Statement stmt,stmt2;;
		try
		{
			con = connectToDB(); //call method to connect to DB
			if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
		stmt = con.createStatement();
		stmt2=con.createStatement();
		
		ResultSet rs = stmt2.executeQuery("SELECT BranchID FROM projectx.storeemployee WHERE UserName = '"+Username+"'");		//get the specific Branch ID
		Integer branchID=null;
		if(rs.next())
			 branchID = rs.getInt(1);
		ResultSet rs1 = stmt.executeQuery("SELECT BranchID,BranchName,BranchManager FROM projectx.store WHERE BranchID = "+branchID+""); //get all the stores (ID,Name,managerID) in the stores table from the data base

		while (rs1.next())
		{
			store = new StoreEntity(rs1.getInt(1), rs1.getString(2), rs1.getInt(3)); //create a new instance of a store
		

		stmt = con.createStatement();
		ResultSet rs2;
		Map<Integer,Double> storeDiscoutsSales; 	//holds all the discounts of the store sale
			rs2 = stmt.executeQuery("SELECT ProductID,ProductPrice FROM projectx.discount WHERE BranchID = "+ store.getBranchID()); //get all the discounts for each store

			if(rs2.next())
			{
				storeDiscoutsSales = new HashMap<Integer, Double>();			//create  a new hashMap for discounts
				storeDiscoutsSales.put(rs2.getInt(1), rs2.getDouble(2)); //insert each store's discounts to a hashMap

				while (rs2.next())
				{
					storeDiscoutsSales.put(rs2.getInt(1), rs2.getDouble(2)); //insert each store's discounts to a hashMap
				}
				store.setStoreDiscoutsSales(storeDiscoutsSales); //insert the hashMap of discounts to the storeEntity

			}

		
		stmt = con.createStatement();
		ResultSet rs3;
		ArrayList<Integer> listOfStoreWorkers;			//holds the list of store workers for the store entity
			rs3 = stmt.executeQuery("SELECT WorkerID FROM projectx.storeemployee WHERE BranchID = "+ store.getBranchID()); //get all the discounts for each store
			
			if(rs3.next())
			{
				listOfStoreWorkers = new ArrayList<Integer>();			//create  a new arrayList for workers
				if(rs3.getInt(1) == store.getStoreManagerWorkerID()) {		//if the worker is the manager 
					store.setStoreManagerWorkerID(rs3.getInt(1));			//set manager to store
				}else													//if a simple store worker
					listOfStoreWorkers.add(rs3.getInt(1)); 				//insert each store worker's worker id to the list

				while (rs3.next())
				{
					if(rs3.getInt(1) == store.getStoreManagerWorkerID()) {		//if the worker is the manager 
						store.setStoreManagerWorkerID(rs3.getInt(1));			//set manager to store
					}else													//if a simple store worker
						listOfStoreWorkers.add(rs3.getInt(1)); 				//insert each store worker's worker id to the list
				}
				store.setStoreWorkers(listOfStoreWorkers);				//set the list of workers for the store

			}

	}

		return store;
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
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		} catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
		}
		stmt = con.createStatement();
		ResultSet rs1 = stmt.executeQuery("SELECT BranchID,BranchName,BranchManager FROM projectx.store"); //get all the stores (ID,Name,managerID) in the stores table from the data base

		while (rs1.next())
		{
			store = new StoreEntity(rs1.getInt(1), rs1.getString(2), rs1.getInt(3)); //create a new instance of a store
		

		stmt = con.createStatement();
		ResultSet rs2;
		Map<Integer,Double> storeDiscoutsSales; 	//holds all the discounts of the store sale
			rs2 = stmt.executeQuery("SELECT ProductID,ProductPrice FROM projectx.discount WHERE BranchID = "+ store.getBranchID()); //get all the discounts for each store

			if(rs2.next())
			{
				storeDiscoutsSales = new HashMap<Integer, Double>();			//create  a new hashMap for discounts
				storeDiscoutsSales.put(rs2.getInt(1), rs2.getDouble(2)); //insert each store's discounts to a hashMap

				while (rs2.next())
				{
					storeDiscoutsSales.put(rs2.getInt(1), rs2.getDouble(2)); //insert each store's discounts to a hashMap
				}
				store.setStoreDiscoutsSales(storeDiscoutsSales); //insert the hashMap of discounts to the storeEntity

			}
		
		stmt = con.createStatement();
		ResultSet rs3;
		ArrayList<Integer> listOfStoreWorkers;			//holds the list of store workers for the store entity
			rs3 = stmt.executeQuery("SELECT WorkerID FROM projectx.storeemployee WHERE BranchID = "+ store.getBranchID()); //get all the discounts for each store
			
			if(rs3.next())
			{
				listOfStoreWorkers = new ArrayList<Integer>();			//create  a new arrayList for workers
				if(rs3.getInt(1) == store.getStoreManagerWorkerID()) {		//if the worker is the manager 
					store.setStoreManagerWorkerID(rs3.getInt(1));			//set manager to store
				}else													//if a simple store worker
					listOfStoreWorkers.add(rs3.getInt(1)); 				//insert each store worker's worker id to the list

				while (rs3.next())
				{
					if(rs3.getInt(1) == store.getStoreManagerWorkerID()) {		//if the worker is the manager 
						store.setStoreManagerWorkerID(rs3.getInt(1));			//set manager to store
					}else													//if a simple store worker
						listOfStoreWorkers.add(rs3.getInt(1)); 				//insert each store worker's worker id to the list
				}
				store.setStoreWorkers(listOfStoreWorkers);				//set the list of workers for the store

			}
			listOfStoresFromDB.add(store); //add the product from the data base to the list
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
  
  /**The method returns all the users from the data base table
   * Array list with user name and type
   * @return
   * @throws SQLException
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public ArrayList<String> getAllUsersFromDB() throws SQLException, IOException, ClassNotFoundException
  {
	  ArrayList<String> AllUsers=new ArrayList<String>();
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
	  ResultSet rs = stmt.executeQuery("SELECT Username,UserType FROM projectx.user");	//get all the users in the user table from the data base
	  while(rs.next())
	  {
		  AllUsers.add(rs.getString(1)+"~"+rs.getString(2));
	  }
	  return AllUsers;
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
		    
		
		    if(product.getProductImage()!=null)
		    {
		    	PreparedStatement ps = con.prepareStatement("UPDATE  projectx.product SET ProductName = ?,ProductType=?,ProductPrice=?,ProductDescription=?,ProductDominantColor=? ,ProductImage=? WHERE ProductName=?");	//prepare a statement
			    ps.setString(1, product.getProductName());
			    ps.setString(2, product.getProductType());
			    ps.setDouble(3, product.getProductPrice());
			    ps.setString(4, product.getProductDescription());
		    	ps.setString(5, product.getProductDominantColor());
		    	ps.setBlob(6,FilesConverter.convertByteArrayToInputStream(product.getProductImage()));/********************************/
				ps.setString(7, OldProduct.getProductName()); 
				ps.executeUpdate();
		    }
		    else {
		    	PreparedStatement ps = con.prepareStatement("UPDATE  projectx.product SET ProductName = ?,ProductType=?,ProductPrice=?,ProductDescription=?,ProductDominantColor=? WHERE ProductName=?");	//prepare a statement
			    ps.setString(1, product.getProductName());
			    ps.setString(2, product.getProductType());
			    ps.setDouble(3, product.getProductPrice());
			    ps.setString(4, product.getProductDescription());
		    	ps.setString(5, product.getProductDominantColor());
				ps.setString(6, OldProduct.getProductName()); 
				ps.executeUpdate();
		    }
		   /* ps.setString(6, product.getProductDominantColor());
		    ps.setString(7, OldProduct.getProductName()); 
		    ps.executeUpdate();*/
		    return "Success";                                                                           
	   // }                                                                       
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
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
		}

		catch (SQLException e) //catch exception
		{
			System.out.println("SQLException: " + e.getMessage());
			ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
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
			PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.complaints (OrderNum,Description,Status,File,Compensation,FiledOn) VALUES (?,?,?,?,?,?)"); //prepare a statement
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
			ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));		//set the time it is filed

			ps.executeUpdate();

			return "Success";
		}
		return "Order does not exist";
	}
 
  /** This method handles product search messages received from the client.
  *    Search is with ID
  * @param asked The message received from the client.
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
	    if (con != null)
		{
			System.out.println("Connection to Data Base succeeded");
			ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
		}
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	      ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
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
	    if (con != null)
		{
			System.out.println("Connection to Data Base succeeded");
			ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
		}
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	      ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
	    }
	  stmt = con.createStatement();
	  
	  /*Query -get all the discounts for the storeID*/
	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.discount WHERE BranchID ='"+storeID+"'");	
	while(rs.next())
	  {
		  key_product_id=rs.getInt(2); //set value's
		  price=rs.getDouble(3);
          product.put(key_product_id, price); //add values to hash map
	  }
    return product;
  }
  
  /**
   * This method return's the discount's from the table in the data base 
   * @param storeID, the store that the customer shop's in
   * @return hash map of the discounts 
   * @throws SQLException 
   * @throws ClassNotFoundException 
   */
    public String AddDiscounts(int storeID,ProductEntity product) throws SQLException, ClassNotFoundException
    {
    	Statement stmt;
    	  try
    	    {
    	    con = connectToDB();	//call method to connect to DB
    	    if (con != null)
    		{
    			System.out.println("Connection to Data Base succeeded");
    			ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
    		}
    	    }
    	    catch( SQLException e)	//catch exception
    	    {
    	      System.out.println("SQLException: " + e.getMessage() );
    	      ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
    	    }
    	  stmt = con.createStatement();
    	  
    	  /*Query -set discounts for the storeID and product*/
    	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.discount WHERE ProductID = " +product.getProductID()+" AND BranchID= "+storeID+"");	
    	  if(!(rs.next()))   //if there isn't a sale on this product 
    	  {  		
    		PreparedStatement ps = con.prepareStatement("INSERT INTO projectx.discount (ProductID,ProductPrice,BranchID) VALUES (?,?,?)");
    		ps.setInt(1, product.getProductID());																			//insert parameters into the statement
  	    ps.setDouble(2, product.getSalePrice());
  	    ps.setInt(3, storeID);
  	    ps.executeUpdate();
    		return "Success";
    	  }
    	  else //there product has allready a sale on it
    	  {
    		PreparedStatement ps = con.prepareStatement("UPDATE  projectx.discount SET ProductPrice=?  WHERE ProductID=? AND BranchID=?");							//insert parameters into the statement
  	    ps.setDouble(1, product.getSalePrice());
  	    ps.setInt(2, product.getProductID());
  	    ps.setInt(3, storeID);
  	    ps.executeUpdate();
    		return "Success";
    	  }
  	        
   }
    
    
    public String DeleteDiscount(int storeID,ProductEntity product) throws ClassNotFoundException, SQLException
    {
    	Statement stmt;
   	  try
   	    {
   	    con = connectToDB();	//call method to connect to DB
   	    if (con != null)
   		{
   			System.out.println("Connection to Data Base succeeded");
   			ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
   		}
   	    }
   	    catch( SQLException e)	//catch exception
   	    {
   	      System.out.println("SQLException: " + e.getMessage() );
   	      ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
   	    }
			stmt = con.createStatement();
	
   	  /*Query -set discounts for the storeID and product*/
   	  ResultSet rs = stmt.executeQuery("SELECT * FROM projectx.discount WHERE ProductID = " +product.getProductID()+" AND BranchID= "+storeID+"");	
   	  if(rs.next())   //if there is a sale on this product 
   	  {  		
   		PreparedStatement ps = con.prepareStatement("DELETE FROM projectx.discount  WHERE ProductID=? AND BranchID=?");
   		ps.setInt(1, product.getProductID());																			//insert parameters into the statement
 	        ps.setInt(2, storeID);
 	        ps.executeUpdate();
   		return "Success";
   	  }else return "Failed";
    }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    ServerMain.serverController.showMessageToUI(  "Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
    ServerMain.serverController.showMessageToUI(  "Server has stopped listening for connections.");
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
	  	
		ArrayList<String>retval=new ArrayList<String>();
		
		try {
		System.out.println("<user>"+operation);
		 ServerMain.serverController.showMessageToUI( "<user>"+operation);
		
		if(operation.equals("getCatalog"))
		{
			ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();	//an arrayList that holds all the products in the catalog
			listOfProducts = getCatalog();
			messageToSend.setMessage(listOfProducts);		//set the message for sending back to the client
			client.sendToClient(messageToSend);
			
		}
		
		else if(operation.equals("getAllUsers"))
		{
			ArrayList<String> listOfUsers = new ArrayList<String>();			//an array list that holds all the users in the catalog
			listOfUsers=getAllUsersFromDB();
			messageToSend.setMessage(listOfUsers);
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getUserDetails")) {
			
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
		else if(operation.equals("getVerbalReports"))
		{
			ArrayList<VerbalReportEntity> listOfVerbalReports = new ArrayList<VerbalReportEntity>();	//an arrayList that holds all the products in the catalog
			listOfVerbalReports = getVerbalReports();
			messageToSend.setMessage(listOfVerbalReports);
			client.sendToClient(messageToSend);
		}
			
		else if(operation.equals("updatePermission")) {
			String value = this.updatePermissions((String[])messageFromClient);
			messageToSend.setMessage(value);
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getSelfDefinedProduct"))		//get an arratList of products who fit the customer's paramaters
		{
			ArrayList<ProductEntity> listOfProducts = new ArrayList<ProductEntity>();	//an arrayList that holds all the products in the catalog
			listOfProducts = getSelfDefinedProducts((ArrayList<String>)messageFromClient);
			messageToSend.setMessage(listOfProducts);
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getDiscounts"))/*get the discounts Hash Map for a specific store*/
		{
			int store= (int)messageToSend.getMessage();
			HashMap<Integer,Double> listOfDiscounts = new HashMap<Integer,Double>();	//an arrayList that holds all the products in the catalog
			listOfDiscounts= getDiscounts(store);                                                                     //get discount from discount table in the data base
			if(listOfDiscounts!=null)
			{
			messageToSend.setMessage(listOfDiscounts);		                                                     //set the message for sending back to the client
			  client.sendToClient(messageToSend);
			}
			else {
				System.out.println("Operation failed");
				 ServerMain.serverController.showMessageToUI( "Operation failed");

				messageToSend.setMessage(null); //set the message for sending back to the client
				  client.sendToClient(messageToSend);
			}
		}
		else if(operation.equals("AddDiscount"))/*get the discounts Hash Map for a specific store*/
		{
			HashMap<ProductEntity,Integer> disc= (HashMap<ProductEntity,Integer>)messageToSend.getMessage();
			Iterator<ProductEntity> it=disc.keySet().iterator();
			ProductEntity p=it.next();
		    String message=AddDiscounts(disc.get(p),(ProductEntity)p);
		    
			if(message.equals("Success"))
			{
		    ServerMain.serverController.showMessageToUI("Success");
			messageToSend.setMessage("Success");		                                                     //set the message for sending back to the client
			  client.sendToClient(messageToSend);
			}
			else {
				System.out.println("Operation failed");
				 ServerMain.serverController.showMessageToUI( "Operation failed");
				messageToSend.setMessage("Failed"); //set the message for sending back to the client
				  client.sendToClient(messageToSend);
			}
		}
		else if(operation.equals("DeleteDiscount"))/*get the discounts Hash Map for a specific store*/
		{
			HashMap<ProductEntity,Integer> disc= (HashMap<ProductEntity,Integer>)messageToSend.getMessage();
			Iterator<ProductEntity> it=disc.keySet().iterator();
			ProductEntity p=it.next();
		    String message=DeleteDiscount(disc.get(p),(ProductEntity)p);
			if(message.equals("Success"))
			{
		    ServerMain.serverController.showMessageToUI("Success");
			messageToSend.setMessage("Success");		                                                     //set the message for sending back to the client
			  client.sendToClient(messageToSend);
			}
			else {
				System.out.println("Operation failed");
				 ServerMain.serverController.showMessageToUI( "Operation failed");
				  messageToSend.setMessage("Failed"); //set the message for sending back to the client
			     client.sendToClient(messageToSend);
			}
		}
		else if(operation.equals("updateAccount")) {
			String toClient = updateAccout((CustomerEntity)messageFromClient);
			
			messageToSend.setMessage(toClient);
			messageToSend.setOperation("updateRetVal");
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("cancelRequest"))
		{
			String retMsg = cancelRequest((Integer)messageFromClient);
			messageToSend.setMessage(retMsg);
			  client.sendToClient(messageToSend);
		}
		else if(operation.equals("getCancelRequests"))		//get all the orders which have a cancel request
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getCancelRequests((String)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			  client.sendToClient(messageToSend);
		}
		else if(operation.equals("cancelOrder"))				//for store manger canceling an order
		{
			String retMsg = cancelOrder((ArrayList<String>)messageFromClient);
			messageToSend.setMessage(retMsg);
			  client.sendToClient(messageToSend);
		}
		else if(operation.equals("addProductToCatalog"))
		{
ProductEntity product = (ProductEntity)messageToSend.getMessage();
			
			if(this.addToCatalog(product).equals("Success")) {
				System.out.println("product added sucessesfuly to the catalog DB");
				 ServerMain.serverController.showMessageToUI( "product added sucessesfuly to the catalog DB");
				messageToSend.setMessage("Added"); 		//set the message for sending back to the client
				  client.sendToClient(messageToSend);
			}
			else {
				System.out.println("Inserting failed");
				 ServerMain.serverController.showMessageToUI( "Inserting failed");
				messageToSend.setMessage("failed"); //set the message for sending back to the client
				client.sendToClient(messageToSend);
			}
		}
		else if(operation.equals("deleteProductFromCatalog"))
		{
			ProductEntity product;
			product =  (ProductEntity)messageToSend.getMessage();
			
			if(this.deleteProductFromCatalog(product).equals("Success")) {
				 ServerMain.serverController.showMessageToUI( "product deleted sucessesfuly from the catalog DB");
				messageToSend.setMessage("Deleted"); 		//set the message for sending back to the client
				client.sendToClient(messageToSend);
			}
			else {
				 ServerMain.serverController.showMessageToUI( "Deletion failed");
				messageToSend.setMessage("failed"); //set the message for sending back to the client
				client.sendToClient(messageToSend);
			}
		}
		else if(operation.equals("createNewOrder"))
		{
			try
			{
			retval = createNewOrder((OrderEntity)messageFromClient);
			}
			catch(Exception e)
			{
				retval.add("We are sorry,Something went wrong, Please try again later.");
				e.printStackTrace();
				 ServerMain.serverController.showMessageToUI( e.getMessage());
			}
			messageToSend.setMessage(retval);
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getCustomerOrders"))		//for getting a specific customer's list of orders
		{
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getCustomerOrders((String)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getAllOrders"))			//for getting ALL of the orders in the DB
		{
			
							///Arraylist recieved in the form of ("all" if the all store OR "<the store name>" for a specific store,<int> for the wanted quarter or "all" ////
			ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
			listOfOrders = getAllOrders((ArrayList<String>)messageFromClient);
			messageToSend.setMessage(listOfOrders);
			client.sendToClient(messageToSend);
		}
		else if(operation.equals("createUser"))
		{
			UserInterface user=(UserInterface)messageFromClient;
			try {
			this.insertNewUser(user);
			MessageToSend toClient=new MessageToSend("added","retval");
			client.sendToClient(toClient);
			
			}
			catch(Exception e) {
				e.printStackTrace();
				 ServerMain.serverController.showMessageToUI( e.getMessage());
				MessageToSend toClient = new MessageToSend("failed","retval");
				client.sendToClient(toClient);
			}
			
		}
		else if(operation.equals("createAccount")) {	
			CustomerEntity custen=(CustomerEntity)messageFromClient;
			try {
			this.insertNewCustomer(custen);
			MessageToSend toClient=new MessageToSend("added","retval");
			client.sendToClient(toClient);
			
			}
			catch(Exception e) {
				e.printStackTrace();
				 ServerMain.serverController.showMessageToUI( e.getMessage());
				MessageToSend toClient = new MessageToSend("failed","retval");
				client.sendToClient(toClient);
			}
			
		}
		else if(operation.equals("getAllStores"))				//gets all the stores in the DB
		{
			ArrayList<StoreEntity> listOfAllStores = new ArrayList<StoreEntity>();		//an arrayList that holds all the stores in the DB
			listOfAllStores = getAllstoresFromDB();
			messageToSend.setMessage(listOfAllStores);		//set the message for sending back to the client
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getSpecificStore"))
		{
			StoreEntity store ;		//an arrayList that holds all the stores in the DB
			store = getSpecificStore((String)messageFromClient);
			messageToSend.setMessage(store);		//set the message for sending back to the client
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("exitApp"))					//for when a user exits the app
		{
			if((String)messageFromClient!=null) {
				this.terminateConnection((String)messageFromClient);	//calls a method to remove the user from the connected list
				System.out.println("client "+(String)messageFromClient+" logged out (App closed)");
				 ServerMain.serverController.showMessageToUI( "client "+(String)messageFromClient+" logged out (App closed)");
		
			}
		}
		
		else if(operation.equals("logOut")) {
			this.terminateConnection((String)messageFromClient);
		}
		
		else if(operation.equals("login"))					//for when a user tries to log in
		{
			retval = this.login(client,(String)messageFromClient);
			messageToSend.setMessage(retval);	//set the message for sending back to the client
			client.sendToClient(messageToSend);	//send arraylist back to client
		}
		
		else if(operation.equals("getProduct"))	
		{

			ProductEntity produ;
			int productid = (int)messageToSend.getMessage();
			produ= this.getProduct(client,productid);	//get the product
			messageToSend.setMessage(produ);	        //set the message for sending back to the client
			client.sendToClient(messageToSend);            //send arrayList back to client
		}
		
		else if(operation.equals("getAllProducts"))	
		{
			ArrayList<ProductEntity> listOfAllProducts = new ArrayList<ProductEntity>();		//an arrayList that holds all the stores in the DB
			listOfAllProducts = this.getAllProductsFromDB();
			messageToSend.setMessage(listOfAllProducts);	        //set the message for sending back to the client
			client.sendToClient(messageToSend);	            //send arrayList back to client
		}
		
		else if(operation.equals("deleteProduct"))	
		{
			String Reply;
			ProductEntity prd = (ProductEntity)messageToSend.getMessage();
			Reply = this.DeleteProductsFromDB(prd);
			messageToSend.setMessage(Reply);	        //set the message for sending back to the client
			client.sendToClient(messageToSend);           //send arrayList back to client
		}
		else if(operation.equals("UpdateProduct"))	
		{
			String Reply;
			ArrayList<ProductEntity> p=(ArrayList<ProductEntity>)messageToSend.getMessage();
			Reply = this.UpdateProductInDB(p.get(0),p.get(1));
			messageToSend.setMessage(Reply);	        //set the message for sending back to the client
			client.sendToClient(messageToSend);	           //send arrayList back to client
		}
		else if(operation.equals("createProduct"))
		{
			
			ProductEntity product = (ProductEntity)messageToSend.getMessage();
			if((this.insertProduct(product, client)).equals("Success"))	//check if asked to create a new product and check if it was create successfully
			{
				System.out.println("product added sucessesfuly to DB");
				messageToSend.setMessage("Added");
				client.sendToClient(messageToSend);
			}
			else
			{
				System.out.println("Failed to insert the product to DB");
				messageToSend.setMessage("Failed");
				client.sendToClient(messageToSend);
			}
			
		}
		else if(operation.equals("getSurvey"))
		{
			SurveyEntity survey = getSurvey();
			messageToSend.setMessage(survey);
			messageToSend.setOperation("getSurvey");
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("getSurveyQs")) {
			
			String [] qsText = getSurveyQuestions((int)messageToSend.getMessage());
			messageToSend.setMessage(qsText);
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("updateSurveyQs"))
		{
			String result = updateSurveyQuestions((SurveyEntity)messageFromClient);
			messageToSend.setMessage(result);
			messageToSend.setOperation("surveyUpdateResult");
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("verbalReport")) {
			VerbalReportEntity verbalRep = (VerbalReportEntity)messageFromClient;
			messageToSend.setMessage(this.uploadReprotToDB(verbalRep));
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("SurveyAnswers")) {
			String result = this.updateSurveyAnswers((SurveyEntity)messageFromClient);
			messageToSend.setMessage(result);
			messageToSend.setOperation("surveyUpdateResult");
			client.sendToClient(messageToSend);
		}
		
		else if(operation.equals("handleComplaint"))
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
			 ServerMain.serverController.showMessageToUI( e.getMessage());
		}
		messageToSend.setMessage(retmsg);
		client.sendToClient(messageToSend);
	}
		
	else if(operation.equals("getComplaintOrders"))	//for getting all the orders with comlpaints
	{
		ArrayList<OrderEntity> listOfOrders = new ArrayList<OrderEntity>();
		listOfOrders = getAllComplaintOrders((ArrayList<ComplaintEntity>)messageFromClient);
		messageToSend.setMessage(listOfOrders);
		client.sendToClient(messageToSend);
	}
	else if(operation.equals("getComplaints"))		//for getting all complaints
	{
		ArrayList<ComplaintEntity> listOfComplaints = new ArrayList<ComplaintEntity>();		//an arrayList that holds all the stores in the DB
		listOfComplaints = getComplaints((ArrayList<String>)messageFromClient);
		messageToSend.setMessage(listOfComplaints);		//set the message for sending back to the client
		client.sendToClient(messageToSend);
	}
		
	else if(operation.equals("getBlockedUsers")) {
		ArrayList<String> ret = new ArrayList<String>();
		ret = this.getBlockedUsers();
		messageToSend.setMessage(ret);
		client.sendToClient(messageToSend);
	}
		
	else if(operation.equals("releaseBlock")) {
		String toClient = this.releaseUserBlock((String)messageFromClient);
		messageToSend.setMessage(toClient);
		client.sendToClient(messageToSend);
	}
		
	else if(operation.equals("getNumberOfSurveys")) {
		ArrayList<Integer> totalSurveys = this.getNumberOfSurveys();
		messageToSend.setMessage(totalSurveys);
		client.sendToClient(messageToSend);
	}
		
	else if(operation.equals("complaint")) {
			ComplaintEntity complaint = (ComplaintEntity)messageToSend.getMessage();
			this.incomingFileName=complaint.getOrderID();
			String ret="";
			ret =	this.complaint(complaint);
			if(ret.equals("Success")) {
				System.out.println("complaint added by customer");
				 ServerMain.serverController.showMessageToUI("complaint added by customer");
			//	generalMessage = (String)("Added");
				messageToSend.setMessage("Success"); 		//set the message for sending back to the client
				client.sendToClient(messageToSend);
			}
			else if(ret.equals("Complaint was already filed"))
			{
				System.out.println("complaint failed");
				 ServerMain.serverController.showMessageToUI("complaint failed");
				messageToSend.setMessage("Complaint was already filed");
				client.sendToClient(messageToSend);
			}
			else if(ret.equals("Order does not exist")){
				System.out.println("failed to add complaint");
				ServerMain.serverController.showMessageToUI("failed to add complaint");
			//	generalMessage = (String)("failed");
				messageToSend.setMessage("failed"); //set the message for sending back to the client
				client.sendToClient(messageToSend);
			}
		}
	}
		
		catch(Exception ex) {
			ex.printStackTrace();
			ServerMain.serverController.showMessageToUI(ex.getMessage());

			}
				
	}
	
  private ArrayList<Integer> getNumberOfSurveys() throws SQLException {
	// TODO Auto-generated method stub
	  ArrayList<Integer> total = new ArrayList<Integer>();
	  Statement st;
	  try {
		  con=connectToDB();
			 System.out.println("Connection to Database succeeded");
			  ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
		}
		catch(Exception e) {
			System.out.println("Connection to database failed");
			ServerMain.serverController.showMessageToUI("Connection to Database failed");
		}
	  st = con.createStatement();
	  ResultSet rs = st.executeQuery("SELECT DISTINCT Surveynum FROM projectx.survey");
	  
	  while(rs.next())
		  total.add(rs.getInt(1));
	  
	return total;
}

private String releaseUserBlock(String user) throws SQLException {
	// TODO Auto-generated method stub
	  Statement stmnt;
	  String s=null;
	  try {
		  con=connectToDB();
			 System.out.println("Connection to Database succeeded");
			  ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
		}
		catch(Exception e) {
			System.out.println("Connection to database failed");
			ServerMain.serverController.showMessageToUI("Connection to Database failed");
		}
	  try {
	  stmnt = con.createStatement();
	  stmnt.executeUpdate("UPDATE projectx.user SET LoginAttempts=0 WHERE Username='"+user+"'");
	  s = "released";
	  }
	  catch(Exception e){
		  e.printStackTrace();
		  s = "releaseFailed";
	  }
	  return s;
}

private ArrayList<String> getBlockedUsers() throws SQLException {
	// TODO Auto-generated method stub
	  ArrayList<String> users= new ArrayList<String>();
	  Statement stmnt;
	  try {
		  con=connectToDB();
			 System.out.println("Connection to Database succeeded");
			  ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
		}
		catch(Exception e) {
			System.out.println("Connection to database failed");
			ServerMain.serverController.showMessageToUI("Connection to Database failed");
		}
	  stmnt = con.createStatement();
	  ResultSet rs = stmnt.executeQuery("SELECT Username FROM projectx.user WHERE LoginAttempts=3");
	  if(!rs.next())
		  return null;
	  else {
		  users.add(rs.getString(1));
		  while(rs.next())
			  users.add(rs.getString(1));
		  return users;
	  }
}

/**
   * method for uploading new verbal report into the data base
   * 
   * @param verbalRep - the report to be uploaded to the data base
   * @throws SQLException
   */
  private String uploadReprotToDB(VerbalReportEntity verbalRep) throws SQLException {
	// TODO Auto-generated method stub
	  PreparedStatement ps;
	  InputStream instrm = FilesConverter.convertByteArrayToInputStream(verbalRep.getFile());
	try {
		con=connectToDB();
		 System.out.println("Connection to Database succeeded");
		  ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
	}
	catch(Exception e) {
		System.out.println("Connection to database failed");
		ServerMain.serverController.showMessageToUI("Connection to Database failed");
	}
	
	try {
	ps=con.prepareStatement("INSERT INTO projectx.verbalreports (Report,Date) VALUES (?,?)");
	ps.setBlob(1, instrm);
	ps.setTimestamp(2, verbalRep.getDate());
	ps.executeUpdate();
	System.out.println("Verbal report uploaded successfully");
	ServerMain.serverController.showMessageToUI("Verbal report uploaded successfully");
	return "uploaded";
	}
	catch(Exception e) {
		System.out.println("Verbal report upload failed");
		ServerMain.serverController.showMessageToUI("Verbal report upload failed");
		return "failed";
	}
	
}

/**
   * method for editing user permission in the data base
   * 
   * @param object
   * @return
   * @throws SQLException
   */
  private String updatePermissions(String[] userPermission) throws SQLException {
	// TODO Auto-generated method stub
	  Statement stmnt;
	  String ret="";
	  try {
		  con=connectToDB();
		  System.out.println("Connection to Database succeeded");
		  ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
	  }
	  catch(Exception e) {
		  System.out.println("Connection to Database failed");
		  ServerMain.serverController.showMessageToUI("Connection to Database failed");
	  }
	  stmnt=con.createStatement();
	  ResultSet rs = stmnt.executeQuery("SELECT UserType FROM projectx.user WHERE Username='"+userPermission[0]+"'");
	  
	  if(!rs.next())
		  return "noUser";
	  else{
		  if(userPermission[1].equals("C")) {
			  ret+="customer";
		  	}
		  else if(userPermission[1].equals("SM") || userPermission[1].equals("SW")) {
			  long userID;
			  int branchNum = Integer.parseInt(userPermission[2]);
			  ResultSet id = stmnt.executeQuery("SELECT UserId,UserType FROM projectx.user WHERE Username='"+userPermission[0]+"'");
			  if(id.next()) {
				  userID = id.getLong(1);
				  if(id.getString(2).equals("SW") || id.getString(2).equals("SM"))
					  stmnt.executeUpdate("UPDATE projectx.storeemployee SET BranchID="+branchNum+", WorkerID="+userID+" WHERE UserName='"+userPermission[0]+"'");
				  else
					  stmnt.executeUpdate("INSERT INTO projectx.storeemployee (UserName,WorkerID,BranchID) VALUES ('"+userPermission[0]+"',"+userID+","+branchNum+")");
			  	  stmnt.executeUpdate("UPDATE projectx.user SET UserType='"+userPermission[1]+"' WHERE Username='"+userPermission[0]+"'");
			  	  ret+="Updated";
			  }
			  else
				 return "noUser";
		  }
		  else {
			  stmnt.executeUpdate("UPDATE projectx.user SET UserType='"+userPermission[1]+"' WHERE Username='"+userPermission[0]+"'");
			  ret+="Updated";
		  }
		  return ret;
	  }
}

private String[] getSurveyQuestions(int surveyNum) throws SQLException {
	// TODO Auto-generated method stub
	  int i=0;
	  String[] ques=new String[6];
	  Statement stmnt;
	  try {
		  con=connectToDB();
		  System.out.println("Connection to Database succeeded");
		  ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
	  }
	  catch(Exception e) {
		  System.out.println("Connection to Database failed");
		  ServerMain.serverController.showMessageToUI("Connection to Database failed");
	  }
	  stmnt=con.createStatement();
	  ResultSet rs = stmnt.executeQuery("SELECT QuestionText FROM projectx.survey WHERE Surveynum="+surveyNum);
	  
	  if(!rs.next())
		  return null;
	  else {
		  ques[i]=rs.getString(1);
		  i++;
  }
	  
	  while(rs.next()) {
			ques[i]=rs.getString(1);
			i++;
		}
	
			  
	return ques;
}

/**
   * This method updates the questions in the DB
   * @param survey the new survey
   * @return success/faild
 * @throws SQLException for SQL
   */
  private String updateSurveyQuestions(SurveyEntity survey) throws SQLException
  {
	  Statement stmt;
	  try {
			 con=connectToDB();
			 System.out.println("Connection to Database succeeded");
			 ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("Connection to Database failed");
			 ServerMain.serverController.showMessageToUI("Connection to Database failed");
		 }
	  
	  
	  try {
	  for(int i=1;i<=6;i++) {
		  PreparedStatement ps= con.prepareStatement("UPDATE projectx.survey SET QuestionText=?, One=?, Two=?, Three=?, Four=?, Five=?, Six=?, Seven=?, Eight=?, Nine=?, Ten=? WHERE Questionnum=?");
		  ps.setString(1, survey.getQuestionText(i));
		  ps.setInt(2, 0);
		  ps.setInt(3, 0);
		  ps.setInt(4, 0);
		  ps.setInt(5, 0);
		  ps.setInt(6, 0);
		  ps.setInt(7, 0);
		  ps.setInt(8, 0);
		  ps.setInt(9, 0);
		  ps.setInt(10, 0);
		  ps.setInt(11, 0);
		  ps.setInt(12, i);
		  ps.executeUpdate();
		  //stmt.executeUpdate("UPDATE projectx.survey SET QuestionText = '"+survey.getQuestionText(i)+"' WHERE Questionnum = "+i);
	  	}
	  }
	  catch(Exception e)
	  {
		 ServerMain.serverController.showMessageToUI("There was a problem updating the questions");
		  ServerMain.serverController.showMessageToUI(e.getMessage());
		  e.printStackTrace();
		  return "faild";
	  }
	  return "Updated";
  }
  
  /**
   * This method get the survey from the DB
   * @return	the survey
   * @throws SQLException	for SQL
   */
  private SurveyEntity getSurvey() throws SQLException
  {
	  int rank;
	  Statement stmt;
	  ResultSet rs;
	  SurveyEntity survey = null;
	  try {
			 con=connectToDB();
			 System.out.println("Connection to Database succeeded");
			 ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 System.out.println("Connection to Database failed");
			 ServerMain.serverController.showMessageToUI("Connection to Database failed");
		 }
	  stmt = con.createStatement();
	   rs = stmt.executeQuery("SELECT * FROM projectx.survey");
	   survey = new SurveyEntity();
	  
		  
		   while(rs.next())
	   		{
		   		survey.setQuestionText(Integer.parseInt(rs.getString(1)), rs.getString(2));
		   		for(int i=3;i<=12;i++) {
		   			rank=rs.getInt(i);
		   			survey.setTotalRanks(Integer.parseInt(rs.getString(1)), i, rank);
		   			}
	   		}
	   
	  return survey;
  }
	
    private String updateSurveyAnswers(SurveyEntity surveyAns) throws SQLException {
	// TODO Auto-generated method stub
	  int counter=0,r;
	  Statement stmnt;
	  String []numToWord= {"Zero","One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten"};
	  try {
		 con=connectToDB();
		 System.out.println("Connection to Database succeeded");
		 ServerMain.serverController.showMessageToUI("Connection to Database succeeded");
	 }
	 catch(Exception e) {
		 e.printStackTrace();
		 System.out.println("Connection to Database failed");
		 ServerMain.serverController.showMessageToUI("Connection to Database failed");
	 }
	 for(int i=1;i<7;i++) {
			 r=surveyAns.getQuestionRank(i);
			 stmnt=con.createStatement();
			 try {
		 		 ResultSet rs = stmnt.executeQuery("SELECT "+numToWord[r]+" From projectx.survey WHERE Questionnum="+i+" AND Surveynum="+surveyAns.getSurveyNum());
		 		 if(rs.next()) {
		 			 counter = rs.getInt(1);
			 		 counter++;
			 		 stmnt.executeUpdate("UPDATE projectx.survey SET "+numToWord[r]+" = "+counter+" WHERE Questionnum="+i+" AND Surveynum="+surveyAns.getSurveyNum());
				 }
			 		 
			 	 }
			 	 catch(Exception e) {
		 
			 		 e.printStackTrace();
			 		 System.out.println("Error occured while updating the survey answers");
			 		 return "Probelm";
			 	 }
	 }
			 
	 return "Success";
  }
  
  private String updateAccout(CustomerEntity customer) throws SQLException {
	// TODO Auto-generated method stub
	 
	  Statement stmnt;
	  
	  try {
		 con=connectToDB();
		 if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
	 }
	 catch(Exception e) {
		 e.printStackTrace();
		 System.out.println("Connection to Database failed");
		 ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
	 }
	 stmnt=con.createStatement();
	 try {
	 stmnt.executeUpdate("UPDATE projectx.customers SET PhoneNumber='"+customer.getPhoneNumber()+
	 		"',Address='"+customer.getAddress()+
	 		"',CreditCard='"+customer.getCreditCardNumber()+
	 		"',Email='"+customer.getEmailAddress()+"' WHERE Username='"+customer.getUserName()+"'");
	 }
	 catch(Exception e) {
		 
		 e.printStackTrace();
		 System.out.println("Account update of customer "+ customer.getUserName() +" failed");
			ServerMain.serverController.showMessageToUI("Account update of customer "+ customer.getUserName() +" failed");

		
		return "updateFailed";
	 }

		System.out.println("Account of customer "+ customer.getUserName() +" updated succesfully");
		ServerMain.serverController.showMessageToUI("Account of customer "+ customer.getUserName() +" updated succesfully");

	 return "accountUpdated";

}

private CustomerEntity getCustomerDetails(String custName) throws SQLException {
	// TODO Auto-generated method stub
	  try {
		  con=connectToDB();
		  if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  System.out.println("Connection to Database failed");
		  ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
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
			ServerMain.serverController.showMessageToUI("customer was pulled from database");

	      return ce;
	  }
	  
	  else {
		  System.out.println("failed to find customer "+custName);
			ServerMain.serverController.showMessageToUI("failed to find customer "+custName);

	  }
	  
	return null;
}

public void insertNewCustomer(CustomerEntity ce) throws SQLException {
	  
	Statement stmnt;
	  try {
		  con=connectToDB();
		  if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  System.out.println("Connection to Database failed");
		  ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
	  }
	  
	  stmnt=con.createStatement();
	  ResultSet rs = stmnt.executeQuery("SELECT UserType FROM projectx.user WHERE Username='"+ce.getUserName()+"'");
	  
	  if(rs.next())
		stmnt.execute("DELETE FROM projectx.user WHERE Username='"+ce.getUserName()+"'");  
	  
	  PreparedStatement ps=con.prepareStatement("INSERT INTO projectx.customers (Username,Password,UserID,Subscription,Address,Email,PhoneNumber,JoinTime,CreditCard) VALUES (?,?,?,?,?,?,?,?,?)");
	  ps.setString(1, ce.getUserName());
	  ps.setString(2, ce.getPassword());
	  ps.setLong(3, ce.getID());
	  ps.setString(4,ce.getSubscriptionDiscount().toString());
	  ps.setString(5, ce.getAddress());
	  ps.setString(6, ce.getEmailAddress());
	  ps.setString(7, ce.getPhoneNumber());
	  
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      
	  ps.setTimestamp(8,timestamp);
	  ps.setLong(9, ce.getCreditCardNumber());
	  ps.executeUpdate();										//add new customer to Database
	  
	  ps=con.prepareStatement("INSERT INTO projectx.user (Username,Password,UserType,LoginAttempts,Email,PhoneNumber,UserId) VALUES (?,?,?,?,?,?,?)");
	  ps.setString(1, ce.getUserName());
	  ps.setString(2, ce.getPassword());
	  ps.setString(3, "C");
	  ps.setInt(4, 0);
	  ps.setString(5, ce.getEmailAddress());
	  ps.setString(6, ce.getPhoneNumber());
	  ps.setLong(7, ce.getID());
	  
	  ps.executeUpdate();
	  
	  System.out.println("new customer added to Database");
		ServerMain.serverController.showMessageToUI("new customer added to Database");

  }

/**
 * create new user
 * checks which user to create
 * @param ue user interface 
 * @throws SQLException
 */
public void insertNewUser(UserInterface ue) throws SQLException {
	  
	  try {
		  con=connectToDB();
		  if (con != null)
			{
				System.out.println("Connection to Data Base succeeded");
				ServerMain.serverController.showMessageToUI("Connection to Data Base succeeded");
			}
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  System.out.println("Connection to Database failed");
		  ServerMain.serverController.showMessageToUI("SQLException: " + e.getMessage());
	  }
	  PreparedStatement ps=con.prepareStatement("INSERT INTO projectx.user (Username,Password,UserType,LoginAttempts,Email,PhoneNumber,UserId) VALUES (?,?,?,?,?,?,?)");
	  ps.setString(1, ue.getUserName());
	  ps.setString(2, ue.getPassword());
	  ps.setString(3,ue.getUserType());
	  ps.setString(4, "0");
	  ps.setString(5, ue.getEmailAddress());
	  ps.setString(6, ue.getPhoneNumber());
	  ps.setLong(7, ue.getID());
	 
	  ps.executeUpdate();										//add new customer to Database
	  String g = ue.getUserType();
	  if(ue.getUserType().equals("SM") || ue.getUserType().equals("SW"))		//create only for store manager or store worker
	  {
		  ps=con.prepareStatement("INSERT INTO projectx.storeemployee (WorkerID,BranchID,UserName) VALUES (?,?,?)");
		  ps.setLong(1,ue.getID());
		  if(ue.getUserType().equals("SM"))			//check if user is store manager
		  {
			  StoreManagerEntity sm =(StoreManagerEntity)ue;
			  ps.setInt(2,sm.getBranchID());
		  }
		  else if(ue.getUserType().equals("SW"))	//check if user is store worker
		  {
			  StoreWorkerEntity sw =(StoreWorkerEntity)ue;
			  ps.setInt(2,sw.getBranchID());
		  }
		  ps.setString(3,ue.getUserName());
		  
		  ps.executeUpdate();
	  }
	  
	  
	  System.out.println("new user added to Database");
	  ServerMain.serverController.showMessageToUI("new user added to Database");
	  

}



}