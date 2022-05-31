
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("Bienvenue sur notre site de recette.");

        int line = 1;
        Scanner scanner = new Scanner(System.in);
        try {
            OUTER:
            while (line != 0) {
                System.out.println("\n1 - Voir la liste des recettes");
                System.out.println("2 - Consulter une recette");
                System.out.println("3 - Ajouter une recette");
                System.out.println("4 - Supprimer une recette");
                System.out.println("5 - Changer le nom d'une recette");
                System.out.println("6 - Ajouter un ingrédient à une recette");
                System.out.println("7 - Supprimer un ingrédient à une recette");
                System.out.println("0 - Quitter");
                System.out.println("\n Entrez votre choix au clavier");
                line = parseInt(scanner.nextLine());
                
                String url = "jdbc:mysql://localhost:3306/recettes_atelier_devops?serverTimezone=UTC";
                String username = "root";
                String password = "";
                
                Connection con = DriverManager.getConnection(url, username, password);
                Statement statement = con.createStatement();
                
                // liste des possibilités
                switch (line) {
                    case 1:
                        showAllRecettes(statement);
                        break;
                    case 2:
                    {
                        System.out.println("\n Entrez l'identifiant de la recette voulue");
                        var id = parseInt(scanner.nextLine());
                        showRecetteById(statement, id);
                        continue;
                    }
                    case 3:
                        continue;
                    case 4:
                        continue;
                    case 5:
                    {
                        System.out.println("\n Entrez l'identifiant de la recette voulue");
                        var id = parseInt(scanner.nextLine());
                        System.out.println("Entrez le nouveau nom");
                        var nouveauNom = scanner.nextLine();
                        changeNomRecette(statement, id, nouveauNom);
                        continue;
                    }
                    case 6:{
                        System.out.println("\n Entrez l'identifiant de la recette voulue");
                        var idRecette = parseInt(scanner.nextLine());
                        System.out.println("\tVoilà la liste des ingrédients :");
                        showAllIngredients(statement);
                        System.out.println("Entrez l'identifiant de l'ingredient que vous voulez ajouter");
                        var idIngredient = parseInt(scanner.nextLine());
                        ajoutIngredientARecette(statement, idIngredient, idRecette);
                        continue;
                    }
                    case 7:
                        continue;
                    default:
                        break OUTER;
                }
            }
        } catch (Exception e) {
            // System.in has been closed
            throw e;
        }
    }
    
    public static void showAllIngredients(Statement statement) throws SQLException{
        String getAllIngredientsQuery = "SELECT * FROM ingredient;";

        ResultSet resultSet = statement.executeQuery(getAllIngredientsQuery);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("idIngredient") + " - " + resultSet.getString("nomIngredient"));
        }
    }

    public static void ajoutIngredientARecette(Statement statement, int idIngredient, int idRecette) throws SQLException {
        String addIngredientToRecette = "INSERT INTO contient VALUES (" + idIngredient + ", " + idRecette + ")";
        statement.execute(addIngredientToRecette);
    }

    public static void changeNomRecette(Statement statement, int id, String nouveauNom) throws SQLException {
        String changeNomRecetteQuery = "UPDATE recette SET nom='" + nouveauNom + "' WHERE idRecette=" + id;
        statement.execute(changeNomRecetteQuery);
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
