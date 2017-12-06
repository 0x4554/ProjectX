package DataBase;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLConnection {

	public static Connection con;
	
	public static void main(String[] args) 
	{
		try 
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
        
        try 
        {
            con = DriverManager.getConnection("jdbc:mysql://localhost/test","root","Braude");

            System.out.println("SQL connection succeed");
          
            
            
            printCourses(con);
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}
	
	public static void printCourses(Connection con)
	{
		Statement stmt;
		try 
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM courses;");
	 		while(rs.next())
	 		{
				 // Print out the values
				 System.out.println(rs.getString(1)+"  " +rs.getString(2));
			} 
			rs.close();
			//stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {e.printStackTrace();}
	}

	
	public static void createTableCourses(Connection con1){
		Statement stmt;
		try {
			stmt = con1.createStatement();
			stmt.executeUpdate("create table courses(num int, name VARCHAR(40), semestr VARCHAR(10));");
			stmt.executeUpdate("load data local infile \"courses.txt\" into table courses");
	 		
		} catch (SQLException e) {	e.printStackTrace();}
		 		
	}
	
	
	
}



