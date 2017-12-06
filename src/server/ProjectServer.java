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

import ocsf.server.*;
import ocsf.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 */
public class ProjectServer extends AbstractServer 
{
	private static Connection con;
	private static String driver="com.mysql.jdbc.Driver";
  final public static int DEFAULT_PORT = 5555;
  
  
  // * Constructs an instance of the echo server.
  
  public ProjectServer(int port) 
  {
    super(port);
  }
  
  //method for connecting to DB
  protected static  Connection connectToDB() throws SQLException
  {
 	 return DriverManager.getConnection("jdbc:mysql://localhost/projectx","root","projectx");	//connect to the sql database
  }

  
  
   /* This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void insertProduct(String msg, ConnectionToClient client) throws SQLException
  {
	  ArrayList<String> msg1 = new ArrayList<String>(); 
	  try
	    {
	    con = connectToDB();	//call method to connect to DB
	      
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	    }
	    System.out.println("Message received: " + msg + " from " + client);
	    this.sendToAllClients(msg);
	    PreparedStatement ps = con.prepareStatement("INSERT INTO product (?,?,?)");	//prepare a statement
	    ps.setString(1,msg1.get(0));	//insert parameters into the statement
	    ps.setString(2, msg1.get(1));
	    ps.setString(3, msg1.get(2));
	    ps.executeUpdate();
	    
	  }
  
  public ArrayList<String> getProduct(ConnectionToClient clnt,Object asked) throws SQLException
  {
	  ArrayList<String> msg1 = new ArrayList<String>();
	  Statement stmt;
	  int i=0;
	  String str = (String) asked;
	  try
	    {
	    con = connectToDB();	//call method to connect to DB
	      
	    }
	    catch( SQLException e)	//catch exception
	    {
	      System.out.println("SQLException: " + e.getMessage() );
	    }
	  stmt = con.createStatement();
	  ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE ProductID LIKE 'asked'");	//query for extracting a prodcut's details
	  
	  while(rs.next())	//run for the extracted data and add it to an arraylist of strings
	  {
		  msg1.add(rs.getString(i));
		  i++;
	  }
	 
	  
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
  
  @Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
	// TODO Auto-generated method stub
		String str = (String) msg;
		try {
		this.insertProduct(str, client);
		}
		catch(Exception ex) {ex.printStackTrace();}
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
//End of EchoServer class
