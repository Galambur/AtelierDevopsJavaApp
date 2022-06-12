/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gaell
 */
public class ApplicationTest {
    
    public ApplicationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of ajouterRecette method, of class Application.
     * @throws java.lang.Exception
     */
    @Test(expected = IllegalArgumentException .class)
    public void testAjouterRecetteWithNullStatement() throws Exception {
        System.out.println("ajouterRecette");
        Statement statement = null;
        String nomRecette = "";
        String description = "";
        Application.ajouterRecette(statement, nomRecette, description);
    }

    /**
     * Test of createAjouterRecetteQuery method, of class Application.
     */
    @Test
    public void testCreateAjouterRecetteQuery() {
        System.out.println("createAjouterRecetteQuery");
        String nomRecette = "nomRecetteTest";
        String description = "description test";
        String expResult = "INSERT INTO recette (nom, description) VALUES ('nomRecetteTest', 'description test');";
        String result = Application.createAjouterRecetteQuery(nomRecette, description);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of showRecetteById method, of class application
     * @throws java.lang.Exception
     */
    @Test(expected = Exception.class)
    public void testShowRecetteById() throws Exception{
        System.out.println("showRecetteById");
        Statement statement = null;
        int id = 0;
        Application.showRecetteById(statement, id);
    }

    /**
     * Test of createShowRecetteByIdQuery method, of class Application.
     */
    @Test
    public void testCreateShowRecetteByIdQuery() {
        System.out.println("createShowRecetteByIdQuery");
        int id = 5;
        String expResult = "SELECT * FROM recette, contient, ingredient "
                + "WHERE recette.idRecette = contient.idRecette "
                + "AND ingredient.idIngredient = contient.idIngredient "
                + "AND recette.idRecette = 5;";
        String result = Application.createShowRecetteByIdQuery(id);
        assertEquals(expResult, result);
    }
}
