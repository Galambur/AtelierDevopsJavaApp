
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("Bienvenue sur notre site de recette. Veuillez choisir une option en entrant au clavier");
        System.out.println("1 - Voir la liste des recettes");

        System.out.println("2 - Consulter une recette");
        System.out.println("3 - Ajouter une recette");
        System.out.println("4 - Supprimer une recette");

        System.out.println("5 - Changer le nom d'une recette");
        System.out.println("6 - Ajouter un ingrédient à une recette");
        System.out.println("7 - Supprimer un ingrédient à une recette");

        System.out.println("");

        // todo : faire l'entrée de l'utilisateur
        // ---- configure 
        String url = "jdbc:mysql://localhost:3306/recettes_atelier_devops?serverTimezone=UTC";
        String username = "root";
        String password = "";

        // ---- configure END
        Connection con = DriverManager.getConnection(url, username, password);
        Statement statement = con.createStatement();

        if (true) {
            showRecetteById(statement, 1);
        }
    }

    public static void showAllRecettes(Statement statement) throws SQLException {
        String getAllRecettesQuery = "SELECT * FROM recette;";

        ResultSet resultSet = statement.executeQuery(getAllRecettesQuery);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("idRecette") + " - " + resultSet.getString("nom"));
        }
    }

    public static void showRecetteById(Statement statement, int id) throws SQLException {
        String getRecetteQuery = "SELECT * FROM recette, contient, ingredient "
                + "WHERE recette.idRecette = contient.idRecette "
                + "AND ingredient.idIngredient = contient.idIngredient "
                + "AND recette.idRecette = " + id + ";";

        ResultSet resultSet = statement.executeQuery(getRecetteQuery);
        resultSet.first();

        System.out.println("Recette : " + resultSet.getString("nom"));
        System.out.println("Ingrédients nécessaires à la recette : ");

        do {
            System.out.println("\t " + resultSet.getString("nomIngredient"));
        } while (resultSet.next());
    }

    public void exemple(String url, String username, String password, String queryGetString) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(queryGetString);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("idIngredient") + " - " + resultSet.getString("nomIngredient"));
            }

            String nomIngredient = "Ciboulette";
            String queryPutString = "INSERT INTO ingredient(nomIngredient) VALUES (' " + nomIngredient + "');";
            statement.execute(queryPutString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
