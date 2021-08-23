package com.revature.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.sql.Date;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustInfoTest{

    private CustInfo ci;
    @BeforeEach
    public void setUp(){
        ci = new CustInfo();

    }

    @Test
    @Order(1)
    public void testSetID() {
        ci.setID(1);
    }

    @Test
    @Order(4)
    public void testSetfName() {
        ci.setfName("Test");
    }

    @Test
    @Order(5)
    public void testSetlName() {
        ci.setlName("Last");
    }

    @Test
    @Order(6)
    public void testSetBirthDate() {
        ci.setBirthDate("1999-05-11");
    }

    @Test
    @Order(7)
    public void testSetPhoneNum() {
        ci.setPhoneNum("123-456-7890");
    }

    @Test
    @Order(8)
    public void testSetEmail() {
        ci.setEmail("test@test.com");
    }

    @Test
    @Order(9)
    public void testGetID() {
        assertEquals(1, ci.getID());
    }

    @Test
    @Order(10)
    public void testGetUserName() {
        assertEquals("username", ci.getUserName());
    }

    @Test
    @Order(11)
    public void testGetPassword() {
        assertEquals("password", ci.getPassword());
    }

    @Test
    @Order(12)
    public void testGetfName() {
        assertEquals("Test", ci.getfName());
    }

    @Test
    @Order(13)
    public void testGetlName() {
        assertEquals("Last", ci.getlName());
    }

    @Test
    @Order(14)
    public void testGetBirthDate() {
        assertEquals(Date.valueOf("1999-05-11"), ci.getBirthDate());
    }

    @Test
    @Order(15)
    public void testGetPhoneNum() {
        assertEquals("123-456-7890", ci.getPhoneNum());
    }

    @Test
    @Order(16)
    public void testGetEmail() {
        assertEquals("test@test.com", ci.getEmail());
    }

    @Test
    @Order(17)
    public void testTestToString() {
    }

    @Test
    @Order(2)
    public void testSetUserName() {
        ci.setUserName("username");
    }

    @Test
    @Order(3)
    public void testSetPassword() {
        ci.setPassword("password");
    }
}