
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;


public class Application {

    public static void main(String[] args) throws Exception {
        // ---- configure this for your site
        
        String username = "root";
        String password = "";
        // The URL that will connect to TECFA’s MySQL server
        // Syntax: jdbc:TYPE:machine:port/DB_NAME
        String url = "jdbc:mysql://localhost:3306/recettes_atelier_devops";
        // A canned query string
        String queryString = "SELECT * FROM ingredient";
        
        // ---- configure END


        try{
            Connection con = DriverManager.getConnection(url, username, password);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        
        
        
        
/*
        try {
            Class.forName("org.gjt.mm.mysql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        
        try {
            Connection con;
            Statement stmt;
// Connection to the database at URL with usename and password
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Ok, connection to the DB worked.");
            System.out.println("Let’s see can retrieve something with: "
                    + queryString);
// Create a Statement Object
            stmt = con.createStatement();
// Send the query and bind to the result set
            ResultSet rs = (ResultSet) stmt.executeQuery(queryString);
            while (rs.next()) {
                String s = rs.getString("nomIngredient");
                float n = rs.getInt("idIngredient");
                System.out.println(s + " " + n);
            }
// Close resources
            stmt.close();
            con.close();
        } // print out decent error messages
        catch (SQLException ex) {
            System.err.println("==> SQLException: ");
            while (ex != null) {
                System.out.println("Message: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("ErrorCode: " + ex.getErrorCode());
                ex = ex.getNextException();
                System.out.println("");
            }
        }*/
    }
}
