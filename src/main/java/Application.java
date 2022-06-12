
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
                    case 3:{
                        System.out.println("\n Entrez le nom de votre recette");
                        var nomRecette = scanner.nextLine();
                        System.out.println("\n Entrez la description de votre recette");
                        var description = scanner.nextLine();
                        var idRecette = ajouterRecette(statement, nomRecette, description);
                        System.out.println("Added " + idRecette);
                        
                        int idIngredient = 0;
                        showAllIngredients(statement);
                        do {
                            System.out.println("Entrez l'identifiant d'un ingredient, ou 0 pour arrêter la saisie");
                            idIngredient = parseInt(scanner.nextLine());
                            if(idIngredient == 0)
                                break;
                            ajouterIngredientToRecette(statement, idRecette, idIngredient);
                        } while (idIngredient != 0);
                        continue;
                    }
                    case 4:{
                        System.out.println("\n Entrez l'identifiant de la recette voulue");
                        var id = parseInt(scanner.nextLine());
                        deleteRecette(statement, id);
                        continue;
                    }
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
                    case 7:{
                        System.out.println("\n Entrez l'identifiant de la recette voulue");
                        var idRecette = parseInt(scanner.nextLine());
                        System.out.println("\tVoilà la liste de ses ingrédients :");
                        showIngredientsByIdRecette(statement, idRecette);
                        System.out.println("Entrez l'identifiant de l'ingredient que vous voulez supprimer");
                        var idIngredient = parseInt(scanner.nextLine());
                        supprimerIngredientARecette(statement, idIngredient, idRecette);
                        continue;
                    }
                    default:
                        break OUTER;
                }
            }
        } catch (Exception e) {
            // System.in has been closed
            throw e;
        }
    }
    
    // methodes utilisees pour les tests unitaires
    public static String createAjouterRecetteQuery(String nomRecette, String description){
        return "INSERT INTO recette (nom, description) VALUES ('" + nomRecette + "', '" + description + "');";
    }
    
    public static String createShowRecetteByIdQuery(int id){
        return "SELECT * FROM recette, contient, ingredient "
                + "WHERE recette.idRecette = contient.idRecette "
                + "AND ingredient.idIngredient = contient.idIngredient "
                + "AND recette.idRecette = " + id + ";";
    }
    
    // methodes utilisees dans le programme
    public static int ajouterRecette(Statement statement, String nomRecette, String description) throws SQLException, IllegalArgumentException  {
        if(statement == null)
            throw new IllegalArgumentException ("Le statement ne peut pas être nul");
        
        if("".equals(nomRecette) || nomRecette == null || "".equals(description) || description == null)
            throw new IllegalArgumentException();
        
        String addRecetteQuery = createAjouterRecetteQuery(nomRecette, description);   
        statement.executeUpdate(addRecetteQuery, Statement.RETURN_GENERATED_KEYS);    
        
        String getLastIdQuery = "SELECT MAX(idRecette) as max FROM recette;";
        ResultSet resultSet = statement.executeQuery(getLastIdQuery);
        
        int id = 0;
        while (resultSet.next()){
            id = resultSet.getInt("max");
        }
        return id;
    }
    
    public static void deleteRecette(Statement statement, int idRecette) throws SQLException, Exception{
        if(statement == null)
            throw new Exception("Le statement ne peut pas être nul");
        
        String deleteContientLinkQuery = "DELETE FROM contient WHERE idRecette=" + idRecette;
        statement.execute(deleteContientLinkQuery);
        
        String deleteRecetteQuery = "DELETE FROM recette WHERE idRecette="+ idRecette;
        statement.execute(deleteRecetteQuery);
    }

    public static void ajoutIngredientARecette(Statement statement, int idIngredient, int idRecette) throws SQLException, Exception {
        if(statement == null)
            throw new Exception("Le statement ne peut pas être nul");
        
        String addIngredientToRecette = "INSERT INTO contient VALUES (" + idIngredient + ", " + idRecette + ")";
        statement.execute(addIngredientToRecette);
    }

    public static void supprimerIngredientARecette(Statement statement, int idIngredient, int idRecette) throws SQLException, Exception {
        if(statement == null)
            throw new Exception("Le statement ne peut pas être nul");
        
        String deleteIngredientToRecette = "DELETE FROM contient WHERE idIngredient=" + idIngredient + " AND idRecette=" + idRecette;
        statement.execute(deleteIngredientToRecette);
    }

    public static void changeNomRecette(Statement statement, int id, String nouveauNom) throws SQLException, Exception {
        if(statement == null)
            throw new Exception("Le statement ne peut pas être nul");
        
        String changeNomRecetteQuery = "UPDATE recette SET nom='" + nouveauNom + "' WHERE idRecette=" + id;
        statement.execute(changeNomRecetteQuery);
    }

    public static void showAllRecettes(Statement statement) throws SQLException, Exception {
        if(statement == null)
            throw new Exception("Le statement ne peut pas être nul");
        
        String getAllRecettesQuery = "SELECT * FROM recette;";

        ResultSet resultSet = statement.executeQuery(getAllRecettesQuery);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("idRecette") + " - " + resultSet.getString("nom"));
        }
    }

    public static void showRecetteById(Statement statement, int id) throws SQLException, Exception {
        if(statement == null)
            throw new Exception("Le statement ne peut pas être nul");
        
        String getRecetteQuery = createShowRecetteByIdQuery(id);

        ResultSet resultSet = statement.executeQuery(getRecetteQuery);
        resultSet.first();

        System.out.println("Recette : " + resultSet.getString("nom"));
        System.out.println("Ingrédients nécessaires à la recette : ");

        do {
            System.out.println("\t " + resultSet.getString("nomIngredient"));
        } while (resultSet.next());
    }
    
    
    public static void ajouterIngredientToRecette(Statement statement, int idRecette, int idIngredient) throws SQLException {
        String addContientQuery = "INSERT INTO contient VALUES (" + idIngredient + ", " + idRecette + ");";
        statement.execute(addContientQuery);
    }
    
    public static void showAllIngredients(Statement statement) throws SQLException{
        String getAllIngredientsQuery = "SELECT * FROM ingredient;";

        ResultSet resultSet = statement.executeQuery(getAllIngredientsQuery);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("idIngredient") + " - " + resultSet.getString("nomIngredient"));
        }
    }
    
    public static void showIngredientsByIdRecette(Statement statement, int idRecette) throws SQLException{
        String showIngredientsQuery = "SELECT * FROM contient, ingredient WHERE contient.idIngredient = ingredient.idIngredient AND contient.idRecette=" + idRecette;

        ResultSet resultSet = statement.executeQuery(showIngredientsQuery);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("idIngredient") + " - " + resultSet.getString("nomIngredient"));
        }
    }
}
