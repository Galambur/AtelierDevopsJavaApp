import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Application {

    public static void main(String[] args) throws Exception {
        // ---- configure this for your site
        
        String url = "jdbc:mysql://localhost:3306/recettes_atelier_devops?serverTimezone=UTC";
        String username = "root";
        String password = "";

        String queryGetString = "SELECT * FROM ingredient;";
        
        // ---- configure END


        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();
            
            ResultSet resultSet = statement.executeQuery(queryGetString);
            
            while (resultSet.next()){
                System.out.println(resultSet.getInt("idIngredient") + " - " + resultSet.getString("nomIngredient"));
            }
            
            String nomIngredient = "Ciboulette";
            String queryPutString = "INSERT INTO ingredient(nomIngredient) VALUES (' " + nomIngredient + "');";
            statement.execute(queryPutString);
            
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
