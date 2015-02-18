/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import controller.Facade;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author johny_000
 */
public class JUnitTest {
    
        
        
    @BeforeClass
    public static void setUpClass() {
        System.out.println("==Setting up class");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("==Tearing down class");
    }
    
    @Before
    public void setUp() {
        System.out.println("=setup");
    }
    
    @After
    public void tearDown() {
        System.out.println("=teardown");
    }
    
   
    
    @Test
    public void TestJUnitTest() {
        
      /*  String username = "a";
        String password = "aa";
        String email = "a";
        String firstname = "a";
        String lastname = "a";
        String competence = "a";
        long startTime = 0;
        */
        String a = "a";
        String expected = "b";
        String result = Facade.test(a);
        assertEquals(expected, result);
             
    }
    
    @Test
    public void testAdminAuthorization(){
    
        String expected = "NOT-AUTHORIZED";
        String result = Facade.checkAuthorizationAdmin();
        
        assertEquals(expected, result);
    }

    
    
    
    
    
    
}

