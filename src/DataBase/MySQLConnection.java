package DataBase;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MySQLConnection {

	public static Connection con;
	
	
	public static ArrayList<String> getProductDetails(Connection con,String prdctID) throws SQLException {
		ArrayList<String> dtls=new ArrayList<String>();
		
		Statement stmt=con.createStatement();
		
		ResultSet rslt=stmt.executeQuery("SELECT * FROM Product where ProductID LIKE ProductID");
		dtls.add(rslt.getString("ProductID"));
		dtls.add(rslt.getString("ProductName"));
		dtls.add(rslt.getString("ProductType"));
		
		return dtls;
	}
		
}



