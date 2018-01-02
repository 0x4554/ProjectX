package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import client.Client;

/**
 * This class holds all of the logged in clients using a hashMap
 * This hashMap will be used both by the client and the server
 * ConnectedClients.java
 *
 * @author Eliran Toledano
 * @author Lana Krikheli
 * @author Katya Yaakovlev
 * @author Tal Gross
 *
 * Project Name gitProjectX
 *
 */
public class ConnectedClients {
	private static ArrayList<String> clnts=new ArrayList<String>();
	
	public ConnectedClients()
	{
		clnts=new ArrayList<String>();
	}
	/**
	 * This method inserts a new connected client to the hashMap using the userName as the key
	 * @param username	the key
	 * @param clnt	the connection 
	 */
	public static void insertNewConnection(String username){
		clnts.add(username);
	}
	/**
	 * This method gets the connected client from the hashMap using the userName as the key
	 * @param username	the key
	 */
	public static boolean isConnected(String username) {
		return clnts.contains(username);
	}
	/**
	 * This method removes the connected client from the hashMap using the userName as the key
	 * @param username	the key
	 */
	public static void removeConnectedClient(String username)
	{
		clnts.remove(username);
	}
}