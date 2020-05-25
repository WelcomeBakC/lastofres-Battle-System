
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


/*
 * Class for testing various features
 */
public class Hello {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		MysqlDataSource thing = new MysqlDataSource();
		
		thing.setPassword("Sean0712");
		thing.setUser("testadmin");
		thing.setUrl("jdbc:mysql://lastofres.cafe24.com");
		Statement stmt = null;
		ResultSet result = null;
		try {
			Connection conn = thing.getConnection();
			stmt = conn.createStatement();
			if (stmt.execute("SELECT * FROM lastofres.avo_character WHERE ch_id == 1;")) {
				result = stmt.getResultSet();
			}
			
			result.next();
			
			System.out.println(result.getString("ch_eng_name"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("something went very wrong :(");
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
			
		} finally {

		    if (result != null) {
		        try {
		        	result.close();
		        } catch (SQLException sqlEx) { } // ignore

		        result = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }
		}
	}

}
